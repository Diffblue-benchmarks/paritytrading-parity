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
package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

class EventClaude_constructorTest {

    @Test
    void testOrderCanceledConstructorInitializesAllFields() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 123456789L;
        poeMessage.orderId = "TESTORDER".getBytes();
        poeMessage.canceledQuantity = 500L;
        poeMessage.reason = 5;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(123456789L, event.timestamp);
        assertEquals("TESTORDER", event.orderId);
        assertEquals(500L, event.canceledQuantity);
        assertEquals((byte) 5, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithMinimumValues() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = Long.MIN_VALUE;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.canceledQuantity = Long.MIN_VALUE;
        poeMessage.reason = Byte.MIN_VALUE;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(Long.MIN_VALUE, event.timestamp);
        assertNotNull(event.orderId);
        assertEquals(Long.MIN_VALUE, event.canceledQuantity);
        assertEquals(Byte.MIN_VALUE, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithMaximumValues() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = Long.MAX_VALUE;
        poeMessage.orderId = "MAX".getBytes();
        poeMessage.canceledQuantity = Long.MAX_VALUE;
        poeMessage.reason = Byte.MAX_VALUE;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(Long.MAX_VALUE, event.timestamp);
        assertEquals("MAX", event.orderId);
        assertEquals(Long.MAX_VALUE, event.canceledQuantity);
        assertEquals(Byte.MAX_VALUE, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithZeroValues() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.canceledQuantity = 0L;
        poeMessage.reason = 0;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(0L, event.timestamp);
        assertNotNull(event.orderId);
        assertEquals(0L, event.canceledQuantity);
        assertEquals((byte) 0, event.reason);
    }

    @Test
    void testOrderCanceledConstructorConvertsOrderIdFromBytes() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals("ORDER123", event.orderId);
        assertTrue(event.orderId instanceof String);
    }

    @Test
    void testOrderCanceledConstructorWithDifferentTimestamps() {
        POE.OrderCanceled poeMessage1 = new POE.OrderCanceled();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.canceledQuantity = 100L;
        poeMessage1.reason = 1;

        POE.OrderCanceled poeMessage2 = new POE.OrderCanceled();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.canceledQuantity = 200L;
        poeMessage2.reason = 2;

        Event.OrderCanceled event1 = new Event.OrderCanceled(poeMessage1);
        Event.OrderCanceled event2 = new Event.OrderCanceled(poeMessage2);

        assertEquals(1000L, event1.timestamp);
        assertEquals(2000L, event2.timestamp);
    }

    @Test
    void testOrderCanceledConstructorWithDifferentCanceledQuantities() {
        POE.OrderCanceled poeMessage1 = new POE.OrderCanceled();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.canceledQuantity = 50L;
        poeMessage1.reason = 1;

        POE.OrderCanceled poeMessage2 = new POE.OrderCanceled();
        poeMessage2.timestamp = 1000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.canceledQuantity = 150L;
        poeMessage2.reason = 1;

        Event.OrderCanceled event1 = new Event.OrderCanceled(poeMessage1);
        Event.OrderCanceled event2 = new Event.OrderCanceled(poeMessage2);

        assertEquals(50L, event1.canceledQuantity);
        assertEquals(150L, event2.canceledQuantity);
    }

    @Test
    void testOrderCanceledConstructorWithDifferentReasonCodes() {
        POE.OrderCanceled poeMessage1 = new POE.OrderCanceled();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.canceledQuantity = 100L;
        poeMessage1.reason = 1;

        POE.OrderCanceled poeMessage2 = new POE.OrderCanceled();
        poeMessage2.timestamp = 1000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.canceledQuantity = 100L;
        poeMessage2.reason = 3;

        Event.OrderCanceled event1 = new Event.OrderCanceled(poeMessage1);
        Event.OrderCanceled event2 = new Event.OrderCanceled(poeMessage2);

        assertEquals((byte) 1, event1.reason);
        assertEquals((byte) 3, event2.reason);
    }

    @Test
    void testOrderCanceledConstructorWithFullLengthOrderId() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        byte[] fullOrderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < POE.ORDER_ID_LENGTH; i++) {
            fullOrderId[i] = (byte) ('A' + (i % 26));
        }
        poeMessage.orderId = fullOrderId;
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertNotNull(event.orderId);
        assertTrue(event.orderId.length() <= POE.ORDER_ID_LENGTH);
    }

    @Test
    void testOrderCanceledConstructorWithNegativeValues() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = -1000L;
        poeMessage.orderId = "ORDER".getBytes();
        poeMessage.canceledQuantity = -100L;
        poeMessage.reason = -1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(-1000L, event.timestamp);
        assertEquals(-100L, event.canceledQuantity);
        assertEquals((byte) -1, event.reason);
    }

    @Test
    void testOrderCanceledConstructorCreatesNewInstance() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertNotNull(event);
        assertTrue(event instanceof Event.OrderCanceled);
    }

    @Test
    void testOrderCanceledConstructorFieldsAreFinal() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(100L, event.canceledQuantity);
        assertEquals((byte) 1, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithAlphanumericOrderId() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ABC123XYZ".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals("ABC123XYZ", event.orderId);
    }

    @Test
    void testOrderCanceledConstructorPreservesInputValues() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        long expectedTimestamp = 987654321L;
        String expectedOrderId = "PRESERVE";
        long expectedQuantity = 777L;
        byte expectedReason = 9;

        poeMessage.timestamp = expectedTimestamp;
        poeMessage.orderId = expectedOrderId.getBytes();
        poeMessage.canceledQuantity = expectedQuantity;
        poeMessage.reason = expectedReason;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(expectedTimestamp, event.timestamp);
        assertEquals(expectedOrderId, event.orderId);
        assertEquals(expectedQuantity, event.canceledQuantity);
        assertEquals(expectedReason, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithSpecialCharactersInOrderId() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORD-123_A".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals("ORD-123_A", event.orderId);
    }

    @Test
    void testOrderCanceledConstructorMultipleInstances() {
        POE.OrderCanceled poeMessage1 = new POE.OrderCanceled();
        poeMessage1.timestamp = 1111L;
        poeMessage1.orderId = "FIRST".getBytes();
        poeMessage1.canceledQuantity = 111L;
        poeMessage1.reason = 1;

        POE.OrderCanceled poeMessage2 = new POE.OrderCanceled();
        poeMessage2.timestamp = 2222L;
        poeMessage2.orderId = "SECOND".getBytes();
        poeMessage2.canceledQuantity = 222L;
        poeMessage2.reason = 2;

        POE.OrderCanceled poeMessage3 = new POE.OrderCanceled();
        poeMessage3.timestamp = 3333L;
        poeMessage3.orderId = "THIRD".getBytes();
        poeMessage3.canceledQuantity = 333L;
        poeMessage3.reason = 3;

        Event.OrderCanceled event1 = new Event.OrderCanceled(poeMessage1);
        Event.OrderCanceled event2 = new Event.OrderCanceled(poeMessage2);
        Event.OrderCanceled event3 = new Event.OrderCanceled(poeMessage3);

        assertEquals(1111L, event1.timestamp);
        assertEquals(2222L, event2.timestamp);
        assertEquals(3333L, event3.timestamp);

        assertNotSame(event1, event2);
        assertNotSame(event2, event3);
        assertNotSame(event1, event3);
    }

    @Test
    void testOrderExecutedConstructorInitializesAllFields() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 123456789L;
        poeMessage.orderId = "TESTORDER".getBytes();
        poeMessage.quantity = 500L;
        poeMessage.price = 1000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 999L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(123456789L, event.timestamp);
        assertEquals("TESTORDER", event.orderId);
        assertEquals(500L, event.quantity);
        assertEquals(1000L, event.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, event.liquidityFlag);
        assertEquals(999L, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithMinimumValues() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = Long.MIN_VALUE;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.quantity = Long.MIN_VALUE;
        poeMessage.price = Long.MIN_VALUE;
        poeMessage.liquidityFlag = Byte.MIN_VALUE;
        poeMessage.matchNumber = Long.MIN_VALUE;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(Long.MIN_VALUE, event.timestamp);
        assertNotNull(event.orderId);
        assertEquals(Long.MIN_VALUE, event.quantity);
        assertEquals(Long.MIN_VALUE, event.price);
        assertEquals(Byte.MIN_VALUE, event.liquidityFlag);
        assertEquals(Long.MIN_VALUE, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithMaximumValues() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = Long.MAX_VALUE;
        poeMessage.orderId = "MAX".getBytes();
        poeMessage.quantity = Long.MAX_VALUE;
        poeMessage.price = Long.MAX_VALUE;
        poeMessage.liquidityFlag = Byte.MAX_VALUE;
        poeMessage.matchNumber = Long.MAX_VALUE;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(Long.MAX_VALUE, event.timestamp);
        assertEquals("MAX", event.orderId);
        assertEquals(Long.MAX_VALUE, event.quantity);
        assertEquals(Long.MAX_VALUE, event.price);
        assertEquals(Byte.MAX_VALUE, event.liquidityFlag);
        assertEquals(Long.MAX_VALUE, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithZeroValues() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.quantity = 0L;
        poeMessage.price = 0L;
        poeMessage.liquidityFlag = 0;
        poeMessage.matchNumber = 0L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(0L, event.timestamp);
        assertNotNull(event.orderId);
        assertEquals(0L, event.quantity);
        assertEquals(0L, event.price);
        assertEquals((byte) 0, event.liquidityFlag);
        assertEquals(0L, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorConvertsOrderIdFromBytes() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 500L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals("ORDER123", event.orderId);
        assertTrue(event.orderId instanceof String);
    }

    @Test
    void testOrderExecutedConstructorWithDifferentTimestamps() {
        POE.OrderExecuted poeMessage1 = new POE.OrderExecuted();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.quantity = 100L;
        poeMessage1.price = 500L;
        poeMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage1.matchNumber = 1L;

        POE.OrderExecuted poeMessage2 = new POE.OrderExecuted();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.quantity = 200L;
        poeMessage2.price = 600L;
        poeMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeMessage2.matchNumber = 2L;

        Event.OrderExecuted event1 = new Event.OrderExecuted(poeMessage1);
        Event.OrderExecuted event2 = new Event.OrderExecuted(poeMessage2);

        assertEquals(1000L, event1.timestamp);
        assertEquals(2000L, event2.timestamp);
    }

    @Test
    void testOrderExecutedConstructorWithDifferentQuantities() {
        POE.OrderExecuted poeMessage1 = new POE.OrderExecuted();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.quantity = 50L;
        poeMessage1.price = 500L;
        poeMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage1.matchNumber = 1L;

        POE.OrderExecuted poeMessage2 = new POE.OrderExecuted();
        poeMessage2.timestamp = 1000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.quantity = 150L;
        poeMessage2.price = 500L;
        poeMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage2.matchNumber = 1L;

        Event.OrderExecuted event1 = new Event.OrderExecuted(poeMessage1);
        Event.OrderExecuted event2 = new Event.OrderExecuted(poeMessage2);

        assertEquals(50L, event1.quantity);
        assertEquals(150L, event2.quantity);
    }

    @Test
    void testOrderExecutedConstructorWithDifferentPrices() {
        POE.OrderExecuted poeMessage1 = new POE.OrderExecuted();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.quantity = 100L;
        poeMessage1.price = 300L;
        poeMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage1.matchNumber = 1L;

        POE.OrderExecuted poeMessage2 = new POE.OrderExecuted();
        poeMessage2.timestamp = 1000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.quantity = 100L;
        poeMessage2.price = 700L;
        poeMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage2.matchNumber = 1L;

        Event.OrderExecuted event1 = new Event.OrderExecuted(poeMessage1);
        Event.OrderExecuted event2 = new Event.OrderExecuted(poeMessage2);

        assertEquals(300L, event1.price);
        assertEquals(700L, event2.price);
    }

    @Test
    void testOrderExecutedConstructorWithDifferentLiquidityFlags() {
        POE.OrderExecuted poeMessage1 = new POE.OrderExecuted();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.quantity = 100L;
        poeMessage1.price = 500L;
        poeMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage1.matchNumber = 1L;

        POE.OrderExecuted poeMessage2 = new POE.OrderExecuted();
        poeMessage2.timestamp = 1000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.quantity = 100L;
        poeMessage2.price = 500L;
        poeMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeMessage2.matchNumber = 1L;

        Event.OrderExecuted event1 = new Event.OrderExecuted(poeMessage1);
        Event.OrderExecuted event2 = new Event.OrderExecuted(poeMessage2);

        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, event1.liquidityFlag);
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, event2.liquidityFlag);
    }

    @Test
    void testOrderExecutedConstructorWithDifferentMatchNumbers() {
        POE.OrderExecuted poeMessage1 = new POE.OrderExecuted();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.quantity = 100L;
        poeMessage1.price = 500L;
        poeMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage1.matchNumber = 111L;

        POE.OrderExecuted poeMessage2 = new POE.OrderExecuted();
        poeMessage2.timestamp = 1000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.quantity = 100L;
        poeMessage2.price = 500L;
        poeMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage2.matchNumber = 222L;

        Event.OrderExecuted event1 = new Event.OrderExecuted(poeMessage1);
        Event.OrderExecuted event2 = new Event.OrderExecuted(poeMessage2);

        assertEquals(111L, event1.matchNumber);
        assertEquals(222L, event2.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithFullLengthOrderId() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        byte[] fullOrderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < POE.ORDER_ID_LENGTH; i++) {
            fullOrderId[i] = (byte) ('A' + (i % 26));
        }
        poeMessage.orderId = fullOrderId;
        poeMessage.quantity = 100L;
        poeMessage.price = 500L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertNotNull(event.orderId);
        assertTrue(event.orderId.length() <= POE.ORDER_ID_LENGTH);
    }

    @Test
    void testOrderExecutedConstructorWithNegativeValues() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = -1000L;
        poeMessage.orderId = "ORDER".getBytes();
        poeMessage.quantity = -100L;
        poeMessage.price = -500L;
        poeMessage.liquidityFlag = -1;
        poeMessage.matchNumber = -42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(-1000L, event.timestamp);
        assertEquals(-100L, event.quantity);
        assertEquals(-500L, event.price);
        assertEquals((byte) -1, event.liquidityFlag);
        assertEquals(-42L, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorCreatesNewInstance() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 500L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertNotNull(event);
        assertTrue(event instanceof Event.OrderExecuted);
    }

    @Test
    void testOrderExecutedConstructorFieldsAreFinal() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 500L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(100L, event.quantity);
        assertEquals(500L, event.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, event.liquidityFlag);
        assertEquals(42L, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithAlphanumericOrderId() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ABC123XYZ".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 500L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals("ABC123XYZ", event.orderId);
    }

    @Test
    void testOrderExecutedConstructorPreservesInputValues() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        long expectedTimestamp = 987654321L;
        String expectedOrderId = "PRESERVE";
        long expectedQuantity = 777L;
        long expectedPrice = 888L;
        byte expectedLiquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        long expectedMatchNumber = 999L;

        poeMessage.timestamp = expectedTimestamp;
        poeMessage.orderId = expectedOrderId.getBytes();
        poeMessage.quantity = expectedQuantity;
        poeMessage.price = expectedPrice;
        poeMessage.liquidityFlag = expectedLiquidityFlag;
        poeMessage.matchNumber = expectedMatchNumber;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(expectedTimestamp, event.timestamp);
        assertEquals(expectedOrderId, event.orderId);
        assertEquals(expectedQuantity, event.quantity);
        assertEquals(expectedPrice, event.price);
        assertEquals(expectedLiquidityFlag, event.liquidityFlag);
        assertEquals(expectedMatchNumber, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithSpecialCharactersInOrderId() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORD-123_A".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 500L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals("ORD-123_A", event.orderId);
    }

    @Test
    void testOrderExecutedConstructorMultipleInstances() {
        POE.OrderExecuted poeMessage1 = new POE.OrderExecuted();
        poeMessage1.timestamp = 1111L;
        poeMessage1.orderId = "FIRST".getBytes();
        poeMessage1.quantity = 111L;
        poeMessage1.price = 511L;
        poeMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage1.matchNumber = 11L;

        POE.OrderExecuted poeMessage2 = new POE.OrderExecuted();
        poeMessage2.timestamp = 2222L;
        poeMessage2.orderId = "SECOND".getBytes();
        poeMessage2.quantity = 222L;
        poeMessage2.price = 522L;
        poeMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeMessage2.matchNumber = 22L;

        POE.OrderExecuted poeMessage3 = new POE.OrderExecuted();
        poeMessage3.timestamp = 3333L;
        poeMessage3.orderId = "THIRD".getBytes();
        poeMessage3.quantity = 333L;
        poeMessage3.price = 533L;
        poeMessage3.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage3.matchNumber = 33L;

        Event.OrderExecuted event1 = new Event.OrderExecuted(poeMessage1);
        Event.OrderExecuted event2 = new Event.OrderExecuted(poeMessage2);
        Event.OrderExecuted event3 = new Event.OrderExecuted(poeMessage3);

        assertEquals(1111L, event1.timestamp);
        assertEquals(2222L, event2.timestamp);
        assertEquals(3333L, event3.timestamp);

        assertNotSame(event1, event2);
        assertNotSame(event2, event3);
        assertNotSame(event1, event3);
    }

    @Test
    void testOrderRejectedConstructorInitializesAllFields() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 123456789L;
        poeMessage.orderId = "TESTORDER".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(123456789L, event.timestamp);
        assertEquals("TESTORDER", event.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithMinimumValues() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = Long.MIN_VALUE;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = Byte.MIN_VALUE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(Long.MIN_VALUE, event.timestamp);
        assertNotNull(event.orderId);
        assertEquals(Byte.MIN_VALUE, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithMaximumValues() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = Long.MAX_VALUE;
        poeMessage.orderId = "MAX".getBytes();
        poeMessage.reason = Byte.MAX_VALUE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(Long.MAX_VALUE, event.timestamp);
        assertEquals("MAX", event.orderId);
        assertEquals(Byte.MAX_VALUE, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithZeroValues() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = 0;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(0L, event.timestamp);
        assertNotNull(event.orderId);
        assertEquals((byte) 0, event.reason);
    }

    @Test
    void testOrderRejectedConstructorConvertsOrderIdFromBytes() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals("ORDER123", event.orderId);
        assertTrue(event.orderId instanceof String);
    }

    @Test
    void testOrderRejectedConstructorWithDifferentTimestamps() {
        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event1 = new Event.OrderRejected(poeMessage1);
        Event.OrderRejected event2 = new Event.OrderRejected(poeMessage2);

        assertEquals(1000L, event1.timestamp);
        assertEquals(2000L, event2.timestamp);
    }

    @Test
    void testOrderRejectedConstructorWithDifferentReasonCodes() {
        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 1000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected poeMessage3 = new POE.OrderRejected();
        poeMessage3.timestamp = 1000L;
        poeMessage3.orderId = "ORDER3".getBytes();
        poeMessage3.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event1 = new Event.OrderRejected(poeMessage1);
        Event.OrderRejected event2 = new Event.OrderRejected(poeMessage2);
        Event.OrderRejected event3 = new Event.OrderRejected(poeMessage3);

        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, event1.reason);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, event2.reason);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, event3.reason);
    }

    @Test
    void testOrderRejectedConstructorWithFullLengthOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        byte[] fullOrderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < POE.ORDER_ID_LENGTH; i++) {
            fullOrderId[i] = (byte) ('A' + (i % 26));
        }
        poeMessage.orderId = fullOrderId;
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertNotNull(event.orderId);
        assertTrue(event.orderId.length() <= POE.ORDER_ID_LENGTH);
    }

    @Test
    void testOrderRejectedConstructorWithNegativeValues() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = -1000L;
        poeMessage.orderId = "ORDER".getBytes();
        poeMessage.reason = -1;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(-1000L, event.timestamp);
        assertEquals((byte) -1, event.reason);
    }

    @Test
    void testOrderRejectedConstructorCreatesNewInstance() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertNotNull(event);
        assertTrue(event instanceof Event.OrderRejected);
    }

    @Test
    void testOrderRejectedConstructorFieldsAreFinal() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithAlphanumericOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ABC123XYZ".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals("ABC123XYZ", event.orderId);
    }

    @Test
    void testOrderRejectedConstructorPreservesInputValues() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        long expectedTimestamp = 987654321L;
        String expectedOrderId = "PRESERVE";
        byte expectedReason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        poeMessage.timestamp = expectedTimestamp;
        poeMessage.orderId = expectedOrderId.getBytes();
        poeMessage.reason = expectedReason;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(expectedTimestamp, event.timestamp);
        assertEquals(expectedOrderId, event.orderId);
        assertEquals(expectedReason, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithSpecialCharactersInOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORD-123_A".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals("ORD-123_A", event.orderId);
    }

    @Test
    void testOrderRejectedConstructorMultipleInstances() {
        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1111L;
        poeMessage1.orderId = "FIRST".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2222L;
        poeMessage2.orderId = "SECOND".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected poeMessage3 = new POE.OrderRejected();
        poeMessage3.timestamp = 3333L;
        poeMessage3.orderId = "THIRD".getBytes();
        poeMessage3.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event1 = new Event.OrderRejected(poeMessage1);
        Event.OrderRejected event2 = new Event.OrderRejected(poeMessage2);
        Event.OrderRejected event3 = new Event.OrderRejected(poeMessage3);

        assertEquals(1111L, event1.timestamp);
        assertEquals(2222L, event2.timestamp);
        assertEquals(3333L, event3.timestamp);

        assertNotSame(event1, event2);
        assertNotSame(event2, event3);
        assertNotSame(event1, event3);
    }
}
