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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.paritytrading.philadelphia.fix44.FIX44Enumerations.*;
import static org.junit.jupiter.api.Assertions.*;

class OrdersTest {

    private Orders orders;
    private Order order1;
    private Order order2;
    private Order order3;

    @BeforeEach
    void setUp() {
        orders = new Orders();
        order1 = new Order(1L, "CLORD-001", "ACCT-001", SideValues.Buy, "AAPL", 100.0);
        order2 = new Order(2L, "CLORD-002", "ACCT-002", SideValues.Sell, "MSFT", 50.0);
        order3 = new Order(3L, "CLORD-003", "ACCT-003", SideValues.Buy, "GOOGL", 75.0);
    }

    @Test
    void testAddOrder() {
        orders.add(order1);
        assertNotNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testAddMultipleOrders() {
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        assertNotNull(orders.findByOrderEntryID(1L));
        assertNotNull(orders.findByOrderEntryID(2L));
        assertNotNull(orders.findByOrderEntryID(3L));
    }

    @Test
    void testFindByClOrdIDReturnsCorrectOrder() {
        orders.add(order1);
        orders.add(order2);

        Order found = orders.findByClOrdID("CLORD-001");
        assertSame(order1, found);
        assertEquals("AAPL", found.getSymbol());
    }

    @Test
    void testFindByClOrdIDReturnsNullWhenNotFound() {
        orders.add(order1);
        assertNull(orders.findByClOrdID("CLORD-999"));
    }

    @Test
    void testFindByClOrdIDWithEmptyOrders() {
        assertNull(orders.findByClOrdID("CLORD-001"));
    }

    @Test
    void testFindByOrderEntryIDReturnsCorrectOrder() {
        orders.add(order1);
        orders.add(order2);

        Order found = orders.findByOrderEntryID(2L);
        assertSame(order2, found);
        assertEquals("MSFT", found.getSymbol());
    }

    @Test
    void testFindByOrderEntryIDReturnsNullWhenNotFound() {
        orders.add(order1);
        assertNull(orders.findByOrderEntryID(999L));
    }

    @Test
    void testFindByOrderEntryIDWithEmptyOrders() {
        assertNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testRemoveByOrderEntryID() {
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        orders.removeByOrderEntryID(2L);

        assertNotNull(orders.findByOrderEntryID(1L));
        assertNull(orders.findByOrderEntryID(2L));
        assertNotNull(orders.findByOrderEntryID(3L));
    }

    @Test
    void testRemoveByOrderEntryIDWhenNotFound() {
        orders.add(order1);
        orders.removeByOrderEntryID(999L);
        assertNotNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testRemoveByOrderEntryIDWithEmptyOrders() {
        orders.removeByOrderEntryID(1L);
        assertNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testRemoveFirstOrder() {
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        orders.removeByOrderEntryID(1L);

        assertNull(orders.findByOrderEntryID(1L));
        assertNotNull(orders.findByOrderEntryID(2L));
        assertNotNull(orders.findByOrderEntryID(3L));
    }

    @Test
    void testRemoveLastOrder() {
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        orders.removeByOrderEntryID(3L);

        assertNotNull(orders.findByOrderEntryID(1L));
        assertNotNull(orders.findByOrderEntryID(2L));
        assertNull(orders.findByOrderEntryID(3L));
    }

    @Test
    void testAddAndRemoveMultipleTimes() {
        orders.add(order1);
        assertNotNull(orders.findByOrderEntryID(1L));

        orders.removeByOrderEntryID(1L);
        assertNull(orders.findByOrderEntryID(1L));

        orders.add(order1);
        assertNotNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testFindReturnsFirstMatchForClOrdID() {
        Order duplicateClOrdIdOrder = new Order(4L, "CLORD-001", "ACCT-004", SideValues.Sell, "AMZN", 25.0);
        orders.add(order1);
        orders.add(duplicateClOrdIdOrder);

        Order found = orders.findByClOrdID("CLORD-001");
        assertSame(order1, found);
    }

    @Test
    void testRemoveOnlyRemovesFirstMatch() {
        orders.add(order1);
        orders.add(order2);

        orders.removeByOrderEntryID(1L);

        assertNull(orders.findByOrderEntryID(1L));
        assertNotNull(orders.findByOrderEntryID(2L));
    }
}
