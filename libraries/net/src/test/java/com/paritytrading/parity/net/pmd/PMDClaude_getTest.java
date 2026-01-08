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

class PMDClaude_getTest {

    @Test
    void testOrderCanceledGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1000L);
        buffer.putLong(12345L);
        buffer.putLong(100L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1000L, orderCanceled.timestamp);
        assertEquals(12345L, orderCanceled.orderNumber);
        assertEquals(100L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
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

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
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

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(Long.MIN_VALUE, orderCanceled.timestamp);
        assertEquals(Long.MIN_VALUE, orderCanceled.orderNumber);
        assertEquals(Long.MIN_VALUE, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(-1L);
        buffer.putLong(-2L);
        buffer.putLong(-3L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(-1L, orderCanceled.timestamp);
        assertEquals(-2L, orderCanceled.orderNumber);
        assertEquals(-3L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithMixedValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(9223372036854775807L);
        buffer.putLong(0L);
        buffer.putLong(-9223372036854775808L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(9223372036854775807L, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.orderNumber);
        assertEquals(-9223372036854775808L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetMultipleTimes() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();

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
    void testOrderCanceledGetWithLargePositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1234567890123456789L);
        buffer.putLong(8765432109876543210L);
        buffer.putLong(5555555555555555555L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1234567890123456789L, orderCanceled.timestamp);
        assertEquals(8765432109876543210L, orderCanceled.orderNumber);
        assertEquals(5555555555555555555L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithOne() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.putLong(1L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1L, orderCanceled.timestamp);
        assertEquals(1L, orderCanceled.orderNumber);
        assertEquals(1L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetOverwritesPreviousValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
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
    void testOrderCanceledGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putLong(4L);
        buffer.flip();

        assertEquals(0, buffer.position());

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(24, buffer.position());
        assertEquals(4L, buffer.getLong());
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

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(10L, orderCanceled.timestamp);
        assertEquals(20L, orderCanceled.orderNumber);
        assertEquals(30L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetWithDifferentByteOrders() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(5000L);
        buffer.putLong(6000L);
        buffer.putLong(7000L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(5000L, orderCanceled.timestamp);
        assertEquals(6000L, orderCanceled.orderNumber);
        assertEquals(7000L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderCanceledGetReadsAllThreeFields() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();

        assertEquals(0L, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.orderNumber);
        assertEquals(0L, orderCanceled.canceledQuantity);

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

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1L, orderCanceled.timestamp);
        assertEquals(2L, orderCanceled.orderNumber);
        assertEquals(3L, orderCanceled.canceledQuantity);
    }

    @Test
    void testOrderExecutedGetWithBasicValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(1000L);      // timestamp
        buffer.putLong(12345L);     // orderNumber
        buffer.putLong(100L);       // quantity
        buffer.putInt(999);         // matchNumber (unsigned int)
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1000L, orderExecuted.timestamp);
        assertEquals(12345L, orderExecuted.orderNumber);
        assertEquals(100L, orderExecuted.quantity);
        assertEquals(999L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putInt(0);
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(0L, orderExecuted.timestamp);
        assertEquals(0L, orderExecuted.orderNumber);
        assertEquals(0L, orderExecuted.quantity);
        assertEquals(0L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithMaxLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putInt(-1);  // 0xFFFFFFFF = max unsigned int
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(Long.MAX_VALUE, orderExecuted.timestamp);
        assertEquals(Long.MAX_VALUE, orderExecuted.orderNumber);
        assertEquals(Long.MAX_VALUE, orderExecuted.quantity);
        assertEquals(0xFFFFFFFFL, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithMinLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putInt(0);
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(Long.MIN_VALUE, orderExecuted.timestamp);
        assertEquals(Long.MIN_VALUE, orderExecuted.orderNumber);
        assertEquals(Long.MIN_VALUE, orderExecuted.quantity);
        assertEquals(0L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithUnsignedIntMaxValue() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putInt(-1);  // -1 as int = 0xFFFFFFFF = 4294967295 as unsigned
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1000L, orderExecuted.timestamp);
        assertEquals(2000L, orderExecuted.orderNumber);
        assertEquals(3000L, orderExecuted.quantity);
        assertEquals(4294967295L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetMultipleTimes() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();

        ByteBuffer buffer1 = ByteBuffer.allocate(28);
        buffer1.putLong(100L);
        buffer1.putLong(200L);
        buffer1.putLong(300L);
        buffer1.putInt(400);
        buffer1.flip();

        orderExecuted.get(buffer1);
        assertEquals(100L, orderExecuted.timestamp);
        assertEquals(200L, orderExecuted.orderNumber);
        assertEquals(300L, orderExecuted.quantity);
        assertEquals(400L, orderExecuted.matchNumber);

        ByteBuffer buffer2 = ByteBuffer.allocate(28);
        buffer2.putLong(500L);
        buffer2.putLong(600L);
        buffer2.putLong(700L);
        buffer2.putInt(800);
        buffer2.flip();

        orderExecuted.get(buffer2);
        assertEquals(500L, orderExecuted.timestamp);
        assertEquals(600L, orderExecuted.orderNumber);
        assertEquals(700L, orderExecuted.quantity);
        assertEquals(800L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetOverwritesPreviousValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 999L;
        orderExecuted.orderNumber = 888L;
        orderExecuted.quantity = 777L;
        orderExecuted.matchNumber = 666L;

        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.putInt(444);
        buffer.flip();

        orderExecuted.get(buffer);

        assertEquals(111L, orderExecuted.timestamp);
        assertEquals(222L, orderExecuted.orderNumber);
        assertEquals(333L, orderExecuted.quantity);
        assertEquals(444L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetAdvancesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putInt(4);
        buffer.putLong(5L);
        buffer.flip();

        assertEquals(0, buffer.position());

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(28, buffer.position());
        assertEquals(5L, buffer.getLong());
    }

    @Test
    void testOrderExecutedGetWithBufferAtNonZeroPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(999L);
        buffer.putLong(10L);
        buffer.putLong(20L);
        buffer.putLong(30L);
        buffer.putInt(40);
        buffer.flip();

        buffer.getLong();  // Skip first long

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(10L, orderExecuted.timestamp);
        assertEquals(20L, orderExecuted.orderNumber);
        assertEquals(30L, orderExecuted.quantity);
        assertEquals(40L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetReadsAllFourFields() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.putInt(444);
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();

        assertEquals(0L, orderExecuted.timestamp);
        assertEquals(0L, orderExecuted.orderNumber);
        assertEquals(0L, orderExecuted.quantity);
        assertEquals(0L, orderExecuted.matchNumber);

        orderExecuted.get(buffer);

        assertEquals(111L, orderExecuted.timestamp);
        assertEquals(222L, orderExecuted.orderNumber);
        assertEquals(333L, orderExecuted.quantity);
        assertEquals(444L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithLargePositiveValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(1234567890123456789L);
        buffer.putLong(8765432109876543210L);
        buffer.putLong(5555555555555555555L);
        buffer.putInt(Integer.MAX_VALUE);
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1234567890123456789L, orderExecuted.timestamp);
        assertEquals(8765432109876543210L, orderExecuted.orderNumber);
        assertEquals(5555555555555555555L, orderExecuted.quantity);
        assertEquals((long) Integer.MAX_VALUE, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithNegativeLongValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(-1L);
        buffer.putLong(-2L);
        buffer.putLong(-3L);
        buffer.putInt(100);
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(-1L, orderExecuted.timestamp);
        assertEquals(-2L, orderExecuted.orderNumber);
        assertEquals(-3L, orderExecuted.quantity);
        assertEquals(100L, orderExecuted.matchNumber);
    }

    @Test
    void testOrderExecutedGetWithSequentialValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.putInt(4);
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1L, orderExecuted.timestamp);
        assertEquals(2L, orderExecuted.orderNumber);
        assertEquals(3L, orderExecuted.quantity);
        assertEquals(4L, orderExecuted.matchNumber);
    }
}
