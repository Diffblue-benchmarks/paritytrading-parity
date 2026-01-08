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
import java.nio.channels.DatagramChannel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MarketReporting class.
 * Tests the open() method, getter methods, and message sending methods.
 */
public class MarketReportingClaudeTest {

    private MarketReporting marketReporting;
    private NetworkInterface networkInterface;
    private InetSocketAddress multicastGroup;
    private InetSocketAddress requestAddress;

    @BeforeEach
    public void setUp() throws IOException {
        // Find a suitable network interface for multicast
        networkInterface = findSuitableNetworkInterface();

        // Use unique addresses for each test
        multicastGroup = new InetSocketAddress("239.255.0.5", 8000 + (int)(Math.random() * 1000));
        requestAddress = new InetSocketAddress("localhost", 0);
    }

    @AfterEach
    public void tearDown() {
        if (marketReporting != null) {
            try {
                // Clean up by closing the transport channels
                if (marketReporting.getTransport() != null && marketReporting.getTransport().getChannel() != null) {
                    marketReporting.getTransport().getChannel().close();
                }
                if (marketReporting.getRequestTransport() != null && marketReporting.getRequestTransport().getChannel() != null) {
                    marketReporting.getRequestTransport().getChannel().close();
                }
            } catch (IOException e) {
                // Ignore cleanup exceptions
            }
        }
    }

    /**
     * Test the open() method successfully creates a MarketReporting instance.
     */
    @Test
    public void testOpenCreatesMarketReportingInstance() throws IOException {
        // Act
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Assert
        assertNotNull(marketReporting, "MarketReporting instance should be created");
    }

    /**
     * Test the open() method with different session strings.
     */
    @Test
    public void testOpenWithDifferentSessionNames() throws IOException {
        // Act - Test with various session names
        marketReporting = MarketReporting.open("SESSION-A", networkInterface, multicastGroup, requestAddress);
        assertNotNull(marketReporting);

        // Cleanup first instance
        marketReporting.getTransport().getChannel().close();
        marketReporting.getRequestTransport().getChannel().close();

        // Create with different session
        InetSocketAddress multicastGroup2 = new InetSocketAddress("239.255.0.6", 8500 + (int)(Math.random() * 1000));
        InetSocketAddress requestAddress2 = new InetSocketAddress("localhost", 0);
        marketReporting = MarketReporting.open("SESSION-B", networkInterface, multicastGroup2, requestAddress2);

        // Assert
        assertNotNull(marketReporting);
    }

    /**
     * Test the open() method with empty session string.
     */
    @Test
    public void testOpenWithEmptySession() throws IOException {
        // Act
        marketReporting = MarketReporting.open("", networkInterface, multicastGroup, requestAddress);

        // Assert
        assertNotNull(marketReporting, "MarketReporting should be created even with empty session");
    }

    /**
     * Test getTransport() returns a non-null MoldUDP64Server.
     */
    @Test
    public void testGetTransportReturnsServer() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act
        MoldUDP64Server transport = marketReporting.getTransport();

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
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act
        MoldUDP64RequestServer requestTransport = marketReporting.getRequestTransport();

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
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act
        DatagramChannel channel = marketReporting.getTransport().getChannel();

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
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act
        DatagramChannel requestChannel = marketReporting.getRequestTransport().getChannel();

