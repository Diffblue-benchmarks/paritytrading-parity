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
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisplayFormatTest {

    private static final String CONFIG_STRING = "" +
            "instruments = {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits  = 4\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits  = 0\n" +
            "  }\n" +
            "}";

    private PrintStream originalOut;
    private ByteArrayOutputStream captured;

    private Instruments instruments;
    private DisplayFormat format;
    private Market market;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true));

        Config config = ConfigFactory.parseString(CONFIG_STRING);
        instruments = Instruments.fromConfig(config, "instruments");
        format = new DisplayFormat(instruments);
        market = new Market(format);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String flushAndCapture() {
        System.out.flush();
        return captured.toString();
    }

    private void resetCapture() {
        System.out.flush();
        captured.reset();
    }

    @Test
    void constructorDoesNotThrow() {
        Config freshConfig = ConfigFactory.parseString(CONFIG_STRING);
        Instruments freshInstruments = Instruments.fromConfig(freshConfig, "instruments");

        assertDoesNotThrow(() -> new DisplayFormat(freshInstruments));
    }

    @Test
    void updateWithBboFalseDoesNothing() {
        resetCapture();

        long instrument = ASCII.packLong("FOO");
        OrderBook book = market.open(instrument);

        format.timestamp(0);
        format.update(book, false);

        assertEquals("", flushAndCapture());
    }

    @Test
    void updateWithEmptyBook() {
        resetCapture();

        long instrument = ASCII.packLong("FOO");
        OrderBook book = market.open(instrument);

        format.timestamp(0);
        format.update(book, true);

        String output = flushAndCapture();

        assertTrue(output.contains("FOO"));
        assertTrue(output.contains("00:00:00.000"));
    }

    @Test
    void updateWithBidAndAsk() {
        long instrument = ASCII.packLong("FOO");
        OrderBook book = market.open(instrument);

        market.add(instrument, 1, Side.BUY, 1050, 10);
        market.add(instrument, 2, Side.SELL, 1100, 20);

        resetCapture();

        format.timestamp(0);
        format.update(book, true);

        String output = flushAndCapture();

        assertTrue(output.contains("FOO"));
        assertTrue(output.contains("10.50"));
        assertTrue(output.contains("11.00"));
    }

    @Test
    void updateWithBidOnly() {
        long instrument = ASCII.packLong("FOO");
        OrderBook book = market.open(instrument);

        market.add(instrument, 1, Side.BUY, 1050, 10);

        resetCapture();

        format.timestamp(0);
        format.update(book, true);

        String output = flushAndCapture();

        assertTrue(output.contains("10.50"));
    }

    @Test
    void updateWithAskOnly() {
        long instrument = ASCII.packLong("FOO");
        OrderBook book = market.open(instrument);

        market.add(instrument, 1, Side.SELL, 1100, 20);

        resetCapture();

        format.timestamp(0);
        format.update(book, true);

        String output = flushAndCapture();

        assertTrue(output.contains("11.00"));
    }

    @Test
    void tradeBuySide() {
        long instrument = ASCII.packLong("FOO");
        OrderBook book = market.open(instrument);

        resetCapture();

        format.timestamp(0);
        format.trade(book, Side.BUY, 1050, 10);

        String output = flushAndCapture();

        assertTrue(output.contains("FOO"));
        assertTrue(output.contains("B "));
        assertTrue(output.contains("10.50"));
    }

    @Test
    void tradeSellSide() {
        long instrument = ASCII.packLong("FOO");
        OrderBook book = market.open(instrument);

        resetCapture();

        format.timestamp(0);
        format.trade(book, Side.SELL, 1100, 20);

        String output = flushAndCapture();

        assertTrue(output.contains("FOO"));
        assertTrue(output.contains("S "));
        assertTrue(output.contains("11.00"));
    }

    @Test
    void tradeOutputContainsTimestamp() {
        long instrument = ASCII.packLong("FOO");
        OrderBook book = market.open(instrument);

        resetCapture();

        format.timestamp(3_723_000_000_000L);
        format.trade(book, Side.BUY, 1000, 5);

        String output = flushAndCapture();

        assertTrue(output.contains("01:02:03.000"));
    }
}
