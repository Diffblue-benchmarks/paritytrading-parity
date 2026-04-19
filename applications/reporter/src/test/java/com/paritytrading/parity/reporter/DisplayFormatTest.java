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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisplayFormatTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void constructorPrintsHeader() {
        Instruments instruments = mock(Instruments.class);
        when(instruments.getPriceWidth()).thenReturn(10);
        when(instruments.getSizeWidth()).thenReturn(8);

        new DisplayFormat(instruments);

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Inst"));
        assertTrue(output.contains("Quantity"));
        assertTrue(output.contains("Price"));
        assertTrue(output.contains("Buyer"));
        assertTrue(output.contains("Seller"));
    }

    @Test
    void tradeFormatsEvent() {
        Instruments instruments = mock(Instruments.class);
        when(instruments.getPriceWidth()).thenReturn(10);
        when(instruments.getSizeWidth()).thenReturn(8);

        Instrument instrument = mock(Instrument.class);
        when(instrument.getSizeFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFactor()).thenReturn(100.0);
        when(instrument.getPriceFormat()).thenReturn("%10.2f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instruments.get("FOO")).thenReturn(instrument);

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade event = new Trade();
        event.timestamp     = "10:00:00.000";
        event.instrument    = "FOO";
        event.quantity      = 10000;
        event.price         = 5000;
        event.buyer         = "BUYER";
        event.seller        = "SELLER";

        format.trade(event);

        String output = outputStream.toString();
        assertTrue(output.contains("10:00:00.000"));
        assertTrue(output.contains("FOO"));
        assertTrue(output.contains("BUYER"));
        assertTrue(output.contains("SELLER"));
    }

    @Test
    void tradeFormatsNumericValues() {
        Instruments instruments = mock(Instruments.class);
        when(instruments.getPriceWidth()).thenReturn(10);
        when(instruments.getSizeWidth()).thenReturn(8);

        Instrument instrument = mock(Instrument.class);
        when(instrument.getSizeFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFactor()).thenReturn(100.0);
        when(instrument.getPriceFormat()).thenReturn("%10.2f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instruments.get("BAR")).thenReturn(instrument);

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade event = new Trade();
        event.timestamp     = "12:30:00.000";
        event.instrument    = "BAR";
        event.quantity      = 25000;
        event.price         = 15075;
        event.buyer         = "ALICE";
        event.seller        = "BOB";

        format.trade(event);

        String output = outputStream.toString();
        assertTrue(output.contains("250.00"));
        assertTrue(output.contains("150.75"));
    }
}
