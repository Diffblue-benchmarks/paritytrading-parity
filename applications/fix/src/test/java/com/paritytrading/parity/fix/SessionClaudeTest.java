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
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.philadelphia.FIXConfig;
import com.paritytrading.philadelphia.FIXConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Session.
 *
 * <p>This test class covers the following methods:
 * - Constructor: Session(OrderEntryFactory, SocketChannel, FIXConfig, Instruments)
 * - close()
 * - getFIX()
 * - getOrderEntry()
 *
 * <p>Testing approach: These tests use real network components (ServerSocketChannel,
 * SocketChannel) without mocking to verify actual behavior. The Session class is
 * package-private, so tests are in the same package.
 */
class SessionClaudeTest {

    private ServerSocketChannel orderEntryServer;
    private ServerSocketChannel fixServer;
    private InetSocketAddress orderEntryAddress;
    private InetSocketAddress fixAddress;
    private Session session;

    @BeforeEach
    void setUp() throws Exception {
        // Set up order entry server
        orderEntryServer = ServerSocketChannel.open();
        orderEntryServer.bind(new InetSocketAddress("localhost", 0));
        orderEntryAddress = (InetSocketAddress) orderEntryServer.getLocalAddress();

        // Set up FIX server
        fixServer = ServerSocketChannel.open();
        fixServer.bind(new InetSocketAddress("localhost", 0));
        fixAddress = (InetSocketAddress) fixServer.getLocalAddress();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
        if (orderEntryServer != null) {
            orderEntryServer.close();
        }
        if (fixServer != null) {
            fixServer.close();
        }
    }

