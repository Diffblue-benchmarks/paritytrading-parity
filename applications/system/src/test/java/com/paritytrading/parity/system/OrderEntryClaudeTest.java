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
 * Test class for OrderEntry class. Tests the open factory method, getChannel, and accept methods.
 */
public class OrderEntryClaudeTest {
  private MarketData marketData;
  private MarketReporting marketReporting;
  private NetworkInterface networkInterface;
  private OrderBooks orderBooks;
  private OrderEntry orderEntry;

  @BeforeEach
  public void setUp() throws IOException {
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
  }

  @AfterEach
  public void tearDown() {
    if (orderEntry != null) {
      try {
        ServerSocketChannel channel = orderEntry.getChannel();
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
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

  /** Test open creates OrderEntry with valid address and OrderBooks. */
  @Test
  public void testOpenCreatesOrderEntry() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    assertNotNull(orderEntry);
  }

  /** Test open creates OrderEntry with specific port. */
  @Test
  public void testOpenWithSpecificPort() throws IOException {
    int port = 10000 + (int) (Math.random() * 10000);
    InetSocketAddress address = new InetSocketAddress("localhost", port);
    orderEntry = OrderEntry.open(address, orderBooks);
    assertNotNull(orderEntry);
    ServerSocketChannel channel = orderEntry.getChannel();
    assertNotNull(channel);
    assertTrue(channel.isOpen());
  }

  /** Test open creates OrderEntry with port 0 (auto-assign). */
  @Test
  public void testOpenWithAutoAssignedPort() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    assertNotNull(orderEntry);
    ServerSocketChannel channel = orderEntry.getChannel();
    assertNotNull(channel);
    assertTrue(channel.isOpen());
  }

  /** Test getChannel returns non-null ServerSocketChannel. */
  @Test
  public void testGetChannelReturnsServerSocketChannel() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    ServerSocketChannel channel = orderEntry.getChannel();
    assertNotNull(channel);
  }

