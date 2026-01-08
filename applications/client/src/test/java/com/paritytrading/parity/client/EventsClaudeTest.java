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
import org.junit.jupiter.api.BeforeEach;

class EventsClaudeTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void testConstructor() {
        Events newEvents = new Events();
        assertNotNull(newEvents);
    }

    @Test
    void testOrderAcceptedAddsEvent() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000L;
        message.orderId = "ORDER123".getBytes();
        message.side = POE.BUY;
        message.instrument = 1L;
        message.quantity = 100L;
        message.price = 5000L;
        message.orderNumber = 42L;

        events.orderAccepted(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderAcceptedVisited);
        assertEquals(1, visitor.orderAcceptedVisitCount);
        assertEquals(1000L, visitor.lastOrderAccepted.timestamp);
        assertEquals("ORDER123", visitor.lastOrderAccepted.orderId);
        assertEquals(POE.BUY, visitor.lastOrderAccepted.side);
        assertEquals(1L, visitor.lastOrderAccepted.instrument);
        assertEquals(100L, visitor.lastOrderAccepted.quantity);
        assertEquals(5000L, visitor.lastOrderAccepted.price);
        assertEquals(42L, visitor.lastOrderAccepted.orderNumber);
    }

    @Test
    void testOrderRejectedAddsEvent() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2000L;
        message.orderId = "ORDER456".getBytes();
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderRejectedVisited);
        assertEquals(1, visitor.orderRejectedVisitCount);
        assertEquals(2000L, visitor.lastOrderRejected.timestamp);
        assertEquals("ORDER456", visitor.lastOrderRejected.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, visitor.lastOrderRejected.reason);
    }

    @Test
    void testOrderExecutedAddsEvent() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 3000L;
        message.orderId = "ORDER789".getBytes();
        message.quantity = 200L;
        message.price = 10000L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber = 100L;

        events.orderExecuted(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderExecutedVisited);
        assertEquals(1, visitor.orderExecutedVisitCount);
        assertEquals(3000L, visitor.lastOrderExecuted.timestamp);
        assertEquals("ORDER789", visitor.lastOrderExecuted.orderId);
        assertEquals(200L, visitor.lastOrderExecuted.quantity);
        assertEquals(10000L, visitor.lastOrderExecuted.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, visitor.lastOrderExecuted.liquidityFlag);
        assertEquals(100L, visitor.lastOrderExecuted.matchNumber);
    }

    @Test
    void testOrderCanceledAddsEvent() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 4000L;
        message.orderId = "ORDER999".getBytes();
        message.canceledQuantity = 50L;
        message.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderCanceled(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderCanceledVisited);
        assertEquals(1, visitor.orderCanceledVisitCount);
        assertEquals(4000L, visitor.lastOrderCanceled.timestamp);
        assertEquals("ORDER999", visitor.lastOrderCanceled.orderId);
        assertEquals(50L, visitor.lastOrderCanceled.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, visitor.lastOrderCanceled.reason);
    }

    @Test
    void testAcceptWithEmptyEventsList() {
        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertFalse(visitor.orderAcceptedVisited);
        assertFalse(visitor.orderRejectedVisited);
        assertFalse(visitor.orderExecutedVisited);
        assertFalse(visitor.orderCanceledVisited);
    }

    @Test
    void testAcceptWithMultipleEvents() {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderRejected rejectedMessage = new POE.OrderRejected();
        rejectedMessage.timestamp = 2000L;
        rejectedMessage.orderId = "ORDER2".getBytes();
        rejectedMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 3000L;
        executedMessage.orderId = "ORDER3".getBytes();
        executedMessage.quantity = 150L;
        executedMessage.price = 7500L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 50L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 4000L;
        canceledMessage.orderId = "ORDER4".getBytes();
        canceledMessage.canceledQuantity = 75L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_SUPERVISORY;

        events.orderAccepted(acceptedMessage);
        events.orderRejected(rejectedMessage);
        events.orderExecuted(executedMessage);
        events.orderCanceled(canceledMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderAcceptedVisited);
        assertTrue(visitor.orderRejectedVisited);
        assertTrue(visitor.orderExecutedVisited);
        assertTrue(visitor.orderCanceledVisited);
        assertEquals(1, visitor.orderAcceptedVisitCount);
        assertEquals(1, visitor.orderRejectedVisitCount);
        assertEquals(1, visitor.orderExecutedVisitCount);
        assertEquals(1, visitor.orderCanceledVisitCount);
    }

    @Test
    void testAcceptCalledMultipleTimes() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000L;
        message.orderId = "ORDER123".getBytes();
        message.side = POE.BUY;
        message.instrument = 1L;
        message.quantity = 100L;
        message.price = 5000L;
        message.orderNumber = 42L;

        events.orderAccepted(message);

        TestEventVisitor visitor1 = new TestEventVisitor();
        events.accept(visitor1);
        assertTrue(visitor1.orderAcceptedVisited);
        assertEquals(1, visitor1.orderAcceptedVisitCount);

        TestEventVisitor visitor2 = new TestEventVisitor();
        events.accept(visitor2);
        assertTrue(visitor2.orderAcceptedVisited);
        assertEquals(1, visitor2.orderAcceptedVisitCount);
    }

    @Test
    void testEventsAreStoredInOrder() {
        POE.OrderAccepted message1 = new POE.OrderAccepted();
        message1.timestamp = 1000L;
        message1.orderId = "ORDER1".getBytes();
        message1.side = POE.BUY;
        message1.instrument = 1L;
        message1.quantity = 100L;
        message1.price = 5000L;
        message1.orderNumber = 1L;

        POE.OrderAccepted message2 = new POE.OrderAccepted();
        message2.timestamp = 2000L;
        message2.orderId = "ORDER2".getBytes();
        message2.side = POE.SELL;
        message2.instrument = 2L;
        message2.quantity = 200L;
        message2.price = 6000L;
        message2.orderNumber = 2L;

        POE.OrderAccepted message3 = new POE.OrderAccepted();
        message3.timestamp = 3000L;
        message3.orderId = "ORDER3".getBytes();
        message3.side = POE.BUY;
        message3.instrument = 3L;
        message3.quantity = 300L;
        message3.price = 7000L;
        message3.orderNumber = 3L;

        events.orderAccepted(message1);
        events.orderAccepted(message2);
        events.orderAccepted(message3);

        OrderTrackingVisitor visitor = new OrderTrackingVisitor();
        events.accept(visitor);

        assertEquals(3, visitor.orderAcceptedVisitCount);
        assertEquals("ORDER1", visitor.firstOrderId);
        assertEquals("ORDER3", visitor.lastOrderId);
    }

    @Test
    void testMultipleOrderAcceptedEvents() {
        POE.OrderAccepted message1 = new POE.OrderAccepted();
        message1.timestamp = 1000L;
        message1.orderId = "ORDER1".getBytes();
        message1.side = POE.BUY;
        message1.instrument = 1L;
        message1.quantity = 100L;
        message1.price = 5000L;
        message1.orderNumber = 1L;

        POE.OrderAccepted message2 = new POE.OrderAccepted();
        message2.timestamp = 2000L;
        message2.orderId = "ORDER2".getBytes();
        message2.side = POE.SELL;
        message2.instrument = 2L;
        message2.quantity = 200L;
        message2.price = 6000L;
        message2.orderNumber = 2L;

        events.orderAccepted(message1);
        events.orderAccepted(message2);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertEquals(2, visitor.orderAcceptedVisitCount);
    }

    @Test
    void testMultipleOrderRejectedEvents() {
        POE.OrderRejected message1 = new POE.OrderRejected();
        message1.timestamp = 1000L;
        message1.orderId = "ORDER1".getBytes();
        message1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected message2 = new POE.OrderRejected();
        message2.timestamp = 2000L;
        message2.orderId = "ORDER2".getBytes();
        message2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(message1);
        events.orderRejected(message2);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertEquals(2, visitor.orderRejectedVisitCount);
    }

    @Test
    void testMultipleOrderExecutedEvents() {
        POE.OrderExecuted message1 = new POE.OrderExecuted();
        message1.timestamp = 1000L;
        message1.orderId = "ORDER1".getBytes();
        message1.quantity = 100L;
        message1.price = 5000L;
        message1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message1.matchNumber = 1L;

        POE.OrderExecuted message2 = new POE.OrderExecuted();
        message2.timestamp = 2000L;
        message2.orderId = "ORDER2".getBytes();
        message2.quantity = 200L;
        message2.price = 6000L;
        message2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        message2.matchNumber = 2L;

        events.orderExecuted(message1);
        events.orderExecuted(message2);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertEquals(2, visitor.orderExecutedVisitCount);
    }

    @Test
    void testMultipleOrderCanceledEvents() {
        POE.OrderCanceled message1 = new POE.OrderCanceled();
        message1.timestamp = 1000L;
        message1.orderId = "ORDER1".getBytes();
        message1.canceledQuantity = 50L;
        message1.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        POE.OrderCanceled message2 = new POE.OrderCanceled();
        message2.timestamp = 2000L;
        message2.orderId = "ORDER2".getBytes();
        message2.canceledQuantity = 75L;
        message2.reason = POE.ORDER_CANCEL_REASON_SUPERVISORY;

        events.orderCanceled(message1);
        events.orderCanceled(message2);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertEquals(2, visitor.orderCanceledVisitCount);
    }

    @Test
    void testOrderAcceptedWithZeroValues() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 0L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.side = 0;
        message.instrument = 0L;
        message.quantity = 0L;
        message.price = 0L;
        message.orderNumber = 0L;

        events.orderAccepted(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderAcceptedVisited);
        assertEquals(0L, visitor.lastOrderAccepted.timestamp);
    }

    @Test
    void testOrderRejectedWithZeroValues() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 0L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.reason = 0;

        events.orderRejected(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderRejectedVisited);
        assertEquals(0L, visitor.lastOrderRejected.timestamp);
    }

    @Test
    void testOrderExecutedWithZeroValues() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 0L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.quantity = 0L;
        message.price = 0L;
        message.liquidityFlag = 0;
        message.matchNumber = 0L;

        events.orderExecuted(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderExecutedVisited);
        assertEquals(0L, visitor.lastOrderExecuted.timestamp);
    }

    @Test
    void testOrderCanceledWithZeroValues() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 0L;
        message.orderId = new byte[POE.ORDER_ID_LENGTH];
        message.canceledQuantity = 0L;
        message.reason = 0;

        events.orderCanceled(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderCanceledVisited);
        assertEquals(0L, visitor.lastOrderCanceled.timestamp);
    }

    @Test
    void testOrderAcceptedWithMaxValues() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = Long.MAX_VALUE;
        message.orderId = "MAXORDER".getBytes();
        message.side = Byte.MAX_VALUE;
        message.instrument = Long.MAX_VALUE;
        message.quantity = Long.MAX_VALUE;
        message.price = Long.MAX_VALUE;
        message.orderNumber = Long.MAX_VALUE;

        events.orderAccepted(message);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderAcceptedVisited);
        assertEquals(Long.MAX_VALUE, visitor.lastOrderAccepted.timestamp);
        assertEquals(Long.MAX_VALUE, visitor.lastOrderAccepted.orderNumber);
    }

    @Test
    void testMixedEventTypes() {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 50L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 3000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 50L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);
        events.orderCanceled(canceledMessage);

        TestEventVisitor visitor = new TestEventVisitor();
        events.accept(visitor);

        assertTrue(visitor.orderAcceptedVisited);
        assertTrue(visitor.orderExecutedVisited);
        assertTrue(visitor.orderCanceledVisited);
        assertFalse(visitor.orderRejectedVisited);
        assertEquals(1, visitor.orderAcceptedVisitCount);
        assertEquals(1, visitor.orderExecutedVisitCount);
        assertEquals(1, visitor.orderCanceledVisitCount);
        assertEquals(0, visitor.orderRejectedVisitCount);
    }

    @Test
    void testAcceptVisitsEventsInSequence() {
        POE.OrderAccepted message1 = new POE.OrderAccepted();
        message1.timestamp = 1000L;
        message1.orderId = "ORDER1".getBytes();
        message1.side = POE.BUY;
        message1.instrument = 1L;
        message1.quantity = 100L;
        message1.price = 5000L;
        message1.orderNumber = 1L;

        POE.OrderRejected message2 = new POE.OrderRejected();
        message2.timestamp = 2000L;
        message2.orderId = "ORDER2".getBytes();
        message2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderAccepted(message1);
        events.orderRejected(message2);

        SequenceTrackingVisitor visitor = new SequenceTrackingVisitor();
        events.accept(visitor);

        assertEquals(2, visitor.eventSequence.size());
        assertEquals("OrderAccepted", visitor.eventSequence.get(0));
        assertEquals("OrderRejected", visitor.eventSequence.get(1));
    }

    private static class TestEventVisitor implements EventVisitor {
        boolean orderAcceptedVisited = false;
        boolean orderRejectedVisited = false;
        boolean orderExecutedVisited = false;
        boolean orderCanceledVisited = false;
        int orderAcceptedVisitCount = 0;
        int orderRejectedVisitCount = 0;
        int orderExecutedVisitCount = 0;
        int orderCanceledVisitCount = 0;
        Event.OrderAccepted lastOrderAccepted = null;
        Event.OrderRejected lastOrderRejected = null;
        Event.OrderExecuted lastOrderExecuted = null;
        Event.OrderCanceled lastOrderCanceled = null;

        @Override
        public void visit(Event.OrderAccepted event) {
            orderAcceptedVisited = true;
            lastOrderAccepted = event;
            orderAcceptedVisitCount++;
        }

        @Override
        public void visit(Event.OrderRejected event) {
            orderRejectedVisited = true;
            lastOrderRejected = event;
            orderRejectedVisitCount++;
        }

        @Override
        public void visit(Event.OrderExecuted event) {
            orderExecutedVisited = true;
            lastOrderExecuted = event;
            orderExecutedVisitCount++;
        }

        @Override
        public void visit(Event.OrderCanceled event) {
            orderCanceledVisited = true;
            lastOrderCanceled = event;
            orderCanceledVisitCount++;
        }
    }

    private static class OrderTrackingVisitor implements EventVisitor {
        int orderAcceptedVisitCount = 0;
        String firstOrderId = null;
        String lastOrderId = null;

        @Override
        public void visit(Event.OrderAccepted event) {
            orderAcceptedVisitCount++;
            if (firstOrderId == null) {
                firstOrderId = event.orderId;
            }
            lastOrderId = event.orderId;
        }

        @Override
        public void visit(Event.OrderRejected event) {
        }

        @Override
        public void visit(Event.OrderExecuted event) {
        }

        @Override
        public void visit(Event.OrderCanceled event) {
        }
    }

    private static class SequenceTrackingVisitor implements EventVisitor {
        java.util.List<String> eventSequence = new java.util.ArrayList<>();

        @Override
        public void visit(Event.OrderAccepted event) {
            eventSequence.add("OrderAccepted");
        }

        @Override
        public void visit(Event.OrderRejected event) {
            eventSequence.add("OrderRejected");
        }

        @Override
        public void visit(Event.OrderExecuted event) {
            eventSequence.add("OrderExecuted");
        }

        @Override
        public void visit(Event.OrderCanceled event) {
            eventSequence.add("OrderCanceled");
        }
    }
}
