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

import static com.paritytrading.philadelphia.fix44.FIX44Enumerations.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.philadelphia.FIXConfig;
import com.paritytrading.philadelphia.FIXConnection;
import com.paritytrading.philadelphia.FIXMessage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SessionTest {

    private Session session;
    private Orders orders;
    private Instruments instruments;
    private FIXConnection fixConnection;
    private Object orderEntryListener;

    @BeforeEach
    public void setUp() throws Exception {
        OrderEntryFactory orderEntryFactory = mock(OrderEntryFactory.class);
        SoupBinTCPClient mockClient = mock(SoupBinTCPClient.class);
        when(orderEntryFactory.create(any(), any())).thenReturn(mockClient);

        SocketChannel mockChannel = mock(SocketChannel.class);
        FIXConfig fixConfig = new FIXConfig.Builder()
            .setSenderCompID("SENDER")
            .setTargetCompID("TARGET")
            .setHeartBtInt(30)
            .build();

        instruments = mock(Instruments.class);

        session = spy(new Session(orderEntryFactory, mockChannel, fixConfig, instruments));

        orders = new Orders();
        fixConnection = mock(FIXConnection.class);
        when(fixConnection.getCurrentTimestamp()).thenReturn("20240101-12:00:00");
        doAnswer(invocation -> {
            FIXMessage msg = invocation.getArgument(0);
            msg.reset();
            return null;
        }).when(fixConnection).prepare(any(FIXMessage.class), anyChar());

        setField(session, "orders", orders);
        setField(session, "fix", fixConnection);

        Class<?> listenerClass = getInnerClass(Session.class, "OrderEntryListener");
        Constructor<?> constructor = listenerClass.getDeclaredConstructor(Session.class);
        constructor.setAccessible(true);
        orderEntryListener = constructor.newInstance(session);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = Session.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Class<?> getInnerClass(Class<?> outerClass, String innerClassName) {
        for (Class<?> innerClass : outerClass.getDeclaredClasses()) {
            if (innerClass.getSimpleName().equals(innerClassName)) {
                return innerClass;
            }
        }
        throw new RuntimeException("Inner class not found: " + innerClassName);
    }

    private Object invokeMethod(Object target, String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    @Test
    public void testOrderAcceptedWithValidOrder() throws Exception {
        POE.OrderAccepted message = new POE.OrderAccepted();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.orderNumber = 99999L;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get("AAPL")).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);

        invokeMethod(orderEntryListener, "orderAccepted",
            new Class<?>[]{POE.OrderAccepted.class}, message);

        assertEquals(99999L, order.getOrderID());
        verify(fixConnection).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection).send(any(FIXMessage.class));
    }

    @Test
    public void testOrderAcceptedWithUnknownOrder() throws Exception {
        POE.OrderAccepted message = new POE.OrderAccepted();
        long orderEntryId = 99999L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.orderNumber = 88888L;

        invokeMethod(orderEntryListener, "orderAccepted",
            new Class<?>[]{POE.OrderAccepted.class}, message);

        verify(fixConnection, never()).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection, never()).send(any(FIXMessage.class));
    }

    @Test
    public void testOrderRejectedUnknownInstrument() throws Exception {
        POE.OrderRejected message = new POE.OrderRejected();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get("AAPL")).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);

        invokeMethod(orderEntryListener, "orderRejected",
            new Class<?>[]{POE.OrderRejected.class}, message);

        verify(fixConnection).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection).send(any(FIXMessage.class));
        assertNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderRejectedInvalidPrice() throws Exception {
        POE.OrderRejected message = new POE.OrderRejected();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get("AAPL")).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);

        invokeMethod(orderEntryListener, "orderRejected",
            new Class<?>[]{POE.OrderRejected.class}, message);

        verify(fixConnection).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection).send(any(FIXMessage.class));
        assertNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderRejectedInvalidQuantity() throws Exception {
        POE.OrderRejected message = new POE.OrderRejected();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get("AAPL")).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);

        invokeMethod(orderEntryListener, "orderRejected",
            new Class<?>[]{POE.OrderRejected.class}, message);

        verify(fixConnection).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection).send(any(FIXMessage.class));
        assertNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderRejectedOtherReason() throws Exception {
        POE.OrderRejected message = new POE.OrderRejected();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.reason = (byte) 99;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get("AAPL")).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);

        invokeMethod(orderEntryListener, "orderRejected",
            new Class<?>[]{POE.OrderRejected.class}, message);

        verify(fixConnection).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection).send(any(FIXMessage.class));
        assertNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderRejectedUnknownOrder() throws Exception {
        POE.OrderRejected message = new POE.OrderRejected();
        long orderEntryId = 99999L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        invokeMethod(orderEntryListener, "orderRejected",
            new Class<?>[]{POE.OrderRejected.class}, message);

        verify(fixConnection, never()).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection, never()).send(any(FIXMessage.class));
    }

    @Test
    public void testOrderExecutedPartialFill() throws Exception {
        POE.OrderExecuted message = new POE.OrderExecuted();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.quantity = 5000L;
        message.price = 15000L;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        order.orderAccepted(99999L);
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get("AAPL")).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);
        when(instrument.getSizeFactor()).thenReturn(100.0);
        when(instrument.getPriceFactor()).thenReturn(100.0);

        invokeMethod(orderEntryListener, "orderExecuted",
            new Class<?>[]{POE.OrderExecuted.class}, message);

        verify(fixConnection, times(1)).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection, times(1)).send(any(FIXMessage.class));
        assertNotNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderExecutedFullFill() throws Exception {
        POE.OrderExecuted message = new POE.OrderExecuted();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.quantity = 10000L;
        message.price = 15000L;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        order.orderAccepted(99999L);
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get("AAPL")).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);
        when(instrument.getSizeFactor()).thenReturn(100.0);
        when(instrument.getPriceFactor()).thenReturn(100.0);

        invokeMethod(orderEntryListener, "orderExecuted",
            new Class<?>[]{POE.OrderExecuted.class}, message);

        verify(fixConnection, times(1)).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection, times(1)).send(any(FIXMessage.class));
        assertNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderExecutedFullFillWithPendingCancel() throws Exception {
        POE.OrderExecuted message = new POE.OrderExecuted();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.quantity = 10000L;
        message.price = 15000L;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        order.orderAccepted(99999L);
        order.setNextClOrdID("CLO124");
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get("AAPL")).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);
        when(instrument.getSizeFactor()).thenReturn(100.0);
        when(instrument.getPriceFactor()).thenReturn(100.0);

        invokeMethod(orderEntryListener, "orderExecuted",
            new Class<?>[]{POE.OrderExecuted.class}, message);

        verify(fixConnection, times(2)).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection, times(2)).send(any(FIXMessage.class));
        assertNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderExecutedUnknownOrder() throws Exception {
        POE.OrderExecuted message = new POE.OrderExecuted();
        long orderEntryId = 99999L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.quantity = 10000L;
        message.price = 15000L;

        invokeMethod(orderEntryListener, "orderExecuted",
            new Class<?>[]{POE.OrderExecuted.class}, message);

        verify(fixConnection, never()).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection, never()).send(any(FIXMessage.class));
    }

    @Test
    public void testOrderCanceledPartial() throws Exception {
        POE.OrderCanceled message = new POE.OrderCanceled();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.canceledQuantity = 5000L;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        order.orderAccepted(99999L);
        order.setNextClOrdID("CLO124");
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get(anyString())).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);
        when(instrument.getSizeFactor()).thenReturn(100.0);
        when(fixConnection.getCurrentTimestamp()).thenReturn("20240101-12:00:00");

        invokeMethod(orderEntryListener, "orderCanceled",
            new Class<?>[]{POE.OrderCanceled.class}, message);

        verify(fixConnection).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection).send(any(FIXMessage.class));
        assertNotNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderCanceledFull() throws Exception {
        POE.OrderCanceled message = new POE.OrderCanceled();
        long orderEntryId = 12345L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.canceledQuantity = 10000L;

        Order order = new Order(orderEntryId, "CLO123", null, SideValues.Buy, "AAPL", 100.0);
        order.orderAccepted(99999L);
        order.setNextClOrdID("CLO124");
        orders.add(order);

        Instrument instrument = mock(Instrument.class);
        when(instruments.get(anyString())).thenReturn(instrument);
        when(instrument.getPriceFractionDigits()).thenReturn(2);
        when(instrument.getSizeFractionDigits()).thenReturn(2);
        when(instrument.getSizeFactor()).thenReturn(100.0);
        when(fixConnection.getCurrentTimestamp()).thenReturn("20240101-12:00:00");

        invokeMethod(orderEntryListener, "orderCanceled",
            new Class<?>[]{POE.OrderCanceled.class}, message);

        verify(fixConnection).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection).send(any(FIXMessage.class));
        assertNull(orders.findByOrderEntryID(orderEntryId));
    }

    @Test
    public void testOrderCanceledUnknownOrder() throws Exception {
        POE.OrderCanceled message = new POE.OrderCanceled();
        long orderEntryId = 99999L;
        ASCII.putLongLeft(message.orderId, orderEntryId);
        message.canceledQuantity = 10000L;

        invokeMethod(orderEntryListener, "orderCanceled",
            new Class<?>[]{POE.OrderCanceled.class}, message);

        verify(fixConnection, never()).prepare(any(FIXMessage.class), anyChar());
        verify(fixConnection, never()).send(any(FIXMessage.class));
    }

    @Test
    public void testHeartbeatTimeout() throws Exception {
        SoupBinTCPClient client = mock(SoupBinTCPClient.class);

        invokeMethod(orderEntryListener, "heartbeatTimeout",
            new Class<?>[]{SoupBinTCPClient.class}, client);

        verify(fixConnection).sendLogout(eq("Trading system not available"));
    }

    @Test
    public void testLoginAccepted() throws Exception {
        SoupBinTCPClient client = mock(SoupBinTCPClient.class);
        SoupBinTCP.LoginAccepted payload = new SoupBinTCP.LoginAccepted();

        invokeMethod(orderEntryListener, "loginAccepted",
            new Class<?>[]{SoupBinTCPClient.class, SoupBinTCP.LoginAccepted.class},
            client, payload);

        verify(fixConnection).sendLogon(eq(false));
    }

    @Test
    public void testLoginRejectedNotAuthorized() throws Exception {
        SoupBinTCPClient client = mock(SoupBinTCPClient.class);
        SoupBinTCP.LoginRejected payload = new SoupBinTCP.LoginRejected();
        payload.rejectReasonCode = SoupBinTCP.LOGIN_REJECT_CODE_NOT_AUTHORIZED;

        invokeMethod(orderEntryListener, "loginRejected",
            new Class<?>[]{SoupBinTCPClient.class, SoupBinTCP.LoginRejected.class},
            client, payload);

        verify(fixConnection).sendLogout(eq("Not authorized"));
    }

    @Test
    public void testLoginRejectedSessionNotAvailable() throws Exception {
        SoupBinTCPClient client = mock(SoupBinTCPClient.class);
        SoupBinTCP.LoginRejected payload = new SoupBinTCP.LoginRejected();
        payload.rejectReasonCode = SoupBinTCP.LOGIN_REJECT_CODE_SESSION_NOT_AVAILABLE;

        invokeMethod(orderEntryListener, "loginRejected",
            new Class<?>[]{SoupBinTCPClient.class, SoupBinTCP.LoginRejected.class},
            client, payload);

        verify(fixConnection).sendLogout(eq("Session not available"));
    }

    @Test
    public void testLoginRejectedOtherReason() throws Exception {
        SoupBinTCPClient client = mock(SoupBinTCPClient.class);
        SoupBinTCP.LoginRejected payload = new SoupBinTCP.LoginRejected();
        payload.rejectReasonCode = 'X';

        invokeMethod(orderEntryListener, "loginRejected",
            new Class<?>[]{SoupBinTCPClient.class, SoupBinTCP.LoginRejected.class},
            client, payload);

        verify(fixConnection, never()).sendLogout(anyString());
    }

    @Test
    public void testEndOfSession() throws Exception {
        SoupBinTCPClient client = mock(SoupBinTCPClient.class);

        invokeMethod(orderEntryListener, "endOfSession",
            new Class<?>[]{SoupBinTCPClient.class}, client);

        verify(fixConnection).sendLogout();
    }
}