  /** Test getChannel returns open ServerSocketChannel. */
  @Test
  public void testGetChannelReturnsOpenChannel() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    ServerSocketChannel channel = orderEntry.getChannel();
    assertTrue(channel.isOpen());
  }

  /** Test getChannel returns bound ServerSocketChannel. */
  @Test
  public void testGetChannelReturnsBoundChannel() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    ServerSocketChannel channel = orderEntry.getChannel();
    assertNotNull(channel.getLocalAddress());
  }

  /** Test getChannel returns non-blocking ServerSocketChannel. */
  @Test
  public void testGetChannelReturnsNonBlockingChannel() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    ServerSocketChannel channel = orderEntry.getChannel();
    assertFalse(channel.isBlocking());
  }

  /** Test accept returns null when no connection is pending. */
  @Test
  public void testAcceptReturnsNullWhenNoConnection() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    Session session = orderEntry.accept();
    assertNull(session);
  }

  /** Test accept returns Session when connection is available. */
  @Test
  public void testAcceptReturnsSessionWhenConnectionAvailable() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);

    ServerSocketChannel serverChannel = orderEntry.getChannel();
    InetSocketAddress boundAddress = (InetSocketAddress) serverChannel.getLocalAddress();

    SocketChannel clientChannel = null;
    Session session = null;
    try {
      clientChannel = SocketChannel.open();
      clientChannel.connect(boundAddress);

      // Give the connection time to complete
      Thread.sleep(50);

      session = orderEntry.accept();
      assertNotNull(session);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      fail("Thread interrupted during test");
    } finally {
      if (session != null) {
        session.close();
      }
      if (clientChannel != null && clientChannel.isOpen()) {
        clientChannel.close();
      }
    }
  }

  /** Test accept creates Session with non-blocking SocketChannel. */
  @Test
  public void testAcceptCreatesSessionWithNonBlockingChannel() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);

    ServerSocketChannel serverChannel = orderEntry.getChannel();
    InetSocketAddress boundAddress = (InetSocketAddress) serverChannel.getLocalAddress();

    SocketChannel clientChannel = null;
    Session session = null;
    try {
      clientChannel = SocketChannel.open();
      clientChannel.connect(boundAddress);

      // Give the connection time to complete
      Thread.sleep(50);

      session = orderEntry.accept();
      assertNotNull(session);

      // Verify the session's transport channel is non-blocking
      assertFalse(session.getTransport().getChannel().isBlocking());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      fail("Thread interrupted during test");
    } finally {
      if (session != null) {
        session.close();
      }
      if (clientChannel != null && clientChannel.isOpen()) {
        clientChannel.close();
      }
    }
  }

  /** Test accept multiple times with no connections always returns null. */
  @Test
  public void testAcceptMultipleTimesWithNoConnectionsReturnsNull() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);

    for (int i = 0; i < 5; i++) {
      Session session = orderEntry.accept();
      assertNull(session);
    }
  }

  /** Test accept can accept multiple connections sequentially. */
  @Test
  public void testAcceptMultipleConnectionsSequentially() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);

    ServerSocketChannel serverChannel = orderEntry.getChannel();
    InetSocketAddress boundAddress = (InetSocketAddress) serverChannel.getLocalAddress();

    SocketChannel clientChannel1 = null;
    SocketChannel clientChannel2 = null;
    Session session1 = null;
    Session session2 = null;
    try {
      // First connection
      clientChannel1 = SocketChannel.open();
      clientChannel1.connect(boundAddress);
      Thread.sleep(50);

      session1 = orderEntry.accept();
      assertNotNull(session1);

      // Second connection
      clientChannel2 = SocketChannel.open();
      clientChannel2.connect(boundAddress);
      Thread.sleep(50);

      session2 = orderEntry.accept();
      assertNotNull(session2);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      fail("Thread interrupted during test");
    } finally {
      if (session1 != null) {
        session1.close();
      }
      if (session2 != null) {
        session2.close();
      }
      if (clientChannel1 != null && clientChannel1.isOpen()) {
        clientChannel1.close();
      }
      if (clientChannel2 != null && clientChannel2.isOpen()) {
        clientChannel2.close();
      }
    }
  }

  /** Test open and getChannel consistency. */
  @Test
  public void testOpenAndGetChannelConsistency() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    ServerSocketChannel channel1 = orderEntry.getChannel();
    ServerSocketChannel channel2 = orderEntry.getChannel();
    assertSame(channel1, channel2);
  }

  /** Test open with loopback address. */
  @Test
  public void testOpenWithLoopbackAddress() throws IOException {
    InetSocketAddress address = new InetSocketAddress("127.0.0.1", 0);
    orderEntry = OrderEntry.open(address, orderBooks);
    assertNotNull(orderEntry);
    ServerSocketChannel channel = orderEntry.getChannel();
    assertNotNull(channel);
    assertTrue(channel.isOpen());
  }

  /** Test open with wildcard address. */
  @Test
  public void testOpenWithWildcardAddress() throws IOException {
    InetSocketAddress address = new InetSocketAddress(0);
    orderEntry = OrderEntry.open(address, orderBooks);
    assertNotNull(orderEntry);
    ServerSocketChannel channel = orderEntry.getChannel();
    assertNotNull(channel);
    assertTrue(channel.isOpen());
  }

  /** Test that accept returns null after accept when no more connections are pending. */
  @Test
  public void testAcceptReturnsNullAfterAcceptingOneConnection() throws IOException {
    InetSocketAddress address = new InetSocketAddress("localhost", 0);
    orderEntry = OrderEntry.open(address, orderBooks);

    ServerSocketChannel serverChannel = orderEntry.getChannel();
    InetSocketAddress boundAddress = (InetSocketAddress) serverChannel.getLocalAddress();

    SocketChannel clientChannel = null;
    Session session = null;
    try {
      clientChannel = SocketChannel.open();
      clientChannel.connect(boundAddress);
      Thread.sleep(50);

      session = orderEntry.accept();
      assertNotNull(session);

      // Try to accept again with no new connections
      Session session2 = orderEntry.accept();
      assertNull(session2);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      fail("Thread interrupted during test");
    } finally {
      if (session != null) {
        session.close();
      }
      if (clientChannel != null && clientChannel.isOpen()) {
        clientChannel.close();
      }
    }
  }

  /** Test open binds to the correct address. */
  @Test
  public void testOpenBindsToCorrectAddress() throws IOException {
    int port = 15000 + (int) (Math.random() * 5000);
    InetSocketAddress address = new InetSocketAddress("localhost", port);
    orderEntry = OrderEntry.open(address, orderBooks);
    ServerSocketChannel channel = orderEntry.getChannel();
    InetSocketAddress boundAddress = (InetSocketAddress) channel.getLocalAddress();
    assertEquals(port, boundAddress.getPort());
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
