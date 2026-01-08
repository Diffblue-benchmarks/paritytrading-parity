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
package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

class TerminalClientClaude_getEventsTest {

    private TerminalClient terminalClient;
    private ServerSocketChannel serverChannel;
    private Thread serverThread;
    private volatile boolean serverRunning;

    @AfterEach
    void tearDown() throws Exception {
        serverRunning = false;
        if (terminalClient != null) {
            terminalClient.close();
        }
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread.join(1000);
        }
        if (serverChannel != null && serverChannel.isOpen()) {
            serverChannel.close();
        }
    }

    @Test
    void testGetEventsReturnsNonNull() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events = terminalClient.getEvents();

        assertNotNull(events);
    }

    @Test
    void testGetEventsReturnsSameInstance() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events1 = terminalClient.getEvents();
        Events events2 = terminalClient.getEvents();

        assertSame(events1, events2);
    }

    @Test
    void testGetEventsMultipleCalls() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events1 = terminalClient.getEvents();
        Events events2 = terminalClient.getEvents();
        Events events3 = terminalClient.getEvents();

        assertNotNull(events1);
        assertSame(events1, events2);
        assertSame(events2, events3);
    }

    @Test
    void testGetEventsAfterClose() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);
        Events beforeClose = terminalClient.getEvents();

        terminalClient.close();

        Events afterClose = terminalClient.getEvents();

        assertNotNull(afterClose);
        assertSame(beforeClose, afterClose);
    }

    @Test
    void testGetEventsFromDifferentClients() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        TerminalClient client1 = TerminalClient.open(address, "user1", "pass1", instruments);
        TerminalClient client2 = TerminalClient.open(address, "user2", "pass2", instruments);

        Events events1 = client1.getEvents();
        Events events2 = client2.getEvents();

        assertNotNull(events1);
        assertNotNull(events2);
        assertNotSame(events1, events2);

        client1.close();
        client2.close();
        terminalClient = null;
    }

    @Test
    void testGetEventsWithNullInstruments() throws Exception {
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", null);

        Events events = terminalClient.getEvents();

        assertNotNull(events);
    }

    @Test
    void testGetEventsWithEmptyCredentials() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "", "", instruments);

        Events events = terminalClient.getEvents();

        assertNotNull(events);
    }

    @Test
    void testGetEventsImmediatelyAfterOpen() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        assertNotNull(terminalClient.getEvents());
    }

    @Test
    void testGetEventsAfterMultipleOperations() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events initial = terminalClient.getEvents();
        terminalClient.getOrderEntry();
        terminalClient.getInstruments();
        terminalClient.getOrderIdGenerator();
        Events afterOps = terminalClient.getEvents();

        assertSame(initial, afterOps);
    }

    @Test
    void testGetEventsCanAcceptVisitor() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events = terminalClient.getEvents();

        TestEventVisitor visitor = new TestEventVisitor();
        assertDoesNotThrow(() -> events.accept(visitor));
    }

    @Test
    void testGetEventsImplementsPOEClientListener() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events = terminalClient.getEvents();

        assertNotNull(events);
        assertTrue(events instanceof com.paritytrading.parity.net.poe.POEClientListener);
    }

    @Test
    void testGetEventsCanReceiveOrderAccepted() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events = terminalClient.getEvents();

        POE.OrderAccepted message = new POE.OrderAccepted();
        message.orderId = new byte[16];
        ASCII.putLeft(message.orderId, "ORDER001");
        message.side = POE.BUY;
        message.instrument = 100L;
        message.quantity = 500L;
        message.price = 10000L;
        message.orderNumber = 12345L;

        assertDoesNotThrow(() -> events.orderAccepted(message));
    }

    @Test
    void testGetEventsCanReceiveOrderRejected() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events = terminalClient.getEvents();

        POE.OrderRejected message = new POE.OrderRejected();
        message.orderId = new byte[16];
        ASCII.putLeft(message.orderId, "ORDER001");
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        assertDoesNotThrow(() -> events.orderRejected(message));
    }

    @Test
    void testGetEventsCanReceiveOrderExecuted() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events = terminalClient.getEvents();

        POE.OrderExecuted message = new POE.OrderExecuted();
        message.orderId = new byte[16];
        ASCII.putLeft(message.orderId, "ORDER001");
        message.quantity = 100L;
        message.price = 10000L;
        message.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        message.matchNumber = 54321L;

        assertDoesNotThrow(() -> events.orderExecuted(message));
    }

    @Test
    void testGetEventsCanReceiveOrderCanceled() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Events events = terminalClient.getEvents();

        POE.OrderCanceled message = new POE.OrderCanceled();
        message.orderId = new byte[16];
        ASCII.putLeft(message.orderId, "ORDER001");
        message.canceledQuantity = 50L;
        message.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        assertDoesNotThrow(() -> events.orderCanceled(message));
    }

    private Instruments createTestInstruments() {
        String configStr = "instruments {\n" +
                "  FOO {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits = 0\n" +
                "  }\n" +
                "  BAR {\n" +
                "    price-fraction-digits = 2\n" +
                "    size-fraction-digits = 0\n" +
                "  }\n" +
                "}";
        Config config = ConfigFactory.parseString(configStr);
        return Instruments.fromConfig(config, "instruments");
    }

    private int startMockServer(boolean acceptLogin) throws Exception {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 0));
        int port = ((InetSocketAddress) serverChannel.getLocalAddress()).getPort();

        serverRunning = true;
        serverThread = new Thread(() -> {
            try {
                while (serverRunning && serverChannel.isOpen()) {
                    try {
                        SocketChannel client = serverChannel.accept();
                        if (client != null) {
                            if (acceptLogin) {
                                new Thread(() -> handleLoginRequest(client)).start();
                            }
                        }
                    } catch (IOException e) {
                        if (serverRunning) {
                            // Only log if we're still supposed to be running
                        }
                    }
                }
            } catch (Exception e) {
                // Expected when server is closed
            }
        });
        serverThread.start();

        // Give the server thread time to start
        Thread.sleep(50);

        return port;
    }

    private void handleLoginRequest(SocketChannel client) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            buffer.flip();

            // Send a basic LoginAccepted response (packet type 'A')
            ByteBuffer response = ByteBuffer.allocate(128);
            response.put((byte) 'S');
            response.putShort((short) 30);
            response.put((byte) 'A');

            byte[] session = new byte[10];
            ASCII.putLeft(session, "SESSION");
            response.put(session);

            byte[] sequenceNumber = new byte[20];
            ASCII.putRight(sequenceNumber, "1");
            response.put(sequenceNumber);

            response.flip();
            client.write(response);
        } catch (IOException e) {
            // Ignore
        }
    }

    private static class TestEventVisitor implements EventVisitor {
        @Override
        public void visit(Event.OrderAccepted event) {
            // Test visitor implementation
        }

        @Override
        public void visit(Event.OrderRejected event) {
            // Test visitor implementation
        }

        @Override
        public void visit(Event.OrderExecuted event) {
            // Test visitor implementation
        }

        @Override
        public void visit(Event.OrderCanceled event) {
            // Test visitor implementation
        }
    }
}
