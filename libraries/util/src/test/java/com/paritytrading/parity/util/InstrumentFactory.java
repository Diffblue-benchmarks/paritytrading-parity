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

import com.diffblue.cover.annotations.InterestingTestFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Factory class for creating Instrument instances for Diffblue Cover tests.
 */
public class InstrumentFactory {

    /**
     * Creates a valid Instrument instance with a simple configuration.
     *
     * @return a valid Instrument instance
     */
    @InterestingTestFactory
    public static Instrument createInstrument() {
        String configString =
                "FOO {\n" +
                "  price-fraction-digits = 2\n" +
                "  size-fraction-digits  = 0\n" +
                "}";

        Config config = ConfigFactory.parseString(configString);
        return Instrument.fromConfig(config, "FOO");
    }

    /**
     * Creates another valid Instrument instance with different configuration values.
     *
     * @return a valid Instrument instance
     */
    @InterestingTestFactory
    public static Instrument createInstrumentWithFractions() {
        String configString =
                "BAR {\n" +
                "  price-fraction-digits = 4\n" +
                "  size-fraction-digits  = 2\n" +
                "}";

        Config config = ConfigFactory.parseString(configString);
        return Instrument.fromConfig(config, "BAR");
    }

    /**
     * Creates a valid Config object for Instrument testing with proper structure.
     *
     * @return a valid Config instance
     */
    @InterestingTestFactory
    public static Config createConfigForInstrument() {
        String configString =
                "TEST_INSTRUMENT {\n" +
                "  price-fraction-digits = 2\n" +
                "  size-fraction-digits  = 4\n" +
                "}\n" +
                "ANOTHER_INSTRUMENT {\n" +
                "  price-fraction-digits = 0\n" +
                "  size-fraction-digits  = 0\n" +
                "}";

        return ConfigFactory.parseString(configString);
    }
}
