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
package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class PMDClaude_putTest {

    @Test
    void testOrderCanceledPutWithBasicValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1000L;
        orderCanceled.orderNumber = 12345L;
        orderCanceled.canceledQuantity = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(12345L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithZeroValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 0L;
        orderCanceled.orderNumber = 0L;
        orderCanceled.canceledQuantity = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithMaxLongValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = Long.MAX_VALUE;
        orderCanceled.orderNumber = Long.MAX_VALUE;
        orderCanceled.canceledQuantity = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithMinLongValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = Long.MIN_VALUE;
        orderCanceled.orderNumber = Long.MIN_VALUE;
        orderCanceled.canceledQuantity = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithNegativeValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = -1L;
        orderCanceled.orderNumber = -2L;
        orderCanceled.canceledQuantity = -3L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(-1L, buffer.getLong());
        assertEquals(-2L, buffer.getLong());
        assertEquals(-3L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithMixedValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = Long.MAX_VALUE;
        orderCanceled.orderNumber = 0L;
        orderCanceled.canceledQuantity = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutMultipleTimes() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 100L;
        orderCanceled.orderNumber = 200L;
        orderCanceled.canceledQuantity = 300L;

        ByteBuffer buffer1 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer1);
        buffer1.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer1.get());
        assertEquals(100L, buffer1.getLong());
        assertEquals(200L, buffer1.getLong());
        assertEquals(300L, buffer1.getLong());

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer2.get());
        assertEquals(100L, buffer2.getLong());
        assertEquals(200L, buffer2.getLong());
        assertEquals(300L, buffer2.getLong());
    }

    @Test
    void testOrderCanceledPutWithLargePositiveValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1234567890123456789L;
        orderCanceled.orderNumber = 8765432109876543210L;
        orderCanceled.canceledQuantity = 5555555555555555555L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(1234567890123456789L, buffer.getLong());
        assertEquals(8765432109876543210L, buffer.getLong());
        assertEquals(5555555555555555555L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithOne() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1L;
        orderCanceled.orderNumber = 1L;
        orderCanceled.canceledQuantity = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutAdvancesBufferPosition() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1L;
        orderCanceled.orderNumber = 2L;
        orderCanceled.canceledQuantity = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        assertEquals(0, buffer.position());

        orderCanceled.put(buffer);

        assertEquals(25, buffer.position());
    }

    @Test
    void testOrderCanceledPutWithBufferAtNonZeroPosition() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 10L;
        orderCanceled.orderNumber = 20L;
        orderCanceled.canceledQuantity = 30L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.putLong(999L);

        int positionBeforePut = buffer.position();
        orderCanceled.put(buffer);

        assertEquals(positionBeforePut + 25, buffer.position());

        buffer.flip();
        buffer.getLong();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(10L, buffer.getLong());
        assertEquals(20L, buffer.getLong());
        assertEquals(30L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWritesMessageTypeFirst() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 123L;
        orderCanceled.orderNumber = 456L;
        orderCanceled.canceledQuantity = 789L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, messageType);
        assertEquals('X', messageType);
    }

    @Test
    void testOrderCanceledPutWritesAllThreeFields() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 111L;
        orderCanceled.orderNumber = 222L;
        orderCanceled.canceledQuantity = 333L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();

        long readTimestamp = buffer.getLong();
        long readOrderNumber = buffer.getLong();
        long readCanceledQuantity = buffer.getLong();

        assertEquals(111L, readTimestamp);
        assertEquals(222L, readOrderNumber);
        assertEquals(333L, readCanceledQuantity);
    }

    @Test
    void testOrderCanceledPutTotalBytesWritten() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1L;
        orderCanceled.orderNumber = 2L;
        orderCanceled.canceledQuantity = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        int startPosition = buffer.position();
        orderCanceled.put(buffer);
        int endPosition = buffer.position();

        assertEquals(25, endPosition - startPosition);
        assertEquals(1 + 8 + 8 + 8, endPosition - startPosition);
    }

    @Test
    void testOrderCanceledPutWithSequentialValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1L;
        orderCanceled.orderNumber = 2L;
        orderCanceled.canceledQuantity = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(2L, buffer.getLong());
        assertEquals(3L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutRespectsByteOrder() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1000L;
        orderCanceled.orderNumber = 2000L;
        orderCanceled.canceledQuantity = 3000L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(2000L, buffer.getLong());
        assertEquals(3000L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithModifiedValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 100L;
        orderCanceled.orderNumber = 200L;
        orderCanceled.canceledQuantity = 300L;

        ByteBuffer buffer1 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer1);
        buffer1.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer1.get());
        assertEquals(100L, buffer1.getLong());

        orderCanceled.timestamp = 400L;
        orderCanceled.orderNumber = 500L;
        orderCanceled.canceledQuantity = 600L;

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer2.get());
        assertEquals(400L, buffer2.getLong());
        assertEquals(500L, buffer2.getLong());
        assertEquals(600L, buffer2.getLong());
    }

    @Test
    void testOrderCanceledPutToLargerBuffer() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 777L;
        orderCanceled.orderNumber = 888L;
        orderCanceled.canceledQuantity = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.putLong(111L);

        orderCanceled.put(buffer);

        buffer.putLong(222L);

        buffer.flip();

        assertEquals(111L, buffer.getLong());
        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(777L, buffer.getLong());
        assertEquals(888L, buffer.getLong());
        assertEquals(999L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
    }

    @Test
    void testOrderExecutedPutWithBasicValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.orderNumber = 12345L;
        orderExecuted.quantity = 100L;
        orderExecuted.matchNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(12345L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
        assertEquals(999, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithZeroValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 0L;
        orderExecuted.orderNumber = 0L;
        orderExecuted.quantity = 0L;
        orderExecuted.matchNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithMaxLongValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = Long.MAX_VALUE;
        orderExecuted.orderNumber = Long.MAX_VALUE;
        orderExecuted.quantity = Long.MAX_VALUE;
        orderExecuted.matchNumber = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(-1, buffer.getInt());  // 0xFFFFFFFF as signed int is -1
    }

    @Test
    void testOrderExecutedPutWithMinLongValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = Long.MIN_VALUE;
        orderExecuted.orderNumber = Long.MIN_VALUE;
        orderExecuted.quantity = Long.MIN_VALUE;
        orderExecuted.matchNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(0, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithNegativeValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = -1L;
        orderExecuted.orderNumber = -2L;
        orderExecuted.quantity = -3L;
        orderExecuted.matchNumber = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(-1L, buffer.getLong());
        assertEquals(-2L, buffer.getLong());
        assertEquals(-3L, buffer.getLong());
        assertEquals(100, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithUnsignedIntMaxValue() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.orderNumber = 2000L;
        orderExecuted.quantity = 3000L;
        orderExecuted.matchNumber = 4294967295L;  // Max unsigned int value

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(2000L, buffer.getLong());
        assertEquals(3000L, buffer.getLong());
        assertEquals(-1, buffer.getInt());  // 4294967295L stored as int becomes -1
    }

    @Test
    void testOrderExecutedPutMultipleTimes() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 100L;
        orderExecuted.orderNumber = 200L;
        orderExecuted.quantity = 300L;
        orderExecuted.matchNumber = 400L;

        ByteBuffer buffer1 = ByteBuffer.allocate(29);
        orderExecuted.put(buffer1);
        buffer1.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer1.get());
        assertEquals(100L, buffer1.getLong());
        assertEquals(200L, buffer1.getLong());
        assertEquals(300L, buffer1.getLong());
        assertEquals(400, buffer1.getInt());

        ByteBuffer buffer2 = ByteBuffer.allocate(29);
        orderExecuted.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer2.get());
        assertEquals(100L, buffer2.getLong());
        assertEquals(200L, buffer2.getLong());
        assertEquals(300L, buffer2.getLong());
        assertEquals(400, buffer2.getInt());
    }

    @Test
    void testOrderExecutedPutAdvancesBufferPosition() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1L;
        orderExecuted.orderNumber = 2L;
        orderExecuted.quantity = 3L;
        orderExecuted.matchNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        assertEquals(0, buffer.position());

        orderExecuted.put(buffer);

        assertEquals(29, buffer.position());
    }

    @Test
    void testOrderExecutedPutWritesMessageTypeFirst() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 123L;
        orderExecuted.orderNumber = 456L;
        orderExecuted.quantity = 789L;
        orderExecuted.matchNumber = 101L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, messageType);
        assertEquals('E', messageType);
    }

    @Test
    void testOrderExecutedPutWritesAllFourFields() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 111L;
        orderExecuted.orderNumber = 222L;
        orderExecuted.quantity = 333L;
        orderExecuted.matchNumber = 444L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        buffer.get();  // Skip message type

        long readTimestamp = buffer.getLong();
        long readOrderNumber = buffer.getLong();
        long readQuantity = buffer.getLong();
        int readMatchNumber = buffer.getInt();

        assertEquals(111L, readTimestamp);
        assertEquals(222L, readOrderNumber);
        assertEquals(333L, readQuantity);
        assertEquals(444, readMatchNumber);
    }

    @Test
    void testOrderExecutedPutTotalBytesWritten() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1L;
        orderExecuted.orderNumber = 2L;
        orderExecuted.quantity = 3L;
        orderExecuted.matchNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        int startPosition = buffer.position();
        orderExecuted.put(buffer);
        int endPosition = buffer.position();

        assertEquals(29, endPosition - startPosition);
        assertEquals(1 + 8 + 8 + 8 + 4, endPosition - startPosition);
    }

    @Test
    void testOrderExecutedPutWithLargePositiveValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1234567890123456789L;
        orderExecuted.orderNumber = 8765432109876543210L;
        orderExecuted.quantity = 5555555555555555555L;
        orderExecuted.matchNumber = Integer.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(1234567890123456789L, buffer.getLong());
        assertEquals(8765432109876543210L, buffer.getLong());
        assertEquals(5555555555555555555L, buffer.getLong());
        assertEquals(Integer.MAX_VALUE, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithBufferAtNonZeroPosition() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 10L;
        orderExecuted.orderNumber = 20L;
        orderExecuted.quantity = 30L;
        orderExecuted.matchNumber = 40L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.putLong(999L);

        int positionBeforePut = buffer.position();
        orderExecuted.put(buffer);

        assertEquals(positionBeforePut + 29, buffer.position());

        buffer.flip();
        buffer.getLong();  // Skip the prepended 999L

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(10L, buffer.getLong());
        assertEquals(20L, buffer.getLong());
        assertEquals(30L, buffer.getLong());
        assertEquals(40, buffer.getInt());
    }

    @Test
    void testOrderExecutedPutWithSequentialValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1L;
        orderExecuted.orderNumber = 2L;
        orderExecuted.quantity = 3L;
        orderExecuted.matchNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(2L, buffer.getLong());
        assertEquals(3L, buffer.getLong());
        assertEquals(4, buffer.getInt());
    }

    // Tests for PMD.Version.put()

    @Test
    void testVersionPutWithBasicValue() {
        PMD.Version version = new PMD.Version();
        version.version = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());  // MESSAGE_TYPE_VERSION
        assertEquals(999L, buffer.getInt() & 0xFFFFFFFFL);
    }

    @Test
    void testVersionPutWithZeroValue() {
        PMD.Version version = new PMD.Version();
        version.version = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals(0L, buffer.getInt() & 0xFFFFFFFFL);
    }

    @Test
    void testVersionPutWithMaxUnsignedIntValue() {
        PMD.Version version = new PMD.Version();
        version.version = 0xFFFFFFFFL;  // Max unsigned int (4294967295)

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals(0xFFFFFFFFL, buffer.getInt() & 0xFFFFFFFFL);
    }

    @Test
    void testVersionPutAdvancesBufferPosition() {
        PMD.Version version = new PMD.Version();
        version.version = 42L;

        ByteBuffer buffer = ByteBuffer.allocate(10);
        assertEquals(0, buffer.position());

        version.put(buffer);

        assertEquals(5, buffer.position());  // 1 byte message type + 4 bytes version
    }

    @Test
    void testVersionPutTotalBytesWritten() {
        PMD.Version version = new PMD.Version();
        version.version = 123L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        int startPosition = buffer.position();
        version.put(buffer);
        int endPosition = buffer.position();

        assertEquals(5, endPosition - startPosition);  // MESSAGE_LENGTH_VERSION = 5
    }
}
