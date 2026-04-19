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

class EventTest {

    @Test
    void acceptOrderAccepted() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp   = 1000L;
        message.side        = (byte) 'B';
        message.instrument  = 12345L;
        message.quantity    = 100L;
        message.price       = 5000L;
        message.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(message);

        boolean[] visited = {false};

        event.accept(new EventVisitor() {
            @Override
            public void visit(Event.OrderAccepted e) {
                visited[0] = true;
                assertSame(event, e);
            }

            @Override
            public void visit(Event.OrderRejected e) {
                fail("Unexpected visit to OrderRejected");
            }

            @Override
            public void visit(Event.OrderExecuted e) {
                fail("Unexpected visit to OrderExecuted");
            }

            @Override
            public void visit(Event.OrderCanceled e) {
                fail("Unexpected visit to OrderCanceled");
            }
        });

        assertTrue(visited[0]);
    }

    @Test
    void acceptOrderRejected() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2000L;
        message.reason    = (byte) 1;

        Event.OrderRejected event = new Event.OrderRejected(message);

        boolean[] visited = {false};

        event.accept(new EventVisitor() {
            @Override
            public void visit(Event.OrderAccepted e) {
                fail("Unexpected visit to OrderAccepted");
            }

            @Override
            public void visit(Event.OrderRejected e) {
                visited[0] = true;
                assertSame(event, e);
            }

            @Override
            public void visit(Event.OrderExecuted e) {
                fail("Unexpected visit to OrderExecuted");
            }

            @Override
            public void visit(Event.OrderCanceled e) {
                fail("Unexpected visit to OrderCanceled");
            }
        });

        assertTrue(visited[0]);
    }

    @Test
    void acceptOrderExecuted() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp     = 3000L;
        message.quantity      = 50L;
        message.price         = 4000L;
        message.liquidityFlag = (byte) 'A';
        message.matchNumber   = 99L;

        Event.OrderExecuted event = new Event.OrderExecuted(message);

        boolean[] visited = {false};

        event.accept(new EventVisitor() {
            @Override
            public void visit(Event.OrderAccepted e) {
                fail("Unexpected visit to OrderAccepted");
            }

            @Override
            public void visit(Event.OrderRejected e) {
                fail("Unexpected visit to OrderRejected");
            }

            @Override
            public void visit(Event.OrderExecuted e) {
                visited[0] = true;
                assertSame(event, e);
            }

            @Override
            public void visit(Event.OrderCanceled e) {
                fail("Unexpected visit to OrderCanceled");
            }
        });

        assertTrue(visited[0]);
    }

    @Test
    void acceptOrderCanceled() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp        = 4000L;
        message.canceledQuantity = 25L;
        message.reason           = (byte) 2;

        Event.OrderCanceled event = new Event.OrderCanceled(message);

        boolean[] visited = {false};

        event.accept(new EventVisitor() {
            @Override
            public void visit(Event.OrderAccepted e) {
                fail("Unexpected visit to OrderAccepted");
            }

            @Override
            public void visit(Event.OrderRejected e) {
                fail("Unexpected visit to OrderRejected");
            }

            @Override
            public void visit(Event.OrderExecuted e) {
                fail("Unexpected visit to OrderExecuted");
            }

            @Override
            public void visit(Event.OrderCanceled e) {
                visited[0] = true;
                assertSame(event, e);
            }
        });

        assertTrue(visited[0]);
    }
}
