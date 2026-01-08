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
package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

class TradeClaudeTest {

    @Test
    void testConstructorWithValidOrderAndExecutedEvent() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 42L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        assertNotNull(trade);
        assertEquals(2000L, trade.getTimestamp());
    }

    @Test
    void testConstructorWithZeroTimestamp() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 42L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 0L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        assertEquals(0L, trade.getTimestamp());
    }

    @Test
    void testConstructorWithMaxTimestamp() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 42L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = Long.MAX_VALUE;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        assertEquals(Long.MAX_VALUE, trade.getTimestamp());
    }

    @Test
    void testConstructorWithNegativeTimestamp() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 42L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = -5000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        assertEquals(-5000L, trade.getTimestamp());
    }

    @Test
    void testConstructorWithDifferentQuantityAndPrice() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.SELL;
        poeAccepted.instrument = ASCII.packLong("BAR");
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 20000L;
        poeAccepted.orderNumber = 42L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 3000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 500L;
        poeExecuted.price = 19500L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeExecuted.matchNumber = 2L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        assertEquals(3000L, trade.getTimestamp());
    }

    @Test
    void testGetTimestampReturnsCorrectValue() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 42L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 123456789L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        assertEquals(123456789L, trade.getTimestamp());
    }

    @Test
    void testGetTimestampIsConsistent() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 42L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 5000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        long timestamp1 = trade.getTimestamp();
        long timestamp2 = trade.getTimestamp();
        assertEquals(timestamp1, timestamp2);
    }

    @Test
    void testFormatWithBasicInstrument() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000000000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000000000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("B"));
        assertTrue(formatted.contains("FOO"));
    }

    @Test
    void testFormatWithSellSide() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  TEST {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000000000L;
        poeAccepted.orderId = "SELL001".getBytes();
        poeAccepted.side = POE.SELL;
        poeAccepted.instrument = ASCII.packLong("TEST");
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 20000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000000000L;
        poeExecuted.orderId = "SELL001".getBytes();
        poeExecuted.quantity = 200L;
        poeExecuted.price = 19500L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("SELL001"));
        assertTrue(formatted.contains("S"));
        assertTrue(formatted.contains("TEST"));
    }

    @Test
    void testFormatIncludesTimestamp() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000000000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 1609459200000000000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
    }

    @Test
    void testFormatWithDifferentPriceFractionDigits() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  ABC {\n" +
            "    price-fraction-digits = 4\n" +
            "    size-fraction-digits = 2\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000000000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("ABC");
        poeAccepted.quantity = 10000L;
        poeAccepted.price = 123456L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000000000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 5000L;
        poeExecuted.price = 123456L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("ABC"));
    }

    @Test
    void testFormatWithZeroQuantityAndPrice() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000000000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000000000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 0L;
        poeExecuted.price = 0L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
    }

    @Test
    void testFormatWithMaxValues() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = Long.MAX_VALUE;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("FOO");
        poeAccepted.quantity = Long.MAX_VALUE;
        poeAccepted.price = Long.MAX_VALUE;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = Long.MAX_VALUE;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = Long.MAX_VALUE;
        poeExecuted.price = Long.MAX_VALUE;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
    }

    @Test
    void testFormatWithMultipleInstruments() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "  BAR {\n" +
            "    price-fraction-digits = 3\n" +
            "    size-fraction-digits = 1\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000000000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("BAR");
        poeAccepted.quantity = 5000L;
        poeAccepted.price = 50000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000000000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 2500L;
        poeExecuted.price = 50000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("BAR"));
    }

    @Test
    void testMultipleTradesAreIndependent() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  FOO {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted1 = new POE.OrderAccepted();
        poeAccepted1.timestamp = 1000L;
        poeAccepted1.orderId = "ORDER1".getBytes();
        poeAccepted1.side = POE.BUY;
        poeAccepted1.instrument = ASCII.packLong("FOO");
        poeAccepted1.quantity = 500L;
        poeAccepted1.price = 10000L;
        poeAccepted1.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent1 = new Event.OrderAccepted(poeAccepted1);
        Order order1 = new Order(acceptedEvent1);

        POE.OrderExecuted poeExecuted1 = new POE.OrderExecuted();
        poeExecuted1.timestamp = 2000L;
        poeExecuted1.orderId = "ORDER1".getBytes();
        poeExecuted1.quantity = 100L;
        poeExecuted1.price = 10000L;
        poeExecuted1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted1.matchNumber = 1L;

        Event.OrderExecuted executedEvent1 = new Event.OrderExecuted(poeExecuted1);
        Trade trade1 = new Trade(order1, executedEvent1);

        POE.OrderAccepted poeAccepted2 = new POE.OrderAccepted();
        poeAccepted2.timestamp = 3000L;
        poeAccepted2.orderId = "ORDER2".getBytes();
        poeAccepted2.side = POE.SELL;
        poeAccepted2.instrument = ASCII.packLong("FOO");
        poeAccepted2.quantity = 1000L;
        poeAccepted2.price = 20000L;
        poeAccepted2.orderNumber = 2L;

        Event.OrderAccepted acceptedEvent2 = new Event.OrderAccepted(poeAccepted2);
        Order order2 = new Order(acceptedEvent2);

        POE.OrderExecuted poeExecuted2 = new POE.OrderExecuted();
        poeExecuted2.timestamp = 4000L;
        poeExecuted2.orderId = "ORDER2".getBytes();
        poeExecuted2.quantity = 200L;
        poeExecuted2.price = 20000L;
        poeExecuted2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted2.matchNumber = 2L;

        Event.OrderExecuted executedEvent2 = new Event.OrderExecuted(poeExecuted2);
        Trade trade2 = new Trade(order2, executedEvent2);

        assertEquals(2000L, trade1.getTimestamp());
        assertEquals(4000L, trade2.getTimestamp());

        String formatted1 = trade1.format(instruments);
        String formatted2 = trade2.format(instruments);

        assertTrue(formatted1.contains("ORDER1"));
        assertTrue(formatted2.contains("ORDER2"));
        assertTrue(formatted1.contains("B"));
        assertTrue(formatted2.contains("S"));
    }

    @Test
    void testFormatWithShortInstrumentName() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  A {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000000000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("A");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000000000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
    }

    @Test
    void testFormatWithLongInstrumentName() {
        String configString = "instruments {\n" +
            "  price-integer-digits = 4\n" +
            "  size-integer-digits = 4\n" +
            "  ABCDEFGH {\n" +
            "    price-fraction-digits = 2\n" +
            "    size-fraction-digits = 0\n" +
            "  }\n" +
            "}";

        Config config = ConfigFactory.parseString(configString);
        Instruments instruments = Instruments.fromConfig(config, "instruments");

        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000000000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = ASCII.packLong("ABCDEFGH");
        poeAccepted.quantity = 500L;
        poeAccepted.price = 10000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000000000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 100L;
        poeExecuted.price = 10000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Trade trade = new Trade(order, executedEvent);

        String formatted = trade.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("ABCDEFGH"));
    }
}
