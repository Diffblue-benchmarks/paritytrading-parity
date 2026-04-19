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
package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PMDOrderAddedTest {

    private PMD.OrderAdded message;

    @BeforeEach
    void setUp() {
        message = new PMD.OrderAdded();
    }

    @Test
    void get() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.putLong(123456789L);
        buffer.putLong(1001L);
        buffer.put(PMD.BUY);
        buffer.putLong(2002L);
        buffer.putLong(500L);
        buffer.putLong(1050L);
        buffer.flip();

        message.get(buffer);

        assertEquals(123456789L, message.timestamp);
        assertEquals(1001L, message.orderNumber);
        assertEquals(PMD.BUY, message.side);
        assertEquals(2002L, message.instrument);
        assertEquals(500L, message.quantity);
        assertEquals(1050L, message.price);
    }

    @Test
    void put() {
        message.timestamp   = 123456789L;
        message.orderNumber = 1001L;
        message.side        = PMD.SELL;
        message.instrument  = 2002L;
        message.quantity    = 500L;
        message.price       = 1050L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        message.put(buffer);
        buffer.flip();

        assertEquals('A', buffer.get());
        assertEquals(123456789L, buffer.getLong());
        assertEquals(1001L, buffer.getLong());
        assertEquals(PMD.SELL, buffer.get());
        assertEquals(2002L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(1050L, buffer.getLong());
    }

    @Test
    void getRoundTrip() {
        message.timestamp   = 987654321L;
        message.orderNumber = 5005L;
        message.side        = PMD.BUY;
        message.instrument  = 3003L;
        message.quantity    = 250L;
        message.price       = 7500L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        message.put(buffer);
        buffer.flip();

        buffer.get();

        PMD.OrderAdded decoded = new PMD.OrderAdded();
        decoded.get(buffer);

        assertEquals(message.timestamp, decoded.timestamp);
        assertEquals(message.orderNumber, decoded.orderNumber);
        assertEquals(message.side, decoded.side);
        assertEquals(message.instrument, decoded.instrument);
        assertEquals(message.quantity, decoded.quantity);
        assertEquals(message.price, decoded.price);
    }
}
