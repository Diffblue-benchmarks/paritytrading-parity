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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.philadelphia.FIXConnection;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

class EventsTest {

    @Test
    void processRegistersServerChannelAndRunsSelectLoop() throws Exception {
        FIXAcceptor fix = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = mock(ServerSocketChannel.class);
        Selector selector = mock(Selector.class);

        when(fix.getServerChannel()).thenReturn(serverChannel);
        when(serverChannel.register(selector, SelectionKey.OP_ACCEPT)).thenReturn(mock(SelectionKey.class));

        Set<SelectionKey> selectedKeys = new LinkedHashSet<>();
        when(selector.selectedKeys()).thenReturn(selectedKeys);

        when(selector.select(500L))
                .thenReturn(0)
                .thenThrow(new IOException("exit loop"));

        try (MockedStatic<Selector> selectorStatic = mockStatic(Selector.class)) {
            selectorStatic.when(Selector::open).thenReturn(selector);

            IOException thrown = assertThrows(IOException.class, () -> Events.process(fix));
            assertEquals("exit loop", thrown.getMessage());
        }

        verify(serverChannel).register(selector, SelectionKey.OP_ACCEPT);
        verify(selector, times(2)).select(500L);
    }

    @Test
    void processAcceptsNewSessionAndRegistersChannels() throws Exception {
        FIXAcceptor fix = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = mock(ServerSocketChannel.class);
        Selector selector = mock(Selector.class);
        Session session = mock(Session.class);
        FIXConnection fixConnection = mock(FIXConnection.class);
        SoupBinTCPClient orderEntry = mock(SoupBinTCPClient.class);
        SocketChannel fixChannel = mock(SocketChannel.class);
        SocketChannel oeChannel = mock(SocketChannel.class);

        when(fix.getServerChannel()).thenReturn(serverChannel);
        when(serverChannel.register(selector, SelectionKey.OP_ACCEPT)).thenReturn(mock(SelectionKey.class));
        when(fix.accept()).thenReturn(session);
        when(session.getFIX()).thenReturn(fixConnection);
        when(session.getOrderEntry()).thenReturn(orderEntry);
        when(fixConnection.getChannel()).thenReturn(fixChannel);
        when(orderEntry.getChannel()).thenReturn(oeChannel);
        when(fixChannel.register(eq(selector), eq(SelectionKey.OP_READ), any())).thenReturn(mock(SelectionKey.class));
        when(oeChannel.register(eq(selector), eq(SelectionKey.OP_READ), any())).thenReturn(mock(SelectionKey.class));

        SelectionKey acceptKey = mock(SelectionKey.class);
        when(acceptKey.isAcceptable()).thenReturn(true);

        Set<SelectionKey> selectedKeys = new LinkedHashSet<>();
        when(selector.selectedKeys()).thenReturn(selectedKeys);

        when(selector.select(500L)).thenAnswer(inv -> {
            selectedKeys.add(acceptKey);
            return 1;
        }).thenThrow(new IOException("exit loop"));

        try (MockedStatic<Selector> selectorStatic = mockStatic(Selector.class)) {
            selectorStatic.when(Selector::open).thenReturn(selector);

            assertThrows(IOException.class, () -> Events.process(fix));
        }

        verify(fix).accept();
        verify(fixChannel).register(eq(selector), eq(SelectionKey.OP_READ), any());
        verify(oeChannel).register(eq(selector), eq(SelectionKey.OP_READ), any());
        verify(fixConnection).updateCurrentTimestamp();
        verify(orderEntry).keepAlive();
        verify(fixConnection).keepAlive();
    }

    @Test
    void processSkipsNullAcceptedSession() throws Exception {
        FIXAcceptor fix = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = mock(ServerSocketChannel.class);
        Selector selector = mock(Selector.class);

        when(fix.getServerChannel()).thenReturn(serverChannel);
        when(serverChannel.register(selector, SelectionKey.OP_ACCEPT)).thenReturn(mock(SelectionKey.class));
        when(fix.accept()).thenReturn(null);

        SelectionKey acceptKey = mock(SelectionKey.class);
        when(acceptKey.isAcceptable()).thenReturn(true);

        Set<SelectionKey> selectedKeys = new LinkedHashSet<>();
        when(selector.selectedKeys()).thenReturn(selectedKeys);

        when(selector.select(500L)).thenAnswer(inv -> {
            selectedKeys.add(acceptKey);
            return 1;
        }).thenThrow(new IOException("exit loop"));

        try (MockedStatic<Selector> selectorStatic = mockStatic(Selector.class)) {
            selectorStatic.when(Selector::open).thenReturn(selector);

            assertThrows(IOException.class, () -> Events.process(fix));
        }

        verify(fix).accept();
    }

