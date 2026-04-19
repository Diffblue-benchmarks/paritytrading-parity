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

import static org.mockito.Mockito.*;

import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderEntryStatusListenerTest {

    private OrderEntry orderEntry;
    private SoupBinTCPClientStatusListener statusListener;

    @BeforeEach
    void setUp() throws Exception {
        orderEntry = mock(OrderEntry.class, CALLS_REAL_METHODS);

        Class<?> slClass = Class.forName(
                "com.paritytrading.parity.client.OrderEntry$StatusListener");
        Constructor<?> slConstructor = slClass.getDeclaredConstructor(OrderEntry.class);
        slConstructor.setAccessible(true);
        statusListener = (SoupBinTCPClientStatusListener) slConstructor.newInstance(orderEntry);
    }

    @Test
    void heartbeatTimeout() throws Exception {
        statusListener.heartbeatTimeout(mock(SoupBinTCPClient.class));

        verify(orderEntry).close();
    }

    @Test
    void loginAccepted() throws Exception {
        SoupBinTCP.LoginAccepted payload = new SoupBinTCP.LoginAccepted();

        statusListener.loginAccepted(mock(SoupBinTCPClient.class), payload);

        verify(orderEntry, never()).close();
    }

    @Test
    void loginRejected() throws Exception {
        SoupBinTCP.LoginRejected payload = new SoupBinTCP.LoginRejected();

        statusListener.loginRejected(mock(SoupBinTCPClient.class), payload);

        verify(orderEntry).close();
    }

    @Test
    void endOfSession() throws Exception {
        statusListener.endOfSession(mock(SoupBinTCPClient.class));

        verify(orderEntry, never()).close();
    }
}
