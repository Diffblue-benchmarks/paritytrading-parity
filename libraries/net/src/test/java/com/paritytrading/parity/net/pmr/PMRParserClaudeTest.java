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

class PMRParserClaudeTest {

    private TestPMRListener listener;
    private PMRParser parser;

    @BeforeEach
    void setUp() {
        listener = new TestPMRListener();
        parser = new PMRParser(listener);
    }

    @Test
    void testConstructorInitializesParser() {
        PMRListener testListener = new TestPMRListener();
        PMRParser testParser = new PMRParser(testListener);

        assertNotNull(testParser);
    }

    @Test
    void testMessageWithEmptyBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(0);

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: no message type", exception.getMessage());
    }

    @Test
    void testMessageWithUnknownMessageType() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'Z');
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Unknown message type: Z", exception.getMessage());
    }

    @Test
    void testMessageWithVersionMessageTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put(MESSAGE_TYPE_VERSION);
        buffer.putShort((short) 1);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    void testMessageWithValidVersionMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION);
        buffer.put(MESSAGE_TYPE_VERSION);
        buffer.putInt(2);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.versionMessages.size());
        assertEquals(2L, listener.versionMessages.get(0).version);
    }

    @Test
    void testMessageWithOrderEnteredMessageTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.put(MESSAGE_TYPE_ORDER_ENTERED);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    void testMessageWithValidOrderEnteredMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ENTERED);
        buffer.put(MESSAGE_TYPE_ORDER_ENTERED);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.put(BUY);
        buffer.putLong(4000L);
        buffer.putLong(5000L);
        buffer.putLong(6000L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderEnteredMessages.size());
        PMR.OrderEntered msg = listener.orderEnteredMessages.get(0);
        assertEquals(1000L, msg.timestamp);
        assertEquals(2000L, msg.username);
        assertEquals(3000L, msg.orderNumber);
        assertEquals(BUY, msg.side);
        assertEquals(4000L, msg.instrument);
        assertEquals(5000L, msg.quantity);
        assertEquals(6000L, msg.price);
    }

    @Test
    void testMessageWithOrderAddedMessageTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(MESSAGE_TYPE_ORDER_ADDED);
        buffer.putLong(1000L);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    void testMessageWithValidOrderAddedMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ADDED);
        buffer.put(MESSAGE_TYPE_ORDER_ADDED);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderAddedMessages.size());
        PMR.OrderAdded msg = listener.orderAddedMessages.get(0);
        assertEquals(1000L, msg.timestamp);
        assertEquals(2000L, msg.orderNumber);
    }

    @Test
    void testMessageWithOrderCanceledMessageTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(15);
        buffer.put(MESSAGE_TYPE_ORDER_CANCELED);
        buffer.putLong(1000L);
        buffer.putInt(2000);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    void testMessageWithValidOrderCanceledMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_CANCELED);
        buffer.put(MESSAGE_TYPE_ORDER_CANCELED);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderCanceledMessages.size());
        PMR.OrderCanceled msg = listener.orderCanceledMessages.get(0);
        assertEquals(1000L, msg.timestamp);
        assertEquals(2000L, msg.orderNumber);
        assertEquals(3000L, msg.canceledQuantity);
    }

    @Test
    void testMessageWithTradeMessageTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(30);
        buffer.put(MESSAGE_TYPE_TRADE);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: T", exception.getMessage());
    }

    @Test
    void testMessageWithValidTradeMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_TRADE);
        buffer.put(MESSAGE_TYPE_TRADE);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putInt(5000);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.tradeMessages.size());
        PMR.Trade msg = listener.tradeMessages.get(0);
        assertEquals(1000L, msg.timestamp);
        assertEquals(2000L, msg.restingOrderNumber);
        assertEquals(3000L, msg.incomingOrderNumber);
        assertEquals(4000L, msg.quantity);
        assertEquals(5000L, msg.matchNumber);
    }

    @Test
    void testMessageWithMultipleMessagesInSequence() throws IOException {
        ByteBuffer buffer1 = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION);
        buffer1.put(MESSAGE_TYPE_VERSION);
        buffer1.putInt(2);
        buffer1.flip();

        ByteBuffer buffer2 = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ADDED);
        buffer2.put(MESSAGE_TYPE_ORDER_ADDED);
        buffer2.putLong(1000L);
        buffer2.putLong(2000L);
        buffer2.flip();

        parser.message(buffer1);
        parser.message(buffer2);

        assertEquals(1, listener.versionMessages.size());
        assertEquals(1, listener.orderAddedMessages.size());
        assertEquals(2L, listener.versionMessages.get(0).version);
        assertEquals(1000L, listener.orderAddedMessages.get(0).timestamp);
    }

    @Test
    void testMessageWithExtraDataAfterValidMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION + 10);
        buffer.put(MESSAGE_TYPE_VERSION);
        buffer.putInt(2);
        buffer.putLong(9999L);
        buffer.putShort((short) 9999);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.versionMessages.size());
        assertEquals(2L, listener.versionMessages.get(0).version);
    }

    @Test
    void testMessageWithSellSideOrderEntered() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ENTERED);
        buffer.put(MESSAGE_TYPE_ORDER_ENTERED);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.put(SELL);
        buffer.putLong(4000L);
        buffer.putLong(5000L);
        buffer.putLong(6000L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderEnteredMessages.size());
        assertEquals(SELL, listener.orderEnteredMessages.get(0).side);
    }

    @Test
    void testMessageWithMaxLongValues() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_TRADE);
        buffer.put(MESSAGE_TYPE_TRADE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putInt(-1);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.tradeMessages.size());
        PMR.Trade msg = listener.tradeMessages.get(0);
        assertEquals(Long.MAX_VALUE, msg.timestamp);
        assertEquals(0xFFFFFFFFL, msg.matchNumber);
    }

    @Test
    void testMessageWithMinLongValues() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_CANCELED);
        buffer.put(MESSAGE_TYPE_ORDER_CANCELED);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.putLong(Long.MIN_VALUE);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderCanceledMessages.size());
        PMR.OrderCanceled msg = listener.orderCanceledMessages.get(0);
        assertEquals(Long.MIN_VALUE, msg.timestamp);
        assertEquals(Long.MIN_VALUE, msg.orderNumber);
        assertEquals(Long.MIN_VALUE, msg.canceledQuantity);
    }

    @Test
    void testMessageWithZeroValues() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ADDED);
        buffer.put(MESSAGE_TYPE_ORDER_ADDED);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderAddedMessages.size());
        PMR.OrderAdded msg = listener.orderAddedMessages.get(0);
        assertEquals(0L, msg.timestamp);
        assertEquals(0L, msg.orderNumber);
    }

    @Test
    void testMessageWithBufferAtExactMinimumSize() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION);
        buffer.put(MESSAGE_TYPE_VERSION);
        buffer.putInt(1);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.versionMessages.size());
    }

    @Test
    void testMessageWithInvalidMessageTypeValue() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 0);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().startsWith("Unknown message type:"));
    }

    @Test
    void testParserCanHandleListenerThatThrowsIOException() {
        PMRListener throwingListener = new PMRListener() {
            @Override
            public void version(Version message) throws IOException {
                throw new IOException("Test exception");
            }

            @Override
            public void orderEntered(OrderEntered message) throws IOException {
                throw new IOException("Test exception");
            }

            @Override
            public void orderAdded(OrderAdded message) throws IOException {
                throw new IOException("Test exception");
            }

            @Override
            public void orderCanceled(OrderCanceled message) throws IOException {
                throw new IOException("Test exception");
            }

            @Override
            public void trade(Trade message) throws IOException {
                throw new IOException("Test exception");
            }
        };

        PMRParser throwingParser = new PMRParser(throwingListener);

        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION);
        buffer.put(MESSAGE_TYPE_VERSION);
        buffer.putInt(2);
        buffer.flip();

        IOException exception = assertThrows(IOException.class, () -> {
            throwingParser.message(buffer);
        });

        assertEquals("Test exception", exception.getMessage());
    }

    private static class TestPMRListener implements PMRListener {
        List<PMR.Version> versionMessages = new ArrayList<>();
        List<PMR.OrderEntered> orderEnteredMessages = new ArrayList<>();
        List<PMR.OrderAdded> orderAddedMessages = new ArrayList<>();
        List<PMR.OrderCanceled> orderCanceledMessages = new ArrayList<>();
        List<PMR.Trade> tradeMessages = new ArrayList<>();

        @Override
        public void version(Version message) throws IOException {
            Version copy = new Version();
            copy.version = message.version;
            versionMessages.add(copy);
        }

        @Override
        public void orderEntered(OrderEntered message) throws IOException {
            OrderEntered copy = new OrderEntered();
            copy.timestamp = message.timestamp;
            copy.username = message.username;
            copy.orderNumber = message.orderNumber;
            copy.side = message.side;
            copy.instrument = message.instrument;
            copy.quantity = message.quantity;
            copy.price = message.price;
            orderEnteredMessages.add(copy);
        }

        @Override
        public void orderAdded(OrderAdded message) throws IOException {
            OrderAdded copy = new OrderAdded();
            copy.timestamp = message.timestamp;
            copy.orderNumber = message.orderNumber;
            orderAddedMessages.add(copy);
        }

        @Override
        public void orderCanceled(OrderCanceled message) throws IOException {
            OrderCanceled copy = new OrderCanceled();
            copy.timestamp = message.timestamp;
            copy.orderNumber = message.orderNumber;
            copy.canceledQuantity = message.canceledQuantity;
            orderCanceledMessages.add(copy);
        }

        @Override
        public void trade(Trade message) throws IOException {
            Trade copy = new Trade();
            copy.timestamp = message.timestamp;
            copy.restingOrderNumber = message.restingOrderNumber;
            copy.incomingOrderNumber = message.incomingOrderNumber;
            copy.quantity = message.quantity;
            copy.matchNumber = message.matchNumber;
            tradeMessages.add(copy);
        }
    }
}
