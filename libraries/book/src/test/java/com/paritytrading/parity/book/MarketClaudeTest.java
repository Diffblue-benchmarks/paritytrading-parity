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

class MarketClaudeTest {

    private static final long INSTRUMENT_1 = 1;
    private static final long INSTRUMENT_2 = 2;

    private MarketEvents events;
    private Market market;

    @BeforeEach
    void setUp() {
        events = new MarketEvents();
        market = new Market(events);
    }

    @Test
    void testConstructor() {
        MarketEvents listener = new MarketEvents();
        Market newMarket = new Market(listener);
        assertNotNull(newMarket);
    }

    @Test
    void testOpenNewOrderBook() {
        OrderBook book = market.open(INSTRUMENT_1);
        assertNotNull(book);
        assertEquals(INSTRUMENT_1, book.getInstrument());
    }

    @Test
    void testOpenExistingOrderBook() {
        OrderBook book1 = market.open(INSTRUMENT_1);
        OrderBook book2 = market.open(INSTRUMENT_1);
        assertSame(book1, book2);
    }

    @Test
    void testOpenMultipleOrderBooks() {
        OrderBook book1 = market.open(INSTRUMENT_1);
        OrderBook book2 = market.open(INSTRUMENT_2);
        assertNotSame(book1, book2);
        assertEquals(INSTRUMENT_1, book1.getInstrument());
        assertEquals(INSTRUMENT_2, book2.getInstrument());
    }

