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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;

import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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

    private void invokeKeepAlive(Events events) throws Exception {
        Method method = Events.class.getDeclaredMethod("keepAlive");
        method.setAccessible(true);
        method.invoke(events);
    }

    @Test
    public void testKeepAliveCallsMarketDataTransport() throws Exception {
        Events events = createEventsInstance();
        MarketData marketData = getMarketDataField(events);
        MoldUDP64Server transport = marketData.getTransport();

        invokeKeepAlive(events);

        verify(transport, times(1)).keepAlive();
    }

    @Test
    public void testKeepAliveCallsMarketReportingTransport() throws Exception {
        Events events = createEventsInstance();
        MarketReporting marketReporting = getMarketReportingField(events);
        MoldUDP64Server transport = marketReporting.getTransport();

        invokeKeepAlive(events);

        verify(transport, times(1)).keepAlive();
    }

    @Test
    public void testKeepAliveHandlesMarketDataIOException() throws Exception {
        MarketData marketData = createMarketDataMock();
        MoldUDP64Server transport = marketData.getTransport();
        org.mockito.Mockito.doThrow(new IOException()).when(transport).keepAlive();

        MarketReporting marketReporting = createMarketReportingMock();
        OrderEntry orderEntry = createOrderEntryMock();
        Events events = new Events(marketData, marketReporting, orderEntry);

        invokeKeepAlive(events);

        verify(transport, times(1)).keepAlive();
    }

    @Test
    public void testKeepAliveHandlesMarketReportingIOException() throws Exception {
        MarketData marketData = createMarketDataMock();
        MarketReporting marketReporting = createMarketReportingMock();
        MoldUDP64Server transport = marketReporting.getTransport();
        org.mockito.Mockito.doThrow(new IOException()).when(transport).keepAlive();

        OrderEntry orderEntry = createOrderEntryMock();
        Events events = new Events(marketData, marketReporting, orderEntry);

        invokeKeepAlive(events);

        verify(transport, times(1)).keepAlive();
    }

    @Test
    public void testKeepAliveCallsSessionTransports() throws Exception {
        Events events = createEventsInstance();

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport1 = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport2 = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);

        Session session1 = mock(Session.class);
        Session session2 = mock(Session.class);
        org.mockito.Mockito.when(session1.getTransport()).thenReturn(sessionTransport1);
        org.mockito.Mockito.when(session2.getTransport()).thenReturn(sessionTransport2);

        List<Session> toKeepAlive = getToKeepAliveList(events);
        toKeepAlive.add(session1);
        toKeepAlive.add(session2);

        invokeKeepAlive(events);

        verify(sessionTransport1, times(1)).keepAlive();
        verify(sessionTransport2, times(1)).keepAlive();
    }

    @Test
    public void testKeepAliveAddsTerminatedSessionToCleanUp() throws Exception {
        Events events = createEventsInstance();

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);
        org.mockito.Mockito.when(session.isTerminated()).thenReturn(true);

        List<Session> toKeepAlive = getToKeepAliveList(events);
        List<Session> toCleanUp = getToCleanUpList(events);
        toKeepAlive.add(session);

        invokeKeepAlive(events);

        assertEquals(1, toCleanUp.size());
        assertTrue(toCleanUp.contains(session));
    }

    @Test
    public void testKeepAliveAddsSessionWithIOExceptionToCleanUp() throws Exception {
        Events events = createEventsInstance();

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        org.mockito.Mockito.doThrow(new IOException()).when(sessionTransport).keepAlive();

        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);

        List<Session> toKeepAlive = getToKeepAliveList(events);
        List<Session> toCleanUp = getToCleanUpList(events);
        toKeepAlive.add(session);

        invokeKeepAlive(events);

        assertEquals(1, toCleanUp.size());
        assertTrue(toCleanUp.contains(session));
    }

    @Test
    public void testKeepAliveWithEmptySessionList() throws Exception {
        Events events = createEventsInstance();

        List<Session> toCleanUp = getToCleanUpList(events);

        invokeKeepAlive(events);

        assertEquals(0, toCleanUp.size());
    }

    @Test
    public void testKeepAliveDoesNotAddNonTerminatedSessionToCleanUp() throws Exception {
        Events events = createEventsInstance();

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);
        org.mockito.Mockito.when(session.isTerminated()).thenReturn(false);

        List<Session> toKeepAlive = getToKeepAliveList(events);
        List<Session> toCleanUp = getToCleanUpList(events);
        toKeepAlive.add(session);

        invokeKeepAlive(events);

        assertEquals(0, toCleanUp.size());
    }

    private MarketData getMarketDataField(Events events) throws Exception {
        Field field = Events.class.getDeclaredField("marketData");
        field.setAccessible(true);
        return (MarketData) field.get(events);
    }

    private MarketReporting getMarketReportingField(Events events) throws Exception {
        Field field = Events.class.getDeclaredField("marketReporting");
        field.setAccessible(true);
        return (MarketReporting) field.get(events);
    }

    private void invokeReceive(Events events, Session session) throws Exception {
        Method method = Events.class.getDeclaredMethod("receive", Session.class);
        method.setAccessible(true);
        method.invoke(events, session);
    }

    @Test
    public void testReceiveAddsSessionToCleanUpWhenReceiveReturnsNegative() throws Exception {
        Events events = createEventsInstance();

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        org.mockito.Mockito.when(sessionTransport.receive()).thenReturn(-1);

        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);

        List<Session> toCleanUp = getToCleanUpList(events);

        invokeReceive(events, session);

        assertEquals(1, toCleanUp.size());
        assertTrue(toCleanUp.contains(session));
    }

    @Test
    public void testReceiveDoesNotAddSessionToCleanUpWhenReceiveReturnsPositive() throws Exception {
        Events events = createEventsInstance();

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        org.mockito.Mockito.when(sessionTransport.receive()).thenReturn(100);

        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);

        List<Session> toCleanUp = getToCleanUpList(events);

        invokeReceive(events, session);

        assertEquals(0, toCleanUp.size());
    }

    @Test
    public void testReceiveDoesNotAddSessionToCleanUpWhenReceiveReturnsZero() throws Exception {
        Events events = createEventsInstance();

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        org.mockito.Mockito.when(sessionTransport.receive()).thenReturn(0);

        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);

        List<Session> toCleanUp = getToCleanUpList(events);

        invokeReceive(events, session);

        assertEquals(0, toCleanUp.size());
    }

    @Test
    public void testReceiveAddsSessionToCleanUpWhenIOExceptionThrown() throws Exception {
        Events events = createEventsInstance();

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        org.mockito.Mockito.when(sessionTransport.receive()).thenThrow(new IOException());

        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);

        List<Session> toCleanUp = getToCleanUpList(events);

        invokeReceive(events, session);

        assertEquals(1, toCleanUp.size());
        assertTrue(toCleanUp.contains(session));
    }

    private void invokeAccept(Events events) throws Exception {
        Method method = Events.class.getDeclaredMethod("accept");
        method.setAccessible(true);
        method.invoke(events);
    }

    private Selector getSelectorField(Events events) throws Exception {
        Field field = Events.class.getDeclaredField("selector");
        field.setAccessible(true);
        return (Selector) field.get(events);
    }

    private OrderEntry getOrderEntryField(Events events) throws Exception {
        Field field = Events.class.getDeclaredField("orderEntry");
        field.setAccessible(true);
        return (OrderEntry) field.get(events);
    }

    @Test
    public void testAcceptAddsSessionToKeepAliveList() throws Exception {
        Events events = createEventsInstance();
        OrderEntry orderEntry = getOrderEntryField(events);
        Selector selector = getSelectorField(events);

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        org.mockito.Mockito.when(sessionTransport.getChannel()).thenReturn(channel);

        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);
        org.mockito.Mockito.when(orderEntry.accept()).thenReturn(session);

        List<Session> toKeepAlive = getToKeepAliveList(events);

        invokeAccept(events);

        assertEquals(1, toKeepAlive.size());
        assertTrue(toKeepAlive.contains(session));

        channel.close();
    }

    @Test
    public void testAcceptDoesNotAddNullSessionToKeepAliveList() throws Exception {
        Events events = createEventsInstance();
        OrderEntry orderEntry = getOrderEntryField(events);

        org.mockito.Mockito.when(orderEntry.accept()).thenReturn(null);

        List<Session> toKeepAlive = getToKeepAliveList(events);

        invokeAccept(events);

        assertEquals(0, toKeepAlive.size());
    }

    @Test
    public void testAcceptHandlesIOExceptionFromOrderEntry() throws Exception {
        Events events = createEventsInstance();
        OrderEntry orderEntry = getOrderEntryField(events);

        org.mockito.Mockito.when(orderEntry.accept()).thenThrow(new IOException());

        List<Session> toKeepAlive = getToKeepAliveList(events);

        invokeAccept(events);

        assertEquals(0, toKeepAlive.size());
    }

    @Test
    public void testAcceptRegistersSessionChannelWithSelector() throws Exception {
        Events events = createEventsInstance();
        OrderEntry orderEntry = getOrderEntryField(events);
        Selector selector = getSelectorField(events);

        com.paritytrading.nassau.soupbintcp.SoupBinTCPServer sessionTransport = mock(com.paritytrading.nassau.soupbintcp.SoupBinTCPServer.class);
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        org.mockito.Mockito.when(sessionTransport.getChannel()).thenReturn(channel);

        Session session = mock(Session.class);
        org.mockito.Mockito.when(session.getTransport()).thenReturn(sessionTransport);
        org.mockito.Mockito.when(orderEntry.accept()).thenReturn(session);

        int initialKeys = selector.keys().size();

        invokeAccept(events);

        assertEquals(initialKeys + 1, selector.keys().size());

        channel.close();
    }
}
