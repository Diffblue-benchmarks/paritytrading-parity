import com.paritytrading.parity.net.pmr.PMRException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PMRExceptionTest {

    @Test
    public void testConstructor() {
        PMRException exception = new PMRException("test message");
        assertNotNull(exception);
        assertEquals("test message", exception.getMessage());
    }

    @Test
    public void testConstructorWithNullMessage() {
        PMRException exception = new PMRException(null);
        assertNotNull(exception);
    }
}
