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
package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class POEEnterOrderTest {

    @Test
    void constructor() {
        POE.EnterOrder order = new POE.EnterOrder();

        assertNotNull(order.orderId);
        assertEquals(16, order.orderId.length);
    }

    @Test
    void getEnterOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++)
            orderId[i] = (byte) (i + 1);
        buffer.put(orderId);
        buffer.put(POE.BUY);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        POE.EnterOrder order = new POE.EnterOrder();
        order.get(buffer);

        assertArrayEquals(orderId, order.orderId);
        assertEquals(POE.BUY, order.side);
        assertEquals(100L, order.instrument);
        assertEquals(200L, order.quantity);
        assertEquals(300L, order.price);
    }

    @Test
    void putEnterOrder() {
        POE.EnterOrder order = new POE.EnterOrder();
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++)
            orderId[i] = (byte) (i + 1);
        System.arraycopy(orderId, 0, order.orderId, 0, 16);
        order.side       = POE.SELL;
        order.instrument = 100L;
        order.quantity   = 200L;
        order.price      = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        order.put(buffer);

        buffer.flip();

        assertEquals('E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(orderId, readOrderId);
        assertEquals(POE.SELL, buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
    }

    @Test
    void roundTrip() {
        POE.EnterOrder original = new POE.EnterOrder();
        for (int i = 0; i < 16; i++)
            original.orderId[i] = (byte) ('A' + i);
        original.side       = POE.BUY;
        original.instrument = 12345L;
        original.quantity   = 500L;
        original.price      = 9999L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        original.put(buffer);

        buffer.flip();
        buffer.get(); // skip message type byte

        POE.EnterOrder parsed = new POE.EnterOrder();
        parsed.get(buffer);

        assertArrayEquals(original.orderId, parsed.orderId);
        assertEquals(original.side, parsed.side);
        assertEquals(original.instrument, parsed.instrument);
        assertEquals(original.quantity, parsed.quantity);
        assertEquals(original.price, parsed.price);
    }
}
