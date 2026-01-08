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
import static com.paritytrading.philadelphia.fix44.FIX44Enumerations.*;

/**
 * Test class for Orders.
 *
 * <p>This test class covers all methods of the Orders class including:
 * - Constructor
 * - add(Order)
 * - findByClOrdID(String)
 * - findByOrderEntryID(long)
 * - removeByOrderEntryID(long)
 *
 * <p>Testing approach: Tests use the public/package-private API without mocking,
 * verifying actual behavior of the Orders collection class. The Orders class is
 * package-private, so tests are in the same package.
 */
class OrdersClaudeTest {

    @Test
    void testConstructorCreatesEmptyCollection() {
        Orders orders = new Orders();

        assertNull(orders.findByClOrdID("nonexistent"));
        assertNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testAddSingleOrder() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);

        orders.add(order);

        assertEquals(order, orders.findByClOrdID("ORDER1"));
        assertEquals(order, orders.findByOrderEntryID(1L));
    }

    @Test
    void testAddMultipleOrders() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);
        Order order3 = new Order(3L, "ORDER3", "ACCT3", SideValues.Buy, "MSFT", 75.0);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        assertEquals(order1, orders.findByClOrdID("ORDER1"));
        assertEquals(order2, orders.findByClOrdID("ORDER2"));
        assertEquals(order3, orders.findByClOrdID("ORDER3"));
        assertEquals(order1, orders.findByOrderEntryID(1L));
        assertEquals(order2, orders.findByOrderEntryID(2L));
        assertEquals(order3, orders.findByOrderEntryID(3L));
    }

    @Test
    void testFindByClOrdIDReturnsNullWhenNotFound() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        assertNull(orders.findByClOrdID("NONEXISTENT"));
    }

    @Test
    void testFindByClOrdIDInEmptyCollection() {
        Orders orders = new Orders();

        assertNull(orders.findByClOrdID("ORDER1"));
    }

    @Test
    void testFindByClOrdIDReturnsFirstMatchingOrder() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER1", "ACCT2", SideValues.Sell, "GOOGL", 50.0);

        orders.add(order1);
        orders.add(order2);

        Order found = orders.findByClOrdID("ORDER1");
        assertEquals(order1, found);
        assertEquals(1L, found.getOrderEntryID());
    }

    @Test
    void testFindByClOrdIDAfterOrderCancellation() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        order.setNextClOrdID("ORDER2");
        order.orderCanceled(50.0);

        assertNull(orders.findByClOrdID("ORDER1"));
        assertEquals(order, orders.findByClOrdID("ORDER2"));
    }

    @Test
    void testFindByOrderEntryIDReturnsNullWhenNotFound() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        assertNull(orders.findByOrderEntryID(999L));
    }

    @Test
    void testFindByOrderEntryIDInEmptyCollection() {
        Orders orders = new Orders();

        assertNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testFindByOrderEntryIDReturnsFirstMatchingOrder() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(1L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);

        orders.add(order1);
        orders.add(order2);

        Order found = orders.findByOrderEntryID(1L);
        assertEquals(order1, found);
        assertEquals("ORDER1", found.getClOrdID());
    }

    @Test
    void testFindByOrderEntryIDWithMultipleOrders() {
        Orders orders = new Orders();
        Order order1 = new Order(100L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(200L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);
        Order order3 = new Order(300L, "ORDER3", "ACCT3", SideValues.Buy, "MSFT", 75.0);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        assertEquals(order1, orders.findByOrderEntryID(100L));
        assertEquals(order2, orders.findByOrderEntryID(200L));
        assertEquals(order3, orders.findByOrderEntryID(300L));
    }

    @Test
    void testRemoveByOrderEntryIDRemovesOrder() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        assertEquals(order, orders.findByOrderEntryID(1L));

        orders.removeByOrderEntryID(1L);

        assertNull(orders.findByOrderEntryID(1L));
        assertNull(orders.findByClOrdID("ORDER1"));
    }

    @Test
    void testRemoveByOrderEntryIDInEmptyCollection() {
        Orders orders = new Orders();

        orders.removeByOrderEntryID(1L);

        assertNull(orders.findByOrderEntryID(1L));
    }

    @Test
    void testRemoveByOrderEntryIDNonExistentOrder() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        orders.removeByOrderEntryID(999L);

        assertEquals(order, orders.findByOrderEntryID(1L));
    }

    @Test
    void testRemoveByOrderEntryIDRemovesOnlyFirstMatch() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(1L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);

        orders.add(order1);
        orders.add(order2);

        orders.removeByOrderEntryID(1L);

        Order found = orders.findByOrderEntryID(1L);
        assertEquals(order2, found);
        assertNull(orders.findByClOrdID("ORDER1"));
        assertEquals(order2, orders.findByClOrdID("ORDER2"));
    }

    @Test
    void testRemoveByOrderEntryIDFromMiddleOfCollection() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);
        Order order3 = new Order(3L, "ORDER3", "ACCT3", SideValues.Buy, "MSFT", 75.0);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        orders.removeByOrderEntryID(2L);

        assertEquals(order1, orders.findByOrderEntryID(1L));
        assertNull(orders.findByOrderEntryID(2L));
        assertEquals(order3, orders.findByOrderEntryID(3L));
    }

    @Test
    void testRemoveByOrderEntryIDFromBeginningOfCollection() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);
        Order order3 = new Order(3L, "ORDER3", "ACCT3", SideValues.Buy, "MSFT", 75.0);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        orders.removeByOrderEntryID(1L);

        assertNull(orders.findByOrderEntryID(1L));
        assertEquals(order2, orders.findByOrderEntryID(2L));
        assertEquals(order3, orders.findByOrderEntryID(3L));
    }

    @Test
    void testRemoveByOrderEntryIDFromEndOfCollection() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);
        Order order3 = new Order(3L, "ORDER3", "ACCT3", SideValues.Buy, "MSFT", 75.0);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        orders.removeByOrderEntryID(3L);

        assertEquals(order1, orders.findByOrderEntryID(1L));
        assertEquals(order2, orders.findByOrderEntryID(2L));
        assertNull(orders.findByOrderEntryID(3L));
    }

    @Test
    void testComplexScenarioAddFindAndRemove() {
        Orders orders = new Orders();
        Order order1 = new Order(100L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(200L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);
        Order order3 = new Order(300L, "ORDER3", "ACCT3", SideValues.Buy, "MSFT", 75.0);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        assertEquals(order1, orders.findByClOrdID("ORDER1"));
        assertEquals(order2, orders.findByOrderEntryID(200L));

        orders.removeByOrderEntryID(200L);

        assertNull(orders.findByClOrdID("ORDER2"));
        assertNull(orders.findByOrderEntryID(200L));
        assertEquals(order1, orders.findByClOrdID("ORDER1"));
        assertEquals(order3, orders.findByClOrdID("ORDER3"));

        Order order4 = new Order(400L, "ORDER4", "ACCT4", SideValues.Sell, "TSLA", 25.0);
        orders.add(order4);

        assertEquals(order4, orders.findByOrderEntryID(400L));
    }

    @Test
    void testMultipleRemovalsUntilEmpty() {
        Orders orders = new Orders();
        Order order1 = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        Order order2 = new Order(2L, "ORDER2", "ACCT2", SideValues.Sell, "GOOGL", 50.0);

        orders.add(order1);
        orders.add(order2);

        orders.removeByOrderEntryID(1L);
        assertNull(orders.findByOrderEntryID(1L));
        assertEquals(order2, orders.findByOrderEntryID(2L));

        orders.removeByOrderEntryID(2L);
        assertNull(orders.findByOrderEntryID(1L));
        assertNull(orders.findByOrderEntryID(2L));
    }

    @Test
    void testFindByClOrdIDWithNullClOrdID() {
        Orders orders = new Orders();

        assertNull(orders.findByClOrdID(null));
    }

    @Test
    void testFindByClOrdIDWithEmptyString() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        assertNull(orders.findByClOrdID(""));
    }

    @Test
    void testOrderEntryIDZero() {
        Orders orders = new Orders();
        Order order = new Order(0L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        assertEquals(order, orders.findByOrderEntryID(0L));

        orders.removeByOrderEntryID(0L);
        assertNull(orders.findByOrderEntryID(0L));
    }

    @Test
    void testNegativeOrderEntryID() {
        Orders orders = new Orders();
        Order order = new Order(-1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        assertEquals(order, orders.findByOrderEntryID(-1L));

        orders.removeByOrderEntryID(-1L);
        assertNull(orders.findByOrderEntryID(-1L));
    }

    @Test
    void testLargeNumberOfOrders() {
        Orders orders = new Orders();

        for (int i = 0; i < 100; i++) {
            Order order = new Order(i, "ORDER" + i, "ACCT" + i, SideValues.Buy, "AAPL", 100.0);
            orders.add(order);
        }

        for (int i = 0; i < 100; i++) {
            assertNotNull(orders.findByOrderEntryID(i));
            assertNotNull(orders.findByClOrdID("ORDER" + i));
        }

        for (int i = 0; i < 50; i++) {
            orders.removeByOrderEntryID(i);
        }

        for (int i = 0; i < 50; i++) {
            assertNull(orders.findByOrderEntryID(i));
        }

        for (int i = 50; i < 100; i++) {
            assertNotNull(orders.findByOrderEntryID(i));
        }
    }

    @Test
    void testAddAndRemoveSameOrderMultipleTimes() {
        Orders orders = new Orders();
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);

        orders.add(order);
        assertEquals(order, orders.findByOrderEntryID(1L));

        orders.removeByOrderEntryID(1L);
        assertNull(orders.findByOrderEntryID(1L));

        orders.add(order);
        assertEquals(order, orders.findByOrderEntryID(1L));

        orders.removeByOrderEntryID(1L);
        assertNull(orders.findByOrderEntryID(1L));
    }
}
