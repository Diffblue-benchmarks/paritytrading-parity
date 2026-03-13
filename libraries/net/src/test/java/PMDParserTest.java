import com.paritytrading.parity.net.pmd.PMD;
import com.paritytrading.parity.net.pmd.PMDException;
import com.paritytrading.parity.net.pmd.PMDListener;
import com.paritytrading.parity.net.pmd.PMDParser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PMDParserTest {

    @Test
    public void testConstructor() {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);
        assertNotNull(parser);
    }

    @Test
    public void testMessageWithEmptyBuffer() {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);
        ByteBuffer buffer = ByteBuffer.allocate(0);

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: no message type", exception.getMessage());
    }

    @Test
    public void testMessageWithVersionType() throws IOException {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte)'V');
        buffer.putInt(2);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(mockListener, Mockito.times(1)).version(Mockito.any(PMD.Version.class));
    }

    @Test
    public void testMessageWithVersionTypeMalformed() {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte)'V');
        buffer.putShort((short)2);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: V", exception.getMessage());
    }

    @Test
    public void testMessageWithOrderAddedType() throws IOException {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(42);
        buffer.put((byte)'A');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.put((byte)'B');
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putLong(5000L);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(mockListener, Mockito.times(1)).orderAdded(Mockito.any(PMD.OrderAdded.class));
    }

    @Test
    public void testMessageWithOrderAddedTypeMalformed() {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)'A');
        buffer.putLong(1000L);
        buffer.put((byte)0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: A", exception.getMessage());
    }

    @Test
    public void testMessageWithOrderExecutedType() throws IOException {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(29);
        buffer.put((byte)'E');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putInt(4000);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(mockListener, Mockito.times(1)).orderExecuted(Mockito.any(PMD.OrderExecuted.class));
    }

    @Test
    public void testMessageWithOrderExecutedTypeMalformed() {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)'E');
        buffer.putLong(1000L);
        buffer.put((byte)0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: E", exception.getMessage());
    }

    @Test
    public void testMessageWithOrderCanceledType() throws IOException {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.put((byte)'X');
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.flip();

        parser.message(buffer);

        Mockito.verify(mockListener, Mockito.times(1)).orderCanceled(Mockito.any(PMD.OrderCanceled.class));
    }

    @Test
    public void testMessageWithOrderCanceledTypeMalformed() {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)'X');
        buffer.putLong(1000L);
        buffer.put((byte)0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Malformed message: X", exception.getMessage());
    }

    @Test
    public void testMessageWithUnknownType() {
        PMDListener mockListener = Mockito.mock(PMDListener.class);
        PMDParser parser = new PMDParser(mockListener);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)'Z');
        buffer.putLong(1000L);
        buffer.put((byte)0);
        buffer.flip();

        PMDException exception = assertThrows(PMDException.class, () -> {
            parser.message(buffer);
        });
        assertEquals("Unknown message type: Z", exception.getMessage());
    }
}
