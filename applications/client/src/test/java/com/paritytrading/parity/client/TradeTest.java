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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

public class TradeTest {

    @Test
    public void testTradeConstructor() {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        ASCII.putLeft(acceptedMessage.orderId, "ORDER123");
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = ASCII.packLong("FOO");
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        Event.OrderAccepted orderAcceptedEvent = new Event.OrderAccepted(acceptedMessage);
        Order order = new Order(orderAcceptedEvent);

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        ASCII.putLeft(executedMessage.orderId, "ORDER123");
        executedMessage.quantity = 50L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 42L;

        Event.OrderExecuted orderExecutedEvent = new Event.OrderExecuted(executedMessage);

        Trade trade = new Trade(order, orderExecutedEvent);

        assertNotNull(trade);
    }

    @Test
    public void testGetTimestamp() {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        ASCII.putLeft(acceptedMessage.orderId, "ORDER123");
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = ASCII.packLong("FOO");
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 5000L;
        acceptedMessage.orderNumber = 1L;

        Event.OrderAccepted orderAcceptedEvent = new Event.OrderAccepted(acceptedMessage);
        Order order = new Order(orderAcceptedEvent);

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        ASCII.putLeft(executedMessage.orderId, "ORDER123");
        executedMessage.quantity = 50L;
        executedMessage.price = 5000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 42L;

        Event.OrderExecuted orderExecutedEvent = new Event.OrderExecuted(executedMessage);

        Trade trade = new Trade(order, orderExecutedEvent);

        assertEquals(2000000000L, trade.getTimestamp());
    }

    @Test
    public void testFormat() throws Exception {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        ASCII.putLeft(acceptedMessage.orderId, "ORDER123");
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = ASCII.packLong("FOO");
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 10000L;
        acceptedMessage.orderNumber = 1L;

        Event.OrderAccepted orderAcceptedEvent = new Event.OrderAccepted(acceptedMessage);
        Order order = new Order(orderAcceptedEvent);

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        ASCII.putLeft(executedMessage.orderId, "ORDER123");
        executedMessage.quantity = 50L;
        executedMessage.price = 10000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 42L;

        Event.OrderExecuted orderExecutedEvent = new Event.OrderExecuted(executedMessage);

        Trade trade = new Trade(order, orderExecutedEvent);

        Instrument instrument = createTestInstrument("FOO", 2, 0);
        Instruments instruments = createTestInstruments(instrument);

        String result = trade.format(instruments);

        assertNotNull(result);
        assertTrue(result.contains("ORDER123"));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("FOO"));
    }

    private Instrument createTestInstrument(String name, int priceFractionDigits, int sizeFractionDigits) throws Exception {
        java.lang.reflect.Constructor<Instrument> constructor = Instrument.class.getDeclaredConstructor(
                String.class, int.class, int.class);
        constructor.setAccessible(true);
        return constructor.newInstance(name, priceFractionDigits, sizeFractionDigits);
    }

    private Instruments createTestInstruments(Instrument instrument) throws Exception {
        Instrument[] instruments = new Instrument[] { instrument };
        java.lang.reflect.Constructor<Instruments> constructor = Instruments.class.getDeclaredConstructor(
                Instrument[].class, int.class, int.class, int.class, int.class);
        constructor.setAccessible(true);
        return constructor.newInstance(instruments, 1, 2, 1, 0);
    }
}
