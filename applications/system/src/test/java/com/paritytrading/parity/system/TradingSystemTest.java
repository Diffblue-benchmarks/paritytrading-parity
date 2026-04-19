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
package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.jvirtanen.config.Configs;
import org.jvirtanen.util.Applications;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

class TradingSystemTest {

    @Test
    void epochMillisIsToday() {
        long expected = LocalDate.now().atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli();

        assertEquals(expected, TradingSystem.EPOCH_MILLIS);
    }

    @Test
    void mainWithNoArgs() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> TradingSystem.main(new String[0]));

            apps.verify(() -> Applications.usage("parity-system <configuration-file>"));
        }
    }

    @Test
    void mainWithConfigException() throws IOException {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            ConfigException exception = new ConfigException.Missing("test");
            apps.when(() -> Applications.config("test.conf")).thenThrow(exception);

            TradingSystem.main(new String[]{"test.conf"});

            apps.verify(() -> Applications.error(exception));
        }
    }

    @Test
    void mainWithFileNotFoundException() throws IOException {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            FileNotFoundException exception = new FileNotFoundException("not found");
            apps.when(() -> Applications.config("missing.conf")).thenThrow(exception);

            TradingSystem.main(new String[]{"missing.conf"});

            apps.verify(() -> Applications.error(exception));
        }
    }

    @Test
    void mainWithValidConfig() throws IOException {
        Config config = mock(Config.class);
        MarketData marketData = mock(MarketData.class);
        MarketReporting marketReporting = mock(MarketReporting.class);
        OrderEntry orderEntry = mock(OrderEntry.class);

        InetAddress address = InetAddress.getLoopbackAddress();

        when(config.getString("market-data.session")).thenReturn("session1");
        when(config.getString("market-report.session")).thenReturn("session2");
        when(config.getStringList("instruments")).thenReturn(Arrays.asList("AAPL"));

        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<Configs> cfgs = mockStatic(Configs.class);
             MockedStatic<MarketData> md = mockStatic(MarketData.class);
             MockedStatic<MarketReporting> mr = mockStatic(MarketReporting.class);
             MockedStatic<OrderEntry> oe = mockStatic(OrderEntry.class);
             MockedConstruction<Events> events = mockConstruction(Events.class)) {

            apps.when(() -> Applications.config("system.conf")).thenReturn(config);

            cfgs.when(() -> Configs.getNetworkInterface(eq(config), anyString())).thenReturn(null);
            cfgs.when(() -> Configs.getInetAddress(eq(config), anyString())).thenReturn(address);
            cfgs.when(() -> Configs.getPort(eq(config), anyString())).thenReturn(1234);

            md.when(() -> MarketData.open(anyString(), any(), any(InetSocketAddress.class), any(InetSocketAddress.class)))
                .thenReturn(marketData);
            mr.when(() -> MarketReporting.open(anyString(), any(), any(InetSocketAddress.class), any(InetSocketAddress.class)))
                .thenReturn(marketReporting);
            oe.when(() -> OrderEntry.open(any(InetSocketAddress.class), any(OrderBooks.class)))
                .thenReturn(orderEntry);

            TradingSystem.main(new String[]{"system.conf"});

            verify(marketData).version();
            verify(marketReporting).version();
            assertEquals(1, events.constructed().size());

            md.verify(() -> MarketData.open(eq("session1"), isNull(),
                any(InetSocketAddress.class), any(InetSocketAddress.class)));
            mr.verify(() -> MarketReporting.open(eq("session2"), isNull(),
                any(InetSocketAddress.class), any(InetSocketAddress.class)));
            oe.verify(() -> OrderEntry.open(any(InetSocketAddress.class), any(OrderBooks.class)));
        }
    }
}
