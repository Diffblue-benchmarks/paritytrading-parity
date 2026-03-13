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

import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DisplayFormatTest {

    private DisplayFormat displayFormat;
    private Instruments instruments;
    private OrderBook orderBook;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        instruments = mock(Instruments.class);
        orderBook = mock(OrderBook.class);

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        when(instruments.getPriceWidth()).thenReturn(10);
        when(instruments.getSizeWidth()).thenReturn(8);
        when(instruments.getPricePlaceholder()).thenReturn("         -");
        when(instruments.getSizePlaceholder()).thenReturn("       -");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testConstructor() {
        displayFormat = new DisplayFormat(instruments);

        assertNotNull(displayFormat);
        String output = outputStream.toString();
        assertFalse(output.isEmpty());
        assertTrue(output.contains("Timestamp") || output.contains("timestamp"));
        assertTrue(output.contains("Inst") || output.contains("inst"));
        verify(instruments).getPriceWidth();
        verify(instruments).getSizeWidth();
        verify(instruments).getPricePlaceholder();
        verify(instruments).getSizePlaceholder();
    }

    @Test
    public void testUpdateWithNonBBO() {
        displayFormat = new DisplayFormat(instruments);
        outputStream.reset();

        displayFormat.update(orderBook, false);

        String output = outputStream.toString();
        assertEquals("", output);
    }

    @Test
    public void testUpdateWithBidAndAsk() {
        displayFormat = new DisplayFormat(instruments);
        outputStream.reset();

        Instrument instrument = mock(Instrument.class);
        when(orderBook.getInstrument()).thenReturn(123456L);
        when(instruments.get(123456L)).thenReturn(instrument);
        when(instrument.asString()).thenReturn("AAPL");
        when(instrument.getPriceFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFormat()).thenReturn("%6.0f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        when(orderBook.getBestBidPrice()).thenReturn(10000L);
        when(orderBook.getBidSize(10000L)).thenReturn(100L);
        when(orderBook.getBestAskPrice()).thenReturn(10100L);
        when(orderBook.getAskSize(10100L)).thenReturn(200L);

        displayFormat.timestamp(1234567890000000L);
        displayFormat.update(orderBook, true);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("100.00"));
        assertTrue(output.contains("101.00"));
    }

    @Test
    public void testUpdateWithNoBid() {
        displayFormat = new DisplayFormat(instruments);
        outputStream.reset();

        Instrument instrument = mock(Instrument.class);
        when(orderBook.getInstrument()).thenReturn(123456L);
        when(instruments.get(123456L)).thenReturn(instrument);
        when(instrument.asString()).thenReturn("AAPL");
        when(instrument.getPriceFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFormat()).thenReturn("%6.0f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        when(orderBook.getBestBidPrice()).thenReturn(0L);
        when(orderBook.getBidSize(0L)).thenReturn(0L);
        when(orderBook.getBestAskPrice()).thenReturn(10100L);
        when(orderBook.getAskSize(10100L)).thenReturn(200L);

        displayFormat.timestamp(1234567890000000L);
        displayFormat.update(orderBook, true);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("101.00"));
    }

    @Test
    public void testUpdateWithNoAsk() {
        displayFormat = new DisplayFormat(instruments);
        outputStream.reset();

        Instrument instrument = mock(Instrument.class);
        when(orderBook.getInstrument()).thenReturn(123456L);
        when(instruments.get(123456L)).thenReturn(instrument);
        when(instrument.asString()).thenReturn("AAPL");
        when(instrument.getPriceFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFormat()).thenReturn("%6.0f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        when(orderBook.getBestBidPrice()).thenReturn(10000L);
        when(orderBook.getBidSize(10000L)).thenReturn(100L);
        when(orderBook.getBestAskPrice()).thenReturn(0L);
        when(orderBook.getAskSize(0L)).thenReturn(0L);

        displayFormat.timestamp(1234567890000000L);
        displayFormat.update(orderBook, true);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("100.00"));
    }

    @Test
    public void testUpdateWithNoBidAndNoAsk() {
        displayFormat = new DisplayFormat(instruments);
        outputStream.reset();

        Instrument instrument = mock(Instrument.class);
        when(orderBook.getInstrument()).thenReturn(123456L);
        when(instruments.get(123456L)).thenReturn(instrument);
        when(instrument.asString()).thenReturn("AAPL");
        when(instrument.getPriceFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFormat()).thenReturn("%6.0f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        when(orderBook.getBestBidPrice()).thenReturn(0L);
        when(orderBook.getBidSize(0L)).thenReturn(0L);
        when(orderBook.getBestAskPrice()).thenReturn(0L);
        when(orderBook.getAskSize(0L)).thenReturn(0L);

        displayFormat.timestamp(1234567890000000L);
        displayFormat.update(orderBook, true);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
    }

    @Test
    public void testTradeBuy() {
        displayFormat = new DisplayFormat(instruments);
        outputStream.reset();

        Instrument instrument = mock(Instrument.class);
        when(orderBook.getInstrument()).thenReturn(123456L);
        when(instruments.get(123456L)).thenReturn(instrument);
        when(instrument.asString()).thenReturn("AAPL");
        when(instrument.getPriceFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFormat()).thenReturn("%6.0f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        displayFormat.timestamp(1234567890000000L);
        displayFormat.trade(orderBook, Side.BUY, 10050L, 150L);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("B"));
        assertTrue(output.contains("100.50"));
        assertTrue(output.contains("150"));
    }

    @Test
    public void testTradeSell() {
        displayFormat = new DisplayFormat(instruments);
        outputStream.reset();

        Instrument instrument = mock(Instrument.class);
        when(orderBook.getInstrument()).thenReturn(123456L);
        when(instruments.get(123456L)).thenReturn(instrument);
        when(instrument.asString()).thenReturn("AAPL");
        when(instrument.getPriceFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFormat()).thenReturn("%6.0f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        displayFormat.timestamp(1234567890000000L);
        displayFormat.trade(orderBook, Side.SELL, 10050L, 150L);

        String output = outputStream.toString();
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("S"));
        assertTrue(output.contains("100.50"));
        assertTrue(output.contains("150"));
    }
}
