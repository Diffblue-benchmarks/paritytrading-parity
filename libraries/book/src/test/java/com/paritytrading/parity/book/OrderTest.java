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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class OrderTest {

    @Test
    public void testConstructorAndGetters() {
        OrderBook orderBook = new OrderBook(1L);
        long price = 1000L;
        long size = 100L;

        Order order = new Order(orderBook, Side.BUY, price, size);

        assertNotNull(order);
        assertSame(orderBook, order.getOrderBook());
        assertEquals(Side.BUY, order.getSide());
        assertEquals(price, order.getPrice());
        assertEquals(size, order.getRemainingQuantity());
    }

    @Test
    public void testGetOrderBook() {
        OrderBook orderBook = new OrderBook(2L);
        Order order = new Order(orderBook, Side.SELL, 2000L, 200L);

        assertSame(orderBook, order.getOrderBook());
    }

    @Test
    public void testGetPrice() {
        OrderBook orderBook = new OrderBook(3L);
        long price = 1500L;
        Order order = new Order(orderBook, Side.BUY, price, 150L);

        assertEquals(price, order.getPrice());
    }

    @Test
    public void testGetSide() {
        OrderBook orderBook = new OrderBook(4L);
        Order buyOrder = new Order(orderBook, Side.BUY, 1000L, 100L);
        Order sellOrder = new Order(orderBook, Side.SELL, 2000L, 200L);

        assertEquals(Side.BUY, buyOrder.getSide());
        assertEquals(Side.SELL, sellOrder.getSide());
    }

    @Test
    public void testGetRemainingQuantity() {
        OrderBook orderBook = new OrderBook(5L);
        long quantity = 250L;
        Order order = new Order(orderBook, Side.BUY, 1000L, quantity);

        assertEquals(quantity, order.getRemainingQuantity());
    }

    @Test
    public void testSetRemainingQuantity() {
        OrderBook orderBook = new OrderBook(6L);
        Order order = new Order(orderBook, Side.BUY, 1000L, 100L);

        long newQuantity = 50L;
        order.setRemainingQuantity(newQuantity);

        assertEquals(newQuantity, order.getRemainingQuantity());
    }

    @Test
    public void testReduce() {
        OrderBook orderBook = new OrderBook(7L);
        long initialQuantity = 100L;
        Order order = new Order(orderBook, Side.BUY, 1000L, initialQuantity);

        long reduceBy = 30L;
        order.reduce(reduceBy);

        assertEquals(initialQuantity - reduceBy, order.getRemainingQuantity());
    }

    @Test
    public void testReduceMultipleTimes() {
        OrderBook orderBook = new OrderBook(8L);
        long initialQuantity = 200L;
        Order order = new Order(orderBook, Side.SELL, 2000L, initialQuantity);

        order.reduce(50L);
        assertEquals(150L, order.getRemainingQuantity());

        order.reduce(30L);
        assertEquals(120L, order.getRemainingQuantity());

        order.reduce(120L);
        assertEquals(0L, order.getRemainingQuantity());
    }
}
