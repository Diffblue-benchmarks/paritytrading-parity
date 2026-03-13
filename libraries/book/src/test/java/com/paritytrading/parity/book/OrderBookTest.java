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

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {

    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        orderBook = new OrderBook(1000L);
    }

    @Test
    void testGetInstrument() {
        assertEquals(1000L, orderBook.getInstrument());
    }

    @Test
    void testGetBestBidPriceWhenEmpty() {
        assertEquals(0L, orderBook.getBestBidPrice());
    }

    @Test
    void testGetBestAskPriceWhenEmpty() {
        assertEquals(0L, orderBook.getBestAskPrice());
    }

    @Test
    void testAddBidUpdatesBestBidPrice() {
        orderBook.add(Side.BUY, 10000L, 100L);
        assertEquals(10000L, orderBook.getBestBidPrice());
    }

    @Test
    void testAddAskUpdatesBestAskPrice() {
        orderBook.add(Side.SELL, 10100L, 100L);
        assertEquals(10100L, orderBook.getBestAskPrice());
    }

    @Test
    void testAddMultipleBidsReturnsBestPrice() {
        orderBook.add(Side.BUY, 10000L, 100L);
        orderBook.add(Side.BUY, 10050L, 50L);
        orderBook.add(Side.BUY, 9950L, 75L);
        assertEquals(10050L, orderBook.getBestBidPrice());
    }

    @Test
    void testAddMultipleAsksReturnsLowestPrice() {
        orderBook.add(Side.SELL, 10100L, 100L);
        orderBook.add(Side.SELL, 10050L, 50L);
        orderBook.add(Side.SELL, 10150L, 75L);
        assertEquals(10050L, orderBook.getBestAskPrice());
    }

    @Test
    void testGetBidSizeReturnsCorrectQuantity() {
        orderBook.add(Side.BUY, 10000L, 100L);
        assertEquals(100L, orderBook.getBidSize(10000L));
    }

    @Test
    void testGetAskSizeReturnsCorrectQuantity() {
        orderBook.add(Side.SELL, 10100L, 100L);
        assertEquals(100L, orderBook.getAskSize(10100L));
    }

    @Test
    void testAddToExistingPriceLevel() {
        orderBook.add(Side.BUY, 10000L, 100L);
        orderBook.add(Side.BUY, 10000L, 50L);
        assertEquals(150L, orderBook.getBidSize(10000L));
    }

    @Test
    void testUpdateReducesSize() {
        orderBook.add(Side.BUY, 10000L, 100L);
        orderBook.update(Side.BUY, 10000L, -30L);
        assertEquals(70L, orderBook.getBidSize(10000L));
    }

    @Test
    void testUpdateIncreasesSize() {
        orderBook.add(Side.BUY, 10000L, 100L);
        orderBook.update(Side.BUY, 10000L, 50L);
        assertEquals(150L, orderBook.getBidSize(10000L));
    }

    @Test
    void testUpdateRemovesPriceLevelWhenSizeBecomesZero() {
        orderBook.add(Side.BUY, 10000L, 100L);
        orderBook.update(Side.BUY, 10000L, -100L);
        assertEquals(0L, orderBook.getBidSize(10000L));
        assertEquals(0L, orderBook.getBestBidPrice());
    }

    @Test
    void testUpdateRemovesPriceLevelWhenSizeBecomesNegative() {
        orderBook.add(Side.SELL, 10100L, 100L);
        orderBook.update(Side.SELL, 10100L, -150L);
        assertEquals(0L, orderBook.getAskSize(10100L));
        assertEquals(0L, orderBook.getBestAskPrice());
    }

    @Test
    void testAddReturnsTrueWhenBestLevelChanges() {
        assertTrue(orderBook.add(Side.BUY, 10000L, 100L));
    }

    @Test
    void testAddReturnsFalseWhenNotBestLevel() {
        orderBook.add(Side.BUY, 10000L, 100L);
        assertFalse(orderBook.add(Side.BUY, 9950L, 50L));
    }

    @Test
    void testUpdateReturnsTrueWhenBestLevelAffected() {
        orderBook.add(Side.BUY, 10000L, 100L);
        assertTrue(orderBook.update(Side.BUY, 10000L, -50L));
    }

    @Test
    void testUpdateReturnsFalseWhenNonBestLevelAffected() {
        orderBook.add(Side.BUY, 10000L, 100L);
        orderBook.add(Side.BUY, 9950L, 50L);
        assertFalse(orderBook.update(Side.BUY, 9950L, -25L));
    }

    @Test
    void testGetBidPricesReturnsAllPriceLevels() {
        orderBook.add(Side.BUY, 10000L, 100L);
        orderBook.add(Side.BUY, 9950L, 50L);
        orderBook.add(Side.BUY, 10050L, 75L);

        assertEquals(3, orderBook.getBidPrices().size());
        assertTrue(orderBook.getBidPrices().contains(10000L));
        assertTrue(orderBook.getBidPrices().contains(9950L));
        assertTrue(orderBook.getBidPrices().contains(10050L));
    }

    @Test
    void testGetAskPricesReturnsAllPriceLevels() {
        orderBook.add(Side.SELL, 10100L, 100L);
        orderBook.add(Side.SELL, 10150L, 50L);
        orderBook.add(Side.SELL, 10050L, 75L);

        assertEquals(3, orderBook.getAskPrices().size());
        assertTrue(orderBook.getAskPrices().contains(10100L));
        assertTrue(orderBook.getAskPrices().contains(10150L));
        assertTrue(orderBook.getAskPrices().contains(10050L));
    }

    @Test
    void testBidPricesAreSortedDescending() {
        orderBook.add(Side.BUY, 10000L, 100L);
        orderBook.add(Side.BUY, 9950L, 50L);
        orderBook.add(Side.BUY, 10050L, 75L);

        long[] prices = orderBook.getBidPrices().toLongArray();
        assertEquals(10050L, prices[0]);
        assertEquals(10000L, prices[1]);
        assertEquals(9950L, prices[2]);
    }

    @Test
    void testAskPricesAreSortedAscending() {
        orderBook.add(Side.SELL, 10100L, 100L);
        orderBook.add(Side.SELL, 10150L, 50L);
        orderBook.add(Side.SELL, 10050L, 75L);

        long[] prices = orderBook.getAskPrices().toLongArray();
        assertEquals(10050L, prices[0]);
        assertEquals(10100L, prices[1]);
        assertEquals(10150L, prices[2]);
    }
}
