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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.paritytrading.parity.net.pmr.PMRParser;
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TradeReporterTest {

    private void invokePrivateMain(Config config, boolean tsv) throws Exception {
        Method mainMethod = TradeReporter.class.getDeclaredMethod("main", Config.class, boolean.class);
        mainMethod.setAccessible(true);
        mainMethod.invoke(null, config, tsv);
    }

    @Test
    public void testInstrumentsFromConfig() {
        Config config = createMulticastConfig();
        Instruments instruments = Instruments.fromConfig(config, "instruments");
        assertNotNull(instruments);
        assertNotNull(instruments.get("AAPL"));
    }

    @Test
    public void testTradeProcessorWithDisplayFormat() {
        Config config = createMulticastConfig();
        Instruments instruments = Instruments.fromConfig(config, "instruments");
        DisplayFormat displayFormat = new DisplayFormat(instruments);
        TradeProcessor processor = new TradeProcessor(displayFormat);
        assertNotNull(processor);
    }

    @Test
    public void testTradeProcessorWithTSVFormat() {
        Config config = createMulticastConfig();
        Instruments instruments = Instruments.fromConfig(config, "instruments");
        TSVFormat tsvFormat = new TSVFormat(instruments);
        TradeProcessor processor = new TradeProcessor(tsvFormat);
        assertNotNull(processor);
    }

    @Test
    public void testPMRParserWithTradeProcessor() {
        Config config = createMulticastConfig();
        Instruments instruments = Instruments.fromConfig(config, "instruments");
        TradeProcessor processor = new TradeProcessor(new DisplayFormat(instruments));
        PMRParser parser = new PMRParser(processor);
        assertNotNull(parser);
    }

    @Test
    public void testMulticastConfigHasRequiredPaths() {
        Config config = createMulticastConfig();
        assertTrue(config.hasPath("trade-report.multicast-interface"));
        assertTrue(config.hasPath("trade-report.multicast-group"));
        assertTrue(config.hasPath("trade-report.multicast-port"));
        assertTrue(config.hasPath("trade-report.request-address"));
        assertTrue(config.hasPath("trade-report.request-port"));
    }

    @Test
    public void testSoupBinTCPConfigHasRequiredPaths() {
        Config config = createSoupBinTCPConfig();
        assertFalse(config.hasPath("trade-report.multicast-interface"));
        assertTrue(config.hasPath("trade-report.address"));
        assertTrue(config.hasPath("trade-report.port"));
        assertTrue(config.hasPath("trade-report.username"));
        assertTrue(config.hasPath("trade-report.password"));
    }

    @Test
    public void testConfigValues() {
        Config config = createMulticastConfig();
        assertEquals("eth0", config.getString("trade-report.multicast-interface"));
        assertEquals("224.0.0.1", config.getString("trade-report.multicast-group"));
        assertEquals(5000, config.getInt("trade-report.multicast-port"));

        Config soupConfig = createSoupBinTCPConfig();
        assertEquals("127.0.0.1", soupConfig.getString("trade-report.address"));
        assertEquals(5000, soupConfig.getInt("trade-report.port"));
        assertEquals("user", soupConfig.getString("trade-report.username"));
        assertEquals("pass", soupConfig.getString("trade-report.password"));
    }

    private Config createMulticastConfig() {
        String configString =
            "instruments {\n" +
            "  AAPL {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}\n" +
            "trade-report {\n" +
            "  multicast-interface = eth0\n" +
            "  multicast-group = 224.0.0.1\n" +
            "  multicast-port = 5000\n" +
            "  request-address = 127.0.0.1\n" +
            "  request-port = 5001\n" +
            "}";
        return ConfigFactory.parseString(configString);
    }

    private Config createSoupBinTCPConfig() {
        String configString =
            "instruments {\n" +
            "  AAPL {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}\n" +
            "trade-report {\n" +
            "  address = 127.0.0.1\n" +
            "  port = 5000\n" +
            "  username = user\n" +
            "  password = pass\n" +
            "}";
        return ConfigFactory.parseString(configString);
    }
}
