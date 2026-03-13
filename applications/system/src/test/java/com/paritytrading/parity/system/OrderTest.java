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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.mock;

import com.paritytrading.parity.match.OrderBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTest {

    private byte[] orderId;
    private long orderNumber;
    private Session session;
    private OrderBook book;

    @BeforeEach
    public void setUp() {
        orderId = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        orderNumber = 12345L;
        session = mock(Session.class);
        book = mock(OrderBook.class);
    }

    @Test
    public void testConstructor() {
        Order order = new Order(orderId, orderNumber, session, book);
        assertNotNull(order);
    }

    @Test
    public void testConstructorClonesOrderId() {
        Order order = new Order(orderId, orderNumber, session, book);
        byte[] retrievedOrderId = order.getOrderId();
        assertArrayEquals(orderId, retrievedOrderId);
        assertNotSame(orderId, retrievedOrderId);
    }

    @Test
    public void testGetOrderId() {
        Order order = new Order(orderId, orderNumber, session, book);
        byte[] retrievedOrderId = order.getOrderId();
        assertArrayEquals(orderId, retrievedOrderId);
    }

    @Test
    public void testGetOrderNumber() {
        Order order = new Order(orderId, orderNumber, session, book);
        assertEquals(orderNumber, order.getOrderNumber());
    }

    @Test
    public void testGetSession() {
        Order order = new Order(orderId, orderNumber, session, book);
        assertEquals(session, order.getSession());
    }

    @Test
    public void testGetBook() {
        Order order = new Order(orderId, orderNumber, session, book);
        assertEquals(book, order.getBook());
    }

    @Test
    public void testConstructorWithEmptyOrderId() {
        byte[] emptyOrderId = new byte[0];
        Order order = new Order(emptyOrderId, orderNumber, session, book);
        assertNotNull(order);
        assertArrayEquals(emptyOrderId, order.getOrderId());
    }

    @Test
    public void testConstructorWithLargeOrderNumber() {
        long largeOrderNumber = Long.MAX_VALUE;
        Order order = new Order(orderId, largeOrderNumber, session, book);
        assertEquals(largeOrderNumber, order.getOrderNumber());
    }

    @Test
    public void testConstructorWithZeroOrderNumber() {
        long zeroOrderNumber = 0L;
        Order order = new Order(orderId, zeroOrderNumber, session, book);
        assertEquals(zeroOrderNumber, order.getOrderNumber());
    }
}