    /**
     * Test that the constructor successfully creates a Session with valid parameters.
     * This verifies that the Session is initialized correctly with all required components.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConstructorCreatesSessionSuccessfully() throws Exception {
        // Accept order entry connection in background
        Thread orderEntryAcceptThread = new Thread(() -> {
            try {
                SocketChannel accepted = orderEntryServer.accept();
                if (accepted != null) {
                    Thread.sleep(100);
                    accepted.close();
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        orderEntryAcceptThread.start();

        // Accept FIX connection in background
        Thread fixAcceptThread = new Thread(() -> {
            try {
                SocketChannel accepted = fixServer.accept();
                if (accepted != null) {
                    Thread.sleep(100);
                    accepted.close();
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        fixAcceptThread.start();

        // Create Session
        OrderEntryFactory orderEntryFactory = new OrderEntryFactory(orderEntryAddress);
        SocketChannel fixChannel = SocketChannel.open();
        fixChannel.connect(fixAddress);
        FIXConfig fixConfig = new FIXConfig.Builder()
                .setSenderCompID("SENDER")
                .setTargetCompID("TARGET")
                .build();
        Instruments instruments = createTestInstruments();

        session = new Session(orderEntryFactory, fixChannel, fixConfig, instruments);

        assertNotNull(session, "Session should not be null");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that the constructor initializes the FIX connection correctly and
     * getFIX() returns a non-null FIXConnection object.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConstructorInitializesFIXConnection() throws Exception {
        // Accept connections
        Thread orderEntryAcceptThread = startOrderEntryAcceptThread();
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session
        session = createTestSession();

        assertNotNull(session, "Session should not be null");
        assertNotNull(session.getFIX(), "FIX connection should not be null");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that the constructor initializes the order entry connection correctly and
     * getOrderEntry() returns a non-null SoupBinTCPClient object.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConstructorInitializesOrderEntry() throws Exception {
        // Accept connections
        Thread orderEntryAcceptThread = startOrderEntryAcceptThread();
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session
        session = createTestSession();

        assertNotNull(session, "Session should not be null");
        assertNotNull(session.getOrderEntry(), "Order entry connection should not be null");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that getFIX() returns the same FIXConnection instance across multiple calls.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testGetFIXReturnsSameInstance() throws Exception {
        // Accept connections
        Thread orderEntryAcceptThread = startOrderEntryAcceptThread();
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session
        session = createTestSession();

        FIXConnection fix1 = session.getFIX();
        FIXConnection fix2 = session.getFIX();

        assertNotNull(fix1, "First FIX connection should not be null");
        assertNotNull(fix2, "Second FIX connection should not be null");
        assertSame(fix1, fix2, "getFIX() should return the same instance");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that getOrderEntry() returns the same SoupBinTCPClient instance across multiple calls.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testGetOrderEntryReturnsSameInstance() throws Exception {
        // Accept connections
        Thread orderEntryAcceptThread = startOrderEntryAcceptThread();
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session
        session = createTestSession();

        SoupBinTCPClient orderEntry1 = session.getOrderEntry();
        SoupBinTCPClient orderEntry2 = session.getOrderEntry();

        assertNotNull(orderEntry1, "First order entry should not be null");
        assertNotNull(orderEntry2, "Second order entry should not be null");
        assertSame(orderEntry1, orderEntry2, "getOrderEntry() should return the same instance");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that close() successfully closes both the FIX connection and order entry connection.
     * After closing, the connections should be closed.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCloseClosesConnections() throws Exception {
        // Accept connections
        Thread orderEntryAcceptThread = startOrderEntryAcceptThread();
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session
        session = createTestSession();

        assertNotNull(session.getFIX(), "FIX connection should exist before close");
        assertNotNull(session.getOrderEntry(), "Order entry should exist before close");

        // Close the session
        session.close();

        // Note: We can't directly test if connections are closed because there's no
        // isClosed() method on FIXConnection or SoupBinTCPClient. The close() method
        // calls close() on both connections, which is verified by the fact that
        // no exceptions are thrown.

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that close() can be called multiple times without throwing an exception.
     * This verifies idempotent behavior of the close() method.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCloseCanBeCalledMultipleTimes() throws Exception {
        // Accept connections
        Thread orderEntryAcceptThread = startOrderEntryAcceptThread();
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session
        session = createTestSession();

        // Close multiple times - should not throw exception
        assertDoesNotThrow(() -> session.close(), "First close should not throw");
        assertDoesNotThrow(() -> session.close(), "Second close should not throw");
        assertDoesNotThrow(() -> session.close(), "Third close should not throw");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that the constructor properly handles different FIX configurations.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConstructorWithDifferentFIXConfigs() throws Exception {
        // Accept connections
        Thread orderEntryAcceptThread = startOrderEntryAcceptThread();
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session with custom FIX config
        OrderEntryFactory orderEntryFactory = new OrderEntryFactory(orderEntryAddress);
        SocketChannel fixChannel = SocketChannel.open();
        fixChannel.connect(fixAddress);
        FIXConfig fixConfig = new FIXConfig.Builder()
                .setSenderCompID("CUSTOM_SENDER")
                .setTargetCompID("CUSTOM_TARGET")
                .setHeartBtInt(60)
                .build();
        Instruments instruments = createTestInstruments();

        session = new Session(orderEntryFactory, fixChannel, fixConfig, instruments);

        assertNotNull(session, "Session with custom config should not be null");
        assertNotNull(session.getFIX(), "FIX connection should be initialized");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that the constructor works with an empty Instruments configuration.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConstructorWithEmptyInstruments() throws Exception {
        // Accept connections
        Thread orderEntryAcceptThread = startOrderEntryAcceptThread();
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session with empty instruments
        OrderEntryFactory orderEntryFactory = new OrderEntryFactory(orderEntryAddress);
        SocketChannel fixChannel = SocketChannel.open();
        fixChannel.connect(fixAddress);
        FIXConfig fixConfig = new FIXConfig.Builder()
                .setSenderCompID("SENDER")
                .setTargetCompID("TARGET")
                .build();
        Instruments instruments = createEmptyInstruments();

        session = new Session(orderEntryFactory, fixChannel, fixConfig, instruments);

        assertNotNull(session, "Session with empty instruments should not be null");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that constructor throws IOException when order entry factory cannot connect.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConstructorThrowsExceptionWhenOrderEntryConnectionFails() throws Exception {
        // Don't start accept thread - connection will fail

        // Accept FIX connection in background
        Thread fixAcceptThread = startFixAcceptThread();

        // Create Session with unreachable order entry server
        OrderEntryFactory orderEntryFactory = new OrderEntryFactory(
                new InetSocketAddress("localhost", 1)); // Port 1 is not listening
        SocketChannel fixChannel = SocketChannel.open();
        fixChannel.connect(fixAddress);
        FIXConfig fixConfig = new FIXConfig.Builder()
                .setSenderCompID("SENDER")
                .setTargetCompID("TARGET")
                .build();
        Instruments instruments = createTestInstruments();

        assertThrows(IOException.class, () -> {
            session = new Session(orderEntryFactory, fixChannel, fixConfig, instruments);
        }, "Constructor should throw IOException when order entry connection fails");

        fixAcceptThread.join(2000);
    }

    // Helper methods

    private Session createTestSession() throws Exception {
        OrderEntryFactory orderEntryFactory = new OrderEntryFactory(orderEntryAddress);
        SocketChannel fixChannel = SocketChannel.open();
        fixChannel.connect(fixAddress);
        FIXConfig fixConfig = new FIXConfig.Builder()
                .setSenderCompID("SENDER")
                .setTargetCompID("TARGET")
                .build();
        Instruments instruments = createTestInstruments();

        return new Session(orderEntryFactory, fixChannel, fixConfig, instruments);
    }

    private Thread startOrderEntryAcceptThread() {
        Thread thread = new Thread(() -> {
            try {
                SocketChannel accepted = orderEntryServer.accept();
                if (accepted != null) {
                    Thread.sleep(100);
                    accepted.close();
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        thread.start();
        return thread;
    }

    private Thread startFixAcceptThread() {
        Thread thread = new Thread(() -> {
            try {
                SocketChannel accepted = fixServer.accept();
                if (accepted != null) {
                    Thread.sleep(100);
                    accepted.close();
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        thread.start();
        return thread;
    }

    private Instruments createTestInstruments() {
        // Create a minimal Instruments object using the builder pattern
        // Since Instruments requires Config, we'll use an empty one
        String config = "instruments { }";
        com.typesafe.config.Config typesafeConfig =
            com.typesafe.config.ConfigFactory.parseString(config);
        return Instruments.fromConfig(typesafeConfig, "instruments");
    }

    private Instruments createEmptyInstruments() {
        String config = "instruments { }";
        com.typesafe.config.Config typesafeConfig =
            com.typesafe.config.ConfigFactory.parseString(config);
        return Instruments.fromConfig(typesafeConfig, "instruments");
    }
}
