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
package com.paritytrading.parity.ticker;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.paritytrading.nassau.util.BinaryFILE;
import com.paritytrading.nassau.util.MoldUDP64;
import com.paritytrading.nassau.util.SoupBinTCP;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import org.junit.jupiter.api.Test;
import org.jvirtanen.config.Configs;
import org.jvirtanen.util.Applications;
import org.mockito.MockedStatic;

class StockTickerTest {

    private static final String INSTRUMENTS_CONFIG =
            "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 7\n" +
            "  AAPL {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}\n";

    private static final String TCP_CONFIG = INSTRUMENTS_CONFIG +
            "market-data {\n" +
            "  address = 127.0.0.1\n" +
            "  port = 5000\n" +
            "  username = parity\n" +
            "  password = parity\n" +
            "}\n";

    private static final String UDP_CONFIG = INSTRUMENTS_CONFIG +
            "market-data {\n" +
            "  multicast-interface = 127.0.0.1\n" +
            "  multicast-group = 224.0.0.1\n" +
            "  multicast-port = 5000\n" +
            "  request-address = 127.0.0.1\n" +
            "  request-port = 5001\n" +
            "}\n";

    @Test
    void readWithDisplayFormat() {
        Config config = ConfigFactory.parseString(TCP_CONFIG);

        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<BinaryFILE> binaryFile = mockStatic(BinaryFILE.class)) {

            apps.when(() -> Applications.config(anyString())).thenReturn(config);

            StockTicker.main(new String[]{"test.conf", "test.dat"});

            binaryFile.verify(() -> BinaryFILE.read(any(File.class), any()));
        }
    }

    @Test
    void readWithTaqFormat() {
        Config config = ConfigFactory.parseString(TCP_CONFIG);

        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<BinaryFILE> binaryFile = mockStatic(BinaryFILE.class)) {

            apps.when(() -> Applications.config(anyString())).thenReturn(config);

            StockTicker.main(new String[]{"-t", "test.conf", "test.dat"});

            binaryFile.verify(() -> BinaryFILE.read(any(File.class), any()));
        }
    }

    @Test
    void listenWithSoupBinTcp() {
        Config config = ConfigFactory.parseString(TCP_CONFIG);

        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<SoupBinTCP> soupBinTcp = mockStatic(SoupBinTCP.class)) {

            apps.when(() -> Applications.config(anyString())).thenReturn(config);

            StockTicker.main(new String[]{"test.conf"});

            soupBinTcp.verify(() -> SoupBinTCP.receive(
                    any(InetSocketAddress.class), anyString(), anyString(), any()));
        }
    }

    @Test
    void listenWithTaqFormatAndSoupBinTcp() {
        Config config = ConfigFactory.parseString(TCP_CONFIG);

        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<SoupBinTCP> soupBinTcp = mockStatic(SoupBinTCP.class)) {

            apps.when(() -> Applications.config(anyString())).thenReturn(config);

            StockTicker.main(new String[]{"-t", "test.conf"});

            soupBinTcp.verify(() -> SoupBinTCP.receive(
                    any(InetSocketAddress.class), anyString(), anyString(), any()));
        }
    }

    @Test
    void listenWithMoldUdp64() throws Exception {
        Config config = ConfigFactory.parseString(UDP_CONFIG);

        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<MoldUDP64> moldUdp64 = mockStatic(MoldUDP64.class);
             MockedStatic<Configs> configs = mockStatic(Configs.class)) {

            apps.when(() -> Applications.config(anyString())).thenReturn(config);
            configs.when(() -> Configs.getNetworkInterface(any(), anyString()))
                    .thenReturn(NetworkInterface.getByName("lo"));
            configs.when(() -> Configs.getInetAddress(any(), anyString()))
                    .thenReturn(InetAddress.getLoopbackAddress());
            configs.when(() -> Configs.getPort(any(), anyString()))
                    .thenReturn(5000);

            StockTicker.main(new String[]{"test.conf"});

            moldUdp64.verify(() -> MoldUDP64.receive(
                    any(), any(InetSocketAddress.class), any(InetSocketAddress.class), any()));
        }
    }

    @Test
    void fileNotFoundCallsError() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            apps.when(() -> Applications.config(anyString()))
                    .thenThrow(new FileNotFoundException("test.conf"));

            StockTicker.main(new String[]{"test.conf"});

            apps.verify(() -> Applications.error(any(Throwable.class)));
        }
    }

    @Test
    void ioExceptionCallsFatal() {
        Config config = ConfigFactory.parseString(TCP_CONFIG);

        try (MockedStatic<Applications> apps = mockStatic(Applications.class);
             MockedStatic<BinaryFILE> binaryFile = mockStatic(BinaryFILE.class)) {

            apps.when(() -> Applications.config(anyString())).thenReturn(config);
            binaryFile.when(() -> BinaryFILE.read(any(File.class), any()))
                    .thenThrow(new IOException("test error"));

            StockTicker.main(new String[]{"test.conf", "test.dat"});

            apps.verify(() -> Applications.fatal(any(Throwable.class)));
        }
    }

    @Test
    void configExceptionCallsError() {
        try (MockedStatic<Applications> apps = mockStatic(Applications.class)) {
            apps.when(() -> Applications.config(anyString()))
                    .thenThrow(new ConfigException.Missing("test"));

            StockTicker.main(new String[]{"test.conf"});

            apps.verify(() -> Applications.error(any(Throwable.class)));
        }
    }
}
