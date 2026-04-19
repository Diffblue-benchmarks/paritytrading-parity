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

import static com.paritytrading.parity.net.pmr.PMR.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PMRParserTest {

    private List<Object> events;
    private PMRParser parser;

    @BeforeEach
    void setUp() {
        events = new ArrayList<>();

        PMRListener listener = new PMRListener() {
            @Override
            public void version(Version message) {
                events.add(message.version);
            }

            @Override
            public void orderEntered(OrderEntered message) {
                events.add("orderEntered");
                events.add(message.timestamp);
                events.add(message.username);
                events.add(message.orderNumber);
                events.add(message.side);
                events.add(message.instrument);
                events.add(message.quantity);
                events.add(message.price);
            }

            @Override
            public void orderAdded(OrderAdded message) {
                events.add("orderAdded");
                events.add(message.timestamp);
                events.add(message.orderNumber);
            }

            @Override
            public void orderCanceled(OrderCanceled message) {
                events.add("orderCanceled");
                events.add(message.timestamp);
                events.add(message.orderNumber);
                events.add(message.canceledQuantity);
            }

            @Override
            public void trade(Trade message) {
                events.add("trade");
                events.add(message.timestamp);
                events.add(message.restingOrderNumber);
                events.add(message.incomingOrderNumber);
                events.add(message.quantity);
                events.add(message.matchNumber);
            }
        };

        parser = new PMRParser(listener);
    }

    @Test
    void versionMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(2);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.size());
        assertEquals(2L, events.get(0));
    }

    @Test
    void orderEnteredMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.put((byte) 'E');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.put(BUY);
        buffer.putLong(4000L);
        buffer.putLong(500L);
        buffer.putLong(1050L);
        buffer.flip();

        parser.message(buffer);

        assertEquals("orderEntered", events.get(0));
        assertEquals(1000L, events.get(1));
        assertEquals(2000L, events.get(2));
        assertEquals(3000L, events.get(3));
        assertEquals(BUY, events.get(4));
        assertEquals(4000L, events.get(5));
        assertEquals(500L, events.get(6));
        assertEquals(1050L, events.get(7));
    }

    @Test
    void orderAddedMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(17);
        buffer.put((byte) 'A');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.flip();

        parser.message(buffer);

        assertEquals("orderAdded", events.get(0));
        assertEquals(1000L, events.get(1));
        assertEquals(2000L, events.get(2));
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

    @Test
    void tradeMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(37);
        buffer.put((byte) 'T');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(400L);
        buffer.putInt(99);
        buffer.flip();

        parser.message(buffer);

        assertEquals("trade", events.get(0));
        assertEquals(1000L, events.get(1));
        assertEquals(2000L, events.get(2));
        assertEquals(3000L, events.get(3));
        assertEquals(400L, events.get(4));
        assertEquals(99L, events.get(5));
    }

    @Test
    void emptyBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(0);

        PMRException exception = assertThrows(PMRException.class, () -> parser.message(buffer));

        assertTrue(exception.getMessage().contains("no message type"));
    }

    @Test
    void unknownMessageType() {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 'Z');
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> parser.message(buffer));

        assertTrue(exception.getMessage().contains("Unknown message type"));
    }

    @Test
    void malformedVersionMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte) 'V');
        buffer.putShort((short) 1);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> parser.message(buffer));

        assertTrue(exception.getMessage().contains("Malformed message"));
    }

    @Test
    void malformedOrderEnteredMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'E');
        buffer.putLong(1000L);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> parser.message(buffer));

        assertTrue(exception.getMessage().contains("Malformed message"));
    }

    @Test
    void malformedOrderAddedMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'A');
        buffer.putInt(1);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> parser.message(buffer));

        assertTrue(exception.getMessage().contains("Malformed message"));
    }

    @Test
    void malformedOrderCanceledMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'X');
        buffer.putInt(1);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> parser.message(buffer));

        assertTrue(exception.getMessage().contains("Malformed message"));
    }

    @Test
    void malformedTradeMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'T');
        buffer.putInt(1);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> parser.message(buffer));

        assertTrue(exception.getMessage().contains("Malformed message"));
    }
}
