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
package com.paritytrading.parity.system;

import com.paritytrading.parity.match.OrderBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Session session;

    @Mock
    private OrderBook orderBook;

    private byte[] orderId;
    private Order order;

    @BeforeEach
    void setUp() {
        orderId = "ORDER-001".getBytes();
        order = new Order(orderId, 12345L, session, orderBook);
    }

    @Test
    void testGetOrderId() {
        byte[] retrievedOrderId = order.getOrderId();
        assertNotNull(retrievedOrderId);
        assertArrayEquals("ORDER-001".getBytes(), retrievedOrderId);
    }

    @Test
    void testConstructorClonesOrderId() {
        byte[] originalOrderId = "ORIGINAL".getBytes();
        Order testOrder = new Order(originalOrderId, 1L, session, orderBook);

        originalOrderId[0] = 'X';

        byte[] storedOrderId = testOrder.getOrderId();
        assertEquals('O', (char) storedOrderId[0]);
    }

    @Test
    void testGetOrderNumber() {
        assertEquals(12345L, order.getOrderNumber());
    }

    @Test
    void testGetSession() {
        assertSame(session, order.getSession());
    }

    @Test
    void testGetBook() {
        assertSame(orderBook, order.getBook());
    }

    @Test
    void testOrderCreationWithDifferentValues() {
        byte[] newOrderId = "ORDER-999".getBytes();
        Order newOrder = new Order(newOrderId, 99999L, session, orderBook);

        assertEquals(99999L, newOrder.getOrderNumber());
        assertArrayEquals("ORDER-999".getBytes(), newOrder.getOrderId());
        assertSame(session, newOrder.getSession());
        assertSame(orderBook, newOrder.getBook());
    }


    @Test
    void testMultipleOrdersWithSameSession() {
        Order order1 = new Order("ORD-1".getBytes(), 1L, session, orderBook);
        Order order2 = new Order("ORD-2".getBytes(), 2L, session, orderBook);

        assertSame(session, order1.getSession());
        assertSame(session, order2.getSession());
        assertNotEquals(order1.getOrderNumber(), order2.getOrderNumber());
    }
}
