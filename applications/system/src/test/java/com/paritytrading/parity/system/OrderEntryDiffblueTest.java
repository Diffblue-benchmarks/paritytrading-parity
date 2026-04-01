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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderEntryDiffblueTest {

    private OrderBooks books;

    private OrderEntry orderEntry;

    @BeforeEach
    void setUp() throws IOException {
        books = mock(OrderBooks.class);
        orderEntry = OrderEntry.open(new InetSocketAddress("127.0.0.1", 0), books);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (orderEntry != null) {
            orderEntry.getChannel().close();
        }
    }

    @Test
    void testOpen_givenValidAddress_thenReturnsNonNullOrderEntry() throws IOException {
        OrderEntry entry = OrderEntry.open(new InetSocketAddress("127.0.0.1", 0), books);

        assertNotNull(entry);

        entry.getChannel().close();
    }

    @Test
    void testGetChannel_givenOpenedEntry_thenReturnsServerSocketChannel() {
        ServerSocketChannel channel = orderEntry.getChannel();

        assertNotNull(channel);
    }

    @Test
    void testAccept_givenNoConnection_thenReturnsNull() throws IOException {
        Session session = orderEntry.accept();

        assertNull(session);
    }

    @Test
    void testAccept_givenPendingConnection_thenReturnsSession() throws IOException {
        int port = ((InetSocketAddress) orderEntry.getChannel().getLocalAddress()).getPort();
        SocketChannel client = SocketChannel.open(new InetSocketAddress("127.0.0.1", port));

        try {
            Session session = orderEntry.accept();

            assertNotNull(session);
        } finally {
            client.close();
        }
    }

}
