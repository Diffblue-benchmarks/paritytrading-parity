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
import static org.mockito.Mockito.*;

import com.paritytrading.parity.net.poe.POEClientListener;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.Test;

class OrderEntryReceiverTest {

    @Test
    void receiverProcessesDataAndExitsWhenClosed() throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress("localhost", 0));
        int port = ((InetSocketAddress) server.getLocalAddress()).getPort();

        POEClientListener listener = mock(POEClientListener.class);

        OrderEntry orderEntry = OrderEntry.open(new InetSocketAddress("localhost", port), listener);
        SocketChannel accepted = server.accept();

        Selector selector = getSelector(orderEntry);

        // Send a server heartbeat to trigger the receive path with numKeys > 0
        ByteBuffer heartbeat = ByteBuffer.allocate(3);
        heartbeat.putShort((short) 1);
        heartbeat.put((byte) 'H');
        heartbeat.flip();
        accepted.write(heartbeat);

        // Let receiver process the heartbeat
        Thread.sleep(300);

        // Close, causing receiver loop to exit
        orderEntry.close();

        // Wait for receiver to finish and clean up
        Thread.sleep(500);

        assertFalse(selector.isOpen());

        accepted.close();
        server.close();
    }

    @Test
    void receiverExitsWhenConnectionClosed() throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress("localhost", 0));
        int port = ((InetSocketAddress) server.getLocalAddress()).getPort();

        POEClientListener listener = mock(POEClientListener.class);

        OrderEntry orderEntry = OrderEntry.open(new InetSocketAddress("localhost", port), listener);
        SocketChannel accepted = server.accept();

        Selector selector = getSelector(orderEntry);

        // Close server-side to make transport.receive() return < 0
        accepted.close();

        // Wait for receiver to detect and clean up
        Thread.sleep(500);

        assertFalse(selector.isOpen());

        orderEntry.close();
        server.close();
    }

    @Test
    void receiverRunsKeepAliveWhenNoData() throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress("localhost", 0));
        int port = ((InetSocketAddress) server.getLocalAddress()).getPort();

        POEClientListener listener = mock(POEClientListener.class);

        OrderEntry orderEntry = OrderEntry.open(new InetSocketAddress("localhost", port), listener);
        SocketChannel accepted = server.accept();

        Selector selector = getSelector(orderEntry);

        // Let receiver run a few iterations with no data (covers keepAlive path)
        Thread.sleep(400);

        orderEntry.close();

        // Wait for cleanup
        Thread.sleep(300);

        assertFalse(selector.isOpen());

        accepted.close();
        server.close();
    }

    private static Selector getSelector(OrderEntry orderEntry) throws Exception {
        Field field = OrderEntry.class.getDeclaredField("selector");
        field.setAccessible(true);
        return (Selector) field.get(orderEntry);
    }
}
