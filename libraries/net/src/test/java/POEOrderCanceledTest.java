import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class POEOrderCanceledTest {

    @Test
    public void testConstructor() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        assertNotNull(orderCanceled);
        assertNotNull(orderCanceled.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, orderCanceled.orderId.length);
    }

    @Test
    public void testGet() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        ByteBuffer buffer = ByteBuffer.allocate(100);

        buffer.putLong(12345L);
        for (int i = 0; i < POE.ORDER_ID_LENGTH; i++) {
            buffer.put((byte) i);
        }
        buffer.putLong(100L);
        buffer.put((byte) 'R');
        buffer.flip();

        orderCanceled.get(buffer);

        assertEquals(12345L, orderCanceled.timestamp);
        for (int i = 0; i < POE.ORDER_ID_LENGTH; i++) {
            assertEquals((byte) i, orderCanceled.orderId[i]);
        }
        assertEquals(100L, orderCanceled.canceledQuantity);
        assertEquals((byte) 'R', orderCanceled.reason);
    }

    @Test
    public void testPut() {
        POE.OrderCanceled orderCanceled = new POE.OrderCanceled();
        orderCanceled.timestamp = 98765L;
        for (int i = 0; i < POE.ORDER_ID_LENGTH; i++) {
            orderCanceled.orderId[i] = (byte) (i + 10);
        }
        orderCanceled.canceledQuantity = 200L;
        orderCanceled.reason = (byte) 'S';

        ByteBuffer buffer = ByteBuffer.allocate(100);
        orderCanceled.put(buffer);
        buffer.flip();

        assertEquals((byte) 'X', buffer.get());
        assertEquals(98765L, buffer.getLong());
        byte[] readOrderId = new byte[POE.ORDER_ID_LENGTH];
        buffer.get(readOrderId);
        for (int i = 0; i < POE.ORDER_ID_LENGTH; i++) {
            assertEquals((byte) (i + 10), readOrderId[i]);
        }
        assertEquals(200L, buffer.getLong());
        assertEquals((byte) 'S', buffer.get());
    }
}
