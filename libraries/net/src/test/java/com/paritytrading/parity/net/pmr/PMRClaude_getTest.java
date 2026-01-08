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

class PMRClaude_getTest {

    @Test
    void testOrderCanceledGetReadsTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1000L, orderCanceled.timestamp);
    }

    @Test
    void testOrderCanceledGetReadsOrderNumber() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(2000L, orderCanceled.orderNumber);
    }

    @Test
    void testOrderCanceledGetReadsCanceledQuantity() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(3000L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetReadsAllThreeFields() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(100L, orderCanceled.timestamp);
        assertEquals(200L, orderCanceled.orderNumber);
        assertEquals(300L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(0L, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.orderNumber);
        assertEquals(0L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithMaxLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(Long.MAX_VALUE, orderCanceled.timestamp);
        assertEquals(Long.MAX_VALUE, orderCanceled.orderNumber);
        assertEquals(Long.MAX_VALUE, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithMinLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(Long.MIN_VALUE, orderCanceled.timestamp);
        assertEquals(Long.MIN_VALUE, orderCanceled.orderNumber);
        assertEquals(Long.MIN_VALUE, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(-100L);
        buffer.putLong(-200L);
        buffer.putLong(-300L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(-100L, orderCanceled.timestamp);
        assertEquals(-200L, orderCanceled.orderNumber);
        assertEquals(-300L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetOverwritesPreviousValues() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.timestamp = 999L;
        orderCanceled.orderNumber = 888L;
        orderCanceled.canceledQuantity = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.flip();

        orderCanceled.get(buffer);

        assertEquals(111L, orderCanceled.timestamp);
        assertEquals(222L, orderCanceled.orderNumber);
        assertEquals(333L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithSequentialValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1L, orderCanceled.timestamp);
        assertEquals(2L, orderCanceled.orderNumber);
        assertEquals(3L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.putLong(10L);
        buffer.putLong(20L);
        buffer.putLong(30L);
        buffer.putLong(40L);
        buffer.flip();

        assertEquals(0, buffer.position());

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(24, buffer.position());
    }

    @Test
    void testOrderCanceledGetWithLargePositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1234567890123456789L);
        buffer.putLong(8765432109876543210L);
        buffer.putLong(5555555555555555555L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1234567890123456789L, orderCanceled.timestamp);
        assertEquals(8765432109876543210L, orderCanceled.orderNumber);
        assertEquals(5555555555555555555L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithMixedValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(0L);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(Long.MAX_VALUE, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.orderNumber);
        assertEquals(Long.MIN_VALUE, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetMultipleTimes() {
        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();

        ByteBuffer buffer1 = ByteBuffer.allocate(24);
        buffer1.putLong(100L);
        buffer1.putLong(200L);
        buffer1.putLong(300L);
        buffer1.flip();

        orderCanceled.get(buffer1);
        assertEquals(100L, orderCanceled.timestamp);
        assertEquals(200L, orderCanceled.orderNumber);
        assertEquals(300L, orderCanceled.canceledQuantity);

        ByteBuffer buffer2 = ByteBuffer.allocate(24);
        buffer2.putLong(400L);
        buffer2.putLong(500L);
        buffer2.putLong(600L);
        buffer2.flip();

        orderCanceled.get(buffer2);
        assertEquals(400L, orderCanceled.timestamp);
        assertEquals(500L, orderCanceled.orderNumber);
        assertEquals(600L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.putLong(999L);
        buffer.putLong(10L);
        buffer.putLong(20L);
        buffer.putLong(30L);
        buffer.flip();

        buffer.getLong();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(10L, orderCanceled.timestamp);
        assertEquals(20L, orderCanceled.orderNumber);
        assertEquals(30L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithOne() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1L, orderCanceled.timestamp);
        assertEquals(1L, orderCanceled.orderNumber);
        assertEquals(1L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetFieldsInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.flip();

        PMR.OrderCanceled orderCanceled = new PMR.OrderCanceled();

        assertEquals(0L, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.orderNumber);
        assertEquals(0L, orderCanceled.canceledQuantity);

        orderCanceled.get(buffer);

        assertEquals(111L, orderCanceled.timestamp);
        assertEquals(222L, orderCanceled.orderNumber);
        assertEquals(333L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderEnteredGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.put((byte) 'B');
        buffer.putLong(4000L);
        buffer.putLong(5000L);
        buffer.putLong(6000L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(1000L, orderEntered.timestamp);
        assertEquals(2000L, orderEntered.username);
        assertEquals(3000L, orderEntered.orderNumber);
        assertEquals((byte) 'B', orderEntered.side);
        assertEquals(4000L, orderEntered.instrument);
        assertEquals(5000L, orderEntered.quantity);
        assertEquals(6000L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(0L, orderEntered.timestamp);
        assertEquals(0L, orderEntered.username);
        assertEquals(0L, orderEntered.orderNumber);
        assertEquals((byte) 0, orderEntered.side);
        assertEquals(0L, orderEntered.instrument);
        assertEquals(0L, orderEntered.quantity);
        assertEquals(0L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithMaxLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.put(Byte.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(Long.MAX_VALUE, orderEntered.timestamp);
        assertEquals(Long.MAX_VALUE, orderEntered.username);
        assertEquals(Long.MAX_VALUE, orderEntered.orderNumber);
        assertEquals(Byte.MAX_VALUE, orderEntered.side);
        assertEquals(Long.MAX_VALUE, orderEntered.instrument);
        assertEquals(Long.MAX_VALUE, orderEntered.quantity);
        assertEquals(Long.MAX_VALUE, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithMinLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.put(Byte.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(Long.MIN_VALUE, orderEntered.timestamp);
        assertEquals(Long.MIN_VALUE, orderEntered.username);
        assertEquals(Long.MIN_VALUE, orderEntered.orderNumber);
        assertEquals(Byte.MIN_VALUE, orderEntered.side);
        assertEquals(Long.MIN_VALUE, orderEntered.instrument);
        assertEquals(Long.MIN_VALUE, orderEntered.quantity);
        assertEquals(Long.MIN_VALUE, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(-1L);
        buffer.putLong(-2L);
        buffer.putLong(-3L);
        buffer.put((byte) -4);
        buffer.putLong(-5L);
        buffer.putLong(-6L);
        buffer.putLong(-7L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(-1L, orderEntered.timestamp);
        assertEquals(-2L, orderEntered.username);
        assertEquals(-3L, orderEntered.orderNumber);
        assertEquals((byte) -4, orderEntered.side);
        assertEquals(-5L, orderEntered.instrument);
        assertEquals(-6L, orderEntered.quantity);
        assertEquals(-7L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithSellSide() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.put((byte) 'S');
        buffer.putLong(4000L);
        buffer.putLong(5000L);
        buffer.putLong(6000L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals((byte) 'S', orderEntered.side);
    }

    @Test
    void testOrderEnteredGetOverwritesPreviousValues() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 999L;
        orderEntered.username = 888L;
        orderEntered.orderNumber = 777L;
        orderEntered.side = (byte) 'X';
        orderEntered.instrument = 666L;
        orderEntered.quantity = 555L;
        orderEntered.price = 444L;

        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.put((byte) 'B');
        buffer.putLong(444L);
        buffer.putLong(555L);
        buffer.putLong(666L);
        buffer.flip();

        orderEntered.get(buffer);

        assertEquals(111L, orderEntered.timestamp);
        assertEquals(222L, orderEntered.username);
        assertEquals(333L, orderEntered.orderNumber);
        assertEquals((byte) 'B', orderEntered.side);
        assertEquals(444L, orderEntered.instrument);
        assertEquals(555L, orderEntered.quantity);
        assertEquals(666L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithSequentialValues() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.put((byte) 4);
        buffer.putLong(5L);
        buffer.putLong(6L);
        buffer.putLong(7L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(1L, orderEntered.timestamp);
        assertEquals(2L, orderEntered.username);
        assertEquals(3L, orderEntered.orderNumber);
        assertEquals((byte) 4, orderEntered.side);
        assertEquals(5L, orderEntered.instrument);
        assertEquals(6L, orderEntered.quantity);
        assertEquals(7L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(10L);
        buffer.putLong(20L);
        buffer.putLong(30L);
        buffer.put((byte) 'B');
        buffer.putLong(40L);
        buffer.putLong(50L);
        buffer.putLong(60L);
        buffer.putLong(70L);
        buffer.flip();

        assertEquals(0, buffer.position());

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(49, buffer.position());
        assertEquals(70L, buffer.getLong());
    }

    @Test
    void testOrderEnteredGetWithLargePositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(1234567890123456789L);
        buffer.putLong(2345678901234567890L);
        buffer.putLong(3456789012345678901L);
        buffer.put((byte) 100);
        buffer.putLong(4567890123456789012L);
        buffer.putLong(5678901234567890123L);
        buffer.putLong(6789012345678901234L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(1234567890123456789L, orderEntered.timestamp);
        assertEquals(2345678901234567890L, orderEntered.username);
        assertEquals(3456789012345678901L, orderEntered.orderNumber);
        assertEquals((byte) 100, orderEntered.side);
        assertEquals(4567890123456789012L, orderEntered.instrument);
        assertEquals(5678901234567890123L, orderEntered.quantity);
        assertEquals(6789012345678901234L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithMixedValues() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(0L);
        buffer.putLong(Long.MIN_VALUE);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(-1L);
        buffer.putLong(1000L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(Long.MAX_VALUE, orderEntered.timestamp);
        assertEquals(0L, orderEntered.username);
        assertEquals(Long.MIN_VALUE, orderEntered.orderNumber);
        assertEquals((byte) 'B', orderEntered.side);
        assertEquals(1L, orderEntered.instrument);
        assertEquals(-1L, orderEntered.quantity);
        assertEquals(1000L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetMultipleTimes() {
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();

        ByteBuffer buffer1 = ByteBuffer.allocate(49);
        buffer1.putLong(100L);
        buffer1.putLong(200L);
        buffer1.putLong(300L);
        buffer1.put((byte) 'B');
        buffer1.putLong(400L);
        buffer1.putLong(500L);
        buffer1.putLong(600L);
        buffer1.flip();

        orderEntered.get(buffer1);
        assertEquals(100L, orderEntered.timestamp);
        assertEquals(200L, orderEntered.username);
        assertEquals(300L, orderEntered.orderNumber);
        assertEquals((byte) 'B', orderEntered.side);
        assertEquals(400L, orderEntered.instrument);
        assertEquals(500L, orderEntered.quantity);
        assertEquals(600L, orderEntered.price);

        ByteBuffer buffer2 = ByteBuffer.allocate(49);
        buffer2.putLong(700L);
        buffer2.putLong(800L);
        buffer2.putLong(900L);
        buffer2.put((byte) 'S');
        buffer2.putLong(1000L);
        buffer2.putLong(1100L);
        buffer2.putLong(1200L);
        buffer2.flip();

        orderEntered.get(buffer2);
        assertEquals(700L, orderEntered.timestamp);
        assertEquals(800L, orderEntered.username);
        assertEquals(900L, orderEntered.orderNumber);
        assertEquals((byte) 'S', orderEntered.side);
        assertEquals(1000L, orderEntered.instrument);
        assertEquals(1100L, orderEntered.quantity);
        assertEquals(1200L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.putLong(999L);
        buffer.putLong(10L);
        buffer.putLong(20L);
        buffer.putLong(30L);
        buffer.put((byte) 'B');
        buffer.putLong(40L);
        buffer.putLong(50L);
        buffer.putLong(60L);
        buffer.flip();

        buffer.getLong();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(10L, orderEntered.timestamp);
        assertEquals(20L, orderEntered.username);
        assertEquals(30L, orderEntered.orderNumber);
        assertEquals((byte) 'B', orderEntered.side);
        assertEquals(40L, orderEntered.instrument);
        assertEquals(50L, orderEntered.quantity);
        assertEquals(60L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetWithOne() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.put((byte) 1);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(1L, orderEntered.timestamp);
        assertEquals(1L, orderEntered.username);
        assertEquals(1L, orderEntered.orderNumber);
        assertEquals((byte) 1, orderEntered.side);
        assertEquals(1L, orderEntered.instrument);
        assertEquals(1L, orderEntered.quantity);
        assertEquals(1L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetFieldsInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.put((byte) 44);
        buffer.putLong(555L);
        buffer.putLong(666L);
        buffer.putLong(777L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();

        assertEquals(0L, orderEntered.timestamp);
        assertEquals(0L, orderEntered.username);
        assertEquals(0L, orderEntered.orderNumber);
        assertEquals((byte) 0, orderEntered.side);
        assertEquals(0L, orderEntered.instrument);
        assertEquals(0L, orderEntered.quantity);
        assertEquals(0L, orderEntered.price);

        orderEntered.get(buffer);

        assertEquals(111L, orderEntered.timestamp);
        assertEquals(222L, orderEntered.username);
        assertEquals(333L, orderEntered.orderNumber);
        assertEquals((byte) 44, orderEntered.side);
        assertEquals(555L, orderEntered.instrument);
        assertEquals(666L, orderEntered.quantity);
        assertEquals(777L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetReadsTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(123456789L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(123456789L, orderEntered.timestamp);
    }

    @Test
    void testOrderEnteredGetReadsUsername() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(0L);
        buffer.putLong(987654321L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(987654321L, orderEntered.username);
    }

    @Test
    void testOrderEnteredGetReadsOrderNumber() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(555555L);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(555555L, orderEntered.orderNumber);
    }

    @Test
    void testOrderEnteredGetReadsSide() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 'B');
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals((byte) 'B', orderEntered.side);
    }

    @Test
    void testOrderEnteredGetReadsInstrument() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putLong(888888L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(888888L, orderEntered.instrument);
    }

    @Test
    void testOrderEnteredGetReadsQuantity() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(777777L);
        buffer.putLong(0L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(777777L, orderEntered.quantity);
    }

    @Test
    void testOrderEnteredGetReadsPrice() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(666666L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(666666L, orderEntered.price);
    }

    @Test
    void testOrderEnteredGetReadsAllSevenFields() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.put((byte) 'S');
        buffer.putLong(400L);
        buffer.putLong(500L);
        buffer.putLong(600L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.get(buffer);

        assertEquals(100L, orderEntered.timestamp);
        assertEquals(200L, orderEntered.username);
        assertEquals(300L, orderEntered.orderNumber);
        assertEquals((byte) 'S', orderEntered.side);
        assertEquals(400L, orderEntered.instrument);
        assertEquals(500L, orderEntered.quantity);
        assertEquals(600L, orderEntered.price);
    }

    @Test
    void testTradeGetReadsTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putInt(5000);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(1000L, trade.timestamp);
    }

    @Test
    void testTradeGetReadsRestingOrderNumber() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putInt(5000);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(2000L, trade.restingOrderNumber);
    }

    @Test
    void testTradeGetReadsIncomingOrderNumber() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putInt(5000);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(3000L, trade.incomingOrderNumber);
    }

    @Test
    void testTradeGetReadsQuantity() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putInt(5000);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(4000L, trade.quantity);
    }

    @Test
    void testTradeGetReadsMatchNumber() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putInt(5000);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(5000L, trade.matchNumber);
    }

    @Test
    void testTradeGetReadsAllFiveFields() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.putLong(444L);
        buffer.putInt(555);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(111L, trade.timestamp);
        assertEquals(222L, trade.restingOrderNumber);
        assertEquals(333L, trade.incomingOrderNumber);
        assertEquals(444L, trade.quantity);
        assertEquals(555L, trade.matchNumber);
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
    void testTradeGetFieldsInCorrectOrder() {
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
}
