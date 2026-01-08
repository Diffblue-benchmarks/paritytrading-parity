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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PMDParserClaudeTest {

    private static class TestPMDListener implements PMDListener {
        List<String> messages = new ArrayList<>();
        PMD.Version lastVersion;
        PMD.OrderAdded lastOrderAdded;
        PMD.OrderExecuted lastOrderExecuted;
        PMD.OrderCanceled lastOrderCanceled;
        IOException toThrow;

        @Override
        public void version(PMD.Version message) throws IOException {
            messages.add("version");
            lastVersion = message;
            if (toThrow != null) throw toThrow;
        }

        @Override
        public void orderAdded(PMD.OrderAdded message) throws IOException {
            messages.add("orderAdded");
            lastOrderAdded = message;
            if (toThrow != null) throw toThrow;
        }

        @Override
        public void orderExecuted(PMD.OrderExecuted message) throws IOException {
            messages.add("orderExecuted");
            lastOrderExecuted = message;
            if (toThrow != null) throw toThrow;
        }

        @Override
        public void orderCanceled(PMD.OrderCanceled message) throws IOException {
            messages.add("orderCanceled");
            lastOrderCanceled = message;
            if (toThrow != null) throw toThrow;
        }
    }

    @Test
    void constructorCreatesParserWithListener() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        assertNotNull(parser);
    }

    @Test
    void messageWithVersionMessageType() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(999);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("version", listener.messages.get(0));
        assertNotNull(listener.lastVersion);
        assertEquals(999L, listener.lastVersion.version);
    }

    @Test
    void messageWithOrderAddedMessageType() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'A');
        buffer.putLong(1000L);      // timestamp
        buffer.putLong(12345L);     // orderNumber
        buffer.put((byte) 'B');     // side
        buffer.putLong(999L);       // instrument
        buffer.putLong(100L);       // quantity
        buffer.putLong(5000L);      // price
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderAdded", listener.messages.get(0));
        assertNotNull(listener.lastOrderAdded);
        assertEquals(1000L, listener.lastOrderAdded.timestamp);
        assertEquals(12345L, listener.lastOrderAdded.orderNumber);
        assertEquals((byte) 'B', listener.lastOrderAdded.side);
        assertEquals(999L, listener.lastOrderAdded.instrument);
        assertEquals(100L, listener.lastOrderAdded.quantity);
        assertEquals(5000L, listener.lastOrderAdded.price);
    }

    @Test
    void messageWithOrderExecutedMessageType() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(29);
        buffer.put((byte) 'E');
        buffer.putLong(2000L);      // timestamp
        buffer.putLong(67890L);     // orderNumber
        buffer.putLong(200L);       // quantity
        buffer.putInt(888);         // matchNumber
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderExecuted", listener.messages.get(0));
        assertNotNull(listener.lastOrderExecuted);
        assertEquals(2000L, listener.lastOrderExecuted.timestamp);
        assertEquals(67890L, listener.lastOrderExecuted.orderNumber);
        assertEquals(200L, listener.lastOrderExecuted.quantity);
        assertEquals(888L, listener.lastOrderExecuted.matchNumber);
    }

    @Test
    void messageWithOrderCanceledMessageType() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.putLong(3000L);      // timestamp
        buffer.putLong(11111L);     // orderNumber
        buffer.putLong(150L);       // canceledQuantity
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals("orderCanceled", listener.messages.get(0));
        assertNotNull(listener.lastOrderCanceled);
        assertEquals(3000L, listener.lastOrderCanceled.timestamp);
        assertEquals(11111L, listener.lastOrderCanceled.orderNumber);
        assertEquals(150L, listener.lastOrderCanceled.canceledQuantity);
    }

    @Test
    void messageWithEmptyBufferThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: no message type", exception.getMessage());
    }

    @Test
    void messageWithUnknownMessageTypeThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'Z');
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Unknown message type: Z", exception.getMessage());
    }

    @Test
    void messageWithVersionButInsufficientDataThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte) 'V');
        buffer.putShort((short) 0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    void messageWithOrderAddedButInsufficientDataThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'A');
        buffer.putLong(1000L);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    void messageWithOrderExecutedButInsufficientDataThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'E');
        buffer.putLong(1000L);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    void messageWithOrderCanceledButInsufficientDataThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'X');
        buffer.putLong(1000L);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    void messageProcessesMultipleVersionMessagesSequentially() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer1 = ByteBuffer.allocate(5);
        buffer1.put((byte) 'V');
        buffer1.putInt(100);
        buffer1.flip();

        parser.message(buffer1);
        assertEquals(100L, listener.lastVersion.version);

        ByteBuffer buffer2 = ByteBuffer.allocate(5);
        buffer2.put((byte) 'V');
        buffer2.putInt(200);
        buffer2.flip();

        parser.message(buffer2);
        assertEquals(200L, listener.lastVersion.version);
        assertEquals(2, listener.messages.size());
    }

    @Test
    void messageProcessesMixedMessageTypes() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer versionBuffer = ByteBuffer.allocate(5);
        versionBuffer.put((byte) 'V');
        versionBuffer.putInt(1);
        versionBuffer.flip();
        parser.message(versionBuffer);

        ByteBuffer orderAddedBuffer = ByteBuffer.allocate(42);
        orderAddedBuffer.put((byte) 'A');
        orderAddedBuffer.putLong(1000L);
        orderAddedBuffer.putLong(12345L);
        orderAddedBuffer.put((byte) 'B');
        orderAddedBuffer.putLong(999L);
        orderAddedBuffer.putLong(100L);
        orderAddedBuffer.putLong(5000L);
        orderAddedBuffer.flip();
        parser.message(orderAddedBuffer);

        ByteBuffer orderExecutedBuffer = ByteBuffer.allocate(29);
        orderExecutedBuffer.put((byte) 'E');
        orderExecutedBuffer.putLong(2000L);
        orderExecutedBuffer.putLong(67890L);
        orderExecutedBuffer.putLong(200L);
        orderExecutedBuffer.putInt(888);
        orderExecutedBuffer.flip();
        parser.message(orderExecutedBuffer);

        ByteBuffer orderCanceledBuffer = ByteBuffer.allocate(25);
        orderCanceledBuffer.put((byte) 'X');
        orderCanceledBuffer.putLong(3000L);
        orderCanceledBuffer.putLong(11111L);
        orderCanceledBuffer.putLong(150L);
        orderCanceledBuffer.flip();
        parser.message(orderCanceledBuffer);

        assertEquals(4, listener.messages.size());
        assertEquals("version", listener.messages.get(0));
        assertEquals("orderAdded", listener.messages.get(1));
        assertEquals("orderExecuted", listener.messages.get(2));
        assertEquals("orderCanceled", listener.messages.get(3));
    }

    @Test
    void messageWithVersionExactMinimumLength() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(42);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(42L, listener.lastVersion.version);
    }

    @Test
    void messageWithOrderAddedExactMinimumLength() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'A');
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.put((byte) 0);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(0L, listener.lastOrderAdded.timestamp);
    }

    @Test
    void messageWithOrderExecutedExactMinimumLength() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(29);
        buffer.put((byte) 'E');
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putInt(0);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(0L, listener.lastOrderExecuted.timestamp);
    }

    @Test
    void messageWithOrderCanceledExactMinimumLength() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.putLong(0L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(0L, listener.lastOrderCanceled.timestamp);
    }

    @Test
    void messageWithVersionAndExtraDataInBuffer() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'V');
        buffer.putInt(555);
        buffer.putInt(999);
        buffer.put((byte) 77);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(555L, listener.lastVersion.version);
        assertEquals(5, buffer.position());
    }

    @Test
    void messageWithOrderAddedMaxValues() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'A');
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.put((byte) 127);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(Long.MAX_VALUE, listener.lastOrderAdded.timestamp);
        assertEquals(Long.MAX_VALUE, listener.lastOrderAdded.orderNumber);
        assertEquals((byte) 127, listener.lastOrderAdded.side);
        assertEquals(Long.MAX_VALUE, listener.lastOrderAdded.instrument);
        assertEquals(Long.MAX_VALUE, listener.lastOrderAdded.quantity);
        assertEquals(Long.MAX_VALUE, listener.lastOrderAdded.price);
    }

    @Test
    void messageWithOrderExecutedMaxValues() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(29);
        buffer.put((byte) 'E');
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putInt(-1);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(Long.MAX_VALUE, listener.lastOrderExecuted.timestamp);
        assertEquals(Long.MAX_VALUE, listener.lastOrderExecuted.orderNumber);
        assertEquals(Long.MAX_VALUE, listener.lastOrderExecuted.quantity);
        assertEquals(0xFFFFFFFFL, listener.lastOrderExecuted.matchNumber);
    }

    @Test
    void messageWithOrderCanceledMaxValues() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(Long.MAX_VALUE, listener.lastOrderCanceled.timestamp);
        assertEquals(Long.MAX_VALUE, listener.lastOrderCanceled.orderNumber);
        assertEquals(Long.MAX_VALUE, listener.lastOrderCanceled.canceledQuantity);
    }

    @Test
    void messageWithVersionUnsignedIntMaxValue() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(-1);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, listener.messages.size());
        assertEquals(0xFFFFFFFFL, listener.lastVersion.version);
    }

    @Test
    void messageWithListenerThrowingIOException() {
        TestPMDListener listener = new TestPMDListener();
        listener.toThrow = new IOException("Test exception");
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(1);
        buffer.flip();

        IOException exception = assertThrows(IOException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    void messageReusesSameMessageObjectsForMultipleCalls() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer1 = ByteBuffer.allocate(5);
        buffer1.put((byte) 'V');
        buffer1.putInt(100);
        buffer1.flip();
        parser.message(buffer1);
        PMD.Version firstVersion = listener.lastVersion;

        ByteBuffer buffer2 = ByteBuffer.allocate(5);
        buffer2.put((byte) 'V');
        buffer2.putInt(200);
        buffer2.flip();
        parser.message(buffer2);
        PMD.Version secondVersion = listener.lastVersion;

        assertSame(firstVersion, secondVersion);
        assertEquals(200L, secondVersion.version);
    }

    @Test
    void messageWithVersionOneByteShortThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put((byte) 'V');
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    void messageWithOrderAddedOneByteLessThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.put((byte) 'A');
        for (int i = 0; i < 40; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    void messageWithOrderExecutedOneByteLessThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.put((byte) 'E');
        for (int i = 0; i < 27; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    void messageWithOrderCanceledOneByteLessThrowsException() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.put((byte) 'X');
        for (int i = 0; i < 23; i++) {
            buffer.put((byte) 0);
        }
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    void messageWithBufferContainingOnlyMessageType() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 'V');
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    void messageUpdatesBufferPosition() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(123);
        buffer.flip();

        assertEquals(0, buffer.position());
        parser.message(buffer);
        assertEquals(5, buffer.position());
    }

    @Test
    void messageWithNonASCIIByteAsUnknownType() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 0xFF);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().startsWith("Unknown message type:"));
    }
}
