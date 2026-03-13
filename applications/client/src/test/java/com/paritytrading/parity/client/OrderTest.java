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

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Event.OrderAccepted acceptedEvent;
    private Order order;

    @BeforeEach
    void setUp() {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        ASCII.putLeft(acceptedMessage.orderId, "ORDER-001");
        acceptedMessage.side = (byte) 'B';
        acceptedMessage.instrument = ASCII.packLong("AAPL");
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 15000L;
        acceptedMessage.orderNumber = 1L;

        acceptedEvent = new Event.OrderAccepted(acceptedMessage);
        order = new Order(acceptedEvent);
    }

    @Test
    void testOrderCreatedFromAcceptedEvent() {
        assertEquals(1000000000L, order.getTimestamp());
        assertTrue(order.getOrderId().startsWith("ORDER-001"));
        assertEquals((byte) 'B', order.getSide());
        assertEquals(ASCII.packLong("AAPL"), order.getInstrument());
        assertEquals(100L, order.getQuantity());
    }

    @Test
    void testApplyOrderExecutedReducesQuantity() {
        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 1000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.quantity = 30L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(executed);
        order.apply(executedEvent);

        assertEquals(70L, order.getQuantity());
    }

    @Test
    void testApplyMultipleExecutions() {
        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.quantity = 20L;
        executed1.timestamp = 1000000000L;
        ASCII.putLeft(executed1.orderId, "ORDER-001");
        executed1.price = 15000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.quantity = 30L;
        executed2.timestamp = 1000000000L;
        ASCII.putLeft(executed2.orderId, "ORDER-001");
        executed2.price = 15000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed2.matchNumber = 2L;

        order.apply(new Event.OrderExecuted(executed1));
        order.apply(new Event.OrderExecuted(executed2));

        assertEquals(50L, order.getQuantity());
    }

    @Test
    void testApplyOrderCanceledReducesQuantity() {
        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 1000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 40L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled canceledEvent = new Event.OrderCanceled(canceled);
        order.apply(canceledEvent);

        assertEquals(60L, order.getQuantity());
    }

    @Test
    void testApplyFullCancellation() {
        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 1000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 100L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled canceledEvent = new Event.OrderCanceled(canceled);
        order.apply(canceledEvent);

        assertEquals(0L, order.getQuantity());
    }

    @Test
    void testApplyExecutionThenCancellation() {
        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.quantity = 30L;
        executed.timestamp = 1000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 1000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 40L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        order.apply(new Event.OrderExecuted(executed));
        order.apply(new Event.OrderCanceled(canceled));

        assertEquals(30L, order.getQuantity());
    }

    @Test
    void testGettersReturnCorrectValues() {
        assertEquals(1000000000L, order.getTimestamp());
        assertEquals((byte) 'B', order.getSide());
        assertEquals(ASCII.packLong("AAPL"), order.getInstrument());
        assertTrue(order.getOrderId().startsWith("ORDER-001"));
        assertEquals(100L, order.getQuantity());
    }

    @Test
    void testApplyDoesNotAffectImmutableFields() {
        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.quantity = 50L;
        executed.timestamp = 1000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        order.apply(new Event.OrderExecuted(executed));

        assertEquals(1000000000L, order.getTimestamp());
        assertTrue(order.getOrderId().startsWith("ORDER-001"));
        assertEquals((byte) 'B', order.getSide());
        assertEquals(ASCII.packLong("AAPL"), order.getInstrument());
    }
}
