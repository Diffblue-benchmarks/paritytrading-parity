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

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketEdgeCaseTest {

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
    void findExistingOrder() {
        market.add(INSTRUMENT, 1, Side.BUY, 999, 100);

        Order order = market.find(1);

        assertNotNull(order);
    }

    @Test
    void findUnknownOrder() {
        Order order = market.find(999);

        assertNull(order);
    }

    @Test
    void addDuplicateOrderId() {
        market.add(INSTRUMENT, 1, Side.BUY, 999, 100);
        market.add(INSTRUMENT, 1, Side.SELL, 1001, 200);

        assertEquals(asList(new MarketEvents.Update(INSTRUMENT, true)), events.collect());
    }

    @Test
    void addUnknownInstrument() {
        long unknownInstrument = 999;

        market.add(unknownInstrument, 1, Side.BUY, 999, 100);

        assertEquals(emptyList(), events.collect());
    }

    @Test
    void modifyUnknownOrder() {
        market.modify(999, 50);

        assertEquals(emptyList(), events.collect());
    }

    @Test
    void modifyToZero() {
        market.add(INSTRUMENT, 1, Side.BUY, 999, 100);
        market.modify(1, 0);

        assertNull(market.find(1));

        MarketEvents.Event updateAfterAdd    = new MarketEvents.Update(INSTRUMENT, true);
        MarketEvents.Event updateAfterModify = new MarketEvents.Update(INSTRUMENT, true);

        assertEquals(asList(updateAfterAdd, updateAfterModify), events.collect());
    }

    @Test
    void executeUnknownOrder() {
        assertEquals(0, market.execute(999, 100));

        assertEquals(emptyList(), events.collect());
    }

    @Test
    void executeWithPriceUnknownOrder() {
        assertEquals(0, market.execute(999, 100, 1000));

        assertEquals(emptyList(), events.collect());
    }

    @Test
    void cancelUnknownOrder() {
        assertEquals(0, market.cancel(999, 100));

        assertEquals(emptyList(), events.collect());
    }

    @Test
    void deleteUnknownOrder() {
        market.delete(999);

        assertEquals(emptyList(), events.collect());
    }
}
