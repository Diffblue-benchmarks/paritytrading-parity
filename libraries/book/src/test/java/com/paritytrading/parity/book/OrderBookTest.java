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
package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderBookTest {

    private static final long INSTRUMENT = 1;

    private MarketEvents events;

    private Market market;

    private OrderBook book;

    @BeforeEach
    void setUp() {
        events = new MarketEvents();
        market = new Market(events);

        book = market.open(INSTRUMENT);
    }

    @Test
    void bestBidPriceEmpty() {
        assertEquals(0, book.getBestBidPrice());
    }

    @Test
    void bestAskPriceEmpty() {
        assertEquals(0, book.getBestAskPrice());
    }

    @Test
    void bestBidPriceWithOrders() {
        market.add(INSTRUMENT, 1, Side.BUY,  999, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1000,  50);

        assertEquals(1000, book.getBestBidPrice());
    }

    @Test
    void bestAskPriceWithOrders() {
        market.add(INSTRUMENT, 1, Side.SELL, 1001, 200);
        market.add(INSTRUMENT, 2, Side.SELL, 1002,  50);

        assertEquals(1001, book.getBestAskPrice());
    }

}
