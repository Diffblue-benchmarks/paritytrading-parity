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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MarketTest {

    private Market market;
    private MarketEvents events;

    @BeforeEach
    public void setUp() {
        events = new MarketEvents();
        market = new Market(events);
    }

    @Test
    public void testFindOrderNotFound() {
        Order order = market.find(123);
        assertNull(order);
    }

    @Test
    public void testFindOrderFound() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);

        Order order = market.find(orderId);
        assertNotNull(order);
        assertEquals(1000, order.getPrice());
        assertEquals(100, order.getRemainingQuantity());
    }

    @Test
    public void testAddWithExistingOrderId() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        market.add(instrument, orderId, Side.BUY, 2000, 200);

        Order order = market.find(orderId);
        assertEquals(1000, order.getPrice());
        assertEquals(100, order.getRemainingQuantity());
        assertEquals(0, events.collect().size());
    }

    @Test
    public void testAddWithClosedBook() {
        long instrument = 1;
        long orderId = 100;

        market.add(instrument, orderId, Side.BUY, 1000, 100);

        Order order = market.find(orderId);
        assertNull(order);
    }

    @Test
    public void testAddSuccessfully() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);

        market.add(instrument, orderId, Side.BUY, 1000, 100);

        Order order = market.find(orderId);
        assertNotNull(order);
        assertEquals(Side.BUY, order.getSide());
        assertEquals(1000, order.getPrice());
        assertEquals(100, order.getRemainingQuantity());

        List<MarketEvents.Event> eventList = events.collect();
        assertEquals(1, eventList.size());
        MarketEvents.Update update = (MarketEvents.Update) eventList.get(0);
        assertEquals(instrument, update.instrument);
        assertEquals(true, update.bbo);
    }

    @Test
    public void testModifyUnknownOrder() {
        market.modify(999, 50);
        assertEquals(0, events.collect().size());
    }

    @Test
    public void testModifyReduceQuantity() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        market.modify(orderId, 50);

        Order order = market.find(orderId);
        assertNotNull(order);
        assertEquals(50, order.getRemainingQuantity());

        List<MarketEvents.Event> eventList = events.collect();
        assertEquals(1, eventList.size());
        MarketEvents.Update update = (MarketEvents.Update) eventList.get(0);
        assertEquals(instrument, update.instrument);
    }

    @Test
    public void testModifyToZero() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        market.modify(orderId, 0);

        Order order = market.find(orderId);
        assertNull(order);

        List<MarketEvents.Event> eventList = events.collect();
        assertEquals(1, eventList.size());
    }

    @Test
    public void testModifyNegativeSizeClampsToZero() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        market.modify(orderId, -10);

        Order order = market.find(orderId);
        assertNull(order);
    }

    @Test
    public void testExecuteUnknownOrder() {
        long remaining = market.execute(999, 50);
        assertEquals(0, remaining);
    }

    @Test
    public void testExecutePartialQuantity() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        long remaining = market.execute(orderId, 30);

        assertEquals(70, remaining);
        Order order = market.find(orderId);
        assertNotNull(order);
        assertEquals(70, order.getRemainingQuantity());

        List<MarketEvents.Event> eventList = events.collect();
        assertEquals(2, eventList.size());
        MarketEvents.Trade trade = (MarketEvents.Trade) eventList.get(0);
        assertEquals(instrument, trade.instrument);
        assertEquals(Side.SELL, trade.side);
        assertEquals(1000, trade.price);
        assertEquals(30, trade.size);
    }

    @Test
    public void testExecuteFullQuantity() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        long remaining = market.execute(orderId, 100);

        assertEquals(0, remaining);
        Order order = market.find(orderId);
        assertNull(order);
    }

    @Test
    public void testExecuteWithPriceUnknownOrder() {
        long remaining = market.execute(999, 50, 1000);
        assertEquals(0, remaining);
    }

    @Test
    public void testExecuteWithPricePartialQuantity() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.SELL, 1000, 100);
        events.collect().clear();

        long remaining = market.execute(orderId, 40, 1000);

        assertEquals(60, remaining);
        Order order = market.find(orderId);
        assertNotNull(order);
        assertEquals(60, order.getRemainingQuantity());

        List<MarketEvents.Event> eventList = events.collect();
        assertEquals(2, eventList.size());
        MarketEvents.Trade trade = (MarketEvents.Trade) eventList.get(0);
        assertEquals(instrument, trade.instrument);
        assertEquals(Side.BUY, trade.side);
        assertEquals(1000, trade.price);
        assertEquals(40, trade.size);
    }

    @Test
    public void testExecuteMoreThanRemaining() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        long remaining = market.execute(orderId, 150);

        assertEquals(0, remaining);
        Order order = market.find(orderId);
        assertNull(order);
    }

    @Test
    public void testCancelUnknownOrder() {
        long remaining = market.cancel(999, 50);
        assertEquals(0, remaining);
    }

    @Test
    public void testCancelPartialQuantity() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        long remaining = market.cancel(orderId, 30);

        assertEquals(70, remaining);
        Order order = market.find(orderId);
        assertNotNull(order);
        assertEquals(70, order.getRemainingQuantity());

        List<MarketEvents.Event> eventList = events.collect();
        assertEquals(1, eventList.size());
        MarketEvents.Update update = (MarketEvents.Update) eventList.get(0);
        assertEquals(instrument, update.instrument);
    }

    @Test
    public void testCancelFullQuantity() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        long remaining = market.cancel(orderId, 100);

        assertEquals(0, remaining);
        Order order = market.find(orderId);
        assertNull(order);
    }

    @Test
    public void testCancelMoreThanRemaining() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        long remaining = market.cancel(orderId, 150);

        assertEquals(0, remaining);
        Order order = market.find(orderId);
        assertNull(order);
    }

    @Test
    public void testDeleteUnknownOrder() {
        market.delete(999);
        assertEquals(0, events.collect().size());
    }

    @Test
    public void testDeleteOrder() {
        long instrument = 1;
        long orderId = 100;
        market.open(instrument);
        market.add(instrument, orderId, Side.BUY, 1000, 100);
        events.collect().clear();

        market.delete(orderId);

        Order order = market.find(orderId);
        assertNull(order);

        List<MarketEvents.Event> eventList = events.collect();
        assertEquals(1, eventList.size());
        MarketEvents.Update update = (MarketEvents.Update) eventList.get(0);
        assertEquals(instrument, update.instrument);
    }
}
