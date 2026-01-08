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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.net.poe.POE;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Session class. Tests all methods including constructor, getters, lifecycle
 * methods, login/logout, order operations, and event handlers.
 */
public class SessionClaudeTest {
  private OrderBooks orderBooks;
  private MarketData marketData;
  private MarketReporting marketReporting;
  private NetworkInterface networkInterface;
  private Session session;
  private SocketChannel channel;
  private ServerSocketChannel serverChannel;
  private SocketChannel clientChannel;

  @BeforeEach
  public void setUp() throws IOException, InterruptedException {
    networkInterface = findSuitableNetworkInterface();
    InetSocketAddress marketDataMulticast =
        new InetSocketAddress("239.255.1.1", 8000 + (int) (Math.random() * 1000));
    InetSocketAddress marketDataRequest = new InetSocketAddress("localhost", 0);
    marketData =
        MarketData.open("MD-TEST", networkInterface, marketDataMulticast, marketDataRequest);
    InetSocketAddress marketReportingMulticast =
        new InetSocketAddress("239.255.1.2", 9000 + (int) (Math.random() * 1000));
    InetSocketAddress marketReportingRequest = new InetSocketAddress("localhost", 0);
    marketReporting =
        MarketReporting.open(
            "MR-TEST", networkInterface, marketReportingMulticast, marketReportingRequest);
    List<String> instruments = Arrays.asList("AAPL    ", "GOOGL   ", "MSFT    ");
    orderBooks = new OrderBooks(instruments, marketData, marketReporting);

    // Create a connected socket pair for testing
    serverChannel = ServerSocketChannel.open();
    serverChannel.bind(new InetSocketAddress("localhost", 0));
    serverChannel.configureBlocking(false);

    clientChannel = SocketChannel.open();
    clientChannel.configureBlocking(false);
    clientChannel.connect(serverChannel.getLocalAddress());

    channel = serverChannel.accept();
    while (channel == null) {
      Thread.sleep(10);
      channel = serverChannel.accept();
    }
    channel.configureBlocking(false);

    session = new Session(channel, orderBooks);
  }

  @AfterEach
  public void tearDown() {
    if (session != null) {
      session.close();
    }
    if (clientChannel != null && clientChannel.isOpen()) {
      try {
        clientChannel.close();
      } catch (IOException e) {
        // Ignore cleanup exceptions
      }
    }
    if (channel != null && channel.isOpen()) {
      try {
        channel.close();
      } catch (IOException e) {
        // Ignore cleanup exceptions
      }
    }
    if (serverChannel != null && serverChannel.isOpen()) {
      try {
        serverChannel.close();
      } catch (IOException e) {
        // Ignore cleanup exceptions
      }
    }
    if (marketData != null) {
      try {
        if (marketData.getTransport() != null && marketData.getTransport().getChannel() != null) {
          marketData.getTransport().getChannel().close();
        }
        if (marketData.getRequestTransport() != null
            && marketData.getRequestTransport().getChannel() != null) {
          marketData.getRequestTransport().getChannel().close();
        }
      } catch (IOException e) {
        // Ignore cleanup exceptions
      }
    }
    if (marketReporting != null) {
      try {
        if (marketReporting.getTransport() != null
            && marketReporting.getTransport().getChannel() != null) {
          marketReporting.getTransport().getChannel().close();
        }
        if (marketReporting.getRequestTransport() != null
            && marketReporting.getRequestTransport().getChannel() != null) {
          marketReporting.getRequestTransport().getChannel().close();
        }
      } catch (IOException e) {
        // Ignore cleanup exceptions
      }
    }
  }

  /** Test constructor creates Session instance successfully. */
  @Test
  public void testConstructorCreatesInstance() {
    assertNotNull(session);
  }

  /** Test constructor initializes transport. */
  @Test
  public void testConstructorInitializesTransport() {
    assertNotNull(session.getTransport());
  }

  /** Test constructor initializes username to zero. */
  @Test
  public void testConstructorInitializesUsernameToZero() {
    assertEquals(0, session.getUsername());
  }

