import com.paritytrading.parity.file.taq.TAQ;
import com.paritytrading.parity.file.taq.TAQConfig;
import com.paritytrading.parity.file.taq.TAQWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TAQWriterTest {

    @TempDir
    Path tempDir;

    @Test
    public void testConstructorWithFile() throws FileNotFoundException {
        File file = tempDir.resolve("test.taq").toFile();
        TAQWriter writer = new TAQWriter(file);
        assertNotNull(writer);
        writer.close();
    }

    @Test
    public void testConstructorWithFileAndConfig() throws FileNotFoundException {
        File file = tempDir.resolve("test.taq").toFile();
        TAQConfig config = TAQConfig.DEFAULTS;
        TAQWriter writer = new TAQWriter(file, config);
        assertNotNull(writer);
        writer.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);
        assertNotNull(writer);
        writer.close();
    }

    @Test
    public void testConstructorWithOutputStreamAndConfig() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQConfig config = TAQConfig.DEFAULTS;
        TAQWriter writer = new TAQWriter(out, config);
        assertNotNull(writer);
        writer.close();
    }

    @Test
    public void testWriteTrade() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2024-01-01";
        trade.timestampMillis = 36000000;
        trade.instrument = "AAPL";
        trade.price = 150.50;
        trade.size = 100.0;
        trade.side = TAQ.BUY;

        writer.write(trade);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("2024-01-01"));
        writer.close();
    }

    @Test
    public void testWriteTradeWithUnknownSide() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2024-01-01";
        trade.timestampMillis = 36000000;
        trade.instrument = "AAPL";
        trade.price = 150.50;
        trade.size = 100.0;
        trade.side = TAQ.UNKNOWN;

        writer.write(trade);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("AAPL"));
        writer.close();
    }

    @Test
    public void testWriteQuote() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2024-01-01";
        quote.timestampMillis = 36000000;
        quote.instrument = "AAPL";
        quote.bidPrice = 150.00;
        quote.bidSize = 100.0;
        quote.askPrice = 150.50;
        quote.askSize = 100.0;

        writer.write(quote);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("2024-01-01"));
        writer.close();
    }

    @Test
    public void testWriteQuoteWithZeroBidSize() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2024-01-01";
        quote.timestampMillis = 36000000;
        quote.instrument = "AAPL";
        quote.bidPrice = 150.00;
        quote.bidSize = 0.0;
        quote.askPrice = 150.50;
        quote.askSize = 100.0;

        writer.write(quote);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("AAPL"));
        writer.close();
    }

    @Test
    public void testWriteQuoteWithZeroAskSize() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2024-01-01";
        quote.timestampMillis = 36000000;
        quote.instrument = "AAPL";
        quote.bidPrice = 150.00;
        quote.bidSize = 100.0;
        quote.askPrice = 150.50;
        quote.askSize = 0.0;

        writer.write(quote);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("AAPL"));
        writer.close();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);
        writer.close();
        assertTrue(out.size() > 0);
    }

    @Test
    public void testFlush() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2024-01-01";
        trade.timestampMillis = 36000000;
        trade.instrument = "AAPL";
        trade.price = 150.50;
        trade.size = 100.0;
        trade.side = TAQ.BUY;

        writer.write(trade);
        writer.flush();

        assertTrue(out.size() > 0);
        writer.close();
    }

    @Test
    public void testTimestampFormatting() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2024-01-01";
        trade.timestampMillis = 3661000;
        trade.instrument = "TEST";
        trade.price = 100.0;
        trade.size = 10.0;
        trade.side = TAQ.BUY;

        writer.write(trade);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("01:01:01"));
        writer.close();
    }

    @Test
    public void testPriceFormatting() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2024-01-01";
        trade.timestampMillis = 36000000;
        trade.instrument = "TEST";
        trade.price = 123.45;
        trade.size = 100.0;
        trade.side = TAQ.BUY;

        writer.write(trade);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("123.45"));
        writer.close();
    }

    @Test
    public void testSizeFormatting() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2024-01-01";
        trade.timestampMillis = 36000000;
        trade.instrument = "TEST";
        trade.price = 100.0;
        trade.size = 250.0;
        trade.side = TAQ.BUY;

        writer.write(trade);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("250"));
        writer.close();
    }

    @Test
    public void testHeaderWritten() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("Date"));
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Instrument"));
        writer.close();
    }

    @Test
    public void testMultipleWrites() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Trade trade1 = new TAQ.Trade();
        trade1.date = "2024-01-01";
        trade1.timestampMillis = 36000000;
        trade1.instrument = "AAPL";
        trade1.price = 150.50;
        trade1.size = 100.0;
        trade1.side = TAQ.BUY;

        TAQ.Trade trade2 = new TAQ.Trade();
        trade2.date = "2024-01-02";
        trade2.timestampMillis = 36000000;
        trade2.instrument = "GOOGL";
        trade2.price = 2800.75;
        trade2.size = 50.0;
        trade2.side = TAQ.SELL;

        writer.write(trade1);
        writer.write(trade2);
        writer.flush();

        String output = out.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("GOOGL"));
        writer.close();
    }
}
