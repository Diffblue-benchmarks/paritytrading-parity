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

import com.paritytrading.parity.net.poe.POE;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TradesTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void collectEmptyEvents() {
        List<Trade> trades = Trades.collect(events);

        assertTrue(trades.isEmpty());
    }

    @Test
    void collectOrderAcceptedOnly() {
        events.orderAccepted(createOrderAccepted("order1          ", POE.BUY, 100, 5000, 1000L));

        List<Trade> trades = Trades.collect(events);

        assertTrue(trades.isEmpty());
    }

    @Test
    void collectSingleTrade() {
        events.orderAccepted(createOrderAccepted("order1          ", POE.BUY, 100, 5000, 1000L));
        events.orderExecuted(createOrderExecuted("order1          ", 50, 5000, 2000L, 1L));

        List<Trade> trades = Trades.collect(events);

        assertEquals(1, trades.size());
        assertEquals(2000L, trades.get(0).getTimestamp());
    }

    @Test
    void collectExecutionWithoutOrder() {
        events.orderExecuted(createOrderExecuted("unknown         ", 50, 5000, 2000L, 1L));

        List<Trade> trades = Trades.collect(events);

        assertTrue(trades.isEmpty());
    }

    @Test
    void collectMultipleTradesSortedByTimestamp() {
        events.orderAccepted(createOrderAccepted("order1          ", POE.BUY, 100, 5000, 1000L));
        events.orderAccepted(createOrderAccepted("order2          ", POE.SELL, 200, 6000, 1500L));
        events.orderExecuted(createOrderExecuted("order2          ", 50, 6000, 3000L, 2L));
        events.orderExecuted(createOrderExecuted("order1          ", 30, 5000, 2000L, 1L));

        List<Trade> trades = Trades.collect(events);

        assertEquals(2, trades.size());
        assertEquals(2000L, trades.get(0).getTimestamp());
        assertEquals(3000L, trades.get(1).getTimestamp());
    }

    @Test
    void collectMultipleExecutionsForSameMatch() {
        events.orderAccepted(createOrderAccepted("order1          ", POE.BUY, 100, 5000, 1000L));
        events.orderAccepted(createOrderAccepted("order2          ", POE.SELL, 100, 5000, 1500L));
        events.orderExecuted(createOrderExecuted("order1          ", 50, 5000, 2000L, 1L));
        events.orderExecuted(createOrderExecuted("order2          ", 50, 5000, 2500L, 1L));

        List<Trade> trades = Trades.collect(events);

        assertEquals(2, trades.size());
        assertEquals(2000L, trades.get(0).getTimestamp());
        assertEquals(2500L, trades.get(1).getTimestamp());
    }

    @Test
    void collectMultipleExecutionsForSameOrder() {
        events.orderAccepted(createOrderAccepted("order1          ", POE.BUY, 100, 5000, 1000L));
        events.orderExecuted(createOrderExecuted("order1          ", 30, 5000, 2000L, 1L));
        events.orderExecuted(createOrderExecuted("order1          ", 40, 5000, 3000L, 2L));

        List<Trade> trades = Trades.collect(events);

        assertEquals(2, trades.size());
        assertEquals(2000L, trades.get(0).getTimestamp());
        assertEquals(3000L, trades.get(1).getTimestamp());
    }

    private static POE.OrderAccepted createOrderAccepted(String orderId, byte side,
            long quantity, long price, long timestamp) {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp   = timestamp;
        message.orderId     = orderId.getBytes();
        message.side        = side;
        message.instrument  = 12345L;
        message.quantity    = quantity;
        message.price       = price;
        message.orderNumber = 1L;
        return message;
    }

    private static POE.OrderExecuted createOrderExecuted(String orderId, long quantity,
            long price, long timestamp, long matchNumber) {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp     = timestamp;
        message.orderId       = orderId.getBytes();
        message.quantity      = quantity;
        message.price         = price;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber   = matchNumber;
        return message;
    }
}
