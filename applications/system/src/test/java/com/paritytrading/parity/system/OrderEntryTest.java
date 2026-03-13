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
package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrderEntryTest {

    private OrderEntry orderEntry;
    private SocketChannel clientChannel;

    @AfterEach
    public void tearDown() throws IOException {
        if (clientChannel != null && clientChannel.isOpen()) {
            clientChannel.close();
        }
        if (orderEntry != null) {
            ServerSocketChannel channel = orderEntry.getChannel();
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        }
    }

    @Test
    public void testOpen() throws IOException {
        OrderBooks books = Mockito.mock(OrderBooks.class);
        InetSocketAddress address = new InetSocketAddress("localhost", 0);

        orderEntry = OrderEntry.open(address, books);

        assertNotNull(orderEntry);
        assertNotNull(orderEntry.getChannel());
        assertEquals(false, orderEntry.getChannel().isBlocking());
    }

    @Test
    public void testGetChannel() throws IOException {
        OrderBooks books = Mockito.mock(OrderBooks.class);
        InetSocketAddress address = new InetSocketAddress("localhost", 0);

        orderEntry = OrderEntry.open(address, books);
        ServerSocketChannel channel = orderEntry.getChannel();

        assertNotNull(channel);
        assertEquals(true, channel.isOpen());
    }

    @Test
    public void testAcceptWithNoConnection() throws IOException {
        OrderBooks books = Mockito.mock(OrderBooks.class);
        InetSocketAddress address = new InetSocketAddress("localhost", 0);

        orderEntry = OrderEntry.open(address, books);
        Session session = orderEntry.accept();

        assertNull(session);
    }

    @Test
    public void testAcceptWithConnection() throws IOException {
        OrderBooks books = Mockito.mock(OrderBooks.class);
        InetSocketAddress address = new InetSocketAddress("localhost", 0);

        orderEntry = OrderEntry.open(address, books);
        InetSocketAddress serverAddress = (InetSocketAddress) orderEntry.getChannel().getLocalAddress();

        clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);
        clientChannel.connect(serverAddress);

        Session session = orderEntry.accept();

        assertNotNull(session);
        assertNotNull(session.getTransport());
    }
}
