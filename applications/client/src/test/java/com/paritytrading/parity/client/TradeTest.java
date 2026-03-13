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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradeTest {

    private Order order;
    private POE.OrderExecuted executedMessage;
    private Event.OrderExecuted executedEvent;

    @BeforeEach
    void setUp() {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        ASCII.putLeft(acceptedMessage.orderId, "ORDER-001");
        acceptedMessage.side = (byte) 'B';
        acceptedMessage.instrument = ASCII.packLong("AAPL");
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 15000L;
        acceptedMessage.orderNumber = 1L;

        order = new Order(new Event.OrderAccepted(acceptedMessage));

        executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        ASCII.putLeft(executedMessage.orderId, "ORDER-001");
        executedMessage.quantity = 50L;
        executedMessage.price = 15000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        executedEvent = new Event.OrderExecuted(executedMessage);
    }

    @Test
    void testTradeCreation() {
        Trade trade = new Trade(order, executedEvent);
        assertNotNull(trade);
    }

    @Test
    void testGetTimestamp() {
        Trade trade = new Trade(order, executedEvent);
        assertEquals(2000000000L, trade.getTimestamp());
    }

    @Test
    void testTradeTimestampMatchesEvent() {
        executedMessage.timestamp = 3000000000L;
        Event.OrderExecuted newEvent = new Event.OrderExecuted(executedMessage);
        Trade trade = new Trade(order, newEvent);
        assertEquals(3000000000L, trade.getTimestamp());
    }

    @Test
    void testMultipleTradesFromSameOrder() {
        Trade trade1 = new Trade(order, executedEvent);

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 3000000000L;
        ASCII.putLeft(executed2.orderId, "ORDER-001");
        executed2.quantity = 30L;
        executed2.price = 15100L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed2.matchNumber = 2L;

        Trade trade2 = new Trade(order, new Event.OrderExecuted(executed2));

        assertEquals(2000000000L, trade1.getTimestamp());
        assertEquals(3000000000L, trade2.getTimestamp());
    }

    @Test
    void testTradeWithDifferentQuantities() {
        executedMessage.quantity = 25L;
        Event.OrderExecuted event = new Event.OrderExecuted(executedMessage);
        Trade trade = new Trade(order, event);
        assertNotNull(trade);
    }

    @Test
    void testTradeWithDifferentPrices() {
        executedMessage.price = 16000L;
        Event.OrderExecuted event = new Event.OrderExecuted(executedMessage);
        Trade trade = new Trade(order, event);
        assertNotNull(trade);
    }

    @Test
    void testTradeWithZeroTimestamp() {
        executedMessage.timestamp = 0L;
        Event.OrderExecuted event = new Event.OrderExecuted(executedMessage);
        Trade trade = new Trade(order, event);
        assertEquals(0L, trade.getTimestamp());
    }

    @Test
    void testTradeWithLargeTimestamp() {
        executedMessage.timestamp = Long.MAX_VALUE;
        Event.OrderExecuted event = new Event.OrderExecuted(executedMessage);
        Trade trade = new Trade(order, event);
        assertEquals(Long.MAX_VALUE, trade.getTimestamp());
    }
}
