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
package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PMROrderAddedTest {

    private PMR.OrderAdded message;

    @BeforeEach
    void setUp() {
        message = new PMR.OrderAdded();
    }

    @Test
    void get() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(123456789L);
        buffer.putLong(987654321L);
        buffer.flip();

        message.get(buffer);

        assertEquals(123456789L, message.timestamp);
        assertEquals(987654321L, message.orderNumber);
    }

    @Test
    void put() {
        message.timestamp   = 123456789L;
        message.orderNumber = 987654321L;

        ByteBuffer buffer = ByteBuffer.allocate(17);
        message.put(buffer);
        buffer.flip();

        assertEquals('A', buffer.get());
        assertEquals(123456789L, buffer.getLong());
        assertEquals(987654321L, buffer.getLong());
    }

    @Test
    void roundTrip() {
        message.timestamp   = Long.MAX_VALUE;
        message.orderNumber = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(17);
        message.put(buffer);
        buffer.flip();

        // skip message type byte
        buffer.get();

        PMR.OrderAdded decoded = new PMR.OrderAdded();
        decoded.get(buffer);

        assertEquals(message.timestamp, decoded.timestamp);
        assertEquals(message.orderNumber, decoded.orderNumber);
    }
}
