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

class DisplayFormatClaudeTest {

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

        new DisplayFormat(instruments);

        String output = outputStream.toString();

        assertTrue(output.contains("Timestamp"), "Output should contain 'Timestamp' header");
        assertTrue(output.contains("Inst"), "Output should contain 'Inst' header");
        assertTrue(output.contains("Quantity"), "Output should contain 'Quantity' header");
        assertTrue(output.contains("Price"), "Output should contain 'Price' header");
        assertTrue(output.contains("Buyer"), "Output should contain 'Buyer' header");
        assertTrue(output.contains("Seller"), "Output should contain 'Seller' header");

        assertTrue(output.contains("---"), "Output should contain separator line with dashes");
        assertTrue(output.startsWith("\n"), "Output should start with newline");
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

        new DisplayFormat(instruments);

        String output = outputStream.toString();

        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Inst"));
        assertTrue(output.contains("Quantity"));
        assertTrue(output.contains("Price"));
        assertTrue(output.contains("Buyer"));
        assertTrue(output.contains("Seller"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "09:30:00.123";
        trade.instrument = "FOO";
        trade.quantity = 1000;
        trade.price = 10050;
        trade.buyer = "BUYER1";
        trade.seller = "SELLER1";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("09:30:00.123"), "Output should contain timestamp");
        assertTrue(output.contains("FOO"), "Output should contain instrument");
        assertTrue(output.contains("1000"), "Output should contain quantity");
        assertTrue(output.contains("100.50"), "Output should contain price formatted with 2 decimal places");
        assertTrue(output.contains("BUYER1"), "Output should contain buyer");
        assertTrue(output.contains("SELLER1"), "Output should contain seller");
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "14:45:30.000";
        trade.instrument = "BAR";
        trade.quantity = 50000;
        trade.price = 9987500;
        trade.buyer = "B2";
        trade.seller = "S2";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("14:45:30.000"));
        assertTrue(output.contains("BAR"));
        assertTrue(output.contains("500.00"));
        assertTrue(output.contains("998.7500"));
        assertTrue(output.contains("B2"));
        assertTrue(output.contains("S2"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "10:00:00.000";
        trade.instrument = "FOO";
        trade.quantity = 0;
        trade.price = 10000;
        trade.buyer = "BUYER";
        trade.seller = "SELLER";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("0"));
        assertTrue(output.contains("100.00"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "10:00:00.000";
        trade.instrument = "FOO";
        trade.quantity = 100;
        trade.price = 0;
        trade.buyer = "BUYER";
        trade.seller = "SELLER";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("100"));
        assertTrue(output.contains("0.00"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "12:00:00.000";
        trade.instrument = "LONGNAME";
        trade.quantity = 500;
        trade.price = 25000;
        trade.buyer = "BUYER";
        trade.seller = "SELLER";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("LONGNAME"));
        assertTrue(output.contains("500"));
        assertTrue(output.contains("250.00"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "15:30:00.000";
        trade.instrument = "FOO";
        trade.quantity = 250;
        trade.price = 15000;
        trade.buyer = "LONGBUYER";
        trade.seller = "LONGSELLER";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("LONGBUYER"));
        assertTrue(output.contains("LONGSELLER"));
        assertTrue(output.contains("250"));
        assertTrue(output.contains("150.00"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade1 = new Trade();
        trade1.timestamp = "09:00:00.000";
        trade1.instrument = "FOO";
        trade1.quantity = 100;
        trade1.price = 10000;
        trade1.buyer = "B1";
        trade1.seller = "S1";

        Trade trade2 = new Trade();
        trade2.timestamp = "09:01:00.000";
        trade2.instrument = "BAR";
        trade2.quantity = 20000;
        trade2.price = 5000000;
        trade2.buyer = "B2";
        trade2.seller = "S2";

        format.trade(trade1);
        format.trade(trade2);

        String output = outputStream.toString();

        assertTrue(output.contains("FOO"));
        assertTrue(output.contains("BAR"));
        assertTrue(output.contains("100"));
        assertTrue(output.contains("200.00"));
        assertTrue(output.contains("100.00"));
        assertTrue(output.contains("500.0000"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "11:00:00.000";
        trade.instrument = "FOO";
        trade.quantity = 1000000;
        trade.price = 10000;
        trade.buyer = "BUYER";
        trade.seller = "SELLER";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("1000000"));
        assertTrue(output.contains("100.00"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "16:00:00.000";
        trade.instrument = "FOO";
        trade.quantity = 100;
        trade.price = 10000000;
        trade.buyer = "BUYER";
        trade.seller = "SELLER";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("100000.00"));
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

        new DisplayFormat(instruments);

        String output = outputStream.toString();

        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Quantity"));
        assertTrue(output.contains("Price"));
        assertTrue(output.startsWith("\n"));
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

        DisplayFormat format = new DisplayFormat(instruments);
        outputStream.reset();

        Trade trade = new Trade();
        trade.timestamp = "13:00:00.000";
        trade.instrument = "CRYPTO";
        trade.quantity = 123456789;
        trade.price = 5000000000L;
        trade.buyer = "BUYER";
        trade.seller = "SELLER";

        format.trade(trade);

        String output = outputStream.toString();

        assertTrue(output.contains("1.23456789"));
        assertTrue(output.contains("50.00000000"));
    }

    private static Instruments createInstruments(String configString) {
        Config config = ConfigFactory.parseString(configString);
        return Instruments.fromConfig(config, "instruments");
    }
}
