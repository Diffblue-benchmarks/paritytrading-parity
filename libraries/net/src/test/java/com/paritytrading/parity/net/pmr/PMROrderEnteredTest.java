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

class PMROrderEnteredTest {

    private PMR.OrderEntered message;

    @BeforeEach
    void setUp() {
        message = new PMR.OrderEntered();
    }

    @Test
    void getFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(123456789L);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.put(PMR.BUY);
        buffer.putLong(300L);
        buffer.putLong(1000L);
        buffer.putLong(5000L);
        buffer.flip();

        message.get(buffer);

        assertEquals(123456789L, message.timestamp);
        assertEquals(100L,       message.username);
        assertEquals(200L,       message.orderNumber);
        assertEquals(PMR.BUY,    message.side);
        assertEquals(300L,       message.instrument);
        assertEquals(1000L,      message.quantity);
        assertEquals(5000L,      message.price);
    }

    @Test
    void putToBuffer() {
        message.timestamp   = 123456789L;
        message.username    = 100L;
        message.orderNumber = 200L;
        message.side        = PMR.SELL;
        message.instrument  = 300L;
        message.quantity    = 1000L;
        message.price       = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        message.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(123456789L, buffer.getLong());
        assertEquals(100L,       buffer.getLong());
        assertEquals(200L,       buffer.getLong());
        assertEquals(PMR.SELL,   buffer.get());
        assertEquals(300L,       buffer.getLong());
        assertEquals(1000L,      buffer.getLong());
        assertEquals(5000L,      buffer.getLong());
    }

    @Test
    void roundTrip() {
        message.timestamp   = 999L;
        message.username    = 111L;
        message.orderNumber = 222L;
        message.side        = PMR.BUY;
        message.instrument  = 333L;
        message.quantity    = 444L;
        message.price       = 555L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        message.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.OrderEntered restored = new PMR.OrderEntered();
        restored.get(buffer);

        assertEquals(message.timestamp,   restored.timestamp);
        assertEquals(message.username,    restored.username);
        assertEquals(message.orderNumber, restored.orderNumber);
        assertEquals(message.side,        restored.side);
        assertEquals(message.instrument,  restored.instrument);
        assertEquals(message.quantity,    restored.quantity);
        assertEquals(message.price,       restored.price);
    }
}
