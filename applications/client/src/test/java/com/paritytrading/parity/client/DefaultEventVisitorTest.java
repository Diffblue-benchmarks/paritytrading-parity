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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultEventVisitorTest {

    private DefaultEventVisitor visitor;

    @BeforeEach
    void setUp() {
        visitor = new DefaultEventVisitor();
    }

    @Test
    void visitOrderAccepted() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp   = 1000L;
        message.side        = POE.BUY;
        message.instrument  = 1L;
        message.quantity    = 100L;
        message.price       = 5000L;
        message.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(message);

        visitor.visit(event);
    }

    @Test
    void visitOrderRejected() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2000L;
        message.reason    = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(message);

        visitor.visit(event);
    }

    @Test
    void visitOrderExecuted() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp     = 3000L;
        message.quantity      = 50L;
        message.price         = 5000L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber   = 1L;

        Event.OrderExecuted event = new Event.OrderExecuted(message);

        visitor.visit(event);
    }

    @Test
    void visitOrderCanceled() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp        = 4000L;
        message.canceledQuantity = 100L;
        message.reason           = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled event = new Event.OrderCanceled(message);

        visitor.visit(event);
    }
}
