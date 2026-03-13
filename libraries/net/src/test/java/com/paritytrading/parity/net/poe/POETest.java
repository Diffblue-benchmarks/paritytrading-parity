package com.paritytrading.parity.net.poe;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class POETest {

    @Test
    public void testEnterOrderConstructor() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();

        assertNotNull(enterOrder.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, enterOrder.orderId.length);
    }

    @Test
    public void testEnterOrderGet() {
        ByteBuffer buffer = ByteBuffer.allocate(42);
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < orderId.length; i++) {
            orderId[i] = (byte) i;
        }
        buffer.put(orderId);
        buffer.put(POE.BUY);
        buffer.putLong(123L);
        buffer.putLong(456L);
        buffer.putLong(789L);
        buffer.flip();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.get(buffer);

        for (int i = 0; i < orderId.length; i++) {
            assertEquals(orderId[i], enterOrder.orderId[i]);
        }
        assertEquals(POE.BUY, enterOrder.side);
        assertEquals(123L, enterOrder.instrument);
        assertEquals(456L, enterOrder.quantity);
        assertEquals(789L, enterOrder.price);
    }

    @Test
    public void testEnterOrderPut() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < orderId.length; i++) {
            orderId[i] = (byte) (i + 1);
        }
        enterOrder.orderId = orderId;
        enterOrder.side = POE.SELL;
        enterOrder.instrument = 999L;
        enterOrder.quantity = 888L;
        enterOrder.price = 777L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        enterOrder.put(buffer);
        buffer.flip();

        assertEquals((byte)'E', buffer.get());
        for (int i = 0; i < orderId.length; i++) {
            assertEquals(orderId[i], buffer.get());
        }
        assertEquals(POE.SELL, buffer.get());
        assertEquals(999L, buffer.getLong());
        assertEquals(888L, buffer.getLong());
        assertEquals(777L, buffer.getLong());
    }
}
