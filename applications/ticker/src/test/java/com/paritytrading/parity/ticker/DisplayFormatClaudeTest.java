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

import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.book.MarketListener;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.foundation.ASCII;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class DisplayFormatClaudeTest {

    private static final long INSTRUMENT_FOO = ASCII.packLong("FOO");
    private static final long INSTRUMENT_BAR = ASCII.packLong("BAR");

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;
    private DisplayFormat displayFormat;
    private Instruments instruments;
    private Market market;
    private OrderBook orderBookFoo;
    private OrderBook orderBookBar;

    @BeforeEach
    void setUp() {
        // Create instruments configuration
        instruments = createInstruments();

        // Redirect System.out to capture output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Create DisplayFormat instance
        displayFormat = new DisplayFormat(instruments);

        // Create market and order books
        market = new Market(new NoOpMarketListener());
        orderBookFoo = market.open(INSTRUMENT_FOO);
        orderBookBar = market.open(INSTRUMENT_BAR);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testConstructor() {
        // The constructor should output a header
        String output = outputStream.toString();

        // Verify that header was printed
        assertTrue(output.contains("Timestamp"), "Header should contain 'Timestamp'");
        assertTrue(output.contains("Inst"), "Header should contain 'Inst'");
        assertTrue(output.contains("Bid Px"), "Header should contain 'Bid Px'");
        assertTrue(output.contains("Bid Size"), "Header should contain 'Bid Size'");
        assertTrue(output.contains("Ask Px"), "Header should contain 'Ask Px'");
        assertTrue(output.contains("Ask Size"), "Header should contain 'Ask Size'");
        assertTrue(output.contains("Trade Px"), "Header should contain 'Trade Px'");
        assertTrue(output.contains("Trade Size"), "Header should contain 'Trade Size'");
    }

    @Test
    void testUpdateWithNonBbo() {
        // Reset output stream after constructor
        outputStream.reset();

        // Set timestamp
        displayFormat.timestamp(1000000000L); // 1 millisecond

        // Call update with bbo=false
        displayFormat.update(orderBookFoo, false);

        // Should produce no output when bbo=false
        String output = outputStream.toString();
        assertEquals("", output, "update with bbo=false should not produce output");
    }

    @Test
    void testUpdateWithBboAndBothSides() {
        // Reset output stream after constructor
        outputStream.reset();

        // Add orders to create bid and ask
        market.add(INSTRUMENT_FOO, 1, Side.BUY, 10000, 100); // price 100.00, size 100
        market.add(INSTRUMENT_FOO, 2, Side.SELL, 10100, 200); // price 101.00, size 200

        // Set timestamp
        displayFormat.timestamp(12345678900000000L); // 12345678.900 seconds = 03:25:45.678

        // Call update with bbo=true
        displayFormat.update(orderBookFoo, true);

        // Verify output
        String output = outputStream.toString();
        assertTrue(output.contains("FOO"), "Output should contain instrument name");
        assertTrue(output.contains("100.00"), "Output should contain bid price");
        assertTrue(output.contains("101.00"), "Output should contain ask price");
    }

    @Test
    void testUpdateWithBboAndOnlyBid() {
        // Reset output stream after constructor
        outputStream.reset();

        // Add only bid order
        market.add(INSTRUMENT_FOO, 1, Side.BUY, 10000, 100);

        // Set timestamp
        displayFormat.timestamp(0L);

        // Call update with bbo=true
        displayFormat.update(orderBookFoo, true);

        // Verify output contains bid but uses placeholder for ask
        String output = outputStream.toString();
        assertTrue(output.contains("100.00"), "Output should contain bid price");
        // The placeholder for ask should be present (spaces and dash)
        assertTrue(output.contains("-"), "Output should contain placeholder for ask");
    }

    @Test
    void testUpdateWithBboAndOnlyAsk() {
        // Reset output stream after constructor
        outputStream.reset();

        // Add only ask order
        market.add(INSTRUMENT_FOO, 2, Side.SELL, 10100, 200);

        // Set timestamp
        displayFormat.timestamp(0L);

        // Call update with bbo=true
        displayFormat.update(orderBookFoo, true);

        // Verify output contains ask but uses placeholder for bid
        String output = outputStream.toString();
        assertTrue(output.contains("101.00"), "Output should contain ask price");
        // The placeholder for bid should be present
        assertTrue(output.contains("-"), "Output should contain placeholder for bid");
    }

    @Test
    void testUpdateWithBboAndNoOrders() {
        // Reset output stream after constructor
        outputStream.reset();

        // Set timestamp
        displayFormat.timestamp(0L);

        // Call update with bbo=true on empty book
        displayFormat.update(orderBookFoo, true);

        // Verify output uses placeholders for both sides
        String output = outputStream.toString();
        assertTrue(output.contains("FOO"), "Output should contain instrument name");
        // Both bid and ask should use placeholders
        long dashCount = output.chars().filter(ch -> ch == '-').count();
        assertTrue(dashCount >= 2, "Output should contain placeholders (dashes) for both bid and ask");
    }

    @Test
    void testUpdateWithDifferentInstrument() {
        // Reset output stream after constructor
        outputStream.reset();

        // Add orders to BAR instrument
        market.add(INSTRUMENT_BAR, 1, Side.BUY, 500000, 1000000000);
        market.add(INSTRUMENT_BAR, 2, Side.SELL, 600000, 2000000000);

        // Set timestamp
        displayFormat.timestamp(0L);

        // Call update with bbo=true
        displayFormat.update(orderBookBar, true);

        // Verify output contains BAR instrument
        String output = outputStream.toString();
        assertTrue(output.contains("BAR"), "Output should contain BAR instrument name");
    }

    @Test
    void testTradeBuySide() {
        // Reset output stream after constructor
        outputStream.reset();

        // Set timestamp
        displayFormat.timestamp(5000000000L); // 5 milliseconds = 00:00:00.005

        // Call trade with BUY side
        displayFormat.trade(orderBookFoo, Side.BUY, 10050, 150);

        // Verify output
        String output = outputStream.toString();
        assertTrue(output.contains("FOO"), "Output should contain instrument name");
        assertTrue(output.contains("B"), "Output should contain 'B' for buy side");
        assertFalse(output.contains("S "), "Output should not contain 'S ' for sell side");
        assertTrue(output.contains("100.50"), "Output should contain trade price");
        assertTrue(output.contains("150"), "Output should contain trade size");
    }

    @Test
    void testTradeSellSide() {
        // Reset output stream after constructor
        outputStream.reset();

        // Set timestamp
        displayFormat.timestamp(5000000000L);

        // Call trade with SELL side
        displayFormat.trade(orderBookFoo, Side.SELL, 10050, 150);

        // Verify output
        String output = outputStream.toString();
        assertTrue(output.contains("FOO"), "Output should contain instrument name");
        assertTrue(output.contains("S"), "Output should contain 'S' for sell side");
        assertTrue(output.contains("100.50"), "Output should contain trade price");
        assertTrue(output.contains("150"), "Output should contain trade size");
    }

    @Test
    void testTradeWithDifferentInstrument() {
        // Reset output stream after constructor
        outputStream.reset();

        // Set timestamp
        displayFormat.timestamp(0L);

        // Call trade on BAR instrument
        displayFormat.trade(orderBookBar, Side.BUY, 550000, 1500000000);

        // Verify output contains BAR instrument
        String output = outputStream.toString();
        assertTrue(output.contains("BAR"), "Output should contain BAR instrument name");
    }

    @Test
    void testTradeWithZeroPrice() {
        // Reset output stream after constructor
        outputStream.reset();

        // Set timestamp
        displayFormat.timestamp(0L);

        // Call trade with zero price
        displayFormat.trade(orderBookFoo, Side.BUY, 0, 100);

        // Verify output is generated (even with zero price)
        String output = outputStream.toString();
        assertTrue(output.contains("FOO"), "Output should contain instrument name");
        assertTrue(output.contains("0.00"), "Output should contain zero price");
    }

    @Test
    void testTradeWithZeroSize() {
        // Reset output stream after constructor
        outputStream.reset();

        // Set timestamp
        displayFormat.timestamp(0L);

        // Call trade with zero size
        displayFormat.trade(orderBookFoo, Side.SELL, 10000, 0);

        // Verify output is generated (even with zero size)
        String output = outputStream.toString();
        assertTrue(output.contains("FOO"), "Output should contain instrument name");
        assertTrue(output.contains("0"), "Output should contain zero size");
    }

    @Test
    void testUpdateFollowedByTrade() {
        // Reset output stream after constructor
        outputStream.reset();

        // Add orders and update
        market.add(INSTRUMENT_FOO, 1, Side.BUY, 10000, 100);
        market.add(INSTRUMENT_FOO, 2, Side.SELL, 10100, 200);
        displayFormat.timestamp(1000000000L);
        displayFormat.update(orderBookFoo, true);

        // Then trade
        displayFormat.timestamp(2000000000L);
        displayFormat.trade(orderBookFoo, Side.BUY, 10050, 75);

        // Verify both outputs are present
        String output = outputStream.toString();
        long fooCount = output.split("FOO", -1).length - 1;
        assertEquals(2, fooCount, "Should have two lines with FOO (one update, one trade)");
    }

    @Test
    void testMultipleUpdates() {
        // Reset output stream after constructor
        outputStream.reset();

        // Add orders
        market.add(INSTRUMENT_FOO, 1, Side.BUY, 10000, 100);
        market.add(INSTRUMENT_FOO, 2, Side.SELL, 10100, 200);

        // First update
        displayFormat.timestamp(1000000000L);
        displayFormat.update(orderBookFoo, true);

        // Modify and second update
        market.modify(1, 150);
        displayFormat.timestamp(2000000000L);
        displayFormat.update(orderBookFoo, true);

        // Verify multiple outputs
        String output = outputStream.toString();
        long fooCount = output.split("FOO", -1).length - 1;
        assertEquals(2, fooCount, "Should have two lines with FOO");
    }

    @Test
    void testTimestampMillisConversion() {
        // Reset output stream after constructor
        outputStream.reset();

        // Set timestamp in nanoseconds (1 second = 1,000,000,000 nanoseconds)
        displayFormat.timestamp(3661000000000L); // 3661 seconds = 01:01:01.000

        // Call update
        market.add(INSTRUMENT_FOO, 1, Side.BUY, 10000, 100);
        displayFormat.update(orderBookFoo, true);

        // Verify timestamp is formatted correctly
        String output = outputStream.toString();
        assertTrue(output.contains("01:01:01"), "Output should contain formatted timestamp");
    }

    @Test
    void testInstrumentWithDifferentPrecision() {
        // Reset output stream after constructor
        outputStream.reset();

        // BAR has 6 price fraction digits and 8 size fraction digits
        market.add(INSTRUMENT_BAR, 1, Side.BUY, 123456, 100000000);
        market.add(INSTRUMENT_BAR, 2, Side.SELL, 234567, 200000000);

        displayFormat.timestamp(0L);
        displayFormat.update(orderBookBar, true);

        // Verify output has correct precision
        String output = outputStream.toString();
        assertTrue(output.contains("BAR"), "Output should contain BAR");
        // BAR has 6 fraction digits for price
        assertTrue(output.contains("0.123456") || output.contains("0.234567"),
                "Output should contain price with 6 decimal places");
    }

    /**
     * Creates test instruments configuration
     */
    private static Instruments createInstruments() {
        String config = "" +
                "instruments = {\n" +
                "  price-integer-digits = 4\n" +
                "  size-integer-digits  = 8\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "  BAR {\n" +
                "    price-fraction-digits = 6\n" +
                "    size-fraction-digits  = 8\n" +
                "  }\n" +
                "}";

        Config parsedConfig = ConfigFactory.parseString(config);
        Instruments instruments = Instruments.fromConfig(parsedConfig, "instruments");

        // Note: The OrderBook uses instrument as a long, but Instruments.get() expects a String.
        // We need to ensure the mapping works. Since OrderBook stores instrument as long,
        // we need to use the string representation.
        // Looking at the Instrument class, it uses ASCII.packLong(asString) to create the long value.
        // For testing, we'll create order books with simple long IDs and rely on string lookup.

        return instruments;
    }

    /**
     * No-op market listener for testing
     */
    private static class NoOpMarketListener implements MarketListener {
        @Override
        public void update(OrderBook book, boolean bbo) {
            // No-op
        }

        @Override
        public void trade(OrderBook book, Side side, long price, long size) {
            // No-op
        }
    }
}
