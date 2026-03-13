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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.paritytrading.parity.net.poe.POEClientListener;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TradesCommandTest {

    @Test
    public void testGetUsage() {
        TradesCommand command = new TradesCommand();
        String usage = command.getUsage();
        assertEquals("trades", usage);
    }

    @Test
    public void testGetDescription() {
        TradesCommand command = new TradesCommand();
        String description = command.getDescription();
        assertEquals("Display occurred trades", description);
    }

    @Test
    public void testGetName() {
        TradesCommand command = new TradesCommand();
        String name = command.getName();
        assertEquals("trades", name);
    }

    @Test
    public void testExecuteWithNoArguments() throws Exception {
        TradesCommand command = new TradesCommand();

        Events events = new Events();
        Instruments instruments = createInstruments(10, 8);

        SocketChannel channel = null;
        Selector selector = null;
        OrderEntry orderEntry = null;

        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            POEClientListener listener = Mockito.mock(POEClientListener.class);
            orderEntry = createOrderEntry(selector, channel, listener);

            TerminalClient client = createTerminalClient(events, orderEntry, instruments);
            Scanner scanner = new Scanner("");

            assertDoesNotThrow(() -> command.execute(client, scanner));
        } finally {
            if (orderEntry != null) orderEntry.close();
            if (channel != null && channel.isOpen()) channel.close();
            if (selector != null && selector.isOpen()) selector.close();
        }
    }

    @Test
    public void testExecuteWithArguments() throws Exception {
        TradesCommand command = new TradesCommand();

        Events events = new Events();
        Instruments instruments = createInstruments(10, 8);

        SocketChannel channel = null;
        Selector selector = null;
        OrderEntry orderEntry = null;

        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            POEClientListener listener = Mockito.mock(POEClientListener.class);
            orderEntry = createOrderEntry(selector, channel, listener);

            TerminalClient client = createTerminalClient(events, orderEntry, instruments);
            Scanner scanner = new Scanner("extra argument");

            assertThrows(IllegalArgumentException.class, () -> command.execute(client, scanner));
        } finally {
            if (orderEntry != null) orderEntry.close();
            if (channel != null && channel.isOpen()) channel.close();
            if (selector != null && selector.isOpen()) selector.close();
        }
    }

    @Test
    public void testExecuteWithTrades() throws Exception {
        TradesCommand command = new TradesCommand();

        Events events = new Events();
        Instruments instruments = createInstruments(12, 10);

        SocketChannel channel = null;
        Selector selector = null;
        OrderEntry orderEntry = null;

        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            POEClientListener listener = Mockito.mock(POEClientListener.class);
            orderEntry = createOrderEntry(selector, channel, listener);

            TerminalClient client = createTerminalClient(events, orderEntry, instruments);
            Scanner scanner = new Scanner("");

            assertDoesNotThrow(() -> command.execute(client, scanner));
        } finally {
            if (orderEntry != null) orderEntry.close();
            if (channel != null && channel.isOpen()) channel.close();
            if (selector != null && selector.isOpen()) selector.close();
        }
    }

    private Instruments createInstruments(int priceWidth, int sizeWidth) throws Exception {
        Constructor<Instruments> constructor = Instruments.class.getDeclaredConstructor(
            Instrument[].class, int.class, int.class, int.class, int.class);
        constructor.setAccessible(true);

        Instrument[] instruments = new Instrument[0];
        int priceIntegerDigits = priceWidth;
        int maxPriceFractionDigits = 0;
        int sizeIntegerDigits = sizeWidth;
        int maxSizeFractionDigits = 0;

        return constructor.newInstance(instruments, priceIntegerDigits, maxPriceFractionDigits,
            sizeIntegerDigits, maxSizeFractionDigits);
    }

    private OrderEntry createOrderEntry(Selector selector, SocketChannel channel, POEClientListener listener) throws Exception {
        Constructor<OrderEntry> constructor = OrderEntry.class.getDeclaredConstructor(
            Selector.class, SocketChannel.class, POEClientListener.class);
        constructor.setAccessible(true);
        return constructor.newInstance(selector, channel, listener);
    }

    private TerminalClient createTerminalClient(Events events, OrderEntry orderEntry, Instruments instruments) throws Exception {
        Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
            Events.class, OrderEntry.class, Instruments.class);
        constructor.setAccessible(true);
        return constructor.newInstance(events, orderEntry, instruments);
    }
}
