import com.paritytrading.parity.net.pmd.PMD;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PMDVersionTest {

    @Test
    public void testVersionGet() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(12345);
        buffer.flip();

        PMD.Version version = new PMD.Version();
        version.get(buffer);

        assertEquals(12345L, version.version);
    }

    @Test
    public void testVersionPut() {
        PMD.Version version = new PMD.Version();
        version.version = 2L;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte)'V', buffer.get());
        assertEquals(2, buffer.getInt());
    }
}