    @Test
    void testFindExistingOrder() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);

        Order order = market.find(100);
        assertNotNull(order);
        assertEquals(Side.BUY, order.getSide());
        assertEquals(1000, order.getPrice());
        assertEquals(50, order.getRemainingQuantity());
    }

    @Test
    void testFindNonExistingOrder() {
        market.open(INSTRUMENT_1);
        Order order = market.find(999);
        assertNull(order);
    }

    @Test
    void testAddToClosedOrderBook() {
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        Order order = market.find(100);
        assertNull(order);
        assertTrue(events.collect().isEmpty());
    }

    @Test
    void testAddDuplicateOrderId() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        market.add(INSTRUMENT_1, 100, Side.SELL, 2000, 100);

        Order order = market.find(100);
        assertNotNull(order);
        assertEquals(Side.BUY, order.getSide());
        assertEquals(1000, order.getPrice());
        assertEquals(50, order.getRemainingQuantity());
        assertTrue(events.collect().isEmpty());
    }

    @Test
    void testAddMultipleOrders() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        market.add(INSTRUMENT_1, 101, Side.SELL, 1100, 75);
        market.add(INSTRUMENT_1, 102, Side.BUY, 1050, 25);

        assertNotNull(market.find(100));
        assertNotNull(market.find(101));
        assertNotNull(market.find(102));
    }

    @Test
    void testModifyUnknownOrder() {
        market.open(INSTRUMENT_1);
        market.modify(999, 100);
        assertTrue(events.collect().isEmpty());
    }

    @Test
    void testModifyOrderToZero() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        market.modify(100, 0);

        assertNull(market.find(100));
        assertEquals(1, events.collect().size());
    }

    @Test
    void testModifyOrderWithNegativeSize() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        market.modify(100, -10);

        assertNull(market.find(100));
        assertEquals(1, events.collect().size());
    }

    @Test
    void testModifyOrderIncrease() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        market.modify(100, 100);

        Order order = market.find(100);
        assertNotNull(order);
        assertEquals(100, order.getRemainingQuantity());
    }

    @Test
    void testModifyOrderDecrease() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        market.modify(100, 25);

        Order order = market.find(100);
        assertNotNull(order);
        assertEquals(25, order.getRemainingQuantity());
    }

    @Test
    void testExecuteUnknownOrder() {
        market.open(INSTRUMENT_1);
        long remaining = market.execute(999, 50);
        assertEquals(0, remaining);
        assertTrue(events.collect().isEmpty());
    }

    @Test
    void testExecuteFullOrder() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.execute(100, 50);

        assertEquals(0, remaining);
        assertNull(market.find(100));
        assertEquals(2, events.collect().size());
    }

    @Test
    void testExecutePartialOrder() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.execute(100, 30);

        assertEquals(20, remaining);
        Order order = market.find(100);
        assertNotNull(order);
        assertEquals(20, order.getRemainingQuantity());
    }

    @Test
    void testExecuteMoreThanAvailable() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.execute(100, 100);

        assertEquals(0, remaining);
        assertNull(market.find(100));
    }

    @Test
    void testExecuteWithPriceUnknownOrder() {
        market.open(INSTRUMENT_1);
        long remaining = market.execute(999, 50, 1000);
        assertEquals(0, remaining);
        assertTrue(events.collect().isEmpty());
    }

    @Test
    void testExecuteWithPriceFullOrder() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.execute(100, 50, 995);

        assertEquals(0, remaining);
        assertNull(market.find(100));
    }

    @Test
    void testExecuteWithPricePartialOrder() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.execute(100, 30, 995);

        assertEquals(20, remaining);
        Order order = market.find(100);
        assertNotNull(order);
        assertEquals(20, order.getRemainingQuantity());
    }

    @Test
    void testExecuteWithPriceMoreThanAvailable() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.execute(100, 100, 995);

        assertEquals(0, remaining);
        assertNull(market.find(100));
    }

    @Test
    void testExecuteBuyOrderGeneratesSellTrade() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        market.execute(100, 50);

        assertEquals(2, events.collect().size());
        MarketEvents.Event firstEvent = events.collect().get(0);
        assertTrue(firstEvent instanceof MarketEvents.Trade);
        MarketEvents.Trade trade = (MarketEvents.Trade) firstEvent;
        assertEquals(Side.SELL, trade.side);
    }

    @Test
    void testExecuteSellOrderGeneratesBuyTrade() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.SELL, 1000, 50);
        events.collect().clear();

        market.execute(100, 50);

        assertEquals(2, events.collect().size());
        MarketEvents.Event firstEvent = events.collect().get(0);
        assertTrue(firstEvent instanceof MarketEvents.Trade);
        MarketEvents.Trade trade = (MarketEvents.Trade) firstEvent;
        assertEquals(Side.BUY, trade.side);
    }

    @Test
    void testCancelUnknownOrder() {
        market.open(INSTRUMENT_1);
        long remaining = market.cancel(999, 50);
        assertEquals(0, remaining);
        assertTrue(events.collect().isEmpty());
    }

    @Test
    void testCancelFullOrder() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.cancel(100, 50);

        assertEquals(0, remaining);
        assertNull(market.find(100));
        assertEquals(1, events.collect().size());
    }

    @Test
    void testCancelPartialOrder() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.cancel(100, 30);

        assertEquals(20, remaining);
        Order order = market.find(100);
        assertNotNull(order);
        assertEquals(20, order.getRemainingQuantity());
    }

    @Test
    void testCancelMoreThanAvailable() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        long remaining = market.cancel(100, 100);

        assertEquals(0, remaining);
        assertNull(market.find(100));
    }

    @Test
    void testDeleteUnknownOrder() {
        market.open(INSTRUMENT_1);
        market.delete(999);
        assertTrue(events.collect().isEmpty());
    }

    @Test
    void testDeleteExistingOrder() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        events.collect().clear();

        market.delete(100);

        assertNull(market.find(100));
        assertEquals(1, events.collect().size());
    }

    @Test
    void testDeleteOrderWithRemainingQuantity() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        market.execute(100, 20);
        events.collect().clear();

        market.delete(100);

        assertNull(market.find(100));
        assertEquals(1, events.collect().size());
    }

    @Test
    void testMultipleInstruments() {
        market.open(INSTRUMENT_1);
        market.open(INSTRUMENT_2);

        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 50);
        market.add(INSTRUMENT_2, 200, Side.SELL, 2000, 75);

        Order order1 = market.find(100);
        Order order2 = market.find(200);

        assertNotNull(order1);
        assertNotNull(order2);
        assertEquals(INSTRUMENT_1, order1.getOrderBook().getInstrument());
        assertEquals(INSTRUMENT_2, order2.getOrderBook().getInstrument());
    }

    @Test
    void testSequentialOperations() {
        market.open(INSTRUMENT_1);
        market.add(INSTRUMENT_1, 100, Side.BUY, 1000, 100);

        market.execute(100, 30);
        Order order = market.find(100);
        assertNotNull(order);
        assertEquals(70, order.getRemainingQuantity());

        market.cancel(100, 20);
        order = market.find(100);
        assertNotNull(order);
        assertEquals(50, order.getRemainingQuantity());

        market.modify(100, 80);
        order = market.find(100);
        assertNotNull(order);
        assertEquals(80, order.getRemainingQuantity());

        market.delete(100);
        assertNull(market.find(100));
    }
}
