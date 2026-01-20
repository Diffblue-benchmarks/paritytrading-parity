package com.paritytrading.parity.system;

import com.diffblue.cover.annotations.InterestingTestFactory;
import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.net.poe.POE;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Test utility class providing factory methods for creating test instances.
 * These methods create objects suitable for testing without requiring real network connections.
 */
public class TestFactories {

    /**
     * Creates a MarketData instance suitable for testing.
     * Uses null network channels which is acceptable for unit tests.
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static MarketData createMarketData() {
        MoldUDP64Server transport = new MoldUDP64Server(null, "TestSession");
        MoldUDP64RequestServer requestTransport = new MoldUDP64RequestServer(null);
        return new MarketData(transport, requestTransport);
    }

    /**
     * Creates a MarketReporting instance suitable for testing.
     * Uses null network channels which is acceptable for unit tests.
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static MarketReporting createMarketReporting() {
        MoldUDP64Server transport = new MoldUDP64Server(null, "TestSession");
        MoldUDP64RequestServer requestTransport = new MoldUDP64RequestServer(null);
        return new MarketReporting(transport, requestTransport);
    }

    /**
     * Creates an OrderBooks instance suitable for testing.
     * Uses test instances of MarketData and MarketReporting with no real network connections.
     */
    public static OrderBooks createOrderBooks() {
        List<String> instruments = new ArrayList<>();
        instruments.add("TEST");
        MarketData marketData = createMarketData();
        MarketReporting marketReporting = createMarketReporting();
        return new OrderBooks(instruments, marketData, marketReporting);
    }

    /**
     * Creates an Order instance suitable for testing.
     */
    public static Order createOrder() {
        byte[] orderId = new byte[]{1, 2, 3, 4};
        long orderNumber = 1L;
        Session session = createSession();
        OrderBook book = null;  // Can be null for basic testing
        return new Order(orderId, orderNumber, session, book);
    }

    /**
     * Factory method for creating an Order with a properly sized orderId (16 bytes).
     * This avoids ArrayIndexOutOfBoundsException when copying orderId to POE message buffers.
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static Order createOrderWithProperOrderId() {
        // POE.ORDER_ID_LENGTH is 16 bytes
        byte[] orderId = new byte[16];
        for (int i = 0; i < orderId.length; i++) {
            orderId[i] = (byte) (i + 1);
        }
        long orderNumber = 1L;
        Session session = createSessionWithTransport();
        OrderBook book = null;  // Can be null for basic testing
        return new Order(orderId, orderNumber, session, book);
    }

    /**
     * Creates a Session instance suitable for testing.
     * Uses null channel and OrderBooks which is acceptable for unit tests.
     */
    public static Session createSession() {
        SocketChannel channel = null;
        OrderBooks books = null;  // Can be null for basic testing to avoid circular dependency
        return new Session(channel, books);
    }

    /**
     * Creates a Session instance with a provided OrderBooks.
     */
    public static Session createSessionWithBooks(OrderBooks books) {
        SocketChannel channel = null;
        return new Session(channel, books);
    }

    /**
     * Creates an OrderEntry instance suitable for testing.
     */
    public static OrderEntry createOrderEntry() {
        ServerSocketChannel serverChannel = null;
        OrderBooks books = createOrderBooks();
        return new OrderEntry(serverChannel, books);
    }

    /**
     * Factory method for creating a Session instance with properly initialized transport.
     * This avoids NullPointerException in Session.close() and Session.cancelOrder().
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static Session createSessionWithTransport() {
        try {
            // Create a pair of connected socket channels for testing
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0));

            SocketChannel clientChannel = SocketChannel.open();
            clientChannel.connect(serverChannel.getLocalAddress());

            SocketChannel acceptedChannel = serverChannel.accept();

            // Close the server channel as we don't need it anymore
            serverChannel.close();

            // Use the accepted channel for the session
            OrderBooks books = createOrderBooks();
            Session session = new Session(acceptedChannel, books);

            // Clean up the client channel
            clientChannel.close();

            return session;
        } catch (IOException e) {
            // Fallback to basic session if socket creation fails
            return createSession();
        }
    }

    /**
     * Factory method for creating an OrderEntry with a properly initialized ServerSocketChannel.
     * This avoids NullPointerException in OrderEntry.accept().
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static OrderEntry createOrderEntryWithChannel() {
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0));
            serverChannel.configureBlocking(false);
            OrderBooks books = createOrderBooks();
            return new OrderEntry(serverChannel, books);
        } catch (IOException e) {
            // Fallback to basic order entry if socket creation fails
            return createOrderEntry();
        }
    }

    /**
     * Factory method for creating a valid InetSocketAddress for testing OrderEntry.open().
     * Uses loopback address to avoid UnresolvedAddressException.
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static InetSocketAddress createResolvedInetSocketAddress() {
        return new InetSocketAddress(InetAddress.getLoopbackAddress(), 0);
    }

    /**
     * Factory method for creating a POE.EnterOrder instance for testing.
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static POE.EnterOrder createEnterOrder() {
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side = POE.BUY;
        enterOrder.instrument = 1L;
        enterOrder.quantity = 100L;
        enterOrder.price = 1000L;
        // orderId is already initialized by the constructor
        return enterOrder;
    }

    /**
     * Factory method for creating a POE.CancelOrder instance for testing.
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static POE.CancelOrder createCancelOrder() {
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.quantity = 50L;
        // orderId is already initialized by the constructor
        return cancelOrder;
    }

    /**
     * Factory method for creating a SoupBinTCP.LoginRequest instance for testing.
     * Annotated with @InterestingTestFactory for Cover test generation.
     */
    @InterestingTestFactory
    public static SoupBinTCP.LoginRequest createLoginRequest() {
        SoupBinTCP.LoginRequest loginRequest = new SoupBinTCP.LoginRequest();
        // Initialize with valid test data
        String username = "TESTUSER";
        byte[] usernameBytes = username.getBytes();
        System.arraycopy(usernameBytes, 0, loginRequest.username, 0,
                         Math.min(usernameBytes.length, loginRequest.username.length));
        String password = "TESTPASS";
        byte[] passwordBytes = password.getBytes();
        System.arraycopy(passwordBytes, 0, loginRequest.password, 0,
                         Math.min(passwordBytes.length, loginRequest.password.length));
        String session = "TEST";
        byte[] sessionBytes = session.getBytes();
        System.arraycopy(sessionBytes, 0, loginRequest.requestedSession, 0,
                         Math.min(sessionBytes.length, loginRequest.requestedSession.length));
        // requestedSequenceNumber is a byte array, use ASCII utility
        com.paritytrading.foundation.ASCII.putLongRight(loginRequest.requestedSequenceNumber, 0);
        return loginRequest;
    }
}