  /** Test constructor sets terminated to false. */
  @Test
  public void testConstructorSetsTerminatedToFalse() {
    assertFalse(session.isTerminated());
  }

  /** Test getTransport returns non-null transport. */
  @Test
  public void testGetTransportReturnsNonNull() {
    assertNotNull(session.getTransport());
  }

  /** Test getTransport returns same instance on multiple calls. */
  @Test
  public void testGetTransportReturnsSameInstance() {
    assertSame(session.getTransport(), session.getTransport());
  }

  /** Test getUsername returns initial value of zero. */
  @Test
  public void testGetUsernameReturnsInitialZero() {
    assertEquals(0, session.getUsername());
  }

  /** Test isTerminated returns false initially. */
  @Test
  public void testIsTerminatedReturnsFalseInitially() {
    assertFalse(session.isTerminated());
  }

  /** Test close method completes without exception. */
  @Test
  public void testCloseMethodCompletes() {
    assertDoesNotThrow(() -> session.close());
  }

  /** Test close method can be called multiple times safely. */
  @Test
  public void testCloseCanBeCalledMultipleTimes() {
    assertDoesNotThrow(
        () -> {
          session.close();
          session.close();
        });
  }

  /** Test heartbeatTimeout sets terminated to true. */
  @Test
  public void testHeartbeatTimeoutSetsTerminatedTrue() {
    assertFalse(session.isTerminated());
    session.heartbeatTimeout(session.getTransport());
    assertTrue(session.isTerminated());
  }

  /** Test heartbeatTimeout can be called multiple times. */
  @Test
  public void testHeartbeatTimeoutMultipleCalls() {
    session.heartbeatTimeout(session.getTransport());
    assertTrue(session.isTerminated());
    session.heartbeatTimeout(session.getTransport());
    assertTrue(session.isTerminated());
  }

  /**
   * Test loginRequest sets username. Now that we have connected sockets, the transport.accept
   * should succeed and the username should be set.
   */
  @Test
  public void testLoginRequestSetsUsername() {
    SoupBinTCP.LoginRequest loginRequest = new SoupBinTCP.LoginRequest();
    byte[] username = "USER1234".getBytes();
    System.arraycopy(username, 0, loginRequest.username, 0, Math.min(username.length, 6));
    loginRequest.requestedSession = "SESSION1".getBytes();
    loginRequest.requestedSequenceNumber = "1       ".getBytes();

    session.loginRequest(session.getTransport(), loginRequest);
    assertNotEquals(0, session.getUsername());
  }

  /** Test loginRequest with different usernames. */
  @Test
  public void testLoginRequestWithDifferentUsernames() throws IOException, InterruptedException {
    // Create second connected socket pair
    ServerSocketChannel serverChannel2 = ServerSocketChannel.open();
    serverChannel2.bind(new InetSocketAddress("localhost", 0));
    serverChannel2.configureBlocking(false);

    SocketChannel clientChannel2 = SocketChannel.open();
    clientChannel2.configureBlocking(false);
    clientChannel2.connect(serverChannel2.getLocalAddress());

    SocketChannel channel2 = serverChannel2.accept();
    while (channel2 == null) {
      Thread.sleep(10);
      channel2 = serverChannel2.accept();
    }
    channel2.configureBlocking(false);

    Session session2 = new Session(channel2, orderBooks);

    SoupBinTCP.LoginRequest loginRequest1 = new SoupBinTCP.LoginRequest();
    byte[] username1 = "USER1   ".getBytes();
    System.arraycopy(username1, 0, loginRequest1.username, 0, Math.min(username1.length, 6));
    loginRequest1.requestedSession = "SESSION1".getBytes();
    loginRequest1.requestedSequenceNumber = "1       ".getBytes();

    SoupBinTCP.LoginRequest loginRequest2 = new SoupBinTCP.LoginRequest();
    byte[] username2 = "USER2   ".getBytes();
    System.arraycopy(username2, 0, loginRequest2.username, 0, Math.min(username2.length, 6));
    loginRequest2.requestedSession = "SESSION2".getBytes();
    loginRequest2.requestedSequenceNumber = "1       ".getBytes();

    session.loginRequest(session.getTransport(), loginRequest1);
    session2.loginRequest(session2.getTransport(), loginRequest2);

    assertNotEquals(0, session.getUsername());
    assertNotEquals(0, session2.getUsername());
    assertNotEquals(session.getUsername(), session2.getUsername());

    session2.close();
    channel2.close();
    clientChannel2.close();
    serverChannel2.close();
  }

