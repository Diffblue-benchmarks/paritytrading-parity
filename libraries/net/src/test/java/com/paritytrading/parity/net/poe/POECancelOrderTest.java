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

class POECancelOrderTest {

    @Test
    void constructorInitializesOrderId() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();

        assertNotNull(cancelOrder.orderId);
        assertEquals(16, cancelOrder.orderId.length);
    }

    @Test
    void constructorInitializesQuantityToZero() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();

        assertEquals(0L, cancelOrder.quantity);
    }

    @Test
    void getReadsFieldsFromBuffer() {
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++)
            orderId[i] = (byte) ('A' + i);
        long quantity = 500L;

        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.put(orderId);
        buffer.putLong(quantity);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(orderId, cancelOrder.orderId);
        assertEquals(500L, cancelOrder.quantity);
    }

    @Test
    void putWritesFieldsToBuffer() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        for (int i = 0; i < 16; i++)
            cancelOrder.orderId[i] = (byte) ('A' + i);
        cancelOrder.quantity = 750L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals('X', buffer.get());

        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(cancelOrder.orderId, readOrderId);

        assertEquals(750L, buffer.getLong());
    }

    @Test
    void roundTrip() {
        POE.CancelOrder original = new POE.CancelOrder();
        for (int i = 0; i < 16; i++)
            original.orderId[i] = (byte) (i + 1);
        original.quantity = 12345L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        original.put(buffer);
        buffer.flip();

        // Skip the message type byte
        buffer.get();

        POE.CancelOrder restored = new POE.CancelOrder();
        restored.get(buffer);

        assertArrayEquals(original.orderId, restored.orderId);
        assertEquals(original.quantity, restored.quantity);
    }
}
