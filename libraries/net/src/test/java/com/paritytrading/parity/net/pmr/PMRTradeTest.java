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
import org.junit.jupiter.api.Test;

class PMRTradeTest {

    @Test
    void getTrade() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(100L);
        buffer.putInt(42);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(1000L, trade.timestamp);
        assertEquals(2000L, trade.restingOrderNumber);
        assertEquals(3000L, trade.incomingOrderNumber);
        assertEquals(100L, trade.quantity);
        assertEquals(42L, trade.matchNumber);
    }

    @Test
    void getTradeLargeUnsignedMatchNumber() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(500L);
        buffer.putLong(600L);
        buffer.putLong(700L);
        buffer.putLong(50L);
        buffer.putInt((int) 0xFFFFFFFFL);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(500L, trade.timestamp);
        assertEquals(600L, trade.restingOrderNumber);
        assertEquals(700L, trade.incomingOrderNumber);
        assertEquals(50L, trade.quantity);
        assertEquals(0xFFFFFFFFL, trade.matchNumber);
    }

    @Test
    void putTrade() {
        ByteBuffer buffer = ByteBuffer.allocate(37);

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp           = 1000L;
        trade.restingOrderNumber  = 2000L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity            = 100L;
        trade.matchNumber         = 42L;
        trade.put(buffer);

        buffer.flip();

        assertEquals('T', buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(2000L, buffer.getLong());
        assertEquals(3000L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
        assertEquals(42, buffer.getInt());
    }

    @Test
    void putTradeLargeUnsignedMatchNumber() {
        ByteBuffer buffer = ByteBuffer.allocate(37);

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp           = 500L;
        trade.restingOrderNumber  = 600L;
        trade.incomingOrderNumber = 700L;
        trade.quantity            = 50L;
        trade.matchNumber         = 0xFFFFFFFFL;
        trade.put(buffer);

        buffer.flip();

        assertEquals('T', buffer.get());
        assertEquals(500L, buffer.getLong());
        assertEquals(600L, buffer.getLong());
        assertEquals(700L, buffer.getLong());
        assertEquals(50L, buffer.getLong());
        assertEquals((int) 0xFFFFFFFFL, buffer.getInt());
    }

    @Test
    void roundTrip() {
        PMR.Trade original = new PMR.Trade();
        original.timestamp           = 9999L;
        original.restingOrderNumber  = 1111L;
        original.incomingOrderNumber = 2222L;
        original.quantity            = 500L;
        original.matchNumber         = 12345L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        original.put(buffer);

        buffer.flip();
        buffer.get(); // skip message type byte

        PMR.Trade parsed = new PMR.Trade();
        parsed.get(buffer);

        assertEquals(original.timestamp, parsed.timestamp);
        assertEquals(original.restingOrderNumber, parsed.restingOrderNumber);
        assertEquals(original.incomingOrderNumber, parsed.incomingOrderNumber);
        assertEquals(original.quantity, parsed.quantity);
        assertEquals(original.matchNumber, parsed.matchNumber);
    }
}