    @Test
    void processHandlesReadableKeyWithPositiveReceive() throws Exception {
        FIXAcceptor fix = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = mock(ServerSocketChannel.class);
        Selector selector = mock(Selector.class);
        Session session = mock(Session.class);
        FIXConnection fixConnection = mock(FIXConnection.class);
        SoupBinTCPClient orderEntry = mock(SoupBinTCPClient.class);
        SocketChannel fixChannel = mock(SocketChannel.class);
        SocketChannel oeChannel = mock(SocketChannel.class);

        when(fix.getServerChannel()).thenReturn(serverChannel);
        when(serverChannel.register(selector, SelectionKey.OP_ACCEPT)).thenReturn(mock(SelectionKey.class));
        when(fix.accept()).thenReturn(session);
        when(session.getFIX()).thenReturn(fixConnection);
        when(session.getOrderEntry()).thenReturn(orderEntry);
        when(fixConnection.getChannel()).thenReturn(fixChannel);
        when(orderEntry.getChannel()).thenReturn(oeChannel);

        ArgumentCaptor<Object> fixReceiverCaptor = ArgumentCaptor.forClass(Object.class);
        when(fixChannel.register(eq(selector), eq(SelectionKey.OP_READ), fixReceiverCaptor.capture()))
                .thenReturn(mock(SelectionKey.class));
        when(oeChannel.register(eq(selector), eq(SelectionKey.OP_READ), any())).thenReturn(mock(SelectionKey.class));

        when(fixConnection.receive()).thenReturn(100);

        SelectionKey acceptKey = mock(SelectionKey.class);
        when(acceptKey.isAcceptable()).thenReturn(true);

        SelectionKey readKey = mock(SelectionKey.class);
        when(readKey.isAcceptable()).thenReturn(false);

        Set<SelectionKey> selectedKeys = new LinkedHashSet<>();
        when(selector.selectedKeys()).thenReturn(selectedKeys);

        AtomicInteger selectCount = new AtomicInteger(0);
        when(selector.select(500L)).thenAnswer(inv -> {
            int count = selectCount.incrementAndGet();
            if (count == 1) {
                selectedKeys.add(acceptKey);
                return 1;
            }
            if (count == 2) {
                when(readKey.attachment()).thenReturn(fixReceiverCaptor.getValue());
                selectedKeys.add(readKey);
                return 1;
            }
            throw new IOException("exit loop");
        });

        try (MockedStatic<Selector> selectorStatic = mockStatic(Selector.class)) {
            selectorStatic.when(Selector::open).thenReturn(selector);

            assertThrows(IOException.class, () -> Events.process(fix));
        }

        verify(fixConnection, atLeastOnce()).receive();
    }

    @Test
    void processClosesReceiverOnNegativeReceive() throws Exception {
        FIXAcceptor fix = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = mock(ServerSocketChannel.class);
        Selector selector = mock(Selector.class);
        Session session = mock(Session.class);
        FIXConnection fixConnection = mock(FIXConnection.class);
        SoupBinTCPClient orderEntry = mock(SoupBinTCPClient.class);
        SocketChannel fixChannel = mock(SocketChannel.class);
        SocketChannel oeChannel = mock(SocketChannel.class);

        when(fix.getServerChannel()).thenReturn(serverChannel);
        when(serverChannel.register(selector, SelectionKey.OP_ACCEPT)).thenReturn(mock(SelectionKey.class));
        when(fix.accept()).thenReturn(session);
        when(session.getFIX()).thenReturn(fixConnection);
        when(session.getOrderEntry()).thenReturn(orderEntry);
        when(fixConnection.getChannel()).thenReturn(fixChannel);
        when(orderEntry.getChannel()).thenReturn(oeChannel);

        ArgumentCaptor<Object> fixReceiverCaptor = ArgumentCaptor.forClass(Object.class);
        when(fixChannel.register(eq(selector), eq(SelectionKey.OP_READ), fixReceiverCaptor.capture()))
                .thenReturn(mock(SelectionKey.class));
        when(oeChannel.register(eq(selector), eq(SelectionKey.OP_READ), any())).thenReturn(mock(SelectionKey.class));

        when(fixConnection.receive()).thenReturn(-1);

        SelectionKey acceptKey = mock(SelectionKey.class);
        when(acceptKey.isAcceptable()).thenReturn(true);

        SelectionKey readKey = mock(SelectionKey.class);
        when(readKey.isAcceptable()).thenReturn(false);

        Set<SelectionKey> selectedKeys = new LinkedHashSet<>();
        when(selector.selectedKeys()).thenReturn(selectedKeys);

        AtomicInteger selectCount = new AtomicInteger(0);
        when(selector.select(500L)).thenAnswer(inv -> {
            int count = selectCount.incrementAndGet();
            if (count == 1) {
                selectedKeys.add(acceptKey);
                return 1;
            }
            if (count == 2) {
                when(readKey.attachment()).thenReturn(fixReceiverCaptor.getValue());
                selectedKeys.add(readKey);
                return 1;
            }
            throw new IOException("exit loop");
        });

        try (MockedStatic<Selector> selectorStatic = mockStatic(Selector.class)) {
            selectorStatic.when(Selector::open).thenReturn(selector);

            assertThrows(IOException.class, () -> Events.process(fix));
        }

        verify(fixConnection, atLeastOnce()).receive();
        verify(session, atLeastOnce()).close();
    }

