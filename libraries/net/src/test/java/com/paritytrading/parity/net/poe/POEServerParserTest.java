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

import com.paritytrading.parity.net.poe.POE.CancelOrder;
import com.paritytrading.parity.net.poe.POE.EnterOrder;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class POEServerParserTest {

    private List<POE.InboundMessage> events;
    private POEServerParser parser;

    @BeforeEach
    void setUp() {
        events = new ArrayList<>();

        POEServerListener listener = new POEServerListener() {
            @Override
            public void enterOrder(EnterOrder message) throws IOException {
                EnterOrder copy = new EnterOrder();
                System.arraycopy(message.orderId, 0, copy.orderId, 0, message.orderId.length);
                copy.side       = message.side;
                copy.instrument = message.instrument;
                copy.quantity   = message.quantity;
                copy.price      = message.price;
                events.add(copy);
            }

            @Override
            public void cancelOrder(CancelOrder message) throws IOException {
                CancelOrder copy = new CancelOrder();
                System.arraycopy(message.orderId, 0, copy.orderId, 0, message.orderId.length);
                copy.quantity = message.quantity;
                events.add(copy);
            }
        };

        parser = new POEServerParser(listener);
    }

    @Test
    void enterOrder() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        buffer.put(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        buffer.put(POE.BUY);
        buffer.putLong(12345678L);
        buffer.putLong(100L);
        buffer.putLong(5000L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.size());
        EnterOrder order = (EnterOrder) events.get(0);
        assertEquals(POE.BUY, order.side);
        assertEquals(12345678L, order.instrument);
        assertEquals(100L, order.quantity);
        assertEquals(5000L, order.price);
    }

    @Test
    void cancelOrder() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        buffer.put(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        buffer.putLong(200L);
        buffer.flip();

        parser.message(buffer);

        assertEquals(1, events.size());
        CancelOrder order = (CancelOrder) events.get(0);
        assertEquals(200L, order.quantity);
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
    void malformedEnterOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'E');
        buffer.put(new byte[9]);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }

    @Test
    void malformedCancelOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'X');
        buffer.put(new byte[9]);
        buffer.flip();

        assertThrows(POEException.class, () -> parser.message(buffer));
    }
}
