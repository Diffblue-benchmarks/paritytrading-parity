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
import java.net.InetSocketAddress;

/**
 * Factory class for creating InetSocketAddress instances for testing.
 */
public class InetSocketAddressTestFactory {

    /**
     * Creates a resolved InetSocketAddress instance for testing.
     * Uses localhost which is always resolved to avoid UnresolvedAddressException.
     */
    @InterestingTestFactory
    public static InetSocketAddress createInetSocketAddress() {
        // Use 127.0.0.1 (localhost) which is always resolved
        // Using port 0 allows the system to choose an available ephemeral port
        return new InetSocketAddress("127.0.0.1", 0);
    }
}