    @Test
    void processClosesReceiverOnReceiveException() throws Exception {
        FIXAcceptor fix = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = mock(ServerSocketChannel.class);
        Selector selector = mock(Selector.class);
        Session session = mock(Session.class);
        FIXConnection fixConnection = mock(FIXConnection.class);
        SoupBinTCPClient orderEntry = mock(SoupBinTCPClient.class);
        SocketChannel fixChannel = mock(SocketChannel.class);
        SocketChannel oeChannel = mock(SocketChannel.class);

        when(fix.getServerChannel()).thenReturn(serverChannel);
        when(serverChannel.register(selector, SelectionKey.OP_ACCEPT)).thenReturn(mock(SelectionKey.class));
        when(fix.accept()).thenReturn(session);
        when(session.getFIX()).thenReturn(fixConnection);
        when(session.getOrderEntry()).thenReturn(orderEntry);
        when(fixConnection.getChannel()).thenReturn(fixChannel);
        when(orderEntry.getChannel()).thenReturn(oeChannel);

        ArgumentCaptor<Object> fixReceiverCaptor = ArgumentCaptor.forClass(Object.class);
        when(fixChannel.register(eq(selector), eq(SelectionKey.OP_READ), fixReceiverCaptor.capture()))
                .thenReturn(mock(SelectionKey.class));
        when(oeChannel.register(eq(selector), eq(SelectionKey.OP_READ), any())).thenReturn(mock(SelectionKey.class));

        when(fixConnection.receive()).thenThrow(new IOException("receive failed"));

        SelectionKey acceptKey = mock(SelectionKey.class);
        when(acceptKey.isAcceptable()).thenReturn(true);

        SelectionKey readKey = mock(SelectionKey.class);
        when(readKey.isAcceptable()).thenReturn(false);

        Set<SelectionKey> selectedKeys = new LinkedHashSet<>();
        when(selector.selectedKeys()).thenReturn(selectedKeys);

        AtomicInteger selectCount = new AtomicInteger(0);
        when(selector.select(500L)).thenAnswer(inv -> {
            int count = selectCount.incrementAndGet();
            if (count == 1) {
                selectedKeys.add(acceptKey);
                return 1;
            }
            if (count == 2) {
                when(readKey.attachment()).thenReturn(fixReceiverCaptor.getValue());
                selectedKeys.add(readKey);
                return 1;
            }
            throw new IOException("exit loop");
        });

        try (MockedStatic<Selector> selectorStatic = mockStatic(Selector.class)) {
            selectorStatic.when(Selector::open).thenReturn(selector);

            assertThrows(IOException.class, () -> Events.process(fix));
        }

        verify(fixConnection, atLeastOnce()).receive();
        verify(session, atLeastOnce()).close();
    }

    @Test
    void processHandlesKeepAliveIOException() throws Exception {
        FIXAcceptor fix = mock(FIXAcceptor.class);
        ServerSocketChannel serverChannel = mock(ServerSocketChannel.class);
        Selector selector = mock(Selector.class);
        Session session = mock(Session.class);
        FIXConnection fixConnection = mock(FIXConnection.class);
        SoupBinTCPClient orderEntry = mock(SoupBinTCPClient.class);
        SocketChannel fixChannel = mock(SocketChannel.class);
        SocketChannel oeChannel = mock(SocketChannel.class);

        when(fix.getServerChannel()).thenReturn(serverChannel);
        when(serverChannel.register(selector, SelectionKey.OP_ACCEPT)).thenReturn(mock(SelectionKey.class));
        when(fix.accept()).thenReturn(session);
        when(session.getFIX()).thenReturn(fixConnection);
        when(session.getOrderEntry()).thenReturn(orderEntry);
        when(fixConnection.getChannel()).thenReturn(fixChannel);
        when(orderEntry.getChannel()).thenReturn(oeChannel);
        when(fixChannel.register(eq(selector), eq(SelectionKey.OP_READ), any())).thenReturn(mock(SelectionKey.class));
        when(oeChannel.register(eq(selector), eq(SelectionKey.OP_READ), any())).thenReturn(mock(SelectionKey.class));

        doNothing().when(fixConnection).updateCurrentTimestamp();
        doThrow(new IOException("keepAlive failed")).when(orderEntry).keepAlive();

        SelectionKey acceptKey = mock(SelectionKey.class);
        when(acceptKey.isAcceptable()).thenReturn(true);

        Set<SelectionKey> selectedKeys = new LinkedHashSet<>();
        when(selector.selectedKeys()).thenReturn(selectedKeys);

        AtomicInteger selectCount = new AtomicInteger(0);
        when(selector.select(500L)).thenAnswer(inv -> {
            int count = selectCount.incrementAndGet();
            if (count == 1) {
                selectedKeys.add(acceptKey);
                return 1;
            }
            throw new IOException("exit loop");
        });

        try (MockedStatic<Selector> selectorStatic = mockStatic(Selector.class)) {
            selectorStatic.when(Selector::open).thenReturn(selector);

            assertThrows(IOException.class, () -> Events.process(fix));
        }

        verify(orderEntry).keepAlive();
        verify(session).close();
    }
}
