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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdersTest {

    private Orders orders;

    @BeforeEach
    void setUp() {
        orders = new Orders();
    }

    @Test
    void findByClOrdIDReturnsNullWhenEmpty() {
        assertNull(orders.findByClOrdID("order1"));
    }

    @Test
    void findByOrderEntryIDReturnsNullWhenEmpty() {
        assertNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void addAndFindByClOrdID() {
        Order order = new Order(1L, "clOrd1", "account1", '1', "AAPL", 100.0);

        orders.add(order);

        assertEquals(order, orders.findByClOrdID("clOrd1"));
    }

    @Test
    void addAndFindByOrderEntryID() {
        Order order = new Order(42L, "clOrd1", "account1", '1', "AAPL", 100.0);

        orders.add(order);

        assertEquals(order, orders.findByOrderEntryID(42L));
    }

    @Test
    void findByClOrdIDReturnsNullWhenNotFound() {
        Order order = new Order(1L, "clOrd1", "account1", '1', "AAPL", 100.0);

        orders.add(order);

        assertNull(orders.findByClOrdID("nonexistent"));
    }

    @Test
    void findByOrderEntryIDReturnsNullWhenNotFound() {
        Order order = new Order(1L, "clOrd1", "account1", '1', "AAPL", 100.0);

        orders.add(order);

        assertNull(orders.findByOrderEntryID(999L));
    }

    @Test
    void findByClOrdIDWithMultipleOrders() {
        Order order1 = new Order(1L, "clOrd1", "account1", '1', "AAPL", 100.0);
        Order order2 = new Order(2L, "clOrd2", "account1", '2', "MSFT", 200.0);

        orders.add(order1);
        orders.add(order2);

        assertEquals(order1, orders.findByClOrdID("clOrd1"));
        assertEquals(order2, orders.findByClOrdID("clOrd2"));
    }

    @Test
    void findByOrderEntryIDWithMultipleOrders() {
        Order order1 = new Order(1L, "clOrd1", "account1", '1', "AAPL", 100.0);
        Order order2 = new Order(2L, "clOrd2", "account1", '2', "MSFT", 200.0);

        orders.add(order1);
        orders.add(order2);

        assertEquals(order1, orders.findByOrderEntryID(1L));
        assertEquals(order2, orders.findByOrderEntryID(2L));
    }

    @Test
    void removeByOrderEntryID() {
        Order order = new Order(1L, "clOrd1", "account1", '1', "AAPL", 100.0);

        orders.add(order);
        orders.removeByOrderEntryID(1L);

        assertNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void removeByOrderEntryIDKeepsOtherOrders() {
        Order order1 = new Order(1L, "clOrd1", "account1", '1', "AAPL", 100.0);
        Order order2 = new Order(2L, "clOrd2", "account1", '2', "MSFT", 200.0);

        orders.add(order1);
        orders.add(order2);
        orders.removeByOrderEntryID(1L);

        assertNull(orders.findByOrderEntryID(1L));
        assertEquals(order2, orders.findByOrderEntryID(2L));
    }

    @Test
    void removeByOrderEntryIDNonexistent() {
        Order order = new Order(1L, "clOrd1", "account1", '1', "AAPL", 100.0);

        orders.add(order);
        orders.removeByOrderEntryID(999L);

        assertEquals(order, orders.findByOrderEntryID(1L));
    }
}
