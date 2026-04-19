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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderEntryTest {

    private OrderBooks books;
    private OrderEntry orderEntry;

    @BeforeEach
    void setUp() throws Exception {
        books = mock(OrderBooks.class);
        orderEntry = OrderEntry.open(new InetSocketAddress("127.0.0.1", 0), books);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (orderEntry != null) {
            orderEntry.getChannel().close();
        }
    }

    @Test
    void open() {
        assertNotNull(orderEntry);

        ServerSocketChannel channel = orderEntry.getChannel();

        assertNotNull(channel);
        assertTrue(channel.isOpen());
        assertFalse(channel.isBlocking());
    }

    @Test
    void getChannel() {
        ServerSocketChannel channel = orderEntry.getChannel();

        assertNotNull(channel);
        assertTrue(channel instanceof ServerSocketChannel);
    }

    @Test
    void acceptReturnsNullWhenNoPendingConnection() throws Exception {
        Session session = orderEntry.accept();

        assertNull(session);
    }

    @Test
    void acceptReturnsSessionWhenConnectionPending() throws Exception {
        InetSocketAddress address = (InetSocketAddress) orderEntry.getChannel().getLocalAddress();

        SocketChannel client = SocketChannel.open();
        try {
            client.connect(address);

            Session session = orderEntry.accept();

            assertNotNull(session);
        } finally {
            client.close();
        }
    }
}
