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

class PMRClaude_putTest {

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
    void testVersionPutWritesVersion() {
        PMR.Version version = new PMR.Version();
        version.version = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(999, buffer.getInt());
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
    void testVersionPutAdvancesBufferPosition() {
        PMR.Version version = new PMR.Version();
        version.version = 50L;

        ByteBuffer buffer = ByteBuffer.allocate(10);
        assertEquals(0, buffer.position());

        version.put(buffer);

        assertEquals(5, buffer.position());
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
    void testVersionPutWritesFieldsInCorrectOrder() {
        PMR.Version version = new PMR.Version();
        version.version = 555L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(555, buffer.getInt());
    }

    @Test
    void testVersionPutMessageTypeIsV() {
        PMR.Version version = new PMR.Version();
        version.version = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
    }

    @Test
    void testVersionPutWithProtocolVersion() {
        PMR.Version version = new PMR.Version();
        version.version = PMR.VERSION;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(2, buffer.getInt());
    }

    @Test
    void testVersionPutAfterFieldChange() {
        PMR.Version version = new PMR.Version();
        version.version = 100L;
        version.version = 200L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(200, buffer.getInt());
    }

    @Test
    void testOrderCanceledPutWritesMessageType() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 1000L;
        orderCanceled.orderNumber = 2000L;
        orderCanceled.canceledQuantity = 3000L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
    }

    @Test
    void testOrderCanceledPutWritesTimestamp() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 1234567890L;
        orderCanceled.orderNumber = 2000L;
        orderCanceled.canceledQuantity = 3000L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(1234567890L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWritesOrderNumber() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 1000L;
        orderCanceled.orderNumber = 9876543210L;
        orderCanceled.canceledQuantity = 3000L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        assertEquals(9876543210L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWritesCanceledQuantity() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 1000L;
        orderCanceled.orderNumber = 2000L;
        orderCanceled.canceledQuantity = 5555555555L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        assertEquals(5555555555L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWritesAllFields() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 100L;
        orderCanceled.orderNumber = 200L;
        orderCanceled.canceledQuantity = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithZeroValues() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 0L;
        orderCanceled.orderNumber = 0L;
        orderCanceled.canceledQuantity = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithMaxLongValues() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = Long.MAX_VALUE;
        orderCanceled.orderNumber = Long.MAX_VALUE;
        orderCanceled.canceledQuantity = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithMinLongValues() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = Long.MIN_VALUE;
        orderCanceled.orderNumber = Long.MIN_VALUE;
        orderCanceled.canceledQuantity = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithNegativeValues() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = -100L;
        orderCanceled.orderNumber = -200L;
        orderCanceled.canceledQuantity = -300L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(-100L, buffer.getLong());
        assertEquals(-200L, buffer.getLong());
        assertEquals(-300L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithMixedValues() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = Long.MAX_VALUE;
        orderCanceled.orderNumber = 0L;
        orderCanceled.canceledQuantity = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutAdvancesBufferPosition() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
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
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 10L;
        orderCanceled.orderNumber = 20L;
        orderCanceled.canceledQuantity = 30L;

        ByteBuffer buffer = ByteBuffer.allocate(40);
        buffer.putLong(999L);

        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'X', buffer.get());
        assertEquals(10L, buffer.getLong());
        assertEquals(20L, buffer.getLong());
        assertEquals(30L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutMultipleTimes() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 100L;
        orderCanceled.orderNumber = 200L;
        orderCanceled.canceledQuantity = 300L;

        ByteBuffer buffer1 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'X', buffer1.get());
        assertEquals(100L, buffer1.getLong());
        assertEquals(200L, buffer1.getLong());
        assertEquals(300L, buffer1.getLong());

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'X', buffer2.get());
        assertEquals(100L, buffer2.getLong());
        assertEquals(200L, buffer2.getLong());
        assertEquals(300L, buffer2.getLong());
    }

    @Test
    void testOrderCanceledPutWithLargePositiveValues() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 1234567890123456789L;
        orderCanceled.orderNumber = 8765432109876543210L;
        orderCanceled.canceledQuantity = 5555555555555555555L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(1234567890123456789L, buffer.getLong());
        assertEquals(8765432109876543210L, buffer.getLong());
        assertEquals(5555555555555555555L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWithOne() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 1L;
        orderCanceled.orderNumber = 1L;
        orderCanceled.canceledQuantity = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutWritesFieldsInCorrectOrder() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 111L;
        orderCanceled.orderNumber = 222L;
        orderCanceled.canceledQuantity = 333L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(111L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
        assertEquals(333L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutMessageTypeIsX() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 5000L;
        orderCanceled.orderNumber = 6000L;
        orderCanceled.canceledQuantity = 7000L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals((byte) 'X', messageType);
    }

    @Test
    void testOrderCanceledPutAfterMultipleFieldChanges() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 100L;
        orderCanceled.orderNumber = 200L;
        orderCanceled.canceledQuantity = 300L;

        orderCanceled.timestamp = 400L;
        orderCanceled.orderNumber = 500L;
        orderCanceled.canceledQuantity = 600L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(400L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(600L, buffer.getLong());
    }

    @Test
    void testOrderCanceledPutSequentialValues() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 1L;
        orderCanceled.orderNumber = 2L;
        orderCanceled.canceledQuantity = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(2L, buffer.getLong());
        assertEquals(3L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWithBasicValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(2000L, buffer.getLong());
        assertEquals(3000L, buffer.getLong());
        assertEquals((byte) 'B', buffer.get());
        assertEquals(4000L, buffer.getLong());
        assertEquals(5000L, buffer.getLong());
        assertEquals(6000L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWithZeroValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 0L;
        orderEntered.username = 0L;
        orderEntered.orderNumber = 0L;
        orderEntered.side = (byte) 0;
        orderEntered.instrument = 0L;
        orderEntered.quantity = 0L;
        orderEntered.price = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals((byte) 0, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWithMaxLongValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = Long.MAX_VALUE;
        orderEntered.username = Long.MAX_VALUE;
        orderEntered.orderNumber = Long.MAX_VALUE;
        orderEntered.side = Byte.MAX_VALUE;
        orderEntered.instrument = Long.MAX_VALUE;
        orderEntered.quantity = Long.MAX_VALUE;
        orderEntered.price = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Byte.MAX_VALUE, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWithMinLongValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = Long.MIN_VALUE;
        orderEntered.username = Long.MIN_VALUE;
        orderEntered.orderNumber = Long.MIN_VALUE;
        orderEntered.side = Byte.MIN_VALUE;
        orderEntered.instrument = Long.MIN_VALUE;
        orderEntered.quantity = Long.MIN_VALUE;
        orderEntered.price = Long.MIN_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Byte.MIN_VALUE, buffer.get());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWithNegativeValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = -1L;
        orderEntered.username = -2L;
        orderEntered.orderNumber = -3L;
        orderEntered.side = (byte) -4;
        orderEntered.instrument = -5L;
        orderEntered.quantity = -6L;
        orderEntered.price = -7L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(-1L, buffer.getLong());
        assertEquals(-2L, buffer.getLong());
        assertEquals(-3L, buffer.getLong());
        assertEquals((byte) -4, buffer.get());
        assertEquals(-5L, buffer.getLong());
        assertEquals(-6L, buffer.getLong());
        assertEquals(-7L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWithSellSide() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'S';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        assertEquals((byte) 'S', buffer.get());
    }

    @Test
    void testOrderEnteredPutWithMixedValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = Long.MAX_VALUE;
        orderEntered.username = 0L;
        orderEntered.orderNumber = Long.MIN_VALUE;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 1L;
        orderEntered.quantity = -1L;
        orderEntered.price = 1000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(Long.MIN_VALUE, buffer.getLong());
        assertEquals((byte) 'B', buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(-1L, buffer.getLong());
        assertEquals(1000L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutAdvancesBufferPosition() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1L;
        orderEntered.username = 2L;
        orderEntered.orderNumber = 3L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4L;
        orderEntered.quantity = 5L;
        orderEntered.price = 6L;

        ByteBuffer buffer = ByteBuffer.allocate(100);
        assertEquals(0, buffer.position());

        orderEntered.put(buffer);

        assertEquals(50, buffer.position());
    }

    @Test
    void testOrderEnteredPutWithBufferAtNonZeroPosition() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 10L;
        orderEntered.username = 20L;
        orderEntered.orderNumber = 30L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 40L;
        orderEntered.quantity = 50L;
        orderEntered.price = 60L;

        ByteBuffer buffer = ByteBuffer.allocate(60);
        buffer.putLong(999L);

        orderEntered.put(buffer);
        buffer.flip();

        assertEquals(999L, buffer.getLong());
        assertEquals((byte) 'E', buffer.get());
        assertEquals(10L, buffer.getLong());
        assertEquals(20L, buffer.getLong());
        assertEquals(30L, buffer.getLong());
        assertEquals((byte) 'B', buffer.get());
        assertEquals(40L, buffer.getLong());
        assertEquals(50L, buffer.getLong());
        assertEquals(60L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutMultipleTimes() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 100L;
        orderEntered.username = 200L;
        orderEntered.orderNumber = 300L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 400L;
        orderEntered.quantity = 500L;
        orderEntered.price = 600L;

        ByteBuffer buffer1 = ByteBuffer.allocate(50);
        orderEntered.put(buffer1);
        buffer1.flip();

        assertEquals((byte) 'E', buffer1.get());
        assertEquals(100L, buffer1.getLong());
        assertEquals(200L, buffer1.getLong());
        assertEquals(300L, buffer1.getLong());
        assertEquals((byte) 'B', buffer1.get());
        assertEquals(400L, buffer1.getLong());
        assertEquals(500L, buffer1.getLong());
        assertEquals(600L, buffer1.getLong());

        ByteBuffer buffer2 = ByteBuffer.allocate(50);
        orderEntered.put(buffer2);
        buffer2.flip();

        assertEquals((byte) 'E', buffer2.get());
        assertEquals(100L, buffer2.getLong());
        assertEquals(200L, buffer2.getLong());
        assertEquals(300L, buffer2.getLong());
        assertEquals((byte) 'B', buffer2.get());
        assertEquals(400L, buffer2.getLong());
        assertEquals(500L, buffer2.getLong());
        assertEquals(600L, buffer2.getLong());
    }

    @Test
    void testOrderEnteredPutWithLargePositiveValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1234567890123456789L;
        orderEntered.username = 2345678901234567890L;
        orderEntered.orderNumber = 3456789012345678901L;
        orderEntered.side = (byte) 100;
        orderEntered.instrument = 4567890123456789012L;
        orderEntered.quantity = 5678901234567890123L;
        orderEntered.price = 6789012345678901234L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1234567890123456789L, buffer.getLong());
        assertEquals(2345678901234567890L, buffer.getLong());
        assertEquals(3456789012345678901L, buffer.getLong());
        assertEquals((byte) 100, buffer.get());
        assertEquals(4567890123456789012L, buffer.getLong());
        assertEquals(5678901234567890123L, buffer.getLong());
        assertEquals(6789012345678901234L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWithOne() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1L;
        orderEntered.username = 1L;
        orderEntered.orderNumber = 1L;
        orderEntered.side = (byte) 1;
        orderEntered.instrument = 1L;
        orderEntered.quantity = 1L;
        orderEntered.price = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals((byte) 1, buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
        assertEquals(1L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWritesFieldsInCorrectOrder() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 111L;
        orderEntered.username = 222L;
        orderEntered.orderNumber = 333L;
        orderEntered.side = (byte) 44;
        orderEntered.instrument = 555L;
        orderEntered.quantity = 666L;
        orderEntered.price = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(111L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
        assertEquals(333L, buffer.getLong());
        assertEquals((byte) 44, buffer.get());
        assertEquals(555L, buffer.getLong());
        assertEquals(666L, buffer.getLong());
        assertEquals(777L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWritesMessageType() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals((byte) 'E', messageType);
    }

    @Test
    void testOrderEnteredPutMessageTypeIsE() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 5000L;
        orderEntered.username = 6000L;
        orderEntered.orderNumber = 7000L;
        orderEntered.side = (byte) 'S';
        orderEntered.instrument = 8000L;
        orderEntered.quantity = 9000L;
        orderEntered.price = 10000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
    }

    @Test
    void testOrderEnteredPutWritesTimestamp() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1234567890L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(1234567890L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWritesUsername() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 9876543210L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        assertEquals(9876543210L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWritesOrderNumber() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 5555555555L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        assertEquals(5555555555L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWritesSide() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        assertEquals((byte) 'B', buffer.get());
    }

    @Test
    void testOrderEnteredPutWritesInstrument() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 8888888888L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        buffer.get();
        assertEquals(8888888888L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWritesQuantity() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 7777777777L;
        orderEntered.price = 6000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        buffer.get();
        buffer.getLong();
        assertEquals(7777777777L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWritesPrice() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6666666666L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        buffer.get();
        buffer.getLong();
        buffer.getLong();
        assertEquals(6666666666L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutAfterMultipleFieldChanges() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 100L;
        orderEntered.username = 200L;
        orderEntered.orderNumber = 300L;
        orderEntered.side = (byte) 'B';
        orderEntered.instrument = 400L;
        orderEntered.quantity = 500L;
        orderEntered.price = 600L;

        orderEntered.timestamp = 700L;
        orderEntered.username = 800L;
        orderEntered.orderNumber = 900L;
        orderEntered.side = (byte) 'S';
        orderEntered.instrument = 1000L;
        orderEntered.quantity = 1100L;
        orderEntered.price = 1200L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(700L, buffer.getLong());
        assertEquals(800L, buffer.getLong());
        assertEquals(900L, buffer.getLong());
        assertEquals((byte) 'S', buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(1100L, buffer.getLong());
        assertEquals(1200L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutSequentialValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1L;
        orderEntered.username = 2L;
        orderEntered.orderNumber = 3L;
        orderEntered.side = (byte) 4;
        orderEntered.instrument = 5L;
        orderEntered.quantity = 6L;
        orderEntered.price = 7L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(2L, buffer.getLong());
        assertEquals(3L, buffer.getLong());
        assertEquals((byte) 4, buffer.get());
        assertEquals(5L, buffer.getLong());
        assertEquals(6L, buffer.getLong());
        assertEquals(7L, buffer.getLong());
    }

    @Test
    void testOrderEnteredPutWritesAllSevenFields() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 100L;
        orderEntered.username = 200L;
        orderEntered.orderNumber = 300L;
        orderEntered.side = (byte) 'S';
        orderEntered.instrument = 400L;
        orderEntered.quantity = 500L;
        orderEntered.price = 600L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderEntered.put(buffer);
        buffer.flip();

        assertEquals((byte) 'E', buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
        assertEquals((byte) 'S', buffer.get());
        assertEquals(400L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(600L, buffer.getLong());
    }

    @Test
    void testTradePutWritesMessageType() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1000L;
        trade.restingOrderNumber = 2000L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity = 4000L;
        trade.matchNumber = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
    }

    @Test
    void testTradePutWritesTimestamp() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1234567890L;
        trade.restingOrderNumber = 2000L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity = 4000L;
        trade.matchNumber = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        buffer.get();
        assertEquals(1234567890L, buffer.getLong());
    }

    @Test
    void testTradePutWritesRestingOrderNumber() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1000L;
        trade.restingOrderNumber = 9876543210L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity = 4000L;
        trade.matchNumber = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        assertEquals(9876543210L, buffer.getLong());
    }

    @Test
    void testTradePutWritesIncomingOrderNumber() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1000L;
        trade.restingOrderNumber = 2000L;
        trade.incomingOrderNumber = 5555555555L;
        trade.quantity = 4000L;
        trade.matchNumber = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        assertEquals(5555555555L, buffer.getLong());
    }

    @Test
    void testTradePutWritesQuantity() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1000L;
        trade.restingOrderNumber = 2000L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity = 8888888888L;
        trade.matchNumber = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        assertEquals(8888888888L, buffer.getLong());
    }

    @Test
    void testTradePutWritesMatchNumber() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1000L;
        trade.restingOrderNumber = 2000L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity = 4000L;
        trade.matchNumber = 7777777777L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        buffer.get();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        buffer.getLong();
        assertEquals(7777777777L & 0xFFFFFFFFL, buffer.getInt() & 0xFFFFFFFFL);
    }

    @Test
    void testTradePutWritesAllFields() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 100L;
        trade.restingOrderNumber = 200L;
        trade.incomingOrderNumber = 300L;
        trade.quantity = 400L;
        trade.matchNumber = 500L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(100L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(300L, buffer.getLong());
        assertEquals(400L, buffer.getLong());
        assertEquals(500, buffer.getInt());
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
    void testTradePutMessageTypeIsT() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 5000L;
        trade.restingOrderNumber = 6000L;
        trade.incomingOrderNumber = 7000L;
        trade.quantity = 8000L;
        trade.matchNumber = 9000L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals((byte) 'T', messageType);
    }

    @Test
    void testTradePutAfterMultipleFieldChanges() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 100L;
        trade.restingOrderNumber = 200L;
        trade.incomingOrderNumber = 300L;
        trade.quantity = 400L;
        trade.matchNumber = 50L;

        trade.timestamp = 500L;
        trade.restingOrderNumber = 600L;
        trade.incomingOrderNumber = 700L;
        trade.quantity = 800L;
        trade.matchNumber = 75L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(500L, buffer.getLong());
        assertEquals(600L, buffer.getLong());
        assertEquals(700L, buffer.getLong());
        assertEquals(800L, buffer.getLong());
        assertEquals(75, buffer.getInt());
    }

    @Test
    void testTradePutSequentialValues() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1L;
        trade.restingOrderNumber = 2L;
        trade.incomingOrderNumber = 3L;
        trade.quantity = 4L;
        trade.matchNumber = 5L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte) 'T', buffer.get());
        assertEquals(1L, buffer.getLong());
        assertEquals(2L, buffer.getLong());
        assertEquals(3L, buffer.getLong());
        assertEquals(4L, buffer.getLong());
        assertEquals(5, buffer.getInt());
    }
}
