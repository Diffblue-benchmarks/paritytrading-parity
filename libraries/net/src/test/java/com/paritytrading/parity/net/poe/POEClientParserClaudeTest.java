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
import org.junit.jupiter.api.Test;

class POEClientParserClaudeTest {

    private static class TestPOEClientListener implements POEClientListener {
        List<String> messages = new ArrayList<>();
        POE.OrderAccepted lastOrderAccepted;
        POE.OrderRejected lastOrderRejected;
        POE.OrderExecuted lastOrderExecuted;
        POE.OrderCanceled lastOrderCanceled;
        IOException toThrow;

        @Override
        public void orderAccepted(POE.OrderAccepted message) throws IOException {
            messages.add("orderAccepted");
            lastOrderAccepted = message;
            if (toThrow != null) throw toThrow;
        }

        @Override
        public void orderRejected(POE.OrderRejected message) throws IOException {
            messages.add("orderRejected");
            lastOrderRejected = message;
            if (toThrow != null) throw toThrow;
        }

        @Override
        public void orderExecuted(POE.OrderExecuted message) throws IOException {
            messages.add("orderExecuted");
            lastOrderExecuted = message;
            if (toThrow != null) throw toThrow;
        }

        @Override
        public void orderCanceled(POE.OrderCanceled message) throws IOException {
            messages.add("orderCanceled");
            lastOrderCanceled = message;
            if (toThrow != null) throw toThrow;
        }
    }

