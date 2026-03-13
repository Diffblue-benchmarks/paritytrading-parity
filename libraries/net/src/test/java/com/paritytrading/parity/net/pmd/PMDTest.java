package com.paritytrading.parity.net.pmd;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PMDTest {

    @Test
    public void testOrderAddedGet() {
        ByteBuffer buffer = ByteBuffer.allocate(49);
        buffer.putLong(1234567890L);
        buffer.putLong(9876543210L);
        buffer.put(PMD.BUY);
        buffer.putLong(555L);
        buffer.putLong(1000L);
        buffer.putLong(9950L);
        buffer.flip();

        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.get(buffer);

        assertEquals(1234567890L, orderAdded.timestamp);
        assertEquals(9876543210L, orderAdded.orderNumber);
        assertEquals(PMD.BUY, orderAdded.side);
        assertEquals(555L, orderAdded.instrument);
        assertEquals(1000L, orderAdded.quantity);
        assertEquals(9950L, orderAdded.price);
    }

    @Test
    public void testOrderAddedPut() {
        PMD.OrderAdded orderAdded = new PMD.OrderAdded();
        orderAdded.timestamp = 1234567890L;
        orderAdded.orderNumber = 9876543210L;
        orderAdded.side = PMD.SELL;
        orderAdded.instrument = 777L;
        orderAdded.quantity = 500L;
        orderAdded.price = 10050L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        orderAdded.put(buffer);
        buffer.flip();

        assertEquals((byte)'A', buffer.get());
        assertEquals(1234567890L, buffer.getLong());
        assertEquals(9876543210L, buffer.getLong());
        assertEquals(PMD.SELL, buffer.get());
        assertEquals(777L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
        assertEquals(10050L, buffer.getLong());
    }

    @Test
    public void testOrderAddedRoundTrip() {
        PMD.OrderAdded original = new PMD.OrderAdded();
        original.timestamp = 1111111111L;
        original.orderNumber = 2222222222L;
        original.side = PMD.BUY;
        original.instrument = 3333L;
        original.quantity = 4444L;
        original.price = 5555L;

        ByteBuffer buffer = ByteBuffer.allocate(42);
        original.put(buffer);
        buffer.flip();
        buffer.get();

        PMD.OrderAdded restored = new PMD.OrderAdded();
        restored.get(buffer);

        assertEquals(original.timestamp, restored.timestamp);
        assertEquals(original.orderNumber, restored.orderNumber);
        assertEquals(original.side, restored.side);
        assertEquals(original.instrument, restored.instrument);
        assertEquals(original.quantity, restored.quantity);
        assertEquals(original.price, restored.price);
    }
}
