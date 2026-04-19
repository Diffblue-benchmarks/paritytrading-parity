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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.philadelphia.FIXConfig;
import com.paritytrading.philadelphia.FIXConnection;
import com.paritytrading.philadelphia.FIXMessage;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;

class SessionOrderEntryListenerTest {

    private FIXConnection fixConnection;
    private Orders ordersMock;
    private Instruments instruments;
    private Instrument instrument;
    private POEClientListener orderEntryListener;
    private SoupBinTCPClientStatusListener statusListener;

    @BeforeEach
    void setUp() throws Exception {
        OrderEntryFactory factory = mock(OrderEntryFactory.class);
        SoupBinTCPClient client = mock(SoupBinTCPClient.class);
        SocketChannel channel = mock(SocketChannel.class);
        FIXConfig config = mock(FIXConfig.class);
        instruments = mock(Instruments.class);
        instrument = mock(Instrument.class);

        when(instruments.get(anyString())).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(0);
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        ArgumentCaptor<POEClientListener> poeCaptor = ArgumentCaptor.forClass(POEClientListener.class);
        ArgumentCaptor<SoupBinTCPClientStatusListener> statusCaptor = ArgumentCaptor.forClass(SoupBinTCPClientStatusListener.class);
        when(factory.create(poeCaptor.capture(), statusCaptor.capture())).thenReturn(client);

        try (MockedConstruction<Orders> ordersConstruction = mockConstruction(Orders.class);
             MockedConstruction<FIXConnection> fixConstruction = mockConstruction(FIXConnection.class,
                 (mock, context) -> {
                     when(mock.getCurrentTimestamp()).thenReturn("20240101-00:00:00.000");
                     doAnswer(invocation -> {
                         FIXMessage msg = invocation.getArgument(0);
                         msg.reset();
                         return null;
                     }).when(mock).prepare(any(FIXMessage.class), anyChar());
                 })) {
            new Session(factory, channel, config, instruments);
            fixConnection = fixConstruction.constructed().get(0);
            ordersMock = ordersConstruction.constructed().get(0);
        }

        orderEntryListener = poeCaptor.getValue();
        statusListener = statusCaptor.getValue();
    }

    @Test
    void orderAccepted() throws IOException {
        Order order = new Order(1L, "CLO001", null, '1', "AAPL", 100.0);
        when(ordersMock.findByOrderEntryID(1L)).thenReturn(order);

        POE.OrderAccepted message = new POE.OrderAccepted();
        ASCII.putLongRight(message.orderId, 1L);
        message.orderNumber = 12345L;

        orderEntryListener.orderAccepted(message);

        assertEquals(12345L, order.getOrderID());
        verify(fixConnection).send(any(FIXMessage.class));
    }

    @Test
    void orderAcceptedUnknownOrder() throws IOException {
        when(ordersMock.findByOrderEntryID(anyLong())).thenReturn(null);

        POE.OrderAccepted message = new POE.OrderAccepted();
        ASCII.putLongRight(message.orderId, 999L);
        message.orderNumber = 12345L;

        orderEntryListener.orderAccepted(message);

        verify(fixConnection, never()).send(any(FIXMessage.class));
    }

    @Test
    void orderRejectedUnknownInstrument() throws IOException {
        Order order = new Order(2L, "CLO002", null, '1', "AAPL", 100.0);
        when(ordersMock.findByOrderEntryID(2L)).thenReturn(order);

        POE.OrderRejected message = new POE.OrderRejected();
        ASCII.putLongRight(message.orderId, 2L);
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        orderEntryListener.orderRejected(message);

        verify(fixConnection).send(any(FIXMessage.class));
        verify(ordersMock).removeByOrderEntryID(2L);
    }

