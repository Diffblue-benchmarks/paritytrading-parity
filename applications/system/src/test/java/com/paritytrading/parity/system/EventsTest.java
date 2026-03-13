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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.List;
import org.junit.jupiter.api.Test;

class EventsTest {

    @Test
    public void testCleanUpWithMultipleSessions() throws Exception {
        Events events = createEventsInstance();

        Session session1 = mock(Session.class);
        Session session2 = mock(Session.class);
        Session session3 = mock(Session.class);

        List<Session> toKeepAlive = getToKeepAliveList(events);
        List<Session> toCleanUp = getToCleanUpList(events);

        toKeepAlive.add(session1);
        toKeepAlive.add(session2);
        toKeepAlive.add(session3);

        toCleanUp.add(session1);
        toCleanUp.add(session3);

        invokeCleanUp(events);

        verify(session1, times(1)).close();
        verify(session3, times(1)).close();
        verify(session2, times(0)).close();

        assertEquals(1, toKeepAlive.size());
        assertTrue(toKeepAlive.contains(session2));
        assertFalse(toKeepAlive.contains(session1));
        assertFalse(toKeepAlive.contains(session3));

        assertEquals(0, toCleanUp.size());
    }

    @Test
    public void testCleanUpWithEmptyList() throws Exception {
        Events events = createEventsInstance();

        Session session1 = mock(Session.class);

        List<Session> toKeepAlive = getToKeepAliveList(events);
        List<Session> toCleanUp = getToCleanUpList(events);

        toKeepAlive.add(session1);

        invokeCleanUp(events);

        verify(session1, times(0)).close();

        assertEquals(1, toKeepAlive.size());
        assertTrue(toKeepAlive.contains(session1));

        assertEquals(0, toCleanUp.size());
    }

    @Test
    public void testCleanUpRemovesAllSessions() throws Exception {
        Events events = createEventsInstance();

        Session session1 = mock(Session.class);
        Session session2 = mock(Session.class);

        List<Session> toKeepAlive = getToKeepAliveList(events);
        List<Session> toCleanUp = getToCleanUpList(events);

        toKeepAlive.add(session1);
        toKeepAlive.add(session2);

        toCleanUp.add(session1);
        toCleanUp.add(session2);

        invokeCleanUp(events);

        verify(session1, times(1)).close();
        verify(session2, times(1)).close();

        assertEquals(0, toKeepAlive.size());
        assertEquals(0, toCleanUp.size());
    }

    @Test
    public void testCleanUpClearsToCleanUpList() throws Exception {
        Events events = createEventsInstance();

        Session session1 = mock(Session.class);

        List<Session> toCleanUp = getToCleanUpList(events);

        toCleanUp.add(session1);

        invokeCleanUp(events);

        verify(session1, times(1)).close();
        assertEquals(0, toCleanUp.size());
    }

    private Events createEventsInstance() throws Exception {
        MarketData marketData = createMarketDataMock();
        MarketReporting marketReporting = createMarketReportingMock();
        OrderEntry orderEntry = createOrderEntryMock();

        return new Events(marketData, marketReporting, orderEntry);
    }

    private MarketData createMarketDataMock() throws Exception {
        MoldUDP64Server transport = mock(MoldUDP64Server.class);
        MoldUDP64RequestServer requestTransport = mock(MoldUDP64RequestServer.class);
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);

        org.mockito.Mockito.when(requestTransport.getChannel()).thenReturn(channel);

        MarketData marketData = mock(MarketData.class);
        org.mockito.Mockito.when(marketData.getTransport()).thenReturn(transport);
        org.mockito.Mockito.when(marketData.getRequestTransport()).thenReturn(requestTransport);

        return marketData;
    }

    private MarketReporting createMarketReportingMock() throws Exception {
        MoldUDP64Server transport = mock(MoldUDP64Server.class);
        MoldUDP64RequestServer requestTransport = mock(MoldUDP64RequestServer.class);
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);

        org.mockito.Mockito.when(requestTransport.getChannel()).thenReturn(channel);

        MarketReporting marketReporting = mock(MarketReporting.class);
        org.mockito.Mockito.when(marketReporting.getTransport()).thenReturn(transport);
        org.mockito.Mockito.when(marketReporting.getRequestTransport()).thenReturn(requestTransport);

        return marketReporting;
    }

    private OrderEntry createOrderEntryMock() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 0));
        serverChannel.configureBlocking(false);

        OrderEntry orderEntry = mock(OrderEntry.class);
        org.mockito.Mockito.when(orderEntry.getChannel()).thenReturn(serverChannel);

        return orderEntry;
    }

    @SuppressWarnings("unchecked")
    private List<Session> getToKeepAliveList(Events events) throws Exception {
        Field field = Events.class.getDeclaredField("toKeepAlive");
        field.setAccessible(true);
        return (List<Session>) field.get(events);
    }

    @SuppressWarnings("unchecked")
    private List<Session> getToCleanUpList(Events events) throws Exception {
        Field field = Events.class.getDeclaredField("toCleanUp");
        field.setAccessible(true);
        return (List<Session>) field.get(events);
    }

    private void invokeCleanUp(Events events) throws Exception {
        Method method = Events.class.getDeclaredMethod("cleanUp");
        method.setAccessible(true);
        method.invoke(events);
    }
}
