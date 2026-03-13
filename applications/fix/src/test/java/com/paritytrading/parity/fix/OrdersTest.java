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
package com.paritytrading.parity.fix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrdersTest {

    @Test
    void testConstructor() {
        Orders orders = new Orders();
        assertNotNull(orders);
    }

    @Test
    void testAdd() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", 'B', "AAPL", 100.0);

        orders.add(order);

        Order found = orders.findByClOrdID("ORDER1");
        assertNotNull(found);
        assertEquals("ORDER1", found.getClOrdID());
    }

    @Test
    void testFindByClOrdIDFound() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", 'B', "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER2", "ACCT2", 'S', "GOOG", 50.0);

        orders.add(order1);
        orders.add(order2);

        Order found = orders.findByClOrdID("ORDER2");
        assertNotNull(found);
        assertEquals("ORDER2", found.getClOrdID());
        assertEquals(2L, found.getOrderEntryID());
    }

    @Test
    void testFindByClOrdIDNotFound() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", 'B', "AAPL", 100.0);

        orders.add(order);

        Order found = orders.findByClOrdID("NONEXISTENT");
        assertNull(found);
    }

    @Test
    void testFindByClOrdIDEmptyList() {
        Orders orders = new Orders();

        Order found = orders.findByClOrdID("ORDER1");
        assertNull(found);
    }

    @Test
    void testFindByOrderEntryIDFound() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", 'B', "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER2", "ACCT2", 'S', "GOOG", 50.0);

        orders.add(order1);
        orders.add(order2);

        Order found = orders.findByOrderEntryID(2L);
        assertNotNull(found);
        assertEquals(2L, found.getOrderEntryID());
        assertEquals("ORDER2", found.getClOrdID());
    }

    @Test
    void testFindByOrderEntryIDNotFound() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", 'B', "AAPL", 100.0);

        orders.add(order);

        Order found = orders.findByOrderEntryID(999L);
        assertNull(found);
    }

    @Test
    void testFindByOrderEntryIDEmptyList() {
        Orders orders = new Orders();

        Order found = orders.findByOrderEntryID(1L);
        assertNull(found);
    }

    @Test
    void testRemoveByOrderEntryIDFound() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", 'B', "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER2", "ACCT2", 'S', "GOOG", 50.0);
        Order order3 = new Order(3L, "ORDER3", "ACCT3", 'B', "MSFT", 75.0);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        orders.removeByOrderEntryID(2L);

        assertNull(orders.findByOrderEntryID(2L));
        assertNotNull(orders.findByOrderEntryID(1L));
        assertNotNull(orders.findByOrderEntryID(3L));
    }

    @Test
    void testRemoveByOrderEntryIDNotFound() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", 'B', "AAPL", 100.0);

        orders.add(order);

        orders.removeByOrderEntryID(999L);

        assertNotNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testRemoveByOrderEntryIDEmptyList() {
        Orders orders = new Orders();

        orders.removeByOrderEntryID(1L);
    }
}
