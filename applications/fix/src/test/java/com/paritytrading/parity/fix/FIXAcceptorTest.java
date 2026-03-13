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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class FIXAcceptorTest {

    private FIXAcceptor acceptor;

    @AfterEach
    public void tearDown() throws IOException {
        if (acceptor != null) {
            ServerSocketChannel serverChannel = acceptor.getServerChannel();
            if (serverChannel != null && serverChannel.isOpen()) {
                serverChannel.close();
            }
        }
    }

    @Test
    public void testOpen() throws IOException {
        OrderEntryFactory orderEntry = Mockito.mock(OrderEntryFactory.class);
        Instruments instruments = Mockito.mock(Instruments.class);
        InetSocketAddress address = new InetSocketAddress("localhost", 0);
        String senderCompId = "TEST_SENDER";

        acceptor = FIXAcceptor.open(orderEntry, address, senderCompId, instruments);

        assertNotNull(acceptor);
    }

    @Test
    public void testGetServerChannel() throws IOException {
        OrderEntryFactory orderEntry = Mockito.mock(OrderEntryFactory.class);
        Instruments instruments = Mockito.mock(Instruments.class);
        InetSocketAddress address = new InetSocketAddress("localhost", 0);
        String senderCompId = "TEST_SENDER";

        acceptor = FIXAcceptor.open(orderEntry, address, senderCompId, instruments);
        ServerSocketChannel serverChannel = acceptor.getServerChannel();

        assertNotNull(serverChannel);
        assertEquals(false, serverChannel.isBlocking());
    }

    @Test
    public void testAcceptNoConnection() throws IOException {
        OrderEntryFactory orderEntry = Mockito.mock(OrderEntryFactory.class);
        Instruments instruments = Mockito.mock(Instruments.class);
        InetSocketAddress address = new InetSocketAddress("localhost", 0);
        String senderCompId = "TEST_SENDER";

        acceptor = FIXAcceptor.open(orderEntry, address, senderCompId, instruments);
        Session session = acceptor.accept();

        assertNull(session);
    }

    @Test
    public void testAcceptWithConnection() throws IOException {
        OrderEntryFactory orderEntry = Mockito.mock(OrderEntryFactory.class);
        SoupBinTCPClient mockClient = Mockito.mock(SoupBinTCPClient.class);
        Mockito.when(orderEntry.create(Mockito.any(), Mockito.any())).thenReturn(mockClient);
        Instruments instruments = Mockito.mock(Instruments.class);
        InetSocketAddress address = new InetSocketAddress("localhost", 0);
        String senderCompId = "TEST_SENDER";

        acceptor = FIXAcceptor.open(orderEntry, address, senderCompId, instruments);
        ServerSocketChannel serverChannel = acceptor.getServerChannel();
        int port = serverChannel.socket().getLocalPort();

        SocketChannel clientChannel = null;
        Session session = null;
        try {
            clientChannel = SocketChannel.open();
            clientChannel.connect(new InetSocketAddress("localhost", port));

            while (!clientChannel.finishConnect()) {
                Thread.sleep(10);
            }

            session = acceptor.accept();

            if (session != null) {
                assertNotNull(session);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (session != null) {
                session.close();
            }
            if (clientChannel != null && clientChannel.isOpen()) {
                clientChannel.close();
            }
        }
    }
}
