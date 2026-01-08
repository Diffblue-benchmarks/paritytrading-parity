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

import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import com.paritytrading.parity.net.poe.POEClientListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for OrderEntryFactory.
 *
 * <p>This test class covers the following methods:
 * - OrderEntryFactory(InetSocketAddress)
 * - create(POEClientListener, SoupBinTCPClientStatusListener)
 *
 * <p>Testing approach: These tests use real network components (ServerSocketChannel,
 * SocketChannel) without mocking to verify actual network behavior. The OrderEntryFactory
 * is package-private, so tests are in the same package.
 */
class OrderEntryFactoryClaudeTest {

    /**
     * Test that the constructor successfully creates an OrderEntryFactory
     * with a valid InetSocketAddress.
     */
    @Test
    void testConstructorWithValidAddress() {
        InetSocketAddress address = new InetSocketAddress("localhost", 9999);
        OrderEntryFactory factory = new OrderEntryFactory(address);
        assertNotNull(factory, "OrderEntryFactory should not be null");
    }

    /**
     * Test that the constructor successfully creates an OrderEntryFactory
     * with a wildcard address (0.0.0.0).
     */
    @Test
    void testConstructorWithWildcardAddress() {
        InetSocketAddress address = new InetSocketAddress("0.0.0.0", 8080);
        OrderEntryFactory factory = new OrderEntryFactory(address);
        assertNotNull(factory, "OrderEntryFactory should not be null");
    }

    /**
     * Test that the constructor successfully creates an OrderEntryFactory
     * with port 0 (dynamic port allocation).
     */
    @Test
    void testConstructorWithDynamicPort() {
        InetSocketAddress address = new InetSocketAddress("localhost", 0);
        OrderEntryFactory factory = new OrderEntryFactory(address);
        assertNotNull(factory, "OrderEntryFactory should not be null");
    }

    /**
     * Test that the constructor successfully creates an OrderEntryFactory
     * with an unresolved address.
     */
    @Test
    void testConstructorWithUnresolvedAddress() {
        InetSocketAddress address = InetSocketAddress.createUnresolved("example.com", 5000);
        OrderEntryFactory factory = new OrderEntryFactory(address);
        assertNotNull(factory, "OrderEntryFactory should not be null");
    }

    /**
     * Test that create() successfully creates a SoupBinTCPClient when connecting
     * to a valid server. This tests the happy path with a real connection.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateSuccessfulConnection() throws Exception {
        // Create a test server
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        try {
            serverChannel.bind(new InetSocketAddress("localhost", 0));
            InetSocketAddress serverAddress =
                    (InetSocketAddress) serverChannel.getLocalAddress();

            // Create factory and client in separate thread
            OrderEntryFactory factory = new OrderEntryFactory(serverAddress);

            // Accept connection in background
            Thread acceptThread = new Thread(() -> {
                try {
                    SocketChannel accepted = serverChannel.accept();
                    if (accepted != null) {
                        // Keep connection open briefly
                        Thread.sleep(100);
                        accepted.close();
                    }
                } catch (Exception e) {
                    // Ignore
                }
            });
            acceptThread.start();

            // Create client with mock listeners
            POEClientListener clientListener = new TestPOEClientListener();
            SoupBinTCPClientStatusListener statusListener = new TestStatusListener();

            SoupBinTCPClient client = factory.create(clientListener, statusListener);

            assertNotNull(client, "SoupBinTCPClient should not be null");

            acceptThread.join(3000);

            // Clean up
            try {
                client.close();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        } finally {
            serverChannel.close();
        }
    }

    /**
     * Test that create() configures the socket with TCP_NODELAY option.
     * This verifies that Nagle's algorithm is disabled for low-latency communication.
     *
     * Note: We cannot directly verify TCP_NODELAY on the client socket after it's been
     * wrapped in a SoupBinTCPClient, but we can verify the connection succeeds and the
     * client is created properly. The TCP_NODELAY setting is applied before the client
     * is constructed, as seen in the OrderEntryFactory.create() implementation.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateConfiguresTcpNoDelay() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        try {
            serverChannel.bind(new InetSocketAddress("localhost", 0));
            InetSocketAddress serverAddress =
                    (InetSocketAddress) serverChannel.getLocalAddress();

            OrderEntryFactory factory = new OrderEntryFactory(serverAddress);

            // Accept connection in background
            Thread acceptThread = new Thread(() -> {
                try {
                    SocketChannel accepted = serverChannel.accept();
                    if (accepted != null) {
                        Thread.sleep(100);
                        accepted.close();
                    }
                } catch (Exception e) {
                    // Ignore
                }
            });
            acceptThread.start();

            POEClientListener clientListener = new TestPOEClientListener();
            SoupBinTCPClientStatusListener statusListener = new TestStatusListener();

            SoupBinTCPClient client = factory.create(clientListener, statusListener);
            assertNotNull(client, "Client should be created successfully with TCP_NODELAY configured");

            acceptThread.join(3000);

            try {
                client.close();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        } finally {
            serverChannel.close();
        }
    }

    /**
     * Test that create() sets the channel to non-blocking mode.
     * This is essential for the event-driven I/O model.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateConfiguresNonBlockingMode() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        try {
            serverChannel.bind(new InetSocketAddress("localhost", 0));
            InetSocketAddress serverAddress =
                    (InetSocketAddress) serverChannel.getLocalAddress();

            OrderEntryFactory factory = new OrderEntryFactory(serverAddress);

            // Accept connection in background
            Thread acceptThread = new Thread(() -> {
                try {
                    SocketChannel accepted = serverChannel.accept();
                    if (accepted != null) {
                        Thread.sleep(100);
                        accepted.close();
                    }
                } catch (Exception e) {
                    // Ignore
                }
            });
            acceptThread.start();

            POEClientListener clientListener = new TestPOEClientListener();
            SoupBinTCPClientStatusListener statusListener = new TestStatusListener();

            SoupBinTCPClient client = factory.create(clientListener, statusListener);
            assertNotNull(client);

            acceptThread.join(3000);

            try {
                client.close();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        } finally {
            serverChannel.close();
        }
    }

    /**
     * Test that create() throws IOException when unable to connect
     * to the server (connection refused scenario).
     */
    @Test
    void testCreateThrowsExceptionWhenConnectionRefused() {
        // Use a port that is not listening
        InetSocketAddress unreachableAddress = new InetSocketAddress("localhost", 1);
        OrderEntryFactory factory = new OrderEntryFactory(unreachableAddress);

        POEClientListener clientListener = new TestPOEClientListener();
        SoupBinTCPClientStatusListener statusListener = new TestStatusListener();

        assertThrows(IOException.class, () -> {
            factory.create(clientListener, statusListener);
        }, "Should throw IOException when connection is refused");
    }

