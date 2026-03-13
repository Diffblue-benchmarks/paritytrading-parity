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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.Test;

class OrderEntryTest {

    @Test
    void testOpenConnection() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 0));
        InetSocketAddress serverAddress = (InetSocketAddress) serverChannel.getLocalAddress();

        Thread acceptThread = new Thread(() -> {
            try {
                serverChannel.accept();
            } catch (IOException e) {
            }
        });
        acceptThread.start();

        POEClientListener listener = mock(POEClientListener.class);
        OrderEntry orderEntry = OrderEntry.open(serverAddress, listener);

        assertNotNull(orderEntry);
        assertNotNull(orderEntry.getTransport());

        orderEntry.close();
        serverChannel.close();

        try {
            acceptThread.join(1000);
        } catch (InterruptedException e) {
        }
    }

    @Test
    void testClose() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 0));
        InetSocketAddress serverAddress = (InetSocketAddress) serverChannel.getLocalAddress();

        Thread acceptThread = new Thread(() -> {
            try {
                serverChannel.accept();
            } catch (IOException e) {
            }
        });
        acceptThread.start();

        POEClientListener listener = mock(POEClientListener.class);
        OrderEntry orderEntry = OrderEntry.open(serverAddress, listener);

        orderEntry.close();

        serverChannel.close();

        try {
            acceptThread.join(1000);
        } catch (InterruptedException e) {
        }
    }

    @Test
    void testGetTransport() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 0));
        InetSocketAddress serverAddress = (InetSocketAddress) serverChannel.getLocalAddress();

        Thread acceptThread = new Thread(() -> {
            try {
                serverChannel.accept();
            } catch (IOException e) {
            }
        });
        acceptThread.start();

        POEClientListener listener = mock(POEClientListener.class);
        OrderEntry orderEntry = OrderEntry.open(serverAddress, listener);

        assertNotNull(orderEntry.getTransport());

        orderEntry.close();
        serverChannel.close();

        try {
            acceptThread.join(1000);
        } catch (InterruptedException e) {
        }
    }

    @Test
    void testSendMessage() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 0));
        InetSocketAddress serverAddress = (InetSocketAddress) serverChannel.getLocalAddress();

        Thread acceptThread = new Thread(() -> {
            try {
                SocketChannel clientChannel = serverChannel.accept();
                clientChannel.configureBlocking(false);
                Thread.sleep(500);
            } catch (IOException | InterruptedException e) {
            }
        });
        acceptThread.start();

        POEClientListener listener = mock(POEClientListener.class);
        OrderEntry orderEntry = OrderEntry.open(serverAddress, listener);

        POE.EnterOrder message = new POE.EnterOrder();
        for (int i = 0; i < message.orderId.length; i++) {
            message.orderId[i] = (byte) i;
        }
        message.side = POE.BUY;
        message.instrument = 1L;
        message.quantity = 100L;
        message.price = 15000L;

        orderEntry.send(message);

        orderEntry.close();
        serverChannel.close();

        try {
            acceptThread.join(1000);
        } catch (InterruptedException e) {
        }
    }
}
