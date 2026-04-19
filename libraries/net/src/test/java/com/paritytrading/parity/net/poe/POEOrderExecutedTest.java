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

class POEOrderExecutedTest {

    private POE.OrderExecuted message;

    @BeforeEach
    void setUp() {
        message = new POE.OrderExecuted();
    }

    @Test
    void constructor() {
        assertNotNull(message.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, message.orderId.length);
    }

    @Test
    void get() {
        ByteBuffer buffer = ByteBuffer.allocate(45);

        buffer.putLong(123456789L);
        buffer.put(new byte[] { 'O', 'R', 'D', '0', '0', '1', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        buffer.putLong(500L);
        buffer.putLong(10050L);
        buffer.put(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY);
        buffer.putInt(42);

        buffer.flip();

        message.get(buffer);

        assertEquals(123456789L, message.timestamp);
        assertArrayEquals(new byte[] { 'O', 'R', 'D', '0', '0', '1', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, message.orderId);
        assertEquals(500L, message.quantity);
        assertEquals(10050L, message.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, message.liquidityFlag);
        assertEquals(42L, message.matchNumber);
    }

    @Test
    void put() {
        message.timestamp     = 123456789L;
        message.orderId       = new byte[] { 'O', 'R', 'D', '0', '0', '1', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        message.quantity      = 500L;
        message.price         = 10050L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        message.matchNumber   = 99L;

        ByteBuffer buffer = ByteBuffer.allocate(46);

        message.put(buffer);

        buffer.flip();

        assertEquals('E', buffer.get());
        assertEquals(123456789L, buffer.getLong());

        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        buffer.get(orderId);
        assertArrayEquals(new byte[] { 'O', 'R', 'D', '0', '0', '1', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, orderId);

        assertEquals(500L, buffer.getLong());
        assertEquals(10050L, buffer.getLong());
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, buffer.get());
        assertEquals(99, buffer.getInt());
    }

    @Test
    void getRoundTrip() {
        message.timestamp     = 999999999L;
        message.orderId       = new byte[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P' };
        message.quantity      = 1000L;
        message.price         = 25000L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber   = 12345L;

        ByteBuffer buffer = ByteBuffer.allocate(46);

        message.put(buffer);

        buffer.flip();
        buffer.get();

        POE.OrderExecuted decoded = new POE.OrderExecuted();
        decoded.get(buffer);

        assertEquals(message.timestamp, decoded.timestamp);
        assertArrayEquals(message.orderId, decoded.orderId);
        assertEquals(message.quantity, decoded.quantity);
        assertEquals(message.price, decoded.price);
        assertEquals(message.liquidityFlag, decoded.liquidityFlag);
        assertEquals(message.matchNumber, decoded.matchNumber);
    }
}
