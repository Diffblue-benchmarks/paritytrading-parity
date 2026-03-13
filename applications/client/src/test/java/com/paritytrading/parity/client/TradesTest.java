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

import com.paritytrading.parity.net.poe.POE;
import java.util.List;
import org.junit.jupiter.api.Test;

class TradesTest {

    @Test
    void testCollectWithNoEvents() {
        Events events = new Events();

        List<Trade> trades = Trades.collect(events);

        assertNotNull(trades);
        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectWithOrderAcceptedOnly() {
        Events events = new Events();
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000L;
        for (int i = 0; i < poeMessage.orderId.length; i++) {
            poeMessage.orderId[i] = (byte) ('A' + i);
        }
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 1L;
        poeMessage.quantity = 100L;
        poeMessage.price = 15000L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        List<Trade> trades = Trades.collect(events);

        assertNotNull(trades);
        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectWithOrderExecutedNoMatch() {
        Events events = new Events();
        POE.OrderExecuted poeMessage = new POE.OrderExecuted();
        poeMessage.timestamp = 2000L;
        for (int i = 0; i < poeMessage.orderId.length; i++) {
            poeMessage.orderId[i] = (byte) ('B' + i);
        }
        poeMessage.quantity = 50L;
        poeMessage.price = 15000L;
        poeMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeMessage.matchNumber = 1L;

        events.orderExecuted(poeMessage);

        List<Trade> trades = Trades.collect(events);

        assertNotNull(trades);
        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectWithSingleTrade() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        for (int i = 0; i < acceptedMessage.orderId.length; i++) {
            acceptedMessage.orderId[i] = (byte) ('A' + i);
        }
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 15000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000L;
        for (int i = 0; i < executedMessage.orderId.length; i++) {
            executedMessage.orderId[i] = (byte) ('A' + i);
        }
        executedMessage.quantity = 50L;
        executedMessage.price = 15000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        List<Trade> trades = Trades.collect(events);

        assertNotNull(trades);
        assertEquals(1, trades.size());
        assertEquals(2000L, trades.get(0).getTimestamp());
    }

    @Test
    void testCollectWithMultipleTrades() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage1 = new POE.OrderAccepted();
        acceptedMessage1.timestamp = 1000L;
        for (int i = 0; i < acceptedMessage1.orderId.length; i++) {
            acceptedMessage1.orderId[i] = (byte) ('A' + i);
        }
        acceptedMessage1.side = POE.BUY;
        acceptedMessage1.instrument = 1L;
        acceptedMessage1.quantity = 100L;
        acceptedMessage1.price = 15000L;
        acceptedMessage1.orderNumber = 1L;

        POE.OrderAccepted acceptedMessage2 = new POE.OrderAccepted();
        acceptedMessage2.timestamp = 1500L;
        for (int i = 0; i < acceptedMessage2.orderId.length; i++) {
            acceptedMessage2.orderId[i] = (byte) ('B' + i);
        }
        acceptedMessage2.side = POE.SELL;
        acceptedMessage2.instrument = 1L;
        acceptedMessage2.quantity = 100L;
        acceptedMessage2.price = 15000L;
        acceptedMessage2.orderNumber = 2L;

        POE.OrderExecuted executedMessage1 = new POE.OrderExecuted();
        executedMessage1.timestamp = 3000L;
        for (int i = 0; i < executedMessage1.orderId.length; i++) {
            executedMessage1.orderId[i] = (byte) ('A' + i);
        }
        executedMessage1.quantity = 50L;
        executedMessage1.price = 15000L;
        executedMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage1.matchNumber = 1L;

        POE.OrderExecuted executedMessage2 = new POE.OrderExecuted();
        executedMessage2.timestamp = 2000L;
        for (int i = 0; i < executedMessage2.orderId.length; i++) {
            executedMessage2.orderId[i] = (byte) ('B' + i);
        }
        executedMessage2.quantity = 50L;
        executedMessage2.price = 15000L;
        executedMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage2.matchNumber = 1L;

        events.orderAccepted(acceptedMessage1);
        events.orderAccepted(acceptedMessage2);
        events.orderExecuted(executedMessage1);
        events.orderExecuted(executedMessage2);

        List<Trade> trades = Trades.collect(events);

        assertNotNull(trades);
        assertEquals(2, trades.size());
        assertEquals(2000L, trades.get(0).getTimestamp());
        assertEquals(3000L, trades.get(1).getTimestamp());
    }

    @Test
    void testCollectWithMultipleExecutionsForSameOrder() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        for (int i = 0; i < acceptedMessage.orderId.length; i++) {
            acceptedMessage.orderId[i] = (byte) ('A' + i);
        }
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 15000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage1 = new POE.OrderExecuted();
        executedMessage1.timestamp = 2000L;
        for (int i = 0; i < executedMessage1.orderId.length; i++) {
            executedMessage1.orderId[i] = (byte) ('A' + i);
        }
        executedMessage1.quantity = 30L;
        executedMessage1.price = 15000L;
        executedMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage1.matchNumber = 1L;

        POE.OrderExecuted executedMessage2 = new POE.OrderExecuted();
        executedMessage2.timestamp = 3000L;
        for (int i = 0; i < executedMessage2.orderId.length; i++) {
            executedMessage2.orderId[i] = (byte) ('A' + i);
        }
        executedMessage2.quantity = 20L;
        executedMessage2.price = 15000L;
        executedMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage2.matchNumber = 2L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage1);
        events.orderExecuted(executedMessage2);

