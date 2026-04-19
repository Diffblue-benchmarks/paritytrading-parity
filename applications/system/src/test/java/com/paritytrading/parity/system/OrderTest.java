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

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.parity.match.OrderBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderTest {

    private byte[]    orderId;
    private long      orderNumber;
    private Session   session;
    private OrderBook book;
    private Order     order;

    @BeforeEach
    void setUp() {
        orderId     = new byte[] { 'A', 'B', 'C', 'D' };
        orderNumber = 12345L;
        session     = Mockito.mock(Session.class);
        book        = Mockito.mock(OrderBook.class);
        order       = new Order(orderId, orderNumber, session, book);
    }

    @Test
    void constructorClonesOrderId() {
        byte[] original = new byte[] { 'X', 'Y' };
        Order  o        = new Order(original, 1L, session, book);

        original[0] = 'Z';

        assertEquals((byte) 'X', o.getOrderId()[0]);
    }

    @Test
    void getOrderId() {
        assertArrayEquals(orderId, order.getOrderId());
    }

    @Test
    void getOrderNumber() {
        assertEquals(12345L, order.getOrderNumber());
    }

    @Test
    void getSession() {
        assertSame(session, order.getSession());
    }

    @Test
    void getBook() {
        assertSame(book, order.getBook());
    }

}
