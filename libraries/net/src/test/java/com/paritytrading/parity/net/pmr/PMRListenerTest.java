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
package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PMRListenerTest {

    private List<Object> events;
    private PMRParser parser;

    @BeforeEach
    void setUp() {
        events = new ArrayList<>();

        PMRListener listener = new PMRListener() {
            @Override
            public void version(PMR.Version message) throws IOException {
                events.add(message);
            }

            @Override
            public void orderEntered(PMR.OrderEntered message) throws IOException {
                events.add(message);
            }

            @Override
            public void orderAdded(PMR.OrderAdded message) throws IOException {
                events.add(message);
            }

            @Override
            public void orderCanceled(PMR.OrderCanceled message) throws IOException {
                events.add(message);
            }

            @Override
            public void trade(PMR.Trade message) throws IOException {
                events.add(message);
            }
        };

        parser = new PMRParser(listener);
    }

    @Test
    void version() throws IOException {
        PMR.Version msg = new PMR.Version();
        msg.version = 2;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof PMR.Version);
        assertEquals(2, ((PMR.Version) events.get(0)).version);
    }

    @Test
    void orderEntered() throws IOException {
        PMR.OrderEntered msg = new PMR.OrderEntered();
        msg.timestamp   = 1000L;
        msg.username    = 2000L;
        msg.orderNumber = 3000L;
        msg.side        = PMR.BUY;
        msg.instrument  = 4000L;
        msg.quantity    = 100L;
        msg.price       = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof PMR.OrderEntered);

        PMR.OrderEntered parsed = (PMR.OrderEntered) events.get(0);
        assertEquals(1000L, parsed.timestamp);
        assertEquals(2000L, parsed.username);
        assertEquals(3000L, parsed.orderNumber);
        assertEquals(PMR.BUY, parsed.side);
        assertEquals(4000L, parsed.instrument);
        assertEquals(100L, parsed.quantity);
        assertEquals(5000L, parsed.price);
    }

    @Test
    void orderAdded() throws IOException {
        PMR.OrderAdded msg = new PMR.OrderAdded();
        msg.timestamp   = 1000L;
        msg.orderNumber = 2000L;

        ByteBuffer buffer = ByteBuffer.allocate(17);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof PMR.OrderAdded);

        PMR.OrderAdded parsed = (PMR.OrderAdded) events.get(0);
        assertEquals(1000L, parsed.timestamp);
        assertEquals(2000L, parsed.orderNumber);
    }

    @Test
    void orderCanceled() throws IOException {
        PMR.OrderCanceled msg = new PMR.OrderCanceled();
        msg.timestamp        = 1000L;
        msg.orderNumber      = 2000L;
        msg.canceledQuantity = 50L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof PMR.OrderCanceled);

        PMR.OrderCanceled parsed = (PMR.OrderCanceled) events.get(0);
        assertEquals(1000L, parsed.timestamp);
        assertEquals(2000L, parsed.orderNumber);
        assertEquals(50L, parsed.canceledQuantity);
    }

    @Test
    void trade() throws IOException {
        PMR.Trade msg = new PMR.Trade();
        msg.timestamp           = 1000L;
        msg.restingOrderNumber  = 2000L;
        msg.incomingOrderNumber = 3000L;
        msg.quantity            = 100L;
        msg.matchNumber         = 1L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof PMR.Trade);

        PMR.Trade parsed = (PMR.Trade) events.get(0);
        assertEquals(1000L, parsed.timestamp);
        assertEquals(2000L, parsed.restingOrderNumber);
        assertEquals(3000L, parsed.incomingOrderNumber);
        assertEquals(100L, parsed.quantity);
        assertEquals(1L, parsed.matchNumber);
    }
}
