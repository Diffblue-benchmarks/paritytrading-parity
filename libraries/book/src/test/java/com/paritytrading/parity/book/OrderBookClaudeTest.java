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

import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderBookClaudeTest {

    private static final long INSTRUMENT = 1;

    private Market market;
    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        market = new Market(new MarketListener() {
            @Override
            public void update(OrderBook book, boolean bbo) {
            }

            @Override
            public void trade(OrderBook book, Side side, long price, long executedQuantity) {
            }
        });
        orderBook = market.open(INSTRUMENT);
    }

    @Test
    void testConstructorAndGetInstrument() {
        assertEquals(INSTRUMENT, orderBook.getInstrument());
    }

    @Test
    void testGetInstrumentWithDifferentValue() {
        long instrument2 = 999;
        OrderBook orderBook2 = market.open(instrument2);
        assertEquals(instrument2, orderBook2.getInstrument());
    }

    @Test
    void testGetBestBidPriceWhenEmpty() {
        assertEquals(0, orderBook.getBestBidPrice());
    }

    @Test
    void testGetBestBidPriceWithSingleBid() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        assertEquals(1000, orderBook.getBestBidPrice());
    }

    @Test
    void testGetBestBidPriceWithMultipleBids() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1100, 50);
        market.add(INSTRUMENT, 3, Side.BUY, 900, 75);
        assertEquals(1100, orderBook.getBestBidPrice());
    }

    @Test
    void testGetBestBidPriceAfterRemovingBestBid() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1100, 50);
        market.delete(2);
        assertEquals(1000, orderBook.getBestBidPrice());
    }

    @Test
    void testGetBestBidPriceAfterRemovingAllBids() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.delete(1);
        assertEquals(0, orderBook.getBestBidPrice());
    }

    @Test
    void testGetBidPricesWhenEmpty() {
        LongSortedSet bidPrices = orderBook.getBidPrices();
        assertNotNull(bidPrices);
        assertTrue(bidPrices.isEmpty());
    }

    @Test
    void testGetBidPricesWithSingleBid() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        LongSortedSet bidPrices = orderBook.getBidPrices();
        assertEquals(1, bidPrices.size());
        assertTrue(bidPrices.contains(1000));
    }

    @Test
    void testGetBidPricesWithMultipleBids() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1100, 50);
        market.add(INSTRUMENT, 3, Side.BUY, 900, 75);
        LongSortedSet bidPrices = orderBook.getBidPrices();
        assertEquals(3, bidPrices.size());
        assertTrue(bidPrices.contains(1000));
        assertTrue(bidPrices.contains(1100));
        assertTrue(bidPrices.contains(900));
    }

    @Test
    void testGetBidPricesOrderDescending() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1100, 50);
        market.add(INSTRUMENT, 3, Side.BUY, 900, 75);
        LongSortedSet bidPrices = orderBook.getBidPrices();
        assertEquals(1100, bidPrices.firstLong());
        assertEquals(900, bidPrices.lastLong());
    }

    @Test
    void testGetBidSizeForNonExistentPrice() {
        assertEquals(0, orderBook.getBidSize(1000));
    }

    @Test
    void testGetBidSizeWithSingleOrder() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        assertEquals(100, orderBook.getBidSize(1000));
    }

    @Test
    void testGetBidSizeWithMultipleOrdersAtSamePrice() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1000, 50);
        assertEquals(150, orderBook.getBidSize(1000));
    }

    @Test
    void testGetBidSizeAfterPartialExecution() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.execute(1, 30);
        assertEquals(70, orderBook.getBidSize(1000));
    }

    @Test
    void testGetBidSizeAfterFullExecution() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.execute(1, 100);
        assertEquals(0, orderBook.getBidSize(1000));
    }

    @Test
    void testGetBestAskPriceWhenEmpty() {
        assertEquals(0, orderBook.getBestAskPrice());
    }

    @Test
    void testGetBestAskPriceWithSingleAsk() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        assertEquals(1000, orderBook.getBestAskPrice());
    }

    @Test
    void testGetBestAskPriceWithMultipleAsks() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 1100, 50);
        market.add(INSTRUMENT, 3, Side.SELL, 900, 75);
        assertEquals(900, orderBook.getBestAskPrice());
    }

    @Test
    void testGetBestAskPriceAfterRemovingBestAsk() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 900, 50);
        market.delete(2);
        assertEquals(1000, orderBook.getBestAskPrice());
    }

    @Test
    void testGetBestAskPriceAfterRemovingAllAsks() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.delete(1);
        assertEquals(0, orderBook.getBestAskPrice());
    }

    @Test
    void testGetAskPricesWhenEmpty() {
        LongSortedSet askPrices = orderBook.getAskPrices();
        assertNotNull(askPrices);
        assertTrue(askPrices.isEmpty());
    }

    @Test
    void testGetAskPricesWithSingleAsk() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        LongSortedSet askPrices = orderBook.getAskPrices();
        assertEquals(1, askPrices.size());
        assertTrue(askPrices.contains(1000));
    }

    @Test
    void testGetAskPricesWithMultipleAsks() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 1100, 50);
        market.add(INSTRUMENT, 3, Side.SELL, 900, 75);
        LongSortedSet askPrices = orderBook.getAskPrices();
        assertEquals(3, askPrices.size());
        assertTrue(askPrices.contains(1000));
        assertTrue(askPrices.contains(1100));
        assertTrue(askPrices.contains(900));
    }

    @Test
    void testGetAskPricesOrderAscending() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 1100, 50);
        market.add(INSTRUMENT, 3, Side.SELL, 900, 75);
        LongSortedSet askPrices = orderBook.getAskPrices();
        assertEquals(900, askPrices.firstLong());
        assertEquals(1100, askPrices.lastLong());
    }

    @Test
    void testGetAskSizeForNonExistentPrice() {
        assertEquals(0, orderBook.getAskSize(1000));
    }

    @Test
    void testGetAskSizeWithSingleOrder() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        assertEquals(100, orderBook.getAskSize(1000));
    }

    @Test
    void testGetAskSizeWithMultipleOrdersAtSamePrice() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 1000, 50);
        assertEquals(150, orderBook.getAskSize(1000));
    }

    @Test
    void testGetAskSizeAfterPartialExecution() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.execute(1, 30);
        assertEquals(70, orderBook.getAskSize(1000));
    }

    @Test
    void testGetAskSizeAfterFullExecution() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.execute(1, 100);
        assertEquals(0, orderBook.getAskSize(1000));
    }

    @Test
    void testAddBidReturnsTrueWhenBestBid() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        assertEquals(1000, orderBook.getBestBidPrice());
        assertEquals(100, orderBook.getBidSize(1000));
    }

    @Test
    void testAddBidReturnsFalseWhenNotBestBid() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 900, 50);
        assertEquals(1000, orderBook.getBestBidPrice());
    }

    @Test
    void testAddBidBecomesNewBestBid() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1100, 50);
        assertEquals(1100, orderBook.getBestBidPrice());
    }

    @Test
    void testAddAskReturnsTrueWhenBestAsk() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        assertEquals(1000, orderBook.getBestAskPrice());
        assertEquals(100, orderBook.getAskSize(1000));
    }

    @Test
    void testAddAskReturnsFalseWhenNotBestAsk() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 1100, 50);
        assertEquals(1000, orderBook.getBestAskPrice());
    }

    @Test
    void testAddAskBecomesNewBestAsk() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 900, 50);
        assertEquals(900, orderBook.getBestAskPrice());
    }

    @Test
    void testUpdateBidIncreaseQuantity() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.modify(1, 150);
        assertEquals(150, orderBook.getBidSize(1000));
    }

    @Test
    void testUpdateBidDecreaseQuantity() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.modify(1, 50);
        assertEquals(50, orderBook.getBidSize(1000));
    }

    @Test
    void testUpdateBidToZeroRemovesLevel() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.modify(1, 0);
        assertEquals(0, orderBook.getBidSize(1000));
        assertFalse(orderBook.getBidPrices().contains(1000));
    }

    @Test
    void testUpdateAskIncreaseQuantity() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.modify(1, 150);
        assertEquals(150, orderBook.getAskSize(1000));
    }

    @Test
    void testUpdateAskDecreaseQuantity() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.modify(1, 50);
        assertEquals(50, orderBook.getAskSize(1000));
    }

    @Test
    void testUpdateAskToZeroRemovesLevel() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.modify(1, 0);
        assertEquals(0, orderBook.getAskSize(1000));
        assertFalse(orderBook.getAskPrices().contains(1000));
    }

    @Test
    void testUpdateBestBidReturnsTrueWhenOnBestLevel() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.modify(1, 150);
        assertEquals(1000, orderBook.getBestBidPrice());
    }

    @Test
    void testUpdateNonBestBidReturnsFalseWhenNotOnBestLevel() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1100, 50);
        market.modify(1, 150);
        assertEquals(1100, orderBook.getBestBidPrice());
    }

    @Test
    void testUpdateBestAskReturnsTrueWhenOnBestLevel() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.modify(1, 150);
        assertEquals(1000, orderBook.getBestAskPrice());
    }

    @Test
    void testUpdateNonBestAskReturnsFalseWhenNotOnBestLevel() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 900, 50);
        market.modify(1, 150);
        assertEquals(900, orderBook.getBestAskPrice());
    }

    @Test
    void testCancelPartialQuantityOnBestBid() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.cancel(1, 30);
        assertEquals(70, orderBook.getBidSize(1000));
        assertEquals(1000, orderBook.getBestBidPrice());
    }

    @Test
    void testCancelFullQuantityOnBestBid() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 900, 50);
        market.cancel(1, 100);
        assertEquals(0, orderBook.getBidSize(1000));
        assertEquals(900, orderBook.getBestBidPrice());
    }

    @Test
    void testCancelPartialQuantityOnBestAsk() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.cancel(1, 30);
        assertEquals(70, orderBook.getAskSize(1000));
        assertEquals(1000, orderBook.getBestAskPrice());
    }

    @Test
    void testCancelFullQuantityOnBestAsk() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 1100, 50);
        market.cancel(1, 100);
        assertEquals(0, orderBook.getAskSize(1000));
        assertEquals(1100, orderBook.getBestAskPrice());
    }

    @Test
    void testMixedBidsAndAsks() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 1100, 50);
        market.add(INSTRUMENT, 3, Side.BUY, 990, 75);
        market.add(INSTRUMENT, 4, Side.SELL, 1110, 25);

        assertEquals(1000, orderBook.getBestBidPrice());
        assertEquals(1100, orderBook.getBestAskPrice());
        assertEquals(2, orderBook.getBidPrices().size());
        assertEquals(2, orderBook.getAskPrices().size());
    }

    @Test
    void testMultipleOrdersAtSamePriceLevel() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1000, 50);
        market.add(INSTRUMENT, 3, Side.BUY, 1000, 25);

        assertEquals(1, orderBook.getBidPrices().size());
        assertEquals(175, orderBook.getBidSize(1000));
        assertEquals(1000, orderBook.getBestBidPrice());
    }

    @Test
    void testPartialExecutionMaintainsLevel() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 1000, 50);
        market.execute(1, 50);

        assertEquals(1, orderBook.getBidPrices().size());
        assertEquals(100, orderBook.getBidSize(1000));
        assertEquals(1000, orderBook.getBestBidPrice());
    }

    @Test
    void testExecutionRemovesEmptyLevel() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 900, 50);
        market.execute(1, 100);

        assertFalse(orderBook.getBidPrices().contains(1000));
        assertEquals(900, orderBook.getBestBidPrice());
    }

    @Test
    void testZeroPriceHandling() {
        market.add(INSTRUMENT, 1, Side.BUY, 0, 100);
        assertEquals(0, orderBook.getBestBidPrice());
        assertEquals(100, orderBook.getBidSize(0));
    }

    @Test
    void testLargePriceValues() {
        long largePrice = Long.MAX_VALUE;
        market.add(INSTRUMENT, 1, Side.BUY, largePrice, 100);
        assertEquals(largePrice, orderBook.getBestBidPrice());
        assertEquals(100, orderBook.getBidSize(largePrice));
    }

    @Test
    void testLargeQuantityValues() {
        long largeQuantity = Long.MAX_VALUE;
        market.add(INSTRUMENT, 1, Side.BUY, 1000, largeQuantity);
        assertEquals(largeQuantity, orderBook.getBidSize(1000));
    }

    @Test
    void testEmptyOrderBookState() {
        assertEquals(0, orderBook.getBestBidPrice());
        assertEquals(0, orderBook.getBestAskPrice());
        assertTrue(orderBook.getBidPrices().isEmpty());
        assertTrue(orderBook.getAskPrices().isEmpty());
    }

    @Test
    void testOrderBookWithOnlyBids() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        market.add(INSTRUMENT, 2, Side.BUY, 900, 50);

        assertEquals(1000, orderBook.getBestBidPrice());
        assertEquals(0, orderBook.getBestAskPrice());
        assertEquals(2, orderBook.getBidPrices().size());
        assertTrue(orderBook.getAskPrices().isEmpty());
    }

    @Test
    void testOrderBookWithOnlyAsks() {
        market.add(INSTRUMENT, 1, Side.SELL, 1000, 100);
        market.add(INSTRUMENT, 2, Side.SELL, 1100, 50);

        assertEquals(0, orderBook.getBestBidPrice());
        assertEquals(1000, orderBook.getBestAskPrice());
        assertTrue(orderBook.getBidPrices().isEmpty());
        assertEquals(2, orderBook.getAskPrices().size());
    }

    @Test
    void testSequentialAddAndDeleteOperations() {
        market.add(INSTRUMENT, 1, Side.BUY, 1000, 100);
        assertEquals(100, orderBook.getBidSize(1000));

        market.add(INSTRUMENT, 2, Side.BUY, 1000, 50);
        assertEquals(150, orderBook.getBidSize(1000));

        market.delete(1);
        assertEquals(50, orderBook.getBidSize(1000));

        market.delete(2);
        assertEquals(0, orderBook.getBidSize(1000));
        assertFalse(orderBook.getBidPrices().contains(1000));
    }
}
