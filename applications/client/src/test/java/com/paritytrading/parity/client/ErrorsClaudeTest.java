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

import java.util.List;

class ErrorsClaudeTest {

    @Test
    void testCollectWithNoEvents() {
        Events events = new Events();

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testCollectWithSingleOrderRejectedEvent() {
        Events events = new Events();
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(poeMessage);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).format().contains("ORDER123"));
        assertTrue(errors.get(0).format().contains("Unknown instrument"));
    }

    @Test
    void testCollectWithMultipleOrderRejectedEvents() {
        Events events = new Events();

        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected poeMessage3 = new POE.OrderRejected();
        poeMessage3.timestamp = 3000L;
        poeMessage3.orderId = "ORDER3".getBytes();
        poeMessage3.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        events.orderRejected(poeMessage1);
        events.orderRejected(poeMessage2);
        events.orderRejected(poeMessage3);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(3, errors.size());
        assertTrue(errors.get(0).format().contains("ORDER1"));
        assertTrue(errors.get(0).format().contains("Unknown instrument"));
        assertTrue(errors.get(1).format().contains("ORDER2"));
        assertTrue(errors.get(1).format().contains("Invalid price"));
        assertTrue(errors.get(2).format().contains("ORDER3"));
        assertTrue(errors.get(2).format().contains("Invalid quantity"));
    }

    @Test
    void testCollectFiltersOnlyOrderRejectedEvents() {
        Events events = new Events();

        // Add various event types
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderRejected rejectedMessage = new POE.OrderRejected();
        rejectedMessage.timestamp = 2000L;
        rejectedMessage.orderId = "ORDER2".getBytes();
        rejectedMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 3000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 25L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 4000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 25L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderRejected(rejectedMessage);
        events.orderExecuted(executedMessage);
        events.orderCanceled(canceledMessage);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).format().contains("ORDER2"));
        assertTrue(errors.get(0).format().contains("Invalid price"));
    }

    @Test
    void testCollectWithMixedEventsAndMultipleRejections() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderRejected rejectedMessage1 = new POE.OrderRejected();
        rejectedMessage1.timestamp = 2000L;
        rejectedMessage1.orderId = "ORDER2".getBytes();
        rejectedMessage1.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected rejectedMessage2 = new POE.OrderRejected();
        rejectedMessage2.timestamp = 3000L;
        rejectedMessage2.orderId = "ORDER3".getBytes();
        rejectedMessage2.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderAccepted(acceptedMessage);
        events.orderRejected(rejectedMessage1);
        events.orderRejected(rejectedMessage2);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertTrue(errors.get(0).format().contains("ORDER2"));
        assertTrue(errors.get(1).format().contains("ORDER3"));
    }

    @Test
    void testCollectPreservesOrderOfEvents() {
        Events events = new Events();

        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "FIRST".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "SECOND".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected poeMessage3 = new POE.OrderRejected();
        poeMessage3.timestamp = 3000L;
        poeMessage3.orderId = "THIRD".getBytes();
        poeMessage3.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        events.orderRejected(poeMessage1);
        events.orderRejected(poeMessage2);
        events.orderRejected(poeMessage3);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(3, errors.size());
        assertTrue(errors.get(0).format().contains("FIRST"));
        assertTrue(errors.get(1).format().contains("SECOND"));
        assertTrue(errors.get(2).format().contains("THIRD"));
    }

    @Test
    void testCollectWithUnknownRejectReason() {
        Events events = new Events();

        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = (byte) 'X';

        events.orderRejected(poeMessage);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).format().contains("ORDER123"));
        assertTrue(errors.get(0).format().contains("<unknown>"));
    }

    @Test
    void testCollectReturnsMutableList() {
        Events events = new Events();

        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(poeMessage);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(1, errors.size());

        // The returned list should be mutable (it's a new ArrayList)
        errors.clear();
        assertEquals(0, errors.size());
    }

    @Test
    void testCollectMultipleTimesReturnsDifferentLists() {
        Events events = new Events();

        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(poeMessage);

        List<Error> errors1 = Errors.collect(events);
        List<Error> errors2 = Errors.collect(events);

        assertNotNull(errors1);
        assertNotNull(errors2);
        assertEquals(1, errors1.size());
        assertEquals(1, errors2.size());

        // They should be different list instances
        assertNotSame(errors1, errors2);

        // Modifying one should not affect the other
        errors1.clear();
        assertEquals(0, errors1.size());
        assertEquals(1, errors2.size());
    }

    @Test
    void testCollectWithEmptyOrderId() {
        Events events = new Events();

        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(poeMessage);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).format().contains("Invalid price"));
    }

    @Test
    void testVisitOrderRejectedDirectly() {
        // Create a custom visitor to test the visit method behavior
        class TestableErrors extends DefaultEventVisitor {
            private int visitCount = 0;

            @Override
            public void visit(Event.OrderRejected event) {
                visitCount++;
            }

            int getVisitCount() {
                return visitCount;
            }
        }

        TestableErrors visitor = new TestableErrors();

        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        visitor.visit(event);

        assertEquals(1, visitor.getVisitCount());
    }

    @Test
    void testCollectWithAllRejectReasonTypes() {
        Events events = new Events();

        POE.OrderRejected unknownInstrument = new POE.OrderRejected();
        unknownInstrument.timestamp = 1000L;
        unknownInstrument.orderId = "ORDER1".getBytes();
        unknownInstrument.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected invalidPrice = new POE.OrderRejected();
        invalidPrice.timestamp = 2000L;
        invalidPrice.orderId = "ORDER2".getBytes();
        invalidPrice.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected invalidQuantity = new POE.OrderRejected();
        invalidQuantity.timestamp = 3000L;
        invalidQuantity.orderId = "ORDER3".getBytes();
        invalidQuantity.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        events.orderRejected(unknownInstrument);
        events.orderRejected(invalidPrice);
        events.orderRejected(invalidQuantity);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(3, errors.size());
        assertTrue(errors.get(0).format().contains("Unknown instrument"));
        assertTrue(errors.get(1).format().contains("Invalid price"));
        assertTrue(errors.get(2).format().contains("Invalid quantity"));
    }

    @Test
    void testCollectWithDuplicateOrderIds() {
        Events events = new Events();

        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER123".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER123".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(poeMessage1);
        events.orderRejected(poeMessage2);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertTrue(errors.get(0).format().contains("ORDER123"));
        assertTrue(errors.get(0).format().contains("Invalid price"));
        assertTrue(errors.get(1).format().contains("ORDER123"));
        assertTrue(errors.get(1).format().contains("Unknown instrument"));
    }
}
