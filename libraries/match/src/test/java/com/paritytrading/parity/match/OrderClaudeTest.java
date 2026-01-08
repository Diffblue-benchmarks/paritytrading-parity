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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderClaudeTest {

    @Test
    void constructorInitializesFieldsCorrectly() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        assertEquals(1L, order.getNumber());
        assertEquals(100L, order.getId());
        assertEquals(Side.BUY, order.getSide());
        assertEquals(5000L, order.getPrice());
        assertEquals(250L, order.getRemainingQuantity());
    }

    @Test
    void constructorWithSellSide() {
        Order order = new Order(2L, 200L, Side.SELL, 6000L, 300L);

        assertEquals(2L, order.getNumber());
        assertEquals(200L, order.getId());
        assertEquals(Side.SELL, order.getSide());
        assertEquals(6000L, order.getPrice());
        assertEquals(300L, order.getRemainingQuantity());
    }

    @Test
    void constructorWithZeroQuantity() {
        Order order = new Order(3L, 300L, Side.BUY, 1000L, 0L);

        assertEquals(0L, order.getRemainingQuantity());
    }

    @Test
    void constructorWithMinimumLongValues() {
        Order order = new Order(Long.MIN_VALUE, Long.MIN_VALUE, Side.BUY, Long.MIN_VALUE, Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, order.getNumber());
        assertEquals(Long.MIN_VALUE, order.getId());
        assertEquals(Long.MIN_VALUE, order.getPrice());
        assertEquals(Long.MIN_VALUE, order.getRemainingQuantity());
    }

    @Test
    void constructorWithMaximumLongValues() {
        Order order = new Order(Long.MAX_VALUE, Long.MAX_VALUE, Side.SELL, Long.MAX_VALUE, Long.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, order.getNumber());
        assertEquals(Long.MAX_VALUE, order.getId());
        assertEquals(Long.MAX_VALUE, order.getPrice());
        assertEquals(Long.MAX_VALUE, order.getRemainingQuantity());
    }

    @Test
    void getNumberReturnsCorrectValue() {
        Order order = new Order(42L, 100L, Side.BUY, 5000L, 250L);

        assertEquals(42L, order.getNumber());
    }

    @Test
    void getIdReturnsCorrectValue() {
        Order order = new Order(1L, 999L, Side.BUY, 5000L, 250L);

        assertEquals(999L, order.getId());
    }

    @Test
    void getSideReturnsBuy() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        assertEquals(Side.BUY, order.getSide());
    }

    @Test
    void getSideReturnsSell() {
        Order order = new Order(1L, 100L, Side.SELL, 5000L, 250L);

        assertEquals(Side.SELL, order.getSide());
    }

    @Test
    void getPriceReturnsCorrectValue() {
        Order order = new Order(1L, 100L, Side.BUY, 12345L, 250L);

        assertEquals(12345L, order.getPrice());
    }

    @Test
    void getRemainingQuantityReturnsInitialQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 500L);

        assertEquals(500L, order.getRemainingQuantity());
    }

    @Test
    void reduceDecreasesRemainingQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.reduce(50L);

        assertEquals(200L, order.getRemainingQuantity());
    }

    @Test
    void reduceWithFullQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 100L);

        order.reduce(100L);

        assertEquals(0L, order.getRemainingQuantity());
    }

    @Test
    void reduceWithZeroQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.reduce(0L);

        assertEquals(250L, order.getRemainingQuantity());
    }

    @Test
    void reduceMultipleTimes() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 300L);

        order.reduce(50L);
        order.reduce(100L);
        order.reduce(50L);

        assertEquals(100L, order.getRemainingQuantity());
    }

    @Test
    void reduceCanResultInNegativeQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 100L);

        order.reduce(150L);

        assertEquals(-50L, order.getRemainingQuantity());
    }

    @Test
    void resizeSetsNewQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.resize(500L);

        assertEquals(500L, order.getRemainingQuantity());
    }

    @Test
    void resizeToZero() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.resize(0L);

        assertEquals(0L, order.getRemainingQuantity());
    }

    @Test
    void resizeToLargerQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 100L);

        order.resize(500L);

        assertEquals(500L, order.getRemainingQuantity());
    }

    @Test
    void resizeToSmallerQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 500L);

        order.resize(100L);

        assertEquals(100L, order.getRemainingQuantity());
    }

    @Test
    void resizeMultipleTimes() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.resize(100L);
        order.resize(300L);
        order.resize(50L);

        assertEquals(50L, order.getRemainingQuantity());
    }

    @Test
    void resizeToNegativeQuantity() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.resize(-100L);

        assertEquals(-100L, order.getRemainingQuantity());
    }

    @Test
    void resizeAfterReduce() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.reduce(50L);
        order.resize(300L);

        assertEquals(300L, order.getRemainingQuantity());
    }

    @Test
    void reduceAfterResize() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.resize(300L);
        order.reduce(100L);

        assertEquals(200L, order.getRemainingQuantity());
    }

    @Test
    void immutableFieldsDoNotChange() {
        Order order = new Order(1L, 100L, Side.BUY, 5000L, 250L);

        order.reduce(50L);
        order.resize(100L);

        assertEquals(1L, order.getNumber());
        assertEquals(100L, order.getId());
        assertEquals(Side.BUY, order.getSide());
        assertEquals(5000L, order.getPrice());
    }

}