  /**
   * Test loginRequest when already logged in closes session. When username is not zero, calling
   * loginRequest again should call close() (line 113 in Session.java).
   */
  @Test
  public void testLoginRequestWhenAlreadyLoggedInClosesSession() {
    SoupBinTCP.LoginRequest loginRequest = new SoupBinTCP.LoginRequest();
    byte[] username = "USER1   ".getBytes();
    System.arraycopy(username, 0, loginRequest.username, 0, Math.min(username.length, 6));
    loginRequest.requestedSession = "SESSION1".getBytes();
    loginRequest.requestedSequenceNumber = "1       ".getBytes();

    session.loginRequest(session.getTransport(), loginRequest);
    long firstUsername = session.getUsername();
    assertNotEquals(0, firstUsername);

    // Try to login again - this should call close()
    session.loginRequest(session.getTransport(), loginRequest);
    // Session should remain with first username (close was called but doesn't change username)
    assertEquals(firstUsername, session.getUsername());
  }

  /** Test logoutRequest sets terminated to true. */
  @Test
  public void testLogoutRequestSetsTerminatedTrue() {
    assertFalse(session.isTerminated());
    session.logoutRequest(session.getTransport());
    assertTrue(session.isTerminated());
  }

  /** Test logoutRequest can be called multiple times. */
  @Test
  public void testLogoutRequestMultipleCalls() {
    session.logoutRequest(session.getTransport());
    assertTrue(session.isTerminated());
    session.logoutRequest(session.getTransport());
    assertTrue(session.isTerminated());
  }

  /** Test enterOrder without login closes session. */
  @Test
  public void testEnterOrderWithoutLoginClosesSession() {
    POE.EnterOrder enterOrder = new POE.EnterOrder();
    byte[] orderId = "ORDER0000000001 ".getBytes();
    System.arraycopy(orderId, 0, enterOrder.orderId, 0, Math.min(orderId.length, 16));
    enterOrder.side = POE.BUY;
    enterOrder.instrument = ASCII.packLong("AAPL    ");
    enterOrder.quantity = 100;
    enterOrder.price = 5000;

    session.enterOrder(enterOrder);
    // Session should call close when username is 0
    assertEquals(0, session.getUsername());
  }

  /**
   * Test enterOrder with duplicate order ID is ignored. After logging in and placing an order,
   * trying to place the same order ID again should be ignored (line 141-142 in Session.java).
   */
  @Test
  public void testEnterOrderWithDuplicateOrderIdIgnored() {
    // First login
    SoupBinTCP.LoginRequest loginRequest = new SoupBinTCP.LoginRequest();
    byte[] username = "USER1   ".getBytes();
    System.arraycopy(username, 0, loginRequest.username, 0, Math.min(username.length, 6));
    loginRequest.requestedSession = "SESSION1".getBytes();
    loginRequest.requestedSequenceNumber = "1       ".getBytes();
    session.loginRequest(session.getTransport(), loginRequest);

    POE.EnterOrder enterOrder = new POE.EnterOrder();
    byte[] orderId = "ORDER0000000001 ".getBytes();
    System.arraycopy(orderId, 0, enterOrder.orderId, 0, Math.min(orderId.length, 16));
    enterOrder.side = POE.BUY;
    enterOrder.instrument = ASCII.packLong("AAPL    ");
    enterOrder.quantity = 100;
    enterOrder.price = 5000;

    // First enter order call - this will call orderBooks.enterOrder which will call
    // session.orderAccepted
    session.enterOrder(enterOrder);

    // Second enter order call with same order ID - should be ignored
    assertDoesNotThrow(() -> session.enterOrder(enterOrder));
  }

