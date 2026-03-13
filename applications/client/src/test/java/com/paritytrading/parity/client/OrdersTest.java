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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdersTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void testCollectWithNoEvents() {
        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testCollectWithSingleAcceptedOrder() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER-001");
        message.side = (byte) 'B';
        message.instrument = ASCII.packLong("AAPL");
        message.quantity = 100L;
        message.price = 15000L;
        message.orderNumber = 1L;

        events.orderAccepted(message);
        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertTrue(orders.get(0).getOrderId().startsWith("ORDER-001"));
        assertEquals(100L, orders.get(0).getQuantity());
    }

    @Test
    void testCollectWithMultipleAcceptedOrders() {
        POE.OrderAccepted order1 = new POE.OrderAccepted();
        order1.timestamp = 1000000000L;
        ASCII.putLeft(order1.orderId, "ORDER-001");
        order1.side = (byte) 'B';
        order1.instrument = ASCII.packLong("AAPL");
        order1.quantity = 100L;
        order1.price = 15000L;
        order1.orderNumber = 1L;

        POE.OrderAccepted order2 = new POE.OrderAccepted();
        order2.timestamp = 2000000000L;
        ASCII.putLeft(order2.orderId, "ORDER-002");
        order2.side = (byte) 'S';
        order2.instrument = ASCII.packLong("MSFT");
        order2.quantity = 200L;
        order2.price = 25000L;
        order2.orderNumber = 2L;

        events.orderAccepted(order1);
        events.orderAccepted(order2);
        List<Order> orders = Orders.collect(events);

        assertEquals(2, orders.size());
        assertTrue(orders.get(0).getOrderId().startsWith("ORDER-001"));
        assertTrue(orders.get(1).getOrderId().startsWith("ORDER-002"));
    }

    @Test
    void testOrderExecutedReducesQuantity() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 2000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.quantity = 30L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderAccepted(accepted);
        events.orderExecuted(executed);
        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(70L, orders.get(0).getQuantity());
    }

    @Test
    void testOrderFullyExecutedRemovesOrder() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 2000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.quantity = 100L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderAccepted(accepted);
        events.orderExecuted(executed);
        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testOrderCanceledReducesQuantity() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 2000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 40L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(accepted);
        events.orderCanceled(canceled);
        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(60L, orders.get(0).getQuantity());
    }

    @Test
    void testOrderFullyCanceledRemovesOrder() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 2000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 100L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(accepted);
        events.orderCanceled(canceled);
        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testExecutionWithoutAcceptedOrderIgnored() {
        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 1000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-UNKNOWN");
        executed.quantity = 50L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderExecuted(executed);
        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testCancelWithoutAcceptedOrderIgnored() {
        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 1000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-UNKNOWN");
        canceled.canceledQuantity = 50L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderCanceled(canceled);
        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testMultipleExecutionsOnSameOrder() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted exec1 = new POE.OrderExecuted();
        exec1.timestamp = 2000000000L;
        ASCII.putLeft(exec1.orderId, "ORDER-001");
        exec1.quantity = 30L;
        exec1.price = 15000L;
        exec1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec1.matchNumber = 1L;

        POE.OrderExecuted exec2 = new POE.OrderExecuted();
        exec2.timestamp = 3000000000L;
        ASCII.putLeft(exec2.orderId, "ORDER-001");
        exec2.quantity = 20L;
        exec2.price = 15000L;
        exec2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec2.matchNumber = 2L;

        events.orderAccepted(accepted);
        events.orderExecuted(exec1);
        events.orderExecuted(exec2);
        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(50L, orders.get(0).getQuantity());
    }

    @Test
    void testMixedExecutionsAndCancels() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 2000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.quantity = 30L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 3000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 40L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(accepted);
        events.orderExecuted(executed);
        events.orderCanceled(canceled);
        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(30L, orders.get(0).getQuantity());
    }

    @Test
    void testOrdersSortedByTimestamp() {
        POE.OrderAccepted order1 = new POE.OrderAccepted();
        order1.timestamp = 3000000000L;
        ASCII.putLeft(order1.orderId, "ORDER-003");
        order1.side = (byte) 'B';
        order1.instrument = ASCII.packLong("AAPL");
        order1.quantity = 100L;
        order1.price = 15000L;
        order1.orderNumber = 3L;

        POE.OrderAccepted order2 = new POE.OrderAccepted();
        order2.timestamp = 1000000000L;
        ASCII.putLeft(order2.orderId, "ORDER-001");
        order2.side = (byte) 'B';
        order2.instrument = ASCII.packLong("MSFT");
        order2.quantity = 200L;
        order2.price = 25000L;
        order2.orderNumber = 1L;

        POE.OrderAccepted order3 = new POE.OrderAccepted();
        order3.timestamp = 2000000000L;
        ASCII.putLeft(order3.orderId, "ORDER-002");
        order3.side = (byte) 'S';
        order3.instrument = ASCII.packLong("GOOG");
        order3.quantity = 150L;
        order3.price = 20000L;
        order3.orderNumber = 2L;

        events.orderAccepted(order1);
        events.orderAccepted(order2);
        events.orderAccepted(order3);
        List<Order> orders = Orders.collect(events);

        assertEquals(3, orders.size());
        assertEquals(1000000000L, orders.get(0).getTimestamp());
        assertEquals(2000000000L, orders.get(1).getTimestamp());
        assertEquals(3000000000L, orders.get(2).getTimestamp());
    }

    @Test
    void testOrderRejectedDoesNotCreateOrder() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-BAD");
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(rejected);
        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void testMultipleOrdersWithDifferentLifecycles() {
        POE.OrderAccepted order1 = new POE.OrderAccepted();
        order1.timestamp = 1000000000L;
        ASCII.putLeft(order1.orderId, "ORDER-001");
        order1.side = (byte) 'B';
        order1.instrument = ASCII.packLong("AAPL");
        order1.quantity = 100L;
        order1.price = 15000L;
        order1.orderNumber = 1L;

        POE.OrderAccepted order2 = new POE.OrderAccepted();
        order2.timestamp = 2000000000L;
        ASCII.putLeft(order2.orderId, "ORDER-002");
        order2.side = (byte) 'S';
        order2.instrument = ASCII.packLong("MSFT");
        order2.quantity = 200L;
        order2.price = 25000L;
        order2.orderNumber = 2L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 3000000000L;
        ASCII.putLeft(executed1.orderId, "ORDER-001");
        executed1.quantity = 100L;
        executed1.price = 15000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderCanceled canceled2 = new POE.OrderCanceled();
        canceled2.timestamp = 4000000000L;
        ASCII.putLeft(canceled2.orderId, "ORDER-002");
        canceled2.canceledQuantity = 50L;
        canceled2.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(order1);
        events.orderAccepted(order2);
        events.orderExecuted(executed1);
        events.orderCanceled(canceled2);
        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertTrue(orders.get(0).getOrderId().startsWith("ORDER-002"));
        assertEquals(150L, orders.get(0).getQuantity());
    }
}
