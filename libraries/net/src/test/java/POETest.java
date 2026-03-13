import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class POETest {

    @Test
    public void testCancelOrderConstructor() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        assertNotNull(cancelOrder.orderId);
        assertEquals(POE.ORDER_ID_LENGTH, cancelOrder.orderId.length);
    }

    @Test
    public void testCancelOrderGet() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        ByteBuffer buffer = ByteBuffer.allocate(24);
        byte[] testOrderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < testOrderId.length; i++) {
            testOrderId[i] = (byte) i;
        }
        buffer.put(testOrderId);
        buffer.putLong(1000L);
        buffer.flip();
        cancelOrder.get(buffer);
        assertArrayEquals(testOrderId, cancelOrder.orderId);
        assertEquals(1000L, cancelOrder.quantity);
    }

    @Test
    public void testCancelOrderPut() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] testOrderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < testOrderId.length; i++) {
            testOrderId[i] = (byte) (i + 10);
        }
        cancelOrder.orderId = testOrderId;
        cancelOrder.quantity = 500L;
        ByteBuffer buffer = ByteBuffer.allocate(25);
        cancelOrder.put(buffer);
        buffer.flip();
        assertEquals((byte) 'X', buffer.get());
        byte[] readOrderId = new byte[POE.ORDER_ID_LENGTH];
        buffer.get(readOrderId);
        assertArrayEquals(testOrderId, readOrderId);
        assertEquals(500L, buffer.getLong());
    }
}
