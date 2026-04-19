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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventOrderAcceptedTest {

    private POE.OrderAccepted message;

    @BeforeEach
    void setUp() {
        message = new POE.OrderAccepted();
        message.timestamp   = 123456789L;
        message.orderId     = new byte[] { 'A', 'B', 'C', '1', '2', '3', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message.side        = (byte) 'B';
        message.instrument  = 100L;
        message.quantity    = 500L;
        message.price       = 10050L;
        message.orderNumber = 42L;
    }

    @Test
    void constructor() {
        Event.OrderAccepted event = new Event.OrderAccepted(message);

        assertEquals(123456789L, event.timestamp);
        assertEquals(ASCII.get(message.orderId), event.orderId);
        assertEquals((byte) 'B', event.side);
        assertEquals(100L, event.instrument);
        assertEquals(500L, event.quantity);
        assertEquals(10050L, event.price);
        assertEquals(42L, event.orderNumber);
    }

    @Test
    void accept() {
        Event.OrderAccepted event = new Event.OrderAccepted(message);

        Event.OrderAccepted[] visited = new Event.OrderAccepted[1];

        EventVisitor visitor = new EventVisitor() {
            @Override
            public void visit(Event.OrderAccepted e) {
                visited[0] = e;
            }

            @Override
            public void visit(Event.OrderRejected e) {
            }

            @Override
            public void visit(Event.OrderExecuted e) {
            }

            @Override
            public void visit(Event.OrderCanceled e) {
            }
        };

        event.accept(visitor);

        assertSame(event, visited[0]);
    }
}
