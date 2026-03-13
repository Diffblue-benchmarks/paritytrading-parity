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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {

    @Test
    public void testGetId() {
        Order order = new Order(1L, 123L, Side.BUY, 100L, 50L);
        assertEquals(123L, order.getId());
    }

    @Test
    public void testGetSide() {
        Order order = new Order(1L, 123L, Side.BUY, 100L, 50L);
        assertEquals(Side.BUY, order.getSide());
    }

    @Test
    public void testGetRemainingQuantity() {
        Order order = new Order(1L, 123L, Side.BUY, 100L, 50L);
        assertEquals(50L, order.getRemainingQuantity());
    }

    @Test
    public void testReduce() {
        Order order = new Order(1L, 123L, Side.BUY, 100L, 50L);
        order.reduce(10L);
        assertEquals(40L, order.getRemainingQuantity());
    }

    @Test
    public void testResize() {
        Order order = new Order(1L, 123L, Side.BUY, 100L, 50L);
        order.resize(30L);
        assertEquals(30L, order.getRemainingQuantity());
    }
}
