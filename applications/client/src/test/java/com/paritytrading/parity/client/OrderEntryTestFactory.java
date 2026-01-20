package com.paritytrading.parity.client;

import com.diffblue.cover.annotations.InterestingTestFactory;
import com.paritytrading.parity.net.poe.POEClientListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Test factory for creating OrderEntry instances for unit testing.
 */
public class OrderEntryTestFactory {

    /**
     * Creates an OrderEntry instance for testing without network connectivity.
     * This factory creates an instance using a non-connected socket channel
     * to avoid UnresolvedAddressException and connection failures.
     *
     * @return an OrderEntry instance for testing
     * @throws IOException if an I/O error occurs
     */
    @InterestingTestFactory
    public static OrderEntry createOrderEntry() throws IOException {
        return createMockOrderEntry();
    }

    /**
     * Creates a mock OrderEntry instance for testing purposes.
     * This is used when network connections are not available.
     * The constructor is package-private, allowing direct instantiation for tests.
     *
     * @return a mock OrderEntry instance
     * @throws IOException if an I/O error occurs
     */
    static OrderEntry createMockOrderEntry() throws IOException {
        // Create a non-blocking SocketChannel that's not connected
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);

        Selector selector = Selector.open();

        POEClientListener listener = new Events();

        return new OrderEntry(selector, channel, listener);
    }

    /**
     * Creates an OrderEntry instance with a provided listener for testing.
     * The constructor is package-private, allowing direct instantiation for tests.
     *
     * @param listener the POEClientListener to use
     * @return an OrderEntry instance for testing
     * @throws IOException if an I/O error occurs
     */
    @InterestingTestFactory
    public static OrderEntry createOrderEntryWithListener(POEClientListener listener) throws IOException {
        if (listener == null) {
            listener = new Events();
        }

        // Create a non-blocking SocketChannel that's not connected
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);

        Selector selector = Selector.open();

        return new OrderEntry(selector, channel, listener);
    }

    /**
     * Creates a connected OrderEntry instance for testing send operations.
     * This factory creates an OrderEntry with a connected socket channel
     * to avoid NotYetConnectedException when testing send methods.
     * The constructor is package-private, allowing direct instantiation for tests.
     *
     * @return a connected OrderEntry instance for testing
     * @throws IOException if an I/O error occurs
     */
    @InterestingTestFactory
    public static OrderEntry createConnectedOrderEntry() throws IOException {
        try {
            // Create a pair of connected socket channels for testing
            java.nio.channels.ServerSocketChannel serverChannel = java.nio.channels.ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("127.0.0.1", 0));
            serverChannel.configureBlocking(false);
            int port = ((InetSocketAddress) serverChannel.getLocalAddress()).getPort();

            SocketChannel clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false);
            clientChannel.connect(new InetSocketAddress("127.0.0.1", port));

            // Wait for connection to be established
            while (!clientChannel.finishConnect()) {
                Thread.sleep(10);
            }

            // Accept the connection on the server side
            SocketChannel serverSideChannel = null;
            for (int i = 0; i < 100; i++) {
                serverSideChannel = serverChannel.accept();
                if (serverSideChannel != null) {
                    break;
                }
                Thread.sleep(10);
            }

            if (serverSideChannel != null) {
                serverSideChannel.configureBlocking(false);
            }

            Selector selector = Selector.open();
            clientChannel.register(selector, java.nio.channels.SelectionKey.OP_READ);

            POEClientListener listener = new Events();

            OrderEntry orderEntry = new OrderEntry(selector, clientChannel, listener);

            // Start a background thread to keep reading from server side to prevent buffer filling
            if (serverSideChannel != null) {
                SocketChannel finalServerSideChannel = serverSideChannel;
                java.nio.channels.ServerSocketChannel finalServerChannel = serverChannel;
                Thread cleanupThread = new Thread(() -> {
                    try {
                        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(8192);
                        while (finalServerSideChannel.isOpen() && finalServerSideChannel.read(buffer) >= 0) {
                            buffer.clear();
                            Thread.sleep(50);
                        }
                    } catch (Exception e) {
                        // Ignore cleanup errors
                    } finally {
                        try {
                            finalServerSideChannel.close();
                        } catch (Exception e) {
                            // Ignore
                        }
                        try {
                            finalServerChannel.close();
                        } catch (Exception e) {
                            // Ignore
                        }
                    }
                });
                cleanupThread.setDaemon(true);
                cleanupThread.start();
            }

            return orderEntry;
        } catch (Exception e) {
            throw new IOException("Failed to create connected OrderEntry for testing", e);
        }
    }
}
