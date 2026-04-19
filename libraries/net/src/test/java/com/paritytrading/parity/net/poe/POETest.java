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

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class POETest {

    @Test
    void privateConstructor() throws Exception {
        Constructor<POE> constructor = POE.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        POE instance = constructor.newInstance();

        assertNotNull(instance);
    }

    @Test
    void orderExecutedRoundTripWithUnsignedMatchNumber() {
        POE.OrderExecuted original = new POE.OrderExecuted();
        original.timestamp     = 1234567890L;
        System.arraycopy("ORDER001________".getBytes(), 0, original.orderId, 0, 16);
        original.quantity      = 500L;
        original.price         = 10050L;
        original.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        original.matchNumber   = 0xABCDEF01L;

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_OUTBOUND_MESSAGE_LENGTH);
        original.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals('E', messageType);

        POE.OrderExecuted parsed = new POE.OrderExecuted();
        parsed.get(buffer);

        assertEquals(1234567890L, parsed.timestamp);
        assertArrayEquals(original.orderId, parsed.orderId);
        assertEquals(500L, parsed.quantity);
        assertEquals(10050L, parsed.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, parsed.liquidityFlag);
        assertEquals(0xABCDEF01L, parsed.matchNumber);
    }

    @Test
    void orderExecutedMaxUnsignedMatchNumber() {
        POE.OrderExecuted original = new POE.OrderExecuted();
        original.timestamp     = 0L;
        original.quantity      = 1L;
        original.price         = 1L;
        original.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        original.matchNumber   = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_OUTBOUND_MESSAGE_LENGTH);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderExecuted parsed = new POE.OrderExecuted();
        parsed.get(buffer);

        assertEquals(0xFFFFFFFFL, parsed.matchNumber);
    }

    @Test
    void orderExecutedZeroMatchNumber() {
        POE.OrderExecuted original = new POE.OrderExecuted();
        original.timestamp     = 0L;
        original.quantity      = 1L;
        original.price         = 1L;
        original.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        original.matchNumber   = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_OUTBOUND_MESSAGE_LENGTH);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderExecuted parsed = new POE.OrderExecuted();
        parsed.get(buffer);

        assertEquals(0L, parsed.matchNumber);
    }
}
