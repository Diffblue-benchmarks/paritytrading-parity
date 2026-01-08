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

class OrdersClaudeTest {

    @Test
    void testCollectWithEmptyEvents() {
        Events events = new Events();

        List<Order> orders = Orders.collect(events);

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    void testCollectWithSingleOrderAccepted() {
        Events events = new Events();
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000L;
        message.orderId = "ORDER1".getBytes();
        message.side = POE.BUY;
        message.instrument = 1L;
        message.quantity = 100L;
        message.price = 5000L;
        message.orderNumber = 1L;

        events.orderAccepted(message);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertEquals("ORDER1", order.getOrderId());
        assertEquals(1000L, order.getTimestamp());
        assertEquals(100L, order.getQuantity());
        assertEquals(POE.BUY, order.getSide());
        assertEquals(1L, order.getInstrument());
    }

    @Test
    void testCollectWithMultipleOrdersAccepted() {
        Events events = new Events();

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

        List<Order> orders = Orders.collect(events);

        assertEquals(2, orders.size());
        assertEquals("ORDER1", orders.get(0).getOrderId());
        assertEquals("ORDER2", orders.get(1).getOrderId());
    }

    @Test
    void testCollectOrdersSortedByTimestamp() {
        Events events = new Events();

        POE.OrderAccepted message1 = new POE.OrderAccepted();
        message1.timestamp = 3000L;
        message1.orderId = "ORDER3".getBytes();
        message1.side = POE.BUY;
        message1.instrument = 1L;
        message1.quantity = 100L;
        message1.price = 5000L;
        message1.orderNumber = 3L;

        POE.OrderAccepted message2 = new POE.OrderAccepted();
        message2.timestamp = 1000L;
        message2.orderId = "ORDER1".getBytes();
        message2.side = POE.BUY;
        message2.instrument = 1L;
        message2.quantity = 100L;
        message2.price = 5000L;
        message2.orderNumber = 1L;

        POE.OrderAccepted message3 = new POE.OrderAccepted();
        message3.timestamp = 2000L;
        message3.orderId = "ORDER2".getBytes();
        message3.side = POE.BUY;
        message3.instrument = 1L;
        message3.quantity = 100L;
        message3.price = 5000L;
        message3.orderNumber = 2L;

        events.orderAccepted(message1);
        events.orderAccepted(message2);
        events.orderAccepted(message3);

        List<Order> orders = Orders.collect(events);

        assertEquals(3, orders.size());
        assertEquals("ORDER1", orders.get(0).getOrderId());
        assertEquals("ORDER2", orders.get(1).getOrderId());
        assertEquals("ORDER3", orders.get(2).getOrderId());
        assertEquals(1000L, orders.get(0).getTimestamp());
        assertEquals(2000L, orders.get(1).getTimestamp());
        assertEquals(3000L, orders.get(2).getTimestamp());
    }

    @Test
    void testVisitOrderAcceptedAddsOrder() {
        Events events = new Events();
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000L;
        message.orderId = "ORDER1".getBytes();
        message.side = POE.BUY;
        message.instrument = 1L;
        message.quantity = 100L;
        message.price = 5000L;
        message.orderNumber = 1L;

        events.orderAccepted(message);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals("ORDER1", orders.get(0).getOrderId());
    }

    @Test
    void testVisitOrderExecutedReducesQuantity() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 300L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(700L, orders.get(0).getQuantity());
    }

    @Test
    void testVisitOrderExecutedRemovesOrderWhenQuantityBecomesZero() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 500L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 500L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testVisitOrderExecutedRemovesOrderWhenQuantityBecomesNegative() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 300L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 500L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testVisitOrderExecutedWithNonExistentOrder() {
        Events events = new Events();

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        executedMessage.orderId = "NONEXISTENT".getBytes();
        executedMessage.quantity = 100L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderExecuted(executedMessage);

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testVisitOrderCanceledReducesQuantity() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 2000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 400L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderCanceled(canceledMessage);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(600L, orders.get(0).getQuantity());
    }

