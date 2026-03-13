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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventsTest {

    @Mock
    private EventVisitor visitor;

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void testOrderAcceptedAddsEvent() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER-001");
        message.side = (byte) 'B';
        message.instrument = ASCII.packLong("AAPL");
        message.quantity = 100L;
        message.price = 15000L;
        message.orderNumber = 1L;

        events.orderAccepted(message);
        events.accept(visitor);

        verify(visitor).visit(any(Event.OrderAccepted.class));
    }

    @Test
    void testOrderRejectedAddsEvent() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2000000000L;
        ASCII.putLeft(message.orderId, "ORDER-002");
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(message);
        events.accept(visitor);

        verify(visitor).visit(any(Event.OrderRejected.class));
    }

    @Test
    void testOrderExecutedAddsEvent() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 3000000000L;
        ASCII.putLeft(message.orderId, "ORDER-003");
        message.quantity = 50L;
        message.price = 15100L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber = 123L;

        events.orderExecuted(message);
        events.accept(visitor);

        verify(visitor).visit(any(Event.OrderExecuted.class));
    }

    @Test
    void testOrderCanceledAddsEvent() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 4000000000L;
        ASCII.putLeft(message.orderId, "ORDER-004");
        message.canceledQuantity = 25L;
        message.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderCanceled(message);
        events.accept(visitor);

        verify(visitor).visit(any(Event.OrderCanceled.class));
    }

    @Test
    void testMultipleEventsCallVisitorInOrder() {
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
        executed.quantity = 50L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderAccepted(accepted);
        events.orderExecuted(executed);
        events.accept(visitor);

        verify(visitor).visit(any(Event.OrderAccepted.class));
        verify(visitor).visit(any(Event.OrderExecuted.class));
    }

    @Test
    void testAcceptWithNoEvents() {
        events.accept(visitor);

        verifyNoInteractions(visitor);
    }

    @Test
    void testAcceptCanBeCalledMultipleTimes() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER-001");
        message.side = (byte) 'B';
        message.instrument = ASCII.packLong("AAPL");
        message.quantity = 100L;
        message.price = 15000L;
        message.orderNumber = 1L;

        events.orderAccepted(message);
        events.accept(visitor);
        events.accept(visitor);

        verify(visitor, times(2)).visit(any(Event.OrderAccepted.class));
    }

    @Test
    void testMixedEventTypes() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 2000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-002");
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 3000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 50L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(accepted);
        events.orderRejected(rejected);
        events.orderCanceled(canceled);
        events.accept(visitor);

        verify(visitor).visit(any(Event.OrderAccepted.class));
        verify(visitor).visit(any(Event.OrderRejected.class));
        verify(visitor).visit(any(Event.OrderCanceled.class));
    }

    @Test
    void testEventsPreservesOrder() {
        POE.OrderAccepted first = new POE.OrderAccepted();
        first.timestamp = 1000000000L;
        ASCII.putLeft(first.orderId, "ORDER-001");
        first.side = (byte) 'B';
        first.instrument = ASCII.packLong("AAPL");
        first.quantity = 100L;
        first.price = 15000L;
        first.orderNumber = 1L;

        POE.OrderAccepted second = new POE.OrderAccepted();
        second.timestamp = 2000000000L;
        ASCII.putLeft(second.orderId, "ORDER-002");
        second.side = (byte) 'S';
        second.instrument = ASCII.packLong("MSFT");
        second.quantity = 200L;
        second.price = 25000L;
        second.orderNumber = 2L;

        events.orderAccepted(first);
        events.orderAccepted(second);

        EventVisitor orderCheckingVisitor = new DefaultEventVisitor() {
            private int count = 0;

            @Override
            public void visit(Event.OrderAccepted event) {
                if (count == 0) {
                    assert event.orderId.startsWith("ORDER-001");
                } else if (count == 1) {
                    assert event.orderId.startsWith("ORDER-002");
                }
                count++;
            }
        };

        events.accept(orderCheckingVisitor);
    }

    @Test
    void testMultipleExecutionEvents() {
        POE.OrderExecuted exec1 = new POE.OrderExecuted();
        exec1.timestamp = 1000000000L;
        ASCII.putLeft(exec1.orderId, "ORDER-001");
        exec1.quantity = 25L;
        exec1.price = 15000L;
        exec1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec1.matchNumber = 1L;

        POE.OrderExecuted exec2 = new POE.OrderExecuted();
        exec2.timestamp = 2000000000L;
        ASCII.putLeft(exec2.orderId, "ORDER-001");
        exec2.quantity = 25L;
        exec2.price = 15000L;
        exec2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec2.matchNumber = 2L;

        events.orderExecuted(exec1);
        events.orderExecuted(exec2);
        events.accept(visitor);

        verify(visitor, times(2)).visit(any(Event.OrderExecuted.class));
    }
}
