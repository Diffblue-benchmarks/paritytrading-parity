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
package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class InstrumentTest {

    @Test
    public void testFromConfig() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("TEST.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("TEST.size-fraction-digits")).thenReturn(4);

        Instrument instrument = Instrument.fromConfig(config, "TEST");

        assertNotNull(instrument);
        assertEquals("TEST", instrument.asString());
        assertEquals(2, instrument.getPriceFractionDigits());
        assertEquals(4, instrument.getSizeFractionDigits());
    }

    @Test
    public void testAsString() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("AAPL.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("AAPL.size-fraction-digits")).thenReturn(0);

        Instrument instrument = Instrument.fromConfig(config, "AAPL");

        assertEquals("AAPL", instrument.asString());
    }

    @Test
    public void testAsLong() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("TEST.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("TEST.size-fraction-digits")).thenReturn(0);

        Instrument instrument = Instrument.fromConfig(config, "TEST");

        assertNotNull(instrument.asLong());
    }

    @Test
    public void testGetPriceFractionDigits() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("SPY.price-fraction-digits")).thenReturn(3);
        Mockito.when(config.getInt("SPY.size-fraction-digits")).thenReturn(0);

        Instrument instrument = Instrument.fromConfig(config, "SPY");

        assertEquals(3, instrument.getPriceFractionDigits());
    }

    @Test
    public void testGetSizeFractionDigits() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("IBM.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("IBM.size-fraction-digits")).thenReturn(5);

        Instrument instrument = Instrument.fromConfig(config, "IBM");

        assertEquals(5, instrument.getSizeFractionDigits());
    }

    @Test
    public void testGetPriceFactor() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("MSFT.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("MSFT.size-fraction-digits")).thenReturn(0);

        Instrument instrument = Instrument.fromConfig(config, "MSFT");

        assertEquals(100.0, instrument.getPriceFactor(), 0.001);
    }

    @Test
    public void testGetSizeFactor() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("GOOG.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("GOOG.size-fraction-digits")).thenReturn(3);

        Instrument instrument = Instrument.fromConfig(config, "GOOG");

        assertEquals(1000.0, instrument.getSizeFactor(), 0.001);
    }

    @Test
    public void testGetPriceFormat() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("AMZN.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("AMZN.size-fraction-digits")).thenReturn(0);

        Instrument instrument = Instrument.fromConfig(config, "AMZN");

        assertNotNull(instrument.getPriceFormat());
        assertEquals("%4.2f", instrument.getPriceFormat());
    }

    @Test
    public void testGetSizeFormat() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("TSLA.price-fraction-digits")).thenReturn(0);
        Mockito.when(config.getInt("TSLA.size-fraction-digits")).thenReturn(3);

        Instrument instrument = Instrument.fromConfig(config, "TSLA");

        assertNotNull(instrument.getSizeFormat());
        assertEquals("%5.3f", instrument.getSizeFormat());
    }

    @Test
    public void testSetPriceFormat() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("FB.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("FB.size-fraction-digits")).thenReturn(0);

        Instrument instrument = Instrument.fromConfig(config, "FB");
        instrument.setPriceFormat(2, 3);

        assertEquals("%5.2f ", instrument.getPriceFormat());
    }

    @Test
    public void testSetSizeFormat() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("NFLX.price-fraction-digits")).thenReturn(0);
        Mockito.when(config.getInt("NFLX.size-fraction-digits")).thenReturn(2);

        Instrument instrument = Instrument.fromConfig(config, "NFLX");
        instrument.setSizeFormat(3, 4);

        assertEquals("%6.2f  ", instrument.getSizeFormat());
    }

    @Test
    public void testGetFormatWithZeroFractionDigits() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("ORCL.price-fraction-digits")).thenReturn(0);
        Mockito.when(config.getInt("ORCL.size-fraction-digits")).thenReturn(0);

        Instrument instrument = Instrument.fromConfig(config, "ORCL");

        assertEquals("%1.0f", instrument.getPriceFormat());
        assertEquals("%1.0f", instrument.getSizeFormat());
    }

    @Test
    public void testGetFormatWithNonZeroMaxFractionAndZeroFraction() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("CSCO.price-fraction-digits")).thenReturn(0);
        Mockito.when(config.getInt("CSCO.size-fraction-digits")).thenReturn(0);

        Instrument instrument = Instrument.fromConfig(config, "CSCO");
        instrument.setPriceFormat(1, 3);

        assertEquals("%1.0f    ", instrument.getPriceFormat());
    }

    @Test
    public void testGetFormatWithPadding() {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getInt("INTC.price-fraction-digits")).thenReturn(2);
        Mockito.when(config.getInt("INTC.size-fraction-digits")).thenReturn(1);

        Instrument instrument = Instrument.fromConfig(config, "INTC");
        instrument.setPriceFormat(1, 4);
        instrument.setSizeFormat(2, 3);

        assertEquals("%4.2f  ", instrument.getPriceFormat());
        assertEquals("%4.1f  ", instrument.getSizeFormat());
    }
}
