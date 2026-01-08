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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultEventVisitorClaudeTest {

    private DefaultEventVisitor visitor;

    @BeforeEach
    void setUp() {
        visitor = new DefaultEventVisitor();
    }

    @Test
    void testConstructor() {
        DefaultEventVisitor newVisitor = new DefaultEventVisitor();
        assertNotNull(newVisitor);
    }

    @Test
    void testVisitorImplementsEventVisitorInterface() {
        assertTrue(visitor instanceof EventVisitor);
    }

    @Test
    void testVisitOrderAccepted() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    void testVisitOrderAcceptedWithNullFields() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.side = 0;
        poeMessage.instrument = 0L;
        poeMessage.quantity = 0L;
        poeMessage.price = 0L;
        poeMessage.orderNumber = 0L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    void testVisitOrderAcceptedMultipleTimes() {
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

        assertDoesNotThrow(() -> {
            visitor.visit(event1);
            visitor.visit(event2);
        });
    }

    @Test
    void testVisitOrderRejected() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    void testVisitOrderRejectedWithDifferentReasons() {
        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event1 = new Event.OrderRejected(poeMessage1);
        Event.OrderRejected event2 = new Event.OrderRejected(poeMessage2);

        assertDoesNotThrow(() -> {
            visitor.visit(event1);
            visitor.visit(event2);
        });
    }

    @Test
    void testVisitOrderRejectedWithNullFields() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = 0;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    void testVisitOrderExecuted() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeMessage.matchNumber = 123L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    void testVisitOrderExecutedWithDifferentLiquidityFlags() {
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

        assertDoesNotThrow(() -> {
            visitor.visit(event1);
            visitor.visit(event2);
        });
    }

    @Test
    void testVisitOrderExecutedWithNullFields() {
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.quantity = 0L;
        poeMessage.price = 0L;
        poeMessage.liquidityFlag = 0;
        poeMessage.matchNumber = 0L;

        Event.OrderExecuted event = new Event.OrderExecuted(poeMessage);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    void testVisitOrderCanceled() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.canceledQuantity = 50L;
        poeMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    void testVisitOrderCanceledWithDifferentReasons() {
        POE.OrderCanceled poeMessage1 = new POE.OrderCanceled();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.canceledQuantity = 50L;
        poeMessage1.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        POE.OrderCanceled poeMessage2 = new POE.OrderCanceled();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.canceledQuantity = 100L;
        poeMessage2.reason = POE.ORDER_CANCEL_REASON_SUPERVISORY;

        Event.OrderCanceled event1 = new Event.OrderCanceled(poeMessage1);
        Event.OrderCanceled event2 = new Event.OrderCanceled(poeMessage2);

        assertDoesNotThrow(() -> {
            visitor.visit(event1);
            visitor.visit(event2);
        });
    }

    @Test
    void testVisitOrderCanceledWithNullFields() {
        POE.OrderCanceled poeMessage = new POE.OrderCanceled();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.canceledQuantity = 0L;
        poeMessage.reason = 0;

        Event.OrderCanceled event = new Event.OrderCanceled(poeMessage);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    void testVisitAllEventTypes() {
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
        executedMessage.quantity = 100L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 4000L;
        canceledMessage.orderId = "ORDER4".getBytes();
        canceledMessage.canceledQuantity = 50L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(acceptedMessage);
        Event.OrderRejected rejectedEvent = new Event.OrderRejected(rejectedMessage);
        Event.OrderExecuted executedEvent = new Event.OrderExecuted(executedMessage);
        Event.OrderCanceled canceledEvent = new Event.OrderCanceled(canceledMessage);

        assertDoesNotThrow(() -> {
            visitor.visit(acceptedEvent);
            visitor.visit(rejectedEvent);
            visitor.visit(executedEvent);
            visitor.visit(canceledEvent);
        });
    }

    @Test
    void testVisitMethodsReturnVoid() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);

        visitor.visit(event);
    }

    @Test
    void testMultipleVisitorsCanBeCreated() {
        DefaultEventVisitor visitor1 = new DefaultEventVisitor();
        DefaultEventVisitor visitor2 = new DefaultEventVisitor();
        DefaultEventVisitor visitor3 = new DefaultEventVisitor();

        assertNotNull(visitor1);
        assertNotNull(visitor2);
        assertNotNull(visitor3);
        assertNotSame(visitor1, visitor2);
        assertNotSame(visitor2, visitor3);
    }
}
