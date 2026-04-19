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

import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TSVFormatTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        originalOut  = System.out;
        outputStream = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private Instruments createInstruments() {
        Map<String, Object> instrumentConfig = new HashMap<>();

        Map<String, Object> aapl = new HashMap<>();
        aapl.put("price-fraction-digits", 2);
        aapl.put("size-fraction-digits", 0);
        instrumentConfig.put("AAPL", aapl);

        Config config = ConfigFactory.parseMap(java.util.Collections.singletonMap(
                "instruments", instrumentConfig));

        return Instruments.fromConfig(config, "instruments");
    }

    @Test
    void constructorPrintsHeader() {
        Instruments instruments = createInstruments();

        new TSVFormat(instruments);

        String output = outputStream.toString();

        assertTrue(output.contains("Timestamp\t"));
        assertTrue(output.contains("Match Number\t"));
        assertTrue(output.contains("Instrument\t"));
        assertTrue(output.contains("Quantity\t"));
        assertTrue(output.contains("Price\t"));
        assertTrue(output.contains("Buyer\t"));
        assertTrue(output.contains("Buy Order Number\t"));
        assertTrue(output.contains("Seller\t"));
        assertTrue(output.contains("Sell Order Number\n"));
    }

    @Test
    void constructorBuildsFormats() {
        Instruments instruments = createInstruments();

        TSVFormat format = new TSVFormat(instruments);

        assertNotNull(format);
    }

    @Test
    void tradeFormatsOutput() {
        Instruments instruments = createInstruments();
        TSVFormat   format      = new TSVFormat(instruments);

        outputStream.reset();

        Trade trade       = new Trade();
        trade.timestamp      = "2024-01-15T10:30:00";
        trade.matchNumber    = 42;
        trade.instrument     = "AAPL";
        trade.quantity       = 100;
        trade.price          = 15050;
        trade.buyer          = "BuyerA";
        trade.buyOrderNumber = 1001;
        trade.seller         = "SellerB";
        trade.sellOrderNumber = 2002;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("2024-01-15T10:30:00"));
        assertTrue(output.contains("42"));
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("BuyerA"));
        assertTrue(output.contains("1001"));
        assertTrue(output.contains("SellerB"));
        assertTrue(output.contains("2002"));
    }

    @Test
    void tradeFormatsPriceAndQuantity() {
        Map<String, Object> instrumentConfig = new HashMap<>();

        Map<String, Object> msft = new HashMap<>();
        msft.put("price-fraction-digits", 3);
        msft.put("size-fraction-digits", 2);
        instrumentConfig.put("MSFT", msft);

        Config config = ConfigFactory.parseMap(java.util.Collections.singletonMap(
                "instruments", instrumentConfig));

        Instruments instruments = Instruments.fromConfig(config, "instruments");
        TSVFormat   format      = new TSVFormat(instruments);

        outputStream.reset();

        Trade trade       = new Trade();
        trade.timestamp      = "2024-02-20T14:00:00";
        trade.matchNumber    = 99;
        trade.instrument     = "MSFT";
        trade.quantity       = 5000;
        trade.price          = 123456;
        trade.buyer          = "Buyer1";
        trade.buyOrderNumber = 500;
        trade.seller         = "Seller1";
        trade.sellOrderNumber = 600;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("MSFT"));
        assertTrue(output.contains("50.00"));
        assertTrue(output.contains("123.456"));
    }

    @Test
    void tradeOutputContainsTabs() {
        Instruments instruments = createInstruments();
        TSVFormat   format      = new TSVFormat(instruments);

        outputStream.reset();

        Trade trade       = new Trade();
        trade.timestamp      = "ts";
        trade.matchNumber    = 1;
        trade.instrument     = "AAPL";
        trade.quantity       = 100;
        trade.price          = 100;
        trade.buyer          = "B";
        trade.buyOrderNumber = 10;
        trade.seller         = "S";
        trade.sellOrderNumber = 20;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("\t"));
        assertTrue(output.endsWith("\n"));
    }
}
