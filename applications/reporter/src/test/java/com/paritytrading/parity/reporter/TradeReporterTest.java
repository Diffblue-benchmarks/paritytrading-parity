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
package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.paritytrading.nassau.MessageListener;
import com.paritytrading.nassau.util.MoldUDP64;
import com.paritytrading.nassau.util.SoupBinTCP;
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.jvirtanen.config.Configs;
import org.jvirtanen.util.Applications;
import org.mockito.MockedStatic;

class TradeReporterTest {

    @Test
    void noArgsCallsUsage() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            apps.when(() -> Applications.usage(anyString()))
                .thenThrow(new RuntimeException("exit"));

            assertThrows(RuntimeException.class, () -> TradeReporter.main(new String[]{}));

            apps.verify(() -> Applications.usage(anyString()));
        }
    }

    @Test
    void tooManyArgsCallsUsage() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            apps.when(() -> Applications.usage(anyString()))
                .thenThrow(new RuntimeException("exit"));

            assertThrows(RuntimeException.class,
                () -> TradeReporter.main(new String[]{"a", "b", "c"}));

            apps.verify(() -> Applications.usage(anyString()));
        }
    }

    @Test
    void invalidFlagCallsUsage() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            apps.when(() -> Applications.usage(anyString()))
                .thenThrow(new RuntimeException("exit"));

            assertThrows(RuntimeException.class,
                () -> TradeReporter.main(new String[]{"-x", "config.conf"}));

            apps.verify(() -> Applications.usage(anyString()));
        }
    }

    @Test
    void configExceptionCallsError() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            ConfigException exception = new ConfigException.Missing("test.path");
            apps.when(() -> Applications.config(anyString()))
                .thenThrow(exception);

            TradeReporter.main(new String[]{"config.conf"});

            apps.verify(() -> Applications.error(exception));
        }
    }

    @Test
    void fileNotFoundCallsError() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            FileNotFoundException exception = new FileNotFoundException("not found");
            apps.when(() -> Applications.config(anyString()))
                .thenThrow(exception);

            TradeReporter.main(new String[]{"config.conf"});

            apps.verify(() -> Applications.error(exception));
        }
    }

    @Test
    void soupBinTcpPath() throws Exception {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<Instruments> instr = mockStatic(Instruments.class);
             MockedStatic<Configs> cfgs = mockStatic(Configs.class);
             MockedStatic<SoupBinTCP> soup = mockStatic(SoupBinTCP.class)) {

            Config config = mock(Config.class);
            apps.when(() -> Applications.config("config.conf"))
                .thenReturn(config);

            Instruments instruments = mock(Instruments.class);
            when(instruments.getPriceWidth()).thenReturn(10);
            when(instruments.getSizeWidth()).thenReturn(10);
            when(instruments.iterator()).thenReturn(Collections.emptyIterator());
            instr.when(() -> Instruments.fromConfig(config, "instruments"))
                .thenReturn(instruments);

            when(config.hasPath("trade-report.multicast-interface")).thenReturn(false);

            InetAddress address = InetAddress.getLoopbackAddress();
            cfgs.when(() -> Configs.getInetAddress(config, "trade-report.address"))
                .thenReturn(address);
            cfgs.when(() -> Configs.getPort(config, "trade-report.port"))
                .thenReturn(1234);
            when(config.getString("trade-report.username")).thenReturn("user");
            when(config.getString("trade-report.password")).thenReturn("pass");

            TradeReporter.main(new String[]{"config.conf"});

            soup.verify(() -> SoupBinTCP.receive(
                eq(new InetSocketAddress(address, 1234)),
                eq("user"), eq("pass"), any(MessageListener.class)));
        }
    }

    @Test
    void moldUdp64Path() throws Exception {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<Instruments> instr = mockStatic(Instruments.class);
             MockedStatic<Configs> cfgs = mockStatic(Configs.class);
             MockedStatic<MoldUDP64> mold = mockStatic(MoldUDP64.class)) {

            Config config = mock(Config.class);
            apps.when(() -> Applications.config("config.conf"))
                .thenReturn(config);

            Instruments instruments = mock(Instruments.class);
            when(instruments.getPriceWidth()).thenReturn(10);
            when(instruments.getSizeWidth()).thenReturn(10);
            when(instruments.iterator()).thenReturn(Collections.emptyIterator());
            instr.when(() -> Instruments.fromConfig(config, "instruments"))
                .thenReturn(instruments);

            when(config.hasPath("trade-report.multicast-interface")).thenReturn(true);

            InetAddress multicastGroup = InetAddress.getByName("224.0.0.1");
            InetAddress requestAddress = InetAddress.getLoopbackAddress();
            cfgs.when(() -> Configs.getNetworkInterface(eq(config), eq("trade-report.multicast-interface")))
                .thenReturn(null);
            cfgs.when(() -> Configs.getInetAddress(config, "trade-report.multicast-group"))
                .thenReturn(multicastGroup);
            cfgs.when(() -> Configs.getPort(config, "trade-report.multicast-port"))
                .thenReturn(5000);
            cfgs.when(() -> Configs.getInetAddress(config, "trade-report.request-address"))
                .thenReturn(requestAddress);
            cfgs.when(() -> Configs.getPort(config, "trade-report.request-port"))
                .thenReturn(5001);

            TradeReporter.main(new String[]{"config.conf"});

            mold.verify(() -> MoldUDP64.receive(
                isNull(),
                eq(new InetSocketAddress(multicastGroup, 5000)),
                eq(new InetSocketAddress(requestAddress, 5001)),
                any(MessageListener.class)));
        }
    }

    @Test
    void tsvFlagUsesTsvFormat() throws Exception {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<Instruments> instr = mockStatic(Instruments.class);
             MockedStatic<Configs> cfgs = mockStatic(Configs.class);
             MockedStatic<SoupBinTCP> soup = mockStatic(SoupBinTCP.class)) {

            Config config = mock(Config.class);
            apps.when(() -> Applications.config("config.conf"))
                .thenReturn(config);

            Instruments instruments = mock(Instruments.class);
            when(instruments.getPriceWidth()).thenReturn(10);
            when(instruments.getSizeWidth()).thenReturn(10);
            when(instruments.iterator()).thenReturn(Collections.emptyIterator());
            instr.when(() -> Instruments.fromConfig(config, "instruments"))
                .thenReturn(instruments);

            when(config.hasPath("trade-report.multicast-interface")).thenReturn(false);

            InetAddress address = InetAddress.getLoopbackAddress();
            cfgs.when(() -> Configs.getInetAddress(config, "trade-report.address"))
                .thenReturn(address);
            cfgs.when(() -> Configs.getPort(config, "trade-report.port"))
                .thenReturn(1234);
            when(config.getString("trade-report.username")).thenReturn("user");
            when(config.getString("trade-report.password")).thenReturn("pass");

            TradeReporter.main(new String[]{"-t", "config.conf"});

            soup.verify(() -> SoupBinTCP.receive(
                any(InetSocketAddress.class),
                anyString(), anyString(), any(MessageListener.class)));
        }
    }

    @Test
    void ioExceptionCallsFatal() throws Exception {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<Instruments> instr = mockStatic(Instruments.class);
             MockedStatic<Configs> cfgs = mockStatic(Configs.class);
             MockedStatic<SoupBinTCP> soup = mockStatic(SoupBinTCP.class)) {

            Config config = mock(Config.class);
            apps.when(() -> Applications.config("config.conf"))
                .thenReturn(config);

            Instruments instruments = mock(Instruments.class);
            when(instruments.getPriceWidth()).thenReturn(10);
            when(instruments.getSizeWidth()).thenReturn(10);
            when(instruments.iterator()).thenReturn(Collections.emptyIterator());
            instr.when(() -> Instruments.fromConfig(config, "instruments"))
                .thenReturn(instruments);

            when(config.hasPath("trade-report.multicast-interface")).thenReturn(false);

            InetAddress address = InetAddress.getLoopbackAddress();
            cfgs.when(() -> Configs.getInetAddress(config, "trade-report.address"))
                .thenReturn(address);
            cfgs.when(() -> Configs.getPort(config, "trade-report.port"))
                .thenReturn(1234);
            when(config.getString("trade-report.username")).thenReturn("user");
            when(config.getString("trade-report.password")).thenReturn("pass");

            IOException exception = new IOException("Connection refused");
            soup.when(() -> SoupBinTCP.receive(
                any(InetSocketAddress.class),
                anyString(), anyString(), any(MessageListener.class)))
                .thenThrow(exception);

            TradeReporter.main(new String[]{"config.conf"});

            apps.verify(() -> Applications.fatal(exception));
        }
    }
}
