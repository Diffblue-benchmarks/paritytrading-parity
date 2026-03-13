package com.paritytrading.parity.book;

import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderBookTest {

    @Test
    public void testGetBestBidPriceEmpty() {
        OrderBook book = new OrderBook(1);
        assertEquals(0, book.getBestBidPrice());
    }

    @Test
    public void testGetBestBidPriceWithBids() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        book.add(Side.BUY, 95, 5);
        assertEquals(100, book.getBestBidPrice());
    }

    @Test
    public void testGetBestAskPriceEmpty() {
        OrderBook book = new OrderBook(1);
        assertEquals(0, book.getBestAskPrice());
    }

    @Test
    public void testGetBestAskPriceWithAsks() {
        OrderBook book = new OrderBook(1);
        book.add(Side.SELL, 105, 10);
        book.add(Side.SELL, 110, 5);
        assertEquals(105, book.getBestAskPrice());
    }

    @Test
    public void testGetBidPrices() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        book.add(Side.BUY, 95, 5);
        LongSortedSet bidPrices = book.getBidPrices();
        assertNotNull(bidPrices);
        assertEquals(2, bidPrices.size());
        assertTrue(bidPrices.contains(100));
        assertTrue(bidPrices.contains(95));
    }

    @Test
    public void testGetAskPrices() {
        OrderBook book = new OrderBook(1);
        book.add(Side.SELL, 105, 10);
        book.add(Side.SELL, 110, 5);
        LongSortedSet askPrices = book.getAskPrices();
        assertNotNull(askPrices);
        assertEquals(2, askPrices.size());
        assertTrue(askPrices.contains(105));
        assertTrue(askPrices.contains(110));
    }

    @Test
    public void testGetBidSize() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        assertEquals(10, book.getBidSize(100));
    }

    @Test
    public void testGetAskSize() {
        OrderBook book = new OrderBook(1);
        book.add(Side.SELL, 105, 15);
        assertEquals(15, book.getAskSize(105));
    }

    @Test
    public void testAddBidOnBestLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 95, 5);
        boolean isBestLevel = book.add(Side.BUY, 100, 10);
        assertTrue(isBestLevel);
        assertEquals(100, book.getBestBidPrice());
    }

    @Test
    public void testAddBidNotOnBestLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        boolean isBestLevel = book.add(Side.BUY, 95, 5);
        assertFalse(isBestLevel);
    }

    @Test
    public void testAddAskOnBestLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.SELL, 110, 5);
        boolean isBestLevel = book.add(Side.SELL, 105, 10);
        assertTrue(isBestLevel);
        assertEquals(105, book.getBestAskPrice());
    }

    @Test
    public void testAddAskNotOnBestLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.SELL, 105, 10);
        boolean isBestLevel = book.add(Side.SELL, 110, 5);
        assertFalse(isBestLevel);
    }

    @Test
    public void testUpdateIncreaseSize() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        boolean isBestLevel = book.update(Side.BUY, 100, 5);
        assertTrue(isBestLevel);
        assertEquals(15, book.getBidSize(100));
    }

    @Test
    public void testUpdateDecreaseSize() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        boolean isBestLevel = book.update(Side.BUY, 100, -3);
        assertTrue(isBestLevel);
        assertEquals(7, book.getBidSize(100));
    }

    @Test
    public void testUpdateRemoveLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        boolean isBestLevel = book.update(Side.BUY, 100, -10);
        assertTrue(isBestLevel);
        assertEquals(0, book.getBidSize(100));
        assertEquals(0, book.getBestBidPrice());
    }

    @Test
    public void testUpdateNotOnBestLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        book.add(Side.BUY, 95, 5);
        boolean isBestLevel = book.update(Side.BUY, 95, 5);
        assertFalse(isBestLevel);
        assertEquals(10, book.getBidSize(95));
    }

    @Test
    public void testUpdateRemoveNonBestLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        book.add(Side.BUY, 95, 5);
        boolean isBestLevel = book.update(Side.BUY, 95, -5);
        assertFalse(isBestLevel);
        assertEquals(0, book.getBidSize(95));
        assertEquals(100, book.getBestBidPrice());
    }

    @Test
    public void testAddMultipleBidLevels() {
        OrderBook book = new OrderBook(1);
        book.add(Side.BUY, 100, 10);
        book.add(Side.BUY, 100, 5);
        assertEquals(15, book.getBidSize(100));
    }

    @Test
    public void testAddMultipleAskLevels() {
        OrderBook book = new OrderBook(1);
        book.add(Side.SELL, 105, 10);
        book.add(Side.SELL, 105, 5);
        assertEquals(15, book.getAskSize(105));
    }

    @Test
    public void testUpdateAskOnBestLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.SELL, 105, 10);
        boolean isBestLevel = book.update(Side.SELL, 105, 5);
        assertTrue(isBestLevel);
        assertEquals(15, book.getAskSize(105));
    }

    @Test
    public void testUpdateAskRemoveLevel() {
        OrderBook book = new OrderBook(1);
        book.add(Side.SELL, 105, 10);
        boolean isBestLevel = book.update(Side.SELL, 105, -10);
        assertTrue(isBestLevel);
        assertEquals(0, book.getAskSize(105));
        assertEquals(0, book.getBestAskPrice());
    }
}
