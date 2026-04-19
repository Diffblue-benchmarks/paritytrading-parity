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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class POEOrderAcceptedTest {

    private POE.OrderAccepted message;

    @BeforeEach
    void setUp() {
        message = new POE.OrderAccepted();
    }

    @Test
    void constructor() {
        assertNotNull(message.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, message.orderId.length);
    }

    @Test
    void get() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(123456789L);
        byte[] orderId = "ORDERID123456789".getBytes();
        buffer.put(orderId);
        buffer.put(POE.BUY);
        buffer.putLong(2002L);
        buffer.putLong(500L);
        buffer.putLong(1050L);
        buffer.putLong(7007L);
        buffer.flip();

        message.get(buffer);

        assertEquals(123456789L, message.timestamp);
        assertArrayEquals(orderId, message.orderId);
        assertEquals(POE.BUY, message.side);
        assertEquals(2002L, message.instrument);
        assertEquals(500L, message.quantity);
        assertEquals(1050L, message.price);
        assertEquals(7007L, message.orderNumber);
    }

    @Test
    void put() {
        message.timestamp   = 123456789L;
        message.orderId     = "ORDERID123456789".getBytes();
        message.side        = POE.SELL;
        message.instrument  = 2002L;
        message.quantity    = 500L;
        message.price       = 1050L;
        message.orderNumber = 7007L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        message.put(buffer);
        buffer.flip();

        assertEquals('A', buffer.get());
        assertEquals(123456789L, buffer.getLong());
        byte[] readOrderId = new byte[POE.ORDER_ID_LENGTH];
        buffer.get(readOrderId);
        assertArrayEquals("ORDERID123456789".getBytes(), readOrderId);
        assertEquals(POE.SELL, buffer.get());
        assertEquals(2002L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(1050L, buffer.getLong());
        assertEquals(7007L, buffer.getLong());
    }

    @Test
    void getRoundTrip() {
        message.timestamp   = 987654321L;
        message.orderId     = "ROUNDTRIP1234567".getBytes();
        message.side        = POE.BUY;
        message.instrument  = 3003L;
        message.quantity    = 250L;
        message.price       = 7500L;
        message.orderNumber = 9009L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        message.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderAccepted decoded = new POE.OrderAccepted();
        decoded.get(buffer);

        assertEquals(message.timestamp, decoded.timestamp);
        assertArrayEquals(message.orderId, decoded.orderId);
        assertEquals(message.side, decoded.side);
        assertEquals(message.instrument, decoded.instrument);
        assertEquals(message.quantity, decoded.quantity);
        assertEquals(message.price, decoded.price);
        assertEquals(message.orderNumber, decoded.orderNumber);
    }
}
