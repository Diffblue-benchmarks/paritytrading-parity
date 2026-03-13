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

import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DisplayFormatTest {

    @Test
    void testConstructor() {
        Instruments instruments = mock(Instruments.class);
        when(instruments.getPriceWidth()).thenReturn(10);
        when(instruments.getSizeWidth()).thenReturn(8);

        DisplayFormat displayFormat = new DisplayFormat(instruments);

        assertNotNull(displayFormat);
        verify(instruments).getPriceWidth();
        verify(instruments).getSizeWidth();
    }

    @Test
    void testTrade() {
        Instruments instruments = mock(Instruments.class);
        when(instruments.getPriceWidth()).thenReturn(10);
        when(instruments.getSizeWidth()).thenReturn(8);

        Instrument instrument = mock(Instrument.class);
        when(instrument.getSizeFormat()).thenReturn("%8.2f");
        when(instrument.getSizeFactor()).thenReturn(100.0);
        when(instrument.getPriceFormat()).thenReturn("%10.2f");
        when(instrument.getPriceFactor()).thenReturn(10000.0);

        when(instruments.get("AAPL")).thenReturn(instrument);

        DisplayFormat displayFormat = new DisplayFormat(instruments);

        Trade trade = new Trade();
        trade.timestamp = "12:34:56.789";
        trade.instrument = "AAPL";
        trade.quantity = 10000;
        trade.price = 1500000;
        trade.buyer = "BUYER001";
        trade.seller = "SELLER01";

        displayFormat.trade(trade);

        verify(instruments).get("AAPL");
        verify(instrument).getSizeFormat();
        verify(instrument).getSizeFactor();
        verify(instrument).getPriceFormat();
        verify(instrument).getPriceFactor();
    }
}