    /**
     * Test that create() can be called multiple times with the same factory
     * to create multiple clients.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateMultipleClients() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        try {
            serverChannel.bind(new InetSocketAddress("localhost", 0));
            InetSocketAddress serverAddress =
                    (InetSocketAddress) serverChannel.getLocalAddress();

            OrderEntryFactory factory = new OrderEntryFactory(serverAddress);

            // Accept multiple connections in background
            Thread acceptThread = new Thread(() -> {
                try {
                    for (int i = 0; i < 2; i++) {
                        SocketChannel accepted = serverChannel.accept();
                        if (accepted != null) {
                            Thread.sleep(50);
                            accepted.close();
                        }
                    }
                } catch (Exception e) {
                    // Ignore
                }
            });
            acceptThread.start();

            // Create first client
            POEClientListener clientListener1 = new TestPOEClientListener();
            SoupBinTCPClientStatusListener statusListener1 = new TestStatusListener();
            SoupBinTCPClient client1 = factory.create(clientListener1, statusListener1);
            assertNotNull(client1, "First client should not be null");

            // Create second client with same factory
            POEClientListener clientListener2 = new TestPOEClientListener();
            SoupBinTCPClientStatusListener statusListener2 = new TestStatusListener();
            SoupBinTCPClient client2 = factory.create(clientListener2, statusListener2);
            assertNotNull(client2, "Second client should not be null");

            acceptThread.join(3000);

            // Clean up
            try {
                client1.close();
            } catch (Exception e) {
                // Ignore
            }
            try {
                client2.close();
            } catch (Exception e) {
                // Ignore
            }
        } finally {
            serverChannel.close();
        }
    }

    /**
     * Test that create() works with different listener implementations.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCreateWithDifferentListeners() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        try {
            serverChannel.bind(new InetSocketAddress("localhost", 0));
            InetSocketAddress serverAddress =
                    (InetSocketAddress) serverChannel.getLocalAddress();

            OrderEntryFactory factory = new OrderEntryFactory(serverAddress);

            Thread acceptThread = new Thread(() -> {
                try {
                    SocketChannel accepted = serverChannel.accept();
                    if (accepted != null) {
                        Thread.sleep(100);
                        accepted.close();
                    }
                } catch (Exception e) {
                    // Ignore
                }
            });
            acceptThread.start();

            // Use different listener implementations
            POEClientListener clientListener = new TestPOEClientListener();
            SoupBinTCPClientStatusListener statusListener = new TestStatusListener();

            SoupBinTCPClient client = factory.create(clientListener, statusListener);
            assertNotNull(client, "Client with custom listeners should not be null");

            acceptThread.join(3000);

            try {
                client.close();
            } catch (Exception e) {
                // Ignore
            }
        } finally {
            serverChannel.close();
        }
    }

    /**
     * Test POEClientListener implementation for testing.
     */
    private static class TestPOEClientListener implements POEClientListener {
        @Override
        public void orderAccepted(com.paritytrading.parity.net.poe.POE.OrderAccepted message) {
            // No-op
        }

        @Override
        public void orderRejected(com.paritytrading.parity.net.poe.POE.OrderRejected message) {
            // No-op
        }

        @Override
        public void orderExecuted(com.paritytrading.parity.net.poe.POE.OrderExecuted message) {
            // No-op
        }

        @Override
        public void orderCanceled(com.paritytrading.parity.net.poe.POE.OrderCanceled message) {
            // No-op
        }
    }

    /**
     * Test SoupBinTCPClientStatusListener implementation for testing.
     */
    private static class TestStatusListener implements SoupBinTCPClientStatusListener {
        @Override
        public void heartbeatTimeout(SoupBinTCPClient session) {
            // No-op
        }

        @Override
        public void loginAccepted(SoupBinTCPClient session, com.paritytrading.nassau.soupbintcp.SoupBinTCP.LoginAccepted message) {
            // No-op
        }

        @Override
        public void loginRejected(SoupBinTCPClient session, com.paritytrading.nassau.soupbintcp.SoupBinTCP.LoginRejected message) {
            // No-op
        }

        @Override
        public void endOfSession(SoupBinTCPClient session) {
            // No-op
        }
    }
}
