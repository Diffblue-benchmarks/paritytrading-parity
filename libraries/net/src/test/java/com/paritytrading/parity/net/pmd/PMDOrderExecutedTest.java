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

class PMDOrderExecutedTest {

    private PMD.OrderExecuted message;

    @BeforeEach
    void setUp() {
        message = new PMD.OrderExecuted();
    }

    @Test
    void get() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(123456789L);
        buffer.putLong(1001L);
        buffer.putLong(500L);
        buffer.putInt(42);
        buffer.flip();

        message.get(buffer);

        assertEquals(123456789L, message.timestamp);
        assertEquals(1001L, message.orderNumber);
        assertEquals(500L, message.quantity);
        assertEquals(42L, message.matchNumber);
    }

    @Test
    void put() {
        message.timestamp   = 123456789L;
        message.orderNumber = 1001L;
        message.quantity    = 500L;
        message.matchNumber = 42L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        message.put(buffer);
        buffer.flip();

        assertEquals('E', buffer.get());
        assertEquals(123456789L, buffer.getLong());
        assertEquals(1001L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(42, buffer.getInt());
    }

    @Test
    void roundTrip() {
        message.timestamp   = 987654321L;
        message.orderNumber = 2002L;
        message.quantity    = 750L;
        message.matchNumber = 99L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        message.put(buffer);
        buffer.flip();

        buffer.get();

        PMD.OrderExecuted restored = new PMD.OrderExecuted();
        restored.get(buffer);

        assertEquals(message.timestamp, restored.timestamp);
        assertEquals(message.orderNumber, restored.orderNumber);
        assertEquals(message.quantity, restored.quantity);
        assertEquals(message.matchNumber, restored.matchNumber);
    }

    @Test
    void getUnsignedInt() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putInt(0xFFFFFFFF);
        buffer.flip();

        message.get(buffer);

        assertEquals(0xFFFFFFFFL, message.matchNumber);
    }

    @Test
    void putUnsignedInt() {
        message.timestamp   = 1L;
        message.orderNumber = 2L;
        message.quantity    = 3L;
        message.matchNumber = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        message.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();

        assertEquals(0xFFFFFFFFL, Integer.toUnsignedLong(buffer.getInt()));
    }
}
