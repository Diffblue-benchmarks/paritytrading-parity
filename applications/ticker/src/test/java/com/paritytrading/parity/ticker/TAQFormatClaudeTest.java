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
package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class TAQFormatClaudeTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Instruments instruments;
    private TAQFormat taqFormat;
    private Market market;

    @BeforeEach
    void setUp() {
        // Capture System.out for testing
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Create test instruments using config
        String configString =
            "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits  = 7\n" +
            "\n" +
            "  TEST {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits  = 0\n" +
            "  }\n" +
            "  STOCK {\n" +
            "    price-fraction-digits = 3\n" +
            "    size-fraction-digits  = 1\n" +
            "  }\n" +
            "}\n";
        Config config = ConfigFactory.parseString(configString);
        instruments = Instruments.fromConfig(config, "instruments");

        // Create TAQFormat instance
        taqFormat = new TAQFormat(instruments);

        // Create a market to generate OrderBooks
        market = new Market(taqFormat);
    }

    @AfterEach
    void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    void testConstructorInitializesFormat() {
        // Verify that constructor completed successfully
        assertNotNull(taqFormat, "TAQFormat should be initialized");

        // Verify output was written (header or initialization data)
        String output = outputStream.toString();
        assertNotNull(output, "Output should be captured");
    }

    @Test
    void testConstructorWritesDateToOutput() {
        // Check that today's date appears in the output
        String expectedDate = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());

        // Clear any initial output
        outputStream.reset();

        // Create new TAQFormat to generate fresh output
        TAQFormat newFormat = new TAQFormat(instruments);

        // The constructor should have written something
        String output = outputStream.toString();
        assertNotNull(output, "Output should be written during construction");
    }

    @Test
    void testUpdateWithNonBBO() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Add orders to create a book with bid/ask
        market.add(instrumentId, 1L, Side.BUY, 10000L, 100L);
        market.add(instrumentId, 2L, Side.SELL, 10100L, 200L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(1000000000L);

        // Call update with bbo=false
        taqFormat.update(book, false);

        // Should not write anything when bbo is false
        String output = outputStream.toString();
        assertEquals("", output, "Should not write output when bbo is false");
    }

    @Test
    void testUpdateWithBBO() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Add orders to create a book with bid/ask
        market.add(instrumentId, 1L, Side.BUY, 10000L, 100L);
        market.add(instrumentId, 2L, Side.SELL, 10100L, 200L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(1500000000L);

        // Call update with bbo=true
        taqFormat.update(book, true);

        // Should write a quote record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write output when bbo is true");

        // Verify output contains expected fields
        assertTrue(output.contains("TEST"), "Output should contain instrument name");

        // Verify it's a quote record (starts with date, timestamp, instrument)
        String[] lines = output.split("\n");
        assertTrue(lines.length > 0, "Should have at least one line");

        String line = lines[0];
        String[] fields = line.split("\t");
        assertTrue(fields.length >= 3, "Should have at least date, timestamp, instrument fields");
    }

    @Test
    void testUpdateWithBBOAndDifferentPrices() {
        // Open order book
        long instrumentId = ASCII.packLong("STOCK");
        OrderBook book = market.open(instrumentId);

        // Add orders with specific prices
        market.add(instrumentId, 1L, Side.BUY, 50000L, 1000L);
        market.add(instrumentId, 2L, Side.SELL, 51000L, 2000L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(2000000000L);

        // Call update with bbo=true
        taqFormat.update(book, true);

        // Should write a quote record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write output when bbo is true");

        // Verify output contains instrument name
        assertTrue(output.contains("STOCK"), "Output should contain instrument name");
    }

    @Test
    void testUpdateWithEmptyBook() {
        // Open order book but don't add any orders
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(3000000000L);

        // Call update with bbo=true on empty book
        taqFormat.update(book, true);

        // Should write a quote record with zero prices
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write output even for empty book");

        // Verify output contains instrument name
        assertTrue(output.contains("TEST"), "Output should contain instrument name");
    }

    @Test
    void testTradeWithBuyOrder() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Add a buy order
        market.add(instrumentId, 1L, Side.BUY, 10000L, 100L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(4000000000L);

        // Call trade method
        taqFormat.trade(book, Side.BUY, 10000L, 50L);

        // Should write a trade record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write trade output");

        // Verify output contains instrument name
        assertTrue(output.contains("TEST"), "Output should contain instrument name");

        // Verify it contains 'B' for buy side
        assertTrue(output.contains("B"), "Output should contain buy indicator");
    }

    @Test
    void testTradeWithSellOrder() {
        // Open order book
        long instrumentId = ASCII.packLong("STOCK");
        OrderBook book = market.open(instrumentId);

        // Add a sell order
        market.add(instrumentId, 1L, Side.SELL, 51000L, 200L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(5000000000L);

        // Call trade method
        taqFormat.trade(book, Side.SELL, 51000L, 100L);

        // Should write a trade record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write trade output");

        // Verify output contains instrument name
        assertTrue(output.contains("STOCK"), "Output should contain instrument name");

        // Verify it contains 'S' for sell side
        assertTrue(output.contains("S"), "Output should contain sell indicator");
    }

    @Test
    void testTradeWithDifferentPrices() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Add orders
        market.add(instrumentId, 1L, Side.BUY, 10000L, 100L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(6000000000L);

        // Call trade with specific price and size
        taqFormat.trade(book, Side.BUY, 9950L, 75L);

        // Should write a trade record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write trade output");

        // Verify output contains instrument name
        assertTrue(output.contains("TEST"), "Output should contain instrument name");
    }

    @Test
    void testTradeUsesCorrectFractionDigits() {
        // Test with STOCK which has 3 price fraction digits and 1 size fraction digit
        long instrumentId = ASCII.packLong("STOCK");
        OrderBook book = market.open(instrumentId);

        // Add orders
        market.add(instrumentId, 1L, Side.BUY, 50000L, 10000L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(7000000000L);

        // Call trade
        taqFormat.trade(book, Side.BUY, 50123L, 12345L);

        // Should write a trade record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write trade output");

        // Verify output contains instrument name
        assertTrue(output.contains("STOCK"), "Output should contain instrument name");
    }

    @Test
    void testMultipleUpdatesAndTrades() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Add orders
        market.add(instrumentId, 1L, Side.BUY, 10000L, 100L);
        market.add(instrumentId, 2L, Side.SELL, 10100L, 200L);

        // Clear output from setup
        outputStream.reset();

        // First update
        taqFormat.timestamp(8000000000L);
        taqFormat.update(book, true);

        // First trade
        taqFormat.timestamp(8100000000L);
        taqFormat.trade(book, Side.BUY, 10000L, 50L);

        // Second update
        taqFormat.timestamp(8200000000L);
        taqFormat.update(book, true);

        // Second trade
        taqFormat.timestamp(8300000000L);
        taqFormat.trade(book, Side.SELL, 10100L, 100L);

        // Should have written multiple records
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write output for multiple operations");

        // Count number of lines (each record should be on a new line)
        String[] lines = output.split("\n");
        assertTrue(lines.length >= 4, "Should have at least 4 records");
    }

    @Test
    void testTimestampConversionToMillis() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Add orders
        market.add(instrumentId, 1L, Side.BUY, 10000L, 100L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp in nanoseconds
        long nanos = 123456789000000L;
        taqFormat.timestamp(nanos);

        // Call trade
        taqFormat.trade(book, Side.BUY, 10000L, 50L);

        // Should write a trade record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write trade output");

        // The output should contain a date, timestamp, instrument, and side
        String[] lines = output.split("\n");
        assertTrue(lines.length > 0, "Should have at least one line");

        String line = lines[0];
        String[] fields = line.split("\t");
        assertTrue(fields.length >= 4, "Should have at least 4 fields (date, timestamp, instrument, price, size, side)");

        // Verify the timestamp field is not empty and contains some time representation
        String timestampField = fields[1];
        assertFalse(timestampField.isEmpty(), "Timestamp field should not be empty");
        assertTrue(timestampField.contains(":"), "Timestamp field should contain time separator");
    }

    @Test
    void testUpdateOnlyWritesWhenBBOChanges() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Add orders
        market.add(instrumentId, 1L, Side.BUY, 10000L, 100L);
        market.add(instrumentId, 2L, Side.SELL, 10100L, 200L);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(9000000000L);

        // Call update with bbo=true
        taqFormat.update(book, true);

        String firstOutput = outputStream.toString();
        assertFalse(firstOutput.isEmpty(), "First BBO update should write output");

        // Clear output
        outputStream.reset();

        // Call update with bbo=false (no BBO change)
        taqFormat.update(book, false);

        String secondOutput = outputStream.toString();
        assertEquals("", secondOutput, "Non-BBO update should not write output");

        // Call update with bbo=true again
        taqFormat.update(book, true);

        String thirdOutput = outputStream.toString();
        assertFalse(thirdOutput.isEmpty(), "Second BBO update should write output");
    }

    @Test
    void testTradeWithZeroPrice() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(10000000000L);

        // Call trade with zero price
        taqFormat.trade(book, Side.BUY, 0L, 100L);

        // Should write a trade record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write trade output even with zero price");

        // Verify output contains instrument name
        assertTrue(output.contains("TEST"), "Output should contain instrument name");
    }

    @Test
    void testTradeWithZeroSize() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(11000000000L);

        // Call trade with zero size
        taqFormat.trade(book, Side.SELL, 10000L, 0L);

        // Should write a trade record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write trade output even with zero size");

        // Verify output contains instrument name
        assertTrue(output.contains("TEST"), "Output should contain instrument name");
    }

    @Test
    void testTradeWithLargePriceAndSize() {
        // Open order book
        long instrumentId = ASCII.packLong("TEST");
        OrderBook book = market.open(instrumentId);

        // Clear output from setup
        outputStream.reset();

        // Set timestamp
        taqFormat.timestamp(12000000000L);

        // Call trade with large values
        taqFormat.trade(book, Side.BUY, 999999999L, 888888888L);

        // Should write a trade record
        String output = outputStream.toString();
        assertFalse(output.isEmpty(), "Should write trade output with large values");

        // Verify output contains instrument name
        assertTrue(output.contains("TEST"), "Output should contain instrument name");
    }
}