    @Test
    void orderRejectedInvalidPrice() throws IOException {
        Order order = new Order(3L, "CLO003", null, '1', "AAPL", 100.0);
        when(ordersMock.findByOrderEntryID(3L)).thenReturn(order);

        POE.OrderRejected message = new POE.OrderRejected();
        ASCII.putLongRight(message.orderId, 3L);
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        orderEntryListener.orderRejected(message);

        verify(fixConnection).send(any(FIXMessage.class));
        verify(ordersMock).removeByOrderEntryID(3L);
    }

    @Test
    void orderRejectedInvalidQuantity() throws IOException {
        Order order = new Order(4L, "CLO004", null, '1', "AAPL", 100.0);
        when(ordersMock.findByOrderEntryID(4L)).thenReturn(order);

        POE.OrderRejected message = new POE.OrderRejected();
        ASCII.putLongRight(message.orderId, 4L);
        message.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        orderEntryListener.orderRejected(message);

        verify(fixConnection).send(any(FIXMessage.class));
        verify(ordersMock).removeByOrderEntryID(4L);
    }

    @Test
    void orderRejectedDefaultReason() throws IOException {
        Order order = new Order(5L, "CLO005", null, '1', "AAPL", 100.0);
        when(ordersMock.findByOrderEntryID(5L)).thenReturn(order);

        POE.OrderRejected message = new POE.OrderRejected();
        ASCII.putLongRight(message.orderId, 5L);
        message.reason = (byte) 'X';

        orderEntryListener.orderRejected(message);

        verify(fixConnection).send(any(FIXMessage.class));
        verify(ordersMock).removeByOrderEntryID(5L);
    }

    @Test
    void orderRejectedUnknownOrder() throws IOException {
        when(ordersMock.findByOrderEntryID(anyLong())).thenReturn(null);

        POE.OrderRejected message = new POE.OrderRejected();
        ASCII.putLongRight(message.orderId, 999L);
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        orderEntryListener.orderRejected(message);

        verify(fixConnection, never()).send(any(FIXMessage.class));
    }

    @Test
    void orderExecutedPartialFill() throws IOException {
        Order order = new Order(6L, "CLO006", null, '1', "AAPL", 100.0);
        order.orderAccepted(12345L);
        when(ordersMock.findByOrderEntryID(6L)).thenReturn(order);

        POE.OrderExecuted message = new POE.OrderExecuted();
        ASCII.putLongRight(message.orderId, 6L);
        message.quantity = 50L;
        message.price = 15000L;

        orderEntryListener.orderExecuted(message);

        verify(fixConnection).send(any(FIXMessage.class));
        assertEquals(50.0, order.getCumQty());
        verify(ordersMock, never()).removeByOrderEntryID(6L);
    }

    @Test
    void orderExecutedFullFill() throws IOException {
        Order order = new Order(7L, "CLO007", null, '1', "AAPL", 100.0);
        order.orderAccepted(12345L);
        when(ordersMock.findByOrderEntryID(7L)).thenReturn(order);

        POE.OrderExecuted message = new POE.OrderExecuted();
        ASCII.putLongRight(message.orderId, 7L);
        message.quantity = 100L;
        message.price = 15000L;

        orderEntryListener.orderExecuted(message);

        verify(fixConnection).send(any(FIXMessage.class));
        assertEquals(0.0, order.getLeavesQty());
        verify(ordersMock).removeByOrderEntryID(7L);
    }

    @Test
    void orderExecutedUnknownOrder() throws IOException {
        when(ordersMock.findByOrderEntryID(anyLong())).thenReturn(null);

        POE.OrderExecuted message = new POE.OrderExecuted();
        ASCII.putLongRight(message.orderId, 999L);
        message.quantity = 50L;
        message.price = 15000L;

        orderEntryListener.orderExecuted(message);

        verify(fixConnection, never()).send(any(FIXMessage.class));
    }

