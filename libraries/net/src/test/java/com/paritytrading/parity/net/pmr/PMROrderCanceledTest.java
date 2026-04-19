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

class PMROrderCanceledTest {

    private PMR.OrderCanceled message;

    @BeforeEach
    void setUp() {
        message = new PMR.OrderCanceled();
    }

    @Test
    void getFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(123456789L);
        buffer.putLong(200L);
        buffer.putLong(500L);
        buffer.flip();

        message.get(buffer);

        assertEquals(123456789L, message.timestamp);
        assertEquals(200L,       message.orderNumber);
        assertEquals(500L,       message.canceledQuantity);
    }

    @Test
    void putToBuffer() {
        message.timestamp        = 123456789L;
        message.orderNumber      = 200L;
        message.canceledQuantity = 500L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        message.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(123456789L, buffer.getLong());
        assertEquals(200L,       buffer.getLong());
        assertEquals(500L,       buffer.getLong());
    }

    @Test
    void roundTrip() {
        message.timestamp        = 999L;
        message.orderNumber      = 111L;
        message.canceledQuantity = 222L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        message.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.OrderCanceled restored = new PMR.OrderCanceled();
        restored.get(buffer);

        assertEquals(message.timestamp,        restored.timestamp);
        assertEquals(message.orderNumber,      restored.orderNumber);
        assertEquals(message.canceledQuantity, restored.canceledQuantity);
    }
}
