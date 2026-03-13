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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventTest {

    @Mock
    private EventVisitor visitor;

    @Test
    void testOrderAcceptedEventCreation() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER-001");
        message.side = (byte) 'B';
        message.instrument = ASCII.packLong("AAPL");
        message.quantity = 100L;
        message.price = 15000L;
        message.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(message);

        assertEquals(1000000000L, event.timestamp);
        assertTrue(event.orderId.startsWith("ORDER-001"));
        assertEquals((byte) 'B', event.side);
        assertEquals(ASCII.packLong("AAPL"), event.instrument);
        assertEquals(100L, event.quantity);
        assertEquals(15000L, event.price);
        assertEquals(1L, event.orderNumber);
    }

    @Test
    void testOrderAcceptedAcceptCallsVisitor() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER-001");
        message.side = (byte) 'B';
        message.instrument = ASCII.packLong("AAPL");
        message.quantity = 100L;
        message.price = 15000L;
        message.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(message);
        event.accept(visitor);

        verify(visitor).visit(event);
    }

    @Test
    void testOrderRejectedEventCreation() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2000000000L;
        ASCII.putLeft(message.orderId, "ORDER-002");
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(message);

        assertEquals(2000000000L, event.timestamp);
        assertTrue(event.orderId.startsWith("ORDER-002"));
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, event.reason);
    }

    @Test
    void testOrderRejectedAcceptCallsVisitor() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2000000000L;
        ASCII.putLeft(message.orderId, "ORDER-002");
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(message);
        event.accept(visitor);

        verify(visitor).visit(event);
    }

    @Test
    void testOrderExecutedEventCreation() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 3000000000L;
        ASCII.putLeft(message.orderId, "ORDER-003");
        message.quantity = 50L;
        message.price = 15100L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber = 123L;

        Event.OrderExecuted event = new Event.OrderExecuted(message);

        assertEquals(3000000000L, event.timestamp);
        assertTrue(event.orderId.startsWith("ORDER-003"));
        assertEquals(50L, event.quantity);
        assertEquals(15100L, event.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, event.liquidityFlag);
        assertEquals(123L, event.matchNumber);
    }

    @Test
    void testOrderExecutedAcceptCallsVisitor() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 3000000000L;
        ASCII.putLeft(message.orderId, "ORDER-003");
        message.quantity = 50L;
        message.price = 15100L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber = 123L;

        Event.OrderExecuted event = new Event.OrderExecuted(message);
        event.accept(visitor);

        verify(visitor).visit(event);
    }

    @Test
    void testOrderCanceledEventCreation() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 4000000000L;
        ASCII.putLeft(message.orderId, "ORDER-004");
        message.canceledQuantity = 25L;
        message.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled event = new Event.OrderCanceled(message);

        assertEquals(4000000000L, event.timestamp);
        assertTrue(event.orderId.startsWith("ORDER-004"));
        assertEquals(25L, event.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, event.reason);
    }

    @Test
    void testOrderCanceledAcceptCallsVisitor() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 4000000000L;
        ASCII.putLeft(message.orderId, "ORDER-004");
        message.canceledQuantity = 25L;
        message.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled event = new Event.OrderCanceled(message);
        event.accept(visitor);

        verify(visitor).visit(event);
    }

    @Test
    void testOrderAcceptedWithDifferentSide() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER-SELL");
        message.side = (byte) 'S';
        message.instrument = ASCII.packLong("MSFT");
        message.quantity = 200L;
        message.price = 25000L;
        message.orderNumber = 2L;

        Event.OrderAccepted event = new Event.OrderAccepted(message);

        assertEquals((byte) 'S', event.side);
        assertEquals(ASCII.packLong("MSFT"), event.instrument);
        assertEquals(200L, event.quantity);
        assertEquals(25000L, event.price);
    }

    @Test
    void testOrderExecutedWithRemovedLiquidity() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 3000000000L;
        ASCII.putLeft(message.orderId, "ORDER-005");
        message.quantity = 75L;
        message.price = 15200L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        message.matchNumber = 456L;

        Event.OrderExecuted event = new Event.OrderExecuted(message);

        assertEquals(POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, event.liquidityFlag);
        assertEquals(456L, event.matchNumber);
    }
}