    @Test
    void testVisitOrderCanceledRemovesOrderWhenQuantityBecomesZero() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 500L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 2000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 500L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderCanceled(canceledMessage);

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testVisitOrderCanceledRemovesOrderWhenQuantityBecomesNegative() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 300L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 2000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 500L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderCanceled(canceledMessage);

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testVisitOrderCanceledWithNonExistentOrder() {
        Events events = new Events();

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 2000L;
        canceledMessage.orderId = "NONEXISTENT".getBytes();
        canceledMessage.canceledQuantity = 100L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderCanceled(canceledMessage);

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testMultipleExecutionsOnSameOrder() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage1 = new POE.OrderExecuted();
        executedMessage1.timestamp = 2000L;
        executedMessage1.orderId = "ORDER1".getBytes();
        executedMessage1.quantity = 200L;
        executedMessage1.price = 5000L;
        executedMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage1.matchNumber = 1L;

        POE.OrderExecuted executedMessage2 = new POE.OrderExecuted();
        executedMessage2.timestamp = 3000L;
        executedMessage2.orderId = "ORDER1".getBytes();
        executedMessage2.quantity = 300L;
        executedMessage2.price = 5000L;
        executedMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage2.matchNumber = 2L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage1);
        events.orderExecuted(executedMessage2);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(500L, orders.get(0).getQuantity());
    }

    @Test
    void testMultipleCancellationsOnSameOrder() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderCanceled canceledMessage1 = new POE.OrderCanceled();
        canceledMessage1.timestamp = 2000L;
        canceledMessage1.orderId = "ORDER1".getBytes();
        canceledMessage1.canceledQuantity = 200L;
        canceledMessage1.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        POE.OrderCanceled canceledMessage2 = new POE.OrderCanceled();
        canceledMessage2.timestamp = 3000L;
        canceledMessage2.orderId = "ORDER1".getBytes();
        canceledMessage2.canceledQuantity = 300L;
        canceledMessage2.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderCanceled(canceledMessage1);
        events.orderCanceled(canceledMessage2);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(500L, orders.get(0).getQuantity());
    }

    @Test
    void testMixedExecutionsAndCancellationsOnSameOrder() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 250L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 3000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 350L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);
        events.orderCanceled(canceledMessage);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(400L, orders.get(0).getQuantity());
    }

    @Test
    void testMultipleOrdersWithDifferentFates() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage1 = new POE.OrderAccepted();
        acceptedMessage1.timestamp = 1000L;
        acceptedMessage1.orderId = "ORDER1".getBytes();
        acceptedMessage1.side = POE.BUY;
        acceptedMessage1.instrument = 1L;
        acceptedMessage1.quantity = 1000L;
        acceptedMessage1.price = 5000L;
        acceptedMessage1.orderNumber = 1L;

        POE.OrderAccepted acceptedMessage2 = new POE.OrderAccepted();
        acceptedMessage2.timestamp = 2000L;
        acceptedMessage2.orderId = "ORDER2".getBytes();
        acceptedMessage2.side = POE.SELL;
        acceptedMessage2.instrument = 2L;
        acceptedMessage2.quantity = 500L;
        acceptedMessage2.price = 6000L;
        acceptedMessage2.orderNumber = 2L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 3000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 1000L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage1);
        events.orderAccepted(acceptedMessage2);
        events.orderExecuted(executedMessage);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals("ORDER2", orders.get(0).getOrderId());
        assertEquals(500L, orders.get(0).getQuantity());
    }

    @Test
    void testOrderExecutedWithZeroQuantity() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 0L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(1000L, orders.get(0).getQuantity());
    }

    @Test
    void testOrderCanceledWithZeroQuantity() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 2000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 0L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderCanceled(canceledMessage);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(1000L, orders.get(0).getQuantity());
    }

    @Test
    void testOrderRejectedDoesNotAffectOrders() {
        Events events = new Events();

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
        rejectedMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderAccepted(acceptedMessage);
        events.orderRejected(rejectedMessage);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals("ORDER1", orders.get(0).getOrderId());
    }

    @Test
    void testCollectMultipleTimes() {
        Events events = new Events();

        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000L;
        message.orderId = "ORDER1".getBytes();
        message.side = POE.BUY;
        message.instrument = 1L;
        message.quantity = 100L;
        message.price = 5000L;
        message.orderNumber = 1L;

        events.orderAccepted(message);

        List<Order> orders1 = Orders.collect(events);
        List<Order> orders2 = Orders.collect(events);

        assertEquals(1, orders1.size());
        assertEquals(1, orders2.size());
        assertEquals("ORDER1", orders1.get(0).getOrderId());
        assertEquals("ORDER1", orders2.get(0).getOrderId());
    }

    @Test
    void testPartialExecutionThenFullCancellation() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 1000L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 400L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 3000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 600L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);
        events.orderCanceled(canceledMessage);

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testOrderWithSameTimestampMaintainsStableSort() {
        Events events = new Events();

        POE.OrderAccepted message1 = new POE.OrderAccepted();
        message1.timestamp = 1000L;
        message1.orderId = "ORDER1".getBytes();
        message1.side = POE.BUY;
        message1.instrument = 1L;
        message1.quantity = 100L;
        message1.price = 5000L;
        message1.orderNumber = 1L;

        POE.OrderAccepted message2 = new POE.OrderAccepted();
        message2.timestamp = 1000L;
        message2.orderId = "ORDER2".getBytes();
        message2.side = POE.SELL;
        message2.instrument = 2L;
        message2.quantity = 200L;
        message2.price = 6000L;
        message2.orderNumber = 2L;

        events.orderAccepted(message1);
        events.orderAccepted(message2);

        List<Order> orders = Orders.collect(events);

        assertEquals(2, orders.size());
        assertEquals(1000L, orders.get(0).getTimestamp());
        assertEquals(1000L, orders.get(1).getTimestamp());
    }

    @Test
    void testComplexScenarioWithMultipleOrdersAndActions() {
        Events events = new Events();

        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000L;
        accepted1.orderId = "ORDER1".getBytes();
        accepted1.side = POE.BUY;
        accepted1.instrument = 1L;
        accepted1.quantity = 1000L;
        accepted1.price = 5000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 2000L;
        accepted2.orderId = "ORDER2".getBytes();
        accepted2.side = POE.SELL;
        accepted2.instrument = 2L;
        accepted2.quantity = 2000L;
        accepted2.price = 6000L;
        accepted2.orderNumber = 2L;

        POE.OrderAccepted accepted3 = new POE.OrderAccepted();
        accepted3.timestamp = 3000L;
        accepted3.orderId = "ORDER3".getBytes();
        accepted3.side = POE.BUY;
        accepted3.instrument = 3L;
        accepted3.quantity = 1500L;
        accepted3.price = 7000L;
        accepted3.orderNumber = 3L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 4000L;
        executed1.orderId = "ORDER1".getBytes();
        executed1.quantity = 600L;
        executed1.price = 5000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderCanceled canceled2 = new POE.OrderCanceled();
        canceled2.timestamp = 5000L;
        canceled2.orderId = "ORDER2".getBytes();
        canceled2.canceledQuantity = 500L;
        canceled2.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        POE.OrderExecuted executed3 = new POE.OrderExecuted();
        executed3.timestamp = 6000L;
        executed3.orderId = "ORDER3".getBytes();
        executed3.quantity = 1500L;
        executed3.price = 7000L;
        executed3.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed3.matchNumber = 2L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderAccepted(accepted3);
        events.orderExecuted(executed1);
        events.orderCanceled(canceled2);
        events.orderExecuted(executed3);

        List<Order> orders = Orders.collect(events);

        assertEquals(2, orders.size());
        assertEquals("ORDER1", orders.get(0).getOrderId());
        assertEquals(400L, orders.get(0).getQuantity());
        assertEquals("ORDER2", orders.get(1).getOrderId());
        assertEquals(1500L, orders.get(1).getQuantity());
    }
}
