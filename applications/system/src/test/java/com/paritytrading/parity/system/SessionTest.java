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
import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPServer;
import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.net.poe.POE;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SessionTest {

    private Session session;
    private SocketChannel channel;
    private OrderBooks orderBooks;
    private SoupBinTCPServer transport;

    @BeforeEach
    public void setUp() throws Exception {
        channel = mock(SocketChannel.class);
        orderBooks = mock(OrderBooks.class);
        session = new Session(channel, orderBooks);

        transport = getField(session, "transport");
    }

    private <T> T getField(Object target, String fieldName) throws Exception {
        Field field = Session.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(target);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = Session.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Object invokeMethod(Object target, String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        Method method = Session.class.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    @Test
    public void testConstructor() throws Exception {
        SocketChannel testChannel = mock(SocketChannel.class);
        OrderBooks testBooks = mock(OrderBooks.class);

        Session testSession = new Session(testChannel, testBooks);

        assertNotNull(testSession);
        assertNotNull(getField(testSession, "transport"));
        assertNotNull(getField(testSession, "orders"));
        assertNotNull(getField(testSession, "orderIds"));
        assertSame(testBooks, getField(testSession, "books"));
        assertFalse((Boolean) getField(testSession, "terminated"));
        assertEquals(0L, (long) getField(testSession, "username"));
    }

    @Test
    public void testGetTransport() {
        SoupBinTCPServer result = session.getTransport();

        assertNotNull(result);
        assertSame(transport, result);
    }

    @Test
    public void testGetUsername() throws Exception {
        setField(session, "username", 12345L);

        long result = session.getUsername();

        assertEquals(12345L, result);
    }

    @Test
    public void testGetUsernameInitialValue() {
        long result = session.getUsername();

        assertEquals(0L, result);
    }

    @Test
    public void testIsTerminated() throws Exception {
        assertFalse(session.isTerminated());

        setField(session, "terminated", true);

        assertTrue(session.isTerminated());
    }

    @Test
    public void testCloseWithOrders() throws Exception {
        Session spySession = spy(session);
        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(spySession, "transport", mockTransport);

        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);

        Object orders = getField(spySession, "orders");
        Method putMethod = orders.getClass().getMethod("put", Object.class, Object.class);
        putMethod.invoke(orders, new byte[]{1}, order1);
        putMethod.invoke(orders, new byte[]{2}, order2);

        spySession.close();

        verify(orderBooks).cancel(order1);
        verify(orderBooks).cancel(order2);
        verify(mockTransport).close();
    }

    @Test
    public void testCloseWithNoOrders() throws Exception {
        Session spySession = spy(session);
        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(spySession, "transport", mockTransport);

        spySession.close();

        verify(orderBooks, never()).cancel(any(Order.class));
        verify(mockTransport).close();
    }

    @Test
    public void testHeartbeatTimeout() throws Exception {
        SoupBinTCPServer mockServer = mock(SoupBinTCPServer.class);

        session.heartbeatTimeout(mockServer);

        assertTrue((Boolean) getField(session, "terminated"));
    }

    @Test
    public void testLoginRequestSuccess() throws Exception {
        SoupBinTCPServer mockServer = mock(SoupBinTCPServer.class);
        SoupBinTCP.LoginRequest loginRequest = new SoupBinTCP.LoginRequest();
        ASCII.putLeft(loginRequest.username, "USER");
        ASCII.putRight(loginRequest.requestedSession, "");
        ASCII.putLongRight(loginRequest.requestedSequenceNumber, 0);

        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(session, "transport", mockTransport);

        session.loginRequest(mockServer, loginRequest);

        verify(mockTransport).accept(any(SoupBinTCP.LoginAccepted.class));
        assertNotEquals(0L, (long) getField(session, "username"));
    }

    @Test
    public void testLoginRequestAlreadyLoggedIn() throws Exception {
        Session spySession = spy(session);
        setField(spySession, "username", 12345L);

        SoupBinTCPServer mockServer = mock(SoupBinTCPServer.class);
        SoupBinTCP.LoginRequest loginRequest = new SoupBinTCP.LoginRequest();
        ASCII.putLeft(loginRequest.username, "USER");

        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(spySession, "transport", mockTransport);

        spySession.loginRequest(mockServer, loginRequest);

        verify(mockTransport, never()).accept(any(SoupBinTCP.LoginAccepted.class));
        verify(mockTransport).close();
    }

    @Test
    public void testLoginRequestIOException() throws Exception {
        Session spySession = spy(session);
        SoupBinTCPServer mockServer = mock(SoupBinTCPServer.class);
        SoupBinTCP.LoginRequest loginRequest = new SoupBinTCP.LoginRequest();
        ASCII.putLeft(loginRequest.username, "USER");
        ASCII.putRight(loginRequest.requestedSession, "");
        ASCII.putLongRight(loginRequest.requestedSequenceNumber, 0);

        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        doThrow(new IOException()).when(mockTransport).accept(any(SoupBinTCP.LoginAccepted.class));
        setField(spySession, "transport", mockTransport);

        Order mockOrder = mock(Order.class);
        Object orders = getField(spySession, "orders");
        Method putMethod = orders.getClass().getMethod("put", Object.class, Object.class);
        putMethod.invoke(orders, new byte[]{1}, mockOrder);

        spySession.loginRequest(mockServer, loginRequest);

        verify(orderBooks).cancel(mockOrder);
        verify(mockTransport).close();
    }

    @Test
    public void testLogoutRequest() throws Exception {
        SoupBinTCPServer mockServer = mock(SoupBinTCPServer.class);

        session.logoutRequest(mockServer);

        assertTrue((Boolean) getField(session, "terminated"));
    }

    @Test
    public void testEnterOrderNotLoggedIn() throws Exception {
        Session spySession = spy(session);
        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(spySession, "transport", mockTransport);

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        ASCII.putLongLeft(enterOrder.orderId, 12345L);

        Order mockOrder = mock(Order.class);
        Object orders = getField(spySession, "orders");
        Method putMethod = orders.getClass().getMethod("put", Object.class, Object.class);
        putMethod.invoke(orders, new byte[]{1}, mockOrder);

        spySession.enterOrder(enterOrder);

        verify(orderBooks).cancel(mockOrder);
        verify(orderBooks, never()).enterOrder(any(POE.EnterOrder.class), any(Session.class));
        verify(mockTransport).close();
    }

    @Test
    public void testEnterOrderDuplicateOrderId() throws Exception {
        setField(session, "username", 12345L);

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        byte[] orderId = new byte[16];
        ASCII.putLongLeft(orderId, 12345L);
        System.arraycopy(orderId, 0, enterOrder.orderId, 0, enterOrder.orderId.length);

        Object orderIds = getField(session, "orderIds");
        Method addMethod = orderIds.getClass().getMethod("add", Object.class);
        addMethod.invoke(orderIds, orderId);

        session.enterOrder(enterOrder);

        verify(orderBooks, never()).enterOrder(any(POE.EnterOrder.class), any(Session.class));
    }

    @Test
    public void testEnterOrderSuccess() throws Exception {
        setField(session, "username", 12345L);

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        ASCII.putLongLeft(enterOrder.orderId, 12345L);
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 1000L;
        enterOrder.quantity = 100L;
        enterOrder.price = 50L;

        session.enterOrder(enterOrder);

        verify(orderBooks).enterOrder(enterOrder, session);
    }

    @Test
    public void testCancelOrderNotLoggedIn() throws Exception {
        Session spySession = spy(session);
        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(spySession, "transport", mockTransport);

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        ASCII.putLongLeft(cancelOrder.orderId, 12345L);

        Order mockOrder = mock(Order.class);
        Object orders = getField(spySession, "orders");
        Method putMethod = orders.getClass().getMethod("put", Object.class, Object.class);
        putMethod.invoke(orders, new byte[]{1}, mockOrder);

        spySession.cancelOrder(cancelOrder);

        verify(orderBooks).cancel(mockOrder);
        verify(orderBooks, never()).cancelOrder(any(POE.CancelOrder.class), any(Order.class));
        verify(mockTransport).close();
    }

    @Test
    public void testCancelOrderUnknownOrder() throws Exception {
        setField(session, "username", 12345L);

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        ASCII.putLongLeft(cancelOrder.orderId, 99999L);

        session.cancelOrder(cancelOrder);

        verify(orderBooks, never()).cancelOrder(any(POE.CancelOrder.class), any(Order.class));
    }

    @Test
    public void testCancelOrderSuccess() throws Exception {
        setField(session, "username", 12345L);

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        byte[] orderId = new byte[16];
        ASCII.putLongLeft(orderId, 12345L);
        System.arraycopy(orderId, 0, cancelOrder.orderId, 0, cancelOrder.orderId.length);
        cancelOrder.quantity = 50L;

        Order mockOrder = mock(Order.class);
        when(mockOrder.getOrderId()).thenReturn(orderId);

        Object orders = getField(session, "orders");
        Method putMethod = orders.getClass().getMethod("put", Object.class, Object.class);
        putMethod.invoke(orders, orderId, mockOrder);

        session.cancelOrder(cancelOrder);

        verify(orderBooks).cancelOrder(cancelOrder, mockOrder);
    }

    @Test
    public void testTrack() throws Exception {
        byte[] orderId = new byte[16];
        ASCII.putLongLeft(orderId, 12345L);
        Order mockOrder = mock(Order.class);
        when(mockOrder.getOrderId()).thenReturn(orderId);

        session.track(mockOrder);

        Object orders = getField(session, "orders");
        Method containsKeyMethod = orders.getClass().getMethod("containsKey", Object.class);
        assertTrue((Boolean) containsKeyMethod.invoke(orders, orderId));
    }

    @Test
    public void testRelease() throws Exception {
        byte[] orderId = new byte[16];
        ASCII.putLongLeft(orderId, 12345L);
        Order mockOrder = mock(Order.class);
        when(mockOrder.getOrderId()).thenReturn(orderId);

        Object orders = getField(session, "orders");
        Method putMethod = orders.getClass().getMethod("put", Object.class, Object.class);
        putMethod.invoke(orders, orderId, mockOrder);

        session.release(mockOrder);

        Method containsKeyMethod = orders.getClass().getMethod("containsKey", Object.class);
        assertFalse((Boolean) containsKeyMethod.invoke(orders, orderId));
    }

    @Test
    public void testOrderAccepted() throws Exception {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        ASCII.putLongLeft(enterOrder.orderId, 12345L);
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 1000L;
        enterOrder.quantity = 100L;
        enterOrder.price = 50L;

        Order mockOrder = mock(Order.class);
        when(mockOrder.getOrderNumber()).thenReturn(99999L);

        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(session, "transport", mockTransport);

        session.orderAccepted(enterOrder, mockOrder);

        verify(mockTransport).send(any(ByteBuffer.class));

        Object orderIds = getField(session, "orderIds");
        Method sizeMethod = orderIds.getClass().getMethod("size");
        assertEquals(1, sizeMethod.invoke(orderIds));
    }

    @Test
    public void testOrderRejected() throws Exception {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        ASCII.putLongLeft(enterOrder.orderId, 12345L);
        byte reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(session, "transport", mockTransport);

        session.orderRejected(enterOrder, reason);

        verify(mockTransport).send(any(ByteBuffer.class));
    }

    @Test
    public void testOrderExecuted() throws Exception {
        byte[] orderId = new byte[16];
        ASCII.putLongLeft(orderId, 12345L);

        Order mockOrder = mock(Order.class);
        when(mockOrder.getOrderId()).thenReturn(orderId);
        when(mockOrder.getOrderNumber()).thenReturn(99999L);

        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(session, "transport", mockTransport);

        session.orderExecuted(50L, 100L, POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, 88888L, mockOrder);

        verify(mockTransport).send(any(ByteBuffer.class));
    }

    @Test
    public void testOrderCanceled() throws Exception {
        byte[] orderId = new byte[16];
        ASCII.putLongLeft(orderId, 12345L);

        Order mockOrder = mock(Order.class);
        when(mockOrder.getOrderId()).thenReturn(orderId);
        when(mockOrder.getOrderNumber()).thenReturn(99999L);

        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        setField(session, "transport", mockTransport);

        session.orderCanceled(50L, POE.ORDER_CANCEL_REASON_REQUEST, mockOrder);

        verify(mockTransport).send(any(ByteBuffer.class));
    }

    @Test
    public void testSendIOException() throws Exception {
        Session spySession = spy(session);
        POE.OrderRejected orderRejected = new POE.OrderRejected();
        ASCII.putLongLeft(orderRejected.orderId, 12345L);
        orderRejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        SoupBinTCPServer mockTransport = mock(SoupBinTCPServer.class);
        doThrow(new IOException()).when(mockTransport).send(any(ByteBuffer.class));
        setField(spySession, "transport", mockTransport);

        Order mockOrder = mock(Order.class);
        Object orders = getField(spySession, "orders");
        Method putMethod = orders.getClass().getMethod("put", Object.class, Object.class);
        putMethod.invoke(orders, new byte[]{1}, mockOrder);

        invokeMethod(spySession, "send", new Class<?>[]{POE.OutboundMessage.class}, orderRejected);

        verify(orderBooks).cancel(mockOrder);
        verify(mockTransport).close();
    }

    @Test
    public void testTimestamp() throws Exception {
        long result = (Long) invokeMethod(session, "timestamp", new Class<?>[]{});

        assertTrue(result >= 0);
    }
}
