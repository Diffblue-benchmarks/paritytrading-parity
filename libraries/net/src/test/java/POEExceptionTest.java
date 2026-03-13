import com.paritytrading.parity.net.poe.POEException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class POEExceptionTest {

    @Test
    public void testConstructor() {
        POEException exception = new POEException("test message");
        assertNotNull(exception);
        assertEquals("test message", exception.getMessage());
    }

    @Test
    public void testConstructorWithNullMessage() {
        POEException exception = new POEException(null);
        assertNotNull(exception);
    }
}
