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
package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TSVFormatTest {

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
        Instrument instrument1 = mock(Instrument.class);
        when(instrument1.asString()).thenReturn("AAPL");
        when(instrument1.getPriceFractionDigits()).thenReturn(2);
        when(instrument1.getSizeFractionDigits()).thenReturn(0);

        Instrument instrument2 = mock(Instrument.class);
        when(instrument2.asString()).thenReturn("MSFT");
        when(instrument2.getPriceFractionDigits()).thenReturn(4);
        when(instrument2.getSizeFractionDigits()).thenReturn(2);

        List<Instrument> instrumentList = new ArrayList<>();
        instrumentList.add(instrument1);
        instrumentList.add(instrument2);

        Instruments instruments = mock(Instruments.class);
        when(instruments.iterator()).thenReturn(instrumentList.iterator());

        new TSVFormat(instruments);

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Match Number"));
        assertTrue(output.contains("Instrument"));
        assertTrue(output.contains("Quantity"));
        assertTrue(output.contains("Price"));
        assertTrue(output.contains("Buyer"));
        assertTrue(output.contains("Buy Order Number"));
        assertTrue(output.contains("Seller"));
        assertTrue(output.contains("Sell Order Number"));
    }

    @Test
    public void testTrade() {
        Instrument instrument = mock(Instrument.class);
        when(instrument.asString()).thenReturn("AAPL");
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(0);
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        List<Instrument> instrumentList = new ArrayList<>();
        instrumentList.add(instrument);

        Instruments instruments = mock(Instruments.class);
        when(instruments.iterator()).thenReturn(instrumentList.iterator());
        when(instruments.get("AAPL")).thenReturn(instrument);

        TSVFormat tsvFormat = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "09:30:00.123456";
        trade.matchNumber = 12345L;
        trade.instrument = "AAPL";
        trade.quantity = 100L;
        trade.price = 15000L;
        trade.buyer = "BUYER1";
        trade.buyOrderNumber = 1001L;
        trade.seller = "SELLER1";
        trade.sellOrderNumber = 2001L;

        tsvFormat.trade(trade);

        String output = outputStream.toString();
        assertTrue(output.contains("09:30:00.123456"));
        assertTrue(output.contains("12345"));
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("100"));
        assertTrue(output.contains("150.00"));
        assertTrue(output.contains("BUYER1"));
        assertTrue(output.contains("1001"));
        assertTrue(output.contains("SELLER1"));
        assertTrue(output.contains("2001"));
    }

    @Test
    public void testTradeWithDifferentPrecision() {
        Instrument instrument = mock(Instrument.class);
        when(instrument.asString()).thenReturn("MSFT");
        when(instrument.getPriceFractionDigits()).thenReturn(4);
        when(instrument.getSizeFractionDigits()).thenReturn(2);
        when(instrument.getPriceFactor()).thenReturn(10000.0);
        when(instrument.getSizeFactor()).thenReturn(100.0);

        List<Instrument> instrumentList = new ArrayList<>();
        instrumentList.add(instrument);

        Instruments instruments = mock(Instruments.class);
        when(instruments.iterator()).thenReturn(instrumentList.iterator());
        when(instruments.get("MSFT")).thenReturn(instrument);

        TSVFormat tsvFormat = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "10:45:30.654321";
        trade.matchNumber = 67890L;
        trade.instrument = "MSFT";
        trade.quantity = 25000L;
        trade.price = 3456789L;
        trade.buyer = "BUYER2";
        trade.buyOrderNumber = 3001L;
        trade.seller = "SELLER2";
        trade.sellOrderNumber = 4001L;

        tsvFormat.trade(trade);

        String output = outputStream.toString();
        assertTrue(output.contains("10:45:30.654321"));
        assertTrue(output.contains("67890"));
        assertTrue(output.contains("MSFT"));
        assertTrue(output.contains("250.00"));
        assertTrue(output.contains("345.6789"));
        assertTrue(output.contains("BUYER2"));
        assertTrue(output.contains("3001"));
        assertTrue(output.contains("SELLER2"));
        assertTrue(output.contains("4001"));
    }
}
