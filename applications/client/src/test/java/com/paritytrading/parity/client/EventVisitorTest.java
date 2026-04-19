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
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventVisitorTest {

    private List<String> visited;
    private EventVisitor visitor;

    @BeforeEach
    void setUp() {
        visited = new ArrayList<>();

        visitor = new EventVisitor() {
            @Override
            public void visit(Event.OrderAccepted event) {
                visited.add("OrderAccepted");
            }

            @Override
            public void visit(Event.OrderRejected event) {
                visited.add("OrderRejected");
            }

            @Override
            public void visit(Event.OrderExecuted event) {
                visited.add("OrderExecuted");
            }

            @Override
            public void visit(Event.OrderCanceled event) {
                visited.add("OrderCanceled");
            }
        };
    }

    @Test
    void visitOrderAccepted() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp   = 1L;
        message.side        = POE.BUY;
        message.instrument  = 100L;
        message.quantity    = 10L;
        message.price       = 1000L;
        message.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(message);

        visitor.visit(event);

        assertEquals(1, visited.size());
        assertEquals("OrderAccepted", visited.get(0));
    }

    @Test
    void visitOrderRejected() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2L;
        message.reason    = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(message);

        visitor.visit(event);

        assertEquals(1, visited.size());
        assertEquals("OrderRejected", visited.get(0));
    }

    @Test
    void visitOrderExecuted() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp     = 3L;
        message.quantity      = 5L;
        message.price         = 500L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber   = 1L;

        Event.OrderExecuted event = new Event.OrderExecuted(message);

        visitor.visit(event);

        assertEquals(1, visited.size());
        assertEquals("OrderExecuted", visited.get(0));
    }

    @Test
    void visitOrderCanceled() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp        = 4L;
        message.canceledQuantity = 10L;
        message.reason           = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled event = new Event.OrderCanceled(message);

        visitor.visit(event);

        assertEquals(1, visited.size());
        assertEquals("OrderCanceled", visited.get(0));
    }
}
