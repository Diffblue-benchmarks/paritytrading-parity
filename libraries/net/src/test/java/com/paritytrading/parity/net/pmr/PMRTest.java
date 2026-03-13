package com.paritytrading.parity.net.pmr;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PMRTest {

    @Test
    public void testOrderEnteredGet() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.put(PMR.MESSAGE_TYPE_ORDER_ENTERED);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.put(PMR.BUY);
        buffer.putLong(4000L);
        buffer.putLong(5000L);
        buffer.putLong(6000L);
        buffer.flip();

        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        buffer.get();
        orderEntered.get(buffer);

        assertEquals(1000L, orderEntered.timestamp);
        assertEquals(2000L, orderEntered.username);
        assertEquals(3000L, orderEntered.orderNumber);
        assertEquals(PMR.BUY, orderEntered.side);
        assertEquals(4000L, orderEntered.instrument);
        assertEquals(5000L, orderEntered.quantity);
        assertEquals(6000L, orderEntered.price);
    }

    @Test
    public void testOrderEnteredPut() {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        PMR.OrderEntered orderEntered = new PMR.OrderEntered();
        orderEntered.timestamp = 1000L;
        orderEntered.username = 2000L;
        orderEntered.orderNumber = 3000L;
        orderEntered.side = PMR.SELL;
        orderEntered.instrument = 4000L;
        orderEntered.quantity = 5000L;
        orderEntered.price = 6000L;
        orderEntered.put(buffer);

        buffer.flip();
        assertEquals(PMR.MESSAGE_TYPE_ORDER_ENTERED, buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(2000L, buffer.getLong());
        assertEquals(3000L, buffer.getLong());
        assertEquals(PMR.SELL, buffer.get());
        assertEquals(4000L, buffer.getLong());
        assertEquals(5000L, buffer.getLong());
        assertEquals(6000L, buffer.getLong());
    }
}
