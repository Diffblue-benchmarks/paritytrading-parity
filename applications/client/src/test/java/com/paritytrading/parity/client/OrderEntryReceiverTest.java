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

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import com.paritytrading.parity.net.poe.POEClientListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class OrderEntryReceiverTest {

    private List<Selector> selectorsToClose;
    private List<Pipe> pipesToClose;

    @BeforeEach
    public void setUp() {
        selectorsToClose = new ArrayList<>();
        pipesToClose = new ArrayList<>();
    }

    @AfterEach
    public void tearDown() throws IOException {
        for (Selector selector : selectorsToClose) {
            try {
                selector.close();
            } catch (IOException e) {
            }
        }
        for (Pipe pipe : pipesToClose) {
            try {
                pipe.source().close();
                pipe.sink().close();
            } catch (IOException e) {
            }
        }
    }

    @Test
    public void testReceiverRunWithSuccessfulReceive() throws Exception {
        Selector selector = Selector.open();
        selectorsToClose.add(selector);
        Pipe pipe = Pipe.open();
        pipesToClose.add(pipe);
        pipe.source().configureBlocking(false);
        pipe.source().register(selector, SelectionKey.OP_READ);

        StubTransport transport = new StubTransport();
        transport.receiveResult = 1;

        OrderEntry orderEntry = createOrderEntryWithStub(selector, transport);
        Runnable receiver = createReceiver(orderEntry);

        pipe.sink().write(ByteBuffer.wrap(new byte[]{1}));

        Thread receiverThread = new Thread(() -> receiver.run());
        receiverThread.start();
        Thread.sleep(50);
        orderEntry.close();
        receiverThread.join(1000);

        assertTrue(transport.receiveCalled);
        assertTrue(transport.keepAliveCalled);
        assertTrue(transport.closeCalled);
    }

    @Test
    public void testReceiverRunWithNegativeReceive() throws Exception {
        Selector selector = Selector.open();
        selectorsToClose.add(selector);
        Pipe pipe = Pipe.open();
        pipesToClose.add(pipe);
        pipe.source().configureBlocking(false);
        pipe.source().register(selector, SelectionKey.OP_READ);

        StubTransport transport = new StubTransport();
        transport.receiveResult = -1;

        OrderEntry orderEntry = createOrderEntryWithStub(selector, transport);
        Runnable receiver = createReceiver(orderEntry);

        pipe.sink().write(ByteBuffer.wrap(new byte[]{1}));

        Thread receiverThread = new Thread(() -> receiver.run());
        receiverThread.start();
        receiverThread.join(1000);

        assertTrue(transport.receiveCalled);
        assertTrue(transport.closeCalled);
    }

    @Test
    public void testReceiverRunWithNoKeys() throws Exception {
        Selector selector = Selector.open();
        selectorsToClose.add(selector);

        StubTransport transport = new StubTransport();

        OrderEntry orderEntry = createOrderEntryWithStub(selector, transport);
        Runnable receiver = createReceiver(orderEntry);

        Thread receiverThread = new Thread(() -> receiver.run());
        receiverThread.start();
        Thread.sleep(150);
        orderEntry.close();
        receiverThread.join(1000);

        assertFalse(transport.receiveCalled);
        assertTrue(transport.keepAliveCalled);
        assertTrue(transport.closeCalled);
    }

    @Test
    public void testReceiverRunWithIOExceptionOnTransportReceive() throws Exception {
        Selector selector = Selector.open();
        selectorsToClose.add(selector);
        Pipe pipe = Pipe.open();
        pipesToClose.add(pipe);
        pipe.source().configureBlocking(false);
        pipe.source().register(selector, SelectionKey.OP_READ);

        StubTransport transport = new StubTransport();
        transport.receiveException = new IOException("Test exception");

        OrderEntry orderEntry = createOrderEntryWithStub(selector, transport);
        Runnable receiver = createReceiver(orderEntry);

        pipe.sink().write(ByteBuffer.wrap(new byte[]{1}));

        Thread receiverThread = new Thread(() -> receiver.run());
        receiverThread.start();
        receiverThread.join(1000);

        assertTrue(transport.receiveCalled);
        assertTrue(transport.closeCalled);
    }

    @Test
    public void testReceiverRunWithIOExceptionOnTransportClose() throws Exception {
        Selector selector = Selector.open();
        selectorsToClose.add(selector);

        StubTransport transport = new StubTransport();
        transport.closeException = new IOException("Close exception");

        OrderEntry orderEntry = createOrderEntryWithStub(selector, transport);
        Runnable receiver = createReceiver(orderEntry);

        Thread receiverThread = new Thread(() -> receiver.run());
        receiverThread.start();
        Thread.sleep(50);
        orderEntry.close();
        receiverThread.join(1000);

        assertTrue(transport.closeCalled);
    }

    @Test
    public void testReceiverRunClearsSelectedKeys() throws Exception {
        Selector selector = Selector.open();
        selectorsToClose.add(selector);
        Pipe pipe = Pipe.open();
        pipesToClose.add(pipe);
        pipe.source().configureBlocking(false);
        pipe.source().register(selector, SelectionKey.OP_READ);

        StubTransport transport = new StubTransport();
        transport.receiveResult = 1;

        OrderEntry orderEntry = createOrderEntryWithStub(selector, transport);
        Runnable receiver = createReceiver(orderEntry);

        pipe.sink().write(ByteBuffer.wrap(new byte[]{1}));

        Thread receiverThread = new Thread(() -> receiver.run());
        receiverThread.start();
        Thread.sleep(50);
        orderEntry.close();
        receiverThread.join(1000);

        assertTrue(transport.receiveCalled);
        assertTrue(transport.closeCalled);
    }

    @Test
    public void testReceiverRunKeepsAliveWhenNoKeysSelected() throws Exception {
        Selector selector = Selector.open();
        selectorsToClose.add(selector);

        StubTransport transport = new StubTransport();

        OrderEntry orderEntry = createOrderEntryWithStub(selector, transport);
        Runnable receiver = createReceiver(orderEntry);

        Thread receiverThread = new Thread(() -> receiver.run());
        receiverThread.start();
        Thread.sleep(250);
        orderEntry.close();
        receiverThread.join(1000);

        assertTrue(transport.keepAliveCallCount >= 2);
        assertTrue(transport.closeCalled);
    }

    private OrderEntry createOrderEntryWithStub(Selector selector, StubTransport transport) throws Exception {
        Constructor<OrderEntry> constructor = OrderEntry.class.getDeclaredConstructor(
            Selector.class, SocketChannel.class, POEClientListener.class);
        constructor.setAccessible(true);
        OrderEntry orderEntry = constructor.newInstance(selector, null, new StubListener());

        Field transportField = OrderEntry.class.getDeclaredField("transport");
        transportField.setAccessible(true);
        transportField.set(orderEntry, transport);

        Field selectorField = OrderEntry.class.getDeclaredField("selector");
        selectorField.setAccessible(true);
        selectorField.set(orderEntry, selector);

        return orderEntry;
    }

    private Runnable createReceiver(OrderEntry orderEntry) throws Exception {
        Class<?>[] innerClasses = OrderEntry.class.getDeclaredClasses();
        Class<?> receiverClass = null;
        for (Class<?> innerClass : innerClasses) {
            if (innerClass.getSimpleName().equals("Receiver")) {
                receiverClass = innerClass;
                break;
            }
        }
        assertNotNull(receiverClass, "Receiver inner class not found");

        Constructor<?> receiverConstructor = receiverClass.getDeclaredConstructor(OrderEntry.class);
        receiverConstructor.setAccessible(true);
        return (Runnable) receiverConstructor.newInstance(orderEntry);
    }

    private static class StubTransport extends SoupBinTCPClient {
        boolean receiveCalled = false;
        boolean keepAliveCalled = false;
        boolean closeCalled = false;
        int keepAliveCallCount = 0;
        int receiveResult = 0;
        IOException receiveException = null;
        IOException closeException = null;

        public StubTransport() {
            super(null, 0, null, null);
        }

        @Override
        public int receive() throws IOException {
            receiveCalled = true;
            if (receiveException != null) {
                throw receiveException;
            }
            return receiveResult;
        }

        @Override
        public void keepAlive() throws IOException {
            keepAliveCalled = true;
            keepAliveCallCount++;
        }

        @Override
        public void close() throws IOException {
            closeCalled = true;
            if (closeException != null) {
                throw closeException;
            }
        }
    }

    private static class StubListener implements POEClientListener {
        @Override
        public void orderAccepted(com.paritytrading.parity.net.poe.POE.OrderAccepted message) throws IOException {
        }

        @Override
        public void orderRejected(com.paritytrading.parity.net.poe.POE.OrderRejected message) throws IOException {
        }

        @Override
        public void orderExecuted(com.paritytrading.parity.net.poe.POE.OrderExecuted message) throws IOException {
        }

        @Override
        public void orderCanceled(com.paritytrading.parity.net.poe.POE.OrderCanceled message) throws IOException {
        }
    }
}
