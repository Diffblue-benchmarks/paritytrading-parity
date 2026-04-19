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
package com.paritytrading.parity.net.poe;

import static com.paritytrading.parity.net.poe.POE.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class POEClientListenerTest {

    private List<Object> events;
    private POEClientParser parser;

    @BeforeEach
    void setUp() {
        events = new ArrayList<>();

        POEClientListener listener = new POEClientListener() {
            @Override
            public void orderAccepted(OrderAccepted message) {
                events.add("orderAccepted");
            }

            @Override
            public void orderRejected(OrderRejected message) {
                events.add("orderRejected");
            }

            @Override
            public void orderExecuted(OrderExecuted message) {
                events.add("orderExecuted");
            }

            @Override
            public void orderCanceled(OrderCanceled message) {
                events.add("orderCanceled");
            }
        };

        parser = new POEClientParser(listener);
    }

    @Test
    void orderAccepted() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_OUTBOUND_MESSAGE_LENGTH);

        OrderAccepted msg = new OrderAccepted();
        msg.timestamp   = 1000L;
        msg.side        = BUY;
        msg.instrument  = 1L;
        msg.quantity    = 100L;
        msg.price       = 5000L;
        msg.orderNumber = 1L;
        msg.put(buffer);

        buffer.flip();
        parser.message(buffer);

        assertEquals(1, events.size());
        assertEquals("orderAccepted", events.get(0));
    }

    @Test
    void orderRejected() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_OUTBOUND_MESSAGE_LENGTH);

        OrderRejected msg = new OrderRejected();
        msg.timestamp = 2000L;
        msg.reason    = ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;
        msg.put(buffer);

        buffer.flip();
        parser.message(buffer);

        assertEquals(1, events.size());
        assertEquals("orderRejected", events.get(0));
    }

    @Test
    void orderExecuted() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_OUTBOUND_MESSAGE_LENGTH);

        OrderExecuted msg = new OrderExecuted();
        msg.timestamp     = 3000L;
        msg.quantity      = 50L;
        msg.price         = 4500L;
        msg.liquidityFlag = LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        msg.matchNumber   = 42L;
        msg.put(buffer);

        buffer.flip();
        parser.message(buffer);

        assertEquals(1, events.size());
        assertEquals("orderExecuted", events.get(0));
    }

    @Test
    void orderCanceled() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_OUTBOUND_MESSAGE_LENGTH);

        OrderCanceled msg = new OrderCanceled();
        msg.timestamp        = 4000L;
        msg.canceledQuantity = 75L;
        msg.reason           = ORDER_CANCEL_REASON_REQUEST;
        msg.put(buffer);

        buffer.flip();
        parser.message(buffer);

        assertEquals(1, events.size());
        assertEquals("orderCanceled", events.get(0));
    }
}
