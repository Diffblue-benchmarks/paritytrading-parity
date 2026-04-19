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

import java.nio.ByteBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class POEOrderRejectedTest {

    private POE.OrderRejected message;

    @BeforeEach
    void setUp() {
        message = new POE.OrderRejected();
    }

    @Test
    void constructor() {
        assertEquals(16, message.orderId.length);
    }

    @Test
    void get() {
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putLong(123456789L);
        buffer.put(new byte[] {
            'O', 'R', 'D', '0', '0', '0', '0', '1',
            '0', '0', '0', '0', '0', '0', '0', '0'
        });
        buffer.put(POE.ORDER_REJECT_REASON_INVALID_PRICE);
        buffer.flip();

        message.get(buffer);

        assertEquals(123456789L, message.timestamp);
        assertArrayEquals(new byte[] {
            'O', 'R', 'D', '0', '0', '0', '0', '1',
            '0', '0', '0', '0', '0', '0', '0', '0'
        }, message.orderId);
        assertEquals(POE.ORDER_REJECT_REASON_INVALID_PRICE, message.reason);
    }

    @Test
    void put() {
        message.timestamp = 123456789L;
        message.orderId   = new byte[] {
            'O', 'R', 'D', '0', '0', '0', '0', '1',
            '0', '0', '0', '0', '0', '0', '0', '0'
        };
        message.reason    = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        message.put(buffer);
        buffer.flip();

        assertEquals('R', buffer.get());
        assertEquals(123456789L, buffer.getLong());

        byte[] orderId = new byte[16];
        buffer.get(orderId);
        assertArrayEquals(new byte[] {
            'O', 'R', 'D', '0', '0', '0', '0', '1',
            '0', '0', '0', '0', '0', '0', '0', '0'
        }, orderId);

        assertEquals(POE.ORDER_REJECT_REASON_INVALID_QUANTITY, buffer.get());
    }

    @Test
    void roundTrip() {
        message.timestamp = 987654321L;
        message.orderId   = new byte[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'
        };
        message.reason    = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        ByteBuffer buffer = ByteBuffer.allocate(26);
        message.put(buffer);
        buffer.flip();

        buffer.get();

        POE.OrderRejected restored = new POE.OrderRejected();
        restored.get(buffer);

        assertEquals(message.timestamp, restored.timestamp);
        assertArrayEquals(message.orderId, restored.orderId);
        assertEquals(message.reason, restored.reason);
    }
}
