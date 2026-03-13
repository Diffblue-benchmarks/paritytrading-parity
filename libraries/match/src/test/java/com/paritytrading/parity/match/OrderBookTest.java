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
package com.paritytrading.parity.match;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;

public class OrderBookTest {

    @Test
    public void testEnterDuplicateOrderId() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 10L);
        book.enter(1L, Side.BUY, 100L, 10L);
        verify(listener, times(1)).add(1L, Side.BUY, 100L, 10L);
    }

    @Test
    public void testEnterSellOrder() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.SELL, 100L, 10L);
        verify(listener).add(1L, Side.SELL, 100L, 10L);
    }

    @Test
    public void testBuyMatchesAskWithPriceBreak() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.SELL, 110L, 10L);
        book.enter(2L, Side.BUY, 100L, 5L);
        verify(listener).add(1L, Side.SELL, 110L, 10L);
        verify(listener).add(2L, Side.BUY, 100L, 5L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testBuyMatchesAskPartially() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.SELL, 100L, 20L);
        book.enter(2L, Side.BUY, 100L, 10L);
        verify(listener).add(1L, Side.SELL, 100L, 20L);
        verify(listener).match(1L, 2L, Side.BUY, 100L, 10L, 10L);
    }

    @Test
    public void testBuyMatchesAskCompletely() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.SELL, 100L, 10L);
        book.enter(2L, Side.BUY, 100L, 10L);
        verify(listener).add(1L, Side.SELL, 100L, 10L);
        verify(listener).match(1L, 2L, Side.BUY, 100L, 10L, 0L);
    }

    @Test
    public void testBuyMatchesMultipleAsks() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.SELL, 100L, 5L);
        book.enter(2L, Side.SELL, 100L, 5L);
        book.enter(3L, Side.BUY, 100L, 15L);
        verify(listener).add(1L, Side.SELL, 100L, 5L);
        verify(listener).add(2L, Side.SELL, 100L, 5L);
        verify(listener).match(1L, 3L, Side.BUY, 100L, 5L, 0L);
        verify(listener).match(2L, 3L, Side.BUY, 100L, 5L, 0L);
        verify(listener).add(3L, Side.BUY, 100L, 5L);
    }

    @Test
    public void testSellMatchesBidWithPriceBreak() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 90L, 10L);
        book.enter(2L, Side.SELL, 100L, 5L);
        verify(listener).add(1L, Side.BUY, 90L, 10L);
        verify(listener).add(2L, Side.SELL, 100L, 5L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testSellMatchesBidPartially() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 20L);
        book.enter(2L, Side.SELL, 100L, 10L);
        verify(listener).add(1L, Side.BUY, 100L, 20L);
        verify(listener).match(1L, 2L, Side.SELL, 100L, 10L, 10L);
    }

    @Test
    public void testSellMatchesBidCompletely() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 10L);
        book.enter(2L, Side.SELL, 100L, 10L);
        verify(listener).add(1L, Side.BUY, 100L, 10L);
        verify(listener).match(1L, 2L, Side.SELL, 100L, 10L, 0L);
    }

    @Test
    public void testSellMatchesMultipleBids() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 5L);
        book.enter(2L, Side.BUY, 100L, 5L);
        book.enter(3L, Side.SELL, 100L, 15L);
        verify(listener).add(1L, Side.BUY, 100L, 5L);
        verify(listener).add(2L, Side.BUY, 100L, 5L);
        verify(listener).match(1L, 3L, Side.SELL, 100L, 5L, 0L);
        verify(listener).match(2L, 3L, Side.SELL, 100L, 5L, 0L);
        verify(listener).add(3L, Side.SELL, 100L, 5L);
    }

    @Test
    public void testCancelUnknownOrder() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.cancel(1L, 5L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testCancelWithSizeGreaterThanRemaining() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 10L);
        book.cancel(1L, 10L);
        verify(listener).add(1L, Side.BUY, 100L, 10L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testCancelWithSizeEqualToRemaining() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 10L);
        book.cancel(1L, 10L);
        verify(listener).add(1L, Side.BUY, 100L, 10L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testCancelPartialQuantityBuyOrder() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 10L);
        book.cancel(1L, 5L);
        verify(listener).add(1L, Side.BUY, 100L, 10L);
        verify(listener).cancel(1L, 5L, 5L);
    }

    @Test
    public void testCancelPartialQuantitySellOrder() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.SELL, 100L, 10L);
        book.cancel(1L, 5L);
        verify(listener).add(1L, Side.SELL, 100L, 10L);
        verify(listener).cancel(1L, 5L, 5L);
    }

    @Test
    public void testCancelEntireOrderBuy() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 10L);
        book.cancel(1L, 0L);
        verify(listener).add(1L, Side.BUY, 100L, 10L);
        verify(listener).cancel(1L, 10L, 0L);
    }

    @Test
    public void testCancelEntireOrderSell() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.SELL, 100L, 10L);
        book.cancel(1L, 0L);
        verify(listener).add(1L, Side.SELL, 100L, 10L);
        verify(listener).cancel(1L, 10L, 0L);
    }

    @Test
    public void testCompareBidsWithDifferentPrices() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.BUY, 100L, 10L);
        book.enter(2L, Side.BUY, 110L, 10L);
        book.enter(3L, Side.SELL, 90L, 25L);
        verify(listener).add(1L, Side.BUY, 100L, 10L);
        verify(listener).add(2L, Side.BUY, 110L, 10L);
        verify(listener).match(2L, 3L, Side.SELL, 110L, 10L, 0L);
        verify(listener).match(1L, 3L, Side.SELL, 100L, 10L, 0L);
        verify(listener).add(3L, Side.SELL, 90L, 5L);
    }

    @Test
    public void testCompareAsksWithDifferentPrices() {
        OrderBookListener listener = Mockito.mock(OrderBookListener.class);
        OrderBook book = new OrderBook(listener);
        book.enter(1L, Side.SELL, 100L, 10L);
        book.enter(2L, Side.SELL, 90L, 10L);
        book.enter(3L, Side.BUY, 110L, 25L);
        verify(listener).add(1L, Side.SELL, 100L, 10L);
        verify(listener).add(2L, Side.SELL, 90L, 10L);
        verify(listener).match(2L, 3L, Side.BUY, 90L, 10L, 0L);
        verify(listener).match(1L, 3L, Side.BUY, 100L, 10L, 0L);
        verify(listener).add(3L, Side.BUY, 110L, 5L);
    }
}
