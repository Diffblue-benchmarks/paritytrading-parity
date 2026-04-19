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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPServer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventsTest {

    private MarketData      marketData;
    private MarketReporting marketReporting;
    private OrderEntry      orderEntry;

    private MoldUDP64RequestServer mdRequestTransport;
    private MoldUDP64RequestServer mrRequestTransport;
    private MoldUDP64Server        mdTransport;
    private MoldUDP64Server        mrTransport;
    private DatagramChannel        mdChannel;
    private DatagramChannel        mrChannel;
    private ServerSocketChannel    oeChannel;

    @BeforeEach
    void setUp() {
        marketData      = mock(MarketData.class);
        marketReporting = mock(MarketReporting.class);
        orderEntry      = mock(OrderEntry.class);

        mdRequestTransport = mock(MoldUDP64RequestServer.class);
        mrRequestTransport = mock(MoldUDP64RequestServer.class);
        mdTransport        = mock(MoldUDP64Server.class);
        mrTransport        = mock(MoldUDP64Server.class);
        mdChannel          = mock(DatagramChannel.class);
        mrChannel          = mock(DatagramChannel.class);
        oeChannel          = mock(ServerSocketChannel.class);

        when(marketData.getRequestTransport()).thenReturn(mdRequestTransport);
        when(marketData.getTransport()).thenReturn(mdTransport);
        when(marketReporting.getRequestTransport()).thenReturn(mrRequestTransport);
        when(marketReporting.getTransport()).thenReturn(mrTransport);
        when(mdRequestTransport.getChannel()).thenReturn(mdChannel);
        when(mrRequestTransport.getChannel()).thenReturn(mrChannel);
        when(orderEntry.getChannel()).thenReturn(oeChannel);
    }

    private Events createEvents() throws IOException {
        return new Events(marketData, marketReporting, orderEntry);
    }

    private Selector replaceSelector(Events events) throws Exception {
        Selector mockSelector = mock(Selector.class);

        Field field = Events.class.getDeclaredField("selector");
        field.setAccessible(true);

        Selector realSelector = (Selector) field.get(events);
        realSelector.close();

        field.set(events, mockSelector);
        return mockSelector;
    }

    @SuppressWarnings("unchecked")
    private List<Session> getToKeepAlive(Events events) throws Exception {
        Field field = Events.class.getDeclaredField("toKeepAlive");
        field.setAccessible(true);
        return (List<Session>) field.get(events);
    }

    @SuppressWarnings("unchecked")
    private List<Session> getToCleanUp(Events events) throws Exception {
        Field field = Events.class.getDeclaredField("toCleanUp");
        field.setAccessible(true);
        return (List<Session>) field.get(events);
    }

    private Set<SelectionKey> keySet(SelectionKey key) {
        Set<SelectionKey> keys = new HashSet<>();
        keys.add(key);
        return keys;
    }

    @Test
    void constructorRegistersChannels() throws IOException {
        createEvents();

        verify(mdChannel).register(any(Selector.class), eq(SelectionKey.OP_READ), eq(marketData));
        verify(mrChannel).register(any(Selector.class), eq(SelectionKey.OP_READ), eq(marketReporting));
        verify(oeChannel).register(any(Selector.class), eq(SelectionKey.OP_ACCEPT), isNull());
    }

    @Test
    void runExitsOnSelectorIOException() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        when(selector.select(anyLong())).thenThrow(new IOException("test"));

        events.run();

        verify(selector).select(anyLong());
    }

    @Test
    void runAcceptsNewConnection() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        Session session = mock(Session.class);
        SoupBinTCPServer sessionTransport = mock(SoupBinTCPServer.class);
        SocketChannel sessionChannel = mock(SocketChannel.class);
        when(session.getTransport()).thenReturn(sessionTransport);
        when(sessionTransport.getChannel()).thenReturn(sessionChannel);
        when(session.isTerminated()).thenReturn(false);
        when(orderEntry.accept()).thenReturn(session);

        SelectionKey key = mock(SelectionKey.class);
        when(key.isAcceptable()).thenReturn(true);
        when(key.isReadable()).thenReturn(false);

        when(selector.select(anyLong()))
                .thenReturn(1)
                .thenThrow(new IOException("stop"));
        when(selector.selectedKeys()).thenReturn(keySet(key));

        events.run();

        verify(orderEntry).accept();
        verify(sessionChannel).register(selector, SelectionKey.OP_READ, session);
    }

    @Test
    void runAcceptsNullConnection() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        when(orderEntry.accept()).thenReturn(null);

        SelectionKey key = mock(SelectionKey.class);
        when(key.isAcceptable()).thenReturn(true);
        when(key.isReadable()).thenReturn(false);

        when(selector.select(anyLong()))
                .thenReturn(1)
                .thenThrow(new IOException("stop"));
        when(selector.selectedKeys()).thenReturn(keySet(key));

        events.run();

        verify(orderEntry).accept();
    }

    @Test
    void runAcceptIOException() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        when(orderEntry.accept()).thenThrow(new IOException("accept failed"));

        SelectionKey key = mock(SelectionKey.class);
        when(key.isAcceptable()).thenReturn(true);
        when(key.isReadable()).thenReturn(false);

        when(selector.select(anyLong()))
                .thenReturn(1)
                .thenThrow(new IOException("stop"));
        when(selector.selectedKeys()).thenReturn(keySet(key));

        events.run();

        verify(orderEntry).accept();
    }

    @Test
    void runServesMarketData() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        SelectionKey key = mock(SelectionKey.class);
        when(key.isAcceptable()).thenReturn(false);
        when(key.isReadable()).thenReturn(true);
        when(key.attachment()).thenReturn(marketData);

        when(selector.select(anyLong()))
                .thenReturn(1)
                .thenThrow(new IOException("stop"));
        when(selector.selectedKeys()).thenReturn(keySet(key));

        events.run();

        verify(marketData).serve();
    }

    @Test
    void runServesMarketReporting() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        SelectionKey key = mock(SelectionKey.class);
        when(key.isAcceptable()).thenReturn(false);
        when(key.isReadable()).thenReturn(true);
        when(key.attachment()).thenReturn(marketReporting);

        when(selector.select(anyLong()))
                .thenReturn(1)
                .thenThrow(new IOException("stop"));
        when(selector.selectedKeys()).thenReturn(keySet(key));

        events.run();

        verify(marketReporting).serve();
    }

    @Test
    void runReceivesFromSession() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        Session session = mock(Session.class);
        SoupBinTCPServer sessionTransport = mock(SoupBinTCPServer.class);
        when(session.getTransport()).thenReturn(sessionTransport);
        when(sessionTransport.receive()).thenReturn(100);

        SelectionKey key = mock(SelectionKey.class);
        when(key.isAcceptable()).thenReturn(false);
        when(key.isReadable()).thenReturn(true);
        when(key.attachment()).thenReturn(session);

        when(selector.select(anyLong()))
                .thenReturn(1)
                .thenThrow(new IOException("stop"));
        when(selector.selectedKeys()).thenReturn(keySet(key));

        events.run();

        verify(sessionTransport).receive();
        verify(session, never()).close();
    }

    @Test
    void runReceiveNegativeTriggersCleanUp() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        Session session = mock(Session.class);
        SoupBinTCPServer sessionTransport = mock(SoupBinTCPServer.class);
        when(session.getTransport()).thenReturn(sessionTransport);
        when(sessionTransport.receive()).thenReturn(-1);

        SelectionKey key = mock(SelectionKey.class);
        when(key.isAcceptable()).thenReturn(false);
        when(key.isReadable()).thenReturn(true);
        when(key.attachment()).thenReturn(session);

        when(selector.select(anyLong()))
                .thenReturn(1)
                .thenThrow(new IOException("stop"));
        when(selector.selectedKeys()).thenReturn(keySet(key));

        events.run();

        verify(session).close();
    }

    @Test
    void runReceiveIOExceptionTriggersCleanUp() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        Session session = mock(Session.class);
        SoupBinTCPServer sessionTransport = mock(SoupBinTCPServer.class);
        when(session.getTransport()).thenReturn(sessionTransport);
        when(sessionTransport.receive()).thenThrow(new IOException("receive failed"));

        SelectionKey key = mock(SelectionKey.class);
        when(key.isAcceptable()).thenReturn(false);
        when(key.isReadable()).thenReturn(true);
        when(key.attachment()).thenReturn(session);

        when(selector.select(anyLong()))
                .thenReturn(1)
                .thenThrow(new IOException("stop"));
        when(selector.selectedKeys()).thenReturn(keySet(key));

        events.run();

        verify(session).close();
    }

    @Test
    void runKeepAliveSession() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        Session session = mock(Session.class);
        SoupBinTCPServer sessionTransport = mock(SoupBinTCPServer.class);
        when(session.getTransport()).thenReturn(sessionTransport);
        when(session.isTerminated()).thenReturn(false);

        getToKeepAlive(events).add(session);

        when(selector.select(anyLong()))
                .thenReturn(0)
                .thenThrow(new IOException("stop"));

        events.run();

        verify(sessionTransport).keepAlive();
        verify(session).isTerminated();
        verify(session, never()).close();
    }

    @Test
    void runKeepAliveTerminatedSessionCleansUp() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        Session session = mock(Session.class);
        SoupBinTCPServer sessionTransport = mock(SoupBinTCPServer.class);
        when(session.getTransport()).thenReturn(sessionTransport);
        when(session.isTerminated()).thenReturn(true);

        getToKeepAlive(events).add(session);

        when(selector.select(anyLong()))
                .thenReturn(0)
                .thenThrow(new IOException("stop"));

        events.run();

        verify(sessionTransport).keepAlive();
        verify(session).isTerminated();
        verify(session).close();
    }

    @Test
    void runKeepAliveIOExceptionCleansUp() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        Session session = mock(Session.class);
        SoupBinTCPServer sessionTransport = mock(SoupBinTCPServer.class);
        when(session.getTransport()).thenReturn(sessionTransport);
        doThrow(new IOException("keepAlive failed")).when(sessionTransport).keepAlive();

        getToKeepAlive(events).add(session);

        when(selector.select(anyLong()))
                .thenReturn(0)
                .thenThrow(new IOException("stop"));

        events.run();

        verify(session).close();
    }

    @Test
    void runKeepAliveMarketDataIOException() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        doThrow(new IOException("md keepAlive failed")).when(mdTransport).keepAlive();

        when(selector.select(anyLong()))
                .thenReturn(0)
                .thenThrow(new IOException("stop"));

        events.run();

        verify(mdTransport).keepAlive();
        verify(mrTransport).keepAlive();
    }

    @Test
    void runCleanUpRemovesAndClosesSession() throws Exception {
        Events events = createEvents();
        Selector selector = replaceSelector(events);

        Session session = mock(Session.class);
        SoupBinTCPServer sessionTransport = mock(SoupBinTCPServer.class);
        when(session.getTransport()).thenReturn(sessionTransport);
        when(session.isTerminated()).thenReturn(false);

        getToKeepAlive(events).add(session);
        getToCleanUp(events).add(session);

        when(selector.select(anyLong()))
                .thenReturn(0)
                .thenThrow(new IOException("stop"));

        events.run();

        verify(session).close();
        assertTrue(getToKeepAlive(events).isEmpty());
        assertTrue(getToCleanUp(events).isEmpty());
    }
}
