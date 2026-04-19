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
package com.paritytrading.parity.net.pmd;

import static com.paritytrading.parity.net.pmd.PMD.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PMDParserTest {

    private List<Object> events;
    private PMDParser parser;

    @BeforeEach
    void setUp() {
        events = new ArrayList<>();

        PMDListener listener = new PMDListener() {
            @Override
            public void version(Version message) {
                events.add("version");
                events.add(message.version);
            }

            @Override
            public void orderAdded(OrderAdded message) {
                events.add("orderAdded");
                events.add(message.timestamp);
                events.add(message.orderNumber);
                events.add(message.side);
                events.add(message.instrument);
                events.add(message.quantity);
                events.add(message.price);
            }

            @Override
            public void orderExecuted(OrderExecuted message) {
                events.add("orderExecuted");
                events.add(message.timestamp);
                events.add(message.orderNumber);
                events.add(message.quantity);
                events.add(message.matchNumber);
            }

            @Override
            public void orderCanceled(OrderCanceled message) {
                events.add("orderCanceled");
                events.add(message.timestamp);
                events.add(message.orderNumber);
                events.add(message.canceledQuantity);
            }
        };

        parser = new PMDParser(listener);
    }

    @Test
    void versionMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(2);
        buffer.flip();

        parser.message(buffer);

        assertEquals("version", events.get(0));
        assertEquals(2L, events.get(1));
    }

    @Test
    void orderAddedMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'A');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.put(BUY);
        buffer.putLong(3000L);
        buffer.putLong(500L);
        buffer.putLong(1050L);
        buffer.flip();

        parser.message(buffer);

        assertEquals("orderAdded", events.get(0));
        assertEquals(1000L, events.get(1));
        assertEquals(2000L, events.get(2));
        assertEquals(BUY, events.get(3));
        assertEquals(3000L, events.get(4));
        assertEquals(500L, events.get(5));
        assertEquals(1050L, events.get(6));
    }

    @Test
    void orderExecutedMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(29);
        buffer.put((byte) 'E');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(400L);
        buffer.putInt(99);
        buffer.flip();

        parser.message(buffer);

        assertEquals("orderExecuted", events.get(0));
        assertEquals(1000L, events.get(1));
        assertEquals(2000L, events.get(2));
        assertEquals(400L, events.get(3));
        assertEquals(99L, events.get(4));
    }

    @Test
    void orderCanceledMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(300L);
        buffer.flip();

        parser.message(buffer);

        assertEquals("orderCanceled", events.get(0));
        assertEquals(1000L, events.get(1));
        assertEquals(2000L, events.get(2));
        assertEquals(300L, events.get(3));
    }
}
