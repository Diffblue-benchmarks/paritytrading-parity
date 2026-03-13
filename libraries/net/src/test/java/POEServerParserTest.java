import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEException;
import com.paritytrading.parity.net.poe.POEServerListener;
import com.paritytrading.parity.net.poe.POEServerParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class POEServerParserTest {

    @Test
    public void testConstructor() {
        POEServerListener listener = Mockito.mock(POEServerListener.class);
        POEServerParser parser = new POEServerParser(listener);
        assertNotNull(parser);
    }

    @Test
    public void testMessageWithEmptyBuffer() {
        POEServerListener listener = Mockito.mock(POEServerListener.class);
        POEServerParser parser = new POEServerParser(listener);
        ByteBuffer buffer = ByteBuffer.allocate(0);
        POEException exception = assertThrows(POEException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: no message type", exception.getMessage());
    }

    @Test
    public void testMessageWithEnterOrder() throws IOException {
        POEServerListener listener = Mockito.mock(POEServerListener.class);
        POEServerParser parser = new POEServerParser(listener);
        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte) 'E');
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < orderId.length; i++) {
            orderId[i] = (byte) i;
        }
        buffer.put(orderId);
        buffer.put(POE.BUY);
        buffer.putLong(100L);
        buffer.putLong(200L);
        buffer.putLong(300L);
        buffer.flip();
        parser.message(buffer);
        Mockito.verify(listener, Mockito.times(1)).enterOrder(Mockito.any(POE.EnterOrder.class));
    }

    @Test
    public void testMessageWithMalformedEnterOrder() {
        POEServerListener listener = Mockito.mock(POEServerListener.class);
        POEServerParser parser = new POEServerParser(listener);
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'E');
        buffer.flip();
        POEException exception = assertThrows(POEException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    public void testMessageWithCancelOrder() throws IOException {
        POEServerListener listener = Mockito.mock(POEServerListener.class);
        POEServerParser parser = new POEServerParser(listener);
        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte) 'X');
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        for (int i = 0; i < orderId.length; i++) {
            orderId[i] = (byte) i;
        }
        buffer.put(orderId);
        buffer.putLong(500L);
        buffer.flip();
        parser.message(buffer);
        Mockito.verify(listener, Mockito.times(1)).cancelOrder(Mockito.any(POE.CancelOrder.class));
    }

    @Test
    public void testMessageWithMalformedCancelOrder() {
        POEServerListener listener = Mockito.mock(POEServerListener.class);
        POEServerParser parser = new POEServerParser(listener);
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'X');
        buffer.flip();
        POEException exception = assertThrows(POEException.class, () -> parser.message(buffer));
        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    public void testMessageWithUnknownMessageType() {
        POEServerListener listener = Mockito.mock(POEServerListener.class);
        POEServerParser parser = new POEServerParser(listener);
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'Z');
        buffer.flip();
        POEException exception = assertThrows(POEException.class, () -> parser.message(buffer));
        assertEquals("Unknown message type: Z", exception.getMessage());
    }
}
