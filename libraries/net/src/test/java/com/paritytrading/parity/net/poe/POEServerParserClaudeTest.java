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

class POEServerParserClaudeTest {

    private static class TestPOEServerListener implements POEServerListener {
        List<String> messages = new ArrayList<>();
        POE.EnterOrder lastEnterOrder;
        POE.CancelOrder lastCancelOrder;
        IOException toThrow;

        @Override
        public void enterOrder(POE.EnterOrder message) throws IOException {
            messages.add("enterOrder");
            lastEnterOrder = message;
            if (toThrow != null) throw toThrow;
        }

        @Override
        public void cancelOrder(POE.CancelOrder message) throws IOException {
            messages.add("cancelOrder");
            lastCancelOrder = message;
            if (toThrow != null) throw toThrow;
        }
    }

    @Test
    void constructorCreatesParserWithListener() {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        assertNotNull(parser);
    }

    @Test
    void messageWithEnterOrderMessageType() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) ('A' + i);
        }
        buffer.put(orderId);
        buffer.put((byte) 'B');
        buffer.putLong(12345L);
        buffer.putLong(100L);
        buffer.putLong(5000L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("enterOrder", listener.messages.get(0));
        assertNotNull(listener.lastEnterOrder);
        assertArrayEquals(orderId, listener.lastEnterOrder.orderId);
        assertEquals((byte) 'B', listener.lastEnterOrder.side);
        assertEquals(12345L, listener.lastEnterOrder.instrument);
        assertEquals(100L, listener.lastEnterOrder.quantity);
        assertEquals(5000L, listener.lastEnterOrder.price);
    }

    @Test
    void messageWithCancelOrderMessageType() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) ('X' + i % 10);
        }
        buffer.put(orderId);
        buffer.putLong(50L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("cancelOrder", listener.messages.get(0));
        assertNotNull(listener.lastCancelOrder);
        assertArrayEquals(orderId, listener.lastCancelOrder.orderId);
        assertEquals(50L, listener.lastCancelOrder.quantity);
    }

    @Test
    void messageWithEmptyBufferThrowsException() {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(0);
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: no message type"));
    }

    @Test
    void messageWithUnknownMessageTypeThrowsException() {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'Z');
        buffer.putLong(1000L);
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Unknown message type: Z"));
    }

    @Test
    void messageWithEnterOrderButInsufficientDataThrowsException() {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.put((byte) 'E');
        for (int i = 0; i < 40; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: E"));
    }

    @Test
    void messageWithCancelOrderButInsufficientDataThrowsException() {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.put((byte) 'X');
        for (int i = 0; i < 23; i++) {
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
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer1 = ByteBuffer.allocate(25);
        buffer1.put((byte) 'X');
        buffer1.put(new byte[16]);
        buffer1.putLong(10L);
        buffer1.flip();

        parser.message(buffer1);
        POE.CancelOrder first = listener.lastCancelOrder;

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        buffer2.put((byte) 'X');
        buffer2.put(new byte[16]);
        buffer2.putLong(20L);
        buffer2.flip();

        parser.message(buffer2);
        POE.CancelOrder second = listener.lastCancelOrder;

        assertSame(first, second);
        assertEquals(20L, second.quantity);
    }

    @Test
    void messageProcessesMixedMessageTypes() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer1 = ByteBuffer.allocate(42);
        buffer1.put((byte) 'E');
        buffer1.put(new byte[16]);
        buffer1.put((byte) 'B');
        buffer1.putLong(111L);
        buffer1.putLong(222L);
        buffer1.putLong(333L);
        buffer1.flip();
        parser.message(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        buffer2.put((byte) 'X');
        buffer2.put(new byte[16]);
        buffer2.putLong(444L);
        buffer2.flip();
        parser.message(buffer2);

        ByteBuffer buffer3 = ByteBuffer.allocate(42);
        buffer3.put((byte) 'E');
        buffer3.put(new byte[16]);
        buffer3.put((byte) 'S');
        buffer3.putLong(555L);
        buffer3.putLong(666L);
        buffer3.putLong(777L);
        buffer3.flip();
        parser.message(buffer3);

        assertEquals(3, listener.messages.size());
        assertEquals("enterOrder", listener.messages.get(0));
        assertEquals("cancelOrder", listener.messages.get(1));
        assertEquals("enterOrder", listener.messages.get(2));
    }

    @Test
    void messageWithListenerThrowingIOException() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        listener.toThrow = new IOException("Test exception");
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.put(new byte[16]);
        buffer.putLong(100L);
        buffer.flip();

        IOException exception = assertThrows(IOException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    void messageWithExactMinimumLengthForEnterOrder() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) i;
        }
        buffer.put(orderId);
        buffer.put((byte) 'S');
        buffer.putLong(999L);
        buffer.putLong(888L);
        buffer.putLong(777L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("enterOrder", listener.messages.get(0));
        assertArrayEquals(orderId, listener.lastEnterOrder.orderId);
        assertEquals((byte) 'S', listener.lastEnterOrder.side);
        assertEquals(999L, listener.lastEnterOrder.instrument);
        assertEquals(888L, listener.lastEnterOrder.quantity);
        assertEquals(777L, listener.lastEnterOrder.price);
    }

    @Test
    void messageWithExactMinimumLengthForCancelOrder() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) (i * 2);
        }
        buffer.put(orderId);
        buffer.putLong(12345L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("cancelOrder", listener.messages.get(0));
        assertArrayEquals(orderId, listener.lastCancelOrder.orderId);
        assertEquals(12345L, listener.lastCancelOrder.quantity);
    }

    @Test
    void messageWithSingleByteBufferThrowsException() {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 'E');
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Malformed message: E"));
    }

    @Test
    void messageWithEnterOrderExtraDataIsIgnored() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.put((byte) 'E');
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) (65 + i);
        }
        buffer.put(orderId);
        buffer.put((byte) 'B');
        buffer.putLong(111L);
        buffer.putLong(222L);
        buffer.putLong(333L);
        for (int i = 0; i < 8; i++) {
            buffer.put((byte) 0xFF);
        }
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("enterOrder", listener.messages.get(0));
        assertArrayEquals(orderId, listener.lastEnterOrder.orderId);
        assertEquals((byte) 'B', listener.lastEnterOrder.side);
        assertEquals(111L, listener.lastEnterOrder.instrument);
        assertEquals(222L, listener.lastEnterOrder.quantity);
        assertEquals(333L, listener.lastEnterOrder.price);
    }

    @Test
    void messageWithCancelOrderExtraDataIsIgnored() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(35);
        buffer.put((byte) 'X');
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) (i + 100);
        }
        buffer.put(orderId);
        buffer.putLong(9999L);
        for (int i = 0; i < 10; i++) {
            buffer.put((byte) 0xFF);
        }
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("cancelOrder", listener.messages.get(0));
        assertArrayEquals(orderId, listener.lastCancelOrder.orderId);
        assertEquals(9999L, listener.lastCancelOrder.quantity);
    }

    @Test
    void messageWithBuySideEnterOrder() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        buffer.put(new byte[16]);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.flip();

        parser.message(buffer);

        assertEquals((byte) 'B', listener.lastEnterOrder.side);
    }

    @Test
    void messageWithSellSideEnterOrder() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        buffer.put(new byte[16]);
        buffer.put((byte) 'S');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.flip();

        parser.message(buffer);

        assertEquals((byte) 'S', listener.lastEnterOrder.side);
    }

    @Test
    void messageWithZeroQuantityCancelOrder() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.put(new byte[16]);
        buffer.putLong(0L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(0L, listener.lastCancelOrder.quantity);
    }

    @Test
    void messageWithMaxLongValuesInEnterOrder() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        buffer.put(new byte[16]);
        buffer.put((byte) 'B');
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        parser.message(buffer);

        assertEquals(Long.MAX_VALUE, listener.lastEnterOrder.instrument);
        assertEquals(Long.MAX_VALUE, listener.lastEnterOrder.quantity);
        assertEquals(Long.MAX_VALUE, listener.lastEnterOrder.price);
    }

    @Test
    void messageWithNegativeValuesInEnterOrder() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        buffer.put(new byte[16]);
        buffer.put((byte) 'B');
        buffer.putLong(-1L);
        buffer.putLong(-2L);
        buffer.putLong(-3L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(-1L, listener.lastEnterOrder.instrument);
        assertEquals(-2L, listener.lastEnterOrder.quantity);
        assertEquals(-3L, listener.lastEnterOrder.price);
    }

    @Test
    void messageWithAllZeroesInOrderId() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        byte[] orderId = new byte[16];
        buffer.put(orderId);
        buffer.putLong(100L);
        buffer.flip();

        parser.message(buffer);

        assertArrayEquals(new byte[16], listener.lastCancelOrder.orderId);
    }

    @Test
    void messageWithAllOnesInOrderId() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        byte[] orderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            orderId[i] = (byte) 0xFF;
        }
        buffer.put(orderId);
        buffer.putLong(100L);
        buffer.flip();

        parser.message(buffer);

        assertArrayEquals(orderId, listener.lastCancelOrder.orderId);
    }

    @Test
    void messageWithAlternatingEnterAndCancelOrders() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        POEServerParser parser = new POEServerParser(listener);

        for (int i = 0; i < 5; i++) {
            ByteBuffer buffer1 = ByteBuffer.allocate(42);
            buffer1.put((byte) 'E');
            buffer1.put(new byte[16]);
            buffer1.put((byte) 'B');
            buffer1.putLong(i);
            buffer1.putLong(i);
            buffer1.putLong(i);
            buffer1.flip();
            parser.message(buffer1);

            ByteBuffer buffer2 = ByteBuffer.allocate(25);
            buffer2.put((byte) 'X');
            buffer2.put(new byte[16]);
            buffer2.putLong(i);
            buffer2.flip();
            parser.message(buffer2);
        }

        assertEquals(10, listener.messages.size());
        for (int i = 0; i < 10; i += 2) {
            assertEquals("enterOrder", listener.messages.get(i));
            assertEquals("cancelOrder", listener.messages.get(i + 1));
        }
    }

    @Test
    void messageWithEnterOrderListenerThrowingIOException() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        listener.toThrow = new IOException("Enter order failed");
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        buffer.put(new byte[16]);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(2L);
        buffer.putLong(3L);
        buffer.flip();

        IOException exception = assertThrows(IOException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Enter order failed", exception.getMessage());
    }

    @Test
    void messageWithCancelOrderListenerThrowingIOException() throws IOException {
        TestPOEServerListener listener = new TestPOEServerListener();
        listener.toThrow = new IOException("Cancel order failed");
        POEServerParser parser = new POEServerParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.put(new byte[16]);
        buffer.putLong(100L);
        buffer.flip();

        IOException exception = assertThrows(IOException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Cancel order failed", exception.getMessage());
    }
}
