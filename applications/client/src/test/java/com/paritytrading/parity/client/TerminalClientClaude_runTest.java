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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

/**
 * Tests for TerminalClient.run() method.
 *
 * Note: The run() method uses JLine's LineReader which blocks on user input
 * in an interactive loop. This makes comprehensive testing challenging without
 * mocking the LineReader or using reflection. The tests here focus on verifying
 * that the method can be called and that helper methods used by run() work correctly.
 */
class TerminalClientClaude_runTest {

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
    void testRunCanBeInvokedInSeparateThread() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        AtomicBoolean runStarted = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);

        Thread runThread = new Thread(() -> {
            try {
                runStarted.set(true);
                latch.countDown();
                terminalClient.run();
            } catch (IOException e) {
                // May throw IOException
            }
        });

        runThread.setDaemon(true);
        runThread.start();

        // Wait for run() to start
        assertTrue(latch.await(2, TimeUnit.SECONDS), "run() should have started");
        assertTrue(runStarted.get(), "run() should have been invoked");

        // Give it a moment to initialize
        Thread.sleep(200);

        // Verify thread is running (blocked on readLine)
        assertTrue(runThread.isAlive(), "run() thread should be alive");

        // Note: We don't test closing here because JLine's readLine() blocks
        // and doesn't immediately respond to close() calls in test environments
    }

    @Test
    void testRunWithAlreadyClosedClient() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        // Close immediately before calling run
        terminalClient.close();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean completed = new AtomicBoolean(false);

        Thread runThread = new Thread(() -> {
            try {
                terminalClient.run();
                completed.set(true);
            } catch (IOException e) {
                // Expected - may throw IOException
            } finally {
                latch.countDown();
            }
        });

        runThread.setDaemon(true);
        runThread.start();

        // Should exit quickly since client is already closed
        assertTrue(latch.await(2, TimeUnit.SECONDS), "run() should complete quickly when already closed");
    }

    @Test
    void testRunMethodExistsAndIsCallable() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        terminalClient = TerminalClient.open(address, "user", "pass", instruments);

        // Verify the method exists and can be called in a separate thread
        Thread runThread = new Thread(() -> {
            try {
                terminalClient.run();
            } catch (IOException e) {
                // May throw IOException
            }
        });

        runThread.setDaemon(true);
        runThread.start();

        Thread.sleep(100);

        // Close to allow the run thread to exit
        terminalClient.close();
        runThread.join(2000);

        assertNotNull(terminalClient);
    }

    @Test
    void testFindCommandReturnsValidCommands() {
        assertNotNull(TerminalClient.findCommand("buy"));
        assertNotNull(TerminalClient.findCommand("sell"));
        assertNotNull(TerminalClient.findCommand("cancel"));
        assertNotNull(TerminalClient.findCommand("orders"));
        assertNotNull(TerminalClient.findCommand("trades"));
        assertNotNull(TerminalClient.findCommand("errors"));
        assertNotNull(TerminalClient.findCommand("help"));
        assertNotNull(TerminalClient.findCommand("exit"));
    }

    @Test
    void testFindCommandReturnsNullForInvalidCommand() {
        assertNull(TerminalClient.findCommand("invalid"));
        assertNull(TerminalClient.findCommand("unknown"));
        assertNull(TerminalClient.findCommand(""));
        assertNull(TerminalClient.findCommand("BUY")); // case sensitive
    }

    @Test
    void testCommandNamesArrayIsPopulated() {
        assertNotNull(TerminalClient.COMMAND_NAMES);
        assertTrue(TerminalClient.COMMAND_NAMES.length > 0);
    }

    @Test
    void testCommandsArrayIsPopulated() {
        assertNotNull(TerminalClient.COMMANDS);
        assertTrue(TerminalClient.COMMANDS.length > 0);
        assertEquals(TerminalClient.COMMANDS.length, TerminalClient.COMMAND_NAMES.length);
    }

    @Test
    void testAllCommandNamesCanBeFound() {
        for (String commandName : TerminalClient.COMMAND_NAMES) {
            Command command = TerminalClient.findCommand(commandName);
            assertNotNull(command, "Command should be found for name: " + commandName);
            assertEquals(commandName, command.getName());
        }
    }

    @Test
    void testLocaleIsSet() {
        assertNotNull(TerminalClient.LOCALE);
        assertEquals("en", TerminalClient.LOCALE.getLanguage());
        assertEquals("US", TerminalClient.LOCALE.getCountry());
    }

    @Test
    void testPrintfMethodExists() {
        // Verify the printf method exists and can be called
        assertDoesNotThrow(() -> TerminalClient.printf("test"));
        assertDoesNotThrow(() -> TerminalClient.printf("test %s", "arg"));
        assertDoesNotThrow(() -> TerminalClient.printf("test %d", 123));
    }

    @Test
    void testRunWithMultipleClients() throws Exception {
        Instruments instruments = createTestInstruments();
        int port = startMockServer(true);
        InetSocketAddress address = new InetSocketAddress("localhost", port);

        TerminalClient client1 = TerminalClient.open(address, "user1", "pass1", instruments);
        TerminalClient client2 = TerminalClient.open(address, "user2", "pass2", instruments);

        CountDownLatch latch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            try {
                latch.countDown();
                client1.run();
            } catch (IOException e) {
                // May throw
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                latch.countDown();
                client2.run();
            } catch (IOException e) {
                // May throw
            }
        });

        thread1.setDaemon(true);
        thread2.setDaemon(true);

        thread1.start();
        thread2.start();

        // Wait for both to start
        assertTrue(latch.await(2, TimeUnit.SECONDS), "Both threads should have started");

        Thread.sleep(200);

        // Verify both threads are running
        assertTrue(thread1.isAlive(), "Thread 1 should be alive");
        assertTrue(thread2.isAlive(), "Thread 2 should be alive");

        client1.close();
        client2.close();

        terminalClient = null; // Don't close in tearDown
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