    @Test
    void orderExecutedFullFillWithPendingCancel() throws IOException {
        Order order = new Order(8L, "CLO008", null, '1', "AAPL", 100.0);
        order.orderAccepted(12345L);
        order.setNextClOrdID("CLO008b");
        when(ordersMock.findByOrderEntryID(8L)).thenReturn(order);

        POE.OrderExecuted message = new POE.OrderExecuted();
        ASCII.putLongRight(message.orderId, 8L);
        message.quantity = 100L;
        message.price = 15000L;

        orderEntryListener.orderExecuted(message);

        verify(fixConnection, times(2)).send(any(FIXMessage.class));
        verify(ordersMock).removeByOrderEntryID(8L);
    }

    @Test
    void orderCanceledPartial() throws IOException {
        Order order = new Order(9L, "CLO009", null, '1', "AAPL", 100.0);
        order.orderAccepted(12345L);
        order.setNextClOrdID("CLO009b");
        when(ordersMock.findByOrderEntryID(9L)).thenReturn(order);

        POE.OrderCanceled message = new POE.OrderCanceled();
        ASCII.putLongRight(message.orderId, 9L);
        message.canceledQuantity = 50L;

        orderEntryListener.orderCanceled(message);

        verify(fixConnection).send(any(FIXMessage.class));
        verify(ordersMock, never()).removeByOrderEntryID(9L);
    }

    @Test
    void orderCanceledFull() throws IOException {
        Order order = new Order(10L, "CLO010", null, '1', "AAPL", 100.0);
        order.orderAccepted(12345L);
        order.setNextClOrdID("CLO010b");
        when(ordersMock.findByOrderEntryID(10L)).thenReturn(order);

        POE.OrderCanceled message = new POE.OrderCanceled();
        ASCII.putLongRight(message.orderId, 10L);
        message.canceledQuantity = 100L;

        orderEntryListener.orderCanceled(message);

        verify(fixConnection).send(any(FIXMessage.class));
        assertEquals(0.0, order.getLeavesQty());
        verify(ordersMock).removeByOrderEntryID(10L);
    }

    @Test
    void orderCanceledUnknownOrder() throws IOException {
        when(ordersMock.findByOrderEntryID(anyLong())).thenReturn(null);

        POE.OrderCanceled message = new POE.OrderCanceled();
        ASCII.putLongRight(message.orderId, 999L);
        message.canceledQuantity = 50L;

        orderEntryListener.orderCanceled(message);

        verify(fixConnection, never()).send(any(FIXMessage.class));
    }

    @Test
    void heartbeatTimeout() throws IOException {
        statusListener.heartbeatTimeout(mock(SoupBinTCPClient.class));

        verify(fixConnection).sendLogout("Trading system not available");
    }

    @Test
    void loginAccepted() throws IOException {
        SoupBinTCP.LoginAccepted payload = new SoupBinTCP.LoginAccepted();

        statusListener.loginAccepted(mock(SoupBinTCPClient.class), payload);

        verify(fixConnection).sendLogon(false);
    }

    @Test
    void loginRejectedNotAuthorized() throws IOException {
        SoupBinTCP.LoginRejected payload = new SoupBinTCP.LoginRejected();
        payload.rejectReasonCode = SoupBinTCP.LOGIN_REJECT_CODE_NOT_AUTHORIZED;

        statusListener.loginRejected(mock(SoupBinTCPClient.class), payload);

        verify(fixConnection).sendLogout("Not authorized");
    }

    @Test
    void loginRejectedSessionNotAvailable() throws IOException {
        SoupBinTCP.LoginRejected payload = new SoupBinTCP.LoginRejected();
        payload.rejectReasonCode = SoupBinTCP.LOGIN_REJECT_CODE_SESSION_NOT_AVAILABLE;

        statusListener.loginRejected(mock(SoupBinTCPClient.class), payload);

        verify(fixConnection).sendLogout("Session not available");
    }

    @Test
    void endOfSession() throws IOException {
        statusListener.endOfSession(mock(SoupBinTCPClient.class));

        verify(fixConnection).sendLogout();
    }
}
