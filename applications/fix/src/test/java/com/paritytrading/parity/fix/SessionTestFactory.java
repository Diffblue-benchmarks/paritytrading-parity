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

import com.diffblue.cover.annotations.InterestingTestFactory;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import com.paritytrading.parity.net.poe.POEClientParser;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.philadelphia.FIXConfig;
import com.paritytrading.philadelphia.FIXVersion;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Factory class for creating Session instances for testing.
 */
public class SessionTestFactory {

    /**
     * Creates a valid Session instance for testing using a stubbed OrderEntryFactory.
     * Avoids actual network operations by using loopback connections.
     */
    @InterestingTestFactory
    public static Session createSession() throws Exception {
        // Create a server socket for the order entry connection
        ServerSocketChannel orderEntryServer = ServerSocketChannel.open();
        orderEntryServer.bind(new InetSocketAddress("127.0.0.1", 0));

        // Create a stubbed OrderEntryFactory that connects to our local server
        OrderEntryFactory stubbedOrderEntryFactory = new OrderEntryFactory(
            (InetSocketAddress) orderEntryServer.getLocalAddress()
        ) {
            @Override
            SoupBinTCPClient create(POEClientListener listener,
                                   SoupBinTCPClientStatusListener statusListener) throws IOException {
                // Create a real connection to the local server
                SocketChannel channel = SocketChannel.open();
                channel.connect(getAddress());
                channel.configureBlocking(false);

                // Accept the connection on the server side
                SocketChannel serverSide = orderEntryServer.accept();
                serverSide.configureBlocking(false);

                // Return a real SoupBinTCPClient connected to our local server
                return new SoupBinTCPClient(channel, POE.MAX_OUTBOUND_MESSAGE_LENGTH,
                        new POEClientParser(listener), statusListener);
            }
        };

        // Create a real SocketChannel pair for the FIX connection
        ServerSocketChannel fixServer = ServerSocketChannel.open();
        fixServer.bind(new InetSocketAddress("127.0.0.1", 0));

        SocketChannel fixClientChannel = SocketChannel.open();
        fixClientChannel.connect(fixServer.getLocalAddress());
        fixClientChannel.configureBlocking(false);

        SocketChannel fixServerChannel = fixServer.accept();
        fixServerChannel.configureBlocking(false);

        fixServer.close();

        // Create a FIXConfig
        FIXConfig config = new FIXConfig.Builder()
            .setVersion(FIXVersion.FIX_4_4)
            .setSenderCompID("TEST_SENDER")
            .setTargetCompID("TEST_TARGET")
            .build();

        // Create a test Instruments configuration using Config
        String configStr =
            "instruments {\n" +
            "  TEST {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 2\n" +
            "  }\n" +
            "}";
        Config instrumentsConfigObj = ConfigFactory.parseString(configStr);
        Instruments instruments = Instruments.fromConfig(instrumentsConfigObj, "instruments");

        // Create the Session - this will now use the stubbed OrderEntryFactory
        return new Session(stubbedOrderEntryFactory, fixClientChannel, config, instruments);
    }
}
