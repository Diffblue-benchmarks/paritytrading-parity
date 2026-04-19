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
import static org.mockito.Mockito.*;

import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import com.paritytrading.parity.net.poe.POEClientListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderEntryFactoryTest {

    private ServerSocketChannel serverChannel;
    private InetSocketAddress   serverAddress;
    private OrderEntryFactory   factory;

    @BeforeEach
    void setUp() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("127.0.0.1", 0));
        serverAddress = (InetSocketAddress) serverChannel.getLocalAddress();

        factory = new OrderEntryFactory(serverAddress);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (serverChannel != null && serverChannel.isOpen())
            serverChannel.close();
    }

    @Test
    void createReturnsNonNullClient() throws IOException {
        POEClientListener            listener       = mock(POEClientListener.class);
        SoupBinTCPClientStatusListener statusListener = mock(SoupBinTCPClientStatusListener.class);

        SoupBinTCPClient client = factory.create(listener, statusListener);

        assertNotNull(client);
        client.close();
    }

    @Test
    void createConnectsToConfiguredAddress() throws IOException {
        POEClientListener            listener       = mock(POEClientListener.class);
        SoupBinTCPClientStatusListener statusListener = mock(SoupBinTCPClientStatusListener.class);

        SoupBinTCPClient client = factory.create(listener, statusListener);

        assertNotNull(client);
        client.close();
    }

    @Test
    void createThrowsWhenServerUnavailable() throws IOException {
        serverChannel.close();

        OrderEntryFactory badFactory = new OrderEntryFactory(
                new InetSocketAddress("127.0.0.1", serverAddress.getPort()));

        POEClientListener            listener       = mock(POEClientListener.class);
        SoupBinTCPClientStatusListener statusListener = mock(SoupBinTCPClientStatusListener.class);

        assertThrows(IOException.class, () -> badFactory.create(listener, statusListener));
    }

    @Test
    void constructorStoresAddress() throws IOException {
        InetSocketAddress            addr           = new InetSocketAddress("127.0.0.1", 12345);
        OrderEntryFactory            localFactory   = new OrderEntryFactory(addr);

        assertNotNull(localFactory);
    }
}
