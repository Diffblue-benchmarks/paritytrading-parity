import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class POEOrderAcceptedTest {

    @Test
    public void testOrderAcceptedConstructor() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        assertNotNull(orderAccepted.orderId);
        assertEquals(16, orderAccepted.orderId.length);
    }

    @Test
    public void testOrderAcceptedGet() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        ByteBuffer buffer = ByteBuffer.allocate(57);

        buffer.putLong(1000L);
        byte[] testOrderId = new byte[16];
        for (int i = 0; i < 16; i++) {
            testOrderId[i] = (byte) i;
        }
        buffer.put(testOrderId);
        buffer.put((byte) 'B');
        buffer.putLong(123L);
        buffer.putLong(456L);
        buffer.putLong(789L);
        buffer.putLong(999L);
        buffer.flip();

        orderAccepted.get(buffer);

        assertEquals(1000L, orderAccepted.timestamp);
        assertArrayEquals(testOrderId, orderAccepted.orderId);
        assertEquals((byte) 'B', orderAccepted.side);
        assertEquals(123L, orderAccepted.instrument);
        assertEquals(456L, orderAccepted.quantity);
        assertEquals(789L, orderAccepted.price);
        assertEquals(999L, orderAccepted.orderNumber);
    }

    @Test
    public void testOrderAcceptedPut() {
        POE.OrderAccepted orderAccepted = new POE.OrderAccepted();
        orderAccepted.timestamp = 2000L;
        for (int i = 0; i < 16; i++) {
            orderAccepted.orderId[i] = (byte) (i + 10);
        }
        orderAccepted.side = (byte) 'S';
        orderAccepted.instrument = 111L;
        orderAccepted.quantity = 222L;
        orderAccepted.price = 333L;
        orderAccepted.orderNumber = 444L;

        ByteBuffer buffer = ByteBuffer.allocate(58);
        orderAccepted.put(buffer);
        buffer.flip();

        assertEquals((byte) 'A', buffer.get());
        assertEquals(2000L, buffer.getLong());
        byte[] readOrderId = new byte[16];
        buffer.get(readOrderId);
        assertArrayEquals(orderAccepted.orderId, readOrderId);
        assertEquals((byte) 'S', buffer.get());
        assertEquals(111L, buffer.getLong());
        assertEquals(222L, buffer.getLong());
        assertEquals(333L, buffer.getLong());
        assertEquals(444L, buffer.getLong());
    }
}
