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

import com.paritytrading.nassau.moldudp64.MoldUDP64DownstreamPacket;
import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import com.paritytrading.parity.net.pmd.PMD;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketDataTest {

    private MoldUDP64Server        transport;
    private MoldUDP64RequestServer requestTransport;
    private MarketData             marketData;

    @BeforeEach
    void setUp() throws Exception {
        transport        = mock(MoldUDP64Server.class);
        requestTransport = mock(MoldUDP64RequestServer.class);

        Constructor<MarketData> ctor = MarketData.class.getDeclaredConstructor(
                MoldUDP64Server.class, MoldUDP64RequestServer.class);
        ctor.setAccessible(true);
        marketData = ctor.newInstance(transport, requestTransport);
    }

    @Test
    void getTransport() {
        assertSame(transport, marketData.getTransport());
    }

    @Test
    void getRequestTransport() {
        assertSame(requestTransport, marketData.getRequestTransport());
    }

    @Test
    void serve() throws Exception {
        marketData.serve();

        verify(requestTransport).serve(any());
    }

    @Test
    void version() throws Exception {
        marketData.version();

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void orderAdded() throws Exception {
        marketData.orderAdded(1L, PMD.BUY, 100L, 50L, 1000L);

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void orderExecuted() throws Exception {
        marketData.orderExecuted(1L, 50L, 1L);

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void orderCanceled() throws Exception {
        marketData.orderCanceled(1L, 50L);

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void multipleSends() throws Exception {
        marketData.version();
        marketData.orderAdded(1L, PMD.BUY, 100L, 50L, 1000L);
        marketData.orderExecuted(1L, 25L, 1L);
        marketData.orderCanceled(1L, 25L);

        verify(transport, times(4)).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void orderAddedWithSell() throws Exception {
        marketData.orderAdded(2L, PMD.SELL, 200L, 100L, 2000L);

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }
}
