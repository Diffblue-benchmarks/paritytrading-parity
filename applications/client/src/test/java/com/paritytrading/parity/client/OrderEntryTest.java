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

import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderEntryTest {

    private ServerSocketChannel serverChannel;
    private SocketChannel       acceptedChannel;
    private OrderEntry          orderEntry;

    @BeforeEach
    void setUp() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("127.0.0.1", 0));
    }

    @AfterEach
    void tearDown() throws IOException {
        if (orderEntry != null)
            orderEntry.close();

        if (acceptedChannel != null)
            acceptedChannel.close();

        if (serverChannel != null)
            serverChannel.close();
    }

    private OrderEntry openConnection() throws IOException {
        POEClientListener listener = Mockito.mock(POEClientListener.class);
        InetSocketAddress address  = (InetSocketAddress) serverChannel.getLocalAddress();

        OrderEntry entry = OrderEntry.open(address, listener);

        acceptedChannel = serverChannel.accept();

        return entry;
    }

    @Test
    void open() throws IOException {
        orderEntry = openConnection();

        assertNotNull(orderEntry);
    }

    @Test
    void getTransport() throws IOException {
        orderEntry = openConnection();

        SoupBinTCPClient transport = orderEntry.getTransport();

        assertNotNull(transport);
    }

    @Test
    void close() throws IOException {
        orderEntry = openConnection();

        orderEntry.close();

        assertNotNull(orderEntry);
    }

    @Test
    void sendEnterOrder() throws IOException {
        orderEntry = openConnection();

        POE.EnterOrder enterOrder = new POE.EnterOrder();
        enterOrder.side       = POE.BUY;
        enterOrder.instrument = 1234L;
        enterOrder.quantity   = 100L;
        enterOrder.price      = 5000L;

        orderEntry.send(enterOrder);
    }

    @Test
    void sendCancelOrder() throws IOException {
        orderEntry = openConnection();

        POE.CancelOrder cancelOrder = new POE.CancelOrder();
        cancelOrder.quantity = 50L;

        orderEntry.send(cancelOrder);
    }
}
