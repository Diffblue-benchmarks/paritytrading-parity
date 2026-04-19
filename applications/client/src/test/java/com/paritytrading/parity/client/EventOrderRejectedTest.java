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

class EventOrderRejectedTest {

    @Test
    void constructorExtractsFields() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 123456789L;
        message.orderId   = new byte[] {
            'A', 'B', 'C', '1', '2', '3', ' ', ' ',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '
        };
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(message);

        assertEquals(123456789L, event.timestamp);
        assertEquals("ABC123          ", event.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT, event.reason);
    }

    @Test
    void acceptCallsVisitorWithSelf() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 100L;
        message.orderId   = new byte[] {
            'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '
        };
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(message);

        Event.OrderRejected[] visited = new Event.OrderRejected[1];

        EventVisitor visitor = new EventVisitor() {
            @Override
            public void visit(Event.OrderAccepted e) {}

            @Override
            public void visit(Event.OrderRejected e) {
                visited[0] = e;
            }

            @Override
            public void visit(Event.OrderExecuted e) {}

            @Override
            public void visit(Event.OrderCanceled e) {}
        };

        event.accept(visitor);

        assertSame(event, visited[0]);
    }
}
