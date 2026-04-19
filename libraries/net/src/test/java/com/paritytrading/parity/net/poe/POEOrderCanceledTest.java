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
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class POEOrderCanceledTest {

    private POE.OrderCanceled message;

    @BeforeEach
    void setUp() {
        message = new POE.OrderCanceled();
    }

    @Test
    void constructorInitializesOrderId() {
        assertNotNull(message.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, message.orderId.length);
    }

    @Test
    void getFromBuffer() {
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        Arrays.fill(orderId, (byte) 'A');

        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(1000L);
        buffer.put(orderId);
        buffer.putLong(500L);
        buffer.put(POE.ORDER_CANCEL_REASON_REQUEST);
        buffer.flip();

        message.get(buffer);

        assertEquals(1000L, message.timestamp);
        assertArrayEquals(orderId, message.orderId);
        assertEquals(500L, message.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, message.reason);
    }

    @Test
    void putToBuffer() {
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        Arrays.fill(orderId, (byte) 'B');

        message.timestamp        = 2000L;
        message.orderId          = orderId;
        message.canceledQuantity = 750L;
        message.reason           = POE.ORDER_CANCEL_REASON_SUPERVISORY;

        ByteBuffer buffer = ByteBuffer.allocate(34);

        message.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(2000L, buffer.getLong());

        byte[] readOrderId = new byte[POE.ORDER_ID_LENGTH];
        buffer.get(readOrderId);
        assertArrayEquals(orderId, readOrderId);

        assertEquals(750L, buffer.getLong());
        assertEquals(POE.ORDER_CANCEL_REASON_SUPERVISORY, buffer.get());
    }

    @Test
    void getRoundTripsThroughPut() {
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        Arrays.fill(orderId, (byte) 'C');

        message.timestamp        = 3000L;
        message.orderId          = orderId;
        message.canceledQuantity = 100L;
        message.reason           = POE.ORDER_CANCEL_REASON_REQUEST;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        message.put(buffer);
        buffer.flip();

        buffer.get(); // skip message type byte

        POE.OrderCanceled decoded = new POE.OrderCanceled();
        decoded.get(buffer);

        assertEquals(message.timestamp, decoded.timestamp);
        assertArrayEquals(message.orderId, decoded.orderId);
        assertEquals(message.canceledQuantity, decoded.canceledQuantity);
        assertEquals(message.reason, decoded.reason);
    }
}
