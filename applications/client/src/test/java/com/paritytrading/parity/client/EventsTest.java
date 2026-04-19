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

class EventsTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void acceptWithNoEvents() {
        List<Event> visited = new ArrayList<>();

        events.accept(new CollectingVisitor(visited));

        assertTrue(visited.isEmpty());
    }

    @Test
    void orderAccepted() {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp   = 100L;
        message.orderId     = new byte[] { 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message.side        = (byte) 'B';
        message.instrument  = 200L;
        message.quantity    = 300L;
        message.price       = 400L;
        message.orderNumber = 500L;

        events.orderAccepted(message);

        List<Event> visited = new ArrayList<>();
        events.accept(new CollectingVisitor(visited));

        assertEquals(1, visited.size());
        assertTrue(visited.get(0) instanceof Event.OrderAccepted);
    }

    @Test
    void orderRejected() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 100L;
        message.orderId   = new byte[] { 'B', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message.reason    = (byte) 'R';

        events.orderRejected(message);

        List<Event> visited = new ArrayList<>();
        events.accept(new CollectingVisitor(visited));

        assertEquals(1, visited.size());
        assertTrue(visited.get(0) instanceof Event.OrderRejected);
    }

    @Test
    void orderExecuted() {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp     = 100L;
        message.orderId       = new byte[] { 'C', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message.quantity      = 200L;
        message.price         = 300L;
        message.liquidityFlag = (byte) 'A';
        message.matchNumber   = 400L;

        events.orderExecuted(message);

        List<Event> visited = new ArrayList<>();
        events.accept(new CollectingVisitor(visited));

        assertEquals(1, visited.size());
        assertTrue(visited.get(0) instanceof Event.OrderExecuted);
    }

    @Test
    void orderCanceled() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp        = 100L;
        message.orderId          = new byte[] { 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message.canceledQuantity = 200L;
        message.reason           = (byte) 'U';

        events.orderCanceled(message);

        List<Event> visited = new ArrayList<>();
        events.accept(new CollectingVisitor(visited));

        assertEquals(1, visited.size());
        assertTrue(visited.get(0) instanceof Event.OrderCanceled);
    }

    @Test
    void multipleEvents() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp   = 1L;
        accepted.orderId     = new byte[] { '1', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        accepted.side        = (byte) 'B';
        accepted.instrument  = 2L;
        accepted.quantity    = 3L;
        accepted.price       = 4L;
        accepted.orderNumber = 5L;

        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 6L;
        rejected.orderId   = new byte[] { '2', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        rejected.reason    = (byte) 'R';

        events.orderAccepted(accepted);
        events.orderRejected(rejected);

        List<Event> visited = new ArrayList<>();
        events.accept(new CollectingVisitor(visited));

        assertEquals(2, visited.size());
        assertTrue(visited.get(0) instanceof Event.OrderAccepted);
        assertTrue(visited.get(1) instanceof Event.OrderRejected);
    }

    private static class CollectingVisitor implements EventVisitor {

        private final List<Event> collected;

        CollectingVisitor(List<Event> collected) {
            this.collected = collected;
        }

        @Override
        public void visit(Event.OrderAccepted event) {
            collected.add(event);
        }

        @Override
        public void visit(Event.OrderRejected event) {
            collected.add(event);
        }

        @Override
        public void visit(Event.OrderExecuted event) {
            collected.add(event);
        }

        @Override
        public void visit(Event.OrderCanceled event) {
            collected.add(event);
        }
    }
}
