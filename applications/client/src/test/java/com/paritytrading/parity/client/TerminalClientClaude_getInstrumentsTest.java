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
import com.paritytrading.parity.util.Instrument;
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

class TerminalClientClaude_getInstrumentsTest {

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
    void testGetInstrumentsReturnsNonNull() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Instruments result = terminalClient.getInstruments();

        assertNotNull(result);
    }

    @Test
    void testGetInstrumentsReturnsSameInstance() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Instruments result1 = terminalClient.getInstruments();
        Instruments result2 = terminalClient.getInstruments();

        assertSame(result1, result2);
    }

    @Test
    void testGetInstrumentsReturnsPassedInstruments() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Instruments result = terminalClient.getInstruments();

        assertSame(instruments, result);
    }

    @Test
    void testGetInstrumentsMultipleCalls() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Instruments result1 = terminalClient.getInstruments();
        Instruments result2 = terminalClient.getInstruments();
        Instruments result3 = terminalClient.getInstruments();

        assertNotNull(result1);
        assertSame(result1, result2);
        assertSame(result2, result3);
        assertSame(instruments, result1);
    }

    @Test
    void testGetInstrumentsAfterClose() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);
        Instruments beforeClose = terminalClient.getInstruments();

        terminalClient.close();

        Instruments afterClose = terminalClient.getInstruments();

        assertNotNull(afterClose);
        assertSame(beforeClose, afterClose);
    }

    @Test
    void testGetInstrumentsWithNullInstruments() throws Exception {
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", null);

        Instruments result = terminalClient.getInstruments();

        assertNull(result);
    }

    @Test
    void testGetInstrumentsFromDifferentClients() throws Exception {
        Instruments instruments1 = createTestInstruments();
        Instruments instruments2 = createAlternateInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        TerminalClient client1 = TerminalClient.open(address, "user1", "pass1", instruments1);
        TerminalClient client2 = TerminalClient.open(address, "user2", "pass2", instruments2);

        Instruments result1 = client1.getInstruments();
        Instruments result2 = client2.getInstruments();

        assertNotNull(result1);
        assertNotNull(result2);
        assertSame(instruments1, result1);
        assertSame(instruments2, result2);
        assertNotSame(result1, result2);

        client1.close();
        client2.close();
        terminalClient = null;
    }

    @Test
    void testGetInstrumentsImmediatelyAfterOpen() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        assertNotNull(terminalClient.getInstruments());
        assertSame(instruments, terminalClient.getInstruments());
    }

    @Test
    void testGetInstrumentsAfterMultipleOperations() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Instruments initial = terminalClient.getInstruments();
        terminalClient.getOrderEntry();
        terminalClient.getEvents();
        terminalClient.getOrderIdGenerator();
        Instruments afterOps = terminalClient.getInstruments();

        assertSame(initial, afterOps);
        assertSame(instruments, afterOps);
    }

    @Test
    void testGetInstrumentsWithEmptyCredentials() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "", "", instruments);

        Instruments result = terminalClient.getInstruments();

        assertNotNull(result);
        assertSame(instruments, result);
    }

    @Test
    void testGetInstrumentsCanAccessInstrumentData() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Instruments result = terminalClient.getInstruments();

        assertNotNull(result);
        Instrument foo = result.get("FOO");
        assertNotNull(foo);
        assertEquals("FOO", foo.asString());
    }

    @Test
    void testGetInstrumentsPreservesMultipleInstruments() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Instruments result = terminalClient.getInstruments();

        assertNotNull(result);
        assertNotNull(result.get("FOO"));
        assertNotNull(result.get("BAR"));
    }

    @Test
    void testGetInstrumentsWithSingleInstrument() throws Exception {
        Instruments instruments = createSingleInstrument();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        Instruments result = terminalClient.getInstruments();

        assertNotNull(result);
        assertSame(instruments, result);
        assertNotNull(result.get("TEST"));
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

    private Instruments createAlternateInstruments() {
        String configStr = "instruments {\n" +
                "  XYZ {\n" +
                "    price-fraction-digits = 3\n" +
                "    size-fraction-digits = 1\n" +
                "  }\n" +
                "  ABC {\n" +
                "    price-fraction-digits = 1\n" +
                "    size-fraction-digits = 2\n" +
                "  }\n" +
                "}";
        Config config = ConfigFactory.parseString(configStr);
        return Instruments.fromConfig(config, "instruments");
    }

    private Instruments createSingleInstrument() {
        String configStr = "instruments {\n" +
                "  TEST {\n" +
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
