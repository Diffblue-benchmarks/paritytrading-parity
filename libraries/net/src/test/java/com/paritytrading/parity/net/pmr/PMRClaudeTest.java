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

class PMRClaudeTest {

    @Test
    void testVersionConstructorInitializesWithZeroValue() {
        PMR.Version version = new PMR.Version();

        assertEquals(0L, version.version);
    }

    @Test
    void testVersionGetWithBasicValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(123);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(123L, version.version);
    }

    @Test
    void testVersionGetWithZeroValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(0);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(0L, version.version);
    }

    @Test
    void testVersionGetWithMaxUnsignedIntValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(-1);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(0xFFFFFFFFL, version.version);
    }

    @Test
    void testVersionGetWithNegativeIntValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(-1000);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals((-1000) & 0xFFFFFFFFL, version.version);
    }

    @Test
    void testVersionGetWithMaxPositiveIntValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(Integer.MAX_VALUE);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(Integer.MAX_VALUE, version.version);
    }

    @Test
    void testVersionGetMultipleTimes() {
        PMR.Version version = new PMR.Version();

        ByteBuffer buffer1 = ByteBuffer.allocate(4);
        buffer1.putInt(100);
        buffer1.flip();

        version.get(buffer1);
        assertEquals(100L, version.version);

        ByteBuffer buffer2 = ByteBuffer.allocate(4);
        buffer2.putInt(200);
        buffer2.flip();

        version.get(buffer2);
        assertEquals(200L, version.version);
    }

    @Test
    void testVersionGetOverwritesPreviousValue() {
        PMR.Version version = new PMR.Version();
        version.version = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(111);
        buffer.flip();

        version.get(buffer);

        assertEquals(111L, version.version);
    }

    @Test
    void testVersionGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt(5);
        buffer.putLong(999L);
        buffer.flip();

        assertEquals(0, buffer.position());

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(4, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testVersionGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putLong(999L);
        buffer.putInt(50);
        buffer.flip();

        buffer.getLong();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(50L, version.version);
    }

    @Test
    void testVersionGetWithOne() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(1);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(1L, version.version);
    }

    @Test
    void testVersionGetWithLargePositiveValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(2147483647);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(2147483647L, version.version);
    }

    @Test
    void testVersionPutWithBasicValue() {
        PMR.Version version = new PMR.Version();
        version.version = 123L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(123, buffer.getInt());
    }

    @Test
    void testVersionPutWithZeroValue() {
        PMR.Version version = new PMR.Version();
        version.version = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(0, buffer.getInt());
    }

    @Test
    void testVersionPutWithMaxUnsignedIntValue() {
        PMR.Version version = new PMR.Version();
        version.version = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void testVersionPutWithMaxPositiveIntValue() {
        PMR.Version version = new PMR.Version();
        version.version = Integer.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(Integer.MAX_VALUE, buffer.getInt());
    }

    @Test
    void testVersionPutWithOne() {
        PMR.Version version = new PMR.Version();
        version.version = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(1, buffer.getInt());
    }

    @Test
    void testVersionPutMultipleTimes() {
        PMR.Version version = new PMR.Version();
        version.version = 100L;

        ByteBuffer buffer1 = ByteBuffer.allocate(5);
        version.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'V', buffer1.get());
        assertEquals(100, buffer1.getInt());

        ByteBuffer buffer2 = ByteBuffer.allocate(5);
        version.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'V', buffer2.get());
        assertEquals(100, buffer2.getInt());
    }

    @Test
    void testVersionPutAdvancesBufferPosition() {
        PMR.Version version = new PMR.Version();
        version.version = 50L;

        ByteBuffer buffer = ByteBuffer.allocate(10);
        assertEquals(0, buffer.position());

        version.put(buffer);

        assertEquals(5, buffer.position());
    }

    @Test
    void testVersionPutWritesMessageType() {
        PMR.Version version = new PMR.Version();
        version.version = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals((byte) 'V', messageType);
    }

    @Test
    void testVersionPutWithBufferAtNonZeroPosition() {
        PMR.Version version = new PMR.Version();
        version.version = 50L;

        ByteBuffer buffer = ByteBuffer.allocate(13);
        buffer.putLong(999L);

        version.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'V', buffer.get());
        assertEquals(50, buffer.getInt());
    }

    @Test
    void testVersionPutWithLargePositiveValue() {
        PMR.Version version = new PMR.Version();
        version.version = 2147483647L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(2147483647, buffer.getInt());
    }

    @Test
    void testVersionGetAndPutRoundTrip() {
        PMR.Version original = new PMR.Version();
        original.version = 123456L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Version recovered = new PMR.Version();
        recovered.get(buffer);

        assertEquals(original.version, recovered.version);
    }

    @Test
    void testVersionGetAndPutRoundTripWithMaxValue() {
        PMR.Version original = new PMR.Version();
        original.version = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Version recovered = new PMR.Version();
        recovered.get(buffer);

        assertEquals(original.version, recovered.version);
    }

    @Test
    void testVersionGetAndPutRoundTripWithZeroValue() {
        PMR.Version original = new PMR.Version();
        original.version = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Version recovered = new PMR.Version();
        recovered.get(buffer);

        assertEquals(original.version, recovered.version);
    }

    @Test
    void testVersionWithProtocolConstant() {
        PMR.Version version = new PMR.Version();
        version.version = PMR.VERSION;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(2, buffer.getInt());
    }

    @Test
    void testTradeConstructorInitializesWithZeroValues() {
        PMR.Trade trade = new PMR.Trade();

        assertEquals(0L, trade.timestamp);
        assertEquals(0L, trade.restingOrderNumber);
        assertEquals(0L, trade.incomingOrderNumber);
        assertEquals(0L, trade.quantity);
        assertEquals(0L, trade.matchNumber);
    }

    @Test
    void testTradeGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1000L);
        buffer.putLong(12345L);
        buffer.putLong(67890L);
        buffer.putLong(5000L);
        buffer.putInt(100);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(1000L, trade.timestamp);
        assertEquals(12345L, trade.restingOrderNumber);
        assertEquals(67890L, trade.incomingOrderNumber);
        assertEquals(5000L, trade.quantity);
        assertEquals(100L, trade.matchNumber);
    }

    @Test
    void testTradeGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putInt(0);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(0L, trade.timestamp);
        assertEquals(0L, trade.restingOrderNumber);
        assertEquals(0L, trade.incomingOrderNumber);
        assertEquals(0L, trade.quantity);
        assertEquals(0L, trade.matchNumber);
    }

    @Test
    void testTradeGetWithMaxLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putInt(-1);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(Long.MAX_VALUE, trade.timestamp);
        assertEquals(Long.MAX_VALUE, trade.restingOrderNumber);
        assertEquals(Long.MAX_VALUE, trade.incomingOrderNumber);
        assertEquals(Long.MAX_VALUE, trade.quantity);
        assertEquals(0xFFFFFFFFL, trade.matchNumber);
    }

    @Test
    void testTradeGetWithMinLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putInt(0);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(Long.MIN_VALUE, trade.timestamp);
        assertEquals(Long.MIN_VALUE, trade.restingOrderNumber);
        assertEquals(Long.MIN_VALUE, trade.incomingOrderNumber);
        assertEquals(Long.MIN_VALUE, trade.quantity);
        assertEquals(0L, trade.matchNumber);
    }

    @Test
    void testTradeGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(-1L);
        buffer.putLong(-2L);
        buffer.putLong(-3L);
        buffer.putLong(-4L);
        buffer.putInt(-1);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(-1L, trade.timestamp);
        assertEquals(-2L, trade.restingOrderNumber);
        assertEquals(-3L, trade.incomingOrderNumber);
        assertEquals(-4L, trade.quantity);
        assertEquals(0xFFFFFFFFL, trade.matchNumber);
    }

    @Test
    void testTradeGetWithMixedValues() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(0L);
        buffer.putLong(12345L);
        buffer.putInt(999);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(Long.MAX_VALUE, trade.timestamp);
        assertEquals(Long.MIN_VALUE, trade.restingOrderNumber);
        assertEquals(0L, trade.incomingOrderNumber);
        assertEquals(12345L, trade.quantity);
        assertEquals(999L, trade.matchNumber);
    }

    @Test
    void testTradeGetMultipleTimes() {
        PMR.Trade trade = new PMR.Trade();

        ByteBuffer buffer1 = ByteBuffer.allocate(36);
        buffer1.putLong(100L);
        buffer1.putLong(200L);
        buffer1.putLong(300L);
        buffer1.putLong(400L);
        buffer1.putInt(50);
        buffer1.flip();

        trade.get(buffer1);
        assertEquals(100L, trade.timestamp);
        assertEquals(200L, trade.restingOrderNumber);
        assertEquals(300L, trade.incomingOrderNumber);
        assertEquals(400L, trade.quantity);
        assertEquals(50L, trade.matchNumber);

        ByteBuffer buffer2 = ByteBuffer.allocate(36);
        buffer2.putLong(500L);
        buffer2.putLong(600L);
        buffer2.putLong(700L);
        buffer2.putLong(800L);
        buffer2.putInt(75);
        buffer2.flip();

        trade.get(buffer2);
        assertEquals(500L, trade.timestamp);
        assertEquals(600L, trade.restingOrderNumber);
        assertEquals(700L, trade.incomingOrderNumber);
        assertEquals(800L, trade.quantity);
        assertEquals(75L, trade.matchNumber);
    }

    @Test
    void testTradeGetWithLargePositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1234567890123456789L);
        buffer.putLong(8765432109876543210L);
        buffer.putLong(5555555555555555555L);
        buffer.putLong(7777777777777777777L);
        buffer.putInt(2147483647);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(1234567890123456789L, trade.timestamp);
        assertEquals(8765432109876543210L, trade.restingOrderNumber);
        assertEquals(5555555555555555555L, trade.incomingOrderNumber);
        assertEquals(7777777777777777777L, trade.quantity);
        assertEquals(2147483647L, trade.matchNumber);
    }

    @Test
    void testTradeGetWithOne() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putInt(1);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(1L, trade.timestamp);
        assertEquals(1L, trade.restingOrderNumber);
        assertEquals(1L, trade.incomingOrderNumber);
        assertEquals(1L, trade.quantity);
        assertEquals(1L, trade.matchNumber);
    }

    @Test
    void testTradeGetOverwritesPreviousValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 999L;
        trade.restingOrderNumber = 888L;
        trade.incomingOrderNumber = 777L;
        trade.quantity = 666L;
        trade.matchNumber = 555L;

        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.putLong(444L);
        buffer.putInt(100);
        buffer.flip();

        trade.get(buffer);

        assertEquals(111L, trade.timestamp);
        assertEquals(222L, trade.restingOrderNumber);
        assertEquals(333L, trade.incomingOrderNumber);
        assertEquals(444L, trade.quantity);
        assertEquals(100L, trade.matchNumber);
    }

    @Test
    void testTradeGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(44);
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(4L);
        buffer.putInt(5);
        buffer.putLong(999L);
        buffer.flip();

        assertEquals(0, buffer.position());

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(36, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testTradeGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(44);
        buffer.putLong(999L);
        buffer.putLong(10L);
        buffer.putLong(20L);
        buffer.putLong(30L);
        buffer.putLong(40L);
        buffer.putInt(50);
        buffer.flip();

        buffer.getLong();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(10L, trade.timestamp);
        assertEquals(20L, trade.restingOrderNumber);
        assertEquals(30L, trade.incomingOrderNumber);
        assertEquals(40L, trade.quantity);
        assertEquals(50L, trade.matchNumber);
    }

    @Test
    void testTradeGetReadsAllFields() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.putLong(444L);
        buffer.putInt(555);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();

        assertEquals(0L, trade.timestamp);
        assertEquals(0L, trade.restingOrderNumber);
        assertEquals(0L, trade.incomingOrderNumber);
        assertEquals(0L, trade.quantity);
        assertEquals(0L, trade.matchNumber);

        trade.get(buffer);

        assertEquals(111L, trade.timestamp);
        assertEquals(222L, trade.restingOrderNumber);
        assertEquals(333L, trade.incomingOrderNumber);
        assertEquals(444L, trade.quantity);
        assertEquals(555L, trade.matchNumber);
    }

    @Test
    void testTradeGetWithSequentialValues() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(4L);
        buffer.putInt(5);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(1L, trade.timestamp);
        assertEquals(2L, trade.restingOrderNumber);
        assertEquals(3L, trade.incomingOrderNumber);
        assertEquals(4L, trade.quantity);
        assertEquals(5L, trade.matchNumber);
    }

    @Test
    void testTradeGetMatchNumberAsUnsignedInt() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.putInt(-1000);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(100L, trade.timestamp);
        assertEquals(200L, trade.restingOrderNumber);
        assertEquals(300L, trade.incomingOrderNumber);
        assertEquals(400L, trade.quantity);
        assertEquals((-1000) & 0xFFFFFFFFL, trade.matchNumber);
    }

    @Test
    void testTradePutWithBasicValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1000L;
        trade.restingOrderNumber = 12345L;
        trade.incomingOrderNumber = 67890L;
        trade.quantity = 5000L;
        trade.matchNumber = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(12345L, buffer.getLong());
        assertEquals(67890L, buffer.getLong());
        assertEquals(5000L, buffer.getLong());
        assertEquals(100, buffer.getInt());
    }

    @Test
    void testTradePutWithZeroValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 0L;
        trade.restingOrderNumber = 0L;
        trade.incomingOrderNumber = 0L;
        trade.quantity = 0L;
        trade.matchNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0, buffer.getInt());
    }

    @Test
    void testTradePutWithMaxLongValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = Long.MAX_VALUE;
        trade.restingOrderNumber = Long.MAX_VALUE;
        trade.incomingOrderNumber = Long.MAX_VALUE;
        trade.quantity = Long.MAX_VALUE;
        trade.matchNumber = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void testTradePutWithMinLongValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = Long.MIN_VALUE;
        trade.restingOrderNumber = Long.MIN_VALUE;
        trade.incomingOrderNumber = Long.MIN_VALUE;
        trade.quantity = Long.MIN_VALUE;
        trade.matchNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(0, buffer.getInt());
    }

    @Test
    void testTradePutWithNegativeValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = -1L;
        trade.restingOrderNumber = -2L;
        trade.incomingOrderNumber = -3L;
        trade.quantity = -4L;
        trade.matchNumber = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(-1L, buffer.getLong());
        assertEquals(-2L, buffer.getLong());
        assertEquals(-3L, buffer.getLong());
        assertEquals(-4L, buffer.getLong());
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void testTradePutWithMixedValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = Long.MAX_VALUE;
        trade.restingOrderNumber = Long.MIN_VALUE;
        trade.incomingOrderNumber = 0L;
        trade.quantity = 12345L;
        trade.matchNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(12345L, buffer.getLong());
        assertEquals(999, buffer.getInt());
    }

    @Test
    void testTradePutMultipleTimes() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 100L;
        trade.restingOrderNumber = 200L;
        trade.incomingOrderNumber = 300L;
        trade.quantity = 400L;
        trade.matchNumber = 50L;

        ByteBuffer buffer1 = ByteBuffer.allocate(37);
        trade.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'T', buffer1.get());
        assertEquals(100L, buffer1.getLong());
        assertEquals(200L, buffer1.getLong());
        assertEquals(300L, buffer1.getLong());
        assertEquals(400L, buffer1.getLong());
        assertEquals(50, buffer1.getInt());

        ByteBuffer buffer2 = ByteBuffer.allocate(37);
        trade.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'T', buffer2.get());
        assertEquals(100L, buffer2.getLong());
        assertEquals(200L, buffer2.getLong());
        assertEquals(300L, buffer2.getLong());
        assertEquals(400L, buffer2.getLong());
        assertEquals(50, buffer2.getInt());
    }

    @Test
    void testTradePutWithLargePositiveValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1234567890123456789L;
        trade.restingOrderNumber = 8765432109876543210L;
        trade.incomingOrderNumber = 5555555555555555555L;
        trade.quantity = 7777777777777777777L;
        trade.matchNumber = 2147483647L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(1234567890123456789L, buffer.getLong());
        assertEquals(8765432109876543210L, buffer.getLong());
        assertEquals(5555555555555555555L, buffer.getLong());
        assertEquals(7777777777777777777L, buffer.getLong());
        assertEquals(2147483647, buffer.getInt());
    }

    @Test
    void testTradePutWithOne() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1L;
        trade.restingOrderNumber = 1L;
        trade.incomingOrderNumber = 1L;
        trade.quantity = 1L;
        trade.matchNumber = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1, buffer.getInt());
    }

    @Test
    void testTradePutAdvancesBufferPosition() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1L;
        trade.restingOrderNumber = 2L;
        trade.incomingOrderNumber = 3L;
        trade.quantity = 4L;
        trade.matchNumber = 5L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        assertEquals(0, buffer.position());

        trade.put(buffer);

        assertEquals(37, buffer.position());
    }

    @Test
    void testTradePutWritesMessageType() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1000L;
        trade.restingOrderNumber = 2000L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity = 4000L;
        trade.matchNumber = 500L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals((byte) 'T', messageType);
    }

    @Test
    void testTradePutWritesFieldsInCorrectOrder() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 111L;
        trade.restingOrderNumber = 222L;
        trade.incomingOrderNumber = 333L;
        trade.quantity = 444L;
        trade.matchNumber = 555L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(111L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
        assertEquals(333L, buffer.getLong());
        assertEquals(444L, buffer.getLong());
        assertEquals(555, buffer.getInt());
    }

    @Test
    void testTradePutWithBufferAtNonZeroPosition() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 100L;
        trade.restingOrderNumber = 200L;
        trade.incomingOrderNumber = 300L;
        trade.quantity = 400L;
        trade.matchNumber = 50L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.putLong(999L);

        trade.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'T', buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
        assertEquals(400L, buffer.getLong());
        assertEquals(50, buffer.getInt());
    }

    @Test
    void testTradePutMatchNumberAsUnsignedInt() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 100L;
        trade.restingOrderNumber = 200L;
        trade.incomingOrderNumber = 300L;
        trade.quantity = 400L;
        trade.matchNumber = 4294967295L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void testTradeGetAndPutRoundTrip() {
        PMR.Trade original = new PMR.Trade();
        original.timestamp = 1234567890L;
        original.restingOrderNumber = 9876543210L;
        original.incomingOrderNumber = 5555555555L;
        original.quantity = 1111111111L;
        original.matchNumber = 999999L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Trade recovered = new PMR.Trade();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertEquals(original.restingOrderNumber, recovered.restingOrderNumber);
        assertEquals(original.incomingOrderNumber, recovered.incomingOrderNumber);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.matchNumber, recovered.matchNumber);
    }

    @Test
    void testTradeGetAndPutRoundTripWithMaxValues() {
        PMR.Trade original = new PMR.Trade();
        original.timestamp = Long.MAX_VALUE;
        original.restingOrderNumber = Long.MAX_VALUE;
        original.incomingOrderNumber = Long.MAX_VALUE;
        original.quantity = Long.MAX_VALUE;
        original.matchNumber = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Trade recovered = new PMR.Trade();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertEquals(original.restingOrderNumber, recovered.restingOrderNumber);
        assertEquals(original.incomingOrderNumber, recovered.incomingOrderNumber);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.matchNumber, recovered.matchNumber);
    }

    @Test
    void testTradeGetAndPutRoundTripWithMinValues() {
        PMR.Trade original = new PMR.Trade();
        original.timestamp = Long.MIN_VALUE;
        original.restingOrderNumber = Long.MIN_VALUE;
        original.incomingOrderNumber = Long.MIN_VALUE;
        original.quantity = Long.MIN_VALUE;
        original.matchNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Trade recovered = new PMR.Trade();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertEquals(original.restingOrderNumber, recovered.restingOrderNumber);
        assertEquals(original.incomingOrderNumber, recovered.incomingOrderNumber);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.matchNumber, recovered.matchNumber);
    }

    @Test
    void testTradeGetAndPutRoundTripWithZeroValues() {
        PMR.Trade original = new PMR.Trade();
        original.timestamp = 0L;
        original.restingOrderNumber = 0L;
        original.incomingOrderNumber = 0L;
        original.quantity = 0L;
        original.matchNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Trade recovered = new PMR.Trade();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertEquals(original.restingOrderNumber, recovered.restingOrderNumber);
        assertEquals(original.incomingOrderNumber, recovered.incomingOrderNumber);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.matchNumber, recovered.matchNumber);
    }
}
