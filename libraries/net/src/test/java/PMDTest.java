import com.paritytrading.parity.net.pmd.PMD;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PMDTest {

    @Test
    public void testOrderCanceledGet() {
        ByteBuffer buffer = ByteBuffer.allocate(24);
        buffer.putLong(123456789L);
        buffer.putLong(987654321L);
        buffer.putLong(500L);
        buffer.flip();

        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.get(buffer);

        assertEquals(123456789L, orderCanceled.timestamp);
        assertEquals(987654321L, orderCanceled.orderNumber);
        assertEquals(500L, orderCanceled.canceledQuantity);
    }

    @Test
    public void testOrderCanceledPut() {
        PMD.OrderCanceled orderCanceled = new PMD.OrderCanceled();
        orderCanceled.timestamp = 123456789L;
        orderCanceled.orderNumber = 987654321L;
        orderCanceled.canceledQuantity = 500L;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        orderCanceled.put(buffer);

        buffer.flip();
        assertEquals('X', buffer.get());
        assertEquals(123456789L, buffer.getLong());
        assertEquals(987654321L, buffer.getLong());
        assertEquals(500L, buffer.getLong());
    }
}
