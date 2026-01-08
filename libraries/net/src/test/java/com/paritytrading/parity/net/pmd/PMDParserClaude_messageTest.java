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
import org.junit.jupiter.api.Test;

class PMDParserClaude_messageTest {

    private static class TestPMDListener implements PMDListener {
        PMD.Version lastVersion;
        PMD.OrderAdded lastOrderAdded;
        PMD.OrderExecuted lastOrderExecuted;
        PMD.OrderCanceled lastOrderCanceled;

        @Override
        public void version(PMD.Version message) {
            lastVersion = message;
        }

        @Override
        public void orderAdded(PMD.OrderAdded message) {
            lastOrderAdded = message;
        }

        @Override
        public void orderExecuted(PMD.OrderExecuted message) {
            lastOrderExecuted = message;
        }

        @Override
        public void orderCanceled(PMD.OrderCanceled message) {
            lastOrderCanceled = message;
        }
    }

    @Test
    void messageWithVersionInsufficientLength() {
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
    void messageWithOrderAddedInsufficientLength() {
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
    void messageWithOrderExecutedInsufficientLength() {
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
    void messageWithOrderCanceledInsufficientLength() {
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
    void messageWithVersionExactlyOneByteTooShort() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put((byte) 'V');
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    void messageWithOrderAddedExactlyOneByteTooShort() {
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
    void messageWithOrderExecutedExactlyOneByteTooShort() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.put((byte) 'E');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    void messageWithOrderCanceledExactlyOneByteTooShort() {
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
    void messageWithVersionMinimumLength() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'V');
        buffer.put((byte) 0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    void messageWithOrderAddedMinimumLength() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'A');
        buffer.put((byte) 0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    void messageWithOrderExecutedMinimumLength() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'E');
        buffer.put((byte) 0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    void messageWithOrderCanceledMinimumLength() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'X');
        buffer.put((byte) 0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    void messageWithVersionOnlyMessageType() {
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
    void messageWithOrderAddedOnlyMessageType() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 'A');
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    void messageWithOrderExecutedOnlyMessageType() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 'E');
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    void messageWithOrderCanceledOnlyMessageType() {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 'X');
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });

        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    void messageWithValidVersionMessage() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte) 'V');
        buffer.putInt(42);
        buffer.flip();

        parser.message(buffer);

        assertNotNull(listener.lastVersion);
        assertEquals(42L, listener.lastVersion.version);
    }

    @Test
    void messageWithValidOrderAddedMessage() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'A');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.put((byte) 'B');
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putLong(5000L);
        buffer.flip();

        parser.message(buffer);

        assertNotNull(listener.lastOrderAdded);
        assertEquals(1000L, listener.lastOrderAdded.timestamp);
    }

    @Test
    void messageWithValidOrderExecutedMessage() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(29);
        buffer.put((byte) 'E');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putInt(4000);
        buffer.flip();

        parser.message(buffer);

        assertNotNull(listener.lastOrderExecuted);
        assertEquals(1000L, listener.lastOrderExecuted.timestamp);
    }

    @Test
    void messageWithValidOrderCanceledMessage() throws IOException {
        TestPMDListener listener = new TestPMDListener();
        PMDParser parser = new PMDParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.flip();

        parser.message(buffer);

        assertNotNull(listener.lastOrderCanceled);
        assertEquals(1000L, listener.lastOrderCanceled.timestamp);
    }
}
