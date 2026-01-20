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
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Factory class for creating OrderEntryFactory instances for testing.
 */
public class OrderEntryFactoryTestFactory {

    /**
     * Creates a valid OrderEntryFactory instance for testing with a resolved address.
     * Uses a simple InetSocketAddress without performing any actual network operations.
     */
    @InterestingTestFactory
    public static OrderEntryFactory createOrderEntryFactory() {
        // Create an InetSocketAddress with a resolved address (localhost is always resolved)
        // This avoids any actual network operations that would violate sandboxing policy
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 12345);
        return new OrderEntryFactory(address);
    }
}
