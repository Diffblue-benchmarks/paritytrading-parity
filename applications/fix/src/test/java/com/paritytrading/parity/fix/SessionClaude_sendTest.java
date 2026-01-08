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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.philadelphia.FIXConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class specifically focused on improving coverage for the private send() method.
 *
 * <p>This test class targets the uncovered lines in the private method:
 * send(POE.InboundMessage)
 *
 * <p>Lines to cover:
 * - Line 98: txBuffer.clear();
 * - Line 99: message.put(txBuffer);
 * - Line 100: txBuffer.flip();
 * - Line 102: orderEntry.send(txBuffer);
 *
 * <p>Since send() is a private method, we test it using reflection to directly invoke it.
 * This approach is justified because:
 * - The send() method is private with no public API for testing
 * - Setting up the full FIX protocol flow (logon, order messages, etc.) is complex
 * - We need to verify the method's buffer manipulation behavior in isolation
 *
 * <p>Testing approach: Use reflection to call the private send() method directly
 * and verify that it correctly manipulates the buffer and sends to order entry.
 */
class SessionClaude_sendTest {

    private ServerSocketChannel orderEntryServer;
    private ServerSocketChannel fixServer;
    private InetSocketAddress orderEntryAddress;
    private InetSocketAddress fixAddress;
    private Session session;
    private SocketChannel orderEntryConnection;
    private Thread orderEntryAcceptThread;
    private Thread fixAcceptThread;

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
        if (orderEntryConnection != null) {
            try {
                orderEntryConnection.close();
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
        if (orderEntryAcceptThread != null) {
            orderEntryAcceptThread.interrupt();
        }
        if (fixAcceptThread != null) {
            fixAcceptThread.interrupt();
        }
    }

    /**
     * Test that the send() method correctly manipulates the buffer and sends to order entry.
     * This tests lines 98-102 using reflection to directly invoke the private method.
     *
     * <p>This approach uses reflection because the send() method is private and testing through
     * the full FIX protocol flow would require complex setup including FIX logon sequences.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendWithEnterOrderMessage() throws Exception {
        AtomicBoolean messageReceived = new AtomicBoolean(false);

        // Set up order entry server that will receive the POE message
        orderEntryAcceptThread = new Thread(() -> {
            try {
                orderEntryConnection = orderEntryServer.accept();
                if (orderEntryConnection != null) {
                    orderEntryConnection.configureBlocking(true);

                    // Read the message sent by send()
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = orderEntryConnection.read(buffer);
                    if (bytesRead > 0) {
                        messageReceived.set(true);
                    }

                    Thread.sleep(1000); // Keep connection alive
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        orderEntryAcceptThread.start();

        // Set up FIX server
        fixAcceptThread = new Thread(() -> {
            try {
                SocketChannel fixConnection = fixServer.accept();
                if (fixConnection != null) {
                    Thread.sleep(2000);
                    fixConnection.close();
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

        // Give time for connections to establish
        Thread.sleep(300);

        // Create a POE EnterOrder message
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        ASCII.putLongLeft(enterOrder.orderId, 12345L);
        enterOrder.side = POE.BUY;
        enterOrder.instrument = ASCII.packLong("FOO");
        enterOrder.quantity = 100;
        enterOrder.price = 15000;

        // Use reflection to access the private send() method
        Method sendMethod = Session.class.getDeclaredMethod("send", POE.InboundMessage.class);
        sendMethod.setAccessible(true);

        // Invoke the send method - this covers lines 98-102
        sendMethod.invoke(session, enterOrder);

        // Give time for the message to be sent and received
        Thread.sleep(300);

        // Verify that a message was received
        assertTrue(messageReceived.get(), "Expected message to be sent to order entry");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that the send() method works with CancelOrder message.
     * This further tests lines 98-102 with a different message type.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendWithCancelOrderMessage() throws Exception {
        AtomicBoolean messageReceived = new AtomicBoolean(false);

        // Set up order entry server
        orderEntryAcceptThread = new Thread(() -> {
            try {
                orderEntryConnection = orderEntryServer.accept();
                if (orderEntryConnection != null) {
                    orderEntryConnection.configureBlocking(true);

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = orderEntryConnection.read(buffer);
                    if (bytesRead > 0) {
                        messageReceived.set(true);
                    }

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        orderEntryAcceptThread.start();

        // Set up FIX server
        fixAcceptThread = new Thread(() -> {
            try {
                SocketChannel fixConnection = fixServer.accept();
                if (fixConnection != null) {
                    Thread.sleep(2000);
                    fixConnection.close();
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
        Thread.sleep(300);

        // Create a POE CancelOrder message
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        ASCII.putLongLeft(cancelOrder.orderId, 67890L);
        cancelOrder.quantity = 50;

        // Use reflection to access the private send() method
        Method sendMethod = Session.class.getDeclaredMethod("send", POE.InboundMessage.class);
        sendMethod.setAccessible(true);

        // Invoke the send method
        sendMethod.invoke(session, cancelOrder);

        Thread.sleep(300);

        // Verify that a message was received
        assertTrue(messageReceived.get(), "Expected cancel order message to be sent");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test that multiple send() calls work correctly.
     * This verifies that the buffer clearing (line 98) works properly between calls.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testMultipleSendCalls() throws Exception {
        AtomicBoolean firstReceived = new AtomicBoolean(false);
        AtomicBoolean secondReceived = new AtomicBoolean(false);

        // Set up order entry server
        orderEntryAcceptThread = new Thread(() -> {
            try {
                orderEntryConnection = orderEntryServer.accept();
                if (orderEntryConnection != null) {
                    orderEntryConnection.configureBlocking(true);

                    // First message
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = orderEntryConnection.read(buffer);
                    if (bytesRead > 0) {
                        firstReceived.set(true);
                    }

                    // Second message
                    buffer.clear();
                    Thread.sleep(200);
                    bytesRead = orderEntryConnection.read(buffer);
                    if (bytesRead > 0) {
                        secondReceived.set(true);
                    }

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        orderEntryAcceptThread.start();

        // Set up FIX server
        fixAcceptThread = new Thread(() -> {
            try {
                SocketChannel fixConnection = fixServer.accept();
                if (fixConnection != null) {
                    Thread.sleep(2000);
                    fixConnection.close();
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
        Thread.sleep(300);

        // Use reflection to access the private send() method
        Method sendMethod = Session.class.getDeclaredMethod("send", POE.InboundMessage.class);
        sendMethod.setAccessible(true);

        // Send first message
        POE.EnterOrder enterOrder = new POE.EnterOrder();
        ASCII.putLongLeft(enterOrder.orderId, 11111L);
        enterOrder.side = POE.BUY;
        enterOrder.instrument = ASCII.packLong("FOO");
        enterOrder.quantity = 100;
        enterOrder.price = 10000;

        sendMethod.invoke(session, enterOrder);
        Thread.sleep(200);

        // Send second message
        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        ASCII.putLongLeft(cancelOrder.orderId, 22222L);
        cancelOrder.quantity = 25;

        sendMethod.invoke(session, cancelOrder);
        Thread.sleep(300);

        // Verify both messages were received
        assertTrue(firstReceived.get(), "Expected first message to be sent");
        assertTrue(secondReceived.get(), "Expected second message to be sent");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    // Helper methods

    private Instruments createTestInstruments() {
        String config = "instruments {\n" +
                "  price-integer-digits = 4\n" +
                "  size-integer-digits = 8\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits = 0\n" +
                "  }\n" +
                "}";
        com.typesafe.config.Config typesafeConfig =
            com.typesafe.config.ConfigFactory.parseString(config);
        return Instruments.fromConfig(typesafeConfig, "instruments");
    }
}
