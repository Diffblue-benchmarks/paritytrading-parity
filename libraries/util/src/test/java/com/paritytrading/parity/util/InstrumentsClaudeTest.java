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

import com.paritytrading.foundation.ASCII;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class InstrumentsClaudeTest {

    private static final Instruments TEST_INSTRUMENTS = fromString("" +
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
            "}");

    private static final Instruments SINGLE_INSTRUMENT = fromString("" +
            "instruments = {\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits  = 0\n" +
            "  }\n" +
            "}");

    private static final Instruments EMPTY_INSTRUMENTS = fromString("" +
            "instruments = {\n" +
            "}");

    @Test
    void getByStringReturnsInstrument() {
        Instrument foo = TEST_INSTRUMENTS.get("FOO");
        assertNotNull(foo);
        assertEquals("FOO", foo.asString());
    }

    @Test
    void getByStringReturnsCorrectInstrument() {
        Instrument bar = TEST_INSTRUMENTS.get("BAR");
        assertNotNull(bar);
        assertEquals("BAR", bar.asString());
        assertEquals(6, bar.getPriceFractionDigits());
    }

    @Test
    void getByStringReturnsNullForNonexistentInstrument() {
        Instrument result = TEST_INSTRUMENTS.get("NONEXISTENT");
        assertNull(result);
    }

    @Test
    void getByStringReturnsNullForEmptyString() {
        Instrument result = TEST_INSTRUMENTS.get("");
        assertNull(result);
    }

    @Test
    void getByLongReturnsInstrument() {
        long fooLong = ASCII.packLong("FOO");
        Instrument foo = TEST_INSTRUMENTS.get(fooLong);
        assertNotNull(foo);
        assertEquals("FOO", foo.asString());
    }

    @Test
    void getByLongReturnsCorrectInstrument() {
        long barLong = ASCII.packLong("BAR");
        Instrument bar = TEST_INSTRUMENTS.get(barLong);
        assertNotNull(bar);
        assertEquals("BAR", bar.asString());
        assertEquals(8, bar.getSizeFractionDigits());
    }

    @Test
    void getByLongReturnsNullForNonexistentInstrument() {
        long nonexistentLong = ASCII.packLong("NOTFOUND");
        Instrument result = TEST_INSTRUMENTS.get(nonexistentLong);
        assertNull(result);
    }

    @Test
    void getByLongReturnsNullForZero() {
        Instrument result = TEST_INSTRUMENTS.get(0L);
        assertNull(result);
    }

    @Test
    void iteratorReturnsAllInstruments() {
        List<String> instrumentNames = new ArrayList<>();
        for (Instrument instrument : TEST_INSTRUMENTS) {
            instrumentNames.add(instrument.asString());
        }
        assertEquals(2, instrumentNames.size());
        assertTrue(instrumentNames.contains("FOO"));
        assertTrue(instrumentNames.contains("BAR"));
    }

    @Test
    void iteratorWorksWithSingleInstrument() {
        List<String> instrumentNames = new ArrayList<>();
        for (Instrument instrument : SINGLE_INSTRUMENT) {
            instrumentNames.add(instrument.asString());
        }
        assertEquals(1, instrumentNames.size());
        assertTrue(instrumentNames.contains("FOO"));
    }

    @Test
    void iteratorWorksWithEmptyInstruments() {
        Iterator<Instrument> iterator = EMPTY_INSTRUMENTS.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void iteratorCanBeUsedMultipleTimes() {
        int firstCount = 0;
        for (Instrument ignored : TEST_INSTRUMENTS) {
            firstCount++;
        }

        int secondCount = 0;
        for (Instrument ignored : TEST_INSTRUMENTS) {
            secondCount++;
        }

        assertEquals(firstCount, secondCount);
        assertEquals(2, firstCount);
    }

    @Test
    void getMaxPriceFractionDigitsWithSingleInstrument() {
        assertEquals(2, SINGLE_INSTRUMENT.getMaxPriceFractionDigits());
    }

    @Test
    void getMaxSizeFractionDigitsWithSingleInstrument() {
        assertEquals(0, SINGLE_INSTRUMENT.getMaxSizeFractionDigits());
    }

    @Test
    void getMaxPriceFractionDigitsWithEmptyInstruments() {
        assertEquals(0, EMPTY_INSTRUMENTS.getMaxPriceFractionDigits());
    }

    @Test
    void getMaxSizeFractionDigitsWithEmptyInstruments() {
        assertEquals(0, EMPTY_INSTRUMENTS.getMaxSizeFractionDigits());
    }

    @Test
    void getPriceWidthWithDefaults() {
        assertEquals(4, SINGLE_INSTRUMENT.getPriceWidth());
    }

    @Test
    void getSizeWidthWithDefaults() {
        assertEquals(1, SINGLE_INSTRUMENT.getSizeWidth());
    }

    @Test
    void getPricePlaceholderWithDefaults() {
        assertEquals(" -  ", SINGLE_INSTRUMENT.getPricePlaceholder());
    }

    @Test
    void getSizePlaceholderWithDefaults() {
        assertEquals("-", SINGLE_INSTRUMENT.getSizePlaceholder());
    }

    @Test
    void fromConfigWithCustomIntegerDigits() {
        Instruments instruments = fromString("" +
                "instruments = {\n" +
                "  price-integer-digits = 10\n" +
                "  size-integer-digits  = 5\n" +
                "  TEST {\n" +
                "    price-fraction-digits = 3\n" +
                "    size-fraction-digits  = 2\n" +
                "  }\n" +
                "}");
        assertEquals(14, instruments.getPriceWidth());
        assertEquals(8, instruments.getSizeWidth());
    }

    @Test
    void fromConfigWithOnlyRequiredFields() {
        Instruments instruments = fromString("" +
                "instruments = {\n" +
                "  TEST {\n" +
                "    price-fraction-digits = 0\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");
        assertNotNull(instruments);
        assertNotNull(instruments.get("TEST"));
    }

    @Test
    void fromConfigWithZeroFractionDigits() {
        Instruments instruments = fromString("" +
                "instruments = {\n" +
                "  price-integer-digits = 5\n" +
                "  size-integer-digits  = 10\n" +
                "  TEST {\n" +
                "    price-fraction-digits = 0\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");
        assertEquals(5, instruments.getPriceWidth());
        assertEquals(10, instruments.getSizeWidth());
        assertEquals("    -", instruments.getPricePlaceholder());
        assertEquals("         -", instruments.getSizePlaceholder());
    }

    @Test
    void fromConfigWithMultipleInstrumentsDifferentFractionDigits() {
        Instruments instruments = fromString("" +
                "instruments = {\n" +
                "  price-integer-digits = 3\n" +
                "  size-integer-digits  = 6\n" +
                "  A {\n" +
                "    price-fraction-digits = 1\n" +
                "    size-fraction-digits  = 2\n" +
                "  }\n" +
                "  B {\n" +
                "    price-fraction-digits = 4\n" +
                "    size-fraction-digits  = 5\n" +
                "  }\n" +
                "  C {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");
        assertEquals(4, instruments.getMaxPriceFractionDigits());
        assertEquals(5, instruments.getMaxSizeFractionDigits());
        assertEquals(8, instruments.getPriceWidth());
        assertEquals(12, instruments.getSizeWidth());
    }

    private static Instruments fromString(String s) {
        Config config = ConfigFactory.parseString(s);
        return Instruments.fromConfig(config, "instruments");
    }
}
