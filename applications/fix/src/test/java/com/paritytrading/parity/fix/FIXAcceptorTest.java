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

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.parity.util.Instruments;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class FIXAcceptorTest {

    private FIXAcceptor acceptor;
    private OrderEntryFactory orderEntryFactory;
    private Instruments instruments;

    @BeforeEach
    void setUp() throws IOException {
        orderEntryFactory = Mockito.mock(OrderEntryFactory.class);
        instruments = Mockito.mock(Instruments.class);
        acceptor = FIXAcceptor.open(orderEntryFactory,
                new InetSocketAddress("127.0.0.1", 0), "SENDER", instruments);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (acceptor != null) {
            acceptor.getServerChannel().close();
        }
    }

    @Test
    void openCreatesAcceptor() {
        assertNotNull(acceptor);
    }

    @Test
    void openCreatesNonBlockingServerChannel() throws IOException {
        ServerSocketChannel channel = acceptor.getServerChannel();

        assertNotNull(channel);
        assertTrue(channel.isOpen());
        assertFalse(channel.isBlocking());
    }

    @Test
    void openBindsServerChannel() throws IOException {
        ServerSocketChannel channel = acceptor.getServerChannel();

        assertNotNull(channel.getLocalAddress());
    }

    @Test
    void getServerChannelReturnsChannel() {
        ServerSocketChannel channel = acceptor.getServerChannel();

        assertNotNull(channel);
    }

    @Test
    void acceptReturnsNullWhenNoConnectionPending() {
        Session session = acceptor.accept();

        assertNull(session);
    }

    @Test
    void acceptReturnsNullWhenSessionCreationFails() throws IOException {
        Mockito.when(orderEntryFactory.create(Mockito.any(), Mockito.any()))
                .thenThrow(new IOException("Connection refused"));

        InetSocketAddress address = (InetSocketAddress) acceptor.getServerChannel().getLocalAddress();

        SocketChannel client = SocketChannel.open();
        try {
            client.connect(address);

            Session session = acceptor.accept();

            assertNull(session);
        } finally {
            client.close();
        }
    }

    @Test
    void acceptReturnsSessionOnSuccessfulConnection() throws IOException {
        SoupBinTCPClient mockOrderEntry = Mockito.mock(SoupBinTCPClient.class);
        Mockito.when(orderEntryFactory.create(Mockito.any(), Mockito.any()))
                .thenReturn(mockOrderEntry);

        InetSocketAddress address = (InetSocketAddress) acceptor.getServerChannel().getLocalAddress();

        SocketChannel client = SocketChannel.open();
        try {
            client.connect(address);

            Session session = acceptor.accept();

            assertNotNull(session);
            session.close();
        } finally {
            client.close();
        }
    }
}
