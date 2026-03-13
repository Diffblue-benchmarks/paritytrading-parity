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

class OrderTest {

    private OrderBook orderBook;
    private Order buyOrder;
    private Order sellOrder;

    @BeforeEach
    void setUp() {
        orderBook = new OrderBook(1000L);
        buyOrder = new Order(orderBook, Side.BUY, 10000L, 100L);
        sellOrder = new Order(orderBook, Side.SELL, 10100L, 150L);
    }

    @Test
    void testGetOrderBook() {
        assertSame(orderBook, buyOrder.getOrderBook());
        assertSame(orderBook, sellOrder.getOrderBook());
    }

    @Test
    void testGetPrice() {
        assertEquals(10000L, buyOrder.getPrice());
        assertEquals(10100L, sellOrder.getPrice());
    }

    @Test
    void testGetSide() {
        assertEquals(Side.BUY, buyOrder.getSide());
        assertEquals(Side.SELL, sellOrder.getSide());
    }

    @Test
    void testGetRemainingQuantity() {
        assertEquals(100L, buyOrder.getRemainingQuantity());
        assertEquals(150L, sellOrder.getRemainingQuantity());
    }

    @Test
    void testSetRemainingQuantity() {
        buyOrder.setRemainingQuantity(75L);
        assertEquals(75L, buyOrder.getRemainingQuantity());
    }

    @Test
    void testSetRemainingQuantityToZero() {
        buyOrder.setRemainingQuantity(0L);
        assertEquals(0L, buyOrder.getRemainingQuantity());
    }

    @Test
    void testReduce() {
        buyOrder.reduce(30L);
        assertEquals(70L, buyOrder.getRemainingQuantity());
    }

    @Test
    void testReduceMultipleTimes() {
        buyOrder.reduce(20L);
        buyOrder.reduce(30L);
        buyOrder.reduce(10L);
        assertEquals(40L, buyOrder.getRemainingQuantity());
    }

    @Test
    void testReduceToZero() {
        buyOrder.reduce(100L);
        assertEquals(0L, buyOrder.getRemainingQuantity());
    }

    @Test
    void testReduceDoesNotValidateNegative() {
        buyOrder.reduce(150L);
        assertEquals(-50L, buyOrder.getRemainingQuantity());
    }

    @Test
    void testOrderMaintainsImmutablePrice() {
        long originalPrice = buyOrder.getPrice();
        buyOrder.reduce(50L);
        assertEquals(originalPrice, buyOrder.getPrice());
    }

    @Test
    void testOrderMaintainsImmutableSide() {
        Side originalSide = buyOrder.getSide();
        buyOrder.reduce(50L);
        assertEquals(originalSide, buyOrder.getSide());
    }
}
