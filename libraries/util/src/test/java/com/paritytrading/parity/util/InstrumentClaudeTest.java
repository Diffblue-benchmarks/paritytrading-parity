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

import static org.junit.jupiter.api.Assertions.*;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

class InstrumentClaudeTest {

    @Test
    void asStringReturnsInstrumentName() {
        Instrument instrument = createInstrument("AAPL", 2, 0);
        assertEquals("AAPL", instrument.asString());
    }

    @Test
    void asStringReturnsInstrumentNameWithLongerString() {
        Instrument instrument = createInstrument("TESTLONG", 4, 2);
        assertEquals("TESTLONG", instrument.asString());
    }

    @Test
    void asLongReturnsPackedLongValue() {
        Instrument instrument = createInstrument("FOO", 2, 0);
        // The asLong value is created by ASCII.packLong, we test that it's consistent
        long value = instrument.asLong();
        assertTrue(value != 0);

        // Create another instrument with same name and verify same long value
        Instrument instrument2 = createInstrument("FOO", 3, 1);
        assertEquals(value, instrument2.asLong());
    }

    @Test
    void asLongReturnsDifferentValuesForDifferentNames() {
        Instrument instrument1 = createInstrument("FOO", 2, 0);
        Instrument instrument2 = createInstrument("BAR", 2, 0);
        assertNotEquals(instrument1.asLong(), instrument2.asLong());
    }

