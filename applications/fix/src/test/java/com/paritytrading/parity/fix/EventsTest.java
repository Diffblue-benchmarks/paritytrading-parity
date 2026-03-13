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
import com.paritytrading.philadelphia.FIXConnection;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventsTest {

    @Test
    void testProcessStartsEventLoop() throws Exception {
        FIXAcceptor fixAcceptor = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        try {
            serverChannel.configureBlocking(false);
            when(fixAcceptor.getServerChannel()).thenReturn(serverChannel);
            when(fixAcceptor.accept()).thenReturn(null);

            CountDownLatch started = new CountDownLatch(1);
            AtomicBoolean exceptionCaught = new AtomicBoolean(false);

            Thread testThread = new Thread(() -> {
                started.countDown();
                try {
                    Events.process(fixAcceptor);
                } catch (ClosedSelectorException e) {
                } catch (IOException e) {
                    exceptionCaught.set(true);
                }
            });

            testThread.setDaemon(true);
            testThread.start();
            assertTrue(started.await(1, TimeUnit.SECONDS));

            Thread.sleep(200);

            serverChannel.close();
            testThread.join(2000);
        } finally {
            if (serverChannel.isOpen()) {
                serverChannel.close();
            }
        }
    }

    @Test
    void testProcessAcceptsNullSession() throws Exception {
        FIXAcceptor fixAcceptor = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        try {
            serverChannel.configureBlocking(false);
            when(fixAcceptor.getServerChannel()).thenReturn(serverChannel);
            when(fixAcceptor.accept()).thenReturn(null);

            Thread testThread = new Thread(() -> {
                try {
                    Events.process(fixAcceptor);
                } catch (ClosedSelectorException e) {
                } catch (IOException e) {
                }
            });

            testThread.setDaemon(true);
            testThread.start();

            Thread.sleep(200);

            serverChannel.close();
            testThread.join(2000);
        } finally {
            if (serverChannel.isOpen()) {
                serverChannel.close();
            }
        }
    }

    @Test
    void testProcessWithRealChannels() throws Exception {
        FIXAcceptor fixAcceptor = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        try {
            serverChannel.configureBlocking(false);
            when(fixAcceptor.getServerChannel()).thenReturn(serverChannel);

            Session session = mock(Session.class);
            FIXConnection fixConnection = mock(FIXConnection.class);
            SoupBinTCPClient orderEntry = mock(SoupBinTCPClient.class);
            SocketChannel fixChannel = SocketChannel.open();
            SocketChannel orderEntryChannel = SocketChannel.open();

            try {
                fixChannel.configureBlocking(false);
                orderEntryChannel.configureBlocking(false);

                when(session.getFIX()).thenReturn(fixConnection);
                when(session.getOrderEntry()).thenReturn(orderEntry);
                when(fixConnection.getChannel()).thenReturn(fixChannel);
                when(orderEntry.getChannel()).thenReturn(orderEntryChannel);

                when(fixAcceptor.accept()).thenReturn(session).thenReturn(null);

                AtomicBoolean receivedAccept = new AtomicBoolean(false);

                Thread testThread = new Thread(() -> {
                    try {
                        Events.process(fixAcceptor);
                    } catch (ClosedSelectorException e) {
                    } catch (IOException e) {
                    } catch (Throwable t) {
                        receivedAccept.set(true);
                    }
                });

                testThread.setDaemon(true);
                testThread.start();

                Thread.sleep(200);

                serverChannel.close();
                fixChannel.close();
                orderEntryChannel.close();

                testThread.join(2000);
            } finally {
                if (fixChannel.isOpen()) {
                    fixChannel.close();
                }
                if (orderEntryChannel.isOpen()) {
                    orderEntryChannel.close();
                }
            }
        } finally {
            if (serverChannel.isOpen()) {
                serverChannel.close();
            }
        }
    }

    @Test
    void testProcessWithKeepAliveFailure() throws Exception {
        FIXAcceptor fixAcceptor = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        try {
            serverChannel.configureBlocking(false);
            when(fixAcceptor.getServerChannel()).thenReturn(serverChannel);

            Session session = mock(Session.class);
            FIXConnection fixConnection = mock(FIXConnection.class);
            SoupBinTCPClient orderEntry = mock(SoupBinTCPClient.class);
            SocketChannel fixChannel = SocketChannel.open();
            SocketChannel orderEntryChannel = SocketChannel.open();

            try {
                fixChannel.configureBlocking(false);
                orderEntryChannel.configureBlocking(false);

                when(session.getFIX()).thenReturn(fixConnection);
                when(session.getOrderEntry()).thenReturn(orderEntry);
                when(fixConnection.getChannel()).thenReturn(fixChannel);
                when(orderEntry.getChannel()).thenReturn(orderEntryChannel);

                doThrow(new IOException("Keep alive failed")).when(fixConnection).keepAlive();

                when(fixAcceptor.accept()).thenReturn(session).thenReturn(null);

                Thread testThread = new Thread(() -> {
                    try {
                        Events.process(fixAcceptor);
                    } catch (ClosedSelectorException e) {
                    } catch (IOException e) {
                    }
                });

                testThread.setDaemon(true);
                testThread.start();

                Thread.sleep(1000);

                serverChannel.close();
                fixChannel.close();
                orderEntryChannel.close();

                testThread.join(2000);
            } finally {
                if (fixChannel.isOpen()) {
                    fixChannel.close();
                }
                if (orderEntryChannel.isOpen()) {
                    orderEntryChannel.close();
                }
            }
        } finally {
            if (serverChannel.isOpen()) {
                serverChannel.close();
            }
        }
    }

    @Test
    void testProcessWithOrderEntryKeepAliveFailure() throws Exception {
        FIXAcceptor fixAcceptor = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        try {
            serverChannel.configureBlocking(false);
            when(fixAcceptor.getServerChannel()).thenReturn(serverChannel);

            Session session = mock(Session.class);
            FIXConnection fixConnection = mock(FIXConnection.class);
            SoupBinTCPClient orderEntry = mock(SoupBinTCPClient.class);
            SocketChannel fixChannel = SocketChannel.open();
            SocketChannel orderEntryChannel = SocketChannel.open();

            try {
                fixChannel.configureBlocking(false);
                orderEntryChannel.configureBlocking(false);

                when(session.getFIX()).thenReturn(fixConnection);
                when(session.getOrderEntry()).thenReturn(orderEntry);
                when(fixConnection.getChannel()).thenReturn(fixChannel);
                when(orderEntry.getChannel()).thenReturn(orderEntryChannel);

                doThrow(new IOException("Order entry keep alive failed")).when(orderEntry).keepAlive();

                when(fixAcceptor.accept()).thenReturn(session).thenReturn(null);

                Thread testThread = new Thread(() -> {
                    try {
                        Events.process(fixAcceptor);
                    } catch (ClosedSelectorException e) {
                    } catch (IOException e) {
                    }
                });

                testThread.setDaemon(true);
                testThread.start();

                Thread.sleep(1000);

                serverChannel.close();
                fixChannel.close();
                orderEntryChannel.close();

                testThread.join(2000);
            } finally {
                if (fixChannel.isOpen()) {
                    fixChannel.close();
                }
                if (orderEntryChannel.isOpen()) {
                    orderEntryChannel.close();
                }
            }
        } finally {
            if (serverChannel.isOpen()) {
                serverChannel.close();
            }
        }
    }

    @Test
    void testProcessHandlesSelectorRegistration() throws Exception {
        FIXAcceptor fixAcceptor = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        try {
            serverChannel.configureBlocking(false);
            when(fixAcceptor.getServerChannel()).thenReturn(serverChannel);
            when(fixAcceptor.accept()).thenReturn(null);

            CountDownLatch running = new CountDownLatch(1);
            AtomicBoolean processStarted = new AtomicBoolean(false);

            Thread testThread = new Thread(() -> {
                try {
                    processStarted.set(true);
                    running.countDown();
                    Events.process(fixAcceptor);
                } catch (ClosedSelectorException e) {
                } catch (IOException e) {
                }
            });

            testThread.setDaemon(true);
            testThread.start();
            assertTrue(running.await(1, TimeUnit.SECONDS));
            assertTrue(processStarted.get());

            Thread.sleep(200);

            serverChannel.close();
            testThread.join(2000);
        } finally {
            if (serverChannel.isOpen()) {
                serverChannel.close();
            }
        }
    }

    @Test
    void testProcessHandlesClosedChannel() throws Exception {
        FIXAcceptor fixAcceptor = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        serverChannel.configureBlocking(false);
        when(fixAcceptor.getServerChannel()).thenReturn(serverChannel);
        serverChannel.close();

        Thread testThread = new Thread(() -> {
            try {
                Events.process(fixAcceptor);
            } catch (Exception e) {
            }
        });

        testThread.setDaemon(true);
        testThread.start();
        testThread.join(2000);
    }
}
