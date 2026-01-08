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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PMRParserClaude_messageTest {

    private TestPMRListener listener;
    private PMRParser parser;

    @BeforeEach
    void setUp() {
        listener = new TestPMRListener();
        parser = new PMRParser(listener);
    }

    @Test
    void testMessageWithVersionMessageTooShortCallsMalformedMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION - 1);
        buffer.put(MESSAGE_TYPE_VERSION);
        for (int i = 1; i < MESSAGE_LENGTH_VERSION - 1; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    void testMessageWithOrderEnteredMessageTooShortCallsMalformedMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ENTERED - 1);
        buffer.put(MESSAGE_TYPE_ORDER_ENTERED);
        for (int i = 1; i < MESSAGE_LENGTH_ORDER_ENTERED - 1; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    void testMessageWithOrderAddedMessageTooShortCallsMalformedMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ADDED - 1);
        buffer.put(MESSAGE_TYPE_ORDER_ADDED);
        for (int i = 1; i < MESSAGE_LENGTH_ORDER_ADDED - 1; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    void testMessageWithOrderCanceledMessageTooShortCallsMalformedMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_CANCELED - 1);
        buffer.put(MESSAGE_TYPE_ORDER_CANCELED);
        for (int i = 1; i < MESSAGE_LENGTH_ORDER_CANCELED - 1; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    void testMessageWithTradeMessageTooShortCallsMalformedMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_TRADE - 1);
        buffer.put(MESSAGE_TYPE_TRADE);
        for (int i = 1; i < MESSAGE_LENGTH_TRADE - 1; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: T", exception.getMessage());
    }

    @Test
    void testMessageWithValidVersionMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION);
        buffer.put(MESSAGE_TYPE_VERSION);
        buffer.putInt(2);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.versionCount);
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

        assertEquals(1, listener.orderEnteredCount);
    }

    @Test
    void testMessageWithValidOrderAddedMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ADDED);
        buffer.put(MESSAGE_TYPE_ORDER_ADDED);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderAddedCount);
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

        assertEquals(1, listener.orderCanceledCount);
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

        assertEquals(1, listener.tradeCount);
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
    void testMessageWithVersionMessageExactlyMinimumLength() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION);
        buffer.put(MESSAGE_TYPE_VERSION);
        buffer.putInt(1);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.versionCount);
    }

    @Test
    void testMessageWithOrderEnteredMessageExactlyMinimumLength() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ENTERED);
        buffer.put(MESSAGE_TYPE_ORDER_ENTERED);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderEnteredCount);
    }

    @Test
    void testMessageWithOrderAddedMessageExactlyMinimumLength() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ADDED);
        buffer.put(MESSAGE_TYPE_ORDER_ADDED);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderAddedCount);
    }

    @Test
    void testMessageWithOrderCanceledMessageExactlyMinimumLength() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_CANCELED);
        buffer.put(MESSAGE_TYPE_ORDER_CANCELED);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.orderCanceledCount);
    }

    @Test
    void testMessageWithTradeMessageExactlyMinimumLength() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_TRADE);
        buffer.put(MESSAGE_TYPE_TRADE);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putInt(0);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.tradeCount);
    }

    @Test
    void testMessageWithVersionMessageOneByteTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_VERSION - 1);
        buffer.put(MESSAGE_TYPE_VERSION);
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    void testMessageWithOrderEnteredMessageOneByteTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ENTERED - 1);
        buffer.put(MESSAGE_TYPE_ORDER_ENTERED);
        for (int i = 1; i < MESSAGE_LENGTH_ORDER_ENTERED - 1; i++) {
            buffer.put((byte) i);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    void testMessageWithOrderAddedMessageOneByteTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_ADDED - 1);
        buffer.put(MESSAGE_TYPE_ORDER_ADDED);
        for (int i = 1; i < MESSAGE_LENGTH_ORDER_ADDED - 1; i++) {
            buffer.put((byte) i);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    void testMessageWithOrderCanceledMessageOneByteTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_ORDER_CANCELED - 1);
        buffer.put(MESSAGE_TYPE_ORDER_CANCELED);
        for (int i = 1; i < MESSAGE_LENGTH_ORDER_CANCELED - 1; i++) {
            buffer.put((byte) i);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    void testMessageWithTradeMessageOneByteTooShort() {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH_TRADE - 1);
        buffer.put(MESSAGE_TYPE_TRADE);
        for (int i = 1; i < MESSAGE_LENGTH_TRADE - 1; i++) {
            buffer.put((byte) i);
        }
        buffer.flip();

        PMRException exception = assertThrows(PMRException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: T", exception.getMessage());
    }

    private static class TestPMRListener implements PMRListener {
        int versionCount = 0;
        int orderEnteredCount = 0;
        int orderAddedCount = 0;
        int orderCanceledCount = 0;
        int tradeCount = 0;

        @Override
        public void version(Version message) throws IOException {
            versionCount++;
        }

        @Override
        public void orderEntered(OrderEntered message) throws IOException {
            orderEnteredCount++;
        }

        @Override
        public void orderAdded(OrderAdded message) throws IOException {
            orderAddedCount++;
        }

        @Override
        public void orderCanceled(OrderCanceled message) throws IOException {
            orderCanceledCount++;
        }

        @Override
        public void trade(Trade message) throws IOException {
            tradeCount++;
        }
    }
}
