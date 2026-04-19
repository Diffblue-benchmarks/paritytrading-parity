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

import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPServer;
import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.net.poe.POE;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

class SessionTest {

    private MockedConstruction<SoupBinTCPServer> serverConstruction;
    private SoupBinTCPServer mockTransport;
    private OrderBooks mockBooks;
    private Session session;

    @BeforeEach
    void setUp() {
        mockBooks = mock(OrderBooks.class);
        SocketChannel mockChannel = mock(SocketChannel.class);

        serverConstruction = mockConstruction(SoupBinTCPServer.class);
        session = new Session(mockChannel, mockBooks);
        mockTransport = serverConstruction.constructed().get(0);
    }

    @AfterEach
    void tearDown() {
        serverConstruction.close();
    }

    @Test
    void initialState() {
        assertSame(mockTransport, session.getTransport());
        assertEquals(0, session.getUsername());
        assertFalse(session.isTerminated());
    }

    @Test
    void heartbeatTimeout() {
        session.heartbeatTimeout(mockTransport);

        assertTrue(session.isTerminated());
    }

    @Test
    void logoutRequest() {
        session.logoutRequest(mockTransport);

        assertTrue(session.isTerminated());
    }

    @Test
    void loginRequest() throws IOException {
        SoupBinTCP.LoginRequest request = new SoupBinTCP.LoginRequest();
        fillBytes(request.username, "testus");

        session.loginRequest(mockTransport, request);

        assertNotEquals(0, session.getUsername());
        verify(mockTransport).accept(any(SoupBinTCP.LoginAccepted.class));
    }

    @Test
    void loginRequestWhenAlreadyLoggedIn() throws IOException {
        login();

        session.loginRequest(mockTransport, new SoupBinTCP.LoginRequest());

        verify(mockTransport).close();
    }

    @Test
    void loginRequestAcceptFailure() throws IOException {
        doThrow(new IOException("test")).when(mockTransport).accept(any(SoupBinTCP.LoginAccepted.class));

        SoupBinTCP.LoginRequest request = new SoupBinTCP.LoginRequest();
        fillBytes(request.username, "testus");

        session.loginRequest(mockTransport, request);

        verify(mockTransport).close();
    }

    @Test
    void closeWithNoOrders() throws IOException {
        session.close();

        verify(mockTransport).close();
    }

    @Test
    void closeWithTrackedOrder() throws IOException {
        OrderBook mockBook = mock(OrderBook.class);
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        orderId[0] = 1;
        Order order = new Order(orderId, 1L, session, mockBook);

        session.track(order);
        session.close();

        verify(mockBooks).cancel(any(Order.class));
        verify(mockTransport).close();
    }

    @Test
    void closeSwallowsTransportIOException() throws IOException {
        doThrow(new IOException("test")).when(mockTransport).close();

        session.close();
    }

    @Test
    void enterOrderWhenNotLoggedIn() throws IOException {
        POE.EnterOrder message = new POE.EnterOrder();

        session.enterOrder(message);

        verify(mockTransport).close();
    }

    @Test
    void enterOrderWhenLoggedIn() throws IOException {
        login();

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId[0] = 1;
        message.side        = POE.BUY;
        message.instrument  = 12345L;
        message.quantity    = 100L;
        message.price       = 5000L;

        session.enterOrder(message);

        verify(mockBooks).enterOrder(eq(message), eq(session));
    }

    @Test
    void enterOrderDuplicateOrderId() throws IOException {
        login();

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId[0] = 1;
        message.side        = POE.BUY;
        message.instrument  = 12345L;
        message.quantity    = 100L;
        message.price       = 5000L;

        OrderBook mockBook = mock(OrderBook.class);
        Order order = new Order(message.orderId, 1L, session, mockBook);
        session.orderAccepted(message, order);

        session.enterOrder(message);

        verify(mockBooks, never()).enterOrder(any(POE.EnterOrder.class), any(Session.class));
    }

