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

import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.match.OrderBookListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Order class.
 * Tests the constructor and getter methods to ensure proper initialization and data retrieval.
 */
public class OrderClaudeTest {

    private Session session;
    private OrderBook orderBook;
    private byte[] orderId;
    private long orderNumber;

    @BeforeEach
    public void setUp() throws IOException {
        // Create a minimal OrderBook with a no-op listener
        OrderBookListener listener = new OrderBookListener() {
            @Override
            public void match(long restingOrderId, long incomingOrderId,
                            com.paritytrading.parity.match.Side side,
                            long price, long executedQuantity, long remainingQuantity) {
            }

            @Override
            public void add(long orderId, com.paritytrading.parity.match.Side side,
                          long price, long size) {
            }

            @Override
            public void cancel(long orderId, long canceledQuantity, long remainingQuantity) {
            }
        };
        orderBook = new OrderBook(listener);

        // Create a Session using a socket channel and OrderBooks
        NetworkInterface networkInterface = findSuitableNetworkInterface();

        InetSocketAddress marketDataMulticast = new InetSocketAddress("239.255.0.1", 5000 + (int)(Math.random() * 1000));
        InetSocketAddress marketDataRequest = new InetSocketAddress("localhost", 0);
        MarketData marketData = MarketData.open("MD-SESSION", networkInterface, marketDataMulticast, marketDataRequest);

        InetSocketAddress marketReportingMulticast = new InetSocketAddress("239.255.0.2", 6000 + (int)(Math.random() * 1000));
        InetSocketAddress marketReportingRequest = new InetSocketAddress("localhost", 0);
        MarketReporting marketReporting = MarketReporting.open("MR-SESSION", networkInterface, marketReportingMulticast, marketReportingRequest);

        List<String> instruments = Arrays.asList("AAPL    ");
        OrderBooks orderBooks = new OrderBooks(instruments, marketData, marketReporting);

        SocketChannel channel = SocketChannel.open();
        session = new Session(channel, orderBooks);

        // Initialize test data
        orderId = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
        orderNumber = 12345L;
    }

    /**
     * Test the Order constructor with valid parameters.
     * Verifies that all fields are properly initialized.
     */
    @Test
    public void testConstructorWithValidParameters() {
        // Act
        Order order = new Order(orderId, orderNumber, session, orderBook);

        // Assert
        assertNotNull(order);
        assertNotNull(order.getOrderId());
        assertEquals(orderNumber, order.getOrderNumber());
        assertSame(session, order.getSession());
        assertSame(orderBook, order.getBook());
    }

    /**
     * Test that getOrderId returns the correct order ID.
     */
    @Test
    public void testGetOrderIdReturnsCorrectValue() {
        // Arrange
        Order order = new Order(orderId, orderNumber, session, orderBook);

        // Act
        byte[] retrievedOrderId = order.getOrderId();

        // Assert
        assertNotNull(retrievedOrderId);
        assertArrayEquals(orderId, retrievedOrderId);
    }

    /**
     * Test that getOrderNumber returns the correct order number.
     */
    @Test
    public void testGetOrderNumberReturnsCorrectValue() {
        // Arrange
        Order order = new Order(orderId, orderNumber, session, orderBook);

        // Act
        long retrievedOrderNumber = order.getOrderNumber();

        // Assert
        assertEquals(orderNumber, retrievedOrderNumber);
    }

    /**
     * Test that getSession returns the correct session.
     */
    @Test
    public void testGetSessionReturnsCorrectValue() {
        // Arrange
        Order order = new Order(orderId, orderNumber, session, orderBook);

        // Act
        Session retrievedSession = order.getSession();

        // Assert
        assertSame(session, retrievedSession);
    }

    /**
     * Test that getBook returns the correct order book.
     */
    @Test
    public void testGetBookReturnsCorrectValue() {
        // Arrange
        Order order = new Order(orderId, orderNumber, session, orderBook);

        // Act
        OrderBook retrievedBook = order.getBook();

        // Assert
        assertSame(orderBook, retrievedBook);
    }

