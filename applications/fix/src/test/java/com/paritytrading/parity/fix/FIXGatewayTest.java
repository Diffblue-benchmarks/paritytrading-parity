/*
 * Copyright 2014 Parity authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.junit.jupiter.api.Test;
import org.jvirtanen.config.Configs;
import org.jvirtanen.util.Applications;
import org.mockito.MockedStatic;

class FIXGatewayTest {

    @Test
    void mainWithNoArguments() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            apps.when(() -> Applications.usage(anyString())).thenThrow(new RuntimeException());

            assertThrows(RuntimeException.class, () -> FIXGateway.main(new String[]{}));

            apps.verify(() -> Applications.usage("parity-fix <configuration-file>"));
        }
    }

    @Test
    void mainWithTooManyArguments() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            apps.when(() -> Applications.usage(anyString())).thenThrow(new RuntimeException());

            assertThrows(RuntimeException.class, () -> FIXGateway.main(new String[]{"a", "b"}));

            apps.verify(() -> Applications.usage("parity-fix <configuration-file>"));
        }
    }

    @Test
    void mainWithFileNotFoundException() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            FileNotFoundException exception = new FileNotFoundException("not found");
            apps.when(() -> Applications.config("missing.conf")).thenThrow(exception);

            FIXGateway.main(new String[]{"missing.conf"});

            apps.verify(() -> Applications.error(exception));
        }
    }

    @Test
    void mainWithConfigException() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            ConfigException exception = new ConfigException.Missing("missing.key");
            apps.when(() -> Applications.config("bad.conf")).thenThrow(exception);

            FIXGateway.main(new String[]{"bad.conf"});

            apps.verify(() -> Applications.error(exception));
        }
    }

    @Test
    void mainWithIOException() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<Configs> configs = mockStatic(Configs.class);
             MockedStatic<FIXAcceptor> acceptor = mockStatic(FIXAcceptor.class);
             MockedStatic<Instruments> instr = mockStatic(Instruments.class)) {

            Config config = mock(Config.class);
            apps.when(() -> Applications.config("test.conf")).thenReturn(config);

            InetAddress address = InetAddress.getLoopbackAddress();
            configs.when(() -> Configs.getInetAddress(config, "order-entry.address")).thenReturn(address);
            configs.when(() -> Configs.getPort(config, "order-entry.port")).thenReturn(1234);
            configs.when(() -> Configs.getInetAddress(config, "fix.address")).thenReturn(address);
            configs.when(() -> Configs.getPort(config, "fix.port")).thenReturn(5678);
            when(config.getString("fix.sender-comp-id")).thenReturn("SENDER");

            Instruments instruments = mock(Instruments.class);
            instr.when(() -> Instruments.fromConfig(config, "instruments")).thenReturn(instruments);

            IOException ioException = new IOException("connection failed");
            acceptor.when(() -> FIXAcceptor.open(any(), any(), anyString(), any())).thenThrow(ioException);

            FIXGateway.main(new String[]{"test.conf"});

            apps.verify(() -> Applications.fatal(ioException));
        }
    }

    @Test
    void mainWithValidConfig() throws Exception {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<Configs> configs = mockStatic(Configs.class);
             MockedStatic<FIXAcceptor> acceptor = mockStatic(FIXAcceptor.class);
             MockedStatic<Events> events = mockStatic(Events.class);
             MockedStatic<Instruments> instr = mockStatic(Instruments.class)) {

            Config config = mock(Config.class);
            apps.when(() -> Applications.config("test.conf")).thenReturn(config);

            InetAddress address = InetAddress.getLoopbackAddress();
            configs.when(() -> Configs.getInetAddress(config, "order-entry.address")).thenReturn(address);
            configs.when(() -> Configs.getPort(config, "order-entry.port")).thenReturn(1234);
            configs.when(() -> Configs.getInetAddress(config, "fix.address")).thenReturn(address);
            configs.when(() -> Configs.getPort(config, "fix.port")).thenReturn(5678);
            when(config.getString("fix.sender-comp-id")).thenReturn("SENDER");

            Instruments instruments = mock(Instruments.class);
            instr.when(() -> Instruments.fromConfig(config, "instruments")).thenReturn(instruments);

            FIXAcceptor mockAcceptor = mock(FIXAcceptor.class);
            acceptor.when(() -> FIXAcceptor.open(any(), any(), anyString(), any())).thenReturn(mockAcceptor);

            FIXGateway.main(new String[]{"test.conf"});

            acceptor.verify(() -> FIXAcceptor.open(
                    any(OrderEntryFactory.class),
                    eq(new InetSocketAddress(address, 5678)),
                    eq("SENDER"),
                    eq(instruments)));

            events.verify(() -> Events.process(mockAcceptor));
        }
    }
}
