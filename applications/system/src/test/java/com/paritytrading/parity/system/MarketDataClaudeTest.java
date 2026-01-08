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

import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MarketData class.
 * Tests the open() method, getter methods, and message sending methods.
 */
public class MarketDataClaudeTest {

    private MarketData marketData;
    private NetworkInterface networkInterface;
    private InetSocketAddress multicastGroup;
    private InetSocketAddress requestAddress;

    @BeforeEach
    public void setUp() throws IOException {
        // Find a suitable network interface for multicast
        networkInterface = findSuitableNetworkInterface();

        // Use unique addresses for each test
        multicastGroup = new InetSocketAddress("239.255.0.3", 7000 + (int)(Math.random() * 1000));
        requestAddress = new InetSocketAddress("localhost", 0);
    }

    @AfterEach
    public void tearDown() {
        if (marketData != null) {
            try {
                // Clean up by closing the transport channels
                if (marketData.getTransport() != null && marketData.getTransport().getChannel() != null) {
                    marketData.getTransport().getChannel().close();
                }
                if (marketData.getRequestTransport() != null && marketData.getRequestTransport().getChannel() != null) {
                    marketData.getRequestTransport().getChannel().close();
                }
            } catch (IOException e) {
                // Ignore cleanup exceptions
            }
        }
    }

    /**
     * Test the open() method successfully creates a MarketData instance.
     */
    @Test
    public void testOpenCreatesMarketDataInstance() throws IOException {
        // Act
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Assert
        assertNotNull(marketData, "MarketData instance should be created");
    }

    /**
     * Test the open() method with different session strings.
     */
    @Test
    public void testOpenWithDifferentSessionNames() throws IOException {
        // Act - Test with various session names
        marketData = MarketData.open("SESSION-1", networkInterface, multicastGroup, requestAddress);
        assertNotNull(marketData);

        // Cleanup first instance
        marketData.getTransport().getChannel().close();
        marketData.getRequestTransport().getChannel().close();

        // Create with different session
        InetSocketAddress multicastGroup2 = new InetSocketAddress("239.255.0.4", 7500 + (int)(Math.random() * 1000));
        InetSocketAddress requestAddress2 = new InetSocketAddress("localhost", 0);
        marketData = MarketData.open("SESSION-2", networkInterface, multicastGroup2, requestAddress2);

        // Assert
        assertNotNull(marketData);
    }

    /**
     * Test getTransport() returns a non-null MoldUDP64Server.
     */
    @Test
    public void testGetTransportReturnsServer() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act
        MoldUDP64Server transport = marketData.getTransport();

