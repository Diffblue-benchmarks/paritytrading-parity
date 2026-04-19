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

class POEClientParserTest {

    private POEClientParser parser;
    private EventCollector  events;

    @BeforeEach
    void setUp() {
        events = new EventCollector();
        parser = new POEClientParser(events);
    }

    @Test
    void orderAccepted() throws IOException {
        POE.OrderAccepted msg = new POE.OrderAccepted();
        msg.timestamp   = 100L;
        msg.side        = POE.BUY;
        msg.instrument  = 200L;
        msg.quantity    = 300L;
        msg.price       = 400L;
        msg.orderNumber = 500L;
        fillOrderId(msg.orderId, (byte) 'A');

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_OUTBOUND_MESSAGE_LENGTH);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.orderAccepted.size());
        POE.OrderAccepted received = events.orderAccepted.get(0);
        assertEquals(100L, received.timestamp);
        assertEquals(POE.BUY, received.side);
        assertEquals(200L, received.instrument);
        assertEquals(300L, received.quantity);
        assertEquals(400L, received.price);
        assertEquals(500L, received.orderNumber);
    }

    @Test
    void orderRejected() throws IOException {
        POE.OrderRejected msg = new POE.OrderRejected();
        msg.timestamp = 101L;
        msg.reason    = POE.ORDER_REJECT_REASON_INVALID_PRICE;
        fillOrderId(msg.orderId, (byte) 'B');

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_OUTBOUND_MESSAGE_LENGTH);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.orderRejected.size());
        POE.OrderRejected received = events.orderRejected.get(0);
        assertEquals(101L, received.timestamp);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, received.reason);
    }

    @Test
    void orderExecuted() throws IOException {
        POE.OrderExecuted msg = new POE.OrderExecuted();
        msg.timestamp     = 102L;
        msg.quantity      = 50L;
        msg.price         = 999L;
        msg.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        msg.matchNumber   = 12345L;
        fillOrderId(msg.orderId, (byte) 'C');

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_OUTBOUND_MESSAGE_LENGTH);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.orderExecuted.size());
        POE.OrderExecuted received = events.orderExecuted.get(0);
        assertEquals(102L, received.timestamp);
        assertEquals(50L, received.quantity);
        assertEquals(999L, received.price);
        assertEquals(POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, received.liquidityFlag);
        assertEquals(12345L, received.matchNumber);
    }

    @Test
    void orderCanceled() throws IOException {
        POE.OrderCanceled msg = new POE.OrderCanceled();
        msg.timestamp        = 103L;
        msg.canceledQuantity = 75L;
        msg.reason           = POE.ORDER_CANCEL_REASON_REQUEST;
        fillOrderId(msg.orderId, (byte) 'D');

        ByteBuffer buffer = ByteBuffer.allocate(POE.MAX_OUTBOUND_MESSAGE_LENGTH);
        msg.put(buffer);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.orderCanceled.size());
        POE.OrderCanceled received = events.orderCanceled.get(0);
        assertEquals(103L, received.timestamp);
        assertEquals(75L, received.canceledQuantity);
        assertEquals(POE.ORDER_CANCEL_REASON_REQUEST, received.reason);
    }

    @Test
    void emptyBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(0);

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    @Test
    void unknownMessageType() {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 'Z');
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> parser.message(buffer));
        assertTrue(exception.getMessage().contains("Unknown message type"));
    }

    @Test
    void malformedOrderAccepted() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'A');
        buffer.put((byte) 0);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    @Test
    void malformedOrderRejected() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'R');
        buffer.put((byte) 0);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    @Test
    void malformedOrderExecuted() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'E');
        buffer.put((byte) 0);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    @Test
    void malformedOrderCanceled() {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'X');
        buffer.put((byte) 0);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    private static void fillOrderId(byte[] orderId, byte value) {
        for (int i = 0; i < orderId.length; i++)
            orderId[i] = value;
    }

    private static class EventCollector implements POEClientListener {

        List<POE.OrderAccepted> orderAccepted = new ArrayList<>();
        List<POE.OrderRejected> orderRejected = new ArrayList<>();
        List<POE.OrderExecuted> orderExecuted = new ArrayList<>();
        List<POE.OrderCanceled> orderCanceled = new ArrayList<>();

        @Override
        public void orderAccepted(POE.OrderAccepted message) {
            POE.OrderAccepted copy = new POE.OrderAccepted();
            copy.timestamp   = message.timestamp;
            copy.side        = message.side;
            copy.instrument  = message.instrument;
            copy.quantity    = message.quantity;
            copy.price       = message.price;
            copy.orderNumber = message.orderNumber;
            System.arraycopy(message.orderId, 0, copy.orderId, 0, message.orderId.length);
            orderAccepted.add(copy);
        }

        @Override
        public void orderRejected(POE.OrderRejected message) {
            POE.OrderRejected copy = new POE.OrderRejected();
            copy.timestamp = message.timestamp;
            copy.reason    = message.reason;
            System.arraycopy(message.orderId, 0, copy.orderId, 0, message.orderId.length);
            orderRejected.add(copy);
        }

        @Override
        public void orderExecuted(POE.OrderExecuted message) {
            POE.OrderExecuted copy = new POE.OrderExecuted();
            copy.timestamp     = message.timestamp;
            copy.quantity      = message.quantity;
            copy.price         = message.price;
            copy.liquidityFlag = message.liquidityFlag;
            copy.matchNumber   = message.matchNumber;
            System.arraycopy(message.orderId, 0, copy.orderId, 0, message.orderId.length);
            orderExecuted.add(copy);
        }

        @Override
        public void orderCanceled(POE.OrderCanceled message) {
            POE.OrderCanceled copy = new POE.OrderCanceled();
            copy.timestamp        = message.timestamp;
            copy.canceledQuantity = message.canceledQuantity;
            copy.reason           = message.reason;
            System.arraycopy(message.orderId, 0, copy.orderId, 0, message.orderId.length);
            orderCanceled.add(copy);
        }
    }
}