    @Test
    void constructorCreatesParserWithListener() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        assertNotNull(parser);
    }

    @Test
    void messageWithOrderAcceptedMessageType() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(58);
        buffer.put((byte) 'A');           // message type
        buffer.putLong(1000L);            // timestamp
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) ('A' + i);
        }
        buffer.put(orderId);              // orderId
        buffer.put((byte) 'B');           // side
        buffer.putLong(12345L);           // instrument
        buffer.putLong(100L);             // quantity
        buffer.putLong(5000L);            // price
        buffer.putLong(99999L);           // orderNumber
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderAccepted", listener.messages.get(0));
        assertNotNull(listener.lastOrderAccepted);
        assertEquals(1000L, listener.lastOrderAccepted.timestamp);
        assertArrayEquals(orderId, listener.lastOrderAccepted.orderId);
        assertEquals((byte) 'B', listener.lastOrderAccepted.side);
        assertEquals(12345L, listener.lastOrderAccepted.instrument);
        assertEquals(100L, listener.lastOrderAccepted.quantity);
        assertEquals(5000L, listener.lastOrderAccepted.price);
        assertEquals(99999L, listener.lastOrderAccepted.orderNumber);
    }

    @Test
    void messageWithOrderRejectedMessageType() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.put((byte) 'R');           // message type
        buffer.putLong(2000L);            // timestamp
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) ('X' + i % 10);
        }
        buffer.put(orderId);              // orderId
        buffer.put((byte) 'I');           // reason
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderRejected", listener.messages.get(0));
        assertNotNull(listener.lastOrderRejected);
        assertEquals(2000L, listener.lastOrderRejected.timestamp);
        assertArrayEquals(orderId, listener.lastOrderRejected.orderId);
        assertEquals((byte) 'I', listener.lastOrderRejected.reason);
    }

    @Test
    void messageWithOrderExecutedMessageType() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(46);
        buffer.put((byte) 'E');           // message type
        buffer.putLong(3000L);            // timestamp
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) ('Z' - i);
        }
        buffer.put(orderId);              // orderId
        buffer.putLong(50L);              // quantity
        buffer.putLong(4500L);            // price
        buffer.put((byte) 'A');           // liquidityFlag
        buffer.putInt(777);               // matchNumber (unsigned int)
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderExecuted", listener.messages.get(0));
        assertNotNull(listener.lastOrderExecuted);
        assertEquals(3000L, listener.lastOrderExecuted.timestamp);
        assertArrayEquals(orderId, listener.lastOrderExecuted.orderId);
        assertEquals(50L, listener.lastOrderExecuted.quantity);
        assertEquals(4500L, listener.lastOrderExecuted.price);
        assertEquals((byte) 'A', listener.lastOrderExecuted.liquidityFlag);
        assertEquals(777L, listener.lastOrderExecuted.matchNumber);
    }

    @Test
    void messageWithOrderCanceledMessageType() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(34);
        buffer.put((byte) 'X');           // message type
        buffer.putLong(4000L);            // timestamp
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) i;
        }
        buffer.put(orderId);              // orderId
        buffer.putLong(25L);              // canceledQuantity
        buffer.put((byte) 'R');           // reason
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderCanceled", listener.messages.get(0));
        assertNotNull(listener.lastOrderCanceled);
        assertEquals(4000L, listener.lastOrderCanceled.timestamp);
        assertArrayEquals(orderId, listener.lastOrderCanceled.orderId);
        assertEquals(25L, listener.lastOrderCanceled.canceledQuantity);
        assertEquals((byte) 'R', listener.lastOrderCanceled.reason);
    }

    @Test
    void messageWithEmptyBufferThrowsException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(0);
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: no message type"));
    }

    @Test
    void messageWithUnknownMessageTypeThrowsException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'Z');           // unknown message type
        buffer.putLong(1000L);
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Unknown message type: Z"));
    }

    @Test
    void messageWithOrderAcceptedButInsufficientDataThrowsException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.put((byte) 'A');
        for (int i = 0; i < 56; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: A"));
    }

    @Test
    void messageWithOrderRejectedButInsufficientDataThrowsException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'R');
        for (int i = 0; i < 24; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: R"));
    }

    @Test
    void messageWithOrderExecutedButInsufficientDataThrowsException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.put((byte) 'E');
        for (int i = 0; i < 44; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: E"));
    }

    @Test
    void messageWithOrderCanceledButInsufficientDataThrowsException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.put((byte) 'X');
        for (int i = 0; i < 32; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: X"));
    }

    @Test
    void messageReusesSameMessageObjectsForMultipleCalls() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer1 = ByteBuffer.allocate(26);
        buffer1.put((byte) 'R');
        buffer1.putLong(1000L);
        buffer1.put(new byte[16]);
        buffer1.put((byte) 'P');
        buffer1.flip();

        parser.message(buffer1);
        POE.OrderRejected first = listener.lastOrderRejected;

        ByteBuffer buffer2 = ByteBuffer.allocate(26);
        buffer2.put((byte) 'R');
        buffer2.putLong(2000L);
        buffer2.put(new byte[16]);
        buffer2.put((byte) 'Q');
        buffer2.flip();

        parser.message(buffer2);
        POE.OrderRejected second = listener.lastOrderRejected;

        assertSame(first, second);
        assertEquals(2000L, second.timestamp);
        assertEquals((byte) 'Q', second.reason);
    }

    @Test
    void messageProcessesMixedMessageTypes() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer1 = ByteBuffer.allocate(26);
        buffer1.put((byte) 'R');
        buffer1.putLong(1000L);
        buffer1.put(new byte[16]);
        buffer1.put((byte) 'I');
        buffer1.flip();
        parser.message(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(34);
        buffer2.put((byte) 'X');
        buffer2.putLong(2000L);
        buffer2.put(new byte[16]);
        buffer2.putLong(10L);
        buffer2.put((byte) 'S');
        buffer2.flip();
        parser.message(buffer2);

        ByteBuffer buffer3 = ByteBuffer.allocate(58);
        buffer3.put((byte) 'A');
        buffer3.putLong(3000L);
        buffer3.put(new byte[16]);
        buffer3.put((byte) 'S');
        buffer3.putLong(999L);
        buffer3.putLong(50L);
        buffer3.putLong(1000L);
        buffer3.putLong(12345L);
        buffer3.flip();
        parser.message(buffer3);

        assertEquals(3, listener.messages.size());
        assertEquals("orderRejected", listener.messages.get(0));
        assertEquals("orderCanceled", listener.messages.get(1));
        assertEquals("orderAccepted", listener.messages.get(2));
    }

    @Test
    void messageWithListenerThrowingIOException() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        listener.toThrow = new IOException("Test exception");
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.put((byte) 'R');
        buffer.putLong(1000L);
        buffer.put(new byte[16]);
        buffer.put((byte) 'I');
        buffer.flip();

        IOException exception = assertThrows(IOException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    void messageWithExactMinimumLengthForOrderAccepted() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(58);
        buffer.put((byte) 'A');
        buffer.putLong(5000L);
        buffer.put(new byte[16]);
        buffer.put((byte) 'B');
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        buffer.putLong(444L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderAccepted", listener.messages.get(0));
        assertEquals(5000L, listener.lastOrderAccepted.timestamp);
    }

    @Test
    void messageWithExactMinimumLengthForOrderRejected() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.put((byte) 'R');
        buffer.putLong(6000L);
        buffer.put(new byte[16]);
        buffer.put((byte) 'P');
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderRejected", listener.messages.get(0));
        assertEquals(6000L, listener.lastOrderRejected.timestamp);
    }

    @Test
    void messageWithExactMinimumLengthForOrderExecuted() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(46);
        buffer.put((byte) 'E');
        buffer.putLong(7000L);
        buffer.put(new byte[16]);
        buffer.putLong(11L);
        buffer.putLong(22L);
        buffer.put((byte) 'R');
        buffer.putInt(33);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderExecuted", listener.messages.get(0));
        assertEquals(7000L, listener.lastOrderExecuted.timestamp);
    }

    @Test
    void messageWithExactMinimumLengthForOrderCanceled() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(34);
        buffer.put((byte) 'X');
        buffer.putLong(8000L);
        buffer.put(new byte[16]);
        buffer.putLong(99L);
        buffer.put((byte) 'R');
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderCanceled", listener.messages.get(0));
        assertEquals(8000L, listener.lastOrderCanceled.timestamp);
    }

    @Test
    void messageWithSingleByteBufferThrowsException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 'A');
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: A"));
    }

    @Test
    void messageWithOrderAcceptedExtraDataIsIgnored() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(70);
        buffer.put((byte) 'A');
        buffer.putLong(9000L);
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) (65 + i);
        }
        buffer.put(orderId);
        buffer.put((byte) 'B');
        buffer.putLong(555L);
        buffer.putLong(666L);
        buffer.putLong(777L);
        buffer.putLong(888L);
        for (int i = 0; i < 12; i++) {
            buffer.put((byte) 0xFF);
        }
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderAccepted", listener.messages.get(0));
        assertEquals(9000L, listener.lastOrderAccepted.timestamp);
        assertArrayEquals(orderId, listener.lastOrderAccepted.orderId);
    }

    @Test
    void messageWithAllMessageTypesInSequence() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer1 = ByteBuffer.allocate(58);
        buffer1.put((byte) 'A');
        buffer1.putLong(1L);
        buffer1.put(new byte[16]);
        buffer1.put((byte) 'B');
        buffer1.putLong(1L);
        buffer1.putLong(1L);
        buffer1.putLong(1L);
        buffer1.putLong(1L);
        buffer1.flip();
        parser.message(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(26);
        buffer2.put((byte) 'R');
        buffer2.putLong(2L);
        buffer2.put(new byte[16]);
        buffer2.put((byte) 'I');
        buffer2.flip();
        parser.message(buffer2);

        ByteBuffer buffer3 = ByteBuffer.allocate(46);
        buffer3.put((byte) 'E');
        buffer3.putLong(3L);
        buffer3.put(new byte[16]);
        buffer3.putLong(3L);
        buffer3.putLong(3L);
        buffer3.put((byte) 'A');
        buffer3.putInt(3);
        buffer3.flip();
        parser.message(buffer3);

        ByteBuffer buffer4 = ByteBuffer.allocate(34);
        buffer4.put((byte) 'X');
        buffer4.putLong(4L);
        buffer4.put(new byte[16]);
        buffer4.putLong(4L);
        buffer4.put((byte) 'R');
        buffer4.flip();
        parser.message(buffer4);

        assertEquals(4, listener.messages.size());
        assertEquals("orderAccepted", listener.messages.get(0));
        assertEquals("orderRejected", listener.messages.get(1));
        assertEquals("orderExecuted", listener.messages.get(2));
        assertEquals("orderCanceled", listener.messages.get(3));
    }
}
