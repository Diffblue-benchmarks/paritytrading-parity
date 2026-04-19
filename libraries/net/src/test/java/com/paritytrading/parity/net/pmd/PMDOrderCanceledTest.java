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

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class PMDOrderCanceledTest {

    @Test
    void get() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(123456789L);
        buffer.putLong(1001L);
        buffer.putLong(500L);
        buffer.flip();

        PMD.OrderCanceled msg = new PMD.OrderCanceled();
        msg.get(buffer);

        assertEquals(123456789L, msg.timestamp);
        assertEquals(1001L, msg.orderNumber);
        assertEquals(500L, msg.canceledQuantity);
    }

    @Test
    void put() {
        ByteBuffer buffer = ByteBuffer.allocate(25);

        PMD.OrderCanceled msg = new PMD.OrderCanceled();
        msg.timestamp        = 123456789L;
        msg.orderNumber      = 1001L;
        msg.canceledQuantity = 500L;
        msg.put(buffer);

        buffer.flip();

        assertEquals('X', buffer.get());
        assertEquals(123456789L, buffer.getLong());
        assertEquals(1001L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
    }

    @Test
    void roundTrip() {
        PMD.OrderCanceled original = new PMD.OrderCanceled();
        original.timestamp        = 987654321L;
        original.orderNumber      = 2002L;
        original.canceledQuantity = 750L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        original.put(buffer);

        buffer.flip();
        buffer.get(); // skip message type byte

        PMD.OrderCanceled parsed = new PMD.OrderCanceled();
        parsed.get(buffer);

        assertEquals(original.timestamp, parsed.timestamp);
        assertEquals(original.orderNumber, parsed.orderNumber);
        assertEquals(original.canceledQuantity, parsed.canceledQuantity);
    }
}
