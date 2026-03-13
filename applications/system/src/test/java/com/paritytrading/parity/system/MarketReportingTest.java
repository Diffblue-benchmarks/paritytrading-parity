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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.paritytrading.nassau.moldudp64.MoldUDP64DefaultMessageStore;
import com.paritytrading.nassau.moldudp64.MoldUDP64DownstreamPacket;
import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class MarketReportingTest {

    private MoldUDP64Server mockTransport;
    private MoldUDP64RequestServer mockRequestTransport;
    private MarketReporting marketReporting;

    @BeforeEach
    public void setUp() throws Exception {
        mockTransport = mock(MoldUDP64Server.class);
        mockRequestTransport = mock(MoldUDP64RequestServer.class);

        Constructor<MarketReporting> constructor = MarketReporting.class.getDeclaredConstructor(
            MoldUDP64Server.class, MoldUDP64RequestServer.class);
        constructor.setAccessible(true);
        marketReporting = constructor.newInstance(mockTransport, mockRequestTransport);
    }

    @Test
    public void testConstructor() throws Exception {
        assertNotNull(marketReporting);

        Field versionField = MarketReporting.class.getDeclaredField("version");
        versionField.setAccessible(true);
        assertNotNull(versionField.get(marketReporting));

        Field orderEnteredField = MarketReporting.class.getDeclaredField("orderEntered");
        orderEnteredField.setAccessible(true);
        assertNotNull(orderEnteredField.get(marketReporting));

        Field orderAddedField = MarketReporting.class.getDeclaredField("orderAdded");
        orderAddedField.setAccessible(true);
        assertNotNull(orderAddedField.get(marketReporting));

        Field orderCanceledField = MarketReporting.class.getDeclaredField("orderCanceled");
        orderCanceledField.setAccessible(true);
        assertNotNull(orderCanceledField.get(marketReporting));

        Field tradeField = MarketReporting.class.getDeclaredField("trade");
        tradeField.setAccessible(true);
        assertNotNull(tradeField.get(marketReporting));

        Field transportField = MarketReporting.class.getDeclaredField("transport");
        transportField.setAccessible(true);
        assertEquals(mockTransport, transportField.get(marketReporting));

        Field requestTransportField = MarketReporting.class.getDeclaredField("requestTransport");
        requestTransportField.setAccessible(true);
        assertEquals(mockRequestTransport, requestTransportField.get(marketReporting));

        Field messagesField = MarketReporting.class.getDeclaredField("messages");
        messagesField.setAccessible(true);
        assertNotNull(messagesField.get(marketReporting));

        Field packetField = MarketReporting.class.getDeclaredField("packet");
        packetField.setAccessible(true);
        assertNotNull(packetField.get(marketReporting));

        Field bufferField = MarketReporting.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        assertNotNull(bufferField.get(marketReporting));
    }

    @Test
    public void testGetTransport() {
        MoldUDP64Server transport = marketReporting.getTransport();
        assertNotNull(transport);
        assertEquals(mockTransport, transport);
    }

    @Test
    public void testGetRequestTransport() {
        MoldUDP64RequestServer requestTransport = marketReporting.getRequestTransport();
        assertNotNull(requestTransport);
        assertEquals(mockRequestTransport, requestTransport);
    }

    @Test
    public void testServe() throws Exception {
        marketReporting.serve();
        verify(mockRequestTransport).serve(any(MoldUDP64DefaultMessageStore.class));
    }

    @Test
    public void testVersion() throws Exception {
        marketReporting.version();
        verify(mockTransport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testOrderEntered() throws Exception {
        long username = 100L;
        long orderNumber = 200L;
        byte side = 1;
        long instrument = 300L;
        long quantity = 400L;
        long price = 500L;

        marketReporting.orderEntered(username, orderNumber, side, instrument, quantity, price);
        verify(mockTransport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testOrderAdded() throws Exception {
        long orderNumber = 123L;

        marketReporting.orderAdded(orderNumber);
        verify(mockTransport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testOrderCanceled() throws Exception {
        long orderNumber = 123L;
        long canceledQuantity = 50L;

        marketReporting.orderCanceled(orderNumber, canceledQuantity);
        verify(mockTransport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testTrade() throws Exception {
        long restingOrderNumber = 100L;
        long incomingOrderNumber = 200L;
        long quantity = 50L;
        long matchNumber = 300L;

        marketReporting.trade(restingOrderNumber, incomingOrderNumber, quantity, matchNumber);
        verify(mockTransport).send(any(MoldUDP64DownstreamPacket.class));
    }

    @Test
    public void testTimestamp() throws Exception {
        Method timestampMethod = MarketReporting.class.getDeclaredMethod("timestamp");
        timestampMethod.setAccessible(true);

        long timestamp = (Long) timestampMethod.invoke(marketReporting);
        assertTrue(timestamp > 0);
    }

    @Test
    public void testOpen() throws Exception {
        String session = "TEST_SESSION";
        NetworkInterface multicastInterface = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());
        InetSocketAddress multicastGroup = new InetSocketAddress("239.255.0.1", 5000);
        InetSocketAddress requestAddress = new InetSocketAddress("127.0.0.1", 0);

        MarketReporting reporting = MarketReporting.open(session, multicastInterface, multicastGroup, requestAddress);

        assertNotNull(reporting);
        assertNotNull(reporting.getTransport());
        assertNotNull(reporting.getRequestTransport());
    }
}
