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
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.parity.util.OrderIDGenerator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

class TerminalClientClaude_getOrderIdGeneratorTest {

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
    void testGetOrderIdGeneratorReturnsNonNull() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator generator = terminalClient.getOrderIdGenerator();

        assertNotNull(generator);
    }

    @Test
    void testGetOrderIdGeneratorReturnsSameInstance() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator generator1 = terminalClient.getOrderIdGenerator();
        OrderIDGenerator generator2 = terminalClient.getOrderIdGenerator();

        assertSame(generator1, generator2);
    }

    @Test
    void testGetOrderIdGeneratorMultipleCalls() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator generator1 = terminalClient.getOrderIdGenerator();
        OrderIDGenerator generator2 = terminalClient.getOrderIdGenerator();
        OrderIDGenerator generator3 = terminalClient.getOrderIdGenerator();

        assertNotNull(generator1);
        assertSame(generator1, generator2);
        assertSame(generator2, generator3);
    }

    @Test
    void testGetOrderIdGeneratorAfterClose() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);
        OrderIDGenerator beforeClose = terminalClient.getOrderIdGenerator();

        terminalClient.close();

        OrderIDGenerator afterClose = terminalClient.getOrderIdGenerator();

        assertNotNull(afterClose);
        assertSame(beforeClose, afterClose);
    }

    @Test
    void testGetOrderIdGeneratorCanGenerateIds() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator generator = terminalClient.getOrderIdGenerator();

        String orderId = generator.next();

        assertNotNull(orderId);
        assertFalse(orderId.isEmpty());
    }

    @Test
    void testGetOrderIdGeneratorGeneratesSequentialIds() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator generator = terminalClient.getOrderIdGenerator();

        String orderId1 = generator.next();
        String orderId2 = generator.next();
        String orderId3 = generator.next();

        assertNotNull(orderId1);
        assertNotNull(orderId2);
        assertNotNull(orderId3);
        assertNotEquals(orderId1, orderId2);
        assertNotEquals(orderId2, orderId3);
    }

    @Test
    void testGetOrderIdGeneratorFromDifferentClients() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        TerminalClient client1 = TerminalClient.open(address, "user1", "pass1", instruments);
        TerminalClient client2 = TerminalClient.open(address, "user2", "pass2", instruments);

        OrderIDGenerator generator1 = client1.getOrderIdGenerator();
        OrderIDGenerator generator2 = client2.getOrderIdGenerator();

        assertNotNull(generator1);
        assertNotNull(generator2);
        assertNotSame(generator1, generator2);

        client1.close();
        client2.close();
        terminalClient = null;
    }

    @Test
    void testGetOrderIdGeneratorWithNullInstruments() throws Exception {
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", null);

        OrderIDGenerator generator = terminalClient.getOrderIdGenerator();

        assertNotNull(generator);
    }

    @Test
    void testGetOrderIdGeneratorWithEmptyCredentials() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "", "", instruments);

        OrderIDGenerator generator = terminalClient.getOrderIdGenerator();

        assertNotNull(generator);
    }

    @Test
    void testGetOrderIdGeneratorImmediatelyAfterOpen() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        assertNotNull(terminalClient.getOrderIdGenerator());
    }

    @Test
    void testGetOrderIdGeneratorAfterMultipleOperations() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator initial = terminalClient.getOrderIdGenerator();
        terminalClient.getOrderEntry();
        terminalClient.getInstruments();
        terminalClient.getEvents();
        OrderIDGenerator afterOps = terminalClient.getOrderIdGenerator();

        assertSame(initial, afterOps);
    }

    @Test
    void testGetOrderIdGeneratorStatePreserved() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator generator = terminalClient.getOrderIdGenerator();
        String firstId = generator.next();

        OrderIDGenerator sameGenerator = terminalClient.getOrderIdGenerator();
        String secondId = sameGenerator.next();

        assertSame(generator, sameGenerator);
        assertNotEquals(firstId, secondId);
    }

    @Test
    void testGetOrderIdGeneratorGeneratesMultipleUniqueIds() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator generator = terminalClient.getOrderIdGenerator();

        String id1 = generator.next();
        String id2 = generator.next();
        String id3 = generator.next();
        String id4 = generator.next();
        String id5 = generator.next();

        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
        assertNotEquals(id3, id4);
        assertNotEquals(id4, id5);
    }

    @Test
    void testGetOrderIdGeneratorGeneratesValidFormat() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        OrderIDGenerator generator = terminalClient.getOrderIdGenerator();
        String orderId = generator.next();

        assertNotNull(orderId);
        assertTrue(orderId.contains("-"));
        assertTrue(orderId.length() > 0);
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
}
