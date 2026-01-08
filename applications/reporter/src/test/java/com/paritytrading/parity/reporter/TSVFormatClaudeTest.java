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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TSVFormatClaudeTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void constructorPrintsHeaderWithSingleInstrument() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  price-integer-digits = 4\n" +
                "  size-integer-digits  = 8\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        new TSVFormat(instruments);

        String output = outputStream.toString();

        assertTrue(output.contains("Timestamp\t"), "Output should contain 'Timestamp' header");
        assertTrue(output.contains("Match Number\t"), "Output should contain 'Match Number' header");
        assertTrue(output.contains("Instrument\t"), "Output should contain 'Instrument' header");
        assertTrue(output.contains("Quantity\t"), "Output should contain 'Quantity' header");
        assertTrue(output.contains("Price\t"), "Output should contain 'Price' header");
        assertTrue(output.contains("Buyer\t"), "Output should contain 'Buyer' header");
        assertTrue(output.contains("Buy Order Number\t"), "Output should contain 'Buy Order Number' header");
        assertTrue(output.contains("Seller\t"), "Output should contain 'Seller' header");
        assertTrue(output.contains("Sell Order Number\n"), "Output should contain 'Sell Order Number' header");
        assertEquals("Timestamp\tMatch Number\tInstrument\tQuantity\tPrice\tBuyer\tBuy Order Number\tSeller\tSell Order Number\n", output);
    }

    @Test
    void constructorPrintsHeaderWithMultipleInstruments() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
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
                "}");

        new TSVFormat(instruments);

        String output = outputStream.toString();

        assertTrue(output.contains("Timestamp\t"));
        assertTrue(output.contains("Match Number\t"));
        assertTrue(output.contains("Instrument\t"));
        assertTrue(output.contains("Quantity\t"));
        assertTrue(output.contains("Price\t"));
        assertTrue(output.contains("Buyer\t"));
        assertTrue(output.contains("Seller\t"));
    }

    @Test
    void constructorCreatesFormatStringsForEachInstrument() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
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
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade1 = new Trade();
        trade1.timestamp = "09:00:00.000";
        trade1.matchNumber = 1;
        trade1.instrument = "FOO";
        trade1.quantity = 100;
        trade1.price = 10000;
        trade1.buyer = "B1";
        trade1.buyOrderNumber = 100;
        trade1.seller = "S1";
        trade1.sellOrderNumber = 200;

        format.trade(trade1);

        String output = outputStream.toString();
        assertTrue(output.contains("100\t"));
        assertTrue(output.contains("100.00\t"));
    }

    @Test
    void tradePrintsFormattedTradeWithIntegerSize() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  price-integer-digits = 4\n" +
                "  size-integer-digits  = 8\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "09:30:00.123";
        trade.matchNumber = 12345;
        trade.instrument = "FOO";
        trade.quantity = 1000;
        trade.price = 10050;
        trade.buyer = "BUYER1";
        trade.buyOrderNumber = 9876;
        trade.seller = "SELLER1";
        trade.sellOrderNumber = 5432;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("09:30:00.123\t"), "Output should contain timestamp");
        assertTrue(output.contains("12345\t"), "Output should contain match number");
        assertTrue(output.contains("FOO\t"), "Output should contain instrument");
        assertTrue(output.contains("1000\t"), "Output should contain quantity");
        assertTrue(output.contains("100.50\t"), "Output should contain price formatted with 2 decimal places");
        assertTrue(output.contains("BUYER1\t"), "Output should contain buyer");
        assertTrue(output.contains("9876\t"), "Output should contain buy order number");
        assertTrue(output.contains("SELLER1\t"), "Output should contain seller");
        assertTrue(output.contains("5432\n"), "Output should contain sell order number");
    }

    @Test
    void tradePrintsFormattedTradeWithFractionalSize() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  price-integer-digits = 4\n" +
                "  size-integer-digits  = 8\n" +
                "  BAR {\n" +
                "    price-fraction-digits = 4\n" +
                "    size-fraction-digits  = 2\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "14:45:30.000";
        trade.matchNumber = 98765;
        trade.instrument = "BAR";
        trade.quantity = 50000;
        trade.price = 9987500;
        trade.buyer = "B2";
        trade.buyOrderNumber = 111;
        trade.seller = "S2";
        trade.sellOrderNumber = 222;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("14:45:30.000\t"));
        assertTrue(output.contains("98765\t"));
        assertTrue(output.contains("BAR\t"));
        assertTrue(output.contains("500.00\t"));
        assertTrue(output.contains("998.7500\t"));
        assertTrue(output.contains("B2\t"));
        assertTrue(output.contains("111\t"));
        assertTrue(output.contains("S2\t"));
        assertTrue(output.contains("222\n"));
    }

    @Test
    void tradeWithZeroQuantity() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "10:00:00.000";
        trade.matchNumber = 1;
        trade.instrument = "FOO";
        trade.quantity = 0;
        trade.price = 10000;
        trade.buyer = "BUYER";
        trade.buyOrderNumber = 1;
        trade.seller = "SELLER";
        trade.sellOrderNumber = 2;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("0\t"));
        assertTrue(output.contains("100.00\t"));
    }

    @Test
    void tradeWithZeroPrice() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "10:00:00.000";
        trade.matchNumber = 2;
        trade.instrument = "FOO";
        trade.quantity = 100;
        trade.price = 0;
        trade.buyer = "BUYER";
        trade.buyOrderNumber = 3;
        trade.seller = "SELLER";
        trade.sellOrderNumber = 4;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("100\t"));
        assertTrue(output.contains("0.00\t"));
    }

    @Test
    void tradeWithLongInstrumentName() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  LONGNAME {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "12:00:00.000";
        trade.matchNumber = 555;
        trade.instrument = "LONGNAME";
        trade.quantity = 500;
        trade.price = 25000;
        trade.buyer = "BUYER";
        trade.buyOrderNumber = 10;
        trade.seller = "SELLER";
        trade.sellOrderNumber = 20;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("LONGNAME\t"));
        assertTrue(output.contains("500\t"));
        assertTrue(output.contains("250.00\t"));
    }

    @Test
    void tradeWithLongBuyerAndSellerNames() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "15:30:00.000";
        trade.matchNumber = 789;
        trade.instrument = "FOO";
        trade.quantity = 250;
        trade.price = 15000;
        trade.buyer = "LONGBUYER";
        trade.buyOrderNumber = 123;
        trade.seller = "LONGSELLER";
        trade.sellOrderNumber = 456;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("LONGBUYER\t"));
        assertTrue(output.contains("LONGSELLER\t"));
        assertTrue(output.contains("250\t"));
        assertTrue(output.contains("150.00\t"));
    }

    @Test
    void multipleTradesWithDifferentInstruments() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
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
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade1 = new Trade();
        trade1.timestamp = "09:00:00.000";
        trade1.matchNumber = 100;
        trade1.instrument = "FOO";
        trade1.quantity = 100;
        trade1.price = 10000;
        trade1.buyer = "B1";
        trade1.buyOrderNumber = 1;
        trade1.seller = "S1";
        trade1.sellOrderNumber = 2;

        Trade trade2 = new Trade();
        trade2.timestamp = "09:01:00.000";
        trade2.matchNumber = 101;
        trade2.instrument = "BAR";
        trade2.quantity = 20000;
        trade2.price = 5000000;
        trade2.buyer = "B2";
        trade2.buyOrderNumber = 3;
        trade2.seller = "S2";
        trade2.sellOrderNumber = 4;

        format.trade(trade1);
        format.trade(trade2);

        String output = outputStream.toString();

        assertTrue(output.contains("FOO\t"));
        assertTrue(output.contains("BAR\t"));
        assertTrue(output.contains("100\t"));
        assertTrue(output.contains("100.00\t"));
        assertTrue(output.contains("200.00\t"));
        assertTrue(output.contains("500.0000\t"));
    }

    @Test
    void tradeWithVeryLargeQuantity() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "11:00:00.000";
        trade.matchNumber = 9999;
        trade.instrument = "FOO";
        trade.quantity = 1000000;
        trade.price = 10000;
        trade.buyer = "BUYER";
        trade.buyOrderNumber = 99;
        trade.seller = "SELLER";
        trade.sellOrderNumber = 88;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("1000000\t"));
        assertTrue(output.contains("100.00\t"));
    }

    @Test
    void tradeWithVeryLargePrice() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "16:00:00.000";
        trade.matchNumber = 8888;
        trade.instrument = "FOO";
        trade.quantity = 100;
        trade.price = 10000000;
        trade.buyer = "BUYER";
        trade.buyOrderNumber = 77;
        trade.seller = "SELLER";
        trade.sellOrderNumber = 66;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("100000.00\t"));
    }

    @Test
    void constructorWithHighPrecisionInstruments() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  price-integer-digits = 8\n" +
                "  size-integer-digits  = 12\n" +
                "  CRYPTO {\n" +
                "    price-fraction-digits = 8\n" +
                "    size-fraction-digits  = 8\n" +
                "  }\n" +
                "}");

        new TSVFormat(instruments);

        String output = outputStream.toString();

        assertTrue(output.contains("Timestamp\t"));
        assertTrue(output.contains("Match Number\t"));
        assertTrue(output.contains("Quantity\t"));
        assertTrue(output.contains("Price\t"));
    }

    @Test
    void tradeWithHighPrecisionValues() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  price-integer-digits = 8\n" +
                "  size-integer-digits  = 12\n" +
                "  CRYPTO {\n" +
                "    price-fraction-digits = 8\n" +
                "    size-fraction-digits  = 8\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "13:00:00.000";
        trade.matchNumber = 7777;
        trade.instrument = "CRYPTO";
        trade.quantity = 123456789;
        trade.price = 5000000000L;
        trade.buyer = "BUYER";
        trade.buyOrderNumber = 55;
        trade.seller = "SELLER";
        trade.sellOrderNumber = 44;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("1.23456789\t"));
        assertTrue(output.contains("50.00000000\t"));
    }

    @Test
    void tradeWithLargeOrderNumbers() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "17:00:00.000";
        trade.matchNumber = Long.MAX_VALUE;
        trade.instrument = "FOO";
        trade.quantity = 100;
        trade.price = 10000;
        trade.buyer = "BUYER";
        trade.buyOrderNumber = Long.MAX_VALUE - 1;
        trade.seller = "SELLER";
        trade.sellOrderNumber = Long.MAX_VALUE - 2;

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains(String.valueOf(Long.MAX_VALUE)));
        assertTrue(output.contains(String.valueOf(Long.MAX_VALUE - 1)));
        assertTrue(output.contains(String.valueOf(Long.MAX_VALUE - 2)));
    }

    @Test
    void tradeWithDifferentPrecisionFormats() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  price-integer-digits = 4\n" +
                "  size-integer-digits  = 8\n" +
                "  INST1 {\n" +
                "    price-fraction-digits = 0\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "  INST2 {\n" +
                "    price-fraction-digits = 1\n" +
                "    size-fraction-digits  = 1\n" +
                "  }\n" +
                "  INST3 {\n" +
                "    price-fraction-digits = 3\n" +
                "    size-fraction-digits  = 3\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade1 = new Trade();
        trade1.timestamp = "10:00:00.000";
        trade1.matchNumber = 1;
        trade1.instrument = "INST1";
        trade1.quantity = 100;
        trade1.price = 100;
        trade1.buyer = "B1";
        trade1.buyOrderNumber = 1;
        trade1.seller = "S1";
        trade1.sellOrderNumber = 2;

        Trade trade2 = new Trade();
        trade2.timestamp = "10:01:00.000";
        trade2.matchNumber = 2;
        trade2.instrument = "INST2";
        trade2.quantity = 100;
        trade2.price = 100;
        trade2.buyer = "B2";
        trade2.buyOrderNumber = 3;
        trade2.seller = "S2";
        trade2.sellOrderNumber = 4;

        Trade trade3 = new Trade();
        trade3.timestamp = "10:02:00.000";
        trade3.matchNumber = 3;
        trade3.instrument = "INST3";
        trade3.quantity = 1000;
        trade3.price = 1000;
        trade3.buyer = "B3";
        trade3.buyOrderNumber = 5;
        trade3.seller = "S3";
        trade3.sellOrderNumber = 6;

        format.trade(trade1);
        format.trade(trade2);
        format.trade(trade3);

        String output = outputStream.toString();

        assertTrue(output.contains("100\t100\t"));
        assertTrue(output.contains("10.0\t10.0\t"));
        assertTrue(output.contains("1.000\t1.000\t"));
    }

    @Test
    void tradeOutputFormatIsTabDelimited() {
        Instruments instruments = createInstruments("" +
                "instruments = {\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits  = 0\n" +
                "  }\n" +
                "}");

        TSVFormat format = new TSVFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "10:00:00.000";
        trade.matchNumber = 123;
        trade.instrument = "FOO";
        trade.quantity = 100;
        trade.price = 10000;
        trade.buyer = "BUYER";
        trade.buyOrderNumber = 1;
        trade.seller = "SELLER";
        trade.sellOrderNumber = 2;

        format.trade(trade);

        String output = outputStream.toString();

        long tabCount = output.chars().filter(ch -> ch == '\t').count();
        assertEquals(8, tabCount, "Output should contain exactly 8 tab characters");
    }

    private static Instruments createInstruments(String configString) {
        Config config = ConfigFactory.parseString(configString);
        return Instruments.fromConfig(config, "instruments");
    }
}
