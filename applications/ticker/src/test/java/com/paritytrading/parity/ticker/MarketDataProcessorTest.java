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
package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.net.pmd.PMD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketDataProcessorTest {

    private static final long INSTRUMENT = 1;

    private TestMarketDataListener listener;

    private Market market;

    private MarketDataProcessor processor;

    @BeforeEach
    void setUp() {
        listener  = new TestMarketDataListener();
        market    = new Market(listener);

        market.open(INSTRUMENT);

        processor = new MarketDataProcessor(market, listener);
    }

    @Test
    void orderAddedWithBuySide() {
        PMD.OrderAdded message = new PMD.OrderAdded();
        message.timestamp   = 1_000_000L;
        message.orderNumber = 1;
        message.side        = PMD.BUY;
        message.instrument  = INSTRUMENT;
        message.quantity    = 100;
        message.price       = 999;

        processor.orderAdded(message);

        assertEquals(1, listener.timestampMillis());
        assertNotNull(market.find(1));
    }

    @Test
    void orderAddedWithSellSide() {
        PMD.OrderAdded message = new PMD.OrderAdded();
        message.timestamp   = 2_000_000L;
        message.orderNumber = 2;
        message.side        = PMD.SELL;
        message.instrument  = INSTRUMENT;
        message.quantity    = 200;
        message.price       = 1001;

        processor.orderAdded(message);

        assertEquals(2, listener.timestampMillis());
        assertNotNull(market.find(2));
    }

    @Test
    void orderExecuted() {
        addBuyOrder(1, 100, 999);

        PMD.OrderExecuted message = new PMD.OrderExecuted();
        message.timestamp   = 2_000_000L;
        message.orderNumber = 1;
        message.quantity    = 100;

        processor.orderExecuted(message);

        assertEquals(2, listener.timestampMillis());
        assertNull(market.find(1));
    }

    @Test
    void orderCanceled() {
        addBuyOrder(1, 100, 999);

        PMD.OrderCanceled message = new PMD.OrderCanceled();
        message.timestamp        = 3_000_000L;
        message.orderNumber      = 1;
        message.canceledQuantity = 50;

        processor.orderCanceled(message);

        assertEquals(3, listener.timestampMillis());
        assertNotNull(market.find(1));
    }

    @Test
    void versionWithMatchingVersion() {
        PMD.Version message = new PMD.Version();
        message.version = PMD.VERSION;

        processor.version(message);
    }

    private void addBuyOrder(long orderNumber, long quantity, long price) {
        PMD.OrderAdded message = new PMD.OrderAdded();
        message.timestamp   = 1_000_000L;
        message.orderNumber = orderNumber;
        message.side        = PMD.BUY;
        message.instrument  = INSTRUMENT;
        message.quantity    = quantity;
        message.price       = price;

        processor.orderAdded(message);
    }

    private static class TestMarketDataListener extends MarketDataListener {

        @Override
        public void update(OrderBook book, boolean bbo) {
        }

        @Override
        public void trade(OrderBook book, Side side, long price, long size) {
        }
    }
}
