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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.match.Side;
import com.paritytrading.parity.net.pmd.PMD;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderBooksTest {

    private MarketData marketData;
    private MarketReporting marketReporting;
    private Session session;
    private OrderBooks orderBooks;

    @BeforeEach
    public void setUp() {
        marketData = mock(MarketData.class);
        marketReporting = mock(MarketReporting.class);
        session = mock(Session.class);
    }

    @Test
    public void testConstructor() {
        List<String> instruments = Arrays.asList("FOO", "BAR");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);
        assertNotNull(orderBooks);
    }

    @Test
    public void testConstructorWithEmptyInstruments() {
        List<String> instruments = Collections.emptyList();
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);
        assertNotNull(orderBooks);
    }

    @Test
    public void testEnterOrderWithUnknownInstrument() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId = new byte[16];
        message.instrument = ASCII.packLong("BAR");
        message.side = POE.BUY;
        message.price = 1000;
        message.quantity = 100;

        orderBooks.enterOrder(message, session);

        verify(session).orderRejected(message, POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT);
    }

    @Test
    public void testEnterOrderWithInvalidPrice() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId = new byte[16];
        message.instrument = ASCII.packLong("FOO");
        message.side = POE.BUY;
        message.price = -1;
        message.quantity = 100;

        orderBooks.enterOrder(message, session);

        verify(session).orderRejected(message, POE.ORDER_REJECT_REASON_INVALID_PRICE);
    }

    @Test
    public void testEnterOrderWithInvalidQuantityZero() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId = new byte[16];
        message.instrument = ASCII.packLong("FOO");
        message.side = POE.BUY;
        message.price = 1000;
        message.quantity = 0;

        orderBooks.enterOrder(message, session);

        verify(session).orderRejected(message, POE.ORDER_REJECT_REASON_INVALID_QUANTITY);
    }

    @Test
    public void testEnterOrderWithInvalidQuantityNegative() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId = new byte[16];
        message.instrument = ASCII.packLong("FOO");
        message.side = POE.BUY;
        message.price = 1000;
        message.quantity = -100;

        orderBooks.enterOrder(message, session);

        verify(session).orderRejected(message, POE.ORDER_REJECT_REASON_INVALID_QUANTITY);
    }

    @Test
    public void testEnterOrderValid() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        when(session.getUsername()).thenReturn(123456L);

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId = new byte[16];
        message.instrument = ASCII.packLong("FOO");
        message.side = POE.BUY;
        message.price = 1000;
        message.quantity = 100;

        orderBooks.enterOrder(message, session);

        verify(session).orderAccepted(eq(message), any(Order.class));
        verify(marketReporting).orderEntered(eq(123456L), eq(1L), eq(POE.BUY),
                eq(message.instrument), eq(100L), eq(1000L));
    }

    @Test
    public void testEnterOrderValidSell() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        when(session.getUsername()).thenReturn(789L);

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId = new byte[16];
        message.instrument = ASCII.packLong("FOO");
        message.side = POE.SELL;
        message.price = 2000;
        message.quantity = 50;

        orderBooks.enterOrder(message, session);

        verify(session).orderAccepted(eq(message), any(Order.class));
        verify(marketReporting).orderEntered(eq(789L), eq(1L), eq(POE.SELL),
                eq(message.instrument), eq(50L), eq(2000L));
    }

    @Test
    public void testCancelOrder() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        OrderBook book = mock(OrderBook.class);
        Order order = new Order(new byte[16], 1L, session, book);

        POE.CancelOrder message = new POE.CancelOrder();
        message.orderId = new byte[16];
        message.quantity = 50;

        orderBooks.cancelOrder(message, order);

        verify(book).cancel(1L, 50);
    }

    @Test
    public void testCancelOrderFullQuantity() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        OrderBook book = mock(OrderBook.class);
        Order order = new Order(new byte[16], 2L, session, book);

        POE.CancelOrder message = new POE.CancelOrder();
        message.orderId = new byte[16];
        message.quantity = 100;

        orderBooks.cancelOrder(message, order);

        verify(book).cancel(2L, 100);
    }

    @Test
    public void testCancel() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        OrderBook book = mock(OrderBook.class);
        Order order = new Order(new byte[16], 3L, session, book);

        orderBooks.cancel(order);

        verify(book).cancel(3L, 0);
    }

    @Test
    public void testSideByteConversion() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        POE.EnterOrder buyMessage = new POE.EnterOrder();
        buyMessage.orderId = new byte[16];
        buyMessage.instrument = ASCII.packLong("FOO");
        buyMessage.side = POE.BUY;
        buyMessage.price = 1000;
        buyMessage.quantity = 100;

        when(session.getUsername()).thenReturn(1L);

        orderBooks.enterOrder(buyMessage, session);

        verify(session).orderAccepted(eq(buyMessage), any(Order.class));
    }

    @Test
    public void testSideSellByteConversion() {
        List<String> instruments = Arrays.asList("FOO");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        POE.EnterOrder sellMessage = new POE.EnterOrder();
        sellMessage.orderId = new byte[16];
        sellMessage.instrument = ASCII.packLong("FOO");
        sellMessage.side = POE.SELL;
        sellMessage.price = 1000;
        sellMessage.quantity = 100;

        when(session.getUsername()).thenReturn(1L);

        orderBooks.enterOrder(sellMessage, session);

        verify(session).orderAccepted(eq(sellMessage), any(Order.class));
    }
}
