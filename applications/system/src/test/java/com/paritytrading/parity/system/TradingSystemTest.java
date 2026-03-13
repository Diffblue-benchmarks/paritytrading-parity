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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.typesafe.config.Config;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class TradingSystemTest {

    @Test
    public void testMarketData() throws Exception {
        Config mockConfig = mock(Config.class);
        when(mockConfig.getString("market-data.session")).thenReturn("TEST_SESSION");
        when(mockConfig.getString("market-data.multicast-interface")).thenReturn("127.0.0.1");
        when(mockConfig.getString("market-data.multicast-group")).thenReturn("239.255.0.1");
        when(mockConfig.getInt("market-data.multicast-port")).thenReturn(5000);
        when(mockConfig.getString("market-data.request-address")).thenReturn("127.0.0.1");
        when(mockConfig.getInt("market-data.request-port")).thenReturn(5001);

        Method marketDataMethod = TradingSystem.class.getDeclaredMethod("marketData", Config.class);
        marketDataMethod.setAccessible(true);

        MarketData result = (MarketData) marketDataMethod.invoke(null, mockConfig);
        assertNotNull(result);
    }

    @Test
    public void testMarketReporting() throws Exception {
        Config mockConfig = mock(Config.class);
        when(mockConfig.getString("market-report.session")).thenReturn("TEST_SESSION");
        when(mockConfig.getString("market-report.multicast-interface")).thenReturn("127.0.0.1");
        when(mockConfig.getString("market-report.multicast-group")).thenReturn("239.255.0.1");
        when(mockConfig.getInt("market-report.multicast-port")).thenReturn(6000);
        when(mockConfig.getString("market-report.request-address")).thenReturn("127.0.0.1");
        when(mockConfig.getInt("market-report.request-port")).thenReturn(6001);

        Method marketReportingMethod = TradingSystem.class.getDeclaredMethod("marketReporting", Config.class);
        marketReportingMethod.setAccessible(true);

        MarketReporting result = (MarketReporting) marketReportingMethod.invoke(null, mockConfig);
        assertNotNull(result);
    }

    @Test
    public void testOrderEntry() throws Exception {
        Config mockConfig = mock(Config.class);
        when(mockConfig.getString("order-entry.address")).thenReturn("127.0.0.1");
        when(mockConfig.getInt("order-entry.port")).thenReturn(7000);

        OrderBooks mockBooks = mock(OrderBooks.class);

        Method orderEntryMethod = TradingSystem.class.getDeclaredMethod("orderEntry", Config.class, OrderBooks.class);
        orderEntryMethod.setAccessible(true);

        OrderEntry result = (OrderEntry) orderEntryMethod.invoke(null, mockConfig, mockBooks);
        assertNotNull(result);
    }
}
