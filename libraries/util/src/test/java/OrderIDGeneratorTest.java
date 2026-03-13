import com.paritytrading.parity.util.OrderIDGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderIDGeneratorTest {

    @Test
    public void testDefaultConstructor() {
        OrderIDGenerator generator = new OrderIDGenerator();
        assertNotNull(generator);
        String id = generator.next();
        assertNotNull(id);
    }

    @Test
    public void testConstructorWithTime() {
        LocalTime time = LocalTime.of(10, 30, 45);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        assertNotNull(generator);
        String id = generator.next();
        assertNotNull(id);
        assertTrue(id.startsWith("10:30:45"));
    }

    @Test
    public void testNextGeneratesFormattedId() {
        LocalTime time = LocalTime.of(14, 25, 30);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        String id = generator.next();
        assertEquals("14:25:30-0000001", id);
    }

    @Test
    public void testNextIncrementsCounter() {
        LocalTime time = LocalTime.of(9, 15, 20);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        String id1 = generator.next();
        String id2 = generator.next();
        String id3 = generator.next();
        assertEquals("09:15:20-0000001", id1);
        assertEquals("09:15:20-0000002", id2);
        assertEquals("09:15:20-0000003", id3);
    }

    @Test
    public void testNextWithMidnightTime() {
        LocalTime time = LocalTime.of(0, 0, 0);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        String id = generator.next();
        assertEquals("00:00:00-0000001", id);
    }
}