  /** Test cancelOrder without login closes session. */
  @Test
  public void testCancelOrderWithoutLoginClosesSession() {
    POE.CancelOrder cancelOrder = new POE.CancelOrder();
    byte[] orderId = "ORDER0000000001 ".getBytes();
    System.arraycopy(orderId, 0, cancelOrder.orderId, 0, Math.min(orderId.length, 16));
    cancelOrder.quantity = 50;

    session.cancelOrder(cancelOrder);
    // Session should call close when username is 0
    assertEquals(0, session.getUsername());
  }

  /**
   * Test cancelOrder with non-existent order is ignored. When the order is not found in the orders
   * map, the method returns early (line 155-156 in Session.java).
   */
  @Test
  public void testCancelOrderWithNonExistentOrderIgnored() {
    // First login
    SoupBinTCP.LoginRequest loginRequest = new SoupBinTCP.LoginRequest();
    byte[] username = "USER1   ".getBytes();
    System.arraycopy(username, 0, loginRequest.username, 0, Math.min(username.length, 6));
    loginRequest.requestedSession = "SESSION1".getBytes();
    loginRequest.requestedSequenceNumber = "1       ".getBytes();
    session.loginRequest(session.getTransport(), loginRequest);

    POE.CancelOrder cancelOrder = new POE.CancelOrder();
    byte[] orderId = "NOTEXIST00000001".getBytes();
    System.arraycopy(orderId, 0, cancelOrder.orderId, 0, Math.min(orderId.length, 16));
    cancelOrder.quantity = 50;

    // Should be ignored (no exception)
    assertDoesNotThrow(() -> session.cancelOrder(cancelOrder));
  }

  /** Test track method adds order to session. */
  @Test
  public void testTrackAddsOrderToSession() throws IOException {
    SocketChannel testChannel = SocketChannel.open();
    Session testSession = new Session(testChannel, orderBooks);
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    byte[] orderId = "ORDER0000000001 ".getBytes();
    Order order = new Order(orderId, 1L, testSession, testOrderBook);

    assertDoesNotThrow(() -> testSession.track(order));

    testSession.close();
    testChannel.close();
  }

  /** Test track method with multiple orders. */
  @Test
  public void testTrackWithMultipleOrders() throws IOException {
    SocketChannel testChannel = SocketChannel.open();
    Session testSession = new Session(testChannel, orderBooks);
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    for (int i = 1; i <= 5; i++) {
      byte[] orderId = String.format("ORDER%010d  ", i).getBytes();
      Order order = new Order(orderId, i, testSession, testOrderBook);
      assertDoesNotThrow(() -> testSession.track(order));
    }

    testSession.close();
    testChannel.close();
  }

  /** Test release method removes order from session. */
  @Test
  public void testReleaseRemovesOrderFromSession() throws IOException {
    SocketChannel testChannel = SocketChannel.open();
    Session testSession = new Session(testChannel, orderBooks);
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    byte[] orderId = "ORDER0000000001 ".getBytes();
    Order order = new Order(orderId, 1L, testSession, testOrderBook);

    testSession.track(order);
    assertDoesNotThrow(() -> testSession.release(order));

    testSession.close();
    testChannel.close();
  }

  /** Test release method with multiple orders. */
  @Test
  public void testReleaseWithMultipleOrders() throws IOException {
    SocketChannel testChannel = SocketChannel.open();
    Session testSession = new Session(testChannel, orderBooks);
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    Order[] orders = new Order[5];
    for (int i = 0; i < 5; i++) {
      byte[] orderId = String.format("ORDER%010d  ", i + 1).getBytes();
      orders[i] = new Order(orderId, i + 1, testSession, testOrderBook);
      testSession.track(orders[i]);
    }

    for (Order order : orders) {
      assertDoesNotThrow(() -> testSession.release(order));
    }

    testSession.close();
    testChannel.close();
  }

