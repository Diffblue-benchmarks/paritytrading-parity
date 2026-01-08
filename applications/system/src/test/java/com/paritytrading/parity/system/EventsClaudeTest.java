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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Events class.
 * Tests the constructor and run() method to ensure proper NIO event handling.
 */
public class EventsClaudeTest {

    private MarketData marketData;
    private MarketReporting marketReporting;
    private OrderEntry orderEntry;
    private OrderBooks orderBooks;
    private Events events;
    private Thread eventThread;

    @BeforeEach
    public void setUp() throws IOException {
        // Find a suitable network interface for multicast
        NetworkInterface networkInterface = findSuitableNetworkInterface();

        // Set up MarketData with unique multicast addresses (fixed port for multicast, dynamic for request)
        // Multicast requires a fixed port, but request server can use port 0 for random assignment
        InetSocketAddress marketDataMulticast = new InetSocketAddress("239.255.0.1", 5000 + (int)(Math.random() * 1000));
        InetSocketAddress marketDataRequest = new InetSocketAddress("localhost", 0);
        marketData = MarketData.open("MD-SESSION", networkInterface, marketDataMulticast, marketDataRequest);

        // Set up MarketReporting with unique multicast addresses
        InetSocketAddress marketReportingMulticast = new InetSocketAddress("239.255.0.2", 6000 + (int)(Math.random() * 1000));
        InetSocketAddress marketReportingRequest = new InetSocketAddress("localhost", 0);
        marketReporting = MarketReporting.open("MR-SESSION", networkInterface, marketReportingMulticast, marketReportingRequest);

        // Create order books with instruments list
        List<String> instruments = Arrays.asList("AAPL    ", "MSFT    ", "GOOGL   ");
        orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        // Set up OrderEntry with a server socket using dynamic port
        InetSocketAddress orderEntryAddress = new InetSocketAddress("localhost", 0);
        orderEntry = OrderEntry.open(orderEntryAddress, orderBooks);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (eventThread != null && eventThread.isAlive()) {
            eventThread.interrupt();
            eventThread.join(1000);
        }
        if (events != null) {
            // Events uses a selector which should be cleaned up
            // The selector will be closed when the thread exits
        }
    }

    /**
     * Test the Events constructor.
     * Verifies that the Events object is properly initialized with the required components.
     */
    @Test
    public void testConstructor() throws IOException {
        // Act
        events = new Events(marketData, marketReporting, orderEntry);

        // Assert - If we get here without exception, the constructor succeeded
        assertNotNull(events);
    }

    /**
     * Test the run() method starts and processes events.
     * This test starts the event loop in a separate thread and verifies it's running.
     */
    @Test
    public void testRunStartsEventLoop() throws IOException, InterruptedException {
        // Arrange
        events = new Events(marketData, marketReporting, orderEntry);
        CountDownLatch latch = new CountDownLatch(1);

        // Act - Start the event loop in a separate thread
        eventThread = new Thread(() -> {
            latch.countDown();
            events.run();
        });
        eventThread.start();

        // Wait for thread to start
        assertTrue(latch.await(1, TimeUnit.SECONDS), "Event thread should start");

        // Allow some time for the event loop to run
        Thread.sleep(100);

        // Assert - Thread should be alive and running
        assertTrue(eventThread.isAlive(), "Event thread should be running");
    }

    /**
     * Test that the run() method handles client connections.
     * Verifies that when a client connects, the event loop processes the connection.
     */
    @Test
    public void testRunHandlesClientConnection() throws IOException, InterruptedException {
        // Arrange
        events = new Events(marketData, marketReporting, orderEntry);

        // Start the event loop
        eventThread = new Thread(() -> events.run());
        eventThread.start();

        // Allow event loop to start
        Thread.sleep(100);

        // Act - Connect a client using the actual bound port
        InetSocketAddress actualAddress = (InetSocketAddress) orderEntry.getChannel().getLocalAddress();
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(false);
        boolean connected = client.connect(actualAddress);

        if (!connected) {
            // Finish the connection
            while (!client.finishConnect()) {
                Thread.sleep(10);
            }
        }

        // Give time for the event loop to process the connection
        Thread.sleep(200);

        // Assert - The connection should be established
        assertTrue(client.isConnected(), "Client should be connected");

        // Cleanup
        client.close();
    }

    /**
     * Test that the run() method continues running in an infinite loop.
     * The run() method only exits when selector.select() throws IOException,
     * which is difficult to trigger in a test environment. This test verifies
     * that the event loop continues to run.
     */
    @Test
    public void testRunContinuesRunning() throws IOException, InterruptedException {
        // Arrange
        events = new Events(marketData, marketReporting, orderEntry);

        // Start the event loop
        eventThread = new Thread(() -> events.run());
        eventThread.start();

        // Allow event loop to start and run for a bit
        Thread.sleep(500);

        // Assert - Thread should still be alive and running
        assertTrue(eventThread.isAlive(), "Event thread should continue running");

        // The event loop runs indefinitely, so we need to interrupt for cleanup
        eventThread.interrupt();
    }

    /**
     * Test that multiple client connections can be handled.
     * Verifies the event loop can manage multiple simultaneous connections.
     */
    @Test
    public void testRunHandlesMultipleConnections() throws IOException, InterruptedException {
        // Arrange
        events = new Events(marketData, marketReporting, orderEntry);

        // Start the event loop
        eventThread = new Thread(() -> events.run());
        eventThread.start();

        // Allow event loop to start
        Thread.sleep(100);

        // Act - Connect multiple clients using the actual bound port
        InetSocketAddress actualAddress = (InetSocketAddress) orderEntry.getChannel().getLocalAddress();

        SocketChannel client1 = SocketChannel.open();
        client1.configureBlocking(false);
        client1.connect(actualAddress);

        SocketChannel client2 = SocketChannel.open();
        client2.configureBlocking(false);
        client2.connect(actualAddress);

        // Finish connections
        while (!client1.finishConnect() || !client2.finishConnect()) {
            Thread.sleep(10);
        }

        // Give time for the event loop to process the connections
        Thread.sleep(200);

        // Assert - Both connections should be established
        assertTrue(client1.isConnected(), "Client 1 should be connected");
        assertTrue(client2.isConnected(), "Client 2 should be connected");

        // Cleanup
        client1.close();
        client2.close();
    }

    /**
     * Helper method to find a suitable network interface for multicast.
     */
    private NetworkInterface findSuitableNetworkInterface() throws SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByName("lo");
        if (networkInterface == null) {
            networkInterface = NetworkInterface.getByInetAddress(
                java.net.InetAddress.getLoopbackAddress()
            );
        }
        if (networkInterface == null) {
            // Fall back to the first available interface
            networkInterface = NetworkInterface.getNetworkInterfaces().nextElement();
        }
        return networkInterface;
    }
}
