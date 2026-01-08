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

class POEClaude_putTest {

    @Test
    void testPutWritesMessageType() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
    }

    @Test
    void testPutWritesOrderId() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
    }

    @Test
    void testPutWritesSide() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'S';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) 'S', buffer.get());
    }

    @Test
    void testPutWritesInstrument() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 12345L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.get();
        assertEquals(12345L, buffer.getLong());
    }

    @Test
    void testPutWritesQuantity() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 54321L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.get();
        buffer.getLong();
        assertEquals(54321L, buffer.getLong());
    }

    @Test
    void testPutWritesPrice() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 98765L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.get();
        buffer.getLong();
        buffer.getLong();
        assertEquals(98765L, buffer.getLong());
    }

    @Test
    void testPutWritesAllFieldsInCorrectOrder() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 111L;
        enterOrder.quantity = 222L;
        enterOrder.price = 333L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.BUY, buffer.get());
        assertEquals(111L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
        assertEquals(333L, buffer.getLong());
    }

    @Test
    void testPutWithZeroValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        enterOrder.side = (byte) 0;
        enterOrder.instrument = 0L;
        enterOrder.quantity = 0L;
        enterOrder.price = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 0, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void testPutWithMaxValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 0xFF;
        enterOrder.instrument = Long.MAX_VALUE;
        enterOrder.quantity = Long.MAX_VALUE;
        enterOrder.price = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 0xFF, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testPutWithMinValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        enterOrder.side = Byte.MIN_VALUE;
        enterOrder.instrument = Long.MIN_VALUE;
        enterOrder.quantity = Long.MIN_VALUE;
        enterOrder.price = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(Byte.MIN_VALUE, buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testPutWithNegativeValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        enterOrder.side = (byte) -1;
        enterOrder.instrument = -100L;
        enterOrder.quantity = -200L;
        enterOrder.price = -300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) -1, buffer.get());
        assertEquals(-100L, buffer.getLong());
        assertEquals(-200L, buffer.getLong());
        assertEquals(-300L, buffer.getLong());
    }

    @Test
    void testPutAdvancesBufferPosition() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 1L;
        enterOrder.quantity = 2L;
        enterOrder.price = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        int initialPosition = buffer.position();

        enterOrder.put(buffer);

        assertEquals(initialPosition + 42, buffer.position());
    }

    @Test
    void testPutWithBufferAtNonZeroPosition() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = POE.SELL;
        enterOrder.instrument = 50L;
        enterOrder.quantity = 100L;
        enterOrder.price = 200L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.putLong(999L);

        enterOrder.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.SELL, buffer.get());
        assertEquals(50L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
    }

    @Test
    void testPutWritesExactly42Bytes() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 1L;
        enterOrder.quantity = 2L;
        enterOrder.price = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);

        assertEquals(42, buffer.position());
        assertFalse(buffer.hasRemaining());
    }

    @Test
    void testPutCanBeCalledMultipleTimes() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "ORDER000000000AB".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 1L;
        enterOrder.quantity = 2L;
        enterOrder.price = 3L;

        ByteBuffer buffer1 = ByteBuffer.allocate(42);
        enterOrder.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'E', buffer1.get());
        byte[] readOrderId1 = new byte[16];
        buffer1.get(readOrderId1);
        assertArrayEquals(testOrderId, readOrderId1);

        ByteBuffer buffer2 = ByteBuffer.allocate(42);
        enterOrder.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'E', buffer2.get());
        byte[] readOrderId2 = new byte[16];
        buffer2.get(readOrderId2);
        assertArrayEquals(testOrderId, readOrderId2);
        assertEquals((byte) 'B', buffer2.get());
        assertEquals(1L, buffer2.getLong());
        assertEquals(2L, buffer2.getLong());
        assertEquals(3L, buffer2.getLong());
    }

    @Test
    void testPutWithBuySideConstant() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.BUY, buffer.get());
        assertEquals((byte) 'B', POE.BUY);
    }

    @Test
    void testPutWithSellSideConstant() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = POE.SELL;
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.SELL, buffer.get());
        assertEquals((byte) 'S', POE.SELL);
    }

    @Test
    void testPutWithComplexOrderId() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) (i * 17);
        }
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 555L;
        enterOrder.quantity = 666L;
        enterOrder.price = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        buffer.get();
        assertEquals(555L, buffer.getLong());
        assertEquals(666L, buffer.getLong());
        assertEquals(777L, buffer.getLong());
    }

    @Test
    void testPutWithAlphanumericOrderId() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "ORD123ABC456DEF7".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 1000L;
        enterOrder.quantity = 2000L;
        enterOrder.price = 3000L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
    }

    @Test
    void testPutPreservesOrderIdLength() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        testOrderId[15] = 15;
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 1L;
        enterOrder.quantity = 2L;
        enterOrder.price = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(16, readOrderId.length);
        assertEquals(1, readOrderId[0]);
        assertEquals(15, readOrderId[15]);
    }

    @Test
    void testPutWithOneValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 1;
        enterOrder.instrument = 1L;
        enterOrder.quantity = 1L;
        enterOrder.price = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) 1, buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
    }

    @Test
    void testPutWithLargePositiveValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        enterOrder.side = (byte) 127;
        enterOrder.instrument = 9223372036854775806L;
        enterOrder.quantity = 9223372036854775805L;
        enterOrder.price = 9223372036854775804L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) 127, buffer.get());
        assertEquals(9223372036854775806L, buffer.getLong());
        assertEquals(9223372036854775805L, buffer.getLong());
        assertEquals(9223372036854775804L, buffer.getLong());
    }

    @Test
    void testPutWritesFieldsSequentially() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "0123456789ABCDEF".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);

        int pos1 = buffer.position();
        enterOrder.put(buffer);
        int pos2 = buffer.position();

        assertEquals(0, pos1);
        assertEquals(42, pos2);

        buffer.flip();
        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 'B', buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
    }

    @Test
    void testPutDoesNotChangeBufferLimit() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 1L;
        enterOrder.quantity = 2L;
        enterOrder.price = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(100);
        int originalLimit = buffer.limit();

        enterOrder.put(buffer);

        assertEquals(originalLimit, buffer.limit());
    }

    @Test
    void testPutWithRealisticTradingScenario() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "TRD9999999999999".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 1234L;
        enterOrder.quantity = 500L;
        enterOrder.price = 9850L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.BUY, buffer.get());
        assertEquals(1234L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(9850L, buffer.getLong());
    }

    @Test
    void testPutWithMessageLengthConstant() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(POE.MESSAGE_LENGTH_ENTER_ORDER);
        enterOrder.put(buffer);

        assertEquals(POE.MESSAGE_LENGTH_ENTER_ORDER, buffer.position());
        assertEquals(42, buffer.position());
    }

    @Test
    void testPutWritesMessageTypeFirst() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);

        buffer.flip();
        byte firstByte = buffer.get();

        assertEquals((byte) 'E', firstByte);
    }

    @Test
    void testPutWritesCorrectMessageTypeConstant() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 200L;
        enterOrder.price = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();

        assertEquals((byte) 'E', messageType);
        assertEquals('E', messageType);
    }

    // ========== OrderAccepted Put Tests ==========

    @Test
    void testOrderAcceptedPutWritesMessageType() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 123L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
    }

    @Test
    void testOrderAcceptedPutWritesTimestamp() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 1234567890L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(1234567890L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWritesOrderId() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.timestamp = 123L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
    }

    @Test
    void testOrderAcceptedPutWritesSide() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 123L;
        orderAccepted.side = (byte) 'S';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) 'S', buffer.get());
    }

    @Test
    void testOrderAcceptedPutWritesInstrument() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 123L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 12345L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.get();
        assertEquals(12345L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWritesQuantity() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 123L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 54321L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.get();
        buffer.getLong();
        assertEquals(54321L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWritesPrice() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 123L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 98765L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.get();
        buffer.getLong();
        buffer.getLong();
        assertEquals(98765L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWritesOrderNumber() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 123L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 99999L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        assertEquals(99999L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWritesAllFieldsInCorrectOrder() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 1609459200000L;
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = POE.BUY;
        orderAccepted.instrument = 111L;
        orderAccepted.quantity = 222L;
        orderAccepted.price = 333L;
        orderAccepted.orderNumber = 444L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(1609459200000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.BUY, buffer.get());
        assertEquals(111L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
        assertEquals(333L, buffer.getLong());
        assertEquals(444L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithZeroValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = new byte[16];
        orderAccepted.timestamp = 0L;
        orderAccepted.side = (byte) 0;
        orderAccepted.instrument = 0L;
        orderAccepted.quantity = 0L;
        orderAccepted.price = 0L;
        orderAccepted.orderNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(0L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 0, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithMaxValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.timestamp = Long.MAX_VALUE;
        orderAccepted.side = (byte) 0xFF;
        orderAccepted.instrument = Long.MAX_VALUE;
        orderAccepted.quantity = Long.MAX_VALUE;
        orderAccepted.price = Long.MAX_VALUE;
        orderAccepted.orderNumber = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 0xFF, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithMinValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = new byte[16];
        orderAccepted.timestamp = Long.MIN_VALUE;
        orderAccepted.side = Byte.MIN_VALUE;
        orderAccepted.instrument = Long.MIN_VALUE;
        orderAccepted.quantity = Long.MIN_VALUE;
        orderAccepted.price = Long.MIN_VALUE;
        orderAccepted.orderNumber = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(Byte.MIN_VALUE, buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithNegativeValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = new byte[16];
        orderAccepted.timestamp = -1000L;
        orderAccepted.side = (byte) -1;
        orderAccepted.instrument = -100L;
        orderAccepted.quantity = -200L;
        orderAccepted.price = -300L;
        orderAccepted.orderNumber = -400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(-1000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) -1, buffer.get());
        assertEquals(-100L, buffer.getLong());
        assertEquals(-200L, buffer.getLong());
        assertEquals(-300L, buffer.getLong());
        assertEquals(-400L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutAdvancesBufferPosition() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 111L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 1L;
        orderAccepted.quantity = 2L;
        orderAccepted.price = 3L;
        orderAccepted.orderNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(65);
        int initialPosition = buffer.position();

        orderAccepted.put(buffer);

        assertEquals(initialPosition + 58, buffer.position());
    }

    @Test
    void testOrderAcceptedPutWithBufferAtNonZeroPosition() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 8888L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = POE.SELL;
        orderAccepted.instrument = 50L;
        orderAccepted.quantity = 100L;
        orderAccepted.price = 200L;
        orderAccepted.orderNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(66);
        buffer.putLong(999L);

        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'A', buffer.get());
        assertEquals(8888L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.SELL, buffer.get());
        assertEquals(50L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWritesExactly58Bytes() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 5000L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 1L;
        orderAccepted.quantity = 2L;
        orderAccepted.price = 3L;
        orderAccepted.orderNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);

        assertEquals(58, buffer.position());
        assertFalse(buffer.hasRemaining());
    }

    @Test
    void testOrderAcceptedPutCanBeCalledMultipleTimes() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 7777L;
        byte[] testOrderId = "ORDER000000000AB".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 1L;
        orderAccepted.quantity = 2L;
        orderAccepted.price = 3L;
        orderAccepted.orderNumber = 10L;

        ByteBuffer buffer1 = ByteBuffer.allocate(58);
        orderAccepted.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'A', buffer1.get());
        assertEquals(7777L, buffer1.getLong());
        byte[] readOrderId1 = new byte[16];
        buffer1.get(readOrderId1);
        assertArrayEquals(testOrderId, readOrderId1);

        ByteBuffer buffer2 = ByteBuffer.allocate(58);
        orderAccepted.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'A', buffer2.get());
        assertEquals(7777L, buffer2.getLong());
        byte[] readOrderId2 = new byte[16];
        buffer2.get(readOrderId2);
        assertArrayEquals(testOrderId, readOrderId2);
        assertEquals((byte) 'B', buffer2.get());
        assertEquals(1L, buffer2.getLong());
        assertEquals(2L, buffer2.getLong());
        assertEquals(3L, buffer2.getLong());
        assertEquals(10L, buffer2.getLong());
    }

    @Test
    void testOrderAcceptedPutWithBuySideConstant() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 1000L;
        orderAccepted.side = POE.BUY;
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.BUY, buffer.get());
        assertEquals((byte) 'B', POE.BUY);
    }

    @Test
    void testOrderAcceptedPutWithSellSideConstant() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 2000L;
        orderAccepted.side = POE.SELL;
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.SELL, buffer.get());
        assertEquals((byte) 'S', POE.SELL);
    }

    @Test
    void testOrderAcceptedPutWithComplexOrderId() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 3000L;
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) (i * 17);
        }
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 555L;
        orderAccepted.quantity = 666L;
        orderAccepted.price = 777L;
        orderAccepted.orderNumber = 888L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        buffer.get();
        assertEquals(555L, buffer.getLong());
        assertEquals(666L, buffer.getLong());
        assertEquals(777L, buffer.getLong());
        assertEquals(888L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithAlphanumericOrderId() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 4000L;
        byte[] testOrderId = "ORD123ABC456DEF7".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 1000L;
        orderAccepted.quantity = 2000L;
        orderAccepted.price = 3000L;
        orderAccepted.orderNumber = 4000L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
    }

    @Test
    void testOrderAcceptedPutPreservesOrderIdLength() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 5000L;
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        testOrderId[15] = 15;
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 1L;
        orderAccepted.quantity = 2L;
        orderAccepted.price = 3L;
        orderAccepted.orderNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(16, readOrderId.length);
        assertEquals(1, readOrderId[0]);
        assertEquals(15, readOrderId[15]);
    }

    @Test
    void testOrderAcceptedPutWithOneValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.timestamp = 1L;
        orderAccepted.side = (byte) 1;
        orderAccepted.instrument = 1L;
        orderAccepted.quantity = 1L;
        orderAccepted.price = 1L;
        orderAccepted.orderNumber = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(1L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) 1, buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithLargePositiveValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = new byte[16];
        orderAccepted.timestamp = 9223372036854775807L;
        orderAccepted.side = (byte) 127;
        orderAccepted.instrument = 9223372036854775806L;
        orderAccepted.quantity = 9223372036854775805L;
        orderAccepted.price = 9223372036854775804L;
        orderAccepted.orderNumber = 9223372036854775803L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(9223372036854775807L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) 127, buffer.get());
        assertEquals(9223372036854775806L, buffer.getLong());
        assertEquals(9223372036854775805L, buffer.getLong());
        assertEquals(9223372036854775804L, buffer.getLong());
        assertEquals(9223372036854775803L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWritesFieldsSequentially() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 6000L;
        byte[] testOrderId = "0123456789ABCDEF".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);

        int pos1 = buffer.position();
        orderAccepted.put(buffer);
        int pos2 = buffer.position();

        assertEquals(0, pos1);
        assertEquals(58, pos2);

        buffer.flip();
        assertEquals((byte) 'A', buffer.get());
        assertEquals(6000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 'B', buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
        assertEquals(400L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutDoesNotChangeBufferLimit() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 7000L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 1L;
        orderAccepted.quantity = 2L;
        orderAccepted.price = 3L;
        orderAccepted.orderNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(100);
        int originalLimit = buffer.limit();

        orderAccepted.put(buffer);

        assertEquals(originalLimit, buffer.limit());
    }

    @Test
    void testOrderAcceptedPutWithRealisticTradingScenario() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 1609459200000L;
        byte[] testOrderId = "TRD9999999999999".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = POE.BUY;
        orderAccepted.instrument = 1234L;
        orderAccepted.quantity = 500L;
        orderAccepted.price = 9850L;
        orderAccepted.orderNumber = 55555L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(1609459200000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.BUY, buffer.get());
        assertEquals(1234L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(9850L, buffer.getLong());
        assertEquals(55555L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithMessageLengthConstant() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 100L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(POE.MESSAGE_LENGTH_ORDER_ACCEPTED);
        orderAccepted.put(buffer);

        assertEquals(POE.MESSAGE_LENGTH_ORDER_ACCEPTED, buffer.position());
        assertEquals(58, buffer.position());
    }

    @Test
    void testOrderAcceptedPutWritesMessageTypeFirst() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 222L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);

        buffer.flip();
        byte firstByte = buffer.get();

        assertEquals((byte) 'A', firstByte);
    }

    @Test
    void testOrderAcceptedPutWritesCorrectMessageTypeConstant() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 333L;
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 200L;
        orderAccepted.price = 300L;
        orderAccepted.orderNumber = 400L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();

        assertEquals((byte) 'A', messageType);
        assertEquals('A', messageType);
    }

    // ========== OrderCanceled Put Tests ==========

    @Test
    void testOrderCanceledPutWritesMessageType() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 123L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
    }

    @Test
    void testOrderCanceledPutWritesTimestamp() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 1234567890L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(1234567890L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWritesOrderId() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.timestamp = 123L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
    }

    @Test
    void testOrderCanceledPutWritesCanceledQuantity() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 123L;
        orderCanceled.canceledQuantity = 54321L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(54321L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWritesReason() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 123L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'S';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.getLong();
        assertEquals((byte) 'S', buffer.get());
    }

    @Test
    void testOrderCanceledPutWritesAllFieldsInCorrectOrder() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 1609459200000L;
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 222L;
        orderCanceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(1609459200000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(222L, buffer.getLong());
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, buffer.get());
    }

    @Test
    void testOrderCanceledPutWithZeroValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        byte[] testOrderId = new byte[16];
        orderCanceled.timestamp = 0L;
        orderCanceled.canceledQuantity = 0L;
        orderCanceled.reason = (byte) 0;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(0L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(0L, buffer.getLong());
        assertEquals((byte) 0, buffer.get());
    }

    @Test
    void testOrderCanceledPutWithMaxValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.timestamp = Long.MAX_VALUE;
        orderCanceled.canceledQuantity = Long.MAX_VALUE;
        orderCanceled.reason = (byte) 0xFF;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals((byte) 0xFF, buffer.get());
    }

    @Test
    void testOrderCanceledPutWithMinValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        byte[] testOrderId = new byte[16];
        orderCanceled.timestamp = Long.MIN_VALUE;
        orderCanceled.canceledQuantity = Long.MIN_VALUE;
        orderCanceled.reason = Byte.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Byte.MIN_VALUE, buffer.get());
    }

    @Test
    void testOrderCanceledPutWithNegativeValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        byte[] testOrderId = new byte[16];
        orderCanceled.timestamp = -1000L;
        orderCanceled.canceledQuantity = -200L;
        orderCanceled.reason = (byte) -1;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(-1000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(-200L, buffer.getLong());
        assertEquals((byte) -1, buffer.get());
    }

    @Test
    void testOrderCanceledPutAdvancesBufferPosition() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 111L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(42);
        int initialPosition = buffer.position();

        orderCanceled.put(buffer);

        assertEquals(initialPosition + 34, buffer.position());
    }

    @Test
    void testOrderCanceledPutWithBufferAtNonZeroPosition() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 8888L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = POE.ORDER_CANCEL_REASON_SUPERVISORY;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.putLong(999L);

        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'X', buffer.get());
        assertEquals(8888L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(100L, buffer.getLong());
        assertEquals(POE.ORDER_CANCEL_REASON_SUPERVISORY, buffer.get());
    }

    @Test
    void testOrderCanceledPutWritesExactly34Bytes() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 5000L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);

        assertEquals(34, buffer.position());
        assertFalse(buffer.hasRemaining());
    }

    @Test
    void testOrderCanceledPutCanBeCalledMultipleTimes() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 7777L;
        byte[] testOrderId = "ORDER000000000AB".getBytes();
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer1 = ByteBuffer.allocate(34);
        orderCanceled.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'X', buffer1.get());
        assertEquals(7777L, buffer1.getLong());
        byte[] readOrderId1 = new byte[16];
        buffer1.get(readOrderId1);
        assertArrayEquals(testOrderId, readOrderId1);

        ByteBuffer buffer2 = ByteBuffer.allocate(34);
        orderCanceled.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'X', buffer2.get());
        assertEquals(7777L, buffer2.getLong());
        byte[] readOrderId2 = new byte[16];
        buffer2.get(readOrderId2);
        assertArrayEquals(testOrderId, readOrderId2);
        assertEquals(100L, buffer2.getLong());
        assertEquals((byte) 'R', buffer2.get());
    }

    @Test
    void testOrderCanceledPutWithRequestReasonConstant() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 1000L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.getLong();
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, buffer.get());
        assertEquals((byte) 'R', POE.ORDER_CANCEL_REASON_REQUEST);
    }

    @Test
    void testOrderCanceledPutWithSupervisoryReasonConstant() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 2000L;
        orderCanceled.canceledQuantity = 200L;
        orderCanceled.reason = POE.ORDER_CANCEL_REASON_SUPERVISORY;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        buffer.getLong();
        assertEquals(POE.ORDER_CANCEL_REASON_SUPERVISORY, buffer.get());
        assertEquals((byte) 'S', POE.ORDER_CANCEL_REASON_SUPERVISORY);
    }

    @Test
    void testOrderCanceledPutWithComplexOrderId() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 3000L;
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) (i * 17);
        }
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 555L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(555L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithAlphanumericOrderId() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 4000L;
        byte[] testOrderId = "ORD123ABC456DEF7".getBytes();
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 1000L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
    }

    @Test
    void testOrderCanceledPutPreservesOrderIdLength() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 5000L;
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        testOrderId[15] = 15;
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(16, readOrderId.length);
        assertEquals(1, readOrderId[0]);
        assertEquals(15, readOrderId[15]);
    }

    @Test
    void testOrderCanceledPutWithOneValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.timestamp = 1L;
        orderCanceled.canceledQuantity = 1L;
        orderCanceled.reason = (byte) 1;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(1L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(1L, buffer.getLong());
        assertEquals((byte) 1, buffer.get());
    }

    @Test
    void testOrderCanceledPutWithLargePositiveValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        byte[] testOrderId = new byte[16];
        orderCanceled.timestamp = 9223372036854775807L;
        orderCanceled.canceledQuantity = 9223372036854775806L;
        orderCanceled.reason = (byte) 127;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(9223372036854775807L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(9223372036854775806L, buffer.getLong());
        assertEquals((byte) 127, buffer.get());
    }

    @Test
    void testOrderCanceledPutWritesFieldsSequentially() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 6000L;
        byte[] testOrderId = "0123456789ABCDEF".getBytes();
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 300L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);

        int pos1 = buffer.position();
        orderCanceled.put(buffer);
        int pos2 = buffer.position();

        assertEquals(0, pos1);
        assertEquals(34, pos2);

        buffer.flip();
        assertEquals((byte) 'X', buffer.get());
        assertEquals(6000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(300L, buffer.getLong());
        assertEquals((byte) 'R', buffer.get());
    }

    @Test
    void testOrderCanceledPutDoesNotChangeBufferLimit() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 7000L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(100);
        int originalLimit = buffer.limit();

        orderCanceled.put(buffer);

        assertEquals(originalLimit, buffer.limit());
    }

    @Test
    void testOrderCanceledPutWithRealisticTradingScenario() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 1609459200000L;
        byte[] testOrderId = "TRD9999999999999".getBytes();
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 500L;
        orderCanceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(1609459200000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(500L, buffer.getLong());
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, buffer.get());
    }

    @Test
    void testOrderCanceledPutWithMessageLengthConstant() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 100L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(POE.MESSAGE_LENGTH_ORDER_CANCELED);
        orderCanceled.put(buffer);

        assertEquals(POE.MESSAGE_LENGTH_ORDER_CANCELED, buffer.position());
        assertEquals(34, buffer.position());
    }

    @Test
    void testOrderCanceledPutWritesMessageTypeFirst() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 222L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);

        buffer.flip();
        byte firstByte = buffer.get();

        assertEquals((byte) 'X', firstByte);
    }

    @Test
    void testOrderCanceledPutWritesCorrectMessageTypeConstant() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 333L;
        orderCanceled.canceledQuantity = 100L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();

        assertEquals((byte) 'X', messageType);
        assertEquals('X', messageType);
    }

    // ========== OrderExecuted Put Tests ==========

    @Test
    void testOrderExecutedPutWritesMessageType() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
    }

    @Test
    void testOrderExecutedPutWritesTimestamp() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1234567890000L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(1234567890000L, buffer.getLong());
    }

    @Test
    void testOrderExecutedPutWritesOrderId() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
    }

    @Test
    void testOrderExecutedPutWritesQuantity() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 54321L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        assertEquals(54321L, buffer.getLong());
    }

    @Test
    void testOrderExecutedPutWritesPrice() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 98765L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        buffer.getLong();
        assertEquals(98765L, buffer.getLong());
    }

    @Test
    void testOrderExecutedPutWritesLiquidityFlag() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        buffer.getLong();
        buffer.getLong();
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, buffer.get());
    }

    @Test
    void testOrderExecutedPutWritesMatchNumber() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 12345L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        buffer.getLong();
        buffer.getLong();
        buffer.get();
        assertEquals(12345, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWritesAllFieldsCorrectly() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1609459200000L;
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 500L;
        orderExecuted.price = 10050L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        orderExecuted.matchNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1609459200000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(500L, buffer.getLong());
        assertEquals(10050L, buffer.getLong());
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, buffer.get());
        assertEquals(999, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithZeroValues() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 0L;
        orderExecuted.quantity = 0L;
        orderExecuted.price = 0L;
        orderExecuted.liquidityFlag = (byte) 0;
        orderExecuted.matchNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(0L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals((byte) 0, buffer.get());
        assertEquals(0, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithMaxValues() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = Long.MAX_VALUE;
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = Long.MAX_VALUE;
        orderExecuted.price = Long.MAX_VALUE;
        orderExecuted.liquidityFlag = (byte) 0xFF;
        orderExecuted.matchNumber = 4294967295L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals((byte) 0xFF, buffer.get());
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithUnsignedIntMaxMatchNumber() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 1L;
        orderExecuted.price = 1L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 4294967295L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        buffer.getLong();
        buffer.getLong();
        buffer.get();
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutAdvancesBufferPosition() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        assertEquals(0, buffer.position());

        orderExecuted.put(buffer);

        assertEquals(46, buffer.position());
    }

    @Test
    void testOrderExecutedPutWithBufferAtNonZeroPosition() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1234567890000L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 500L;
        orderExecuted.price = 10050L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        orderExecuted.matchNumber = 12345L;

        ByteBuffer buffer = ByteBuffer.allocate(54);
        buffer.putLong(999L);

        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'E', buffer.get());
        assertEquals(1234567890000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(500L, buffer.getLong());
        assertEquals(10050L, buffer.getLong());
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, buffer.get());
        assertEquals(12345, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutCanBeCalledMultipleTimes() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer1 = ByteBuffer.allocate(46);
        orderExecuted.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'E', buffer1.get());
        assertEquals(1000L, buffer1.getLong());

        ByteBuffer buffer2 = ByteBuffer.allocate(46);
        orderExecuted.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'E', buffer2.get());
        assertEquals(1000L, buffer2.getLong());
    }

    @Test
    void testOrderExecutedPutWithAddedLiquidityFlag() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1609459200000L;
        byte[] testOrderId = "EXEC000000000001".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 250L;
        orderExecuted.price = 5000L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        orderExecuted.matchNumber = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        buffer.getLong();
        buffer.getLong();
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, buffer.get());
        assertEquals((byte) 'A', POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY);
    }

    @Test
    void testOrderExecutedPutWithRemovedLiquidityFlag() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1609459200000L;
        byte[] testOrderId = "EXEC000000000002".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 250L;
        orderExecuted.price = 5000L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        orderExecuted.matchNumber = 888L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        buffer.getLong();
        buffer.getLong();
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, buffer.get());
        assertEquals((byte) 'R', POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY);
    }

    @Test
    void testOrderExecutedPutWithMinPositiveValues() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1L;
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 1L;
        orderExecuted.price = 1L;
        orderExecuted.liquidityFlag = (byte) 1;
        orderExecuted.matchNumber = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(1, readOrderId[0]);
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals((byte) 1, buffer.get());
        assertEquals(1, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutRoundTripWithGet() {
        POE.OrderExecuted original = new POE.OrderExecuted();
        original.timestamp = 1609459200000L;
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.quantity = 250L;
        original.price = 5000L;
        original.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        original.matchNumber = 888L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        original.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());

        POE.OrderExecuted recovered = new POE.OrderExecuted();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
        assertEquals(original.liquidityFlag, recovered.liquidityFlag);
        assertEquals(original.matchNumber, recovered.matchNumber);
    }

    @Test
    void testOrderExecutedPutWritesCorrectMessageLength() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(100);
        int startPosition = buffer.position();

        orderExecuted.put(buffer);

        int endPosition = buffer.position();
        assertEquals(46, endPosition - startPosition);
    }

    @Test
    void testOrderExecutedPutWritesCorrectMessageTypeConstant() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 100L;
        orderExecuted.price = 200L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();

        assertEquals((byte) 'E', messageType);
        assertEquals('E', messageType);
    }

    @Test
    void testOrderExecutedPutWithRealisticTradingScenario() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1609459200000L;
        byte[] testOrderId = "TRD9999999999999".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 1000L;
        orderExecuted.price = 15075L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        orderExecuted.matchNumber = 123456L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1609459200000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(1000L, buffer.getLong());
        assertEquals(15075L, buffer.getLong());
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, buffer.get());
        assertEquals(123456, buffer.getInt());
    }

    // ========== OrderRejected Put Tests ==========

    @Test
    void testOrderRejectedPutWritesMessageType() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 100L;
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
    }

    @Test
    void testOrderRejectedPutWritesTimestamp() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 1234567890L;
        orderRejected.reason = (byte) 'I';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(1234567890L, buffer.getLong());
    }

    @Test
    void testOrderRejectedPutWritesOrderId() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.timestamp = 555L;
        orderRejected.reason = (byte) 'Q';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
    }

    @Test
    void testOrderRejectedPutWritesReason() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 777L;
        orderRejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, buffer.get());
    }

    @Test
    void testOrderRejectedPutWritesAllFieldsInOrder() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 9876543210L;
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        System.arraycopy(testOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(9876543210L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, buffer.get());
    }

    @Test
    void testOrderRejectedPutWithZeroTimestamp() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 0L;
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void testOrderRejectedPutWithMaxTimestamp() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = Long.MAX_VALUE;
        orderRejected.reason = (byte) 'I';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testOrderRejectedPutWithMinTimestamp() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = Long.MIN_VALUE;
        orderRejected.reason = (byte) 'Q';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testOrderRejectedPutWithNegativeTimestamp() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = -12345L;
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(-12345L, buffer.getLong());
    }

    @Test
    void testOrderRejectedPutWithInvalidQuantityReason() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 123L;
        orderRejected.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, buffer.get());
        assertEquals((byte) 'Q', POE.ORDER_REJECT_REASON_INVALID_QUANTITY);
    }

    @Test
    void testOrderRejectedPutWithUnknownInstrumentReason() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 456L;
        orderRejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, buffer.get());
        assertEquals((byte) 'I', POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT);
    }

    @Test
    void testOrderRejectedPutAdvancesBufferPosition() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 50L;
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(30);
        assertEquals(0, buffer.position());

        orderRejected.put(buffer);

        assertEquals(26, buffer.position());
    }

    @Test
    void testOrderRejectedPutWithBufferAtNonZeroPosition() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 5678L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = (byte) 'I';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        buffer.putLong(999L);

        orderRejected.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'R', buffer.get());
        assertEquals(5678L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 'I', buffer.get());
    }

    @Test
    void testOrderRejectedPutWithEmptyOrderId() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 333L;
        orderRejected.reason = (byte) 'Q';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        byte[] expectedEmptyId = new byte[16];
        assertArrayEquals(expectedEmptyId, readOrderId);
    }

    @Test
    void testOrderRejectedPutWithSpecialCharacterOrderId() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 444L;
        byte[] specialOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            specialOrderId[i] = (byte) (i * 17);
        }
        System.arraycopy(specialOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(specialOrderId, readOrderId);
    }

    @Test
    void testOrderRejectedPutMultipleTimes() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 7777L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = (byte) 'I';

        ByteBuffer buffer1 = ByteBuffer.allocate(26);
        orderRejected.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'R', buffer1.get());
        assertEquals(7777L, buffer1.getLong());
        byte[] readOrderId1 = new byte[16];
        buffer1.get(readOrderId1);
        assertArrayEquals(testOrderId, readOrderId1);
        assertEquals((byte) 'I', buffer1.get());

        ByteBuffer buffer2 = ByteBuffer.allocate(26);
        orderRejected.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'R', buffer2.get());
        assertEquals(7777L, buffer2.getLong());
        byte[] readOrderId2 = new byte[16];
        buffer2.get(readOrderId2);
        assertArrayEquals(testOrderId, readOrderId2);
        assertEquals((byte) 'I', buffer2.get());
    }

    @Test
    void testOrderRejectedPutWithZeroReason() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 666L;
        orderRejected.reason = (byte) 0;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        assertEquals((byte) 0, buffer.get());
    }

    @Test
    void testOrderRejectedPutWithMaxByteReason() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 888L;
        orderRejected.reason = (byte) 0xFF;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] orderId = new byte[16];
        buffer.get(orderId);
        assertEquals((byte) 0xFF, buffer.get());
    }

    @Test
    void testOrderRejectedPutWithOneValues() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 1L;
        orderRejected.reason = (byte) 1;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(1L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) 1, buffer.get());
    }

    @Test
    void testOrderRejectedPutMessageTypeMatchesConstant() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 100L;
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();

        assertEquals((byte) 'R', messageType);
        assertEquals('R', messageType);
    }

    @Test
    void testOrderRejectedPutWithRealisticTradingScenario() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 1609459200000L;
        byte[] testOrderId = "REJ0000000000001".getBytes();
        System.arraycopy(testOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(1609459200000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, buffer.get());
    }
}