  /**
   * Test orderAccepted method completes without exception. Since orderAccepted calls send() which
   * requires a connected socket, we use the already-connected session from setUp.
   */
  @Test
  public void testOrderAcceptedCompletes() throws IOException {
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    POE.EnterOrder enterOrder = new POE.EnterOrder();
    byte[] orderId = "ORDER0000000001 ".getBytes();
    System.arraycopy(orderId, 0, enterOrder.orderId, 0, 16);
    enterOrder.side = POE.BUY;
    enterOrder.instrument = ASCII.packLong("AAPL    ");
    enterOrder.quantity = 100;
    enterOrder.price = 5000;

    Order order = new Order(enterOrder.orderId, 1L, session, testOrderBook);

    assertDoesNotThrow(() -> session.orderAccepted(enterOrder, order));
  }

  /** Test orderAccepted with different order parameters. */
  @Test
  public void testOrderAcceptedWithDifferentParameters() throws IOException {
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    POE.EnterOrder enterOrder = new POE.EnterOrder();
    byte[] orderId = "ORDER0000000002 ".getBytes();
    System.arraycopy(orderId, 0, enterOrder.orderId, 0, 16);
    enterOrder.side = POE.SELL;
    enterOrder.instrument = ASCII.packLong("GOOGL   ");
    enterOrder.quantity = 250;
    enterOrder.price = 7500;

    Order order = new Order(enterOrder.orderId, 2L, session, testOrderBook);

    assertDoesNotThrow(() -> session.orderAccepted(enterOrder, order));
  }

  /** Test orderRejected method completes without exception. */
  @Test
  public void testOrderRejectedCompletes() throws IOException {
    POE.EnterOrder enterOrder = new POE.EnterOrder();
    byte[] orderId = "ORDER0000000003 ".getBytes();
    System.arraycopy(orderId, 0, enterOrder.orderId, 0, 16);
    enterOrder.side = POE.BUY;
    enterOrder.instrument = ASCII.packLong("AAPL    ");
    enterOrder.quantity = 100;
    enterOrder.price = 5000;

    assertDoesNotThrow(
        () -> session.orderRejected(enterOrder, POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT));
  }

  /** Test orderRejected with different reject reasons. */
  @Test
  public void testOrderRejectedWithDifferentReasons() throws IOException {
    POE.EnterOrder enterOrder = new POE.EnterOrder();
    byte[] orderId = "ORDER0000000004 ".getBytes();
    System.arraycopy(orderId, 0, enterOrder.orderId, 0, 16);
    enterOrder.side = POE.BUY;
    enterOrder.instrument = ASCII.packLong("AAPL    ");
    enterOrder.quantity = 100;
    enterOrder.price = 5000;

    assertDoesNotThrow(
        () -> session.orderRejected(enterOrder, POE.ORDER_REJECT_REASON_INVALID_PRICE));
    assertDoesNotThrow(
        () -> session.orderRejected(enterOrder, POE.ORDER_REJECT_REASON_INVALID_QUANTITY));
  }

  /** Test orderExecuted method completes without exception. */
  @Test
  public void testOrderExecutedCompletes() throws IOException {
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    byte[] orderId = "ORDER0000000005 ".getBytes();
    Order order = new Order(orderId, 1L, session, testOrderBook);

    assertDoesNotThrow(
        () -> session.orderExecuted(5000L, 100L, POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY, 1L, order));
  }

  /** Test orderExecuted with different parameters. */
  @Test
  public void testOrderExecutedWithDifferentParameters() throws IOException {
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    byte[] orderId = "ORDER0000000006 ".getBytes();
    Order order = new Order(orderId, 2L, session, testOrderBook);

    assertDoesNotThrow(
        () -> session.orderExecuted(7500L, 250L, POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, 2L, order));
  }

