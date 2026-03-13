import com.paritytrading.parity.net.pmr.PMR;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PMRTest {

    @Test
    public void testVersionGetUnsignedInt() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(0xFFFFFFFF);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(0xFFFFFFFFL, version.version);
    }

    @Test
    public void testVersionPutUnsignedInt() {
        PMR.Version version = new PMR.Version();
        version.version = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals((byte)'V', buffer.get());
        assertEquals(-1, buffer.getInt());
    }

    @Test
    public void testTradeGetUnsignedInt() {
        ByteBuffer buffer = ByteBuffer.allocate(36);
        buffer.putLong(1000L);
        buffer.putLong(2000L);
        buffer.putLong(3000L);
        buffer.putLong(4000L);
        buffer.putInt(0x80000000);
        buffer.flip();

        PMR.Trade trade = new PMR.Trade();
        trade.get(buffer);

        assertEquals(0x80000000L, trade.matchNumber);
    }

    @Test
    public void testTradePutUnsignedInt() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 1000L;
        trade.restingOrderNumber = 2000L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity = 4000L;
        trade.matchNumber = 0x80000000L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        assertEquals((byte)'T', buffer.get());
        assertEquals(1000L, buffer.getLong());
        assertEquals(2000L, buffer.getLong());
        assertEquals(3000L, buffer.getLong());
        assertEquals(4000L, buffer.getLong());
        assertEquals(0x80000000, buffer.getInt());
    }
}
