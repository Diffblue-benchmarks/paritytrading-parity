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
import static org.mockito.Mockito.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.match.Side;
import com.paritytrading.parity.net.pmd.PMD;
import com.paritytrading.parity.net.poe.POE;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderBooksTest {

    private MarketData      marketData;
    private MarketReporting marketReporting;
    private Session         session;

    private OrderBooks books;

    private long instrument;

    @BeforeEach
    void setUp() {
        marketData      = mock(MarketData.class);
        marketReporting = mock(MarketReporting.class);
        session         = mock(Session.class);

        instrument = ASCII.packLong("FOO     ");

        books = new OrderBooks(Arrays.asList("FOO     "), marketData, marketReporting);
    }

    @Test
    void constructWithMultipleInstruments() {
        OrderBooks multi = new OrderBooks(
                Arrays.asList("FOO     ", "BAR     "), marketData, marketReporting);

        assertNotNull(multi);
    }

    @Test
    void constructWithNoInstruments() {
        OrderBooks empty = new OrderBooks(Collections.emptyList(), marketData, marketReporting);

        assertNotNull(empty);
    }

    @Test
    void enterBuyOrder() {
        POE.EnterOrder message = newEnterOrder("order001        ", POE.BUY, instrument, 100, 1000);

        books.enterOrder(message, session);

        verify(session).orderAccepted(eq(message), any(Order.class));
        verify(marketReporting).orderEntered(eq(0L), eq(1L), eq(POE.BUY),
                eq(instrument), eq(100L), eq(1000L));
    }

    @Test
    void enterSellOrder() {
        POE.EnterOrder message = newEnterOrder("order002        ", POE.SELL, instrument, 200, 2000);

        books.enterOrder(message, session);

        verify(session).orderAccepted(eq(message), any(Order.class));
        verify(marketReporting).orderEntered(eq(0L), eq(1L), eq(POE.SELL),
                eq(instrument), eq(200L), eq(2000L));
    }

    @Test
    void rejectUnknownInstrument() {
        long unknown = ASCII.packLong("UNKNOWN ");
        POE.EnterOrder message = newEnterOrder("order003        ", POE.BUY, unknown, 100, 1000);

        books.enterOrder(message, session);

        verify(session).orderRejected(message, POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT);
        verify(session, never()).orderAccepted(any(), any());
    }

    @Test
    void rejectNegativePrice() {
        POE.EnterOrder message = newEnterOrder("order004        ", POE.BUY, instrument, 100, -1);

        books.enterOrder(message, session);

        verify(session).orderRejected(message, POE.ORDER_REJECT_REASON_INVALID_PRICE);
        verify(session, never()).orderAccepted(any(), any());
    }

    @Test
    void rejectZeroQuantity() {
        POE.EnterOrder message = newEnterOrder("order005        ", POE.BUY, instrument, 0, 1000);

        books.enterOrder(message, session);

        verify(session).orderRejected(message, POE.ORDER_REJECT_REASON_INVALID_QUANTITY);
        verify(session, never()).orderAccepted(any(), any());
    }

    @Test
    void rejectNegativeQuantity() {
        POE.EnterOrder message = newEnterOrder("order006        ", POE.BUY, instrument, -5, 1000);

        books.enterOrder(message, session);

        verify(session).orderRejected(message, POE.ORDER_REJECT_REASON_INVALID_QUANTITY);
        verify(session, never()).orderAccepted(any(), any());
    }

    @Test
    void orderNumbersIncrement() {
        POE.EnterOrder first  = newEnterOrder("order007        ", POE.BUY, instrument, 100, 1000);
        POE.EnterOrder second = newEnterOrder("order008        ", POE.BUY, instrument, 200, 2000);

        books.enterOrder(first, session);
        books.enterOrder(second, session);

        verify(marketReporting).orderEntered(eq(0L), eq(1L), eq(POE.BUY),
                eq(instrument), eq(100L), eq(1000L));
        verify(marketReporting).orderEntered(eq(0L), eq(2L), eq(POE.BUY),
                eq(instrument), eq(200L), eq(2000L));
    }

    @Test
    void cancelOrderByRequest() {
        POE.EnterOrder enter = newEnterOrder("order009        ", POE.BUY, instrument, 100, 1000);
        books.enterOrder(enter, session);

        Order order = captureAcceptedOrder();

        POE.CancelOrder cancel = new POE.CancelOrder();
        System.arraycopy(enter.orderId, 0, cancel.orderId, 0, cancel.orderId.length);
        cancel.quantity = 50;

        books.cancelOrder(cancel, order);

        verify(marketData).orderCanceled(eq(order.getOrderNumber()), anyLong());
    }

    @Test
    void cancelOrderBySystem() {
        POE.EnterOrder enter = newEnterOrder("order010        ", POE.BUY, instrument, 100, 1000);
        books.enterOrder(enter, session);

        Order order = captureAcceptedOrder();

        books.cancel(order);

        verify(marketData).orderCanceled(eq(order.getOrderNumber()), anyLong());
    }

    @Test
    void enterOrderMarketDataAdded() {
        POE.EnterOrder message = newEnterOrder("order011        ", POE.BUY, instrument, 100, 1000);

        books.enterOrder(message, session);

        verify(marketData).orderAdded(eq(1L), eq(PMD.BUY), eq(instrument), eq(100L), eq(1000L));
    }

    @Test
    void enterSellOrderMarketDataAdded() {
        POE.EnterOrder message = newEnterOrder("order012        ", POE.SELL, instrument, 300, 5000);

        books.enterOrder(message, session);

        verify(marketData).orderAdded(eq(1L), eq(PMD.SELL), eq(instrument), eq(300L), eq(5000L));
    }

    @Test
    void enterOrderMarketReportingAdded() {
        POE.EnterOrder message = newEnterOrder("order013        ", POE.BUY, instrument, 100, 1000);

        books.enterOrder(message, session);

        verify(marketReporting).orderAdded(1L);
    }

    @Test
    void sessionTrackCalledOnEnter() {
        POE.EnterOrder message = newEnterOrder("order014        ", POE.BUY, instrument, 100, 1000);

        books.enterOrder(message, session);

        verify(session).track(any(Order.class));
    }

    @Test
    void sessionReleaseCalledOnFullCancel() {
        POE.EnterOrder enter = newEnterOrder("order015        ", POE.BUY, instrument, 100, 1000);
        books.enterOrder(enter, session);

        Order order = captureAcceptedOrder();

        POE.CancelOrder cancel = new POE.CancelOrder();
        System.arraycopy(enter.orderId, 0, cancel.orderId, 0, cancel.orderId.length);
        cancel.quantity = 0;

        books.cancelOrder(cancel, order);

        verify(session).orderCanceled(anyLong(), eq(POE.ORDER_CANCEL_REASON_REQUEST), eq(order));
        verify(session).release(order);
    }

    @Test
    void matchingOrders() {
        Session buySession  = mock(Session.class);
        Session sellSession = mock(Session.class);

        POE.EnterOrder buyOrder = newEnterOrder("buyorder0001    ", POE.BUY, instrument, 100, 1000);
        books.enterOrder(buyOrder, buySession);

        POE.EnterOrder sellOrder = newEnterOrder("sellorder001    ", POE.SELL, instrument, 100, 1000);
        books.enterOrder(sellOrder, sellSession);

        verify(marketReporting).trade(eq(1L), eq(2L), eq(100L), eq(1L));
    }

    private POE.EnterOrder newEnterOrder(String orderId, byte side, long instrument,
            long quantity, long price) {
        POE.EnterOrder message = new POE.EnterOrder();

        byte[] idBytes = orderId.getBytes();
        System.arraycopy(idBytes, 0, message.orderId, 0,
                Math.min(idBytes.length, message.orderId.length));

        message.side       = side;
        message.instrument = instrument;
        message.quantity   = quantity;
        message.price      = price;

        return message;
    }

    private Order captureAcceptedOrder() {
        org.mockito.ArgumentCaptor<Order> captor = org.mockito.ArgumentCaptor.forClass(Order.class);
        verify(session).orderAccepted(any(POE.EnterOrder.class), captor.capture());
        return captor.getValue();
    }
}
