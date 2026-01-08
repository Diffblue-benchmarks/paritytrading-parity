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

class OrderClaudeTest {

    private static final long INSTRUMENT = 1;
    private static final long ORDER_ID = 100;
    private static final long PRICE = 1000;
    private static final long SIZE = 200;

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
    void testConstructorAndGettersForBuyOrder() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        assertNotNull(order);
        assertEquals(orderBook, order.getOrderBook());
        assertEquals(Side.BUY, order.getSide());
        assertEquals(PRICE, order.getPrice());
        assertEquals(SIZE, order.getRemainingQuantity());
    }

    @Test
    void testConstructorAndGettersForSellOrder() {
        market.add(INSTRUMENT, ORDER_ID, Side.SELL, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        assertNotNull(order);
        assertEquals(orderBook, order.getOrderBook());
        assertEquals(Side.SELL, order.getSide());
        assertEquals(PRICE, order.getPrice());
        assertEquals(SIZE, order.getRemainingQuantity());
    }

    @Test
    void testConstructorWithZeroSize() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, 0);
        Order order = market.find(ORDER_ID);

        assertNotNull(order);
        assertEquals(0, order.getRemainingQuantity());
    }

    @Test
    void testConstructorWithZeroPrice() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, 0, SIZE);
        Order order = market.find(ORDER_ID);

        assertNotNull(order);
        assertEquals(0, order.getPrice());
    }

    @Test
    void testConstructorWithLargeValues() {
        long largePrice = Long.MAX_VALUE;
        long largeSize = Long.MAX_VALUE;
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, largePrice, largeSize);
        Order order = market.find(ORDER_ID);

        assertNotNull(order);
        assertEquals(largePrice, order.getPrice());
        assertEquals(largeSize, order.getRemainingQuantity());
    }

    @Test
    void testGetOrderBook() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        OrderBook retrievedBook = order.getOrderBook();
        assertNotNull(retrievedBook);
        assertSame(orderBook, retrievedBook);
        assertEquals(INSTRUMENT, retrievedBook.getInstrument());
    }

    @Test
    void testGetPrice() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        assertEquals(PRICE, order.getPrice());
    }

    @Test
    void testGetPriceWithDifferentValues() {
        long price1 = 500;
        long price2 = 1500;

        market.add(INSTRUMENT, ORDER_ID, Side.BUY, price1, SIZE);
        market.add(INSTRUMENT, ORDER_ID + 1, Side.SELL, price2, SIZE);

        Order order1 = market.find(ORDER_ID);
        Order order2 = market.find(ORDER_ID + 1);

        assertEquals(price1, order1.getPrice());
        assertEquals(price2, order2.getPrice());
    }

    @Test
    void testGetSideBuy() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        assertEquals(Side.BUY, order.getSide());
    }

    @Test
    void testGetSideSell() {
        market.add(INSTRUMENT, ORDER_ID, Side.SELL, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        assertEquals(Side.SELL, order.getSide());
    }

    @Test
    void testGetRemainingQuantity() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        assertEquals(SIZE, order.getRemainingQuantity());
    }

    @Test
    void testSetRemainingQuantity() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        long newQuantity = 150;
        market.modify(ORDER_ID, newQuantity);

        assertEquals(newQuantity, order.getRemainingQuantity());
    }

    @Test
    void testSetRemainingQuantityToZero() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        market.modify(ORDER_ID, 0);
        Order order = market.find(ORDER_ID);

        assertNull(order);
    }

    @Test
    void testSetRemainingQuantityIncreasing() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        long newQuantity = 300;
        market.modify(ORDER_ID, newQuantity);

        assertEquals(newQuantity, order.getRemainingQuantity());
    }

    @Test
    void testSetRemainingQuantityDecreasing() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        long newQuantity = 50;
        market.modify(ORDER_ID, newQuantity);

        assertEquals(newQuantity, order.getRemainingQuantity());
    }

    @Test
    void testReduceFullQuantity() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        long quantityToReduce = SIZE;
        long remainingQuantity = market.cancel(ORDER_ID, quantityToReduce);

        assertEquals(0, remainingQuantity);
        assertNull(market.find(ORDER_ID));
    }

    @Test
    void testReducePartialQuantity() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        long quantityToReduce = 50;
        long remainingQuantity = market.cancel(ORDER_ID, quantityToReduce);

        assertEquals(SIZE - quantityToReduce, remainingQuantity);
        assertEquals(SIZE - quantityToReduce, order.getRemainingQuantity());
    }

    @Test
    void testReduceMultipleTimes() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        long firstReduction = 50;
        long secondReduction = 30;

        market.cancel(ORDER_ID, firstReduction);
        assertEquals(SIZE - firstReduction, order.getRemainingQuantity());

        market.cancel(ORDER_ID, secondReduction);
        assertEquals(SIZE - firstReduction - secondReduction, order.getRemainingQuantity());
    }

    @Test
    void testReduceToExactlyZero() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);

        long remainingQuantity = market.cancel(ORDER_ID, SIZE);

        assertEquals(0, remainingQuantity);
        assertNull(market.find(ORDER_ID));
    }

    @Test
    void testReduceMoreThanRemaining() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        long quantityToReduce = SIZE + 100;
        long remainingQuantity = market.cancel(ORDER_ID, quantityToReduce);

        assertEquals(0, remainingQuantity);
        assertNull(market.find(ORDER_ID));
    }

    @Test
    void testReduceViaExecute() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        long quantityToExecute = 50;
        long remainingQuantity = market.execute(ORDER_ID, quantityToExecute);

        assertEquals(SIZE - quantityToExecute, remainingQuantity);
        assertEquals(SIZE - quantityToExecute, order.getRemainingQuantity());
    }

    @Test
    void testReduceViaExecuteFullQuantity() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);

        long remainingQuantity = market.execute(ORDER_ID, SIZE);

        assertEquals(0, remainingQuantity);
        assertNull(market.find(ORDER_ID));
    }

    @Test
    void testMultipleOrdersIndependence() {
        long orderId1 = 100;
        long orderId2 = 101;
        long price1 = 1000;
        long price2 = 1100;
        long size1 = 200;
        long size2 = 300;

        market.add(INSTRUMENT, orderId1, Side.BUY, price1, size1);
        market.add(INSTRUMENT, orderId2, Side.SELL, price2, size2);

        Order order1 = market.find(orderId1);
        Order order2 = market.find(orderId2);

        assertEquals(Side.BUY, order1.getSide());
        assertEquals(Side.SELL, order2.getSide());
        assertEquals(price1, order1.getPrice());
        assertEquals(price2, order2.getPrice());
        assertEquals(size1, order1.getRemainingQuantity());
        assertEquals(size2, order2.getRemainingQuantity());

        market.cancel(orderId1, 50);
        assertEquals(size1 - 50, order1.getRemainingQuantity());
        assertEquals(size2, order2.getRemainingQuantity());
    }

    @Test
    void testOrderBookReference() {
        market.add(INSTRUMENT, ORDER_ID, Side.BUY, PRICE, SIZE);
        Order order = market.find(ORDER_ID);

        OrderBook book = order.getOrderBook();
        assertNotNull(book);
        assertEquals(PRICE, book.getBestBidPrice());
        assertEquals(SIZE, book.getBidSize(PRICE));
    }
}
