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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class POEServerListenerTest {

    private List<POE.InboundMessage> messages;
    private POEServerParser parser;

    @BeforeEach
    void setUp() {
        messages = new ArrayList<>();

        POEServerListener listener = new POEServerListener() {
            @Override
            public void enterOrder(POE.EnterOrder message) throws IOException {
                POE.EnterOrder copy = new POE.EnterOrder();
                System.arraycopy(message.orderId, 0, copy.orderId, 0, message.orderId.length);
                copy.side       = message.side;
                copy.instrument = message.instrument;
                copy.quantity   = message.quantity;
                copy.price      = message.price;
                messages.add(copy);
            }

            @Override
            public void cancelOrder(POE.CancelOrder message) throws IOException {
                POE.CancelOrder copy = new POE.CancelOrder();
                System.arraycopy(message.orderId, 0, copy.orderId, 0, message.orderId.length);
                copy.quantity = message.quantity;
                messages.add(copy);
            }
        };

        parser = new POEServerParser(listener);
    }

    @Test
    void enterOrder() throws IOException {
        POE.EnterOrder order = new POE.EnterOrder();
        for (int i = 0; i < order.orderId.length; i++)
            order.orderId[i] = (byte) ('A' + i);
        order.side       = POE.BUY;
        order.instrument = 100L;
        order.quantity   = 200L;
        order.price      = 300L;

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_INBOUND_MESSAGE_LENGTH);
        order.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, messages.size());
        POE.EnterOrder received = (POE.EnterOrder) messages.get(0);
        assertArrayEquals(order.orderId, received.orderId);
        assertEquals(POE.BUY, received.side);
        assertEquals(100L, received.instrument);
        assertEquals(200L, received.quantity);
        assertEquals(300L, received.price);
    }

    @Test
    void cancelOrder() throws IOException {
        POE.CancelOrder order = new POE.CancelOrder();
        for (int i = 0; i < order.orderId.length; i++)
            order.orderId[i] = (byte) ('Z' - i);
        order.quantity = 500L;

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_INBOUND_MESSAGE_LENGTH);
        order.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, messages.size());
        POE.CancelOrder received = (POE.CancelOrder) messages.get(0);
        assertArrayEquals(order.orderId, received.orderId);
        assertEquals(500L, received.quantity);
    }
}
