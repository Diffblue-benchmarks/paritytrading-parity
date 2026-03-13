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

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValueFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InstrumentsTest {

    @Test
    public void testFromConfigBasic() {
        Map<String, Object> instrumentConfig = new HashMap<>();
        instrumentConfig.put("price-fraction-digits", 2);
        instrumentConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", instrumentConfig);
        instrumentsMap.put("price-integer-digits", 5);
        instrumentsMap.put("size-integer-digits", 6);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertNotNull(instruments);
    }

    @Test
    public void testGetByString() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        Instrument found = instruments.get("AAPL");
        assertNotNull(found);
        assertEquals("AAPL", found.asString());
    }

    @Test
    public void testGetByStringNotFound() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        Instrument notFound = instruments.get("GOOG");
        assertNull(notFound);
    }

    @Test
    public void testGetByLong() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        Instrument aapl = instruments.get("AAPL");
        assertNotNull(aapl);

        long aaplLong = aapl.asLong();
        Instrument foundByLong = instruments.get(aaplLong);
        assertNotNull(foundByLong);
        assertEquals("AAPL", foundByLong.asString());
    }

    @Test
    public void testGetByLongNotFound() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        Instrument notFound = instruments.get(999999999L);
        assertNull(notFound);
    }

    @Test
    public void testIterator() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> googConfig = new HashMap<>();
        googConfig.put("price-fraction-digits", 2);
        googConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("GOOG", googConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        Iterator<Instrument> iterator = instruments.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());

        int count = 0;
        while (iterator.hasNext()) {
            Instrument instrument = iterator.next();
            assertNotNull(instrument);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGetMaxPriceFractionDigits() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> googConfig = new HashMap<>();
        googConfig.put("price-fraction-digits", 3);
        googConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("GOOG", googConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertEquals(3, instruments.getMaxPriceFractionDigits());
    }

    @Test
    public void testGetMaxSizeFractionDigits() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 1);

        Map<String, Object> googConfig = new HashMap<>();
        googConfig.put("price-fraction-digits", 2);
        googConfig.put("size-fraction-digits", 4);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("GOOG", googConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertEquals(4, instruments.getMaxSizeFractionDigits());
    }

    @Test
    public void testGetPriceWidth() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("price-integer-digits", 5);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        int expectedWidth = 5 + 1 + 2;
        assertEquals(expectedWidth, instruments.getPriceWidth());
    }

    @Test
    public void testGetSizeWidth() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 3);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("size-integer-digits", 6);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        int expectedWidth = 6 + 1 + 3;
        assertEquals(expectedWidth, instruments.getSizeWidth());
    }

    @Test
    public void testGetPricePlaceholder() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("price-integer-digits", 3);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        String placeholder = instruments.getPricePlaceholder();
        assertNotNull(placeholder);
        assertEquals("   -  ", placeholder);
    }

    @Test
    public void testGetSizePlaceholder() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 1);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("size-integer-digits", 4);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        String placeholder = instruments.getSizePlaceholder();
        assertNotNull(placeholder);
        assertEquals("    - ", placeholder);
    }

    @Test
    public void testGetPricePlaceholderNoFraction() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 0);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("price-integer-digits", 5);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        String placeholder = instruments.getPricePlaceholder();
        assertNotNull(placeholder);
        assertEquals("    -", placeholder);
    }

    @Test
    public void testGetSizePlaceholderNoFraction() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 0);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("size-integer-digits", 7);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        String placeholder = instruments.getSizePlaceholder();
        assertNotNull(placeholder);
        assertEquals("      -", placeholder);
    }

    @Test
    public void testFromConfigWithDefaults() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertNotNull(instruments);
        assertEquals(1, instruments.getPriceWidth() - (2 + 1));
        assertEquals(1, instruments.getSizeWidth());
    }

    @Test
    public void testFromConfigWithCustomIntegerDigits() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("price-integer-digits", 10);
        instrumentsMap.put("size-integer-digits", 8);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertNotNull(instruments);
        assertEquals(13, instruments.getPriceWidth());
        assertEquals(8, instruments.getSizeWidth());
    }

    @Test
    public void testFromConfigWithNestedPath() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("trading", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "trading");

        assertNotNull(instruments);
        Instrument aapl = instruments.get("AAPL");
        assertNotNull(aapl);
    }

    @Test
    public void testPriceWidthWithNoFraction() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 0);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("price-integer-digits", 5);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertEquals(5, instruments.getPriceWidth());
    }

    @Test
    public void testSizeWidthWithNoFraction() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 0);
        aaplConfig.put("size-fraction-digits", 0);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("size-integer-digits", 7);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertEquals(7, instruments.getSizeWidth());
    }

    @Test
    public void testEmptyInstruments() {
        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("price-integer-digits", 5);
        instrumentsMap.put("size-integer-digits", 6);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertNotNull(instruments);
        assertFalse(instruments.iterator().hasNext());
    }

    @Test
    public void testMultipleInstrumentsWithDifferentFractions() {
        Map<String, Object> aaplConfig = new HashMap<>();
        aaplConfig.put("price-fraction-digits", 2);
        aaplConfig.put("size-fraction-digits", 1);

        Map<String, Object> googConfig = new HashMap<>();
        googConfig.put("price-fraction-digits", 4);
        googConfig.put("size-fraction-digits", 3);

        Map<String, Object> msftConfig = new HashMap<>();
        msftConfig.put("price-fraction-digits", 1);
        msftConfig.put("size-fraction-digits", 2);

        Map<String, Object> instrumentsMap = new HashMap<>();
        instrumentsMap.put("AAPL", aaplConfig);
        instrumentsMap.put("GOOG", googConfig);
        instrumentsMap.put("MSFT", msftConfig);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("instruments", instrumentsMap);

        Config config = ConfigFactory.parseMap(rootMap);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        assertEquals(4, instruments.getMaxPriceFractionDigits());
        assertEquals(3, instruments.getMaxSizeFractionDigits());
    }
}