    @Test
    void cancelOrderWhenNotLoggedIn() throws IOException {
        POE.CancelOrder message = new POE.CancelOrder();

        session.cancelOrder(message);

        verify(mockTransport).close();
    }

    @Test
    void cancelOrderWhenOrderNotFound() throws IOException {
        login();

        POE.CancelOrder message = new POE.CancelOrder();
        message.orderId[0] = 99;

        session.cancelOrder(message);

        verify(mockBooks, never()).cancelOrder(any(POE.CancelOrder.class), any(Order.class));
    }

    @Test
    void cancelOrderWhenOrderFound() throws IOException {
        login();

        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        orderId[0] = 1;
        OrderBook mockBook = mock(OrderBook.class);
        Order order = new Order(orderId, 1L, session, mockBook);
        session.track(order);

        POE.CancelOrder message = new POE.CancelOrder();
        message.orderId[0] = 1;
        message.quantity    = 50L;

        session.cancelOrder(message);

        verify(mockBooks).cancelOrder(eq(message), any(Order.class));
    }

    @Test
    void trackAndRelease() throws IOException {
        login();

        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        orderId[0] = 1;
        OrderBook mockBook = mock(OrderBook.class);
        Order order = new Order(orderId, 1L, session, mockBook);

        session.track(order);

        POE.CancelOrder cancelMessage = new POE.CancelOrder();
        cancelMessage.orderId[0] = 1;
        session.cancelOrder(cancelMessage);
        verify(mockBooks).cancelOrder(any(POE.CancelOrder.class), any(Order.class));

        session.release(order);
        reset(mockBooks);

        session.cancelOrder(cancelMessage);
        verify(mockBooks, never()).cancelOrder(any(POE.CancelOrder.class), any(Order.class));
    }

    @Test
    void orderAccepted() throws IOException {
        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId[0]  = 1;
        message.side        = POE.BUY;
        message.instrument  = 12345L;
        message.quantity    = 100L;
        message.price       = 5000L;

        OrderBook mockBook = mock(OrderBook.class);
        Order order = new Order(message.orderId, 42L, session, mockBook);

        session.orderAccepted(message, order);

        verify(mockTransport).send(any(ByteBuffer.class));
    }

    @Test
    void orderRejected() throws IOException {
        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId[0] = 1;

        session.orderRejected(message, POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT);

        verify(mockTransport).send(any(ByteBuffer.class));
    }

    @Test
    void orderExecuted() throws IOException {
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        orderId[0] = 1;
        OrderBook mockBook = mock(OrderBook.class);
        Order order = new Order(orderId, 1L, session, mockBook);

        session.orderExecuted(5000L, 100L, POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, 1L, order);

        verify(mockTransport).send(any(ByteBuffer.class));
    }

    @Test
    void orderCanceled() throws IOException {
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        orderId[0] = 1;
        OrderBook mockBook = mock(OrderBook.class);
        Order order = new Order(orderId, 1L, session, mockBook);

        session.orderCanceled(50L, POE.ORDER_CANCEL_REASON_REQUEST, order);

        verify(mockTransport).send(any(ByteBuffer.class));
    }

    @Test
    void sendFailureClosesSession() throws IOException {
        doThrow(new IOException("test")).when(mockTransport).send(any(ByteBuffer.class));

        POE.EnterOrder message = new POE.EnterOrder();
        message.orderId[0] = 1;

        session.orderRejected(message, POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT);

        verify(mockTransport).close();
    }

    private void login() throws IOException {
        SoupBinTCP.LoginRequest request = new SoupBinTCP.LoginRequest();
        fillBytes(request.username, "testus");

        session.loginRequest(mockTransport, request);
    }

    private static void fillBytes(byte[] target, String value) {
        byte[] src = value.getBytes();
        int len = Math.min(src.length, target.length);
        System.arraycopy(src, 0, target, 0, len);
        for (int i = len; i < target.length; i++)
            target[i] = ' ';
    }
}
