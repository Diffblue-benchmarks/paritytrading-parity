import com.paritytrading.parity.util.TableHeader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TableHeaderTest {

    @Test
    public void testAddColumn() {
        TableHeader header = new TableHeader();
        header.add("Name", 10);
        String result = header.format();
        assertNotNull(result);
    }

    @Test
    public void testFormatWithSingleColumn() {
        TableHeader header = new TableHeader();
        header.add("Test", 5);
        String result = header.format();
        assertEquals("Test \n-----\n", result);
    }

    @Test
    public void testFormatWithMultipleColumns() {
        TableHeader header = new TableHeader();
        header.add("Name", 10);
        header.add("Age", 5);
        String result = header.format();
        assertNotNull(result);
    }

    @Test
    public void testColumnWidth() {
        TableHeader header = new TableHeader();
        header.add("LongColumnName", 10);
        String result = header.format();
        assertNotNull(result);
    }
}
