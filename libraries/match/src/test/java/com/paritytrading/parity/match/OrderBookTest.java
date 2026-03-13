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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderBookTest {

    @Mock
    private OrderBookListener listener;

    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        orderBook = new OrderBook(listener);
    }

    @Test
    void testEnterBuyOrderWithNoMatches() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterSellOrderWithNoMatches() {
        orderBook.enter(1L, Side.SELL, 10100L, 100L);
        verify(listener).add(1L, Side.SELL, 10100L, 100L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterDuplicateOrderId() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.enter(1L, Side.BUY, 10050L, 50L);
        verify(listener, times(1)).add(anyLong(), any(Side.class), anyLong(), anyLong());
    }

    @Test
    void testEnterBuyOrderMatchesSellOrder() {
        orderBook.enter(1L, Side.SELL, 10100L, 100L);
        orderBook.enter(2L, Side.BUY, 10100L, 50L);

        verify(listener).add(1L, Side.SELL, 10100L, 100L);
        verify(listener).match(1L, 2L, Side.BUY, 10100L, 50L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterSellOrderMatchesBuyOrder() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.enter(2L, Side.SELL, 10000L, 50L);

        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verify(listener).match(1L, 2L, Side.SELL, 10000L, 50L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterBuyOrderMatchesFullySellOrder() {
        orderBook.enter(1L, Side.SELL, 10100L, 100L);
        orderBook.enter(2L, Side.BUY, 10100L, 100L);

        verify(listener).add(1L, Side.SELL, 10100L, 100L);
        verify(listener).match(1L, 2L, Side.BUY, 10100L, 100L, 0L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterBuyOrderPartiallyFillsAndRests() {
        orderBook.enter(1L, Side.SELL, 10100L, 50L);
        orderBook.enter(2L, Side.BUY, 10100L, 100L);

        verify(listener).add(1L, Side.SELL, 10100L, 50L);
        verify(listener).match(1L, 2L, Side.BUY, 10100L, 50L, 0L);
        verify(listener).add(2L, Side.BUY, 10100L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterSellOrderPartiallyFillsAndRests() {
        orderBook.enter(1L, Side.BUY, 10000L, 50L);
        orderBook.enter(2L, Side.SELL, 10000L, 100L);

        verify(listener).add(1L, Side.BUY, 10000L, 50L);
        verify(listener).match(1L, 2L, Side.SELL, 10000L, 50L, 0L);
        verify(listener).add(2L, Side.SELL, 10000L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterBuyOrderMatchesMultipleSellOrders() {
        orderBook.enter(1L, Side.SELL, 10100L, 30L);
        orderBook.enter(2L, Side.SELL, 10100L, 40L);
        orderBook.enter(3L, Side.BUY, 10100L, 60L);

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).add(1L, Side.SELL, 10100L, 30L);
        inOrder.verify(listener).add(2L, Side.SELL, 10100L, 40L);
        inOrder.verify(listener).match(1L, 3L, Side.BUY, 10100L, 30L, 0L);
        inOrder.verify(listener).match(2L, 3L, Side.BUY, 10100L, 30L, 10L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterSellOrderMatchesMultipleBuyOrders() {
        orderBook.enter(1L, Side.BUY, 10000L, 30L);
        orderBook.enter(2L, Side.BUY, 10000L, 40L);
        orderBook.enter(3L, Side.SELL, 10000L, 60L);

        InOrder inOrder = inOrder(listener);
        inOrder.verify(listener).add(1L, Side.BUY, 10000L, 30L);
        inOrder.verify(listener).add(2L, Side.BUY, 10000L, 40L);
        inOrder.verify(listener).match(1L, 3L, Side.SELL, 10000L, 30L, 0L);
        inOrder.verify(listener).match(2L, 3L, Side.SELL, 10000L, 30L, 10L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterBuyOrderDoesNotMatchHigherSellPrice() {
        orderBook.enter(1L, Side.SELL, 10100L, 100L);
        orderBook.enter(2L, Side.BUY, 10050L, 50L);

        verify(listener).add(1L, Side.SELL, 10100L, 100L);
        verify(listener).add(2L, Side.BUY, 10050L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterSellOrderDoesNotMatchLowerBuyPrice() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.enter(2L, Side.SELL, 10050L, 50L);

        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verify(listener).add(2L, Side.SELL, 10050L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testPriceTimePriorityForBids() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.enter(2L, Side.BUY, 10050L, 100L);
        orderBook.enter(3L, Side.BUY, 10000L, 100L);
        orderBook.enter(4L, Side.SELL, 10000L, 150L);

        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verify(listener).add(2L, Side.BUY, 10050L, 100L);
        verify(listener).add(3L, Side.BUY, 10000L, 100L);
        verify(listener).match(2L, 4L, Side.SELL, 10050L, 100L, 0L);
        verify(listener).match(1L, 4L, Side.SELL, 10000L, 50L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testPriceTimePriorityForAsks() {
        orderBook.enter(1L, Side.SELL, 10100L, 100L);
        orderBook.enter(2L, Side.SELL, 10050L, 100L);
        orderBook.enter(3L, Side.SELL, 10100L, 100L);
        orderBook.enter(4L, Side.BUY, 10100L, 150L);

        verify(listener).add(1L, Side.SELL, 10100L, 100L);
        verify(listener).add(2L, Side.SELL, 10050L, 100L);
        verify(listener).add(3L, Side.SELL, 10100L, 100L);
        verify(listener).match(2L, 4L, Side.BUY, 10050L, 100L, 0L);
        verify(listener).match(1L, 4L, Side.BUY, 10100L, 50L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testCancelOrderWithUnknownOrderId() {
        orderBook.cancel(999L, 50L);
        verifyNoInteractions(listener);
    }

    @Test
    void testCancelOrderPartially() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.cancel(1L, 60L);

        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verify(listener).cancel(1L, 40L, 60L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testCancelOrderFully() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.cancel(1L, 0L);

        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verify(listener).cancel(1L, 100L, 0L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testCancelOrderWithSameSize() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.cancel(1L, 100L);

        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testCancelOrderWithLargerSize() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.cancel(1L, 150L);

        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testCancelAfterPartialExecution() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.enter(2L, Side.SELL, 10000L, 30L);
        orderBook.cancel(1L, 40L);

        verify(listener).cancel(1L, 30L, 40L);
    }

    @Test
    void testComplexMatchingScenario() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.enter(2L, Side.BUY, 10050L, 50L);
        orderBook.enter(3L, Side.SELL, 10100L, 75L);
        orderBook.enter(4L, Side.SELL, 10000L, 100L);

        verify(listener).add(1L, Side.BUY, 10000L, 100L);
        verify(listener).add(2L, Side.BUY, 10050L, 50L);
        verify(listener).add(3L, Side.SELL, 10100L, 75L);
        verify(listener).match(2L, 4L, Side.SELL, 10050L, 50L, 0L);
        verify(listener).match(1L, 4L, Side.SELL, 10000L, 50L, 50L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testExecutionAtRestingOrderPrice() {
        orderBook.enter(1L, Side.BUY, 10000L, 100L);
        orderBook.enter(2L, Side.SELL, 9950L, 50L);

        verify(listener).match(1L, 2L, Side.SELL, 10000L, 50L, 50L);
    }

    @Test
    void testEnterBuyOrderSweepsMultiplePriceLevels() {
        orderBook.enter(1L, Side.SELL, 10050L, 30L);
        orderBook.enter(2L, Side.SELL, 10100L, 40L);
        orderBook.enter(3L, Side.SELL, 10150L, 50L);
        orderBook.enter(4L, Side.BUY, 10200L, 100L);

        verify(listener).add(1L, Side.SELL, 10050L, 30L);
        verify(listener).add(2L, Side.SELL, 10100L, 40L);
        verify(listener).add(3L, Side.SELL, 10150L, 50L);
        verify(listener).match(1L, 4L, Side.BUY, 10050L, 30L, 0L);
        verify(listener).match(2L, 4L, Side.BUY, 10100L, 40L, 0L);
        verify(listener).match(3L, 4L, Side.BUY, 10150L, 30L, 20L);
        verifyNoMoreInteractions(listener);
    }

    @Test
    void testEnterSellOrderSweepsMultiplePriceLevels() {
        orderBook.enter(1L, Side.BUY, 10150L, 30L);
        orderBook.enter(2L, Side.BUY, 10100L, 40L);
        orderBook.enter(3L, Side.BUY, 10050L, 50L);
        orderBook.enter(4L, Side.SELL, 10000L, 100L);

        verify(listener).add(1L, Side.BUY, 10150L, 30L);
        verify(listener).add(2L, Side.BUY, 10100L, 40L);
        verify(listener).add(3L, Side.BUY, 10050L, 50L);
        verify(listener).match(1L, 4L, Side.SELL, 10150L, 30L, 0L);
        verify(listener).match(2L, 4L, Side.SELL, 10100L, 40L, 0L);
        verify(listener).match(3L, 4L, Side.SELL, 10050L, 30L, 20L);
        verifyNoMoreInteractions(listener);
    }
}
