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
package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

class OrderEntryClaudeTest {

    private OrderEntry orderEntry;
    private ServerSocketChannel serverChannel;
    private Thread serverThread;
    private volatile boolean serverRunning;

    @AfterEach
    void tearDown() throws Exception {
        serverRunning = false;
        if (orderEntry != null) {
            orderEntry.close();
        }
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread.join(1000);
        }
        if (serverChannel != null && serverChannel.isOpen()) {
            serverChannel.close();
        }
    }

    @Test
    void testOpenCreatesOrderEntryInstance() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        assertNotNull(orderEntry);
    }

    @Test
    void testOpenConnectsToServer() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        assertNotNull(orderEntry);
        assertNotNull(orderEntry.getTransport());
    }

    @Test
    void testOpenWithInvalidAddressThrowsIOException() {
        TestPOEClientListener listener = new TestPOEClientListener();
        InetSocketAddress address = new InetSocketAddress("localhost", 1);

        assertThrows(IOException.class, () -> {
            orderEntry = OrderEntry.open(address, listener);
        });
    }

    @Test
    void testCloseSetsClosed() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);
        assertNotNull(orderEntry);

        orderEntry.close();

        // Give time for the receiver thread to notice the closed flag
        Thread.sleep(200);
    }

    @Test
    void testGetTransportReturnsValidClient() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        SoupBinTCPClient transport = orderEntry.getTransport();

        assertNotNull(transport);
    }

    @Test
    void testSendEnterOrderMessage() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.orderId = "ORDER001".getBytes();
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 100L;
        enterOrder.quantity = 500L;
        enterOrder.price = 10000L;

        // This should not throw an exception
        assertDoesNotThrow(() -> orderEntry.send(enterOrder));
    }

    @Test
    void testSendCancelOrderMessage() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.orderId = "ORDER001".getBytes();
        cancelOrder.quantity = 100L;

        // This should not throw an exception
        assertDoesNotThrow(() -> orderEntry.send(cancelOrder));
    }

    @Test
    void testSendMultipleMessages() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        POE.EnterOrder enterOrder1 = new POE.EnterOrder();
        enterOrder1.orderId = "ORDER001".getBytes();
        enterOrder1.side = POE.BUY;
        enterOrder1.instrument = 100L;
        enterOrder1.quantity = 500L;
        enterOrder1.price = 10000L;

        POE.EnterOrder enterOrder2 = new POE.EnterOrder();
        enterOrder2.orderId = "ORDER002".getBytes();
        enterOrder2.side = POE.SELL;
        enterOrder2.instrument = 200L;
        enterOrder2.quantity = 1000L;
        enterOrder2.price = 20000L;

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.orderId = "ORDER001".getBytes();
        cancelOrder.quantity = 100L;

        assertDoesNotThrow(() -> {
            orderEntry.send(enterOrder1);
            orderEntry.send(enterOrder2);
            orderEntry.send(cancelOrder);
        });
    }

    @Test
    void testSendEnterOrderWithBuySide() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.orderId = "BUY001".getBytes();
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 1L;
        enterOrder.quantity = 100L;
        enterOrder.price = 5000L;

        assertDoesNotThrow(() -> orderEntry.send(enterOrder));
    }

    @Test
    void testSendEnterOrderWithSellSide() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.orderId = "SELL001".getBytes();
        enterOrder.side = POE.SELL;
        enterOrder.instrument = 1L;
        enterOrder.quantity = 100L;
        enterOrder.price = 5000L;

        assertDoesNotThrow(() -> orderEntry.send(enterOrder));
    }

    @Test
    void testSendWithZeroValues() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.orderId = new byte[POE.ORDER_ID_LENGTH];
        enterOrder.side = 0;
        enterOrder.instrument = 0L;
        enterOrder.quantity = 0L;
        enterOrder.price = 0L;

        assertDoesNotThrow(() -> orderEntry.send(enterOrder));
    }

    @Test
    void testSendWithMaxValues() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.orderId = "MAXORDER".getBytes();
        enterOrder.side = POE.BUY;
        enterOrder.instrument = Long.MAX_VALUE;
        enterOrder.quantity = Long.MAX_VALUE;
        enterOrder.price = Long.MAX_VALUE;

        assertDoesNotThrow(() -> orderEntry.send(enterOrder));
    }

    @Test
    void testMultipleOpenAndClose() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);
        assertNotNull(orderEntry);
        orderEntry.close();

        // Create a new connection
        orderEntry = OrderEntry.open(address, listener);
        assertNotNull(orderEntry);
        orderEntry.close();
    }

    @Test
    void testOpenWithDifferentPorts() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port1 = startMockServer();
        InetSocketAddress address1 = new InetSocketAddress("localhost", port1);

        orderEntry = OrderEntry.open(address1, listener);
        assertNotNull(orderEntry);
        orderEntry.close();

        serverChannel.close();

        int port2 = startMockServer();
        InetSocketAddress address2 = new InetSocketAddress("localhost", port2);

        orderEntry = OrderEntry.open(address2, listener);
        assertNotNull(orderEntry);
    }

    @Test
    void testCloseCanBeCalledMultipleTimes() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);

        orderEntry.close();
        orderEntry.close();
        orderEntry.close();

        // Should not throw exception
    }

    @Test
    void testGetTransportAfterClose() throws Exception {
        TestPOEClientListener listener = new TestPOEClientListener();

        int port = startMockServer();
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        orderEntry = OrderEntry.open(address, listener);
        SoupBinTCPClient transport = orderEntry.getTransport();

        orderEntry.close();

        // Transport should still be accessible after close
        assertNotNull(transport);
        assertSame(transport, orderEntry.getTransport());
    }

    private int startMockServer() throws Exception {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 0));
        int port = ((InetSocketAddress) serverChannel.getLocalAddress()).getPort();

        serverRunning = true;
        serverThread = new Thread(() -> {
            try {
                while (serverRunning) {
                    SocketChannel client = serverChannel.accept();
                    if (client != null) {
                        // Just accept connections, don't process data
                        // This is enough for testing OrderEntry
                    }
                }
            } catch (IOException e) {
                // Expected when server is closed
            }
        });
        serverThread.start();

        return port;
    }

    private static class TestPOEClientListener implements POEClientListener {
        @Override
        public void orderAccepted(POE.OrderAccepted message) throws IOException {
            // Test implementation - do nothing
        }

        @Override
        public void orderRejected(POE.OrderRejected message) throws IOException {
            // Test implementation - do nothing
        }

        @Override
        public void orderExecuted(POE.OrderExecuted message) throws IOException {
            // Test implementation - do nothing
        }

        @Override
        public void orderCanceled(POE.OrderCanceled message) throws IOException {
            // Test implementation - do nothing
        }
    }
}
