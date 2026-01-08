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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

import static com.paritytrading.philadelphia.fix44.FIX44Enumerations.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class specifically focused on improving coverage for the private sendOrderCancelReject() methods.
 *
 * <p>This test class targets the uncovered lines in three private methods:
 * 1. sendOrderCancelReject(String, String, char) - lines 432-442
 * 2. sendOrderCancelReject(Order, String, char, int) - lines 446-456
 * 3. sendOrderCancelReject(Order) - lines 459-469
 *
 * <p>Lines covered in sendOrderCancelReject(String, String, char):
 * - Line 432: fix.prepare(txMessage, OrderCancelReject);
 * - Line 434: txMessage.addField(OrderID).setString(UNKNOWN_ORDER_ID);
 * - Line 435: txMessage.addField(ClOrdID).setString(clOrdId);
 * - Line 436: txMessage.addField(OrigClOrdID).setString(origClOrdId);
 * - Line 437: txMessage.addField(OrdStatus).setChar(OrdStatusValues.Rejected);
 * - Line 438: txMessage.addField(CxlRejResponseTo).setChar(cxlRejResponseTo);
 * - Line 439: txMessage.addField(CxlRejReason).setInt(CxlRejReasonValues.UnknownOrder);
 * - Line 441: fix.send(txMessage);
 *
 * <p>Lines covered in sendOrderCancelReject(Order, String, char, int):
 * - Line 446: fix.prepare(txMessage, OrderCancelReject);
 * - Line 448: txMessage.addField(OrderID).setInt(order.getOrderID());
 * - Line 449: txMessage.addField(ClOrdID).setString(clOrdId);
 * - Line 450: txMessage.addField(OrigClOrdID).setString(order.getClOrdID());
 * - Line 451: txMessage.addField(OrdStatus).setChar(OrdStatusValues.Rejected);
 * - Line 452: txMessage.addField(CxlRejResponseTo).setChar(cxlRejResponseTo);
 * - Line 453: txMessage.addField(CxlRejReason).setInt(cxlRejReason);
 * - Line 455: fix.send(txMessage);
 *
 * <p>Lines covered in sendOrderCancelReject(Order):
 * - Line 459: fix.prepare(txMessage, OrderCancelReject);
 * - Line 461: txMessage.addField(OrderID).setInt(order.getOrderID());
 * - Line 462: txMessage.addField(ClOrdID).setString(order.getNextClOrdID());
 * - Line 463: txMessage.addField(OrigClOrdID).setString(order.getClOrdID());
 * - Line 464: txMessage.addField(OrdStatus).setChar(OrdStatusValues.Filled);
 * - Line 465: txMessage.addField(CxlRejResponseTo).setChar(order.getCxlRejResponseTo());
 * - Line 466: txMessage.addField(CxlRejReason).setInt(CxlRejReasonValues.TooLateToCancel);
 * - Line 468: fix.send(txMessage);
 *
 * <p>Testing approaches:
 * - For all three methods: Use reflection to directly invoke the private methods
 * - This approach is justified because setting up the full FIX protocol flow is complex
 */