        // Assert
        assertNotNull(transport, "Transport should not be null");
        assertNotNull(transport.getChannel(), "Transport channel should not be null");
    }

    /**
     * Test getRequestTransport() returns a non-null MoldUDP64RequestServer.
     */
    @Test
    public void testGetRequestTransportReturnsServer() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act
        MoldUDP64RequestServer requestTransport = marketData.getRequestTransport();

        // Assert
        assertNotNull(requestTransport, "Request transport should not be null");
        assertNotNull(requestTransport.getChannel(), "Request transport channel should not be null");
    }

    /**
     * Test that the transport channel is properly configured.
     */
    @Test
    public void testTransportChannelConfiguration() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act
        DatagramChannel channel = marketData.getTransport().getChannel();

        // Assert
        assertNotNull(channel, "Channel should not be null");
        assertTrue(channel.isConnected(), "Channel should be connected");
    }

    /**
     * Test that the request transport channel is properly configured.
     */
    @Test
    public void testRequestTransportChannelConfiguration() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act
        DatagramChannel requestChannel = marketData.getRequestTransport().getChannel();

        // Assert
        assertNotNull(requestChannel, "Request channel should not be null");
        assertTrue(requestChannel.isOpen(), "Request channel should be open");
        assertFalse(requestChannel.isBlocking(), "Request channel should be non-blocking");
        assertNotNull(requestChannel.getLocalAddress(), "Request channel should be bound");
    }

    /**
     * Test serve() method processes requests without throwing exceptions.
     * This method calls serve on the request transport with the internal message store.
     */
    @Test
    public void testServeProcessesRequests() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - serve() should not throw an exception
        assertDoesNotThrow(() -> marketData.serve(), "serve() should not throw exception");
    }

    /**
     * Test serve() method can be called multiple times.
     */
    @Test
    public void testServeCanBeCalledMultipleTimes() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Multiple calls should not throw exceptions
        assertDoesNotThrow(() -> {
            marketData.serve();
            marketData.serve();
            marketData.serve();
        }, "Multiple serve() calls should not throw exception");
    }

    /**
     * Test version() method sends a version message.
     * This verifies the method executes without throwing exceptions.
     */
    @Test
    public void testVersionSendsMessage() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> marketData.version(), "version() should not throw exception");
    }

    /**
     * Test version() method can be called multiple times.
     */
    @Test
    public void testVersionCanBeCalledMultipleTimes() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketData.version();
            marketData.version();
            marketData.version();
        }, "Multiple version() calls should not throw exception");
    }

    /**
     * Test orderAdded() method with valid parameters.
     */
    @Test
    public void testOrderAddedWithValidParameters() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            marketData.orderAdded(12345L, (byte) 'B', 100L, 50L, 10000L);
        }, "orderAdded() with valid parameters should not throw exception");
    }

    /**
     * Test orderAdded() method with buy side.
     */
    @Test
    public void testOrderAddedBuySide() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketData.orderAdded(1L, (byte) 'B', 1L, 100L, 5000L);
        }, "orderAdded() with buy side should not throw exception");
    }

    /**
     * Test orderAdded() method with sell side.
     */
    @Test
    public void testOrderAddedSellSide() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketData.orderAdded(2L, (byte) 'S', 2L, 200L, 6000L);
        }, "orderAdded() with sell side should not throw exception");
    }

    /**
     * Test orderAdded() method with various order numbers.
     */
    @Test
    public void testOrderAddedWithVariousOrderNumbers() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Test with different order numbers
        assertDoesNotThrow(() -> {
            marketData.orderAdded(0L, (byte) 'B', 1L, 10L, 100L);
            marketData.orderAdded(Long.MAX_VALUE, (byte) 'S', 2L, 20L, 200L);
            marketData.orderAdded(12345L, (byte) 'B', 3L, 30L, 300L);
        }, "orderAdded() with various order numbers should not throw exception");
    }

    /**
     * Test orderExecuted() method with valid parameters.
     */
    @Test
    public void testOrderExecutedWithValidParameters() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            marketData.orderExecuted(12345L, 50L, 99999L);
        }, "orderExecuted() with valid parameters should not throw exception");
    }

    /**
     * Test orderExecuted() method with various quantities.
     */
    @Test
    public void testOrderExecutedWithVariousQuantities() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketData.orderExecuted(1L, 1L, 1L);
            marketData.orderExecuted(2L, 100L, 2L);
            marketData.orderExecuted(3L, 1000L, 3L);
            marketData.orderExecuted(4L, Long.MAX_VALUE, 4L);
        }, "orderExecuted() with various quantities should not throw exception");
    }

    /**
     * Test orderExecuted() method can be called multiple times.
     */
    @Test
    public void testOrderExecutedMultipleCalls() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketData.orderExecuted(1L, 10L, 100L);
            marketData.orderExecuted(2L, 20L, 200L);
            marketData.orderExecuted(3L, 30L, 300L);
        }, "Multiple orderExecuted() calls should not throw exception");
    }

    /**
     * Test orderCanceled() method with valid parameters.
     */
    @Test
    public void testOrderCanceledWithValidParameters() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            marketData.orderCanceled(12345L, 25L);
        }, "orderCanceled() with valid parameters should not throw exception");
    }

    /**
     * Test orderCanceled() method with full cancellation.
     */
    @Test
    public void testOrderCanceledFullCancellation() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketData.orderCanceled(1L, 100L);
        }, "orderCanceled() with full cancellation should not throw exception");
    }

    /**
     * Test orderCanceled() method with partial cancellation.
     */
    @Test
    public void testOrderCanceledPartialCancellation() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketData.orderCanceled(2L, 50L);
        }, "orderCanceled() with partial cancellation should not throw exception");
    }

    /**
     * Test orderCanceled() method with various canceled quantities.
     */
    @Test
    public void testOrderCanceledWithVariousQuantities() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketData.orderCanceled(1L, 1L);
            marketData.orderCanceled(2L, 50L);
            marketData.orderCanceled(3L, 1000L);
            marketData.orderCanceled(4L, Long.MAX_VALUE);
        }, "orderCanceled() with various quantities should not throw exception");
    }

    /**
     * Test a sequence of operations simulating real usage.
     */
    @Test
    public void testSequenceOfOperations() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Simulate a sequence of market data events
        assertDoesNotThrow(() -> {
            marketData.version();
            marketData.orderAdded(1L, (byte) 'B', 1L, 100L, 5000L);
            marketData.orderAdded(2L, (byte) 'S', 1L, 100L, 5100L);
            marketData.orderExecuted(1L, 50L, 1L);
            marketData.orderCanceled(1L, 50L);
            marketData.serve();
        }, "Sequence of operations should not throw exception");
    }

    /**
     * Test multiple orders and executions.
     */
    @Test
    public void testMultipleOrdersAndExecutions() throws IOException {
        // Arrange
        marketData = MarketData.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            // Add multiple orders
            for (int i = 1; i <= 5; i++) {
                marketData.orderAdded((long) i, (byte) 'B', (long) i, i * 100L, i * 1000L);
            }

            // Execute some orders
            for (int i = 1; i <= 3; i++) {
                marketData.orderExecuted((long) i, i * 50L, (long) i * 10);
            }

            // Cancel some orders
            for (int i = 4; i <= 5; i++) {
                marketData.orderCanceled((long) i, i * 100L);
            }
        }, "Multiple orders and executions should not throw exception");
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