        // Assert
        assertNotNull(requestChannel, "Request channel should not be null");
        assertTrue(requestChannel.isOpen(), "Request channel should be open");
        assertFalse(requestChannel.isBlocking(), "Request channel should be non-blocking");
        assertNotNull(requestChannel.getLocalAddress(), "Request channel should be bound");
    }

    /**
     * Test serve() method processes requests without throwing exceptions.
     */
    @Test
    public void testServeProcessesRequests() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - serve() should not throw an exception
        assertDoesNotThrow(() -> marketReporting.serve(), "serve() should not throw exception");
    }

    /**
     * Test serve() method can be called multiple times.
     */
    @Test
    public void testServeCanBeCalledMultipleTimes() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Multiple calls should not throw exceptions
        assertDoesNotThrow(() -> {
            marketReporting.serve();
            marketReporting.serve();
            marketReporting.serve();
        }, "Multiple serve() calls should not throw exception");
    }

    /**
     * Test version() method sends a version message.
     */
    @Test
    public void testVersionSendsMessage() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> marketReporting.version(), "version() should not throw exception");
    }

    /**
     * Test version() method can be called multiple times.
     */
    @Test
    public void testVersionCanBeCalledMultipleTimes() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.version();
            marketReporting.version();
            marketReporting.version();
        }, "Multiple version() calls should not throw exception");
    }

    /**
     * Test orderEntered() method with valid parameters for buy order.
     */
    @Test
    public void testOrderEnteredBuySide() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            marketReporting.orderEntered(1000L, 12345L, (byte) 'B', 100L, 50L, 10000L);
        }, "orderEntered() with buy side should not throw exception");
    }

    /**
     * Test orderEntered() method with valid parameters for sell order.
     */
    @Test
    public void testOrderEnteredSellSide() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.orderEntered(2000L, 67890L, (byte) 'S', 200L, 100L, 20000L);
        }, "orderEntered() with sell side should not throw exception");
    }

    /**
     * Test orderEntered() method with various parameter values.
     */
    @Test
    public void testOrderEnteredWithVariousParameters() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Test with different parameter values
        assertDoesNotThrow(() -> {
            marketReporting.orderEntered(0L, 0L, (byte) 'B', 0L, 0L, 0L);
            marketReporting.orderEntered(Long.MAX_VALUE, Long.MAX_VALUE, (byte) 'S', Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
            marketReporting.orderEntered(123L, 456L, (byte) 'B', 789L, 100L, 5000L);
        }, "orderEntered() with various parameters should not throw exception");
    }

    /**
     * Test orderEntered() method with minimum values.
     */
    @Test
    public void testOrderEnteredWithMinimumValues() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.orderEntered(0L, 1L, (byte) 'B', 1L, 1L, 1L);
        }, "orderEntered() with minimum values should not throw exception");
    }

    /**
     * Test orderAdded() method with valid order number.
     */
    @Test
    public void testOrderAddedWithValidOrderNumber() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            marketReporting.orderAdded(12345L);
        }, "orderAdded() with valid order number should not throw exception");
    }

    /**
     * Test orderAdded() method with various order numbers.
     */
    @Test
    public void testOrderAddedWithVariousOrderNumbers() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Test with different order numbers
        assertDoesNotThrow(() -> {
            marketReporting.orderAdded(0L);
            marketReporting.orderAdded(1L);
            marketReporting.orderAdded(Long.MAX_VALUE);
            marketReporting.orderAdded(999999L);
        }, "orderAdded() with various order numbers should not throw exception");
    }

    /**
     * Test orderAdded() method can be called multiple times.
     */
    @Test
    public void testOrderAddedMultipleCalls() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.orderAdded(1L);
            marketReporting.orderAdded(2L);
            marketReporting.orderAdded(3L);
        }, "Multiple orderAdded() calls should not throw exception");
    }

    /**
     * Test orderCanceled() method with valid parameters.
     */
    @Test
    public void testOrderCanceledWithValidParameters() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            marketReporting.orderCanceled(12345L, 25L);
        }, "orderCanceled() with valid parameters should not throw exception");
    }

    /**
     * Test orderCanceled() method with full cancellation.
     */
    @Test
    public void testOrderCanceledFullCancellation() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.orderCanceled(1L, 100L);
        }, "orderCanceled() with full cancellation should not throw exception");
    }

    /**
     * Test orderCanceled() method with partial cancellation.
     */
    @Test
    public void testOrderCanceledPartialCancellation() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.orderCanceled(2L, 50L);
        }, "orderCanceled() with partial cancellation should not throw exception");
    }

    /**
     * Test orderCanceled() method with various canceled quantities.
     */
    @Test
    public void testOrderCanceledWithVariousQuantities() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.orderCanceled(1L, 1L);
            marketReporting.orderCanceled(2L, 50L);
            marketReporting.orderCanceled(3L, 1000L);
            marketReporting.orderCanceled(4L, Long.MAX_VALUE);
        }, "orderCanceled() with various quantities should not throw exception");
    }

    /**
     * Test orderCanceled() method with zero quantity.
     */
    @Test
    public void testOrderCanceledWithZeroQuantity() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.orderCanceled(1L, 0L);
        }, "orderCanceled() with zero quantity should not throw exception");
    }

    /**
     * Test trade() method with valid parameters.
     */
    @Test
    public void testTradeWithValidParameters() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            marketReporting.trade(12345L, 67890L, 100L, 1L);
        }, "trade() with valid parameters should not throw exception");
    }

    /**
     * Test trade() method with various parameter values.
     */
    @Test
    public void testTradeWithVariousParameters() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.trade(0L, 0L, 0L, 0L);
            marketReporting.trade(1L, 1L, 1L, 1L);
            marketReporting.trade(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
            marketReporting.trade(100L, 200L, 50L, 999L);
        }, "trade() with various parameters should not throw exception");
    }

    /**
     * Test trade() method with different quantities.
     */
    @Test
    public void testTradeWithDifferentQuantities() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.trade(1L, 2L, 1L, 1L);
            marketReporting.trade(3L, 4L, 100L, 2L);
            marketReporting.trade(5L, 6L, 1000L, 3L);
            marketReporting.trade(7L, 8L, 10000L, 4L);
        }, "trade() with different quantities should not throw exception");
    }

    /**
     * Test trade() method can be called multiple times.
     */
    @Test
    public void testTradeMultipleCalls() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.trade(1L, 10L, 50L, 100L);
            marketReporting.trade(2L, 20L, 60L, 200L);
            marketReporting.trade(3L, 30L, 70L, 300L);
        }, "Multiple trade() calls should not throw exception");
    }

    /**
     * Test trade() method with same order numbers for resting and incoming.
     */
    @Test
    public void testTradeWithSameOrderNumbers() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.trade(123L, 123L, 50L, 1L);
        }, "trade() with same order numbers should not throw exception");
    }

    /**
     * Test a sequence of operations simulating real usage.
     */
    @Test
    public void testSequenceOfOperations() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Simulate a sequence of market reporting events
        assertDoesNotThrow(() -> {
            marketReporting.version();
            marketReporting.orderEntered(1000L, 1L, (byte) 'B', 1L, 100L, 5000L);
            marketReporting.orderAdded(1L);
            marketReporting.orderEntered(2000L, 2L, (byte) 'S', 1L, 100L, 5100L);
            marketReporting.orderAdded(2L);
            marketReporting.trade(1L, 2L, 100L, 1L);
            marketReporting.orderCanceled(1L, 0L);
            marketReporting.serve();
        }, "Sequence of operations should not throw exception");
    }

    /**
     * Test multiple orders and trades.
     */
    @Test
    public void testMultipleOrdersAndTrades() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            // Enter multiple orders
            for (int i = 1; i <= 5; i++) {
                marketReporting.orderEntered((long) i * 1000, (long) i, (byte) 'B', (long) i, i * 100L, i * 1000L);
                marketReporting.orderAdded((long) i);
            }

            // Execute some trades
            for (int i = 1; i <= 3; i++) {
                marketReporting.trade((long) i, (long) (i + 1), i * 50L, (long) i * 10);
            }

            // Cancel some orders
            for (int i = 4; i <= 5; i++) {
                marketReporting.orderCanceled((long) i, i * 100L);
            }
        }, "Multiple orders and trades should not throw exception");
    }

    /**
     * Test interleaved version calls with other operations.
     */
    @Test
    public void testInterleavedVersionCalls() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.version();
            marketReporting.orderEntered(1000L, 1L, (byte) 'B', 1L, 100L, 5000L);
            marketReporting.version();
            marketReporting.orderAdded(1L);
            marketReporting.version();
            marketReporting.serve();
        }, "Interleaved version calls should not throw exception");
    }

    /**
     * Test serve() calls interleaved with other operations.
     */
    @Test
    public void testInterleavedServeCalls() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marketReporting.serve();
            marketReporting.version();
            marketReporting.serve();
            marketReporting.orderAdded(1L);
            marketReporting.serve();
        }, "Interleaved serve calls should not throw exception");
    }

    /**
     * Test high-frequency message sending.
     */
    @Test
    public void testHighFrequencyMessageSending() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Send many messages in rapid succession
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marketReporting.orderAdded((long) i);
            }
        }, "High frequency message sending should not throw exception");
    }

    /**
     * Test all message types in sequence.
     */
    @Test
    public void testAllMessageTypesInSequence() throws IOException {
        // Arrange
        marketReporting = MarketReporting.open("TEST-SESSION", networkInterface, multicastGroup, requestAddress);

        // Act & Assert - Test all message types
        assertDoesNotThrow(() -> {
            marketReporting.version();
            marketReporting.orderEntered(1000L, 1L, (byte) 'B', 100L, 50L, 10000L);
            marketReporting.orderAdded(1L);
            marketReporting.orderCanceled(1L, 25L);
            marketReporting.trade(1L, 2L, 25L, 1L);
        }, "All message types in sequence should not throw exception");
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
