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

class EventOrderCanceledTest {

    @Test
    void constructor() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp        = 123456789L;
        message.orderId          = new byte[] { 'A', 'B', 'C', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message.canceledQuantity = 500L;
        message.reason           = (byte) 1;

        Event.OrderCanceled event = new Event.OrderCanceled(message);

        assertEquals(123456789L, event.timestamp);
        assertEquals("ABC             ", event.orderId);
        assertEquals(500L, event.canceledQuantity);
        assertEquals((byte) 1, event.reason);
    }

    @Test
    void accept() {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp        = 100L;
        message.orderId          = new byte[] { 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message.canceledQuantity = 200L;
        message.reason           = (byte) 2;

        Event.OrderCanceled event = new Event.OrderCanceled(message);

        Event.OrderCanceled[] visited = new Event.OrderCanceled[1];

        EventVisitor visitor = new EventVisitor() {
            @Override
            public void visit(Event.OrderAccepted event) {}

            @Override
            public void visit(Event.OrderRejected event) {}

            @Override
            public void visit(Event.OrderExecuted event) {}

            @Override
            public void visit(Event.OrderCanceled event) {
                visited[0] = event;
            }
        };

        event.accept(visitor);

        assertSame(event, visited[0]);
    }
}
