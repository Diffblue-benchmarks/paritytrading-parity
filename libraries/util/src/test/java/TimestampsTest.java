import com.paritytrading.parity.util.Timestamps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimestampsTest {

    @Test
    public void testFormatAtMidnight() {
        String result = Timestamps.format(0);
        assertEquals("00:00:00.000", result);
    }

    @Test
    public void testFormatWithMilliseconds() {
        String result = Timestamps.format(12345678);
        assertEquals("03:25:45.678", result);
    }

    @Test
    public void testFormatWithMaxDayValue() {
        long maxMillisInDay = 24 * 60 * 60 * 1000 - 1;
        String result = Timestamps.format(maxMillisInDay);
        assertEquals("23:59:59.999", result);
    }

    @Test
    public void testFormatWrapsAroundDay() {
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        String result = Timestamps.format(oneDayInMillis);
        assertEquals("00:00:00.000", result);
    }

    @Test
    public void testFormatMultipleDays() {
        long twoDaysAndHour = (2 * 24 * 60 * 60 * 1000) + (3600 * 1000);
        String result = Timestamps.format(twoDaysAndHour);
        assertEquals("01:00:00.000", result);
    }
}
