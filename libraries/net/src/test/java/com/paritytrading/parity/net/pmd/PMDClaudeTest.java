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

class PMDClaudeTest {

    @Test
    void orderAddedConstructor() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();

        assertNotNull(orderAdded);
        assertEquals(0L, orderAdded.timestamp);
        assertEquals(0L, orderAdded.orderNumber);
        assertEquals(0, orderAdded.side);
        assertEquals(0L, orderAdded.instrument);
        assertEquals(0L, orderAdded.quantity);
        assertEquals(0L, orderAdded.price);
    }

    @Test
    void orderAddedGetBasic() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.putLong(1000L);      // timestamp
        buffer.putLong(12345L);     // orderNumber
        buffer.put(PMD.BUY);        // side
        buffer.putLong(999L);       // instrument
        buffer.putLong(100L);       // quantity
        buffer.putLong(5000L);      // price
        buffer.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.get(buffer);

        assertEquals(1000L, orderAdded.timestamp);
        assertEquals(12345L, orderAdded.orderNumber);
        assertEquals(PMD.BUY, orderAdded.side);
        assertEquals(999L, orderAdded.instrument);
        assertEquals(100L, orderAdded.quantity);
        assertEquals(5000L, orderAdded.price);
    }

    @Test
    void orderAddedGetWithSell() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.putLong(2000L);      // timestamp
        buffer.putLong(67890L);     // orderNumber
        buffer.put(PMD.SELL);       // side
        buffer.putLong(888L);       // instrument
        buffer.putLong(200L);       // quantity
        buffer.putLong(4500L);      // price
        buffer.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.get(buffer);

        assertEquals(2000L, orderAdded.timestamp);
        assertEquals(67890L, orderAdded.orderNumber);
        assertEquals(PMD.SELL, orderAdded.side);
        assertEquals(888L, orderAdded.instrument);
        assertEquals(200L, orderAdded.quantity);
        assertEquals(4500L, orderAdded.price);
    }

    @Test
    void orderAddedGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.putLong(0L);         // timestamp
        buffer.putLong(0L);         // orderNumber
        buffer.put((byte) 0);       // side
        buffer.putLong(0L);         // instrument
        buffer.putLong(0L);         // quantity
        buffer.putLong(0L);         // price
        buffer.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.get(buffer);

        assertEquals(0L, orderAdded.timestamp);
        assertEquals(0L, orderAdded.orderNumber);
        assertEquals((byte) 0, orderAdded.side);
        assertEquals(0L, orderAdded.instrument);
        assertEquals(0L, orderAdded.quantity);
        assertEquals(0L, orderAdded.price);
    }

    @Test
    void orderAddedGetWithMaxValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.putLong(Long.MAX_VALUE);     // timestamp
        buffer.putLong(Long.MAX_VALUE);     // orderNumber
        buffer.put((byte) 127);             // side
        buffer.putLong(Long.MAX_VALUE);     // instrument
        buffer.putLong(Long.MAX_VALUE);     // quantity
        buffer.putLong(Long.MAX_VALUE);     // price
        buffer.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.get(buffer);

        assertEquals(Long.MAX_VALUE, orderAdded.timestamp);
        assertEquals(Long.MAX_VALUE, orderAdded.orderNumber);
        assertEquals((byte) 127, orderAdded.side);
        assertEquals(Long.MAX_VALUE, orderAdded.instrument);
        assertEquals(Long.MAX_VALUE, orderAdded.quantity);
        assertEquals(Long.MAX_VALUE, orderAdded.price);
    }

    @Test
    void orderAddedGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(41);
        buffer.putLong(-1000L);     // timestamp
        buffer.putLong(-12345L);    // orderNumber
        buffer.put((byte) -1);      // side
        buffer.putLong(-999L);      // instrument
        buffer.putLong(-100L);      // quantity
        buffer.putLong(-5000L);     // price
        buffer.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.get(buffer);

        assertEquals(-1000L, orderAdded.timestamp);
        assertEquals(-12345L, orderAdded.orderNumber);
        assertEquals((byte) -1, orderAdded.side);
        assertEquals(-999L, orderAdded.instrument);
        assertEquals(-100L, orderAdded.quantity);
        assertEquals(-5000L, orderAdded.price);
    }

    @Test
    void orderAddedPutBasic() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.timestamp = 1000L;
        orderAdded.orderNumber = 12345L;
        orderAdded.side = PMD.BUY;
        orderAdded.instrument = 999L;
        orderAdded.quantity = 100L;
        orderAdded.price = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        orderAdded.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_ADDED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(12345L, buffer.getLong());
        assertEquals(PMD.BUY, buffer.get());
        assertEquals(999L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
        assertEquals(5000L, buffer.getLong());
    }

    @Test
    void orderAddedPutWithSell() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.timestamp = 2000L;
        orderAdded.orderNumber = 67890L;
        orderAdded.side = PMD.SELL;
        orderAdded.instrument = 888L;
        orderAdded.quantity = 200L;
        orderAdded.price = 4500L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        orderAdded.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_ADDED, buffer.get());
        assertEquals(2000L, buffer.getLong());
        assertEquals(67890L, buffer.getLong());
        assertEquals(PMD.SELL, buffer.get());
        assertEquals(888L, buffer.getLong());
        assertEquals(200L, buffer.getLong());
        assertEquals(4500L, buffer.getLong());
    }

    @Test
    void orderAddedPutWithZeroValues() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.timestamp = 0L;
        orderAdded.orderNumber = 0L;
        orderAdded.side = (byte) 0;
        orderAdded.instrument = 0L;
        orderAdded.quantity = 0L;
        orderAdded.price = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        orderAdded.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_ADDED, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals((byte) 0, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void orderAddedPutWithMaxValues() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.timestamp = Long.MAX_VALUE;
        orderAdded.orderNumber = Long.MAX_VALUE;
        orderAdded.side = (byte) 127;
        orderAdded.instrument = Long.MAX_VALUE;
        orderAdded.quantity = Long.MAX_VALUE;
        orderAdded.price = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        orderAdded.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_ADDED, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals((byte) 127, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void orderAddedPutWithNegativeValues() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.timestamp = -1000L;
        orderAdded.orderNumber = -12345L;
        orderAdded.side = (byte) -1;
        orderAdded.instrument = -999L;
        orderAdded.quantity = -100L;
        orderAdded.price = -5000L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        orderAdded.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_ADDED, buffer.get());
        assertEquals(-1000L, buffer.getLong());
        assertEquals(-12345L, buffer.getLong());
        assertEquals((byte) -1, buffer.get());
        assertEquals(-999L, buffer.getLong());
        assertEquals(-100L, buffer.getLong());
        assertEquals(-5000L, buffer.getLong());
    }

    @Test
    void orderAddedGetThenPutRoundTrip() {
        ByteBuffer buffer1 = ByteBuffer.allocate(41);
        buffer1.putLong(1500L);     // timestamp
        buffer1.putLong(11111L);    // orderNumber
        buffer1.put(PMD.BUY);       // side
        buffer1.putLong(777L);      // instrument
        buffer1.putLong(150L);      // quantity
        buffer1.putLong(6000L);     // price
        buffer1.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.get(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(42);
        orderAdded.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_ADDED, buffer2.get());
        assertEquals(1500L, buffer2.getLong());
        assertEquals(11111L, buffer2.getLong());
        assertEquals(PMD.BUY, buffer2.get());
        assertEquals(777L, buffer2.getLong());
        assertEquals(150L, buffer2.getLong());
        assertEquals(6000L, buffer2.getLong());
    }

    @Test
    void orderAddedPutThenGetRoundTrip() {
        PMD.OrderAdded orderAdded1 = new PMD.OrderAdded();
        orderAdded1.timestamp = 2500L;
        orderAdded1.orderNumber = 22222L;
        orderAdded1.side = PMD.SELL;
        orderAdded1.instrument = 666L;
        orderAdded1.quantity = 250L;
        orderAdded1.price = 7000L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        orderAdded1.put(buffer);
        buffer.flip();

        buffer.get(); // Skip message type

        PMD.OrderAdded orderAdded2 = new PMD.OrderAdded();
        orderAdded2.get(buffer);

        assertEquals(orderAdded1.timestamp, orderAdded2.timestamp);
        assertEquals(orderAdded1.orderNumber, orderAdded2.orderNumber);
        assertEquals(orderAdded1.side, orderAdded2.side);
        assertEquals(orderAdded1.instrument, orderAdded2.instrument);
        assertEquals(orderAdded1.quantity, orderAdded2.quantity);
        assertEquals(orderAdded1.price, orderAdded2.price);
    }

    @Test
    void orderAddedBufferPositionAfterGet() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.putLong(1000L);
        buffer.putLong(12345L);
        buffer.put(PMD.BUY);
        buffer.putLong(999L);
        buffer.putLong(100L);
        buffer.putLong(5000L);
        buffer.put((byte) 99); // Extra data
        buffer.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.get(buffer);

        assertEquals(41, buffer.position());
        assertEquals((byte) 99, buffer.get());
    }

    @Test
    void orderAddedBufferPositionAfterPut() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.timestamp = 1000L;
        orderAdded.orderNumber = 12345L;
        orderAdded.side = PMD.BUY;
        orderAdded.instrument = 999L;
        orderAdded.quantity = 100L;
        orderAdded.price = 5000L;

        ByteBuffer buffer = ByteBuffer.allocate(50);
        orderAdded.put(buffer);

        assertEquals(42, buffer.position());
    }

    @Test
    void orderAddedMultipleGetsFromDifferentBuffers() {
        ByteBuffer buffer1 = ByteBuffer.allocate(41);
        buffer1.putLong(1000L);
        buffer1.putLong(11111L);
        buffer1.put(PMD.BUY);
        buffer1.putLong(111L);
        buffer1.putLong(10L);
        buffer1.putLong(1000L);
        buffer1.flip();

        ByteBuffer buffer2 = ByteBuffer.allocate(41);
        buffer2.putLong(2000L);
        buffer2.putLong(22222L);
        buffer2.put(PMD.SELL);
        buffer2.putLong(222L);
        buffer2.putLong(20L);
        buffer2.putLong(2000L);
        buffer2.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();

        orderAdded.get(buffer1);
        assertEquals(1000L, orderAdded.timestamp);
        assertEquals(PMD.BUY, orderAdded.side);

        orderAdded.get(buffer2);
        assertEquals(2000L, orderAdded.timestamp);
        assertEquals(PMD.SELL, orderAdded.side);
    }

    @Test
    void orderAddedMultiplePutsToDifferentBuffers() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.timestamp = 3000L;
        orderAdded.orderNumber = 33333L;
        orderAdded.side = PMD.BUY;
        orderAdded.instrument = 333L;
        orderAdded.quantity = 30L;
        orderAdded.price = 3000L;

        ByteBuffer buffer1 = ByteBuffer.allocate(42);
        orderAdded.put(buffer1);
        buffer1.flip();

        ByteBuffer buffer2 = ByteBuffer.allocate(42);
        orderAdded.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_ADDED, buffer1.get());
        assertEquals(3000L, buffer1.getLong());

        assertEquals(PMD.MESSAGE_TYPE_ORDER_ADDED, buffer2.get());
        assertEquals(3000L, buffer2.getLong());
    }

    @Test
    void orderAddedFieldsArePubliclyAccessible() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();

        orderAdded.timestamp = 999L;
        orderAdded.orderNumber = 888L;
        orderAdded.side = PMD.BUY;
        orderAdded.instrument = 777L;
        orderAdded.quantity = 666L;
        orderAdded.price = 555L;

        assertEquals(999L, orderAdded.timestamp);
        assertEquals(888L, orderAdded.orderNumber);
        assertEquals(PMD.BUY, orderAdded.side);
        assertEquals(777L, orderAdded.instrument);
        assertEquals(666L, orderAdded.quantity);
        assertEquals(555L, orderAdded.price);
    }

    @Test
    void orderCanceledConstructor() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();

        assertNotNull(orderCanceled);
        assertEquals(0L, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.orderNumber);
        assertEquals(0L, orderCanceled.canceledQuantity);
    }

    @Test
    void orderCanceledGetBasic() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(1000L);      // timestamp
        buffer.putLong(12345L);     // orderNumber
        buffer.putLong(100L);       // canceledQuantity
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(1000L, orderCanceled.timestamp);
        assertEquals(12345L, orderCanceled.orderNumber);
        assertEquals(100L, orderCanceled.canceledQuantity);
    }

    @Test
    void orderCanceledGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(0L);         // timestamp
        buffer.putLong(0L);         // orderNumber
        buffer.putLong(0L);         // canceledQuantity
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(0L, orderCanceled.timestamp);
        assertEquals(0L, orderCanceled.orderNumber);
        assertEquals(0L, orderCanceled.canceledQuantity);
    }

    @Test
    void orderCanceledGetWithMaxValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(Long.MAX_VALUE);     // timestamp
        buffer.putLong(Long.MAX_VALUE);     // orderNumber
        buffer.putLong(Long.MAX_VALUE);     // canceledQuantity
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(Long.MAX_VALUE, orderCanceled.timestamp);
        assertEquals(Long.MAX_VALUE, orderCanceled.orderNumber);
        assertEquals(Long.MAX_VALUE, orderCanceled.canceledQuantity);
    }

    @Test
    void orderCanceledGetWithNegativeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(-1000L);     // timestamp
        buffer.putLong(-12345L);    // orderNumber
        buffer.putLong(-100L);      // canceledQuantity
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(-1000L, orderCanceled.timestamp);
        assertEquals(-12345L, orderCanceled.orderNumber);
        assertEquals(-100L, orderCanceled.canceledQuantity);
    }

    @Test
    void orderCanceledGetWithLargeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(9999999999L);        // timestamp
        buffer.putLong(8888888888L);        // orderNumber
        buffer.putLong(7777777777L);        // canceledQuantity
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(9999999999L, orderCanceled.timestamp);
        assertEquals(8888888888L, orderCanceled.orderNumber);
        assertEquals(7777777777L, orderCanceled.canceledQuantity);
    }

    @Test
    void orderCanceledPutBasic() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1000L;
        orderCanceled.orderNumber = 12345L;
        orderCanceled.canceledQuantity = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(12345L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
    }

    @Test
    void orderCanceledPutWithZeroValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 0L;
        orderCanceled.orderNumber = 0L;
        orderCanceled.canceledQuantity = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
    }

    @Test
    void orderCanceledPutWithMaxValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = Long.MAX_VALUE;
        orderCanceled.orderNumber = Long.MAX_VALUE;
        orderCanceled.canceledQuantity = Long.MAX_VALUE;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
    }

    @Test
    void orderCanceledPutWithNegativeValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = -1000L;
        orderCanceled.orderNumber = -12345L;
        orderCanceled.canceledQuantity = -100L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(-1000L, buffer.getLong());
        assertEquals(-12345L, buffer.getLong());
        assertEquals(-100L, buffer.getLong());
    }

    @Test
    void orderCanceledPutWithLargeValues() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 9999999999L;
        orderCanceled.orderNumber = 8888888888L;
        orderCanceled.canceledQuantity = 7777777777L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer.get());
        assertEquals(9999999999L, buffer.getLong());
        assertEquals(8888888888L, buffer.getLong());
        assertEquals(7777777777L, buffer.getLong());
    }

    @Test
    void orderCanceledGetThenPutRoundTrip() {
        ByteBuffer buffer1 = ByteBuffer.allocate(24);
        buffer1.putLong(1500L);     // timestamp
        buffer1.putLong(11111L);    // orderNumber
        buffer1.putLong(150L);      // canceledQuantity
        buffer1.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer2.get());
        assertEquals(1500L, buffer2.getLong());
        assertEquals(11111L, buffer2.getLong());
        assertEquals(150L, buffer2.getLong());
    }

    @Test
    void orderCanceledPutThenGetRoundTrip() {
        PMD.OrderCanceled orderCanceled1 = new PMD.OrderCanceled();
        orderCanceled1.timestamp = 2500L;
        orderCanceled1.orderNumber = 22222L;
        orderCanceled1.canceledQuantity = 250L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled1.put(buffer);
        buffer.flip();

        buffer.get(); // Skip message type

        PMD.OrderCanceled orderCanceled2 = new PMD.OrderCanceled();
        orderCanceled2.get(buffer);

        assertEquals(orderCanceled1.timestamp, orderCanceled2.timestamp);
        assertEquals(orderCanceled1.orderNumber, orderCanceled2.orderNumber);
        assertEquals(orderCanceled1.canceledQuantity, orderCanceled2.canceledQuantity);
    }

    @Test
    void orderCanceledBufferPositionAfterGet() {
        ByteBuffer buffer = ByteBuffer.allocate(30);
        buffer.putLong(1000L);
        buffer.putLong(12345L);
        buffer.putLong(100L);
        buffer.put((byte) 99); // Extra data
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(24, buffer.position());
        assertEquals((byte) 99, buffer.get());
    }

    @Test
    void orderCanceledBufferPositionAfterPut() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1000L;
        orderCanceled.orderNumber = 12345L;
        orderCanceled.canceledQuantity = 100L;

        ByteBuffer buffer = ByteBuffer.allocate(30);
        orderCanceled.put(buffer);

        assertEquals(25, buffer.position());
    }

    @Test
    void orderCanceledMultipleGetsFromDifferentBuffers() {
        ByteBuffer buffer1 = ByteBuffer.allocate(24);
        buffer1.putLong(1000L);
        buffer1.putLong(11111L);
        buffer1.putLong(10L);
        buffer1.flip();

        ByteBuffer buffer2 = ByteBuffer.allocate(24);
        buffer2.putLong(2000L);
        buffer2.putLong(22222L);
        buffer2.putLong(20L);
        buffer2.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();

        orderCanceled.get(buffer1);
        assertEquals(1000L, orderCanceled.timestamp);
        assertEquals(11111L, orderCanceled.orderNumber);
        assertEquals(10L, orderCanceled.canceledQuantity);

        orderCanceled.get(buffer2);
        assertEquals(2000L, orderCanceled.timestamp);
        assertEquals(22222L, orderCanceled.orderNumber);
        assertEquals(20L, orderCanceled.canceledQuantity);
    }

    @Test
    void orderCanceledMultiplePutsToDifferentBuffers() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 3000L;
        orderCanceled.orderNumber = 33333L;
        orderCanceled.canceledQuantity = 30L;

        ByteBuffer buffer1 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer1);
        buffer1.flip();

        ByteBuffer buffer2 = ByteBuffer.allocate(25);
        orderCanceled.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer1.get());
        assertEquals(3000L, buffer1.getLong());
        assertEquals(33333L, buffer1.getLong());
        assertEquals(30L, buffer1.getLong());

        assertEquals(PMD.MESSAGE_TYPE_ORDER_CANCELED, buffer2.get());
        assertEquals(3000L, buffer2.getLong());
        assertEquals(33333L, buffer2.getLong());
        assertEquals(30L, buffer2.getLong());
    }

    @Test
    void orderCanceledFieldsArePubliclyAccessible() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();

        orderCanceled.timestamp = 999L;
        orderCanceled.orderNumber = 888L;
        orderCanceled.canceledQuantity = 777L;

        assertEquals(999L, orderCanceled.timestamp);
        assertEquals(888L, orderCanceled.orderNumber);
        assertEquals(777L, orderCanceled.canceledQuantity);
    }

    @Test
    void orderCanceledMessageTypeConstant() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1L;
        orderCanceled.orderNumber = 2L;
        orderCanceled.canceledQuantity = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals('X', buffer.get());
    }

    @Test
    void orderCanceledMessageLength() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 1L;
        orderCanceled.orderNumber = 2L;
        orderCanceled.canceledQuantity = 3L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        int startPosition = buffer.position();
        orderCanceled.put(buffer);
        int endPosition = buffer.position();

        assertEquals(25, endPosition - startPosition);
    }

    @Test
    void orderExecutedConstructor() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();

        assertNotNull(orderExecuted);
        assertEquals(0L, orderExecuted.timestamp);
        assertEquals(0L, orderExecuted.orderNumber);
        assertEquals(0L, orderExecuted.quantity);
        assertEquals(0L, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedGetBasic() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(1000L);      // timestamp
        buffer.putLong(12345L);     // orderNumber
        buffer.putLong(100L);       // quantity
        buffer.putInt(999);         // matchNumber (unsigned int)
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1000L, orderExecuted.timestamp);
        assertEquals(12345L, orderExecuted.orderNumber);
        assertEquals(100L, orderExecuted.quantity);
        assertEquals(999L, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedGetWithZeroValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(0L);         // timestamp
        buffer.putLong(0L);         // orderNumber
        buffer.putLong(0L);         // quantity
        buffer.putInt(0);           // matchNumber
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(0L, orderExecuted.timestamp);
        assertEquals(0L, orderExecuted.orderNumber);
        assertEquals(0L, orderExecuted.quantity);
        assertEquals(0L, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedGetWithMaxValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(Long.MAX_VALUE);     // timestamp
        buffer.putLong(Long.MAX_VALUE);     // orderNumber
        buffer.putLong(Long.MAX_VALUE);     // quantity
        buffer.putInt(-1);                  // matchNumber (0xFFFFFFFF as unsigned)
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(Long.MAX_VALUE, orderExecuted.timestamp);
        assertEquals(Long.MAX_VALUE, orderExecuted.orderNumber);
        assertEquals(Long.MAX_VALUE, orderExecuted.quantity);
        assertEquals(0xFFFFFFFFL, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedGetWithNegativeTimestamp() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(-1000L);     // timestamp
        buffer.putLong(-12345L);    // orderNumber
        buffer.putLong(-100L);      // quantity
        buffer.putInt(999);         // matchNumber
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(-1000L, orderExecuted.timestamp);
        assertEquals(-12345L, orderExecuted.orderNumber);
        assertEquals(-100L, orderExecuted.quantity);
        assertEquals(999L, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedGetWithLargeValues() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(9999999999L);        // timestamp
        buffer.putLong(8888888888L);        // orderNumber
        buffer.putLong(7777777777L);        // quantity
        buffer.putInt(123456789);           // matchNumber
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(9999999999L, orderExecuted.timestamp);
        assertEquals(8888888888L, orderExecuted.orderNumber);
        assertEquals(7777777777L, orderExecuted.quantity);
        assertEquals(123456789L, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedGetWithUnsignedIntMaxValue() {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putInt(-1);  // -1 as int = 0xFFFFFFFF = 4294967295 as unsigned
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(1000L, orderExecuted.timestamp);
        assertEquals(2000L, orderExecuted.orderNumber);
        assertEquals(3000L, orderExecuted.quantity);
        assertEquals(4294967295L, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedPutBasic() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.orderNumber = 12345L;
        orderExecuted.quantity = 100L;
        orderExecuted.matchNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(12345L, buffer.getLong());
        assertEquals(100L, buffer.getLong());
        assertEquals(999, buffer.getInt());
    }

    @Test
    void orderExecutedPutWithZeroValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 0L;
        orderExecuted.orderNumber = 0L;
        orderExecuted.quantity = 0L;
        orderExecuted.matchNumber = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0L, buffer.getLong());
        assertEquals(0, buffer.getInt());
    }

    @Test
    void orderExecutedPutWithMaxValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = Long.MAX_VALUE;
        orderExecuted.orderNumber = Long.MAX_VALUE;
        orderExecuted.quantity = Long.MAX_VALUE;
        orderExecuted.matchNumber = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(Long.MAX_VALUE, buffer.getLong());
        assertEquals(-1, buffer.getInt());
    }

    @Test
    void orderExecutedPutWithNegativeTimestamp() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = -1000L;
        orderExecuted.orderNumber = -12345L;
        orderExecuted.quantity = -100L;
        orderExecuted.matchNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(-1000L, buffer.getLong());
        assertEquals(-12345L, buffer.getLong());
        assertEquals(-100L, buffer.getLong());
        assertEquals(999, buffer.getInt());
    }

    @Test
    void orderExecutedPutWithLargeValues() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 9999999999L;
        orderExecuted.orderNumber = 8888888888L;
        orderExecuted.quantity = 7777777777L;
        orderExecuted.matchNumber = 123456789L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(9999999999L, buffer.getLong());
        assertEquals(8888888888L, buffer.getLong());
        assertEquals(7777777777L, buffer.getLong());
        assertEquals(123456789, buffer.getInt());
    }

    @Test
    void orderExecutedPutWithUnsignedIntMaxValue() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.orderNumber = 2000L;
        orderExecuted.quantity = 3000L;
        orderExecuted.matchNumber = 4294967295L;  // Max unsigned int value

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(2000L, buffer.getLong());
        assertEquals(3000L, buffer.getLong());
        assertEquals(-1, buffer.getInt());  // 4294967295L stored as int becomes -1
    }

    @Test
    void orderExecutedGetThenPutRoundTrip() {
        ByteBuffer buffer1 = ByteBuffer.allocate(28);
        buffer1.putLong(1500L);     // timestamp
        buffer1.putLong(11111L);    // orderNumber
        buffer1.putLong(150L);      // quantity
        buffer1.putInt(888);        // matchNumber
        buffer1.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(29);
        orderExecuted.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer2.get());
        assertEquals(1500L, buffer2.getLong());
        assertEquals(11111L, buffer2.getLong());
        assertEquals(150L, buffer2.getLong());
        assertEquals(888, buffer2.getInt());
    }

    @Test
    void orderExecutedPutThenGetRoundTrip() {
        PMD.OrderExecuted orderExecuted1 = new PMD.OrderExecuted();
        orderExecuted1.timestamp = 2500L;
        orderExecuted1.orderNumber = 22222L;
        orderExecuted1.quantity = 250L;
        orderExecuted1.matchNumber = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted1.put(buffer);
        buffer.flip();

        buffer.get(); // Skip message type

        PMD.OrderExecuted orderExecuted2 = new PMD.OrderExecuted();
        orderExecuted2.get(buffer);

        assertEquals(orderExecuted1.timestamp, orderExecuted2.timestamp);
        assertEquals(orderExecuted1.orderNumber, orderExecuted2.orderNumber);
        assertEquals(orderExecuted1.quantity, orderExecuted2.quantity);
        assertEquals(orderExecuted1.matchNumber, orderExecuted2.matchNumber);
    }

    @Test
    void orderExecutedBufferPositionAfterGet() {
        ByteBuffer buffer = ByteBuffer.allocate(35);
        buffer.putLong(1000L);
        buffer.putLong(12345L);
        buffer.putLong(100L);
        buffer.putInt(999);
        buffer.put((byte) 99); // Extra data
        buffer.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.get(buffer);

        assertEquals(28, buffer.position());
        assertEquals((byte) 99, buffer.get());
    }

    @Test
    void orderExecutedBufferPositionAfterPut() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1000L;
        orderExecuted.orderNumber = 12345L;
        orderExecuted.quantity = 100L;
        orderExecuted.matchNumber = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(35);
        orderExecuted.put(buffer);

        assertEquals(29, buffer.position());
    }

    @Test
    void orderExecutedMultipleGetsFromDifferentBuffers() {
        ByteBuffer buffer1 = ByteBuffer.allocate(28);
        buffer1.putLong(1000L);
        buffer1.putLong(11111L);
        buffer1.putLong(10L);
        buffer1.putInt(111);
        buffer1.flip();

        ByteBuffer buffer2 = ByteBuffer.allocate(28);
        buffer2.putLong(2000L);
        buffer2.putLong(22222L);
        buffer2.putLong(20L);
        buffer2.putInt(222);
        buffer2.flip();

        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();

        orderExecuted.get(buffer1);
        assertEquals(1000L, orderExecuted.timestamp);
        assertEquals(11111L, orderExecuted.orderNumber);
        assertEquals(10L, orderExecuted.quantity);
        assertEquals(111L, orderExecuted.matchNumber);

        orderExecuted.get(buffer2);
        assertEquals(2000L, orderExecuted.timestamp);
        assertEquals(22222L, orderExecuted.orderNumber);
        assertEquals(20L, orderExecuted.quantity);
        assertEquals(222L, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedMultiplePutsToDifferentBuffers() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 3000L;
        orderExecuted.orderNumber = 33333L;
        orderExecuted.quantity = 30L;
        orderExecuted.matchNumber = 333L;

        ByteBuffer buffer1 = ByteBuffer.allocate(29);
        orderExecuted.put(buffer1);
        buffer1.flip();

        ByteBuffer buffer2 = ByteBuffer.allocate(29);
        orderExecuted.put(buffer2);
        buffer2.flip();

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer1.get());
        assertEquals(3000L, buffer1.getLong());
        assertEquals(33333L, buffer1.getLong());
        assertEquals(30L, buffer1.getLong());
        assertEquals(333, buffer1.getInt());

        assertEquals(PMD.MESSAGE_TYPE_ORDER_EXECUTED, buffer2.get());
        assertEquals(3000L, buffer2.getLong());
        assertEquals(33333L, buffer2.getLong());
        assertEquals(30L, buffer2.getLong());
        assertEquals(333, buffer2.getInt());
    }

    @Test
    void orderExecutedFieldsArePubliclyAccessible() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();

        orderExecuted.timestamp = 999L;
        orderExecuted.orderNumber = 888L;
        orderExecuted.quantity = 777L;
        orderExecuted.matchNumber = 666L;

        assertEquals(999L, orderExecuted.timestamp);
        assertEquals(888L, orderExecuted.orderNumber);
        assertEquals(777L, orderExecuted.quantity);
        assertEquals(666L, orderExecuted.matchNumber);
    }

    @Test
    void orderExecutedMessageTypeConstant() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1L;
        orderExecuted.orderNumber = 2L;
        orderExecuted.quantity = 3L;
        orderExecuted.matchNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        orderExecuted.put(buffer);
        buffer.flip();

        assertEquals('E', buffer.get());
    }

    @Test
    void orderExecutedMessageLength() {
        PMD.OrderExecuted orderExecuted = new PMD.OrderExecuted();
        orderExecuted.timestamp = 1L;
        orderExecuted.orderNumber = 2L;
        orderExecuted.quantity = 3L;
        orderExecuted.matchNumber = 4L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        int startPosition = buffer.position();
        orderExecuted.put(buffer);
        int endPosition = buffer.position();

        assertEquals(29, endPosition - startPosition);
    }

    // Tests for PMD.Version class

    @Test
    void versionConstructor() {
        PMD.Version version = new PMD.Version();

        assertNotNull(version);
        assertEquals(0L, version.version);
    }

    @Test
    void versionGetBasic() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(999);
        buffer.flip();

        PMD.Version version = new PMD.Version();
        version.get(buffer);

        assertEquals(999L, version.version);
    }

    @Test
    void versionGetWithZeroValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(0);
        buffer.flip();

        PMD.Version version = new PMD.Version();
        version.get(buffer);

        assertEquals(0L, version.version);
    }

    @Test
    void versionGetWithMaxUnsignedIntValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(-1);  // -1 as signed int = 0xFFFFFFFF = max unsigned int (4294967295)
        buffer.flip();

        PMD.Version version = new PMD.Version();
        version.get(buffer);

        assertEquals(0xFFFFFFFFL, version.version);
    }

    @Test
    void versionGetWithLargeUnsignedValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        // Value greater than Integer.MAX_VALUE but valid unsigned int
        buffer.putInt((int) 0x80000000L);  // 2147483648 as unsigned
        buffer.flip();

        PMD.Version version = new PMD.Version();
        version.get(buffer);

        assertEquals(0x80000000L, version.version);
    }

    @Test
    void versionGetUpdatesBufferPosition() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(123);
        buffer.putInt(456);  // extra data
        buffer.flip();

        PMD.Version version = new PMD.Version();
        int startPosition = buffer.position();
        version.get(buffer);
        int endPosition = buffer.position();

        assertEquals(4, endPosition - startPosition);
    }

    @Test
    void versionPutBasic() {
        PMD.Version version = new PMD.Version();
        version.version = 999L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());  // MESSAGE_TYPE_VERSION
        assertEquals(999, buffer.getInt() & 0xffffffffL);
    }

    @Test
    void versionPutWithZeroValue() {
        PMD.Version version = new PMD.Version();
        version.version = 0L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals(0L, buffer.getInt() & 0xffffffffL);
    }

    @Test
    void versionPutWithMaxUnsignedIntValue() {
        PMD.Version version = new PMD.Version();
        version.version = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals(0xFFFFFFFFL, buffer.getInt() & 0xffffffffL);
    }

    @Test
    void versionPutWithLargeUnsignedValue() {
        PMD.Version version = new PMD.Version();
        version.version = 0x80000000L;  // 2147483648

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals(0x80000000L, buffer.getInt() & 0xffffffffL);
    }

    @Test
    void versionPutUpdatesBufferPosition() {
        PMD.Version version = new PMD.Version();
        version.version = 42L;

        ByteBuffer buffer = ByteBuffer.allocate(10);
        int startPosition = buffer.position();
        version.put(buffer);
        int endPosition = buffer.position();

        assertEquals(5, endPosition - startPosition);  // MESSAGE_LENGTH_VERSION = 5
    }

    @Test
    void versionGetThenPutRoundTrip() {
        // Create a buffer with version data
        ByteBuffer sourceBuffer = ByteBuffer.allocate(4);
        sourceBuffer.putInt(12345);
        sourceBuffer.flip();

        // Read into Version object
        PMD.Version version = new PMD.Version();
        version.get(sourceBuffer);

        // Write to new buffer
        ByteBuffer destBuffer = ByteBuffer.allocate(5);
        version.put(destBuffer);
        destBuffer.flip();

        // Verify message type and value
        assertEquals('V', destBuffer.get());
        assertEquals(12345L, destBuffer.getInt() & 0xffffffffL);
    }

    @Test
    void versionPutThenGetRoundTrip() {
        // Set up version
        PMD.Version originalVersion = new PMD.Version();
        originalVersion.version = 9876543L;

        // Write to buffer
        ByteBuffer buffer = ByteBuffer.allocate(5);
        originalVersion.put(buffer);
        buffer.flip();

        // Skip message type byte
        buffer.get();

        // Read into new Version object
        PMD.Version newVersion = new PMD.Version();
        newVersion.get(buffer);

        assertEquals(originalVersion.version, newVersion.version);
    }

    @Test
    void versionMultipleGetOperations() {
        PMD.Version version = new PMD.Version();

        ByteBuffer buffer1 = ByteBuffer.allocate(4);
        buffer1.putInt(100);
        buffer1.flip();
        version.get(buffer1);
        assertEquals(100L, version.version);

        ByteBuffer buffer2 = ByteBuffer.allocate(4);
        buffer2.putInt(200);
        buffer2.flip();
        version.get(buffer2);
        assertEquals(200L, version.version);
    }

    @Test
    void versionMultiplePutOperations() {
        PMD.Version version = new PMD.Version();

        version.version = 111L;
        ByteBuffer buffer1 = ByteBuffer.allocate(5);
        version.put(buffer1);
        buffer1.flip();
        assertEquals('V', buffer1.get());
        assertEquals(111L, buffer1.getInt() & 0xffffffffL);

        version.version = 222L;
        ByteBuffer buffer2 = ByteBuffer.allocate(5);
        version.put(buffer2);
        buffer2.flip();
        assertEquals('V', buffer2.get());
        assertEquals(222L, buffer2.getInt() & 0xffffffffL);
    }
}