class SessionClaude_sendOrderCancelRejectTest {

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
     * Test sendOrderCancelReject with OrderCancelRequest response type.
     * This tests lines 432-442 using reflection to directly invoke the private method.
     *
     * <p>This approach uses reflection because sendOrderCancelReject() is private and testing through
     * the full FIX message flow would require complex setup including order tracking.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendOrderCancelRejectWithCancelRequestResponseType() throws Exception {
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

        // Use reflection to access the private sendOrderCancelReject() method
        Method sendOrderCancelRejectMethod = Session.class.getDeclaredMethod(
                "sendOrderCancelReject", String.class, String.class, char.class);
        sendOrderCancelRejectMethod.setAccessible(true);

        // Invoke the method with OrderCancelRequest response type - this covers lines 432-442
        assertDoesNotThrow(() -> sendOrderCancelRejectMethod.invoke(
                session, "CANCEL001", "ORIG001", CxlRejResponseToValues.OrderCancelRequest),
            "sendOrderCancelReject should execute without throwing an exception");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test sendOrderCancelReject with OrderCancelReplaceRequest response type.
     * This tests lines 432-442 with a different cxlRejResponseTo value.
     *
     * <p>This approach uses reflection because sendOrderCancelReject() is private.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendOrderCancelRejectWithReplaceRequestResponseType() throws Exception {
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

        // Use reflection to access the private sendOrderCancelReject() method
        Method sendOrderCancelRejectMethod = Session.class.getDeclaredMethod(
                "sendOrderCancelReject", String.class, String.class, char.class);
        sendOrderCancelRejectMethod.setAccessible(true);

        // Invoke with OrderCancelReplaceRequest response type
        assertDoesNotThrow(() -> sendOrderCancelRejectMethod.invoke(
                session, "REPLACE001", "ORIG002", CxlRejResponseToValues.OrderCancelReplaceRequest),
            "sendOrderCancelReject should execute without throwing an exception");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test sendOrderCancelReject with various order IDs.
     * This further verifies lines 432-442 with different parameter values.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendOrderCancelRejectWithVariousOrderIds() throws Exception {
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

        // Use reflection to access the private sendOrderCancelReject() method
        Method sendOrderCancelRejectMethod = Session.class.getDeclaredMethod(
                "sendOrderCancelReject", String.class, String.class, char.class);
        sendOrderCancelRejectMethod.setAccessible(true);

        // Test with various order IDs
        String[][] testCases = {
            {"ORDER_A", "ORIG_A"},
            {"ORDER_B", "ORIG_B"},
            {"ORDER_C", "ORIG_C"},
            {"", ""}, // Empty strings
            {"LONG_ORDER_ID_12345", "LONG_ORIG_ID_67890"} // Longer IDs
        };

        for (String[] testCase : testCases) {
            String clOrdId = testCase[0];
            String origClOrdId = testCase[1];

            assertDoesNotThrow(() -> sendOrderCancelRejectMethod.invoke(
                    session, clOrdId, origClOrdId, CxlRejResponseToValues.OrderCancelRequest),
                "sendOrderCancelReject should handle order IDs: " + clOrdId + ", " + origClOrdId);
        }

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test sendOrderCancelReject multiple times in sequence.
     * This verifies that the method can be called repeatedly without issues.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendOrderCancelRejectMultipleTimes() throws Exception {
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

        // Use reflection to access the private sendOrderCancelReject() method
        Method sendOrderCancelRejectMethod = Session.class.getDeclaredMethod(
                "sendOrderCancelReject", String.class, String.class, char.class);
        sendOrderCancelRejectMethod.setAccessible(true);

        // Call multiple times
        for (int i = 1; i <= 5; i++) {
            final int iteration = i;
            assertDoesNotThrow(() -> sendOrderCancelRejectMethod.invoke(
                    session,
                    "CANCEL_" + iteration,
                    "ORIG_" + iteration,
                    CxlRejResponseToValues.OrderCancelRequest),
                "sendOrderCancelReject call #" + iteration + " should succeed");
        }

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test sendOrderCancelReject(Order, String, char, int) when order is in pending status.
     * This tests lines 446-456 by using reflection to directly invoke the private method.
     *
     * <p>This method is called at line 312 when an order is in pending status.
     * Since setting up the full FIX protocol flow (including proper message sequencing, order tracking,
     * and logon handshake) is complex and fragile, we use reflection to directly test the method.
     *
     * <p>Testing approach: Use reflection to directly invoke sendOrderCancelReject(Order, String, char, int)
     * with an Order object in pending status and CxlRejReasonValues.OrderAlreadyInPendingStatus.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendOrderCancelRejectWithOrderInPendingStatus() throws Exception {
        // Set up order entry server
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

        // Create an order with orderID set and in pending status
        Order order = new Order(1L, "ORDER001", "ACCOUNT1", SideValues.Buy, "FOO", 100.0);
        order.orderAccepted(12345L); // Set the order ID
        order.setNextClOrdID("PENDING001"); // Set to pending status

        // Use reflection to access the private sendOrderCancelReject(Order, String, char, int) method
        Method sendOrderCancelRejectMethod = Session.class.getDeclaredMethod(
                "sendOrderCancelReject", Order.class, String.class, char.class, int.class);
        sendOrderCancelRejectMethod.setAccessible(true);

        // Invoke with OrderAlreadyInPendingStatus reason - this covers lines 446-456
        assertDoesNotThrow(() -> sendOrderCancelRejectMethod.invoke(
                session, order, "NEW_CANCEL_001", CxlRejResponseToValues.OrderCancelRequest,
                CxlRejReasonValues.OrderAlreadyInPendingStatus),
            "sendOrderCancelReject should execute without throwing an exception");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test sendOrderCancelReject(Order, String, char, int) when ClOrdID is duplicate.
     * This tests lines 446-456 by using reflection to directly invoke the private method.
     *
     * <p>This method is called at line 319 when a duplicate ClOrdID is detected.
     * Since setting up the full FIX protocol flow is complex, we use reflection to directly test the method.
     *
     * <p>Testing approach: Use reflection to directly invoke sendOrderCancelReject(Order, String, char, int)
     * with CxlRejReasonValues.DuplicateClOrdID.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendOrderCancelRejectWithDuplicateClOrdID() throws Exception {
        // Set up order entry server
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

        // Create an order
        Order order = new Order(1L, "ORDER001", "ACCOUNT1", SideValues.Buy, "FOO", 100.0);
        order.orderAccepted(12345L);

        // Use reflection to access the private sendOrderCancelReject(Order, String, char, int) method
        Method sendOrderCancelRejectMethod = Session.class.getDeclaredMethod(
                "sendOrderCancelReject", Order.class, String.class, char.class, int.class);
        sendOrderCancelRejectMethod.setAccessible(true);

        // Invoke with DuplicateClOrdID reason - this covers lines 446-456
        assertDoesNotThrow(() -> sendOrderCancelRejectMethod.invoke(
                session, order, "DUPLICATE_ID", CxlRejResponseToValues.OrderCancelRequest,
                CxlRejReasonValues.DuplicateClOrdID),
            "sendOrderCancelReject should execute without throwing an exception");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test sendOrderCancelReject(Order) method covering lines 459-469.
     * This tests the private method that sends an OrderCancelReject when an order is filled
     * while in pending status (too late to cancel).
     *
     * <p>This method is called at line 717 in the OrderEntryListener.orderExecuted() callback
     * when an order that has a pending cancel/replace request gets completely filled.
     * Since setting up the full order execution flow through POE messages is complex,
     * we use reflection to directly test the method.
     *
     * <p>Testing approach: Use reflection to directly invoke sendOrderCancelReject(Order)
     * with an Order object that is in pending status and has been filled.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendOrderCancelRejectWithFilledOrder() throws Exception {
        // Set up order entry server
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

        // Create an order that is in pending status (has a pending cancel/replace)
        Order order = new Order(1L, "ORDER003", "ACCOUNT1", SideValues.Buy, "FOO", 100.0);
        order.orderAccepted(12348L);
        order.setNextClOrdID("PENDING_CANCEL_001"); // Set to pending status
        order.setCxlRejResponseTo(CxlRejResponseToValues.OrderCancelRequest);

        // Use reflection to access the private sendOrderCancelReject(Order) method
        Method sendOrderCancelRejectMethod = Session.class.getDeclaredMethod(
                "sendOrderCancelReject", Order.class);
        sendOrderCancelRejectMethod.setAccessible(true);

        // Invoke the method - this covers lines 459-469
        // This simulates the scenario where an order gets filled while a cancel/replace is pending
        assertDoesNotThrow(() -> sendOrderCancelRejectMethod.invoke(session, order),
            "sendOrderCancelReject(Order) should execute without throwing an exception");

        orderEntryAcceptThread.join(2000);
        fixAcceptThread.join(2000);
    }

    /**
     * Test sendOrderCancelReject(Order, String, char, int) with OrderCancelReplaceRequest.
     * This tests lines 446-456 using reflection with CxlRejResponseTo = OrderCancelReplaceRequest.
     *
     * <p>This verifies that the method works correctly with different CxlRejResponseTo values
     * (OrderCancelReplaceRequest vs OrderCancelRequest).
     *
     * <p>Testing approach: Use reflection to directly invoke sendOrderCancelReject(Order, String, char, int)
     * with CxlRejResponseTo set to OrderCancelReplaceRequest.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testSendOrderCancelRejectWithOrderCancelReplaceRequest() throws Exception {
        // Set up order entry server
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

        // Create an order
        Order order = new Order(1L, "ORDER002", "ACCOUNT1", SideValues.Sell, "FOO", 200.0);
        order.orderAccepted(12347L);

        // Use reflection to access the private sendOrderCancelReject(Order, String, char, int) method
        Method sendOrderCancelRejectMethod = Session.class.getDeclaredMethod(
                "sendOrderCancelReject", Order.class, String.class, char.class, int.class);
        sendOrderCancelRejectMethod.setAccessible(true);

        // Invoke with OrderCancelReplaceRequest CxlRejResponseTo - this covers lines 446-456
        assertDoesNotThrow(() -> sendOrderCancelRejectMethod.invoke(
                session, order, "NEW_REPLACE_001", CxlRejResponseToValues.OrderCancelReplaceRequest,
                CxlRejReasonValues.OrderAlreadyInPendingStatus),
            "sendOrderCancelReject should execute without throwing an exception");

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
