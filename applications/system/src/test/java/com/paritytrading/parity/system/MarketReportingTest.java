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
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class MarketReportingTest {

    MoldUDP64Server        transport;
    MoldUDP64RequestServer requestTransport;
    MarketReporting        reporting;

    @BeforeEach
    void setUp() throws Exception {
        transport        = mock(MoldUDP64Server.class);
        requestTransport = mock(MoldUDP64RequestServer.class);

        Constructor<MarketReporting> ctor = MarketReporting.class.getDeclaredConstructor(
                MoldUDP64Server.class, MoldUDP64RequestServer.class);
        ctor.setAccessible(true);
        reporting = ctor.newInstance(transport, requestTransport);
    }

    @Test
    void getTransport() {
        assertSame(transport, reporting.getTransport());
    }

    @Test
    void getRequestTransport() {
        assertSame(requestTransport, reporting.getRequestTransport());
    }

    @Test
    void version() throws Exception {
        reporting.version();

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void orderEntered() throws Exception {
        reporting.orderEntered(1L, 2L, (byte) 'B', 3L, 100L, 5000L);

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void orderAdded() throws Exception {
        reporting.orderAdded(42L);

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void orderCanceled() throws Exception {
        reporting.orderCanceled(42L, 50L);

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void trade() throws Exception {
        reporting.trade(1L, 2L, 100L, 3L);

        verify(transport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void serve() throws Exception {
        reporting.serve();

        verify(requestTransport).serve(any());
    }

    @Test
    void multipleMessages() throws Exception {
        reporting.version();
        reporting.orderEntered(1L, 2L, (byte) 'B', 3L, 100L, 5000L);
        reporting.orderAdded(2L);

        verify(transport, times(3)).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    void openCreatesMarketReportingWithConfiguredChannels() throws Exception {
        DatagramChannel channel        = mock(DatagramChannel.class);
        DatagramChannel requestChannel = mock(DatagramChannel.class);

        when(channel.setOption(any(), any())).thenReturn(channel);
        when(channel.connect(any())).thenReturn(channel);
        when(requestChannel.bind(any())).thenReturn(requestChannel);
        when(requestChannel.configureBlocking(anyBoolean())).thenReturn(requestChannel);

        NetworkInterface  ni             = mock(NetworkInterface.class);
        InetSocketAddress multicastGroup = new InetSocketAddress("239.1.2.3", 5000);
        InetSocketAddress requestAddress = new InetSocketAddress("127.0.0.1", 6000);

        try (MockedStatic<DatagramChannel> dcMock = mockStatic(DatagramChannel.class)) {
            dcMock.when(() -> DatagramChannel.open(StandardProtocolFamily.INET))
                  .thenReturn(channel);
            dcMock.when(() -> DatagramChannel.open())
                  .thenReturn(requestChannel);

            MarketReporting result = MarketReporting.open("testsessio", ni,
                    multicastGroup, requestAddress);

            assertNotNull(result);
            assertNotNull(result.getTransport());
            assertNotNull(result.getRequestTransport());
        }

        verify(channel).setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
        verify(channel).connect(multicastGroup);
        verify(requestChannel).bind(requestAddress);
        verify(requestChannel).configureBlocking(false);
    }
}
