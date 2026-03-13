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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.paritytrading.nassau.moldudp64.MoldUDP64DefaultMessageStore;
import com.paritytrading.nassau.moldudp64.MoldUDP64DownstreamPacket;
import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import com.paritytrading.parity.net.pmd.PMD;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MarketDataTest {

    private MarketData marketData;
    private MoldUDP64Server mockTransport;
    private MoldUDP64RequestServer mockRequestTransport;
    private DatagramChannel multicastChannel;
    private DatagramChannel requestChannel;

    @BeforeEach
    public void setUp() throws Exception {
        mockTransport = Mockito.mock(MoldUDP64Server.class);
        mockRequestTransport = Mockito.mock(MoldUDP64RequestServer.class);

        Constructor<MarketData> constructor = MarketData.class.getDeclaredConstructor(
            MoldUDP64Server.class, MoldUDP64RequestServer.class);
        constructor.setAccessible(true);
        marketData = constructor.newInstance(mockTransport, mockRequestTransport);
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (multicastChannel != null && multicastChannel.isOpen()) {
            multicastChannel.close();
        }
        if (requestChannel != null && requestChannel.isOpen()) {
            requestChannel.close();
        }
    }

    @Test
    public void testConstructor() throws Exception {
        assertNotNull(marketData);

        Field transportField = MarketData.class.getDeclaredField("transport");
        transportField.setAccessible(true);
        Object transport = transportField.get(marketData);
        assertNotNull(transport);

        Field requestTransportField = MarketData.class.getDeclaredField("requestTransport");
        requestTransportField.setAccessible(true);
        Object requestTransport = requestTransportField.get(marketData);
        assertNotNull(requestTransport);
    }

    @Test
    public void testOpen() throws Exception {
        NetworkInterface loopback = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());
        if (loopback == null) {
            loopback = NetworkInterface.getNetworkInterfaces().nextElement();
        }

        multicastChannel = DatagramChannel.open(StandardProtocolFamily.INET);
        multicastChannel.bind(new InetSocketAddress("127.0.0.1", 0));
        InetSocketAddress multicastGroup = (InetSocketAddress) multicastChannel.getLocalAddress();

        requestChannel = DatagramChannel.open();
        requestChannel.bind(new InetSocketAddress("127.0.0.1", 0));
        InetSocketAddress requestAddress = (InetSocketAddress) requestChannel.getLocalAddress();

        multicastChannel.close();
        requestChannel.close();

        MarketData md = MarketData.open("TEST", loopback, multicastGroup, requestAddress);

        assertNotNull(md);
        assertNotNull(md.getTransport());
        assertNotNull(md.getRequestTransport());

        Field transportField = MarketData.class.getDeclaredField("transport");
        transportField.setAccessible(true);
        MoldUDP64Server transport = (MoldUDP64Server) transportField.get(md);
        if (transport != null) {
            Field channelField = MoldUDP64Server.class.getDeclaredField("channel");
            channelField.setAccessible(true);
            DatagramChannel channel = (DatagramChannel) channelField.get(transport);
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        }

        Field requestTransportField = MarketData.class.getDeclaredField("requestTransport");
        requestTransportField.setAccessible(true);
        MoldUDP64RequestServer requestTransport = (MoldUDP64RequestServer) requestTransportField.get(md);
        if (requestTransport != null) {
            Field channelField = MoldUDP64RequestServer.class.getDeclaredField("channel");
            channelField.setAccessible(true);
            DatagramChannel channel = (DatagramChannel) channelField.get(requestTransport);
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        }
    }

    @Test
    public void testGetTransport() {
        MoldUDP64Server transport = marketData.getTransport();

        assertNotNull(transport);
        assertEquals(mockTransport, transport);
    }

    @Test
    public void testGetRequestTransport() {
        MoldUDP64RequestServer requestTransport = marketData.getRequestTransport();

        assertNotNull(requestTransport);
        assertEquals(mockRequestTransport, requestTransport);
    }

    @Test
    public void testServe() throws Exception {
        marketData.serve();

        Mockito.verify(mockRequestTransport).serve(Mockito.any(MoldUDP64DefaultMessageStore.class));
    }

    @Test
    public void testVersion() throws Exception {
        marketData.version();

        Mockito.verify(mockTransport).send(Mockito.any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testOrderAdded() throws Exception {
        long orderNumber = 12345L;
        byte side = PMD.BUY;
        long instrument = 1L;
        long quantity = 100L;
        long price = 5000L;

        marketData.orderAdded(orderNumber, side, instrument, quantity, price);

        Mockito.verify(mockTransport).send(Mockito.any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testOrderExecuted() throws Exception {
        long orderNumber = 12345L;
        long quantity = 100L;
        long matchNumber = 1L;

        marketData.orderExecuted(orderNumber, quantity, matchNumber);

        Mockito.verify(mockTransport).send(Mockito.any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testOrderCanceled() throws Exception {
        long orderNumber = 12345L;
        long canceledQuantity = 50L;

        marketData.orderCanceled(orderNumber, canceledQuantity);

        Mockito.verify(mockTransport).send(Mockito.any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testTimestamp() throws Exception {
        Method timestampMethod = MarketData.class.getDeclaredMethod("timestamp");
        timestampMethod.setAccessible(true);

        long timestamp = (Long) timestampMethod.invoke(marketData);

        assertTrue(timestamp > 0);
    }

    @Test
    public void testSend() throws Exception {
        PMD.Version version = new PMD.Version();
        version.version = PMD.VERSION;

        Method sendMethod = MarketData.class.getDeclaredMethod("send", PMD.Message.class);
        sendMethod.setAccessible(true);

        sendMethod.invoke(marketData, version);

        Mockito.verify(mockTransport).send(Mockito.any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testOrderAddedWithSellSide() throws Exception {
        long orderNumber = 67890L;
        byte side = PMD.SELL;
        long instrument = 2L;
        long quantity = 200L;
        long price = 6000L;

        marketData.orderAdded(orderNumber, side, instrument, quantity, price);

        Mockito.verify(mockTransport).send(Mockito.any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testMultipleOrders() throws Exception {
        marketData.orderAdded(1L, PMD.BUY, 1L, 100L, 5000L);
        marketData.orderExecuted(1L, 50L, 1L);
        marketData.orderCanceled(1L, 50L);

        Mockito.verify(mockTransport, Mockito.times(3)).send(Mockito.any(MoldUDP64DownstreamPacket.class));
    }
}
