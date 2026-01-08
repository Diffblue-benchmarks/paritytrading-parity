/*
 * Copyright 2014 Parity authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paritytrading.parity.file.taq;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TAQWriterClaudeTest {

    @TempDir
    File tempDir;

    @Test
    void constructorWithFileCreatesWriterAndWritesHeader() throws Exception {
        File file = new File(tempDir, "test.taq");

        try (TAQWriter writer = new TAQWriter(file)) {
            writer.flush();
        }

        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.US_ASCII);
        assertTrue(content.startsWith("Date\tTimestamp\tInstrument\tRecord Type\t"));
    }

    @Test
    void constructorWithFileAndConfigCreatesWriterWithCustomConfig() throws Exception {
        File file = new File(tempDir, "test.taq");
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits(4)
            .build();

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 0;
        quote.instrument = "FOO";
        quote.bidPrice = 100.5;
        quote.bidSize = 1000;
        quote.askPrice = 0;
        quote.askSize = 0;

        try (TAQWriter writer = new TAQWriter(file, config)) {
            writer.write(quote);
            writer.flush();
        }

        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.US_ASCII);
        assertTrue(content.contains("100.5000"));
    }

    @Test
    void constructorWithFileThrowsFileNotFoundExceptionForInvalidPath() {
        File file = new File("/invalid/path/that/does/not/exist/test.taq");

        assertThrows(FileNotFoundException.class, () -> new TAQWriter(file));
    }

    @Test
    void constructorWithOutputStreamCreatesWriterWithDefaultConfig() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.startsWith("Date\tTimestamp\tInstrument\tRecord Type\t"));
        }
    }

    @Test
    void constructorWithOutputStreamAndConfigCreatesWriterWithCustomConfig() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQConfig config = new TAQConfig.Builder()
            .setEncoding(StandardCharsets.UTF_8)
            .build();

        try (TAQWriter writer = new TAQWriter(out, config)) {
            writer.flush();

            String output = out.toString("UTF-8");
            assertTrue(output.startsWith("Date\tTimestamp\tInstrument\tRecord Type\t"));
        }
    }

    @Test
    void writeTradeRecordWritesAllFields() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 8 * 60 * 60 * 1000;
        trade.instrument = "FOO";
        trade.price = 100.50;
        trade.size = 1000;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("2016-01-01"));
            assertTrue(output.contains("08:00:00.000"));
            assertTrue(output.contains("FOO"));
            assertTrue(output.contains("T"));
            assertTrue(output.contains("100.50"));
            assertTrue(output.contains("1000"));
            assertTrue(output.contains("B"));
        }
    }

    @Test
    void writeTradeRecordWithSellSide() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-02";
        trade.timestampMillis = 9 * 60 * 60 * 1000;
        trade.instrument = "BAR";
        trade.price = 50.25;
        trade.size = 500;
        trade.side = TAQ.SELL;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("S"));
        }
    }

    @Test
    void writeTradeRecordWithUnknownSideDoesNotWriteSide() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 100;
        trade.side = TAQ.UNKNOWN;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");
            String dataLine = lines[1];

            assertFalse(dataLine.contains("B"));
            assertFalse(dataLine.contains("S"));
            assertTrue(dataLine.endsWith("\t"));
        }
    }

    @Test
    void writeTradeRecordWithMidnightTimestamp() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 100;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("00:00:00.000"));
        }
    }

    @Test
    void writeTradeRecordWithEndOfDayTimestamp() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000 + 999;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 100;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("23:59:59.999"));
        }
    }

    @Test
    void writeQuoteRecordWritesAllFields() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 8 * 60 * 60 * 1000;
        quote.instrument = "FOO";
        quote.bidPrice = 100.50;
        quote.bidSize = 1000;
        quote.askPrice = 100.75;
        quote.askSize = 250;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(quote);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("2016-01-01"));
            assertTrue(output.contains("08:00:00.000"));
            assertTrue(output.contains("FOO"));
            assertTrue(output.contains("Q"));
            assertTrue(output.contains("100.50"));
            assertTrue(output.contains("1000"));
            assertTrue(output.contains("100.75"));
            assertTrue(output.contains("250"));
        }
    }

    @Test
    void writeQuoteRecordWithZeroBidSizeSkipsBidFields() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 0;
        quote.instrument = "FOO";
        quote.bidPrice = 100.50;
        quote.bidSize = 0;
        quote.askPrice = 100.75;
        quote.askSize = 250;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(quote);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");
            String dataLine = lines[1];
            String[] fields = dataLine.split("\t", -1);

            assertEquals("", fields[4]);
            assertEquals("", fields[5]);
            assertTrue(fields[6].contains("100.75"));
        }
    }

    @Test
    void writeQuoteRecordWithZeroAskSizeSkipsAskFields() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 0;
        quote.instrument = "FOO";
        quote.bidPrice = 100.50;
        quote.bidSize = 1000;
        quote.askPrice = 100.75;
        quote.askSize = 0;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(quote);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");
            String dataLine = lines[1];
            String[] fields = dataLine.split("\t", -1);

            assertTrue(fields[4].contains("100.50"));
            assertTrue(fields[5].contains("1000"));
            assertEquals("", fields[6]);
            assertEquals("", fields[7]);
        }
    }

    @Test
    void writeQuoteRecordWithBothSizesZeroSkipsBothBidAndAskFields() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 0;
        quote.instrument = "FOO";
        quote.bidPrice = 100.50;
        quote.bidSize = 0;
        quote.askPrice = 100.75;
        quote.askSize = 0;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(quote);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");
            String dataLine = lines[1];
            String[] fields = dataLine.split("\t", -1);

            assertEquals("", fields[4]);
            assertEquals("", fields[5]);
            assertEquals("", fields[6]);
            assertEquals("", fields[7]);
        }
    }

    @Test
    void writeMultipleTradeRecords() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade1 = new TAQ.Trade();
        trade1.date = "2016-01-01";
        trade1.timestampMillis = 0;
        trade1.instrument = "FOO";
        trade1.price = 100;
        trade1.size = 100;
        trade1.side = TAQ.BUY;

        TAQ.Trade trade2 = new TAQ.Trade();
        trade2.date = "2016-01-01";
        trade2.timestampMillis = 1000;
        trade2.instrument = "BAR";
        trade2.price = 200;
        trade2.size = 200;
        trade2.side = TAQ.SELL;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade1);
            writer.write(trade2);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");

            assertEquals(3, lines.length);
            assertTrue(lines[1].contains("FOO"));
            assertTrue(lines[2].contains("BAR"));
        }
    }

    @Test
    void writeMultipleQuoteRecords() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Quote quote1 = new TAQ.Quote();
        quote1.date = "2016-01-01";
        quote1.timestampMillis = 0;
        quote1.instrument = "FOO";
        quote1.bidPrice = 100;
        quote1.bidSize = 100;
        quote1.askPrice = 101;
        quote1.askSize = 100;

        TAQ.Quote quote2 = new TAQ.Quote();
        quote2.date = "2016-01-01";
        quote2.timestampMillis = 1000;
        quote2.instrument = "BAR";
        quote2.bidPrice = 200;
        quote2.bidSize = 200;
        quote2.askPrice = 201;
        quote2.askSize = 200;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(quote1);
            writer.write(quote2);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");

            assertEquals(3, lines.length);
            assertTrue(lines[1].contains("FOO"));
            assertTrue(lines[2].contains("BAR"));
        }
    }

    @Test
    void writeMixedTradeAndQuoteRecords() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 100;
        trade.side = TAQ.BUY;

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 1000;
        quote.instrument = "BAR";
        quote.bidPrice = 200;
        quote.bidSize = 200;
        quote.askPrice = 201;
        quote.askSize = 200;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.write(quote);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");

            assertEquals(3, lines.length);
            assertTrue(lines[1].contains("T"));
            assertTrue(lines[2].contains("Q"));
        }
    }

    @Test
    void writeTradeWithCustomPriceFormat() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("BTC", 8)
            .build();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "BTC";
        trade.price = 0.00012345;
        trade.size = 100;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out, config)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("0.00012345"));
        }
    }

    @Test
    void writeTradeWithCustomSizeFormat() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits(4)
            .build();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 1234.5678;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out, config)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("1234.5678"));
        }
    }

    @Test
    void writeQuoteWithCustomPriceFormat() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQConfig config = new TAQConfig.Builder()
            .setPriceFractionDigits("FOREX", 5)
            .build();

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 0;
        quote.instrument = "FOREX";
        quote.bidPrice = 1.23456;
        quote.bidSize = 1000;
        quote.askPrice = 1.23457;
        quote.askSize = 1000;

        try (TAQWriter writer = new TAQWriter(out, config)) {
            writer.write(quote);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("1.23456"));
            assertTrue(output.contains("1.23457"));
        }
    }

    @Test
    void writeQuoteWithCustomSizeFormat() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQConfig config = new TAQConfig.Builder()
            .setSizeFractionDigits("CRYPTO", 8)
            .build();

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 0;
        quote.instrument = "CRYPTO";
        quote.bidPrice = 100;
        quote.bidSize = 0.00000001;
        quote.askPrice = 101;
        quote.askSize = 0.00000002;

        try (TAQWriter writer = new TAQWriter(out, config)) {
            writer.write(quote);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("0.00000001"));
            assertTrue(output.contains("0.00000002"));
        }
    }

    @Test
    void flushWritesDataToOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (TAQWriter writer = new TAQWriter(out)) {
            TAQ.Trade trade = new TAQ.Trade();
            trade.date = "2016-01-01";
            trade.timestampMillis = 0;
            trade.instrument = "FOO";
            trade.price = 100;
            trade.size = 100;
            trade.side = TAQ.BUY;

            writer.write(trade);
            writer.flush();

            assertTrue(out.size() > 0);
            String output = out.toString("US-ASCII");
            assertTrue(output.contains("FOO"));
        }
    }

    @Test
    void closeClosesWriter() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TAQWriter writer = new TAQWriter(out);

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 100;
        trade.side = TAQ.BUY;

        writer.write(trade);
        writer.close();

        assertTrue(out.size() > 0);
    }

    @Test
    void writeToFileAndVerifyContent() throws Exception {
        File file = new File(tempDir, "test.taq");

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 8 * 60 * 60 * 1000;
        trade.instrument = "FOO";
        trade.price = 100.50;
        trade.size = 1000;
        trade.side = TAQ.BUY;

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 8 * 60 * 60 * 1000 + 1000;
        quote.instrument = "BAR";
        quote.bidPrice = 200.25;
        quote.bidSize = 500;
        quote.askPrice = 200.50;
        quote.askSize = 500;

        try (TAQWriter writer = new TAQWriter(file)) {
            writer.write(trade);
            writer.write(quote);
        }

        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.US_ASCII);
        String[] lines = content.split("\n");

        assertEquals(3, lines.length);
        assertTrue(lines[0].startsWith("Date"));
        assertTrue(lines[1].contains("FOO"));
        assertTrue(lines[1].contains("T"));
        assertTrue(lines[2].contains("BAR"));
        assertTrue(lines[2].contains("Q"));
    }

    @Test
    void writeToFileWithCustomConfigAndVerifyContent() throws Exception {
        File file = new File(tempDir, "test.taq");
        TAQConfig config = new TAQConfig.Builder()
            .setEncoding(StandardCharsets.UTF_8)
            .setPriceFractionDigits(4)
            .setSizeFractionDigits(2)
            .build();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 100.5;
        trade.size = 1000.5;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(file, config)) {
            writer.write(trade);
        }

        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        assertTrue(content.contains("100.5000"));
        assertTrue(content.contains("1000.50"));
    }

    @Test
    void writeTradeWithEmptyInstrument() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "";
        trade.price = 100;
        trade.size = 100;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");
            String dataLine = lines[1];
            String[] fields = dataLine.split("\t");

            assertEquals("", fields[2]);
        }
    }

    @Test
    void writeQuoteWithEmptyInstrument() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Quote quote = new TAQ.Quote();
        quote.date = "2016-01-01";
        quote.timestampMillis = 0;
        quote.instrument = "";
        quote.bidPrice = 100;
        quote.bidSize = 100;
        quote.askPrice = 101;
        quote.askSize = 100;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(quote);
            writer.flush();

            String output = out.toString("US-ASCII");
            String[] lines = output.split("\n");
            String dataLine = lines[1];
            String[] fields = dataLine.split("\t");

            assertEquals("", fields[2]);
        }
    }

    @Test
    void writeTradeWithZeroPrice() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 0;
        trade.size = 100;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("0.00"));
        }
    }

    @Test
    void writeTradeWithZeroSize() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 0;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("0\t"));
        }
    }

    @Test
    void writeTradeWithLargePrice() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 999999999.99;
        trade.size = 100;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("999999999.99"));
        }
    }

    @Test
    void writeTradeWithLargeSize() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-01";
        trade.timestampMillis = 0;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 999999999;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("999999999"));
        }
    }

    @Test
    void headerContainsAllExpectedColumns() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.flush();

            String output = out.toString("US-ASCII");
            String headerLine = output.split("\n")[0];

            assertTrue(headerLine.contains("Date"));
            assertTrue(headerLine.contains("Timestamp"));
            assertTrue(headerLine.contains("Instrument"));
            assertTrue(headerLine.contains("Record Type"));
            assertTrue(headerLine.contains("Bid Price"));
            assertTrue(headerLine.contains("Bid Size"));
            assertTrue(headerLine.contains("Ask Price"));
            assertTrue(headerLine.contains("Ask Size"));
            assertTrue(headerLine.contains("Trade Price"));
            assertTrue(headerLine.contains("Trade Size"));
            assertTrue(headerLine.contains("Trade Side"));
        }
    }

    @Test
    void timestampWrapAroundMidnightForLargeValue() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TAQ.Trade trade = new TAQ.Trade();
        trade.date = "2016-01-02";
        trade.timestampMillis = 25 * 60 * 60 * 1000;
        trade.instrument = "FOO";
        trade.price = 100;
        trade.size = 100;
        trade.side = TAQ.BUY;

        try (TAQWriter writer = new TAQWriter(out)) {
            writer.write(trade);
            writer.flush();

            String output = out.toString("US-ASCII");
            assertTrue(output.contains("01:00:00.000"));
        }
    }
}
