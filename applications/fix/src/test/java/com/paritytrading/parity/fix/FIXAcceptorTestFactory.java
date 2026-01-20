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
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Factory class for creating FIXAcceptor instances for testing.
 */
public class FIXAcceptorTestFactory {

    /**
     * Creates a valid FIXAcceptor instance for testing using reflection.
     * Uses a real ServerSocketChannel bound to localhost to support selector operations.
     */
    @InterestingTestFactory
    public static FIXAcceptor createFIXAcceptor() throws Exception {
        // Create a test OrderEntryFactory with localhost address
        InetSocketAddress orderEntryAddress = new InetSocketAddress("127.0.0.1", 12345);
        OrderEntryFactory orderEntry = new OrderEntryFactory(orderEntryAddress);

        // Create a test Instruments configuration using Config
        String configStr =
            "instruments {\n" +
            "  TEST {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 2\n" +
            "  }\n" +
            "}";
        Config config = ConfigFactory.parseString(configStr);
        Instruments instrumentsConfig = Instruments.fromConfig(config, "instruments");

        // Create a real ServerSocketChannel bound to localhost with an ephemeral port
        // This is needed to support selector registration
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("127.0.0.1", 0));
        serverChannel.configureBlocking(false);

        // Use reflection to access the private constructor
        Constructor<FIXAcceptor> constructor = FIXAcceptor.class.getDeclaredConstructor(
            OrderEntryFactory.class,
            ServerSocketChannel.class,
            String.class,
            Instruments.class
        );
        constructor.setAccessible(true);

        // Create the FIXAcceptor using reflection
        return constructor.newInstance(orderEntry, serverChannel, "TEST_SENDER", instrumentsConfig);
    }
}
