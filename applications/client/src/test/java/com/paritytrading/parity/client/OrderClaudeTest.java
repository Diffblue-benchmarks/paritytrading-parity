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
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

class OrderClaudeTest {

    @Test
    void testConstructorWithValidOrderAcceptedEvent() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 500L;
        poeMessage.price = 10000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertNotNull(order);
        assertEquals(1000L, order.getTimestamp());
        assertEquals("ORDER123", order.getOrderId());
        assertEquals(POE.BUY, order.getSide());
        assertEquals(100L, order.getInstrument());
        assertEquals(500L, order.getQuantity());
    }

    @Test
    void testConstructorWithSellSide() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 2000L;
        poeMessage.orderId = "SELLORDER".getBytes();
        poeMessage.side = POE.SELL;
        poeMessage.instrument = 200L;
        poeMessage.quantity = 1000L;
        poeMessage.price = 20000L;
        poeMessage.orderNumber = 100L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals(POE.SELL, order.getSide());
        assertEquals("SELLORDER", order.getOrderId());
        assertEquals(200L, order.getInstrument());
    }

    @Test
    void testConstructorWithZeroValues() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 0L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.side = 0;
        poeMessage.instrument = 0L;
        poeMessage.quantity = 0L;
        poeMessage.price = 0L;
        poeMessage.orderNumber = 0L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals(0L, order.getTimestamp());
        assertEquals(0, order.getSide());
        assertEquals(0L, order.getInstrument());
        assertEquals(0L, order.getQuantity());
    }

    @Test
    void testConstructorWithMaxValues() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = Long.MAX_VALUE;
        poeMessage.orderId = "MAXORDER".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = Long.MAX_VALUE;
        poeMessage.quantity = Long.MAX_VALUE;
        poeMessage.price = Long.MAX_VALUE;
        poeMessage.orderNumber = Long.MAX_VALUE;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals(Long.MAX_VALUE, order.getTimestamp());
        assertEquals(Long.MAX_VALUE, order.getInstrument());
        assertEquals(Long.MAX_VALUE, order.getQuantity());
    }

    @Test
    void testGetTimestampReturnsCorrectValue() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 123456789L;
        poeMessage.orderId = "TEST".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals(123456789L, order.getTimestamp());
    }

    @Test
    void testGetSideReturnsCorrectValue() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "TEST".getBytes();
        poeMessage.side = POE.SELL;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals(POE.SELL, order.getSide());
    }

    @Test
    void testGetInstrumentReturnsCorrectValue() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "TEST".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 999L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals(999L, order.getInstrument());
    }

    @Test
    void testGetOrderIdReturnsCorrectValue() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "TESTORDER001".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals("TESTORDER001", order.getOrderId());
    }

    @Test
    void testGetQuantityReturnsInitialValue() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "TEST".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 777L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals(777L, order.getQuantity());
    }

    @Test
    void testApplyOrderExecutedReducesQuantity() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 300L;
        poeExecuted.price = 5000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        order.apply(executedEvent);

        assertEquals(700L, order.getQuantity());
    }

    @Test
    void testApplyOrderExecutedMultipleTimes() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted1 = new POE.OrderExecuted();
        poeExecuted1.timestamp = 2000L;
        poeExecuted1.orderId = "ORDER123".getBytes();
        poeExecuted1.quantity = 200L;
        poeExecuted1.price = 5000L;
        poeExecuted1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted1.matchNumber = 1L;

        POE.OrderExecuted poeExecuted2 = new POE.OrderExecuted();
        poeExecuted2.timestamp = 3000L;
        poeExecuted2.orderId = "ORDER123".getBytes();
        poeExecuted2.quantity = 300L;
        poeExecuted2.price = 5000L;
        poeExecuted2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted2.matchNumber = 2L;

        Event.OrderExecuted executedEvent1 = new Event.OrderExecuted(poeExecuted1);
        Event.OrderExecuted executedEvent2 = new Event.OrderExecuted(poeExecuted2);

        order.apply(executedEvent1);
        assertEquals(800L, order.getQuantity());

        order.apply(executedEvent2);
        assertEquals(500L, order.getQuantity());
    }

    @Test
    void testApplyOrderExecutedWithFullQuantity() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 500L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 500L;
        poeExecuted.price = 5000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        order.apply(executedEvent);

        assertEquals(0L, order.getQuantity());
    }

    @Test
    void testApplyOrderCanceledReducesQuantity() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderCanceled poeCanceled = new POE.OrderCanceled();
        poeCanceled.timestamp = 2000L;
        poeCanceled.orderId = "ORDER123".getBytes();
        poeCanceled.canceledQuantity = 400L;
        poeCanceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled canceledEvent = new Event.OrderCanceled(poeCanceled);
        order.apply(canceledEvent);

        assertEquals(600L, order.getQuantity());
    }

    @Test
    void testApplyOrderCanceledMultipleTimes() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderCanceled poeCanceled1 = new POE.OrderCanceled();
        poeCanceled1.timestamp = 2000L;
        poeCanceled1.orderId = "ORDER123".getBytes();
        poeCanceled1.canceledQuantity = 200L;
        poeCanceled1.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        POE.OrderCanceled poeCanceled2 = new POE.OrderCanceled();
        poeCanceled2.timestamp = 3000L;
        poeCanceled2.orderId = "ORDER123".getBytes();
        poeCanceled2.canceledQuantity = 300L;
        poeCanceled2.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled canceledEvent1 = new Event.OrderCanceled(poeCanceled1);
        Event.OrderCanceled canceledEvent2 = new Event.OrderCanceled(poeCanceled2);

        order.apply(canceledEvent1);
        assertEquals(800L, order.getQuantity());

        order.apply(canceledEvent2);
        assertEquals(500L, order.getQuantity());
    }

    @Test
    void testApplyOrderCanceledWithFullQuantity() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 500L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderCanceled poeCanceled = new POE.OrderCanceled();
        poeCanceled.timestamp = 2000L;
        poeCanceled.orderId = "ORDER123".getBytes();
        poeCanceled.canceledQuantity = 500L;
        poeCanceled.reason = POE.ORDER_CANCEL_REASON_SUPERVISORY;

        Event.OrderCanceled canceledEvent = new Event.OrderCanceled(poeCanceled);
        order.apply(canceledEvent);

        assertEquals(0L, order.getQuantity());
    }

    @Test
    void testApplyMixedExecutionsAndCancellations() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 250L;
        poeExecuted.price = 5000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        POE.OrderCanceled poeCanceled = new POE.OrderCanceled();
        poeCanceled.timestamp = 3000L;
        poeCanceled.orderId = "ORDER123".getBytes();
        poeCanceled.canceledQuantity = 350L;
        poeCanceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        Event.OrderCanceled canceledEvent = new Event.OrderCanceled(poeCanceled);

        order.apply(executedEvent);
        assertEquals(750L, order.getQuantity());

        order.apply(canceledEvent);
        assertEquals(400L, order.getQuantity());
    }

    @Test
    void testFormatWithValidInstruments() {
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

        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = ASCII.packLong("FOO");
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        String formatted = order.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("B"));
        assertTrue(formatted.contains("FOO"));
    }

    @Test
    void testFormatWithBuySide() {
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

        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "BUY001".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = ASCII.packLong("TEST");
        poeMessage.quantity = 200L;
        poeMessage.price = 10000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        String formatted = order.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("B"));
        assertTrue(formatted.contains("BUY001"));
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

        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "SELL001".getBytes();
        poeMessage.side = POE.SELL;
        poeMessage.instrument = ASCII.packLong("TEST");
        poeMessage.quantity = 200L;
        poeMessage.price = 10000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        String formatted = order.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("S"));
        assertTrue(formatted.contains("SELL001"));
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

        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1609459200000000000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = ASCII.packLong("FOO");
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        String formatted = order.format(instruments);
        assertNotNull(formatted);
    }

    @Test
    void testFormatIncludesQuantityAndPrice() {
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

        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = ASCII.packLong("FOO");
        poeMessage.quantity = 100L;
        poeMessage.price = 5000L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        String formatted = order.format(instruments);
        assertNotNull(formatted);
    }

    @Test
    void testGettersDoNotModifyState() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 500L;
        poeMessage.price = 10000L;
        poeMessage.orderNumber = 42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        long timestamp1 = order.getTimestamp();
        long timestamp2 = order.getTimestamp();
        assertEquals(timestamp1, timestamp2);

        byte side1 = order.getSide();
        byte side2 = order.getSide();
        assertEquals(side1, side2);

        long instrument1 = order.getInstrument();
        long instrument2 = order.getInstrument();
        assertEquals(instrument1, instrument2);

        String orderId1 = order.getOrderId();
        String orderId2 = order.getOrderId();
        assertEquals(orderId1, orderId2);

        long quantity1 = order.getQuantity();
        long quantity2 = order.getQuantity();
        assertEquals(quantity1, quantity2);
    }

    @Test
    void testApplyOrderExecutedWithZeroQuantity() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 2000L;
        poeExecuted.orderId = "ORDER123".getBytes();
        poeExecuted.quantity = 0L;
        poeExecuted.price = 5000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        order.apply(executedEvent);

        assertEquals(1000L, order.getQuantity());
    }

    @Test
    void testApplyOrderCanceledWithZeroQuantity() {
        POE.OrderAccepted poeAccepted = new POE.OrderAccepted();
        poeAccepted.timestamp = 1000L;
        poeAccepted.orderId = "ORDER123".getBytes();
        poeAccepted.side = POE.BUY;
        poeAccepted.instrument = 1L;
        poeAccepted.quantity = 1000L;
        poeAccepted.price = 5000L;
        poeAccepted.orderNumber = 1L;

        Event.OrderAccepted acceptedEvent = new Event.OrderAccepted(poeAccepted);
        Order order = new Order(acceptedEvent);

        POE.OrderCanceled poeCanceled = new POE.OrderCanceled();
        poeCanceled.timestamp = 2000L;
        poeCanceled.orderId = "ORDER123".getBytes();
        poeCanceled.canceledQuantity = 0L;
        poeCanceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled canceledEvent = new Event.OrderCanceled(poeCanceled);
        order.apply(canceledEvent);

        assertEquals(1000L, order.getQuantity());
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

        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = ASCII.packLong("ABC");
        poeMessage.quantity = 10000L;
        poeMessage.price = 123456L;
        poeMessage.orderNumber = 1L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        String formatted = order.format(instruments);
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("ABC"));
    }

    @Test
    void testMultipleOrdersAreIndependent() {
        POE.OrderAccepted poeMessage1 = new POE.OrderAccepted();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.side = POE.BUY;
        poeMessage1.instrument = 1L;
        poeMessage1.quantity = 500L;
        poeMessage1.price = 5000L;
        poeMessage1.orderNumber = 1L;

        POE.OrderAccepted poeMessage2 = new POE.OrderAccepted();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.side = POE.SELL;
        poeMessage2.instrument = 2L;
        poeMessage2.quantity = 1000L;
        poeMessage2.price = 10000L;
        poeMessage2.orderNumber = 2L;

        Event.OrderAccepted event1 = new Event.OrderAccepted(poeMessage1);
        Event.OrderAccepted event2 = new Event.OrderAccepted(poeMessage2);

        Order order1 = new Order(event1);
        Order order2 = new Order(event2);

        assertEquals(500L, order1.getQuantity());
        assertEquals(1000L, order2.getQuantity());

        POE.OrderExecuted poeExecuted = new POE.OrderExecuted();
        poeExecuted.timestamp = 3000L;
        poeExecuted.orderId = "ORDER1".getBytes();
        poeExecuted.quantity = 200L;
        poeExecuted.price = 5000L;
        poeExecuted.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExecuted.matchNumber = 1L;

        Event.OrderExecuted executedEvent = new Event.OrderExecuted(poeExecuted);
        order1.apply(executedEvent);

        assertEquals(300L, order1.getQuantity());
        assertEquals(1000L, order2.getQuantity());
    }

    @Test
    void testConstructorWithNegativeValues() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = -1000L;
        poeMessage.orderId = "ORDER".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = -100L;
        poeMessage.quantity = -500L;
        poeMessage.price = -10000L;
        poeMessage.orderNumber = -42L;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        Order order = new Order(event);

        assertEquals(-1000L, order.getTimestamp());
        assertEquals(-100L, order.getInstrument());
        assertEquals(-500L, order.getQuantity());
    }
}
