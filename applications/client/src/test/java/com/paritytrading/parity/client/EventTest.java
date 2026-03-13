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

import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class EventTest {

    @Test
    public void testOrderAcceptedAccept() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 123456789L;
        message.orderId = "ORDER123".getBytes();
        message.side = POE.BUY;
        message.instrument = 1L;
        message.quantity = 100L;
        message.price = 5000L;
        message.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(message);
        EventVisitor visitor = Mockito.mock(EventVisitor.class);

        event.accept(visitor);

        verify(visitor).visit(event);
    }

    @Test
    public void testOrderRejectedAccept() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 123456789L;
        message.orderId = "ORDER123".getBytes();
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(message);
        EventVisitor visitor = Mockito.mock(EventVisitor.class);

        event.accept(visitor);

        verify(visitor).visit(event);
    }

    @Test
    public void testOrderExecutedAccept() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 123456789L;
        message.orderId = "ORDER123".getBytes();
        message.quantity = 50L;
        message.price = 5000L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber = 999L;

        Event.OrderExecuted event = new Event.OrderExecuted(message);
        EventVisitor visitor = Mockito.mock(EventVisitor.class);

        event.accept(visitor);

        verify(visitor).visit(event);
    }

    @Test
    public void testOrderCanceledAccept() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 123456789L;
        message.orderId = "ORDER123".getBytes();
        message.canceledQuantity = 25L;
        message.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled event = new Event.OrderCanceled(message);
        EventVisitor visitor = Mockito.mock(EventVisitor.class);

        event.accept(visitor);

        verify(visitor).visit(event);
    }

}
