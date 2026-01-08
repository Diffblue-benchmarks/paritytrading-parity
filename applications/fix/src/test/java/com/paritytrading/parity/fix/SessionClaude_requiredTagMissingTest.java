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

import com.paritytrading.parity.util.Instruments;
import com.paritytrading.philadelphia.FIXConfig;
import com.paritytrading.philadelphia.FIXMessage;
import com.paritytrading.philadelphia.FIXValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class specifically focused on improving coverage for the private requiredTagMissing() method.
 *
 * <p>This test class targets the uncovered lines in the private method:
 * requiredTagMissing(FIXMessage, FIXValue, String)
 *
 * <p>Lines to cover:
 * - Line 415: if (value != null)
 * - Line 416: return false;
 * - Line 418: fix.sendReject(...)
 * - Line 420: return true;
 *
 * <p>Since requiredTagMissing() is a private method, we test it using reflection to directly invoke it.
 * This approach is justified because:
 * - The method is private with no simple public API for testing
 * - Setting up the full FIX protocol flow (including proper handshakes and message sequencing) is complex
 * - We need to verify the method's conditional logic and reject behavior in isolation
 *
 * <p>Testing approach: Use reflection to call the private requiredTagMissing() method directly
 * and verify that it correctly checks for null values and sends reject messages.
 */
class SessionClaude_requiredTagMissingTest {

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
     * Test requiredTagMissing when value is null (tag is missing).
     * This tests line 415 (value == null), line 418 (sendReject), and line 420 (return true).
     *
     * <p>This approach uses reflection because requiredTagMissing() is private and testing through
     * the full FIX message flow would require complex protocol setup.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRequiredTagMissingWithNullValue() throws Exception {
        // Set up servers
        orderEntryAcceptThread = new Thread(() -> {
            try {
                orderEntryConnection = orderEntryServer.accept();
                if (orderEntryConnection != null) {
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        orderEntryAcceptThread.start();

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

        // Create a FIX message
        FIXMessage testMessage = new FIXMessage(64, 64);
        testMessage.addField(35).setString("D"); // MsgType = NewOrderSingle
        testMessage.addField(34).setInt(1); // MsgSeqNum

        // Use reflection to access the private requiredTagMissing() method
        Method requiredTagMissingMethod = Session.class.getDeclaredMethod(
                "requiredTagMissing", FIXMessage.class, FIXValue.class, String.class);
        requiredTagMissingMethod.setAccessible(true);

        // Invoke with null value (simulating missing tag) - this covers lines 415, 418, 420
        boolean result = (boolean) requiredTagMissingMethod.invoke(
                session, testMessage, null, "TestTag(123)");

        // Verify that the method returns true when tag is missing
        assertTrue(result, "Expected requiredTagMissing to return true when value is null");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test requiredTagMissing when value is not null (tag is present).
     * This tests line 415 (value != null) and line 416 (return false).
     *
     * <p>This approach uses reflection because requiredTagMissing() is private.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRequiredTagMissingWithNonNullValue() throws Exception {
        // Set up servers
        orderEntryAcceptThread = new Thread(() -> {
            try {
                orderEntryConnection = orderEntryServer.accept();
                if (orderEntryConnection != null) {
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        orderEntryAcceptThread.start();

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

        // Create a FIX message with a value
        FIXMessage testMessage = new FIXMessage(64, 64);
        testMessage.addField(35).setString("D"); // MsgType
        testMessage.addField(34).setInt(1); // MsgSeqNum
        FIXValue testValue = testMessage.addField(11); // ClOrdID
        testValue.setString("ORDER123");

        // Use reflection to access the private requiredTagMissing() method
        Method requiredTagMissingMethod = Session.class.getDeclaredMethod(
                "requiredTagMissing", FIXMessage.class, FIXValue.class, String.class);
        requiredTagMissingMethod.setAccessible(true);

        // Invoke with non-null value (simulating present tag) - this covers lines 415, 416
        boolean result = (boolean) requiredTagMissingMethod.invoke(
                session, testMessage, testValue, "ClOrdID(11)");

        // Verify that the method returns false when tag is present
        assertFalse(result, "Expected requiredTagMissing to return false when value is not null");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test requiredTagMissing with multiple different tag names.
     * This further verifies lines 415, 418, and 420 with various tag descriptions.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRequiredTagMissingWithDifferentTags() throws Exception {
        // Set up servers
        orderEntryAcceptThread = new Thread(() -> {
            try {
                orderEntryConnection = orderEntryServer.accept();
                if (orderEntryConnection != null) {
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        orderEntryAcceptThread.start();

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

        // Create a FIX message
        FIXMessage testMessage = new FIXMessage(64, 64);
        testMessage.addField(35).setString("D");
        testMessage.addField(34).setInt(1);

        // Use reflection to access the private requiredTagMissing() method
        Method requiredTagMissingMethod = Session.class.getDeclaredMethod(
                "requiredTagMissing", FIXMessage.class, FIXValue.class, String.class);
        requiredTagMissingMethod.setAccessible(true);

        // Test with different tag names
        String[] tagNames = {
            "ClOrdID(11)",
            "Side(54)",
            "Symbol(55)",
            "OrderQty(38)",
            "Price(44)",
            "Username(553)",
            "Password(554)",
            "OrigClOrdID(41)"
        };

        for (String tagName : tagNames) {
            boolean result = (boolean) requiredTagMissingMethod.invoke(
                    session, testMessage, null, tagName);
            assertTrue(result, "Expected requiredTagMissing to return true for missing " + tagName);
        }

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test requiredTagMissing with edge case of empty string value (not null).
     * This tests that non-null values return false, even if empty.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testRequiredTagMissingWithEmptyStringValue() throws Exception {
        // Set up servers
        orderEntryAcceptThread = new Thread(() -> {
            try {
                orderEntryConnection = orderEntryServer.accept();
                if (orderEntryConnection != null) {
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                // Ignore
            }
        });
        orderEntryAcceptThread.start();

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

        // Create a FIX message with an empty string value
        FIXMessage testMessage = new FIXMessage(64, 64);
        testMessage.addField(35).setString("D");
        testMessage.addField(34).setInt(1);
        FIXValue emptyValue = testMessage.addField(11);
        emptyValue.setString(""); // Empty but not null

        // Use reflection to access the private requiredTagMissing() method
        Method requiredTagMissingMethod = Session.class.getDeclaredMethod(
                "requiredTagMissing", FIXMessage.class, FIXValue.class, String.class);
        requiredTagMissingMethod.setAccessible(true);

        // Invoke with empty string value (not null)
        boolean result = (boolean) requiredTagMissingMethod.invoke(
                session, testMessage, emptyValue, "ClOrdID(11)");

        // Verify that the method returns false even for empty values (since they're not null)
        assertFalse(result, "Expected requiredTagMissing to return false when value is empty but not null");

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
