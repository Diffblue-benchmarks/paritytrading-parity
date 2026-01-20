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
 * Factory class for creating Instruments instances for Diffblue Cover tests.
 */
public class InstrumentsFactory {

    /**
     * Creates a valid Instruments instance with a simple configuration.
     *
     * @return a valid Instruments instance
     */
    @InterestingTestFactory
    public static Instruments createInstruments() {
        String configString =
                "instruments {\n" +
                "  price-integer-digits = 4\n" +
                "  size-integer-digits  = 8\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}";

        Config config = ConfigFactory.parseString(configString);
        return Instruments.fromConfig(config, "instruments");
    }

    /**
     * Creates a valid Config object for Instruments testing with proper structure.
     *
     * @return a valid Config instance
     */
    @InterestingTestFactory
    public static Config createConfigForInstruments() {
        String configString =
                "instruments {\n" +
                "  price-integer-digits = 4\n" +
                "  size-integer-digits  = 8\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "  BAR {\n" +
                "    price-fraction-digits = 4\n" +
                "    size-fraction-digits  = 2\n" +
                "  }\n" +
                "}";

        return ConfigFactory.parseString(configString);
    }
}
