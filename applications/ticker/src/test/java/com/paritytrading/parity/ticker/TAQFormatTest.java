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
import static org.mockito.Mockito.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.file.taq.TAQ;
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TAQFormatTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;
    private Instruments instruments;
    private TAQFormat format;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Config config = ConfigFactory.parseString(
            "instruments { AAPL { price-fraction-digits = 2, size-fraction-digits = 0 } }");
        instruments = Instruments.fromConfig(config, "instruments");

        format = new TAQFormat(instruments);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void constructorWritesHeader() {
        String output = outputStream.toString();

        assertTrue(output.contains("Date"));
        assertTrue(output.contains("Instrument"));
        assertTrue(output.contains("Record Type"));
        assertTrue(output.contains("Bid Price"));
        assertTrue(output.contains("Ask Price"));
        assertTrue(output.contains("Trade Price"));
    }

    @Test
    void updateWithBbo() {
        OrderBook book = mock(OrderBook.class);
        when(book.getInstrument()).thenReturn(ASCII.packLong("AAPL"));
        when(book.getBestBidPrice()).thenReturn(10050L);
        when(book.getBestAskPrice()).thenReturn(10100L);
        when(book.getBidSize(10050L)).thenReturn(100L);
        when(book.getAskSize(10100L)).thenReturn(200L);

        format.timestamp(5000000000L);
        outputStream.reset();
        format.update(book, true);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
    }

    @Test
    void updateWithoutBbo() {
        OrderBook book = mock(OrderBook.class);

        outputStream.reset();
        format.update(book, false);

        String output = outputStream.toString();
        assertEquals("", output);
    }

    @Test
    void tradeWithBuySide() {
        OrderBook book = mock(OrderBook.class);
        when(book.getInstrument()).thenReturn(ASCII.packLong("AAPL"));

        format.timestamp(5000000000L);
        outputStream.reset();
        format.trade(book, Side.BUY, 10050L, 100L);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains(String.valueOf(TAQ.BUY)));
    }

    @Test
    void tradeWithSellSide() {
        OrderBook book = mock(OrderBook.class);
        when(book.getInstrument()).thenReturn(ASCII.packLong("AAPL"));

        format.timestamp(5000000000L);
        outputStream.reset();
        format.trade(book, Side.SELL, 10050L, 100L);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains(String.valueOf(TAQ.SELL)));
    }
}
