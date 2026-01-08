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

import static com.paritytrading.parity.match.OrderBookEvents.*;
import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderBookClaudeTest {

    private OrderBookEvents events;
    private OrderBook book;

    @BeforeEach
    void setUp() {
        events = new OrderBookEvents();
        book = new OrderBook(events);
    }

    // Constructor tests
    @Test
    void constructorInitializesOrderBook() {
        OrderBookEvents listener = new OrderBookEvents();
        OrderBook orderBook = new OrderBook(listener);
        assertNotNull(orderBook);
    }

    // Enter method tests - duplicate order ID while order is still active
    @Test
    void enterWithDuplicateActiveOrderId() {
        book.enter(1, Side.BUY, 1000, 100);
        book.enter(1, Side.BUY, 900, 50);

        Event firstBid = new Add(1, Side.BUY, 1000, 100);

        assertEquals(asList(firstBid), events.collect());
    }

    @Test
    void enterWithDuplicateActiveOrderIdDifferentSide() {
        book.enter(1, Side.BUY, 1000, 100);
        book.enter(1, Side.SELL, 900, 50);

        Event bid = new Add(1, Side.BUY, 1000, 100);

        assertEquals(asList(bid), events.collect());
    }

    // Enter method tests - BUY side matching scenarios
    @Test
    void buyOrderMatchesExactlyWithAsk() {
        book.enter(1, Side.SELL, 1000, 100);
        book.enter(2, Side.BUY, 1000, 100);

        Event ask = new Add(1, Side.SELL, 1000, 100);
        Event match = new Match(1, 2, Side.BUY, 1000, 100, 0);

        assertEquals(asList(ask, match), events.collect());
    }

    @Test
    void buyOrderNoMatchWhenPriceTooLow() {
        book.enter(1, Side.SELL, 1001, 100);
        book.enter(2, Side.BUY, 1000, 100);

        Event ask = new Add(1, Side.SELL, 1001, 100);
        Event bid = new Add(2, Side.BUY, 1000, 100);

        assertEquals(asList(ask, bid), events.collect());
    }

    @Test
    void buyOrderPartiallyFillsLargerAsk() {
        book.enter(1, Side.SELL, 1000, 150);
        book.enter(2, Side.BUY, 1000, 100);

        Event ask = new Add(1, Side.SELL, 1000, 150);
        Event match = new Match(1, 2, Side.BUY, 1000, 100, 50);

        assertEquals(asList(ask, match), events.collect());
    }

    @Test
    void buyOrderConsumesMultipleAsks() {
        book.enter(1, Side.SELL, 1000, 50);
        book.enter(2, Side.SELL, 1000, 50);
        book.enter(3, Side.BUY, 1000, 100);

        Event firstAsk = new Add(1, Side.SELL, 1000, 50);
        Event secondAsk = new Add(2, Side.SELL, 1000, 50);
        Event firstMatch = new Match(1, 3, Side.BUY, 1000, 50, 0);
        Event secondMatch = new Match(2, 3, Side.BUY, 1000, 50, 0);

        assertEquals(asList(firstAsk, secondAsk, firstMatch, secondMatch), events.collect());
    }

    @Test
    void buyOrderMatchesAtAskPrice() {
        book.enter(1, Side.SELL, 990, 100);
        book.enter(2, Side.BUY, 1000, 100);

        Event ask = new Add(1, Side.SELL, 990, 100);
        Event match = new Match(1, 2, Side.BUY, 990, 100, 0);

        assertEquals(asList(ask, match), events.collect());
    }

    // Enter method tests - SELL side matching scenarios
    @Test
    void sellOrderMatchesExactlyWithBid() {
        book.enter(1, Side.BUY, 1000, 100);
        book.enter(2, Side.SELL, 1000, 100);

        Event bid = new Add(1, Side.BUY, 1000, 100);
        Event match = new Match(1, 2, Side.SELL, 1000, 100, 0);

        assertEquals(asList(bid, match), events.collect());
    }

    @Test
    void sellOrderNoMatchWhenPriceTooHigh() {
        book.enter(1, Side.BUY, 999, 100);
        book.enter(2, Side.SELL, 1000, 100);

        Event bid = new Add(1, Side.BUY, 999, 100);
        Event ask = new Add(2, Side.SELL, 1000, 100);

        assertEquals(asList(bid, ask), events.collect());
    }

    @Test
    void sellOrderPartiallyFillsLargerBid() {
        book.enter(1, Side.BUY, 1000, 150);
        book.enter(2, Side.SELL, 1000, 100);

        Event bid = new Add(1, Side.BUY, 1000, 150);
        Event match = new Match(1, 2, Side.SELL, 1000, 100, 50);

        assertEquals(asList(bid, match), events.collect());
    }

    @Test
    void sellOrderConsumesMultipleBids() {
        book.enter(1, Side.BUY, 1000, 50);
        book.enter(2, Side.BUY, 1000, 50);
        book.enter(3, Side.SELL, 1000, 100);

        Event firstBid = new Add(1, Side.BUY, 1000, 50);
        Event secondBid = new Add(2, Side.BUY, 1000, 50);
        Event firstMatch = new Match(1, 3, Side.SELL, 1000, 50, 0);
        Event secondMatch = new Match(2, 3, Side.SELL, 1000, 50, 0);

        assertEquals(asList(firstBid, secondBid, firstMatch, secondMatch), events.collect());
    }

    @Test
    void sellOrderMatchesAtBidPrice() {
        book.enter(1, Side.BUY, 1010, 100);
        book.enter(2, Side.SELL, 1000, 100);

        Event bid = new Add(1, Side.BUY, 1010, 100);
        Event match = new Match(1, 2, Side.SELL, 1010, 100, 0);

        assertEquals(asList(bid, match), events.collect());
    }

    // Enter method tests - price/time priority
    @Test
    void bidsPriorityByPriceThenTime() {
        book.enter(1, Side.BUY, 1000, 100);
        book.enter(2, Side.BUY, 1001, 100);
        book.enter(3, Side.BUY, 1000, 100);
        book.enter(4, Side.SELL, 1000, 150);

        Event firstBid = new Add(1, Side.BUY, 1000, 100);
        Event secondBid = new Add(2, Side.BUY, 1001, 100);
        Event thirdBid = new Add(3, Side.BUY, 1000, 100);
        Event firstMatch = new Match(2, 4, Side.SELL, 1001, 100, 0);
        Event secondMatch = new Match(1, 4, Side.SELL, 1000, 50, 50);

        assertEquals(asList(firstBid, secondBid, thirdBid, firstMatch, secondMatch), events.collect());
    }

    @Test
    void asksPriorityByPriceThenTime() {
        book.enter(1, Side.SELL, 1000, 100);
        book.enter(2, Side.SELL, 999, 100);
        book.enter(3, Side.SELL, 1000, 100);
        book.enter(4, Side.BUY, 1000, 150);

        Event firstAsk = new Add(1, Side.SELL, 1000, 100);
        Event secondAsk = new Add(2, Side.SELL, 999, 100);
        Event thirdAsk = new Add(3, Side.SELL, 1000, 100);
        Event firstMatch = new Match(2, 4, Side.BUY, 999, 100, 0);
        Event secondMatch = new Match(1, 4, Side.BUY, 1000, 50, 50);

        assertEquals(asList(firstAsk, secondAsk, thirdAsk, firstMatch, secondMatch), events.collect());
    }

    // Cancel method tests - full cancel
    @Test
    void cancelEntireOrder() {
        book.enter(1, Side.BUY, 1000, 100);
        book.cancel(1, 0);

        Event bid = new Add(1, Side.BUY, 1000, 100);
        Event cancel = new Cancel(1, 100, 0);

        assertEquals(asList(bid, cancel), events.collect());
    }

    @Test
    void cancelEntireSellOrder() {
        book.enter(1, Side.SELL, 1000, 100);
        book.cancel(1, 0);

        Event ask = new Add(1, Side.SELL, 1000, 100);
        Event cancel = new Cancel(1, 100, 0);

        assertEquals(asList(ask, cancel), events.collect());
    }

    // Cancel method tests - partial cancel
    @Test
    void cancelPartiallyReducesOrder() {
        book.enter(1, Side.BUY, 1000, 100);
        book.cancel(1, 60);

        Event bid = new Add(1, Side.BUY, 1000, 100);
        Event cancel = new Cancel(1, 40, 60);

        assertEquals(asList(bid, cancel), events.collect());
    }

    @Test
    void partialCancelDoesNotRemoveOrderFromBook() {
        book.enter(1, Side.BUY, 1000, 100);
        book.cancel(1, 75);
        book.enter(2, Side.SELL, 1000, 80);

        Event bid = new Add(1, Side.BUY, 1000, 100);
        Event cancel = new Cancel(1, 25, 75);
        Event match = new Match(1, 2, Side.SELL, 1000, 75, 0);
        Event ask = new Add(2, Side.SELL, 1000, 5);

        assertEquals(asList(bid, cancel, match, ask), events.collect());
    }

    // Cancel method tests - ineffective cancel (size >= remainingQuantity)
    @Test
    void cancelWithSizeEqualToRemainingQuantityDoesNothing() {
        book.enter(1, Side.BUY, 1000, 100);
        book.cancel(1, 100);

        Event bid = new Add(1, Side.BUY, 1000, 100);

        assertEquals(asList(bid), events.collect());
    }

    @Test
    void cancelWithSizeGreaterThanRemainingQuantityDoesNothing() {
        book.enter(1, Side.BUY, 1000, 100);
        book.cancel(1, 150);

        Event bid = new Add(1, Side.BUY, 1000, 100);

        assertEquals(asList(bid), events.collect());
    }

    @Test
    void cancelAfterPartialFillWithSizeEqualToRemainingDoesNothing() {
        book.enter(1, Side.BUY, 1000, 100);
        book.enter(2, Side.SELL, 1000, 40);
        book.cancel(1, 60);

        Event bid = new Add(1, Side.BUY, 1000, 100);
        Event match = new Match(1, 2, Side.SELL, 1000, 40, 60);

        assertEquals(asList(bid, match), events.collect());
    }

    // Cancel method tests - unknown order
    @Test
    void cancelUnknownOrderDoesNothing() {
        book.cancel(999, 0);

        assertTrue(events.collect().isEmpty());
    }

    @Test
    void cancelAfterOrderFullyMatched() {
        book.enter(1, Side.BUY, 1000, 100);
        book.enter(2, Side.SELL, 1000, 100);
        book.cancel(1, 0);

        Event bid = new Add(1, Side.BUY, 1000, 100);
        Event match = new Match(1, 2, Side.SELL, 1000, 100, 0);

        assertEquals(asList(bid, match), events.collect());
    }

    // Cancel method tests - cancel reduces order then allow matching
    @Test
    void cancelReducesThenBuyOrderMatches() {
        book.enter(1, Side.SELL, 1000, 100);
        book.cancel(1, 50);
        book.enter(2, Side.BUY, 1000, 60);

        Event ask = new Add(1, Side.SELL, 1000, 100);
        Event cancel = new Cancel(1, 50, 50);
        Event match = new Match(1, 2, Side.BUY, 1000, 50, 0);
        Event bid = new Add(2, Side.BUY, 1000, 10);

        assertEquals(asList(ask, cancel, match, bid), events.collect());
    }

    // Complex scenarios
    @Test
    void multipleOrdersAndCancellations() {
        book.enter(1, Side.BUY, 1000, 100);
        book.enter(2, Side.BUY, 1001, 50);
        book.enter(3, Side.SELL, 1002, 75);
        book.cancel(2, 25);
        book.enter(4, Side.SELL, 1000, 150);

        Event firstBid = new Add(1, Side.BUY, 1000, 100);
        Event secondBid = new Add(2, Side.BUY, 1001, 50);
        Event ask = new Add(3, Side.SELL, 1002, 75);
        Event cancel = new Cancel(2, 25, 25);
        Event firstMatch = new Match(2, 4, Side.SELL, 1001, 25, 0);
        Event secondMatch = new Match(1, 4, Side.SELL, 1000, 100, 0);
        Event finalAsk = new Add(4, Side.SELL, 1000, 25);

        assertEquals(asList(firstBid, secondBid, ask, cancel, firstMatch, secondMatch, finalAsk),
                     events.collect());
    }

    @Test
    void orderBookEmptyAfterAllMatches() {
        book.enter(1, Side.BUY, 1000, 100);
        book.enter(2, Side.SELL, 1000, 100);
        book.enter(3, Side.BUY, 1000, 50);

        Event firstBid = new Add(1, Side.BUY, 1000, 100);
        Event match = new Match(1, 2, Side.SELL, 1000, 100, 0);
        Event secondBid = new Add(3, Side.BUY, 1000, 50);

        assertEquals(asList(firstBid, match, secondBid), events.collect());
    }

    // Edge cases with specific sizes
    @Test
    void enterOrderWithSize1() {
        book.enter(1, Side.BUY, 1000, 1);

        Event bid = new Add(1, Side.BUY, 1000, 1);

        assertEquals(asList(bid), events.collect());
    }

    @Test
    void cancelToSize1() {
        book.enter(1, Side.BUY, 1000, 100);
        book.cancel(1, 1);

        Event bid = new Add(1, Side.BUY, 1000, 100);
        Event cancel = new Cancel(1, 99, 1);

        assertEquals(asList(bid, cancel), events.collect());
    }

    @Test
    void largeQuantities() {
        book.enter(1, Side.BUY, 1000, 1000000);
        book.enter(2, Side.SELL, 1000, 1000000);

        Event bid = new Add(1, Side.BUY, 1000, 1000000);
        Event match = new Match(1, 2, Side.SELL, 1000, 1000000, 0);

        assertEquals(asList(bid, match), events.collect());
    }

    @Test
    void largePrice() {
        book.enter(1, Side.BUY, 999999999L, 100);
        book.enter(2, Side.SELL, 999999999L, 100);

        Event bid = new Add(1, Side.BUY, 999999999L, 100);
        Event match = new Match(1, 2, Side.SELL, 999999999L, 100, 0);

        assertEquals(asList(bid, match), events.collect());
    }
}
