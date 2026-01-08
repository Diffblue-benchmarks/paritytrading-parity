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

class POEClaudeTest {

    @Test
    void testCancelOrderConstructorInitializesOrderIdArray() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();

        assertNotNull(cancelOrder.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, cancelOrder.orderId.length);
        assertEquals(16, cancelOrder.orderId.length);
    }

    @Test
    void testCancelOrderConstructorInitializesQuantityToZero() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();

        assertEquals(0L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderConstructorCreatesNewArray() {
        POE.CancelOrder cancelOrder1 = new POE.CancelOrder();
        POE.CancelOrder cancelOrder2 = new POE.CancelOrder();

        assertNotSame(cancelOrder1.orderId, cancelOrder2.orderId);
    }

    @Test
    void testCancelOrderGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) i;
        }
        buffer.put(testOrderId);
        buffer.putLong(1000L);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(1000L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(0L);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(0L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetWithMaxLongValue() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(Long.MAX_VALUE, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetWithMinLongValue() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(Long.MIN_VALUE, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetWithNegativeQuantity() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(-500L);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(-500L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetWithAlphanumericOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(2500L);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(2500L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetMultipleTimes() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();

        ByteBuffer buffer1 = ByteBuffer.allocate(24);
        byte[] testOrderId1 = "ORDER1234567890A".getBytes();
        buffer1.put(testOrderId1);
        buffer1.putLong(100L);
        buffer1.flip();

        cancelOrder.get(buffer1);
        assertArrayEquals(testOrderId1, cancelOrder.orderId);
        assertEquals(100L, cancelOrder.quantity);

        ByteBuffer buffer2 = ByteBuffer.allocate(24);
        byte[] testOrderId2 = "ORDER9876543210B".getBytes();
        buffer2.put(testOrderId2);
        buffer2.putLong(200L);
        buffer2.flip();

        cancelOrder.get(buffer2);
        assertArrayEquals(testOrderId2, cancelOrder.orderId);
        assertEquals(200L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetOverwritesPreviousValues() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] initialOrderId = "INITIAL123456789".getBytes();
        System.arraycopy(initialOrderId, 0, cancelOrder.orderId, 0, 16);
        cancelOrder.quantity = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] newOrderId = "NEWORDER12345678".getBytes();
        buffer.put(newOrderId);
        buffer.putLong(111L);
        buffer.flip();

        cancelOrder.get(buffer);

        assertArrayEquals(newOrderId, cancelOrder.orderId);
        assertEquals(111L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(500L);
        buffer.putLong(999L);
        buffer.flip();

        assertEquals(0, buffer.position());

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertEquals(24, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testCancelOrderGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.putLong(999L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(50L);
        buffer.flip();

        buffer.getLong();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(50L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetWithOne() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = new byte[16];
        testOrderId[0] = 1;
        buffer.put(testOrderId);
        buffer.putLong(1L);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(1L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderGetWithLargePositiveQuantity() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.putLong(9223372036854775807L);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.get(buffer);

        assertEquals(9223372036854775807L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderPutWithBasicValues() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, cancelOrder.orderId, 0, 16);
        cancelOrder.quantity = 1000L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(1000L, buffer.getLong());
    }

    @Test
    void testCancelOrderPutWithZeroQuantity() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = new byte[16];
        cancelOrder.quantity = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void testCancelOrderPutWithMaxLongValue() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, cancelOrder.orderId, 0, 16);
        cancelOrder.quantity = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testCancelOrderPutWithMinLongValue() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = new byte[16];
        cancelOrder.quantity = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testCancelOrderPutWithNegativeQuantity() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = new byte[16];
        cancelOrder.quantity = -500L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(-500L, buffer.getLong());
    }

    @Test
    void testCancelOrderPutMultipleTimes() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, cancelOrder.orderId, 0, 16);
        cancelOrder.quantity = 100L;

        ByteBuffer buffer1 = ByteBuffer.allocate(25);
        cancelOrder.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'X', buffer1.get());
        byte[] readOrderId1 = new byte[16];
        buffer1.get(readOrderId1);
        assertArrayEquals(testOrderId, readOrderId1);
        assertEquals(100L, buffer1.getLong());

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        cancelOrder.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'X', buffer2.get());
        byte[] readOrderId2 = new byte[16];
        buffer2.get(readOrderId2);
        assertArrayEquals(testOrderId, readOrderId2);
        assertEquals(100L, buffer2.getLong());
    }

    @Test
    void testCancelOrderPutAdvancesBufferPosition() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.quantity = 50L;

        ByteBuffer buffer = ByteBuffer.allocate(30);
        assertEquals(0, buffer.position());

        cancelOrder.put(buffer);

        assertEquals(25, buffer.position());
    }

    @Test
    void testCancelOrderPutWritesMessageType() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.quantity = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals((byte) 'X', messageType);
    }

    @Test
    void testCancelOrderPutWithBufferAtNonZeroPosition() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, cancelOrder.orderId, 0, 16);
        cancelOrder.quantity = 50L;

        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(999L);

        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(50L, buffer.getLong());
    }

    @Test
    void testCancelOrderPutWithLargePositiveQuantity() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.quantity = 9223372036854775807L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(9223372036854775807L, buffer.getLong());
    }

    @Test
    void testCancelOrderPutWithOne() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.quantity = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(1L, buffer.getLong());
    }

    @Test
    void testCancelOrderGetAndPutRoundTrip() {
        POE.CancelOrder original = new POE.CancelOrder();
        byte[] testOrderId = "ORDER987654321XY".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.quantity = 123456L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.CancelOrder recovered = new POE.CancelOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.quantity, recovered.quantity);
    }

    @Test
    void testCancelOrderGetAndPutRoundTripWithMaxValues() {
        POE.CancelOrder original = new POE.CancelOrder();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.quantity = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.CancelOrder recovered = new POE.CancelOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.quantity, recovered.quantity);
    }

    @Test
    void testCancelOrderGetAndPutRoundTripWithMinValues() {
        POE.CancelOrder original = new POE.CancelOrder();
        byte[] testOrderId = new byte[16];
        original.quantity = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.CancelOrder recovered = new POE.CancelOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.quantity, recovered.quantity);
    }

    @Test
    void testCancelOrderGetAndPutRoundTripWithZeroValues() {
        POE.CancelOrder original = new POE.CancelOrder();
        byte[] testOrderId = new byte[16];
        original.quantity = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.CancelOrder recovered = new POE.CancelOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.quantity, recovered.quantity);
    }

    @Test
    void testCancelOrderPutWritesFieldsInCorrectOrder() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = "ORDER111222333AB".getBytes();
        System.arraycopy(testOrderId, 0, cancelOrder.orderId, 0, 16);
        cancelOrder.quantity = 555L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(555L, buffer.getLong());
    }

    @Test
    void testCancelOrderGetReadsAllFields() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = "ORDER444555666AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(777L);
        buffer.flip();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();

        byte[] initialOrderId = new byte[16];
        assertArrayEquals(initialOrderId, cancelOrder.orderId);
        assertEquals(0L, cancelOrder.quantity);

        cancelOrder.get(buffer);

        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(777L, cancelOrder.quantity);
    }

    @Test
    void testCancelOrderWithMessageLengthConstant() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.quantity = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(POE.MESSAGE_LENGTH_CANCEL_ORDER);
        cancelOrder.put(buffer);

        assertEquals(POE.MESSAGE_LENGTH_CANCEL_ORDER, buffer.position());
        assertEquals(25, buffer.position());
    }

    // ========== EnterOrder Tests ==========

    @Test
    void testEnterOrderConstructorInitializesOrderIdArray() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertNotNull(enterOrder.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, enterOrder.orderId.length);
        assertEquals(16, enterOrder.orderId.length);
    }

    @Test
    void testEnterOrderConstructorInitializesFieldsToDefault() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertEquals(0, enterOrder.side);
        assertEquals(0L, enterOrder.instrument);
        assertEquals(0L, enterOrder.quantity);
        assertEquals(0L, enterOrder.price);
    }

    @Test
    void testEnterOrderConstructorCreatesNewArray() {
        POE.EnterOrder enterOrder1 = new POE.EnterOrder();
        POE.EnterOrder enterOrder2 = new POE.EnterOrder();

        assertNotSame(enterOrder1.orderId, enterOrder2.orderId);
    }

    @Test
    void testEnterOrderGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) i;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(500L);
        buffer.putLong(10000L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals((byte) 'B', enterOrder.side);
        assertEquals(100L, enterOrder.instrument);
        assertEquals(500L, enterOrder.quantity);
        assertEquals(10000L, enterOrder.price);
    }

    @Test
    void testEnterOrderGetWithBuySide() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.BUY);
        buffer.putLong(1L);
        buffer.putLong(10L);
        buffer.putLong(100L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(POE.BUY, enterOrder.side);
        assertEquals((byte) 'B', enterOrder.side);
    }

    @Test
    void testEnterOrderGetWithSellSide() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.SELL);
        buffer.putLong(2L);
        buffer.putLong(20L);
        buffer.putLong(200L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(POE.SELL, enterOrder.side);
        assertEquals((byte) 'S', enterOrder.side);
    }

    @Test
    void testEnterOrderGetWithZeroValues() {
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
    void testEnterOrderGetWithMaxLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals(Long.MAX_VALUE, enterOrder.instrument);
        assertEquals(Long.MAX_VALUE, enterOrder.quantity);
        assertEquals(Long.MAX_VALUE, enterOrder.price);
    }

    @Test
    void testEnterOrderGetWithMinLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'S');
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(Long.MIN_VALUE, enterOrder.instrument);
        assertEquals(Long.MIN_VALUE, enterOrder.quantity);
        assertEquals(Long.MIN_VALUE, enterOrder.price);
    }

    @Test
    void testEnterOrderGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(-100L);
        buffer.putLong(-500L);
        buffer.putLong(-1000L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(-100L, enterOrder.instrument);
        assertEquals(-500L, enterOrder.quantity);
        assertEquals(-1000L, enterOrder.price);
    }

    @Test
    void testEnterOrderGetWithAlphanumericOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(123L);
        buffer.putLong(456L);
        buffer.putLong(789L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals(123L, enterOrder.instrument);
        assertEquals(456L, enterOrder.quantity);
        assertEquals(789L, enterOrder.price);
    }

    @Test
    void testEnterOrderGetMultipleTimes() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        ByteBuffer buffer1 = ByteBuffer.allocate(41);
        byte[] testOrderId1 = "ORDER1234567890A".getBytes();
        buffer1.put(testOrderId1);
        buffer1.put((byte) 'B');
        buffer1.putLong(10L);
        buffer1.putLong(100L);
        buffer1.putLong(1000L);
        buffer1.flip();

        enterOrder.get(buffer1);
        assertArrayEquals(testOrderId1, enterOrder.orderId);
        assertEquals((byte) 'B', enterOrder.side);
        assertEquals(10L, enterOrder.instrument);
        assertEquals(100L, enterOrder.quantity);
        assertEquals(1000L, enterOrder.price);

        ByteBuffer buffer2 = ByteBuffer.allocate(41);
        byte[] testOrderId2 = "ORDER9876543210B".getBytes();
        buffer2.put(testOrderId2);
        buffer2.put((byte) 'S');
        buffer2.putLong(20L);
        buffer2.putLong(200L);
        buffer2.putLong(2000L);
        buffer2.flip();

        enterOrder.get(buffer2);
        assertArrayEquals(testOrderId2, enterOrder.orderId);
        assertEquals((byte) 'S', enterOrder.side);
        assertEquals(20L, enterOrder.instrument);
        assertEquals(200L, enterOrder.quantity);
        assertEquals(2000L, enterOrder.price);
    }

    @Test
    void testEnterOrderGetOverwritesPreviousValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] initialOrderId = "INITIAL123456789".getBytes();
        System.arraycopy(initialOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 999L;
        enterOrder.quantity = 888L;
        enterOrder.price = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] newOrderId = "NEWORDER12345678".getBytes();
        buffer.put(newOrderId);
        buffer.put((byte) 'S');
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.flip();

        enterOrder.get(buffer);

        assertArrayEquals(newOrderId, enterOrder.orderId);
        assertEquals((byte) 'S', enterOrder.side);
        assertEquals(111L, enterOrder.instrument);
        assertEquals(222L, enterOrder.quantity);
        assertEquals(333L, enterOrder.price);
    }

    @Test
    void testEnterOrderGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(999L);
        buffer.flip();

        assertEquals(0, buffer.position());

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertEquals(41, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testEnterOrderGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(999L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(50L);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.flip();

        buffer.getLong();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals((byte) 'B', enterOrder.side);
        assertEquals(50L, enterOrder.instrument);
        assertEquals(100L, enterOrder.quantity);
        assertEquals(200L, enterOrder.price);
    }

    @Test
    void testEnterOrderGetWithOneValues() {
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

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals((byte) 1, enterOrder.side);
        assertEquals(1L, enterOrder.instrument);
        assertEquals(1L, enterOrder.quantity);
        assertEquals(1L, enterOrder.price);
    }

    @Test
    void testEnterOrderPutWithBasicValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 100L;
        enterOrder.quantity = 500L;
        enterOrder.price = 10000L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 'B', buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(10000L, buffer.getLong());
    }

    @Test
    void testEnterOrderPutWithBuySide() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 1L;
        enterOrder.quantity = 10L;
        enterOrder.price = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.BUY, buffer.get());
    }

    @Test
    void testEnterOrderPutWithSellSide() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = POE.SELL;
        enterOrder.instrument = 2L;
        enterOrder.quantity = 20L;
        enterOrder.price = 200L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        buffer.get();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.SELL, buffer.get());
    }

    @Test
    void testEnterOrderPutWithZeroValues() {
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
    void testEnterOrderPutWithMaxLongValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
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
        buffer.get();
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testEnterOrderPutWithMinLongValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        enterOrder.side = (byte) 'S';
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
        buffer.get();
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testEnterOrderPutWithNegativeValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = -100L;
        enterOrder.quantity = -500L;
        enterOrder.price = -1000L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        buffer.get();
        assertEquals(-100L, buffer.getLong());
        assertEquals(-500L, buffer.getLong());
        assertEquals(-1000L, buffer.getLong());
    }

    @Test
    void testEnterOrderPutMultipleTimes() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 10L;
        enterOrder.quantity = 100L;
        enterOrder.price = 1000L;

        ByteBuffer buffer1 = ByteBuffer.allocate(42);
        enterOrder.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'E', buffer1.get());
        byte[] readOrderId1 = new byte[16];
        buffer1.get(readOrderId1);
        assertArrayEquals(testOrderId, readOrderId1);
        assertEquals((byte) 'B', buffer1.get());
        assertEquals(10L, buffer1.getLong());
        assertEquals(100L, buffer1.getLong());
        assertEquals(1000L, buffer1.getLong());

        ByteBuffer buffer2 = ByteBuffer.allocate(42);
        enterOrder.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'E', buffer2.get());
        byte[] readOrderId2 = new byte[16];
        buffer2.get(readOrderId2);
        assertArrayEquals(testOrderId, readOrderId2);
        assertEquals((byte) 'B', buffer2.get());
        assertEquals(10L, buffer2.getLong());
        assertEquals(100L, buffer2.getLong());
        assertEquals(1000L, buffer2.getLong());
    }

    @Test
    void testEnterOrderPutAdvancesBufferPosition() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'B';
        enterOrder.instrument = 1L;
        enterOrder.quantity = 2L;
        enterOrder.price = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        assertEquals(0, buffer.position());

        enterOrder.put(buffer);

        assertEquals(42, buffer.position());
    }

    @Test
    void testEnterOrderPutWritesMessageType() {
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
    }

    @Test
    void testEnterOrderPutWithBufferAtNonZeroPosition() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
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
        assertEquals((byte) 'B', buffer.get());
        assertEquals(50L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
    }

    @Test
    void testEnterOrderPutWithLargeValues() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = (byte) 'S';
        enterOrder.instrument = 9223372036854775807L;
        enterOrder.quantity = 9223372036854775806L;
        enterOrder.price = 9223372036854775805L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals((byte) 'S', buffer.get());
        assertEquals(9223372036854775807L, buffer.getLong());
        assertEquals(9223372036854775806L, buffer.getLong());
        assertEquals(9223372036854775805L, buffer.getLong());
    }

    @Test
    void testEnterOrderPutWithOne() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
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
    void testEnterOrderGetAndPutRoundTrip() {
        POE.EnterOrder original = new POE.EnterOrder();
        byte[] testOrderId = "ORDER987654321XY".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.side = (byte) 'B';
        original.instrument = 123L;
        original.quantity = 456L;
        original.price = 789L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.EnterOrder recovered = new POE.EnterOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.side, recovered.side);
        assertEquals(original.instrument, recovered.instrument);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
    }

    @Test
    void testEnterOrderGetAndPutRoundTripWithMaxValues() {
        POE.EnterOrder original = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.side = (byte) 'S';
        original.instrument = Long.MAX_VALUE;
        original.quantity = Long.MAX_VALUE;
        original.price = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.EnterOrder recovered = new POE.EnterOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.side, recovered.side);
        assertEquals(original.instrument, recovered.instrument);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
    }

    @Test
    void testEnterOrderGetAndPutRoundTripWithMinValues() {
        POE.EnterOrder original = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        original.side = (byte) 'B';
        original.instrument = Long.MIN_VALUE;
        original.quantity = Long.MIN_VALUE;
        original.price = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.EnterOrder recovered = new POE.EnterOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.side, recovered.side);
        assertEquals(original.instrument, recovered.instrument);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
    }

    @Test
    void testEnterOrderGetAndPutRoundTripWithZeroValues() {
        POE.EnterOrder original = new POE.EnterOrder();
        byte[] testOrderId = new byte[16];
        original.side = (byte) 0;
        original.instrument = 0L;
        original.quantity = 0L;
        original.price = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.EnterOrder recovered = new POE.EnterOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.side, recovered.side);
        assertEquals(original.instrument, recovered.instrument);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
    }

    @Test
    void testEnterOrderPutWritesFieldsInCorrectOrder() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] testOrderId = "ORDER111222333AB".getBytes();
        System.arraycopy(testOrderId, 0, enterOrder.orderId, 0, 16);
        enterOrder.side = (byte) 'B';
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
        assertEquals((byte) 'B', buffer.get());
        assertEquals(111L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
        assertEquals(333L, buffer.getLong());
    }

    @Test
    void testEnterOrderGetReadsAllFields() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        byte[] testOrderId = "ORDER444555666AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'S');
        buffer.putLong(444L);
        buffer.putLong(555L);
        buffer.putLong(666L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();

        byte[] initialOrderId = new byte[16];
        assertArrayEquals(initialOrderId, enterOrder.orderId);
        assertEquals((byte) 0, enterOrder.side);
        assertEquals(0L, enterOrder.instrument);
        assertEquals(0L, enterOrder.quantity);
        assertEquals(0L, enterOrder.price);

        enterOrder.get(buffer);

        assertArrayEquals(testOrderId, enterOrder.orderId);
        assertEquals((byte) 'S', enterOrder.side);
        assertEquals(444L, enterOrder.instrument);
        assertEquals(555L, enterOrder.quantity);
        assertEquals(666L, enterOrder.price);
    }

    @Test
    void testEnterOrderWithMessageLengthConstant() {
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
    void testEnterOrderGetAndPutWithRealisticTradingData() {
        POE.EnterOrder original = new POE.EnterOrder();
        byte[] testOrderId = "TRD0000000000001".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.side = POE.BUY;
        original.instrument = 12345L;
        original.quantity = 100L;
        original.price = 9950L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        original.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());

        POE.EnterOrder recovered = new POE.EnterOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(POE.BUY, recovered.side);
        assertEquals(12345L, recovered.instrument);
        assertEquals(100L, recovered.quantity);
        assertEquals(9950L, recovered.price);
    }

    @Test
    void testEnterOrderGetAndPutWithAnotherRealisticTradingData() {
        POE.EnterOrder original = new POE.EnterOrder();
        byte[] testOrderId = "TRD0000000000002".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.side = POE.SELL;
        original.instrument = 54321L;
        original.quantity = 250L;
        original.price = 10050L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        original.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());

        POE.EnterOrder recovered = new POE.EnterOrder();
        recovered.get(buffer);

        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(POE.SELL, recovered.side);
        assertEquals(54321L, recovered.instrument);
        assertEquals(250L, recovered.quantity);
        assertEquals(10050L, recovered.price);
    }

    // ========== OrderAccepted Tests ==========

    @Test
    void testOrderAcceptedConstructorInitializesOrderIdArray() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertNotNull(orderAccepted.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, orderAccepted.orderId.length);
        assertEquals(16, orderAccepted.orderId.length);
    }

    @Test
    void testOrderAcceptedConstructorInitializesFieldsToDefault() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(0L, orderAccepted.timestamp);
        assertEquals(0, orderAccepted.side);
        assertEquals(0L, orderAccepted.instrument);
        assertEquals(0L, orderAccepted.quantity);
        assertEquals(0L, orderAccepted.price);
        assertEquals(0L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorCreatesNewArray() {
        POE.OrderAccepted orderAccepted1 = new POE.OrderAccepted();
        POE.OrderAccepted orderAccepted2 = new POE.OrderAccepted();

        assertNotSame(orderAccepted1.orderId, orderAccepted2.orderId);
    }

    @Test
    void testOrderAcceptedGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(1234567890L);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) i;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(100L);
        buffer.putLong(500L);
        buffer.putLong(10000L);
        buffer.putLong(999L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(1234567890L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals((byte) 'B', orderAccepted.side);
        assertEquals(100L, orderAccepted.instrument);
        assertEquals(500L, orderAccepted.quantity);
        assertEquals(10000L, orderAccepted.price);
        assertEquals(999L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithBuySide() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(123L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.BUY);
        buffer.putLong(1L);
        buffer.putLong(10L);
        buffer.putLong(100L);
        buffer.putLong(1000L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(POE.BUY, orderAccepted.side);
        assertEquals((byte) 'B', orderAccepted.side);
    }

    @Test
    void testOrderAcceptedGetWithSellSide() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(456L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.SELL);
        buffer.putLong(2L);
        buffer.putLong(20L);
        buffer.putLong(200L);
        buffer.putLong(2000L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(POE.SELL, orderAccepted.side);
        assertEquals((byte) 'S', orderAccepted.side);
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
    void testOrderAcceptedGetWithMaxLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(Long.MAX_VALUE);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(Long.MAX_VALUE, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals(Long.MAX_VALUE, orderAccepted.instrument);
        assertEquals(Long.MAX_VALUE, orderAccepted.quantity);
        assertEquals(Long.MAX_VALUE, orderAccepted.price);
        assertEquals(Long.MAX_VALUE, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithMinLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(Long.MIN_VALUE);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'S');
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(Long.MIN_VALUE, orderAccepted.timestamp);
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
        buffer.put((byte) 'B');
        buffer.putLong(-100L);
        buffer.putLong(-500L);
        buffer.putLong(-1000L);
        buffer.putLong(-999L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(-1000L, orderAccepted.timestamp);
        assertEquals(-100L, orderAccepted.instrument);
        assertEquals(-500L, orderAccepted.quantity);
        assertEquals(-1000L, orderAccepted.price);
        assertEquals(-999L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetWithAlphanumericOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(5000L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(123L);
        buffer.putLong(456L);
        buffer.putLong(789L);
        buffer.putLong(555L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(5000L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals(123L, orderAccepted.instrument);
        assertEquals(456L, orderAccepted.quantity);
        assertEquals(789L, orderAccepted.price);
        assertEquals(555L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetMultipleTimes() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        ByteBuffer buffer1 = ByteBuffer.allocate(57);
        buffer1.putLong(1111L);
        byte[] testOrderId1 = "ORDER1234567890A".getBytes();
        buffer1.put(testOrderId1);
        buffer1.put((byte) 'B');
        buffer1.putLong(10L);
        buffer1.putLong(100L);
        buffer1.putLong(1000L);
        buffer1.putLong(10000L);
        buffer1.flip();

        orderAccepted.get(buffer1);
        assertEquals(1111L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId1, orderAccepted.orderId);
        assertEquals((byte) 'B', orderAccepted.side);
        assertEquals(10L, orderAccepted.instrument);
        assertEquals(100L, orderAccepted.quantity);
        assertEquals(1000L, orderAccepted.price);
        assertEquals(10000L, orderAccepted.orderNumber);

        ByteBuffer buffer2 = ByteBuffer.allocate(57);
        buffer2.putLong(2222L);
        byte[] testOrderId2 = "ORDER9876543210B".getBytes();
        buffer2.put(testOrderId2);
        buffer2.put((byte) 'S');
        buffer2.putLong(20L);
        buffer2.putLong(200L);
        buffer2.putLong(2000L);
        buffer2.putLong(20000L);
        buffer2.flip();

        orderAccepted.get(buffer2);
        assertEquals(2222L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId2, orderAccepted.orderId);
        assertEquals((byte) 'S', orderAccepted.side);
        assertEquals(20L, orderAccepted.instrument);
        assertEquals(200L, orderAccepted.quantity);
        assertEquals(2000L, orderAccepted.price);
        assertEquals(20000L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedGetOverwritesPreviousValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 9999L;
        byte[] initialOrderId = "INITIAL123456789".getBytes();
        System.arraycopy(initialOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 999L;
        orderAccepted.quantity = 888L;
        orderAccepted.price = 777L;
        orderAccepted.orderNumber = 666L;

        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(1111L);
        byte[] newOrderId = "NEWORDER12345678".getBytes();
        buffer.put(newOrderId);
        buffer.put((byte) 'S');
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.putLong(444L);
        buffer.flip();

        orderAccepted.get(buffer);

        assertEquals(1111L, orderAccepted.timestamp);
        assertArrayEquals(newOrderId, orderAccepted.orderId);
        assertEquals((byte) 'S', orderAccepted.side);
        assertEquals(111L, orderAccepted.instrument);
        assertEquals(222L, orderAccepted.quantity);
        assertEquals(333L, orderAccepted.price);
        assertEquals(444L, orderAccepted.orderNumber);
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

        assertEquals(0, buffer.position());

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(57, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(65);
        buffer.putLong(999L);
        buffer.putLong(5678L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(50L);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        buffer.getLong();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.get(buffer);

        assertEquals(5678L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals((byte) 'B', orderAccepted.side);
        assertEquals(50L, orderAccepted.instrument);
        assertEquals(100L, orderAccepted.quantity);
        assertEquals(200L, orderAccepted.price);
        assertEquals(300L, orderAccepted.orderNumber);
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
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals((byte) 1, orderAccepted.side);
        assertEquals(1L, orderAccepted.instrument);
        assertEquals(1L, orderAccepted.quantity);
        assertEquals(1L, orderAccepted.price);
        assertEquals(1L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedPutWithBasicValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 1234567890L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 100L;
        orderAccepted.quantity = 500L;
        orderAccepted.price = 10000L;
        orderAccepted.orderNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(1234567890L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 'B', buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(10000L, buffer.getLong());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithBuySide() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 123L;
        orderAccepted.side = POE.BUY;
        orderAccepted.instrument = 1L;
        orderAccepted.quantity = 10L;
        orderAccepted.price = 100L;
        orderAccepted.orderNumber = 1000L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.BUY, buffer.get());
    }

    @Test
    void testOrderAcceptedPutWithSellSide() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 456L;
        orderAccepted.side = POE.SELL;
        orderAccepted.instrument = 2L;
        orderAccepted.quantity = 20L;
        orderAccepted.price = 200L;
        orderAccepted.orderNumber = 2000L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.SELL, buffer.get());
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
    void testOrderAcceptedPutWithMaxLongValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.timestamp = Long.MAX_VALUE;
        orderAccepted.side = (byte) 'B';
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
        buffer.get();
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithMinLongValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        byte[] testOrderId = new byte[16];
        orderAccepted.timestamp = Long.MIN_VALUE;
        orderAccepted.side = (byte) 'S';
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
        buffer.get();
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
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = -100L;
        orderAccepted.quantity = -500L;
        orderAccepted.price = -1000L;
        orderAccepted.orderNumber = -999L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(-1000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        buffer.get();
        assertEquals(-100L, buffer.getLong());
        assertEquals(-500L, buffer.getLong());
        assertEquals(-1000L, buffer.getLong());
        assertEquals(-999L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutMultipleTimes() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 7777L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 10L;
        orderAccepted.quantity = 100L;
        orderAccepted.price = 1000L;
        orderAccepted.orderNumber = 10000L;

        ByteBuffer buffer1 = ByteBuffer.allocate(58);
        orderAccepted.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'A', buffer1.get());
        assertEquals(7777L, buffer1.getLong());
        byte[] readOrderId1 = new byte[16];
        buffer1.get(readOrderId1);
        assertArrayEquals(testOrderId, readOrderId1);
        assertEquals((byte) 'B', buffer1.get());
        assertEquals(10L, buffer1.getLong());
        assertEquals(100L, buffer1.getLong());
        assertEquals(1000L, buffer1.getLong());
        assertEquals(10000L, buffer1.getLong());

        ByteBuffer buffer2 = ByteBuffer.allocate(58);
        orderAccepted.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'A', buffer2.get());
        assertEquals(7777L, buffer2.getLong());
        byte[] readOrderId2 = new byte[16];
        buffer2.get(readOrderId2);
        assertArrayEquals(testOrderId, readOrderId2);
        assertEquals((byte) 'B', buffer2.get());
        assertEquals(10L, buffer2.getLong());
        assertEquals(100L, buffer2.getLong());
        assertEquals(1000L, buffer2.getLong());
        assertEquals(10000L, buffer2.getLong());
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
        assertEquals(0, buffer.position());

        orderAccepted.put(buffer);

        assertEquals(58, buffer.position());
    }

    @Test
    void testOrderAcceptedPutWritesMessageType() {
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

        byte messageType = buffer.get();
        assertEquals((byte) 'A', messageType);
    }

    @Test
    void testOrderAcceptedPutWithBufferAtNonZeroPosition() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 8888L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
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
        assertEquals((byte) 'B', buffer.get());
        assertEquals(50L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithLargeValues() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 9223372036854775807L;
        orderAccepted.side = (byte) 'S';
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
        assertEquals((byte) 'S', buffer.get());
        assertEquals(9223372036854775806L, buffer.getLong());
        assertEquals(9223372036854775805L, buffer.getLong());
        assertEquals(9223372036854775804L, buffer.getLong());
        assertEquals(9223372036854775803L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedPutWithOne() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
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
    void testOrderAcceptedGetAndPutRoundTrip() {
        POE.OrderAccepted original = new POE.OrderAccepted();
        original.timestamp = 1234567890L;
        byte[] testOrderId = "ORDER987654321XY".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.side = (byte) 'B';
        original.instrument = 123L;
        original.quantity = 456L;
        original.price = 789L;
        original.orderNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderAccepted recovered = new POE.OrderAccepted();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.side, recovered.side);
        assertEquals(original.instrument, recovered.instrument);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
        assertEquals(original.orderNumber, recovered.orderNumber);
    }

    @Test
    void testOrderAcceptedGetAndPutRoundTripWithMaxValues() {
        POE.OrderAccepted original = new POE.OrderAccepted();
        original.timestamp = Long.MAX_VALUE;
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.side = (byte) 'S';
        original.instrument = Long.MAX_VALUE;
        original.quantity = Long.MAX_VALUE;
        original.price = Long.MAX_VALUE;
        original.orderNumber = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderAccepted recovered = new POE.OrderAccepted();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.side, recovered.side);
        assertEquals(original.instrument, recovered.instrument);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
        assertEquals(original.orderNumber, recovered.orderNumber);
    }

    @Test
    void testOrderAcceptedGetAndPutRoundTripWithMinValues() {
        POE.OrderAccepted original = new POE.OrderAccepted();
        original.timestamp = Long.MIN_VALUE;
        byte[] testOrderId = new byte[16];
        original.side = (byte) 'B';
        original.instrument = Long.MIN_VALUE;
        original.quantity = Long.MIN_VALUE;
        original.price = Long.MIN_VALUE;
        original.orderNumber = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderAccepted recovered = new POE.OrderAccepted();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.side, recovered.side);
        assertEquals(original.instrument, recovered.instrument);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
        assertEquals(original.orderNumber, recovered.orderNumber);
    }

    @Test
    void testOrderAcceptedGetAndPutRoundTripWithZeroValues() {
        POE.OrderAccepted original = new POE.OrderAccepted();
        original.timestamp = 0L;
        byte[] testOrderId = new byte[16];
        original.side = (byte) 0;
        original.instrument = 0L;
        original.quantity = 0L;
        original.price = 0L;
        original.orderNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderAccepted recovered = new POE.OrderAccepted();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.side, recovered.side);
        assertEquals(original.instrument, recovered.instrument);
        assertEquals(original.quantity, recovered.quantity);
        assertEquals(original.price, recovered.price);
        assertEquals(original.orderNumber, recovered.orderNumber);
    }

    @Test
    void testOrderAcceptedPutWritesFieldsInCorrectOrder() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 5555L;
        byte[] testOrderId = "ORDER111222333AB".getBytes();
        System.arraycopy(testOrderId, 0, orderAccepted.orderId, 0, 16);
        orderAccepted.side = (byte) 'B';
        orderAccepted.instrument = 111L;
        orderAccepted.quantity = 222L;
        orderAccepted.price = 333L;
        orderAccepted.orderNumber = 444L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(5555L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 'B', buffer.get());
        assertEquals(111L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
        assertEquals(333L, buffer.getLong());
        assertEquals(444L, buffer.getLong());
    }

    @Test
    void testOrderAcceptedGetReadsAllFields() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(6666L);
        byte[] testOrderId = "ORDER444555666AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'S');
        buffer.putLong(444L);
        buffer.putLong(555L);
        buffer.putLong(666L);
        buffer.putLong(777L);
        buffer.flip();

        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();

        assertEquals(0L, orderAccepted.timestamp);
        byte[] initialOrderId = new byte[16];
        assertArrayEquals(initialOrderId, orderAccepted.orderId);
        assertEquals((byte) 0, orderAccepted.side);
        assertEquals(0L, orderAccepted.instrument);
        assertEquals(0L, orderAccepted.quantity);
        assertEquals(0L, orderAccepted.price);
        assertEquals(0L, orderAccepted.orderNumber);

        orderAccepted.get(buffer);

        assertEquals(6666L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals((byte) 'S', orderAccepted.side);
        assertEquals(444L, orderAccepted.instrument);
        assertEquals(555L, orderAccepted.quantity);
        assertEquals(666L, orderAccepted.price);
        assertEquals(777L, orderAccepted.orderNumber);
    }

    @Test
    void testOrderAcceptedWithMessageLengthConstant() {
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
    void testOrderAcceptedGetAndPutWithRealisticTradingData() {
        POE.OrderAccepted original = new POE.OrderAccepted();
        original.timestamp = 1609459200000L;
        byte[] testOrderId = "TRD0000000000001".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.side = POE.BUY;
        original.instrument = 12345L;
        original.quantity = 100L;
        original.price = 9950L;
        original.orderNumber = 55555L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        original.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());

        POE.OrderAccepted recovered = new POE.OrderAccepted();
        recovered.get(buffer);

        assertEquals(1609459200000L, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(POE.BUY, recovered.side);
        assertEquals(12345L, recovered.instrument);
        assertEquals(100L, recovered.quantity);
        assertEquals(9950L, recovered.price);
        assertEquals(55555L, recovered.orderNumber);
    }

    @Test
    void testOrderAcceptedGetAndPutWithAnotherRealisticTradingData() {
        POE.OrderAccepted original = new POE.OrderAccepted();
        original.timestamp = 1609459300000L;
        byte[] testOrderId = "TRD0000000000002".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.side = POE.SELL;
        original.instrument = 54321L;
        original.quantity = 250L;
        original.price = 10050L;
        original.orderNumber = 66666L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        original.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());

        POE.OrderAccepted recovered = new POE.OrderAccepted();
        recovered.get(buffer);

        assertEquals(1609459300000L, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(POE.SELL, recovered.side);
        assertEquals(54321L, recovered.instrument);
        assertEquals(250L, recovered.quantity);
        assertEquals(10050L, recovered.price);
        assertEquals(66666L, recovered.orderNumber);
    }

    @Test
    void testOrderCanceledConstructorInitializesOrderIdArray() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertNotNull(orderCanceled.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, orderCanceled.orderId.length);
        assertEquals(16, orderCanceled.orderId.length);
    }

    @Test
    void testOrderCanceledConstructorInitializesFieldsToDefault() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();

        assertEquals(0L, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.canceledQuantity);
        assertEquals(0, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledConstructorCreatesNewArray() {
        POE.OrderCanceled orderCanceled1 = new POE.OrderCanceled();
        POE.OrderCanceled orderCanceled2 = new POE.OrderCanceled();

        assertNotSame(orderCanceled1.orderId, orderCanceled2.orderId);
    }

    @Test
    void testOrderCanceledGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(1234567890L);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) i;
        }
        buffer.put(testOrderId);
        buffer.putLong(500L);
        buffer.put((byte) 'R');
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1234567890L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(500L, orderCanceled.canceledQuantity);
        assertEquals((byte) 'R', orderCanceled.reason);
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
    void testOrderCanceledGetWithRequestReason() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(999L);
        byte[] testOrderId = "ORDER987654321AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(100L);
        buffer.put(POE.ORDER_CANCEL_REASON_REQUEST);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(999L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(100L, orderCanceled.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetWithSupervisoryReason() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(888L);
        byte[] testOrderId = "SUPER12345678ABC".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(250L);
        buffer.put(POE.ORDER_CANCEL_REASON_SUPERVISORY);
        buffer.flip();

        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(888L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(250L, orderCanceled.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_SUPERVISORY, orderCanceled.reason);
    }

    @Test
    void testOrderCanceledGetUpdatesExistingObject() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 111L;
        orderCanceled.canceledQuantity = 222L;
        orderCanceled.reason = (byte) 'X';

        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(333L);
        byte[] testOrderId = "NEWORDER12345678".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(444L);
        buffer.put((byte) 'R');
        buffer.flip();

        orderCanceled.get(buffer);

        assertEquals(333L, orderCanceled.timestamp);
        assertArrayEquals(testOrderId, orderCanceled.orderId);
        assertEquals(444L, orderCanceled.canceledQuantity);
        assertEquals((byte) 'R', orderCanceled.reason);
    }

    @Test
    void testOrderCanceledPutWithBasicValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 1234567890L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
        orderCanceled.canceledQuantity = 500L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(1234567890L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(500L, buffer.getLong());
        assertEquals((byte) 'R', buffer.get());
    }

    @Test
    void testOrderCanceledPutWithZeroValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
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
        assertEquals(0L, buffer.getLong());
        assertEquals((byte) 0, buffer.get());
    }

    @Test
    void testOrderCanceledPutWithMaxValues() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = Long.MAX_VALUE;
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, orderCanceled.orderId, 0, 16);
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
    void testOrderCanceledPutWithRequestReason() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 999L;
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
    }

    @Test
    void testOrderCanceledPutWithSupervisoryReason() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 888L;
        orderCanceled.canceledQuantity = 250L;
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
    }

    @Test
    void testOrderCanceledPutWritesCorrectMessageType() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 123L;
        orderCanceled.canceledQuantity = 10L;
        orderCanceled.reason = (byte) 'R';

        ByteBuffer buffer = ByteBuffer.allocate(34);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
    }

    @Test
    void testOrderCanceledPutAndGetRoundTrip() {
        POE.OrderCanceled original = new POE.OrderCanceled();
        original.timestamp = 1609459300000L;
        byte[] testOrderId = "CANCEL9876543210".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.canceledQuantity = 300L;
        original.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        ByteBuffer buffer = ByteBuffer.allocate(34);
        original.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());

        POE.OrderCanceled recovered = new POE.OrderCanceled();
        recovered.get(buffer);

        assertEquals(1609459300000L, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(300L, recovered.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, recovered.reason);
    }

    @Test
    void testOrderExecutedConstructorInitializesOrderIdArray() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertNotNull(orderExecuted.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, orderExecuted.orderId.length);
        assertEquals(16, orderExecuted.orderId.length);
    }

    @Test
    void testOrderExecutedConstructorInitializesFieldsToZero() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();

        assertEquals(0L, orderExecuted.timestamp);
        assertEquals(0L, orderExecuted.quantity);
        assertEquals(0L, orderExecuted.price);
        assertEquals((byte) 0, orderExecuted.liquidityFlag);
        assertEquals(0L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorCreatesNewArray() {
        POE.OrderExecuted orderExecuted1 = new POE.OrderExecuted();
        POE.OrderExecuted orderExecuted2 = new POE.OrderExecuted();

        assertNotSame(orderExecuted1.orderId, orderExecuted2.orderId);
    }

    @Test
    void testOrderExecutedGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1234567890000L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(500L);
        buffer.putLong(10050L);
        buffer.put(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY);
        buffer.putInt(12345);
        buffer.flip();

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
    void testOrderExecutedGetWithMaxLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(Long.MAX_VALUE);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.put((byte) 'R');
        buffer.putInt(-1);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(Long.MAX_VALUE, orderExecuted.timestamp);
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(Long.MAX_VALUE, orderExecuted.quantity);
        assertEquals(Long.MAX_VALUE, orderExecuted.price);
        assertEquals((byte) 'R', orderExecuted.liquidityFlag);
        assertEquals(4294967295L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithRemovedLiquidityFlag() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1609459200000L);
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        buffer.put(testOrderId);
        buffer.putLong(250L);
        buffer.putLong(5000L);
        buffer.put(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY);
        buffer.putInt(999);
        buffer.flip();

        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1609459200000L, orderExecuted.timestamp);
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(250L, orderExecuted.quantity);
        assertEquals(5000L, orderExecuted.price);
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, orderExecuted.liquidityFlag);
        assertEquals(999L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.putLong(1234567890000L);
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
    void testOrderExecutedGetWithUnsignedIntMax() {
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
    void testOrderExecutedGetWithOne() {
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
        assertArrayEquals(testOrderId, orderExecuted.orderId);
        assertEquals(1L, orderExecuted.quantity);
        assertEquals(1L, orderExecuted.price);
        assertEquals((byte) 1, orderExecuted.liquidityFlag);
        assertEquals(1L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedPutWithBasicValues() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1234567890000L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 500L;
        orderExecuted.price = 10050L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        orderExecuted.matchNumber = 12345L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

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
    void testOrderExecutedPutWithMaxLongValues() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = Long.MAX_VALUE;
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = Long.MAX_VALUE;
        orderExecuted.price = Long.MAX_VALUE;
        orderExecuted.liquidityFlag = (byte) 'R';
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
        assertEquals((byte) 'R', buffer.get());
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithRemovedLiquidityFlag() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1609459200000L;
        byte[] testOrderId = "TESTORDER1234567".getBytes();
        System.arraycopy(testOrderId, 0, orderExecuted.orderId, 0, 16);
        orderExecuted.quantity = 250L;
        orderExecuted.price = 5000L;
        orderExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        orderExecuted.matchNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1609459200000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(250L, buffer.getLong());
        assertEquals(5000L, buffer.getLong());
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, buffer.get());
        assertEquals(999, buffer.getInt());
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

        byte messageType = buffer.get();
        assertEquals((byte) 'E', messageType);
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
    void testOrderExecutedPutMultipleTimes() {
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
        byte[] readOrderId1 = new byte[16];
        buffer1.get(readOrderId1);
        assertArrayEquals(testOrderId, readOrderId1);
        assertEquals(100L, buffer1.getLong());
        assertEquals(200L, buffer1.getLong());
        assertEquals((byte) 'A', buffer1.get());
        assertEquals(300, buffer1.getInt());

        ByteBuffer buffer2 = ByteBuffer.allocate(46);
        orderExecuted.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'E', buffer2.get());
        assertEquals(1000L, buffer2.getLong());
        byte[] readOrderId2 = new byte[16];
        buffer2.get(readOrderId2);
        assertArrayEquals(testOrderId, readOrderId2);
        assertEquals(100L, buffer2.getLong());
        assertEquals(200L, buffer2.getLong());
        assertEquals((byte) 'A', buffer2.get());
        assertEquals(300, buffer2.getInt());
    }

    @Test
    void testOrderExecutedPutWithUnsignedIntMax() {
        POE.OrderExecuted orderExecuted = new POE.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.quantity = 1L;
        orderExecuted.price = 1L;
        orderExecuted.liquidityFlag = (byte) 'A';
        orderExecuted.matchNumber = 4294967295L;

        ByteBuffer buffer = ByteBuffer.allocate(46);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals((byte) 'A', buffer.get());
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithOne() {
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
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals((byte) 1, buffer.get());
        assertEquals(1, buffer.getInt());
    }

    @Test
    void testOrderExecutedRoundTripSerialization() {
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

        assertEquals(1609459200000L, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(250L, recovered.quantity);
        assertEquals(5000L, recovered.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, recovered.liquidityFlag);
        assertEquals(888L, recovered.matchNumber);
    }

    // ========== OrderRejected Tests ==========

    @Test
    void testOrderRejectedConstructorInitializesOrderIdArray() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertNotNull(orderRejected.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, orderRejected.orderId.length);
        assertEquals(16, orderRejected.orderId.length);
    }

    @Test
    void testOrderRejectedConstructorInitializesFieldsToDefault() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        assertEquals(0L, orderRejected.timestamp);
        assertEquals(0, orderRejected.reason);
    }

    @Test
    void testOrderRejectedConstructorCreatesNewArray() {
        POE.OrderRejected orderRejected1 = new POE.OrderRejected();
        POE.OrderRejected orderRejected2 = new POE.OrderRejected();

        assertNotSame(orderRejected1.orderId, orderRejected2.orderId);
    }

    @Test
    void testOrderRejectedGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(1234567890L);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) i;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'P');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(1234567890L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals((byte) 'P', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithUnknownInstrumentReason() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(123L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, orderRejected.reason);
        assertEquals((byte) 'I', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithInvalidPriceReason() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(456L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.ORDER_REJECT_REASON_INVALID_PRICE);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, orderRejected.reason);
        assertEquals((byte) 'P', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithInvalidQuantityReason() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(789L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put(POE.ORDER_REJECT_REASON_INVALID_QUANTITY);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, orderRejected.reason);
        assertEquals((byte) 'Q', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(0L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 0);
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(0L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals((byte) 0, orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetWithMaxLongTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(Long.MAX_VALUE);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'I');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(Long.MAX_VALUE, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
    }

    @Test
    void testOrderRejectedGetWithMinLongTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(Long.MIN_VALUE);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'Q');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(Long.MIN_VALUE, orderRejected.timestamp);
    }

    @Test
    void testOrderRejectedGetWithNegativeTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(-1000L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'P');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(-1000L, orderRejected.timestamp);
    }

    @Test
    void testOrderRejectedGetWithAlphanumericOrderId() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(5000L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'I');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(5000L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
    }

    @Test
    void testOrderRejectedGetMultipleTimes() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();

        ByteBuffer buffer1 = ByteBuffer.allocate(25);
        buffer1.putLong(1111L);
        byte[] testOrderId1 = "ORDER1234567890A".getBytes();
        buffer1.put(testOrderId1);
        buffer1.put((byte) 'P');
        buffer1.flip();

        orderRejected.get(buffer1);
        assertEquals(1111L, orderRejected.timestamp);
        assertArrayEquals(testOrderId1, orderRejected.orderId);
        assertEquals((byte) 'P', orderRejected.reason);

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        buffer2.putLong(2222L);
        byte[] testOrderId2 = "ORDER9876543210B".getBytes();
        buffer2.put(testOrderId2);
        buffer2.put((byte) 'Q');
        buffer2.flip();

        orderRejected.get(buffer2);
        assertEquals(2222L, orderRejected.timestamp);
        assertArrayEquals(testOrderId2, orderRejected.orderId);
        assertEquals((byte) 'Q', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetOverwritesPreviousValues() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 9999L;
        byte[] initialOrderId = "INITIAL123456789".getBytes();
        System.arraycopy(initialOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = (byte) 'I';

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(1111L);
        byte[] newOrderId = "NEWORDER12345678".getBytes();
        buffer.put(newOrderId);
        buffer.put((byte) 'P');
        buffer.flip();

        orderRejected.get(buffer);

        assertEquals(1111L, orderRejected.timestamp);
        assertArrayEquals(newOrderId, orderRejected.orderId);
        assertEquals((byte) 'P', orderRejected.reason);
    }

    @Test
    void testOrderRejectedGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(123L);
        byte[] testOrderId = new byte[16];
        buffer.put(testOrderId);
        buffer.put((byte) 'I');
        buffer.putLong(999L);
        buffer.flip();

        assertEquals(0, buffer.position());

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(25, buffer.position());
        assertEquals(999L, buffer.getLong());
    }

    @Test
    void testOrderRejectedGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.putLong(999L);
        buffer.putLong(5678L);
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'Q');
        buffer.flip();

        buffer.getLong();

        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.get(buffer);

        assertEquals(5678L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals((byte) 'Q', orderRejected.reason);
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
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals((byte) 1, orderRejected.reason);
    }

    @Test
    void testOrderRejectedPutWithBasicValues() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 1234567890L;
        byte[] testOrderId = "ORDER123456789AB".getBytes();
        System.arraycopy(testOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(1234567890L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 'P', buffer.get());
    }

    @Test
    void testOrderRejectedPutWithUnknownInstrumentReason() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 123L;
        orderRejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, buffer.get());
    }

    @Test
    void testOrderRejectedPutWithInvalidPriceReason() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 456L;
        orderRejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, buffer.get());
    }

    @Test
    void testOrderRejectedPutWithInvalidQuantityReason() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 789L;
        orderRejected.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, buffer.get());
    }

    @Test
    void testOrderRejectedPutWithZeroValues() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        byte[] testOrderId = new byte[16];
        orderRejected.timestamp = 0L;
        orderRejected.reason = (byte) 0;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(0L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals((byte) 0, buffer.get());
    }

    @Test
    void testOrderRejectedPutWithMaxLongTimestamp() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.timestamp = Long.MAX_VALUE;
        orderRejected.reason = (byte) 'I';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        buffer.get();
    }

    @Test
    void testOrderRejectedPutWithMinLongTimestamp() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        byte[] testOrderId = new byte[16];
        orderRejected.timestamp = Long.MIN_VALUE;
        orderRejected.reason = (byte) 'Q';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        buffer.get();
    }

    @Test
    void testOrderRejectedPutWithNegativeTimestamp() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        byte[] testOrderId = new byte[16];
        orderRejected.timestamp = -1000L;
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(-1000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        buffer.get();
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
    void testOrderRejectedPutWritesMessageType() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 100L;
        orderRejected.reason = (byte) 'Q';

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals((byte) 'R', messageType);
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
    void testOrderRejectedPutWithOne() {
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
    void testOrderRejectedGetAndPutRoundTrip() {
        POE.OrderRejected original = new POE.OrderRejected();
        original.timestamp = 1234567890L;
        byte[] testOrderId = "ORDER987654321XY".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderRejected recovered = new POE.OrderRejected();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.reason, recovered.reason);
    }

    @Test
    void testOrderRejectedGetAndPutRoundTripWithMaxValues() {
        POE.OrderRejected original = new POE.OrderRejected();
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) 0xFF;
        }
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.timestamp = Long.MAX_VALUE;
        original.reason = (byte) 0xFF;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderRejected recovered = new POE.OrderRejected();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.reason, recovered.reason);
    }

    @Test
    void testOrderRejectedGetAndPutRoundTripWithMinValues() {
        POE.OrderRejected original = new POE.OrderRejected();
        byte[] testOrderId = new byte[16];
        original.timestamp = Long.MIN_VALUE;
        original.reason = (byte) 0;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderRejected recovered = new POE.OrderRejected();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.reason, recovered.reason);
    }

    @Test
    void testOrderRejectedGetAndPutRoundTripWithZeroValues() {
        POE.OrderRejected original = new POE.OrderRejected();
        byte[] testOrderId = new byte[16];
        original.timestamp = 0L;
        original.reason = (byte) 0;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderRejected recovered = new POE.OrderRejected();
        recovered.get(buffer);

        assertEquals(original.timestamp, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(original.reason, recovered.reason);
    }

    @Test
    void testOrderRejectedPutWritesFieldsInCorrectOrder() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 555L;
        byte[] testOrderId = "ORDER111222333AB".getBytes();
        System.arraycopy(testOrderId, 0, orderRejected.orderId, 0, 16);
        orderRejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        orderRejected.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());
        assertEquals(555L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, buffer.get());
    }

    @Test
    void testOrderRejectedGetReadsAllFields() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(777L);
        byte[] testOrderId = "ORDER444555666AB".getBytes();
        buffer.put(testOrderId);
        buffer.put((byte) 'Q');
        buffer.flip();

        POE.OrderRejected orderRejected = new POE.OrderRejected();

        byte[] initialOrderId = new byte[16];
        assertArrayEquals(initialOrderId, orderRejected.orderId);
        assertEquals(0L, orderRejected.timestamp);
        assertEquals((byte) 0, orderRejected.reason);

        orderRejected.get(buffer);

        assertEquals(777L, orderRejected.timestamp);
        assertArrayEquals(testOrderId, orderRejected.orderId);
        assertEquals((byte) 'Q', orderRejected.reason);
    }

    @Test
    void testOrderRejectedWithMessageLengthConstant() {
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        orderRejected.timestamp = 100L;
        orderRejected.reason = (byte) 'P';

        ByteBuffer buffer = ByteBuffer.allocate(POE.MESSAGE_LENGTH_ORDER_REJECTED);
        orderRejected.put(buffer);

        assertEquals(POE.MESSAGE_LENGTH_ORDER_REJECTED, buffer.position());
        assertEquals(26, buffer.position());
    }

    @Test
    void testOrderRejectedGetAndPutWithRealisticTradingData() {
        POE.OrderRejected original = new POE.OrderRejected();
        original.timestamp = 1609459200000L;
        byte[] testOrderId = "TRD0000000000003".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        original.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());

        POE.OrderRejected recovered = new POE.OrderRejected();
        recovered.get(buffer);

        assertEquals(1609459200000L, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, recovered.reason);
    }

    @Test
    void testOrderRejectedGetAndPutWithAnotherRealisticTradingData() {
        POE.OrderRejected original = new POE.OrderRejected();
        original.timestamp = 1609459300000L;
        byte[] testOrderId = "TRD0000000000004".getBytes();
        System.arraycopy(testOrderId, 0, original.orderId, 0, 16);
        original.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        original.put(buffer);
        buffer.flip();

        assertEquals((byte) 'R', buffer.get());

        POE.OrderRejected recovered = new POE.OrderRejected();
        recovered.get(buffer);

        assertEquals(1609459300000L, recovered.timestamp);
        assertArrayEquals(original.orderId, recovered.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, recovered.reason);
    }
}