    @Test
    void getPriceFractionDigitsReturnsZero() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        assertEquals(0, instrument.getPriceFractionDigits());
    }

    @Test
    void getPriceFractionDigitsReturnsTwo() {
        Instrument instrument = createInstrument("TEST", 2, 0);
        assertEquals(2, instrument.getPriceFractionDigits());
    }

    @Test
    void getPriceFractionDigitsReturnsLargeValue() {
        Instrument instrument = createInstrument("TEST", 8, 0);
        assertEquals(8, instrument.getPriceFractionDigits());
    }

    @Test
    void getSizeFractionDigitsReturnsZero() {
        Instrument instrument = createInstrument("TEST", 2, 0);
        assertEquals(0, instrument.getSizeFractionDigits());
    }

    @Test
    void getSizeFractionDigitsReturnsTwo() {
        Instrument instrument = createInstrument("TEST", 0, 2);
        assertEquals(2, instrument.getSizeFractionDigits());
    }

    @Test
    void getSizeFractionDigitsReturnsLargeValue() {
        Instrument instrument = createInstrument("TEST", 0, 8);
        assertEquals(8, instrument.getSizeFractionDigits());
    }

    @Test
    void getPriceFactorReturnsOneForZeroDigits() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        assertEquals(1.0, instrument.getPriceFactor(), 0.0);
    }

    @Test
    void getPriceFactorReturnsHundredForTwoDigits() {
        Instrument instrument = createInstrument("TEST", 2, 0);
        assertEquals(100.0, instrument.getPriceFactor(), 0.0);
    }

    @Test
    void getPriceFactorReturnsThousandForThreeDigits() {
        Instrument instrument = createInstrument("TEST", 3, 0);
        assertEquals(1000.0, instrument.getPriceFactor(), 0.0);
    }

    @Test
    void getPriceFactorReturnsLargePowerOfTen() {
        Instrument instrument = createInstrument("TEST", 6, 0);
        assertEquals(1000000.0, instrument.getPriceFactor(), 0.0);
    }

    @Test
    void getSizeFactorReturnsOneForZeroDigits() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        assertEquals(1.0, instrument.getSizeFactor(), 0.0);
    }

    @Test
    void getSizeFactorReturnsHundredForTwoDigits() {
        Instrument instrument = createInstrument("TEST", 0, 2);
        assertEquals(100.0, instrument.getSizeFactor(), 0.0);
    }

    @Test
    void getSizeFactorReturnsTenThousandForFourDigits() {
        Instrument instrument = createInstrument("TEST", 0, 4);
        assertEquals(10000.0, instrument.getSizeFactor(), 0.0);
    }

    @Test
    void getSizeFactorReturnsLargePowerOfTen() {
        Instrument instrument = createInstrument("TEST", 0, 8);
        assertEquals(100000000.0, instrument.getSizeFactor(), 0.0);
    }

    @Test
    void getPriceFormatWithZeroFractionDigits() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        // Default format: setPriceFormat(1, 0) is called in constructor
        assertEquals("%1.0f", instrument.getPriceFormat());
    }

    @Test
    void getPriceFormatWithTwoFractionDigits() {
        Instrument instrument = createInstrument("TEST", 2, 0);
        // Default format: setPriceFormat(1, 2) is called in constructor
        assertEquals("%4.2f", instrument.getPriceFormat());
    }

    @Test
    void getPriceFormatWithSixFractionDigits() {
        Instrument instrument = createInstrument("TEST", 6, 0);
        // Default format: setPriceFormat(1, 6) is called in constructor
        assertEquals("%8.6f", instrument.getPriceFormat());
    }

    @Test
    void getSizeFormatWithZeroFractionDigits() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        // Default format: setSizeFormat(1, 0) is called in constructor
        assertEquals("%1.0f", instrument.getSizeFormat());
    }

    @Test
    void getSizeFormatWithTwoFractionDigits() {
        Instrument instrument = createInstrument("TEST", 0, 2);
        // Default format: setSizeFormat(1, 2) is called in constructor
        assertEquals("%4.2f", instrument.getSizeFormat());
    }

    @Test
    void getSizeFormatWithEightFractionDigits() {
        Instrument instrument = createInstrument("TEST", 0, 8);
        // Default format: setSizeFormat(1, 8) is called in constructor
        assertEquals("%10.8f", instrument.getSizeFormat());
    }

    @Test
    void setPriceFormatWithLargerMaxFractionDigits() {
        Instrument instrument = createInstrument("TEST", 2, 0);
        instrument.setPriceFormat(3, 5);
        // width = 3 + 1 + 2 = 6, padding = 5 - 2 = 3
        assertEquals("%6.2f   ", instrument.getPriceFormat());
    }

    @Test
    void setPriceFormatWithEqualMaxFractionDigits() {
        Instrument instrument = createInstrument("TEST", 4, 0);
        instrument.setPriceFormat(2, 4);
        // width = 2 + 1 + 4 = 7, padding = 4 - 4 = 0
        assertEquals("%7.4f", instrument.getPriceFormat());
    }

    @Test
    void setPriceFormatWithZeroFractionDigitsAndNonZeroMax() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        instrument.setPriceFormat(2, 4);
        // width = 2 + 0 + 0 = 2, padding = 4 - 0 + 1 = 5
        assertEquals("%2.0f     ", instrument.getPriceFormat());
    }

    @Test
    void setPriceFormatWithZeroFractionDigitsAndZeroMax() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        instrument.setPriceFormat(5, 0);
        // width = 5 + 0 + 0 = 5, padding = 0
        assertEquals("%5.0f", instrument.getPriceFormat());
    }

    @Test
    void setPriceFormatWithMatchingMaxFractionDigits() {
        Instrument instrument = createInstrument("TEST", 3, 0);
        instrument.setPriceFormat(4, 3);
        // width = 4 + 1 + 3 = 8, padding = 3 - 3 = 0
        assertEquals("%8.3f", instrument.getPriceFormat());
    }

    @Test
    void setSizeFormatWithLargerMaxFractionDigits() {
        Instrument instrument = createInstrument("TEST", 0, 2);
        instrument.setSizeFormat(5, 6);
        // width = 5 + 1 + 2 = 8, padding = 6 - 2 = 4
        assertEquals("%8.2f    ", instrument.getSizeFormat());
    }

    @Test
    void setSizeFormatWithEqualMaxFractionDigits() {
        Instrument instrument = createInstrument("TEST", 0, 5);
        instrument.setSizeFormat(3, 5);
        // width = 3 + 1 + 5 = 9, padding = 5 - 5 = 0
        assertEquals("%9.5f", instrument.getSizeFormat());
    }

    @Test
    void setSizeFormatWithZeroFractionDigitsAndNonZeroMax() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        instrument.setSizeFormat(4, 3);
        // width = 4 + 0 + 0 = 4, padding = 3 - 0 + 1 = 4
        assertEquals("%4.0f    ", instrument.getSizeFormat());
    }

    @Test
    void setSizeFormatWithZeroFractionDigitsAndZeroMax() {
        Instrument instrument = createInstrument("TEST", 0, 0);
        instrument.setSizeFormat(3, 0);
        // width = 3 + 0 + 0 = 3, padding = 0
        assertEquals("%3.0f", instrument.getSizeFormat());
    }

    @Test
    void setSizeFormatWithMatchingMaxFractionDigits() {
        Instrument instrument = createInstrument("TEST", 0, 4);
        instrument.setSizeFormat(6, 4);
        // width = 6 + 1 + 4 = 11, padding = 4 - 4 = 0
        assertEquals("%11.4f", instrument.getSizeFormat());
    }

    @Test
    void fromConfigCreatesInstrumentWithCorrectName() {
        String configString = "FOO {\n" +
                "  price-fraction-digits = 2\n" +
                "  size-fraction-digits = 0\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);

        Instrument instrument = Instrument.fromConfig(config, "FOO");

        assertEquals("FOO", instrument.asString());
    }

    @Test
    void fromConfigCreatesInstrumentWithCorrectPriceFractionDigits() {
        String configString = "BAR {\n" +
                "  price-fraction-digits = 4\n" +
                "  size-fraction-digits = 2\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);

        Instrument instrument = Instrument.fromConfig(config, "BAR");

        assertEquals(4, instrument.getPriceFractionDigits());
    }

    @Test
    void fromConfigCreatesInstrumentWithCorrectSizeFractionDigits() {
        String configString = "BAZ {\n" +
                "  price-fraction-digits = 0\n" +
                "  size-fraction-digits = 8\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);

        Instrument instrument = Instrument.fromConfig(config, "BAZ");

        assertEquals(8, instrument.getSizeFractionDigits());
    }

    @Test
    void fromConfigCreatesInstrumentWithZeroFractionDigits() {
        String configString = "ZERO {\n" +
                "  price-fraction-digits = 0\n" +
                "  size-fraction-digits = 0\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);

        Instrument instrument = Instrument.fromConfig(config, "ZERO");

        assertEquals(0, instrument.getPriceFractionDigits());
        assertEquals(0, instrument.getSizeFractionDigits());
    }

    @Test
    void fromConfigCreatesInstrumentWithLargeFractionDigits() {
        String configString = "LARGE {\n" +
                "  price-fraction-digits = 9\n" +
                "  size-fraction-digits = 9\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);

        Instrument instrument = Instrument.fromConfig(config, "LARGE");

        assertEquals(9, instrument.getPriceFractionDigits());
        assertEquals(9, instrument.getSizeFractionDigits());
    }

    @Test
    void fromConfigInitializesFormatsCorrectly() {
        String configString = "TEST {\n" +
                "  price-fraction-digits = 3\n" +
                "  size-fraction-digits = 1\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);

        Instrument instrument = Instrument.fromConfig(config, "TEST");

        // Default format: setPriceFormat(1, 3) and setSizeFormat(1, 1)
        assertEquals("%5.3f", instrument.getPriceFormat());
        assertEquals("%3.1f", instrument.getSizeFormat());
    }

    @Test
    void fromConfigCalculatesPriceFactorCorrectly() {
        String configString = "FACTOR {\n" +
                "  price-fraction-digits = 5\n" +
                "  size-fraction-digits = 0\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);

        Instrument instrument = Instrument.fromConfig(config, "FACTOR");

        assertEquals(100000.0, instrument.getPriceFactor(), 0.0);
    }

    @Test
    void fromConfigCalculatesSizeFactorCorrectly() {
        String configString = "FACTOR {\n" +
                "  price-fraction-digits = 0\n" +
                "  size-fraction-digits = 6\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);

        Instrument instrument = Instrument.fromConfig(config, "FACTOR");

        assertEquals(1000000.0, instrument.getSizeFactor(), 0.0);
    }

    // Helper method to create an Instrument using fromConfig
    private Instrument createInstrument(String name, int priceFractionDigits, int sizeFractionDigits) {
        String configString = name + " {\n" +
                "  price-fraction-digits = " + priceFractionDigits + "\n" +
                "  size-fraction-digits = " + sizeFractionDigits + "\n" +
                "}";
        Config config = ConfigFactory.parseString(configString);
        return Instrument.fromConfig(config, name);
    }
}
