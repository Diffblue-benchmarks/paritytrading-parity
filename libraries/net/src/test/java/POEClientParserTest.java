import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import com.paritytrading.parity.net.poe.POEClientParser;
import com.paritytrading.parity.net.poe.POEException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class POEClientParserTest {

    @Test
    public void testConstructor() {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);
        assertNotNull(parser);
    }

    @Test
    public void testMessageEmptyBuffer() {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);
        ByteBuffer buffer = ByteBuffer.allocate(0);

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: no message type", exception.getMessage());
    }

    @Test
    public void testMessageOrderAccepted() throws IOException {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(58);
        buffer.put((byte) 'A');
        buffer.putLong(123456789L);
        buffer.put(new byte[16]);
        buffer.put((byte) 'B');
        buffer.putLong(1L);
        buffer.putLong(100L);
        buffer.putLong(5000L);
        buffer.putLong(999L);
        buffer.flip();

        parser.message(buffer);
        verify(listener).orderAccepted(any(POE.OrderAccepted.class));
    }

    @Test
    public void testMessageOrderAcceptedTooShort() {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'A');
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    public void testMessageOrderRejected() throws IOException {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(26);
        buffer.put((byte) 'R');
        buffer.putLong(123456789L);
        buffer.put(new byte[16]);
        buffer.put((byte) 'I');
        buffer.flip();

        parser.message(buffer);
        verify(listener).orderRejected(any(POE.OrderRejected.class));
    }

    @Test
    public void testMessageOrderRejectedTooShort() {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'R');
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: R", exception.getMessage());
    }

    @Test
    public void testMessageOrderExecuted() throws IOException {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(46);
        buffer.put((byte) 'E');
        buffer.putLong(123456789L);
        buffer.put(new byte[16]);
        buffer.putLong(100L);
        buffer.putLong(5000L);
        buffer.put((byte) 'A');
        buffer.putInt(12345);
        buffer.flip();

        parser.message(buffer);
        verify(listener).orderExecuted(any(POE.OrderExecuted.class));
    }

    @Test
    public void testMessageOrderExecutedTooShort() {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'E');
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    public void testMessageOrderCanceled() throws IOException {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(34);
        buffer.put((byte) 'X');
        buffer.putLong(123456789L);
        buffer.put(new byte[16]);
        buffer.putLong(50L);
        buffer.put((byte) 'R');
        buffer.flip();

        parser.message(buffer);
        verify(listener).orderCanceled(any(POE.OrderCanceled.class));
    }

    @Test
    public void testMessageOrderCanceledTooShort() {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'X');
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    public void testMessageUnknownMessageType() {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        POEClientParser parser = new POEClientParser(listener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 'Z');
        buffer.flip();

        POEException exception = assertThrows(POEException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Unknown message type: Z", exception.getMessage());
    }
}
