import com.paritytrading.parity.net.pmr.PMR;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PMRVersionTest {

    @Test
    public void testVersionGet() {
        PMR.Version version = new PMR.Version();
        ByteBuffer buffer = ByteBuffer.allocate(4);

        buffer.putInt(12345);
        buffer.flip();

        version.get(buffer);

        assertEquals(12345L, version.version);
    }

    @Test
    public void testVersionPut() {
        PMR.Version version = new PMR.Version();
        version.version = 54321L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte) 'V', buffer.get());
        assertEquals(54321, buffer.getInt());
    }
}
