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

class POEClaude_getTest {

    @Test
    void testGetReadsOrderIdFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
    }

    @Test
    void testGetReadsSideFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'S');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals((byte) 'S', enterOrder.side);
    }

    @Test
    void testGetReadsInstrumentFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(12345L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(12345L, enterOrder.instrument);
    }

    @Test
    void testGetReadsQuantityFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(54321L);
        buffer.putLong(300L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(54321L, enterOrder.quantity);
    }

    @Test
    void testGetReadsPriceFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(98765L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(98765L, enterOrder.price);
    }

    @Test
    void testGetReadsAllFieldsInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.BUY);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals(POE.BUY, enterOrder.side);
        assertEquals(111L, enterOrder.instrument);
        assertEquals(222L, enterOrder.quantity);
        assertEquals(333L, enterOrder.price);
    }

    @Test
    void testGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals((byte) 0, enterOrder.side);
        assertEquals(0L, enterOrder.instrument);
        assertEquals(0L, enterOrder.quantity);
        assertEquals(0L, enterOrder.price);
    }

    @Test
    void testGetWithMaxValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 0xFF);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals((byte) 0xFF, enterOrder.side);
        assertEquals(Long.MAX_VALUE, enterOrder.instrument);
        assertEquals(Long.MAX_VALUE, enterOrder.quantity);
        assertEquals(Long.MAX_VALUE, enterOrder.price);
    }

    @Test
    void testGetWithMinValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(Byte.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals(Byte.MIN_VALUE, enterOrder.side);
        assertEquals(Long.MIN_VALUE, enterOrder.instrument);
        assertEquals(Long.MIN_VALUE, enterOrder.quantity);
        assertEquals(Long.MIN_VALUE, enterOrder.price);
    }

    @Test
    void testGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) -1);
        buffer.putLong(-100L);
        buffer.putLong(-200L);
        buffer.putLong(-300L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals((byte) -1, enterOrder.side);
        assertEquals(-100L, enterOrder.instrument);
        assertEquals(-200L, enterOrder.quantity);
        assertEquals(-300L, enterOrder.price);
    }

    @Test
    void testGetUpdatesExistingValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] oldOrderId = "OLDORDER12345678".getBytes();
        System.arraycopy(oldOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'X';
        enterOrder.instrument = 999L;
        enterOrder.quantity = 888L;
        enterOrder.price = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] newOrderId = "NEWORDER87654321".getBytes();
        buffer.put(newOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(123L);
        buffer.putLong(456L);
        buffer.putLong(789L);
        buffer.flip();

        enterOrder.get(buffer);

        assertArrayEquals(newOrderId, enterOrder.orderId);
        assertEquals((byte) 'B', enterOrder.side);
        assertEquals(123L, enterOrder.instrument);
        assertEquals(456L, enterOrder.quantity);
        assertEquals(789L, enterOrder.price);
    }

    @Test
    void testGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(999L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        int initialPosition = buffer.position();

        enterOrder.get(buffer);

        assertEquals(initialPosition + 41, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.putLong(123456L);
        byte[] testOrderId = "ORDER000000000AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.SELL);
        buffer.putLong(10L);
        buffer.putLong(20L);
        buffer.putLong(30L);
        buffer.flip();

        buffer.getLong();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals(POE.SELL, enterOrder.side);
        assertEquals(10L, enterOrder.instrument);
        assertEquals(20L, enterOrder.quantity);
        assertEquals(30L, enterOrder.price);
    }

    @Test
    void testGetReadsExactly41Bytes() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(41, buffer.position());
        assertFalse(buffer.hasRemaining());
    }

    @Test
    void testGetCanBeCalledMultipleTimes() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        ByteBuffer buffer1 = ByteBuffer.allocate(41);
        byte[] testOrderId1 = "FIRST00000000001".getBytes();
        buffer1.put(testOrderId1);
        buffer1.put((byte) 'B');
        buffer1.putLong(1L);
        buffer1.putLong(2L);
        buffer1.putLong(3L);
        buffer1.flip();

        enterOrder.get(buffer1);

        assertArrayEquals(testOrderId1, enterOrder.orderId);
        assertEquals((byte) 'B', enterOrder.side);
        assertEquals(1L, enterOrder.instrument);

        ByteBuffer buffer2 = ByteBuffer.allocate(41);
        byte[] testOrderId2 = "SECOND0000000002".getBytes();
        buffer2.put(testOrderId2);
        buffer2.put((byte) 'S');
        buffer2.putLong(10L);
        buffer2.putLong(20L);
        buffer2.putLong(30L);
        buffer2.flip();

        enterOrder.get(buffer2);

        assertArrayEquals(testOrderId2, enterOrder.orderId);
        assertEquals((byte) 'S', enterOrder.side);
        assertEquals(10L, enterOrder.instrument);
        assertEquals(20L, enterOrder.quantity);
        assertEquals(30L, enterOrder.price);
    }

    @Test
    void testGetWithBuySideConstant() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(POE.BUY);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(POE.BUY, enterOrder.side);
        assertEquals((byte) 'B', enterOrder.side);
    }

    @Test
    void testGetWithSellSideConstant() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(POE.SELL);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(POE.SELL, enterOrder.side);
        assertEquals((byte) 'S', enterOrder.side);
    }

    @Test
    void testGetWithComplexOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) (i * 17);
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(555L);
        buffer.putLong(666L);
        buffer.putLong(777L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals(555L, enterOrder.instrument);
        assertEquals(666L, enterOrder.quantity);
        assertEquals(777L, enterOrder.price);
    }

    @Test
    void testGetWithAlphanumericOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "ORD123ABC456DEF7".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
    }

    @Test
    void testGetPreservesOrderIdLength() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        testOrderId[15] = 15;
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(16, enterOrder.orderId.length);
        assertEquals(1, enterOrder.orderId[0]);
        assertEquals(15, enterOrder.orderId[15]);
    }

    @Test
    void testGetWithOneValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        buffer.put(testOrderId);
        buffer.put((byte) 1);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals((byte) 1, enterOrder.side);
        assertEquals(1L, enterOrder.instrument);
        assertEquals(1L, enterOrder.quantity);
        assertEquals(1L, enterOrder.price);
    }

    @Test
    void testGetWithLargePositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 127);
        buffer.putLong(9223372036854775806L);
        buffer.putLong(9223372036854775805L);
        buffer.putLong(9223372036854775804L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals((byte) 127, enterOrder.side);
        assertEquals(9223372036854775806L, enterOrder.instrument);
        assertEquals(9223372036854775805L, enterOrder.quantity);
        assertEquals(9223372036854775804L, enterOrder.price);
    }

    @Test
    void testGetReadsFieldsSequentially() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "0123456789ABCDEF".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();

        int pos1 = buffer.position();
        enterOrder.get(buffer);
        int pos2 = buffer.position();

        assertEquals(0, pos1);
        assertEquals(41, pos2);
        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals((byte) 'B', enterOrder.side);
        assertEquals(100L, enterOrder.instrument);
        assertEquals(200L, enterOrder.quantity);
        assertEquals(300L, enterOrder.price);
    }

    @Test
    void testGetDoesNotChangeBufferLimit() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.flip();

        int originalLimit = buffer.limit();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(originalLimit, buffer.limit());
    }

    @Test
    void testGetWithRealisticTradingScenario() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "TRD9999999999999".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.BUY);
        buffer.putLong(1234L);
        buffer.putLong(500L);
        buffer.putLong(9850L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals(POE.BUY, enterOrder.side);
        assertEquals(1234L, enterOrder.instrument);
        assertEquals(500L, enterOrder.quantity);
        assertEquals(9850L, enterOrder.price);
    }

    // ========== OrderAccepted Get Tests ==========

    @Test
    void testOrderAcceptedGetReadsTimestampFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(1234567890L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(1234567890L, orderAccepted.timestamp);
    }

    @Test
    void testOrderAcceptedGetReadsOrderIdFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(123L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertArrayEquals(testOrderId, orderAccepted.orderId);
    }

    @Test
    void testOrderAcceptedGetReadsSideFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'S');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals((byte) 'S', orderAccepted.side);
    }

    @Test
    void testOrderAcceptedGetReadsInstrumentFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(12345L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(12345L, orderAccepted.instrument);
    }

    @Test
    void testOrderAcceptedGetReadsQuantityFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(54321L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(54321L, orderAccepted.quantity);
    }

    @Test
    void testOrderAcceptedGetReadsPriceFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(98765L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(98765L, orderAccepted.price);
    }

    @Test
    void testOrderAcceptedGetReadsOrderNumberFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(99999L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(99999L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetReadsAllFieldsInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.BUY);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.putLong(444L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(1609459200000L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals(POE.BUY, orderAccepted.side);
        assertEquals(111L, orderAccepted.instrument);
        assertEquals(222L, orderAccepted.quantity);
        assertEquals(333L, orderAccepted.price);
        assertEquals(444L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(0L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(0L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals((byte) 0, orderAccepted.side);
        assertEquals(0L, orderAccepted.instrument);
        assertEquals(0L, orderAccepted.quantity);
        assertEquals(0L, orderAccepted.price);
        assertEquals(0L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithMaxValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(Long.MAX_VALUE);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 0xFF);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(Long.MAX_VALUE, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals((byte) 0xFF, orderAccepted.side);
        assertEquals(Long.MAX_VALUE, orderAccepted.instrument);
        assertEquals(Long.MAX_VALUE, orderAccepted.quantity);
        assertEquals(Long.MAX_VALUE, orderAccepted.price);
        assertEquals(Long.MAX_VALUE, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithMinValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(Long.MIN_VALUE);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(Byte.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(Long.MIN_VALUE, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals(Byte.MIN_VALUE, orderAccepted.side);
        assertEquals(Long.MIN_VALUE, orderAccepted.instrument);
        assertEquals(Long.MIN_VALUE, orderAccepted.quantity);
        assertEquals(Long.MIN_VALUE, orderAccepted.price);
        assertEquals(Long.MIN_VALUE, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(-1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) -1);
        buffer.putLong(-100L);
        buffer.putLong(-200L);
        buffer.putLong(-300L);
        buffer.putLong(-400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(-1000L, orderAccepted.timestamp);
        assertEquals((byte) -1, orderAccepted.side);
        assertEquals(-100L, orderAccepted.instrument);
        assertEquals(-200L, orderAccepted.quantity);
        assertEquals(-300L, orderAccepted.price);
        assertEquals(-400L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetUpdatesExistingValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 999999L;
        byte[] oldOrderId = "OLDORDER12345678".getBytes();
        System.arraycopy(oldOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'X';
        orderAccepted.instrument = 999L;
        orderAccepted.quantity = 888L;
        orderAccepted.price = 777L;
        orderAccepted.orderNumber = 666L;

        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(1111L);
        byte[] newOrderId = "NEWORDER87654321".getBytes();
        buffer.put(newOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(123L);
        buffer.putLong(456L);
        buffer.putLong(789L);
        buffer.putLong(555L);
        buffer.flip();

        orderAccepted.get(buffer);

        assertEquals(1111L, orderAccepted.timestamp);
        assertArrayEquals(newOrderId, orderAccepted.orderId);
        assertEquals((byte) 'B', orderAccepted.side);
        assertEquals(123L, orderAccepted.instrument);
        assertEquals(456L, orderAccepted.quantity);
        assertEquals(789L, orderAccepted.price);
        assertEquals(555L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(65);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(4L);
        buffer.putLong(999L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        int initialPosition = buffer.position();

        orderAccepted.get(buffer);

        assertEquals(initialPosition + 57, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(65);
        buffer.putLong(123456L);
        buffer.putLong(7777L);
        byte[] testOrderId = "ORDER000000000AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.SELL);
        buffer.putLong(10L);
        buffer.putLong(20L);
        buffer.putLong(30L);
        buffer.putLong(40L);
        buffer.flip();

        buffer.getLong();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(7777L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals(POE.SELL, orderAccepted.side);
        assertEquals(10L, orderAccepted.instrument);
        assertEquals(20L, orderAccepted.quantity);
        assertEquals(30L, orderAccepted.price);
        assertEquals(40L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetReadsExactly57Bytes() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(5000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(4L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(57, buffer.position());
        assertFalse(buffer.hasRemaining());
    }

    @Test
    void testOrderAcceptedGetCanBeCalledMultipleTimes() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        ByteBuffer buffer1 = ByteBuffer.allocate(57);
        buffer1.putLong(1111L);
        byte[] testOrderId1 = "FIRST00000000001".getBytes();
        buffer1.put(testOrderId1);
        buffer1.put((byte) 'B');
        buffer1.putLong(1L);
        buffer1.putLong(2L);
        buffer1.putLong(3L);
        buffer1.putLong(100L);
        buffer1.flip();

        orderAccepted.get(buffer1);

        assertEquals(1111L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId1, orderAccepted.orderId);
        assertEquals((byte) 'B', orderAccepted.side);
        assertEquals(1L, orderAccepted.instrument);
        assertEquals(100L, orderAccepted.orderNumber);

        ByteBuffer buffer2 = ByteBuffer.allocate(57);
        buffer2.putLong(2222L);
        byte[] testOrderId2 = "SECOND0000000002".getBytes();
        buffer2.put(testOrderId2);
        buffer2.put((byte) 'S');
        buffer2.putLong(10L);
        buffer2.putLong(20L);
        buffer2.putLong(30L);
        buffer2.putLong(200L);
        buffer2.flip();

        orderAccepted.get(buffer2);

        assertEquals(2222L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId2, orderAccepted.orderId);
        assertEquals((byte) 'S', orderAccepted.side);
        assertEquals(10L, orderAccepted.instrument);
        assertEquals(20L, orderAccepted.quantity);
        assertEquals(30L, orderAccepted.price);
        assertEquals(200L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithBuySideConstant() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(POE.BUY);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(POE.BUY, orderAccepted.side);
        assertEquals((byte) 'B', orderAccepted.side);
    }

    @Test
    void testOrderAcceptedGetWithSellSideConstant() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(2000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(POE.SELL);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(POE.SELL, orderAccepted.side);
        assertEquals((byte) 'S', orderAccepted.side);
    }

    @Test
    void testOrderAcceptedGetWithComplexOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(3000L);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) (i * 17);
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(555L);
        buffer.putLong(666L);
        buffer.putLong(777L);
        buffer.putLong(888L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(3000L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals(555L, orderAccepted.instrument);
        assertEquals(666L, orderAccepted.quantity);
        assertEquals(777L, orderAccepted.price);
        assertEquals(888L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithAlphanumericOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(4000L);
        byte[] testOrderId = "ORD123ABC456DEF7".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(4000L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
    }

    @Test
    void testOrderAcceptedGetPreservesOrderIdLength() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(5000L);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        testOrderId[15] = 15;
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(4L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(16, orderAccepted.orderId.length);
        assertEquals(1, orderAccepted.orderId[0]);
        assertEquals(15, orderAccepted.orderId[15]);
    }

    @Test
    void testOrderAcceptedGetWithOneValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(1L);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        buffer.put(testOrderId);
        buffer.put((byte) 1);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(1L, orderAccepted.timestamp);
        assertEquals((byte) 1, orderAccepted.side);
        assertEquals(1L, orderAccepted.instrument);
        assertEquals(1L, orderAccepted.quantity);
        assertEquals(1L, orderAccepted.price);
        assertEquals(1L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithLargePositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(9223372036854775806L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 127);
        buffer.putLong(9223372036854775805L);
        buffer.putLong(9223372036854775804L);
        buffer.putLong(9223372036854775803L);
        buffer.putLong(9223372036854775802L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(9223372036854775806L, orderAccepted.timestamp);
        assertEquals((byte) 127, orderAccepted.side);
        assertEquals(9223372036854775805L, orderAccepted.instrument);
        assertEquals(9223372036854775804L, orderAccepted.quantity);
        assertEquals(9223372036854775803L, orderAccepted.price);
        assertEquals(9223372036854775802L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetReadsFieldsSequentially() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(6000L);
        byte[] testOrderId = "0123456789ABCDEF".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.putLong(400L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        int pos1 = buffer.position();
        orderAccepted.get(buffer);
        int pos2 = buffer.position();

        assertEquals(0, pos1);
        assertEquals(57, pos2);
        assertEquals(6000L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals((byte) 'B', orderAccepted.side);
        assertEquals(100L, orderAccepted.instrument);
        assertEquals(200L, orderAccepted.quantity);
        assertEquals(300L, orderAccepted.price);
        assertEquals(400L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetDoesNotChangeBufferLimit() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.putLong(7000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(4L);
        buffer.flip();

        int originalLimit = buffer.limit();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(originalLimit, buffer.limit());
    }

    @Test
    void testOrderAcceptedGetWithRealisticTradingScenario() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "TRD9999999999999".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.BUY);
        buffer.putLong(1234L);
        buffer.putLong(500L);
        buffer.putLong(9850L);
        buffer.putLong(55555L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(1609459200000L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals(POE.BUY, orderAccepted.side);
        assertEquals(1234L, orderAccepted.instrument);
        assertEquals(500L, orderAccepted.quantity);
        assertEquals(9850L, orderAccepted.price);
        assertEquals(55555L, orderAccepted.orderNumber);
    }

    // ========== OrderCanceled Get Tests ==========

    @Test
    void testOrderCanceledGetReadsTimestampFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(1234567890L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1234567890L, orderCanceled.timestamp);
    }

    @Test
    void testOrderCanceledGetReadsOrderIdFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(123L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertArrayEquals(testOrderId, orderCanceled.orderId);
    }

    @Test
    void testOrderCanceledGetReadsCanceledQuantityFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(54321L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(54321L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetReadsReasonFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put((byte) 'S');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals((byte) 'S', orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetReadsAllFieldsInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(222L);
        buffer.put(POE.ORDER_CANCEL_REASON_REQUEST);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1609459200000L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(222L, orderCanceled.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(0L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(0L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(0L, orderCanceled.canceledQuantity);
        assertEquals((byte) 0, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithMaxValues() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(Long.MAX_VALUE);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.putLong(Long.MAX_VALUE);
        buffer.put((byte) 0xFF);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(Long.MAX_VALUE, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(Long.MAX_VALUE, orderCanceled.canceledQuantity);
        assertEquals((byte) 0xFF, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithMinValues() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(Long.MIN_VALUE);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(Long.MIN_VALUE);
        buffer.put(Byte.MIN_VALUE);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(Long.MIN_VALUE, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(Long.MIN_VALUE, orderCanceled.canceledQuantity);
        assertEquals(Byte.MIN_VALUE, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(-1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(-200L);
        buffer.put((byte) -1);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(-1000L, orderCanceled.timestamp);
        assertEquals(-200L, orderCanceled.canceledQuantity);
        assertEquals((byte) -1, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetUpdatesExistingValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 999999L;
        byte[] oldOrderId = "OLDORDER12345678".getBytes();
        System.arraycopy(oldOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 888L;
        orderCanceled.reason = (byte) 'X';

        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(1111L);
        byte[] newOrderId = "NEWORDER87654321".getBytes();
        buffer.put(newOrderId);
        buffer.putLong(456L);
        buffer.put((byte) 'R');
        buffer.flip();

        orderCanceled.get(buffer);

        assertEquals(1111L, orderCanceled.timestamp);
        assertArrayEquals(newOrderId, orderCanceled.orderId);
        assertEquals(456L, orderCanceled.canceledQuantity);
        assertEquals((byte) 'R', orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put((byte) 'R');
        buffer.putLong(999L);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        int initialPosition = buffer.position();

        orderCanceled.get(buffer);

        assertEquals(initialPosition + 33, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testOrderCanceledGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.putLong(123456L);
        buffer.putLong(7777L);
        byte[] testOrderId = "ORDER000000000AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put(POE.ORDER_CANCEL_REASON_SUPERVISORY);
        buffer.flip();

        buffer.getLong();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(7777L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(100L, orderCanceled.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_SUPERVISORY, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetReadsExactly33Bytes() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(5000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(33, buffer.position());
        assertFalse(buffer.hasRemaining());
    }

    @Test
    void testOrderCanceledGetCanBeCalledMultipleTimes() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        ByteBuffer buffer1 = ByteBuffer.allocate(33);
        buffer1.putLong(1111L);
        byte[] testOrderId1 = "FIRST00000000001".getBytes();
        buffer1.put(testOrderId1);
        buffer1.putLong(100L);
        buffer1.put((byte) 'R');
        buffer1.flip();

        orderCanceled.get(buffer1);

        assertEquals(1111L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId1, orderCanceled.orderId);
        assertEquals(100L, orderCanceled.canceledQuantity);
        assertEquals((byte) 'R', orderCanceled.reason);

        ByteBuffer buffer2 = ByteBuffer.allocate(33);
        buffer2.putLong(2222L);
        byte[] testOrderId2 = "SECOND0000000002".getBytes();
        buffer2.put(testOrderId2);
        buffer2.putLong(200L);
        buffer2.put((byte) 'S');
        buffer2.flip();

        orderCanceled.get(buffer2);

        assertEquals(2222L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId2, orderCanceled.orderId);
        assertEquals(200L, orderCanceled.canceledQuantity);
        assertEquals((byte) 'S', orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithRequestReasonConstant() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put(POE.ORDER_CANCEL_REASON_REQUEST);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, orderCanceled.reason);
        assertEquals((byte) 'R', orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithSupervisoryReasonConstant() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(2000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(200L);
        buffer.put(POE.ORDER_CANCEL_REASON_SUPERVISORY);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(POE.ORDER_CANCEL_REASON_SUPERVISORY, orderCanceled.reason);
        assertEquals((byte) 'S', orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithComplexOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(3000L);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) (i * 17);
        }
        buffer.put(testOrderId);
        buffer.putLong(555L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(3000L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(555L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithAlphanumericOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(4000L);
        byte[] testOrderId = "ORD123ABC456DEF7".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(1000L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(4000L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
    }

    @Test
    void testOrderCanceledGetPreservesOrderIdLength() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(5000L);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        testOrderId[15] = 15;
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(16, orderCanceled.orderId.length);
        assertEquals(1, orderCanceled.orderId[0]);
        assertEquals(15, orderCanceled.orderId[15]);
    }

    @Test
    void testOrderCanceledGetWithOneValues() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(1L);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        buffer.put(testOrderId);
        buffer.putLong(1L);
        buffer.put((byte) 1);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1L, orderCanceled.timestamp);
        assertEquals(1L, orderCanceled.canceledQuantity);
        assertEquals((byte) 1, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithLargePositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(9223372036854775806L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(9223372036854775805L);
        buffer.put((byte) 127);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(9223372036854775806L, orderCanceled.timestamp);
        assertEquals(9223372036854775805L, orderCanceled.canceledQuantity);
        assertEquals((byte) 127, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetReadsFieldsSequentially() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(6000L);
        byte[] testOrderId = "0123456789ABCDEF".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(300L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        int pos1 = buffer.position();
        orderCanceled.get(buffer);
        int pos2 = buffer.position();

        assertEquals(0, pos1);
        assertEquals(33, pos2);
        assertEquals(6000L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(300L, orderCanceled.canceledQuantity);
        assertEquals((byte) 'R', orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetDoesNotChangeBufferLimit() {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.putLong(7000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put((byte) 'R');
        buffer.flip();

        int originalLimit = buffer.limit();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(originalLimit, buffer.limit());
    }

    @Test
    void testOrderCanceledGetWithRealisticTradingScenario() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "TRD9999999999999".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(500L);
        buffer.put(POE.ORDER_CANCEL_REASON_REQUEST);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1609459200000L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(500L, orderCanceled.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, orderCanceled.reason);
    }

    // ========== OrderExecuted Get Tests ==========

    @Test
    void testOrderExecutedGetReadsTimestampFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1234567890000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.put((byte) 'A');
        buffer.putInt(300);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1234567890000L, orderExecuted.timestamp);
    }

    @Test
    void testOrderExecutedGetReadsOrderIdFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1000L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.put((byte) 'A');
        buffer.putInt(300);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertArrayEquals(testOrderId, orderExecuted.orderId);
    }

    @Test
    void testOrderExecutedGetReadsQuantityFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(54321L);
        buffer.putLong(200L);
        buffer.put((byte) 'A');
        buffer.putInt(300);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(54321L, orderExecuted.quantity);
    }

    @Test
    void testOrderExecutedGetReadsPriceFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.putLong(98765L);
        buffer.put((byte) 'A');
        buffer.putInt(300);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(98765L, orderExecuted.price);
    }

    @Test
    void testOrderExecutedGetReadsLiquidityFlagFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.put(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY);
        buffer.putInt(300);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, orderExecuted.liquidityFlag);
    }

    @Test
    void testOrderExecutedGetReadsMatchNumberFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.put((byte) 'A');
        buffer.putInt(12345);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(12345L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetReadsAllFieldsCorrectly() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(500L);
        buffer.putLong(10050L);
        buffer.put(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY);
        buffer.putInt(999);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1609459200000L, orderExecuted.timestamp);
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(500L, orderExecuted.quantity);
        assertEquals(10050L, orderExecuted.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, orderExecuted.liquidityFlag);
        assertEquals(999L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(0L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putInt(0);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(0L, orderExecuted.timestamp);
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(0L, orderExecuted.quantity);
        assertEquals(0L, orderExecuted.price);
        assertEquals((byte) 0, orderExecuted.liquidityFlag);
        assertEquals(0L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithMaxValues() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(Long.MAX_VALUE);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.put((byte) 0xFF);
        buffer.putInt(-1);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(Long.MAX_VALUE, orderExecuted.timestamp);
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(Long.MAX_VALUE, orderExecuted.quantity);
        assertEquals(Long.MAX_VALUE, orderExecuted.price);
        assertEquals((byte) 0xFF, orderExecuted.liquidityFlag);
        assertEquals(4294967295L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithUnsignedIntMaxMatchNumber() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.put((byte) 'A');
        buffer.putInt(-1);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(4294967295L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.put((byte) 'A');
        buffer.putInt(300);
        buffer.flip();

        assertEquals(0, buffer.position());

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(45, buffer.position());
    }

    @Test
    void testOrderExecutedGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(53);
        buffer.putLong(999L);
        buffer.putLong(1234567890000L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(500L);
        buffer.putLong(10050L);
        buffer.put(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY);
        buffer.putInt(12345);
        buffer.flip();

        buffer.getLong();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1234567890000L, orderExecuted.timestamp);
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(500L, orderExecuted.quantity);
        assertEquals(10050L, orderExecuted.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, orderExecuted.liquidityFlag);
        assertEquals(12345L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetOverwritesPreviousValues() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 999L;
        orderExecuted.orderId[0] = (byte) 99;
        orderExecuted.quantity = 999L;
        orderExecuted.price = 999L;
        orderExecuted.liquidityFlag = (byte) 99;
        orderExecuted.matchNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1000L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.put((byte) 'A');
        buffer.putInt(300);
        buffer.flip();

        orderExecuted.get(buffer);

        assertEquals(1000L, orderExecuted.timestamp);
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(100L, orderExecuted.quantity);
        assertEquals(200L, orderExecuted.price);
        assertEquals((byte) 'A', orderExecuted.liquidityFlag);
        assertEquals(300L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetCanBeCalledMultipleTimes() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        ByteBuffer buffer1 = ByteBuffer.allocate(45);
        buffer1.putLong(1000L);
        byte[] testOrderId1 = "ORDER111111111AA".getBytes();
        buffer1.put(testOrderId1);
        buffer1.putLong(100L);
        buffer1.putLong(200L);
        buffer1.put((byte) 'A');
        buffer1.putInt(300);
        buffer1.flip();

        orderExecuted.get(buffer1);

        assertEquals(1000L, orderExecuted.timestamp);
        assertArrayEquals(testOrderId1, orderExecuted.orderId);

        ByteBuffer buffer2 = ByteBuffer.allocate(45);
        buffer2.putLong(2000L);
        byte[] testOrderId2 = "ORDER222222222BB".getBytes();
        buffer2.put(testOrderId2);
        buffer2.putLong(400L);
        buffer2.putLong(500L);
        buffer2.put((byte) 'R');
        buffer2.putInt(600);
        buffer2.flip();

        orderExecuted.get(buffer2);

        assertEquals(2000L, orderExecuted.timestamp);
        assertArrayEquals(testOrderId2, orderExecuted.orderId);
        assertEquals(400L, orderExecuted.quantity);
        assertEquals(500L, orderExecuted.price);
        assertEquals((byte) 'R', orderExecuted.liquidityFlag);
        assertEquals(600L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithAddedLiquidityFlag() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "EXEC000000000001".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(250L);
        buffer.putLong(5000L);
        buffer.put(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY);
        buffer.putInt(777);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, orderExecuted.liquidityFlag);
        assertEquals((byte) 'A', orderExecuted.liquidityFlag);
    }

    @Test
    void testOrderExecutedGetWithRemovedLiquidityFlag() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "EXEC000000000002".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(250L);
        buffer.putLong(5000L);
        buffer.put(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY);
        buffer.putInt(888);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, orderExecuted.liquidityFlag);
        assertEquals((byte) 'R', orderExecuted.liquidityFlag);
    }

    @Test
    void testOrderExecutedGetWithMinPositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1L);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        buffer.put(testOrderId);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.put((byte) 1);
        buffer.putInt(1);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1L, orderExecuted.timestamp);
        assertEquals((byte) 1, orderExecuted.orderId[0]);
        assertEquals(1L, orderExecuted.quantity);
        assertEquals(1L, orderExecuted.price);
        assertEquals((byte) 1, orderExecuted.liquidityFlag);
        assertEquals(1L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithRealisticTradingScenario() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "TRD9999999999999".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(1000L);
        buffer.putLong(15075L);
        buffer.put(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY);
        buffer.putInt(123456);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1609459200000L, orderExecuted.timestamp);
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(1000L, orderExecuted.quantity);
        assertEquals(15075L, orderExecuted.price);
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, orderExecuted.liquidityFlag);
        assertEquals(123456L, orderExecuted.matchNumber);
    }

    // ========== OrderRejected Get Tests ==========

    @Test
    void testOrderRejectedGetReadsTimestampFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(1234567890L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'P');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(1234567890L, orderRejected.timestamp);
    }

    @Test
    void testOrderRejectedGetReadsOrderIdFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(123L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'I');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertArrayEquals(testOrderId, orderRejected.orderId);
    }

    @Test
    void testOrderRejectedGetReadsReasonFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(456L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(POE.ORDER_REJECT_REASON_INVALID_PRICE);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetReadsAllFieldsCorrectly() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(9876543210L);
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(9876543210L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithZeroTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(0L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'Q');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(0L, orderRejected.timestamp);
    }

    @Test
    void testOrderRejectedGetWithMaxTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(Long.MAX_VALUE);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'P');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(Long.MAX_VALUE, orderRejected.timestamp);
    }

    @Test
    void testOrderRejectedGetWithMinTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(Long.MIN_VALUE);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'I');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(Long.MIN_VALUE, orderRejected.timestamp);
    }

    @Test
    void testOrderRejectedGetWithNegativeTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(-12345L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'Q');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(-12345L, orderRejected.timestamp);
    }

    @Test
    void testOrderRejectedGetWithInvalidQuantityReason() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(555L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(POE.ORDER_REJECT_REASON_INVALID_QUANTITY);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, orderRejected.reason);
        assertEquals((byte) 'Q', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithUnknownInstrumentReason() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(777L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, orderRejected.reason);
        assertEquals((byte) 'I', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(100L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'P');
        buffer.putLong(999L);
        buffer.flip();

        assertEquals(0, buffer.position());

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(25, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testOrderRejectedGetOverwritesPreviousValues() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 9999L;
        byte[] oldOrderId = "OLD_ORDER_123456".getBytes();
        System.arraycopy(oldOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = (byte) 'X';

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(1111L);
        byte[] newOrderId = "NEW_ORDER_789012".getBytes();
        buffer.put(newOrderId);
        buffer.put((byte) 'P');
        buffer.flip();

        orderRejected.get(buffer);

        assertEquals(1111L, orderRejected.timestamp);
        assertArrayEquals(newOrderId, orderRejected.orderId);
        assertEquals((byte) 'P', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(888L);
        buffer.putLong(2222L);
        byte[] testOrderId = "REJECT0123456789".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'I');
        buffer.flip();

        buffer.getLong();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(2222L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals((byte) 'I', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithEmptyOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(333L);
        byte[] emptyOrderId = new byte[16];
        buffer.put(emptyOrderId);
        buffer.put((byte) 'Q');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertArrayEquals(emptyOrderId, orderRejected.orderId);
        for (int i = 0; i < 16; i++) {
            assertEquals(0, orderRejected.orderId[i]);
        }
    }

    @Test
    void testOrderRejectedGetWithSpecialCharacterOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(444L);
        byte[] specialOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            specialOrderId[i] = (byte) (i * 17);
        }
        buffer.put(specialOrderId);
        buffer.put((byte) 'P');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertArrayEquals(specialOrderId, orderRejected.orderId);
    }

    @Test
    void testOrderRejectedGetMultipleTimes() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        ByteBuffer buffer1 = ByteBuffer.allocate(25);
        buffer1.putLong(1000L);
        byte[] orderId1 = "FIRST_ORDER_1234".getBytes();
        buffer1.put(orderId1);
        buffer1.put((byte) 'I');
        buffer1.flip();

        orderRejected.get(buffer1);
        assertEquals(1000L, orderRejected.timestamp);
        assertArrayEquals(orderId1, orderRejected.orderId);
        assertEquals((byte) 'I', orderRejected.reason);

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        buffer2.putLong(2000L);
        byte[] orderId2 = "SECOND_ORDER_567".getBytes();
        buffer2.put(orderId2);
        buffer2.put((byte) 'P');
        buffer2.flip();

        orderRejected.get(buffer2);
        assertEquals(2000L, orderRejected.timestamp);
        assertArrayEquals(orderId2, orderRejected.orderId);
        assertEquals((byte) 'P', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetReadsInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(5555L);
        byte[] testOrderId = "CORRECTORDER1234".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'Q');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertEquals(0, buffer.position());
        orderRejected.get(buffer);
        assertEquals(25, buffer.position());

        assertEquals(5555L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals((byte) 'Q', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithZeroReason() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(666L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 0);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals((byte) 0, orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithMaxByteReason() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(888L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 0xFF);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals((byte) 0xFF, orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithOneValues() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(1L);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        buffer.put(testOrderId);
        buffer.put((byte) 1);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(1L, orderRejected.timestamp);
        assertEquals(1, testOrderId[0]);
        assertEquals((byte) 1, orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithRealisticTradingScenario() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "REJ0000000000001".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.ORDER_REJECT_REASON_INVALID_PRICE);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(1609459200000L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, orderRejected.reason);
    }
}
