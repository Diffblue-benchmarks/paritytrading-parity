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
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import com.paritytrading.parity.net.poe.POEClientListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class OrderEntryFactoryTest {

    private ServerSocket serverSocket;
    private InetSocketAddress address;
    private OrderEntryFactory factory;

    @BeforeEach
    public void setUp() throws IOException {
        serverSocket = new ServerSocket(0);
        address = new InetSocketAddress("localhost", serverSocket.getLocalPort());
        factory = new OrderEntryFactory(address);
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    @Test
    public void testConstructor() {
        OrderEntryFactory testFactory = new OrderEntryFactory(address);
        assertNotNull(testFactory);
    }

    @Test
    public void testCreate() throws IOException {
        POEClientListener listener = mock(POEClientListener.class);
        SoupBinTCPClientStatusListener statusListener = mock(SoupBinTCPClientStatusListener.class);

        SoupBinTCPClient client = factory.create(listener, statusListener);

        assertNotNull(client);
    }

    @Test
    public void testCreateWithInvalidAddress() {
        InetSocketAddress invalidAddress = new InetSocketAddress("localhost", 1);
        OrderEntryFactory invalidFactory = new OrderEntryFactory(invalidAddress);

        POEClientListener listener = mock(POEClientListener.class);
        SoupBinTCPClientStatusListener statusListener = mock(SoupBinTCPClientStatusListener.class);

        assertThrows(IOException.class, () -> {
            invalidFactory.create(listener, statusListener);
        });
    }
}
