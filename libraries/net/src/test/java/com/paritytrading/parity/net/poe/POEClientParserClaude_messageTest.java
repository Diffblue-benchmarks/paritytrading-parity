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
import org.junit.jupiter.api.Test;

class POEClientParserClaude_messageTest {

    private static class TestPOEClientListener implements POEClientListener {
        @Override
        public void orderAccepted(POE.OrderAccepted message) throws IOException {
        }

        @Override
        public void orderRejected(POE.OrderRejected message) throws IOException {
        }

        @Override
        public void orderExecuted(POE.OrderExecuted message) throws IOException {
        }

        @Override
        public void orderCanceled(POE.OrderCanceled message) throws IOException {
        }
    }

    @Test
    void messageWithInsufficientDataForOrderAcceptedThrowsMalformedException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Create buffer with Order Accepted message type but insufficient data (57 bytes instead of 58)
        ByteBuffer buffer = ByteBuffer.allocate(57);
        buffer.put((byte) 'A');  // MESSAGE_TYPE_ORDER_ACCEPTED
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
    void messageWithInsufficientDataForOrderRejectedThrowsMalformedException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Create buffer with Order Rejected message type but insufficient data (25 bytes instead of 26)
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'R');  // MESSAGE_TYPE_ORDER_REJECTED
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
    void messageWithInsufficientDataForOrderExecutedThrowsMalformedException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Create buffer with Order Executed message type but insufficient data (45 bytes instead of 46)
        ByteBuffer buffer = ByteBuffer.allocate(45);
        buffer.put((byte) 'E');  // MESSAGE_TYPE_ORDER_EXECUTED
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
    void messageWithInsufficientDataForOrderCanceledThrowsMalformedException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Create buffer with Order Canceled message type but insufficient data (33 bytes instead of 34)
        ByteBuffer buffer = ByteBuffer.allocate(33);
        buffer.put((byte) 'X');  // MESSAGE_TYPE_ORDER_CANCELED
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
    void messageWithValidOrderAcceptedData() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Create buffer with valid Order Accepted message (58 bytes)
        ByteBuffer buffer = ByteBuffer.allocate(58);
        buffer.put((byte) 'A');           // message type
        buffer.putLong(1000L);            // timestamp
        buffer.put(new byte[16]);         // orderId
        buffer.put((byte) 'B');           // side
        buffer.putLong(12345L);           // instrument
        buffer.putLong(100L);             // quantity
        buffer.putLong(5000L);            // price
        buffer.putLong(99999L);           // orderNumber
        buffer.flip();

        assertDoesNotThrow(() -> parser.message(buffer));
    }

    @Test
    void messageWithValidOrderRejectedData() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Create buffer with valid Order Rejected message (26 bytes)
        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.put((byte) 'R');           // message type
        buffer.putLong(2000L);            // timestamp
        buffer.put(new byte[16]);         // orderId
        buffer.put((byte) 'I');           // reason
        buffer.flip();

        assertDoesNotThrow(() -> parser.message(buffer));
    }

    @Test
    void messageWithValidOrderExecutedData() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Create buffer with valid Order Executed message (46 bytes)
        ByteBuffer buffer = ByteBuffer.allocate(46);
        buffer.put((byte) 'E');           // message type
        buffer.putLong(3000L);            // timestamp
        buffer.put(new byte[16]);         // orderId
        buffer.putLong(50L);              // quantity
        buffer.putLong(4500L);            // price
        buffer.put((byte) 'A');           // liquidityFlag
        buffer.putInt(777);               // matchNumber
        buffer.flip();

        assertDoesNotThrow(() -> parser.message(buffer));
    }

    @Test
    void messageWithValidOrderCanceledData() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Create buffer with valid Order Canceled message (34 bytes)
        ByteBuffer buffer = ByteBuffer.allocate(34);
        buffer.put((byte) 'X');           // message type
        buffer.putLong(4000L);            // timestamp
        buffer.put(new byte[16]);         // orderId
        buffer.putLong(25L);              // canceledQuantity
        buffer.put((byte) 'R');           // reason
        buffer.flip();

        assertDoesNotThrow(() -> parser.message(buffer));
    }

    @Test
    void messageWithOrderAcceptedMinimumLengthBoundary() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Test exact boundary: 58 bytes should succeed
        ByteBuffer buffer1 = ByteBuffer.allocate(58);
        buffer1.put((byte) 'A');
        buffer1.putLong(0L);
        buffer1.put(new byte[16]);
        buffer1.put((byte) 'B');
        buffer1.putLong(0L);
        buffer1.putLong(0L);
        buffer1.putLong(0L);
        buffer1.putLong(0L);
        buffer1.flip();

        assertDoesNotThrow(() -> parser.message(buffer1));

        // Test boundary - 1: 57 bytes should fail
        ByteBuffer buffer2 = ByteBuffer.allocate(57);
        buffer2.put((byte) 'A');
        for (int i = 0; i < 56; i++) {
            buffer2.put((byte) 0);
        }
        buffer2.flip();

        assertThrows(POEException.class, () -> parser.message(buffer2));
    }

    @Test
    void messageWithOrderRejectedMinimumLengthBoundary() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Test exact boundary: 26 bytes should succeed
        ByteBuffer buffer1 = ByteBuffer.allocate(26);
        buffer1.put((byte) 'R');
        buffer1.putLong(0L);
        buffer1.put(new byte[16]);
        buffer1.put((byte) 'I');
        buffer1.flip();

        assertDoesNotThrow(() -> parser.message(buffer1));

        // Test boundary - 1: 25 bytes should fail
        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        buffer2.put((byte) 'R');
        for (int i = 0; i < 24; i++) {
            buffer2.put((byte) 0);
        }
        buffer2.flip();

        assertThrows(POEException.class, () -> parser.message(buffer2));
    }

    @Test
    void messageWithOrderExecutedMinimumLengthBoundary() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Test exact boundary: 46 bytes should succeed
        ByteBuffer buffer1 = ByteBuffer.allocate(46);
        buffer1.put((byte) 'E');
        buffer1.putLong(0L);
        buffer1.put(new byte[16]);
        buffer1.putLong(0L);
        buffer1.putLong(0L);
        buffer1.put((byte) 'A');
        buffer1.putInt(0);
        buffer1.flip();

        assertDoesNotThrow(() -> parser.message(buffer1));

        // Test boundary - 1: 45 bytes should fail
        ByteBuffer buffer2 = ByteBuffer.allocate(45);
        buffer2.put((byte) 'E');
        for (int i = 0; i < 44; i++) {
            buffer2.put((byte) 0);
        }
        buffer2.flip();

        assertThrows(POEException.class, () -> parser.message(buffer2));
    }

    @Test
    void messageWithOrderCanceledMinimumLengthBoundary() throws IOException {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Test exact boundary: 34 bytes should succeed
        ByteBuffer buffer1 = ByteBuffer.allocate(34);
        buffer1.put((byte) 'X');
        buffer1.putLong(0L);
        buffer1.put(new byte[16]);
        buffer1.putLong(0L);
        buffer1.put((byte) 'R');
        buffer1.flip();

        assertDoesNotThrow(() -> parser.message(buffer1));

        // Test boundary - 1: 33 bytes should fail
        ByteBuffer buffer2 = ByteBuffer.allocate(33);
        buffer2.put((byte) 'X');
        for (int i = 0; i < 32; i++) {
            buffer2.put((byte) 0);
        }
        buffer2.flip();

        assertThrows(POEException.class, () -> parser.message(buffer2));
    }

    @Test
    void messageWithEmptyBuffer() {
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
    void messageWithUnknownMessageType() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'Z');  // Unknown message type
        buffer.putLong(1000L);
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });

        assertTrue(exception.getMessage().contains("Unknown message type: Z"));
    }

    @Test
    void messageWithTwoByteBufferOrderAccepted() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Buffer with only message type + 1 byte (2 bytes total) - should fail for Order Accepted
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'A');
        buffer.put((byte) 0);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    @Test
    void messageWithTwoByteBufferOrderRejected() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Buffer with only message type + 1 byte (2 bytes total) - should fail for Order Rejected
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'R');
        buffer.put((byte) 0);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    @Test
    void messageWithTwoByteBufferOrderExecuted() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Buffer with only message type + 1 byte (2 bytes total) - should fail for Order Executed
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'E');
        buffer.put((byte) 0);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    @Test
    void messageWithTwoByteBufferOrderCanceled() {
        TestPOEClientListener listener = new TestPOEClientListener();
        POEClientParser parser = new POEClientParser(listener);

        // Buffer with only message type + 1 byte (2 bytes total) - should fail for Order Canceled
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) 'X');
        buffer.put((byte) 0);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }
}
