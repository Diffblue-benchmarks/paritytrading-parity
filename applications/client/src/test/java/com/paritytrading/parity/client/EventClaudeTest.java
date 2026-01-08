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

class EventClaudeTest {

    @Test
    void testOrderAcceptedConstructorWithValidMessage() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertNotNull(event);
        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(POE.BUY, event.side);
        assertEquals(1L, event.instrument);
        assertEquals(100L, event.quantity);
        assertEquals(5000L, event.price);
        assertEquals(42L, event.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorWithBuySide() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 2000L;
        poeMessage.orderId = "BUYORDER".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 2L;
        poeMessage.quantity = 200L;
        poeMessage.price = 10000L;
        poeMessage.orderNumber = 100L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertEquals(POE.BUY, event.side);
        assertEquals("BUYORDER", event.orderId);
    }

    @Test
    void testOrderAcceptedConstructorWithSellSide() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 3000L;
        poeMessage.orderId = "SELLORDER".getBytes();
        poeMessage.side = POE.SELL;
        poeMessage.instrument = 3L;
        poeMessage.quantity = 300L;
        poeMessage.price = 15000L;
        poeMessage.orderNumber = 200L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertEquals(POE.SELL, event.side);
        assertEquals("SELLORDER", event.orderId);
    }

    @Test
    void testOrderAcceptedConstructorWithZeroValues() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.side = 0;
        poeMessage.instrument = 0L;
        poeMessage.quantity = 0L;
        poeMessage.price = 0L;
        poeMessage.orderNumber = 0L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertNotNull(event);
        assertEquals(0L, event.timestamp);
        assertEquals(0, event.side);
        assertEquals(0L, event.instrument);
        assertEquals(0L, event.quantity);
        assertEquals(0L, event.price);
        assertEquals(0L, event.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorWithMaxValues() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = Long.MAX_VALUE;
        poeMessage.orderId = "MAXORDER".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = Long.MAX_VALUE;
        poeMessage.quantity = Long.MAX_VALUE;
        poeMessage.price = Long.MAX_VALUE;
        poeMessage.orderNumber = Long.MAX_VALUE;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertEquals(Long.MAX_VALUE, event.timestamp);
        assertEquals(Long.MAX_VALUE, event.instrument);
        assertEquals(Long.MAX_VALUE, event.quantity);
        assertEquals(Long.MAX_VALUE, event.price);
        assertEquals(Long.MAX_VALUE, event.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorWithMinValues() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = Long.MIN_VALUE;
        poeMessage.orderId = "MINORDER".getBytes();
        poeMessage.side = POE.SELL;
        poeMessage.instrument = Long.MIN_VALUE;
        poeMessage.quantity = Long.MIN_VALUE;
        poeMessage.price = Long.MIN_VALUE;
        poeMessage.orderNumber = Long.MIN_VALUE;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertEquals(Long.MIN_VALUE, event.timestamp);
        assertEquals(Long.MIN_VALUE, event.instrument);
        assertEquals(Long.MIN_VALUE, event.quantity);
        assertEquals(Long.MIN_VALUE, event.price);
        assertEquals(Long.MIN_VALUE, event.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorWithEmptyOrderId() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertNotNull(event);
        assertNotNull(event.orderId);
    }

    @Test
    void testOrderAcceptedConstructorWithFullLengthOrderId() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        byte[] fullOrderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < POE.ORDER_ID_LENGTH; i++) {
            fullOrderId[i] = (byte) ('A' + (i % 26));
        }
        poeMessage.orderId = fullOrderId;
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertNotNull(event);
        assertNotNull(event.orderId);
    }

    @Test
    void testOrderAcceptedAcceptCallsVisitorVisit() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        event.accept(visitor);

        assertTrue(visitor.orderAcceptedVisited);
        assertSame(event, visitor.visitedOrderAccepted);
        assertFalse(visitor.orderRejectedVisited);
        assertFalse(visitor.orderExecutedVisited);
        assertFalse(visitor.orderCanceledVisited);
    }

    @Test
    void testOrderAcceptedAcceptWithDifferentVisitors() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        TestEventVisitor visitor1 = new TestEventVisitor();
        TestEventVisitor visitor2 = new TestEventVisitor();

        event.accept(visitor1);
        event.accept(visitor2);

        assertTrue(visitor1.orderAcceptedVisited);
        assertTrue(visitor2.orderAcceptedVisited);
        assertSame(event, visitor1.visitedOrderAccepted);
        assertSame(event, visitor2.visitedOrderAccepted);
    }

    @Test
    void testOrderAcceptedAcceptMultipleTimes() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        event.accept(visitor);
        event.accept(visitor);
        event.accept(visitor);

        assertTrue(visitor.orderAcceptedVisited);
        assertSame(event, visitor.visitedOrderAccepted);
        assertEquals(3, visitor.orderAcceptedVisitCount);
    }

    @Test
    void testOrderAcceptedImplementsEventInterface() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertTrue(event instanceof Event);
    }

    @Test
    void testOrderAcceptedFieldsArePublicAndFinal() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(POE.BUY, event.side);
        assertEquals(1L, event.instrument);
        assertEquals(100L, event.quantity);
        assertEquals(5000L, event.price);
        assertEquals(42L, event.orderNumber);
    }

    @Test
    void testOrderAcceptedConstructorPreservesAllFields() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 123456789L;
        poeMessage.orderId = "TESTORDER001".getBytes();
        poeMessage.side = POE.SELL;
        poeMessage.instrument = 999L;
        poeMessage.quantity = 555L;
        poeMessage.price = 777L;
        poeMessage.orderNumber = 888L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertEquals(123456789L, event.timestamp);
        assertEquals("TESTORDER001", event.orderId);
        assertEquals(POE.SELL, event.side);
        assertEquals(999L, event.instrument);
        assertEquals(555L, event.quantity);
        assertEquals(777L, event.price);
        assertEquals(888L, event.orderNumber);
    }

    @Test
    void testMultipleOrderAcceptedInstancesAreIndependent() {
        POE.OrderAccepted poeMessage1 = new POE.OrderAccepted();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.side = POE.BUY;
        poeMessage1.instrument = 1L;
        poeMessage1.quantity = 100L;
        poeMessage1.price = 5000L;
        poeMessage1.orderNumber = 1L;

        POE.OrderAccepted poeMessage2 = new POE.OrderAccepted();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.side = POE.SELL;
        poeMessage2.instrument = 2L;
        poeMessage2.quantity = 200L;
        poeMessage2.price = 6000L;
        poeMessage2.orderNumber = 2L;

        Event.OrderAccepted event1 = new Event.OrderAccepted(poeMessage1);
        Event.OrderAccepted event2 = new Event.OrderAccepted(poeMessage2);

        assertEquals(1000L, event1.timestamp);
        assertEquals(2000L, event2.timestamp);
        assertEquals("ORDER1", event1.orderId);
        assertEquals("ORDER2", event2.orderId);
        assertNotSame(event1, event2);
    }

    @Test
    void testOrderRejectedConstructorWithValidMessage() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertNotNull(event);
        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithUnknownInstrumentReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 2000L;
        poeMessage.orderId = "REJECT1".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, event.reason);
        assertEquals("REJECT1", event.orderId);
    }

    @Test
    void testOrderRejectedConstructorWithInvalidPriceReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 3000L;
        poeMessage.orderId = "REJECT2".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, event.reason);
        assertEquals("REJECT2", event.orderId);
    }

    @Test
    void testOrderRejectedConstructorWithInvalidQuantityReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 4000L;
        poeMessage.orderId = "REJECT3".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, event.reason);
        assertEquals("REJECT3", event.orderId);
    }

    @Test
    void testOrderRejectedConstructorWithZeroValues() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = 0;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertNotNull(event);
        assertEquals(0L, event.timestamp);
        assertEquals((byte) 0, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithMaxValues() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = Long.MAX_VALUE;
        poeMessage.orderId = "MAXORDER".getBytes();
        poeMessage.reason = Byte.MAX_VALUE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(Long.MAX_VALUE, event.timestamp);
        assertEquals(Byte.MAX_VALUE, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithMinValues() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = Long.MIN_VALUE;
        poeMessage.orderId = "MINORDER".getBytes();
        poeMessage.reason = Byte.MIN_VALUE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(Long.MIN_VALUE, event.timestamp);
        assertEquals(Byte.MIN_VALUE, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithEmptyOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertNotNull(event);
        assertNotNull(event.orderId);
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

        assertNotNull(event);
        assertNotNull(event.orderId);
    }

    @Test
    void testOrderRejectedConstructorWithDifferentReasons() {
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

        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, event1.reason);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, event2.reason);
    }

    @Test
    void testOrderRejectedConstructorPreservesAllFields() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 123456789L;
        poeMessage.orderId = "TESTORDER001".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(123456789L, event.timestamp);
        assertEquals("TESTORDER001", event.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, event.reason);
    }

    @Test
    void testMultipleOrderRejectedInstancesAreIndependent() {
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
        assertEquals("ORDER1", event1.orderId);
        assertEquals("ORDER2", event2.orderId);
        assertNotSame(event1, event2);
    }

    @Test
    void testOrderRejectedConstructorWithNegativeTimestamp() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = -1000L;
        poeMessage.orderId = "ORDER".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(-1000L, event.timestamp);
    }

    @Test
    void testOrderRejectedConstructorWithNegativeReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER".getBytes();
        poeMessage.reason = -1;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals((byte) -1, event.reason);
    }

    @Test
    void testOrderRejectedConstructorWithAlphanumericOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ABC123XYZ".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals("ABC123XYZ", event.orderId);
    }

    @Test
    void testOrderRejectedConstructorWithSpecialCharactersInOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORD-123_A".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals("ORD-123_A", event.orderId);
    }

    @Test
    void testOrderRejectedAcceptCallsVisitorVisit() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        event.accept(visitor);

        assertTrue(visitor.orderRejectedVisited);
        assertFalse(visitor.orderAcceptedVisited);
        assertFalse(visitor.orderExecutedVisited);
        assertFalse(visitor.orderCanceledVisited);
    }

    @Test
    void testOrderRejectedAcceptWithDifferentVisitors() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        TestEventVisitor visitor1 = new TestEventVisitor();
        TestEventVisitor visitor2 = new TestEventVisitor();

        event.accept(visitor1);
        event.accept(visitor2);

        assertTrue(visitor1.orderRejectedVisited);
        assertTrue(visitor2.orderRejectedVisited);
    }

    @Test
    void testOrderRejectedAcceptMultipleTimes() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        event.accept(visitor);
        event.accept(visitor);
        event.accept(visitor);

        assertTrue(visitor.orderRejectedVisited);
    }

    @Test
    void testOrderRejectedImplementsEventInterface() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertTrue(event instanceof Event);
    }

    @Test
    void testOrderRejectedFieldsArePublicAndFinal() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, event.reason);
    }

    @Test
    void testOrderRejectedConstructorConvertsOrderIdFromBytes() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertEquals("ORDER123", event.orderId);
        assertTrue(event.orderId instanceof String);
    }

    @Test
    void testOrderCanceledConstructorWithValidMessage() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertNotNull(event);
        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(100L, event.canceledQuantity);
        assertEquals((byte) 1, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithZeroValues() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.canceledQuantity = 0L;
        poeMessage.reason = 0;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertNotNull(event);
        assertEquals(0L, event.timestamp);
        assertEquals(0L, event.canceledQuantity);
        assertEquals((byte) 0, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithMaxValues() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = Long.MAX_VALUE;
        poeMessage.orderId = "MAXORDER".getBytes();
        poeMessage.canceledQuantity = Long.MAX_VALUE;
        poeMessage.reason = Byte.MAX_VALUE;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(Long.MAX_VALUE, event.timestamp);
        assertEquals(Long.MAX_VALUE, event.canceledQuantity);
        assertEquals(Byte.MAX_VALUE, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithMinValues() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = Long.MIN_VALUE;
        poeMessage.orderId = "MINORDER".getBytes();
        poeMessage.canceledQuantity = Long.MIN_VALUE;
        poeMessage.reason = Byte.MIN_VALUE;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(Long.MIN_VALUE, event.timestamp);
        assertEquals(Long.MIN_VALUE, event.canceledQuantity);
        assertEquals(Byte.MIN_VALUE, event.reason);
    }

    @Test
    void testOrderCanceledConstructorWithEmptyOrderId() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertNotNull(event);
        assertNotNull(event.orderId);
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

        assertNotNull(event);
        assertNotNull(event.orderId);
    }

    @Test
    void testOrderCanceledConstructorWithDifferentReasons() {
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

        assertEquals((byte) 1, event1.reason);
        assertEquals((byte) 2, event2.reason);
    }

    @Test
    void testOrderCanceledConstructorPreservesAllFields() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 123456789L;
        poeMessage.orderId = "TESTORDER001".getBytes();
        poeMessage.canceledQuantity = 555L;
        poeMessage.reason = 3;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertEquals(123456789L, event.timestamp);
        assertEquals("TESTORDER001", event.orderId);
        assertEquals(555L, event.canceledQuantity);
        assertEquals((byte) 3, event.reason);
    }

    @Test
    void testMultipleOrderCanceledInstancesAreIndependent() {
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
        assertEquals("ORDER1", event1.orderId);
        assertEquals("ORDER2", event2.orderId);
        assertNotSame(event1, event2);
    }

    @Test
    void testOrderCanceledAcceptCallsVisitorVisit() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        event.accept(visitor);

        assertTrue(visitor.orderCanceledVisited);
        assertSame(event, visitor.visitedOrderCanceled);
        assertFalse(visitor.orderAcceptedVisited);
        assertFalse(visitor.orderRejectedVisited);
        assertFalse(visitor.orderExecutedVisited);
    }

    @Test
    void testOrderCanceledAcceptWithDifferentVisitors() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        TestEventVisitor visitor1 = new TestEventVisitor();
        TestEventVisitor visitor2 = new TestEventVisitor();

        event.accept(visitor1);
        event.accept(visitor2);

        assertTrue(visitor1.orderCanceledVisited);
        assertTrue(visitor2.orderCanceledVisited);
        assertSame(event, visitor1.visitedOrderCanceled);
        assertSame(event, visitor2.visitedOrderCanceled);
    }

    @Test
    void testOrderCanceledAcceptMultipleTimes() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        event.accept(visitor);
        event.accept(visitor);
        event.accept(visitor);

        assertTrue(visitor.orderCanceledVisited);
        assertSame(event, visitor.visitedOrderCanceled);
        assertEquals(3, visitor.orderCanceledVisitCount);
    }

    @Test
    void testOrderCanceledImplementsEventInterface() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 100L;
        poeMessage.reason = 1;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertTrue(event instanceof Event);
    }

    @Test
    void testOrderCanceledFieldsArePublicAndFinal() {
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
    void testOrderExecutedConstructorWithValidMessage() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertNotNull(event);
        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(100L, event.quantity);
        assertEquals(5000L, event.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, event.liquidityFlag);
        assertEquals(42L, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithAddedLiquidity() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 2000L;
        poeMessage.orderId = "ADDORDER".getBytes();
        poeMessage.quantity = 200L;
        poeMessage.price = 10000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 100L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, event.liquidityFlag);
        assertEquals("ADDORDER", event.orderId);
    }

    @Test
    void testOrderExecutedConstructorWithRemovedLiquidity() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 3000L;
        poeMessage.orderId = "REMORDER".getBytes();
        poeMessage.quantity = 300L;
        poeMessage.price = 15000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeMessage.matchNumber = 200L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, event.liquidityFlag);
        assertEquals("REMORDER", event.orderId);
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

        assertNotNull(event);
        assertEquals(0L, event.timestamp);
        assertEquals(0L, event.quantity);
        assertEquals(0L, event.price);
        assertEquals((byte) 0, event.liquidityFlag);
        assertEquals(0L, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithMaxValues() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = Long.MAX_VALUE;
        poeMessage.orderId = "MAXORDER".getBytes();
        poeMessage.quantity = Long.MAX_VALUE;
        poeMessage.price = Long.MAX_VALUE;
        poeMessage.liquidityFlag = Byte.MAX_VALUE;
        poeMessage.matchNumber = Long.MAX_VALUE;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(Long.MAX_VALUE, event.timestamp);
        assertEquals(Long.MAX_VALUE, event.quantity);
        assertEquals(Long.MAX_VALUE, event.price);
        assertEquals(Byte.MAX_VALUE, event.liquidityFlag);
        assertEquals(Long.MAX_VALUE, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithMinValues() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = Long.MIN_VALUE;
        poeMessage.orderId = "MINORDER".getBytes();
        poeMessage.quantity = Long.MIN_VALUE;
        poeMessage.price = Long.MIN_VALUE;
        poeMessage.liquidityFlag = Byte.MIN_VALUE;
        poeMessage.matchNumber = Long.MIN_VALUE;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(Long.MIN_VALUE, event.timestamp);
        assertEquals(Long.MIN_VALUE, event.quantity);
        assertEquals(Long.MIN_VALUE, event.price);
        assertEquals(Byte.MIN_VALUE, event.liquidityFlag);
        assertEquals(Long.MIN_VALUE, event.matchNumber);
    }

    @Test
    void testOrderExecutedConstructorWithEmptyOrderId() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertNotNull(event);
        assertNotNull(event.orderId);
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
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertNotNull(event);
        assertNotNull(event.orderId);
    }

    @Test
    void testOrderExecutedConstructorPreservesAllFields() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 123456789L;
        poeMessage.orderId = "TESTORDER001".getBytes();
        poeMessage.quantity = 555L;
        poeMessage.price = 777L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeMessage.matchNumber = 888L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(123456789L, event.timestamp);
        assertEquals("TESTORDER001", event.orderId);
        assertEquals(555L, event.quantity);
        assertEquals(777L, event.price);
        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, event.liquidityFlag);
        assertEquals(888L, event.matchNumber);
    }

    @Test
    void testMultipleOrderExecutedInstancesAreIndependent() {
        POE.OrderExecuted poeMessage1 = new POE.OrderExecuted();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.quantity = 100L;
        poeMessage1.price = 5000L;
        poeMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage1.matchNumber = 1L;

        POE.OrderExecuted poeMessage2 = new POE.OrderExecuted();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.quantity = 200L;
        poeMessage2.price = 6000L;
        poeMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeMessage2.matchNumber = 2L;

        Event.OrderExecuted event1 = new Event.OrderExecuted(poeMessage1);
        Event.OrderExecuted event2 = new Event.OrderExecuted(poeMessage2);

        assertEquals(1000L, event1.timestamp);
        assertEquals(2000L, event2.timestamp);
        assertEquals("ORDER1", event1.orderId);
        assertEquals("ORDER2", event2.orderId);
        assertNotSame(event1, event2);
    }

    @Test
    void testOrderExecutedAcceptCallsVisitorVisit() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        event.accept(visitor);

        assertTrue(visitor.orderExecutedVisited);
        assertSame(event, visitor.visitedOrderExecuted);
        assertFalse(visitor.orderAcceptedVisited);
        assertFalse(visitor.orderRejectedVisited);
        assertFalse(visitor.orderCanceledVisited);
    }

    @Test
    void testOrderExecutedAcceptWithDifferentVisitors() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        TestEventVisitor visitor1 = new TestEventVisitor();
        TestEventVisitor visitor2 = new TestEventVisitor();

        event.accept(visitor1);
        event.accept(visitor2);

        assertTrue(visitor1.orderExecutedVisited);
        assertTrue(visitor2.orderExecutedVisited);
        assertSame(event, visitor1.visitedOrderExecuted);
        assertSame(event, visitor2.visitedOrderExecuted);
    }

    @Test
    void testOrderExecutedAcceptMultipleTimes() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        event.accept(visitor);
        event.accept(visitor);
        event.accept(visitor);

        assertTrue(visitor.orderExecutedVisited);
        assertSame(event, visitor.visitedOrderExecuted);
        assertEquals(3, visitor.orderExecutedVisitCount);
    }

    @Test
    void testOrderExecutedImplementsEventInterface() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertTrue(event instanceof Event);
    }

    @Test
    void testOrderExecutedFieldsArePublicAndFinal() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertEquals(1000L, event.timestamp);
        assertEquals("ORDER123", event.orderId);
        assertEquals(100L, event.quantity);
        assertEquals(5000L, event.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, event.liquidityFlag);
        assertEquals(42L, event.matchNumber);
    }

    private static class TestEventVisitor implements EventVisitor {
        boolean orderAcceptedVisited = false;
        boolean orderRejectedVisited = false;
        boolean orderExecutedVisited = false;
        boolean orderCanceledVisited = false;
        int orderAcceptedVisitCount = 0;
        int orderExecutedVisitCount = 0;
        int orderCanceledVisitCount = 0;
        Event.OrderAccepted visitedOrderAccepted = null;
        Event.OrderExecuted visitedOrderExecuted = null;
        Event.OrderCanceled visitedOrderCanceled = null;

        @Override
        public void visit(Event.OrderAccepted event) {
            orderAcceptedVisited = true;
            visitedOrderAccepted = event;
            orderAcceptedVisitCount++;
        }

        @Override
        public void visit(Event.OrderRejected event) {
            orderRejectedVisited = true;
        }

        @Override
        public void visit(Event.OrderExecuted event) {
            orderExecutedVisited = true;
            visitedOrderExecuted = event;
            orderExecutedVisitCount++;
        }

        @Override
        public void visit(Event.OrderCanceled event) {
            orderCanceledVisited = true;
            visitedOrderCanceled = event;
            orderCanceledVisitCount++;
        }
    }
}
