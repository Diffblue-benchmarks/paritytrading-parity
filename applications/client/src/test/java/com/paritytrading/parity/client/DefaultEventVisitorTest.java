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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

public class DefaultEventVisitorTest {

    @Test
    public void testVisitOrderAccepted() {
        DefaultEventVisitor visitor = new DefaultEventVisitor();

        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER123");
        message.side = POE.BUY;
        message.instrument = ASCII.packLong("FOO");
        message.quantity = 100L;
        message.price = 5000L;
        message.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(message);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    public void testVisitOrderRejected() {
        DefaultEventVisitor visitor = new DefaultEventVisitor();

        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER123");
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(message);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    public void testVisitOrderExecuted() {
        DefaultEventVisitor visitor = new DefaultEventVisitor();

        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = 2000000000L;
        ASCII.putLeft(message.orderId, "ORDER123");
        message.quantity = 50L;
        message.price = 5000L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        message.matchNumber = 42L;

        Event.OrderExecuted event = new Event.OrderExecuted(message);

        assertDoesNotThrow(() -> visitor.visit(event));
    }

    @Test
    public void testVisitOrderCanceled() {
        DefaultEventVisitor visitor = new DefaultEventVisitor();

        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = 3000000000L;
        ASCII.putLeft(message.orderId, "ORDER123");
        message.canceledQuantity = 50L;
        message.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled event = new Event.OrderCanceled(message);

        assertDoesNotThrow(() -> visitor.visit(event));
    }
}
