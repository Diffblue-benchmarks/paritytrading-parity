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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TAQFormatTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testConstructor() {
        Instruments instruments = Mockito.mock(Instruments.class);
        Instrument instrument = Mockito.mock(Instrument.class);

        Mockito.when(instrument.asString()).thenReturn("AAPL");
        Mockito.when(instrument.getPriceFractionDigits()).thenReturn(2);
        Mockito.when(instrument.getSizeFractionDigits()).thenReturn(0);
        Mockito.when(instruments.iterator()).thenReturn(java.util.Collections.singletonList(instrument).iterator());

        TAQFormat format = new TAQFormat(instruments);

        assertNotNull(format);
    }

    @Test
    public void testUpdateWithBbo() {
        Instruments instruments = Mockito.mock(Instruments.class);
        Instrument instrument = Mockito.mock(Instrument.class);
        OrderBook book = Mockito.mock(OrderBook.class);

        Mockito.when(instrument.asString()).thenReturn("AAPL");
        Mockito.when(instrument.getPriceFractionDigits()).thenReturn(2);
        Mockito.when(instrument.getSizeFractionDigits()).thenReturn(0);
        Mockito.when(instrument.getPriceFactor()).thenReturn(100.0);
        Mockito.when(instrument.getSizeFactor()).thenReturn(1.0);
        Mockito.when(instruments.iterator()).thenReturn(java.util.Collections.singletonList(instrument).iterator());
        Mockito.when(instruments.get(Mockito.anyLong())).thenReturn(instrument);
        Mockito.when(book.getInstrument()).thenReturn(1L);
        Mockito.when(book.getBestBidPrice()).thenReturn(10000L);
        Mockito.when(book.getBestAskPrice()).thenReturn(10100L);
        Mockito.when(book.getBidSize(10000L)).thenReturn(100L);
        Mockito.when(book.getAskSize(10100L)).thenReturn(200L);

        TAQFormat format = new TAQFormat(instruments);
        format.timestamp(1000000000L);
        format.update(book, true);

        assertNotNull(outputStream.toString());
    }

    @Test
    public void testUpdateWithoutBbo() {
        Instruments instruments = Mockito.mock(Instruments.class);
        Instrument instrument = Mockito.mock(Instrument.class);
        OrderBook book = Mockito.mock(OrderBook.class);

        Mockito.when(instrument.asString()).thenReturn("AAPL");
        Mockito.when(instrument.getPriceFractionDigits()).thenReturn(2);
        Mockito.when(instrument.getSizeFractionDigits()).thenReturn(0);
        Mockito.when(instruments.iterator()).thenReturn(java.util.Collections.singletonList(instrument).iterator());

        TAQFormat format = new TAQFormat(instruments);

        int sizeBefore = outputStream.size();
        format.update(book, false);
        int sizeAfter = outputStream.size();

        assertNotNull(format);
    }

    @Test
    public void testTradeWithBuy() {
        Instruments instruments = Mockito.mock(Instruments.class);
        Instrument instrument = Mockito.mock(Instrument.class);
        OrderBook book = Mockito.mock(OrderBook.class);

        Mockito.when(instrument.asString()).thenReturn("AAPL");
        Mockito.when(instrument.getPriceFractionDigits()).thenReturn(2);
        Mockito.when(instrument.getSizeFractionDigits()).thenReturn(0);
        Mockito.when(instrument.getPriceFactor()).thenReturn(100.0);
        Mockito.when(instrument.getSizeFactor()).thenReturn(1.0);
        Mockito.when(instruments.iterator()).thenReturn(java.util.Collections.singletonList(instrument).iterator());
        Mockito.when(instruments.get(Mockito.anyLong())).thenReturn(instrument);
        Mockito.when(book.getInstrument()).thenReturn(1L);

        TAQFormat format = new TAQFormat(instruments);
        format.timestamp(2000000000L);
        format.trade(book, Side.BUY, 10050L, 150L);

        assertNotNull(outputStream.toString());
    }

    @Test
    public void testTradeWithSell() {
        Instruments instruments = Mockito.mock(Instruments.class);
        Instrument instrument = Mockito.mock(Instrument.class);
        OrderBook book = Mockito.mock(OrderBook.class);

        Mockito.when(instrument.asString()).thenReturn("AAPL");
        Mockito.when(instrument.getPriceFractionDigits()).thenReturn(2);
        Mockito.when(instrument.getSizeFractionDigits()).thenReturn(0);
        Mockito.when(instrument.getPriceFactor()).thenReturn(100.0);
        Mockito.when(instrument.getSizeFactor()).thenReturn(1.0);
        Mockito.when(instruments.iterator()).thenReturn(java.util.Collections.singletonList(instrument).iterator());
        Mockito.when(instruments.get(Mockito.anyLong())).thenReturn(instrument);
        Mockito.when(book.getInstrument()).thenReturn(1L);

        TAQFormat format = new TAQFormat(instruments);
        format.timestamp(3000000000L);
        format.trade(book, Side.SELL, 10050L, 150L);

        assertNotNull(outputStream.toString());
    }
}
