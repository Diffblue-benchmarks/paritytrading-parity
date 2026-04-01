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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import com.paritytrading.parity.match.OrderBook;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {

    @Test
    void testOrder_givenValidArguments_thenFieldsInitialized() {
        byte[] orderId = new byte[]{1, 2, 3};
        long orderNumber = 42L;
        Session session = mock(Session.class);
        OrderBook book = mock(OrderBook.class);

        Order order = new Order(orderId, orderNumber, session, book);

        assertArrayEquals(new byte[]{1, 2, 3}, order.getOrderId());
        assertEquals(42L, order.getOrderNumber());
        assertSame(session, order.getSession());
        assertSame(book, order.getBook());
    }

    @Test
    void testGetOrderId_givenOrder_thenReturnsOrderId() {
        byte[] orderId = new byte[]{10, 20, 30};
        Session session = mock(Session.class);
        OrderBook book = mock(OrderBook.class);

        Order order = new Order(orderId, 1L, session, book);

        assertArrayEquals(new byte[]{10, 20, 30}, order.getOrderId());
    }

    @Test
    void testGetOrderNumber_givenOrder_thenReturnsOrderNumber() {
        byte[] orderId = new byte[]{0};
        Session session = mock(Session.class);
        OrderBook book = mock(OrderBook.class);

        Order order = new Order(orderId, 99L, session, book);

        assertEquals(99L, order.getOrderNumber());
    }

    @Test
    void testGetSession_givenOrder_thenReturnsSession() {
        byte[] orderId = new byte[]{0};
        Session session = mock(Session.class);
        OrderBook book = mock(OrderBook.class);

        Order order = new Order(orderId, 1L, session, book);

        assertSame(session, order.getSession());
    }

    @Test
    void testGetBook_givenOrder_thenReturnsBook() {
        byte[] orderId = new byte[]{0};
        Session session = mock(Session.class);
        OrderBook book = mock(OrderBook.class);

        Order order = new Order(orderId, 1L, session, book);

        assertSame(book, order.getBook());
    }

    @Test
    void testOrder_givenOrderIdIsCloned_thenMutationDoesNotAffectOrder() {
        byte[] orderId = new byte[]{1, 2, 3};
        Session session = mock(Session.class);
        OrderBook book = mock(OrderBook.class);

        Order order = new Order(orderId, 1L, session, book);
        orderId[0] = 99;

        assertArrayEquals(new byte[]{1, 2, 3}, order.getOrderId());
    }
}
