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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketTest {

    @Mock
    private MarketListener listener;

    private Market market;

    @BeforeEach
    void setUp() {
        market = new Market(listener);
    }

    @Test
    void testOpenCreatesOrderBook() {
        OrderBook book = market.open(1000L);
        assertNotNull(book);
        assertEquals(1000L, book.getInstrument());
    }

    @Test
    void testOpenReturnsSameOrderBookForSameInstrument() {
        OrderBook book1 = market.open(1000L);
        OrderBook book2 = market.open(1000L);
        assertSame(book1, book2);
    }

    @Test
    void testOpenCreatesMultipleOrderBooks() {
        OrderBook book1 = market.open(1000L);
        OrderBook book2 = market.open(2000L);
        assertNotSame(book1, book2);
        assertEquals(1000L, book1.getInstrument());
        assertEquals(2000L, book2.getInstrument());
    }

    @Test
    void testAddOrderWhenBookDoesNotExist() {
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        verifyNoInteractions(listener);
    }

    @Test
    void testAddOrderWithDuplicateOrderId() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        market.add(1000L, 1L, Side.BUY, 10100L, 50L);
        verify(listener, times(1)).update(any(OrderBook.class), anyBoolean());
    }

    @Test
    void testAddOrderTriggersUpdateEvent() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        verify(listener).update(book, true);
    }

    @Test
    void testAddOrderStoresOrderCorrectly() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        Order order = market.find(1L);
        assertNotNull(order);
        assertEquals(Side.BUY, order.getSide());
        assertEquals(10000L, order.getPrice());
        assertEquals(100L, order.getRemainingQuantity());
    }

    @Test
    void testFindReturnsNullForUnknownOrderId() {
        assertNull(market.find(999L));
    }

    @Test
    void testModifyOrderWithUnknownOrderId() {
        market.modify(999L, 50L);
        verifyNoInteractions(listener);
    }

    @Test
    void testModifyOrderReducesSize() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        market.modify(1L, 60L);
        Order order = market.find(1L);
        assertEquals(60L, order.getRemainingQuantity());
        verify(listener, times(2)).update(any(OrderBook.class), anyBoolean());
    }

    @Test
    void testModifyOrderIncreasesSize() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        market.modify(1L, 150L);
        Order order = market.find(1L);
        assertEquals(150L, order.getRemainingQuantity());
    }

    @Test
    void testModifyOrderToZeroRemovesOrder() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        market.modify(1L, 0L);
        assertNull(market.find(1L));
    }

    @Test
    void testModifyOrderWithNegativeSizeTreatsAsZero() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        market.modify(1L, -50L);
        assertNull(market.find(1L));
    }

    @Test
    void testExecuteOrderWithUnknownOrderId() {
        long remaining = market.execute(999L, 50L);
        assertEquals(0L, remaining);
        verifyNoInteractions(listener);
    }

    @Test
    void testExecuteOrderPartially() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        long remaining = market.execute(1L, 30L);
        assertEquals(70L, remaining);
        Order order = market.find(1L);
        assertNotNull(order);
        assertEquals(70L, order.getRemainingQuantity());
        verify(listener).trade(book, Side.SELL, 10000L, 30L);
    }

    @Test
    void testExecuteOrderFully() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        long remaining = market.execute(1L, 100L);
        assertEquals(0L, remaining);
        assertNull(market.find(1L));
        verify(listener).trade(book, Side.SELL, 10000L, 100L);
    }

    @Test
    void testExecuteOrderWithExcessQuantity() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        long remaining = market.execute(1L, 150L);
        assertEquals(0L, remaining);
        assertNull(market.find(1L));
        verify(listener).trade(book, Side.SELL, 10000L, 100L);
    }

    @Test
    void testExecuteOrderWithSpecificPrice() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        long remaining = market.execute(1L, 50L, 9990L);
        assertEquals(50L, remaining);
        verify(listener).trade(book, Side.SELL, 9990L, 50L);
    }

    @Test
    void testExecuteBuyOrderGeneratesSellTrade() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        market.execute(1L, 50L);
        ArgumentCaptor<Side> sideCaptor = ArgumentCaptor.forClass(Side.class);
        verify(listener).trade(eq(book), sideCaptor.capture(), anyLong(), anyLong());
        assertEquals(Side.SELL, sideCaptor.getValue());
    }

    @Test
    void testExecuteSellOrderGeneratesBuyTrade() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.SELL, 10100L, 100L);
        market.execute(1L, 50L);
        ArgumentCaptor<Side> sideCaptor = ArgumentCaptor.forClass(Side.class);
        verify(listener).trade(eq(book), sideCaptor.capture(), anyLong(), anyLong());
        assertEquals(Side.BUY, sideCaptor.getValue());
    }

    @Test
    void testCancelOrderWithUnknownOrderId() {
        long remaining = market.cancel(999L, 50L);
        assertEquals(0L, remaining);
        verifyNoInteractions(listener);
    }

    @Test
    void testCancelOrderPartially() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        long remaining = market.cancel(1L, 30L);
        assertEquals(70L, remaining);
        Order order = market.find(1L);
        assertNotNull(order);
        assertEquals(70L, order.getRemainingQuantity());
    }

    @Test
    void testCancelOrderFully() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        long remaining = market.cancel(1L, 100L);
        assertEquals(0L, remaining);
        assertNull(market.find(1L));
    }

    @Test
    void testCancelOrderWithExcessQuantity() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        long remaining = market.cancel(1L, 150L);
        assertEquals(0L, remaining);
        assertNull(market.find(1L));
    }

    @Test
    void testDeleteOrderWithUnknownOrderId() {
        market.delete(999L);
        verifyNoInteractions(listener);
    }

    @Test
    void testDeleteOrderRemovesFromMarket() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        market.delete(1L);
        assertNull(market.find(1L));
        verify(listener, times(2)).update(any(OrderBook.class), anyBoolean());
    }

    @Test
    void testDeleteOrderUpdatesOrderBook() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        assertEquals(100L, book.getBidSize(10000L));
        market.delete(1L);
        assertEquals(0L, book.getBidSize(10000L));
    }

    @Test
    void testMultipleOrdersAtSamePriceLevel() {
        market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        market.add(1000L, 2L, Side.BUY, 10000L, 50L);
        market.add(1000L, 3L, Side.BUY, 10000L, 75L);

        Order order1 = market.find(1L);
        Order order2 = market.find(2L);
        Order order3 = market.find(3L);

        assertNotNull(order1);
        assertNotNull(order2);
        assertNotNull(order3);
        assertEquals(100L, order1.getRemainingQuantity());
        assertEquals(50L, order2.getRemainingQuantity());
        assertEquals(75L, order3.getRemainingQuantity());
    }

    @Test
    void testUpdateEventWithBboFlag() {
        OrderBook book = market.open(1000L);
        market.add(1000L, 1L, Side.BUY, 10000L, 100L);
        verify(listener).update(book, true);

        market.add(1000L, 2L, Side.BUY, 9950L, 50L);
        verify(listener).update(book, false);
    }
}
