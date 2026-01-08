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
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

class TerminalClientClaude_closeTest {

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
    void testCloseCanBeCalledSuccessfully() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseCanBeCalledMultipleTimes() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        assertDoesNotThrow(() -> terminalClient.close());
        assertDoesNotThrow(() -> terminalClient.close());
        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseImmediatelyAfterOpen() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseAfterAccessingGetters() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        terminalClient.getOrderEntry();
        terminalClient.getInstruments();
        terminalClient.getOrderIdGenerator();
        terminalClient.getEvents();

        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseWithNullInstruments() throws Exception {
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", null);

        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseWithEmptyCredentials() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "", "", instruments);

        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseMultipleClients() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        TerminalClient client1 = TerminalClient.open(address, "user1", "pass1", instruments);
        TerminalClient client2 = TerminalClient.open(address, "user2", "pass2", instruments);
        TerminalClient client3 = TerminalClient.open(address, "user3", "pass3", instruments);

        assertDoesNotThrow(() -> client1.close());
        assertDoesNotThrow(() -> client2.close());
        assertDoesNotThrow(() -> client3.close());

        terminalClient = null;
    }

    @Test
    void testCloseInDifferentOrder() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        TerminalClient client1 = TerminalClient.open(address, "user1", "pass1", instruments);
        TerminalClient client2 = TerminalClient.open(address, "user2", "pass2", instruments);

        assertDoesNotThrow(() -> client2.close());
        assertDoesNotThrow(() -> client1.close());

        terminalClient = null;
    }

    @Test
    void testCloseAfterGeneratingOrderIds() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        terminalClient.getOrderIdGenerator().next();
        terminalClient.getOrderIdGenerator().next();
        terminalClient.getOrderIdGenerator().next();

        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseWithLongLivedClient() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Thread.sleep(100);

        terminalClient.getOrderEntry();
        terminalClient.getInstruments();
        terminalClient.getOrderIdGenerator().next();
        terminalClient.getEvents();

        Thread.sleep(100);

        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseImplementsCloseableInterface() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        assertTrue(terminalClient instanceof java.io.Closeable);
    }

    @Test
    void testCloseInTryWithResources() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        assertDoesNotThrow(() -> {
            try (TerminalClient client = TerminalClient.open(address, "user", "pass", instruments)) {
                assertNotNull(client);
            }
        });

        terminalClient = null;
    }

    @Test
    void testCloseMultipleClientsInTryWithResources() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        assertDoesNotThrow(() -> {
            try (TerminalClient client1 = TerminalClient.open(address, "user1", "pass1", instruments);
                 TerminalClient client2 = TerminalClient.open(address, "user2", "pass2", instruments)) {
                assertNotNull(client1);
                assertNotNull(client2);
            }
        });

        terminalClient = null;
    }

    @Test
    void testCloseWithMinimalCredentials() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "u", "p", instruments);

        assertDoesNotThrow(() -> terminalClient.close());
    }

    @Test
    void testCloseAfterMultipleGetterCalls() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        for (int i = 0; i < 10; i++) {
            terminalClient.getOrderEntry();
            terminalClient.getInstruments();
            terminalClient.getOrderIdGenerator();
            terminalClient.getEvents();
        }

        assertDoesNotThrow(() -> terminalClient.close());
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