        List<Trade> trades = Trades.collect(events);

        assertNotNull(trades);
        assertEquals(2, trades.size());
        assertEquals(2000L, trades.get(0).getTimestamp());
        assertEquals(3000L, trades.get(1).getTimestamp());
    }

    @Test
    void testCollectWithSameMatchNumber() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage1 = new POE.OrderAccepted();
        acceptedMessage1.timestamp = 1000L;
        for (int i = 0; i < acceptedMessage1.orderId.length; i++) {
            acceptedMessage1.orderId[i] = (byte) ('A' + i);
        }
        acceptedMessage1.side = POE.BUY;
        acceptedMessage1.instrument = 1L;
        acceptedMessage1.quantity = 100L;
        acceptedMessage1.price = 15000L;
        acceptedMessage1.orderNumber = 1L;

        POE.OrderAccepted acceptedMessage2 = new POE.OrderAccepted();
        acceptedMessage2.timestamp = 1500L;
        for (int i = 0; i < acceptedMessage2.orderId.length; i++) {
            acceptedMessage2.orderId[i] = (byte) ('B' + i);
        }
        acceptedMessage2.side = POE.SELL;
        acceptedMessage2.instrument = 1L;
        acceptedMessage2.quantity = 100L;
        acceptedMessage2.price = 15000L;
        acceptedMessage2.orderNumber = 2L;

        POE.OrderExecuted executedMessage1 = new POE.OrderExecuted();
        executedMessage1.timestamp = 2000L;
        for (int i = 0; i < executedMessage1.orderId.length; i++) {
            executedMessage1.orderId[i] = (byte) ('A' + i);
        }
        executedMessage1.quantity = 50L;
        executedMessage1.price = 15000L;
        executedMessage1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage1.matchNumber = 1L;

        POE.OrderExecuted executedMessage2 = new POE.OrderExecuted();
        executedMessage2.timestamp = 2000L;
        for (int i = 0; i < executedMessage2.orderId.length; i++) {
            executedMessage2.orderId[i] = (byte) ('B' + i);
        }
        executedMessage2.quantity = 50L;
        executedMessage2.price = 15000L;
        executedMessage2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage2.matchNumber = 1L;

        events.orderAccepted(acceptedMessage1);
        events.orderAccepted(acceptedMessage2);
        events.orderExecuted(executedMessage1);
        events.orderExecuted(executedMessage2);

        List<Trade> trades = Trades.collect(events);

        assertNotNull(trades);
        assertEquals(2, trades.size());
    }
}