    /**
     * Test that the constructor creates a defensive copy of orderId.
     * Modifying the original array should not affect the Order's internal state.
     */
    @Test
    public void testConstructorClonesOrderId() {
        // Arrange
        byte[] originalOrderId = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
        Order order = new Order(originalOrderId, orderNumber, session, orderBook);

        // Act - Modify the original array
        originalOrderId[0] = 99;

        // Assert - The Order's orderId should not be affected
        byte[] retrievedOrderId = order.getOrderId();
        assertEquals(1, retrievedOrderId[0]);
        assertNotEquals(99, retrievedOrderId[0]);
    }

    /**
     * Test constructor with different order numbers including edge cases.
     */
    @Test
    public void testConstructorWithDifferentOrderNumbers() {
        // Test with zero
        Order order1 = new Order(orderId, 0L, session, orderBook);
        assertEquals(0L, order1.getOrderNumber());

        // Test with negative number
        Order order2 = new Order(orderId, -1L, session, orderBook);
        assertEquals(-1L, order2.getOrderNumber());

        // Test with large positive number
        Order order3 = new Order(orderId, Long.MAX_VALUE, session, orderBook);
        assertEquals(Long.MAX_VALUE, order3.getOrderNumber());

        // Test with large negative number
        Order order4 = new Order(orderId, Long.MIN_VALUE, session, orderBook);
        assertEquals(Long.MIN_VALUE, order4.getOrderNumber());
    }

    /**
     * Test constructor with different order ID byte arrays.
     */
    @Test
    public void testConstructorWithDifferentOrderIds() {
        // Test with empty array
        byte[] emptyOrderId = new byte[] {};
        Order order1 = new Order(emptyOrderId, orderNumber, session, orderBook);
        assertEquals(0, order1.getOrderId().length);

        // Test with single byte
        byte[] singleByteOrderId = new byte[] {42};
        Order order2 = new Order(singleByteOrderId, orderNumber, session, orderBook);
        assertArrayEquals(singleByteOrderId, order2.getOrderId());

        // Test with larger array
        byte[] largeOrderId = new byte[100];
        Arrays.fill(largeOrderId, (byte) 7);
        Order order3 = new Order(largeOrderId, orderNumber, session, orderBook);
        assertArrayEquals(largeOrderId, order3.getOrderId());
    }

    /**
     * Test that multiple Order instances maintain independent orderId arrays.
     */
    @Test
    public void testMultipleOrdersHaveIndependentOrderIds() {
        // Arrange
        byte[] orderId1 = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
        byte[] orderId2 = new byte[] {9, 10, 11, 12, 13, 14, 15, 16};

        // Act
        Order order1 = new Order(orderId1, 100L, session, orderBook);
        Order order2 = new Order(orderId2, 200L, session, orderBook);

        // Assert - Each order should have its own independent orderId
        assertArrayEquals(orderId1, order1.getOrderId());
        assertArrayEquals(orderId2, order2.getOrderId());
        assertFalse(Arrays.equals(order1.getOrderId(), order2.getOrderId()));
    }

    /**
     * Test that getOrderId returns a reference to the internal array, not a copy.
     * This tests the actual behavior of the implementation.
     * Note: While defensive copying in the constructor is good, the getter returns
     * the internal array reference, which could allow external modification.
     */
    @Test
    public void testGetOrderIdReturnsSameArrayReference() {
        // Arrange
        Order order = new Order(orderId, orderNumber, session, orderBook);

        // Act
        byte[] retrievedOrderId1 = order.getOrderId();
        byte[] retrievedOrderId2 = order.getOrderId();

        // Assert - Both calls should return the same array reference
        assertSame(retrievedOrderId1, retrievedOrderId2);
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
            networkInterface = NetworkInterface.getNetworkInterfaces().nextElement();
        }
        return networkInterface;
    }
}