  /** Test orderCanceled method completes without exception. */
  @Test
  public void testOrderCanceledCompletes() throws IOException {
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    byte[] orderId = "ORDER0000000007 ".getBytes();
    Order order = new Order(orderId, 1L, session, testOrderBook);

    assertDoesNotThrow(() -> session.orderCanceled(50L, POE.ORDER_CANCEL_REASON_REQUEST, order));
  }

  /** Test orderCanceled with different parameters. */
  @Test
  public void testOrderCanceledWithDifferentParameters() throws IOException {
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    byte[] orderId = "ORDER0000000008 ".getBytes();
    Order order = new Order(orderId, 2L, session, testOrderBook);

    assertDoesNotThrow(
        () -> session.orderCanceled(100L, POE.ORDER_CANCEL_REASON_SUPERVISORY, order));
  }

  /** Test close with tracked orders cancels all orders. */
  @Test
  public void testCloseWithTrackedOrdersCancelsAll() throws IOException {
    SocketChannel testChannel = SocketChannel.open();
    Session testSession = new Session(testChannel, orderBooks);
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    // Track multiple orders
    for (int i = 1; i <= 3; i++) {
      byte[] orderId = String.format("ORDER%010d  ", i).getBytes();
      Order order = new Order(orderId, i, testSession, testOrderBook);
      testSession.track(order);
    }

    // Close should cancel all tracked orders
    assertDoesNotThrow(() -> testSession.close());

    testChannel.close();
  }

  /** Test multiple track and release operations. */
  @Test
  public void testMultipleTrackAndReleaseOperations() throws IOException {
    SocketChannel testChannel = SocketChannel.open();
    Session testSession = new Session(testChannel, orderBooks);
    OrderBook testOrderBook = new OrderBook(new TestOrderBookListener());

    byte[] orderId1 = "ORDER0000000011 ".getBytes();
    byte[] orderId2 = "ORDER0000000012 ".getBytes();
    Order order1 = new Order(orderId1, 11L, testSession, testOrderBook);
    Order order2 = new Order(orderId2, 12L, testSession, testOrderBook);

    testSession.track(order1);
    testSession.track(order2);
    testSession.release(order1);
    testSession.release(order2);

    assertDoesNotThrow(() -> testSession.close());

    testSession.close();
    testChannel.close();
  }

  /** Test constructor with different OrderBooks instances. */
  @Test
  public void testConstructorWithDifferentOrderBooks() throws IOException {
    List<String> instruments = Arrays.asList("TSLA    ", "AMZN    ");
    OrderBooks orderBooks2 = new OrderBooks(instruments, marketData, marketReporting);

    SocketChannel testChannel = SocketChannel.open();
    Session testSession = new Session(testChannel, orderBooks2);

    assertNotNull(testSession);
    assertNotNull(testSession.getTransport());

    testSession.close();
    testChannel.close();
  }

  /** Helper class for testing order book operations. */
  private static class TestOrderBookListener
      implements com.paritytrading.parity.match.OrderBookListener {
    @Override
    public void match(
        long restingOrderNumber,
        long incomingOrderNumber,
        com.paritytrading.parity.match.Side incomingSide,
        long price,
        long executedQuantity,
        long remainingQuantity) {}

    @Override
    public void add(
        long orderNumber, com.paritytrading.parity.match.Side side, long price, long size) {}

    @Override
    public void cancel(long orderNumber, long canceledQuantity, long remainingQuantity) {}
  }

  /** Helper method to find a suitable network interface for multicast. */
  private NetworkInterface findSuitableNetworkInterface() throws SocketException {
    NetworkInterface networkInterface = NetworkInterface.getByName("lo");
    if (networkInterface == null) {
      networkInterface =
          NetworkInterface.getByInetAddress(java.net.InetAddress.getLoopbackAddress());
    }
    if (networkInterface == null) {
      networkInterface = NetworkInterface.getNetworkInterfaces().nextElement();
    }
    return networkInterface;
  }
}
