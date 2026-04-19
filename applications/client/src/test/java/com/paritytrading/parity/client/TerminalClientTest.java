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

import com.paritytrading.parity.util.Instruments;
import com.paritytrading.parity.util.OrderIDGenerator;
import com.typesafe.config.Config;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvirtanen.config.Configs;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TerminalClientTest {

    private Events events;
    private OrderEntry orderEntry;
    private Instruments instruments;
    private TerminalClient client;

    @BeforeEach
    void setUp() throws Exception {
        events      = new Events();
        orderEntry  = Mockito.mock(OrderEntry.class);
        instruments = Mockito.mock(Instruments.class);

        Constructor<TerminalClient> ctor = TerminalClient.class.getDeclaredConstructor(
                Events.class, OrderEntry.class, Instruments.class);
        ctor.setAccessible(true);

        client = ctor.newInstance(events, orderEntry, instruments);
    }

    @Test
    void getEvents() {
        assertSame(events, client.getEvents());
    }

    @Test
    void getOrderEntry() {
        assertSame(orderEntry, client.getOrderEntry());
    }

    @Test
    void getInstruments() {
        assertSame(instruments, client.getInstruments());
    }

    @Test
    void getOrderIdGenerator() {
        OrderIDGenerator generator = client.getOrderIdGenerator();

        assertNotNull(generator);
    }

    @Test
    void orderIdGeneratorProducesIds() {
        OrderIDGenerator generator = client.getOrderIdGenerator();

        String first  = generator.next();
        String second = generator.next();

        assertNotNull(first);
        assertNotNull(second);
        assertNotEquals(first, second);
    }

    @Test
    void close() {
        client.close();

        Mockito.verify(orderEntry).close();
    }

    @Test
    void findCommandBuy() {
        Command command = TerminalClient.findCommand("buy");

        assertNotNull(command);
        assertEquals("buy", command.getName());
    }

    @Test
    void findCommandSell() {
        Command command = TerminalClient.findCommand("sell");

        assertNotNull(command);
        assertEquals("sell", command.getName());
    }

    @Test
    void findCommandCancel() {
        Command command = TerminalClient.findCommand("cancel");

        assertNotNull(command);
        assertEquals("cancel", command.getName());
    }

    @Test
    void findCommandOrders() {
        Command command = TerminalClient.findCommand("orders");

        assertNotNull(command);
        assertEquals("orders", command.getName());
    }

    @Test
    void findCommandTrades() {
        Command command = TerminalClient.findCommand("trades");

        assertNotNull(command);
        assertEquals("trades", command.getName());
    }

    @Test
    void findCommandErrors() {
        Command command = TerminalClient.findCommand("errors");

        assertNotNull(command);
        assertEquals("errors", command.getName());
    }

    @Test
    void findCommandHelp() {
        Command command = TerminalClient.findCommand("help");

        assertNotNull(command);
        assertEquals("help", command.getName());
    }

    @Test
    void findCommandExit() {
        Command command = TerminalClient.findCommand("exit");

        assertNotNull(command);
        assertEquals("exit", command.getName());
    }

    @Test
    void findCommandUnknown() {
        Command command = TerminalClient.findCommand("unknown");

        assertNull(command);
    }

    @Test
    void findCommandEmpty() {
        Command command = TerminalClient.findCommand("");

        assertNull(command);
    }

    @Test
    void printf() {
        PrintStream original = System.out;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            TerminalClient.printf("hello %s %d", "world", 42);

            String output = baos.toString();
            assertEquals("hello world 42", output);
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void scan() throws Exception {
        Method scanMethod = TerminalClient.class.getDeclaredMethod("scan", String.class);
        scanMethod.setAccessible(true);

        Scanner scanner = (Scanner) scanMethod.invoke(client, "buy 100 AAPL 50.00");

        assertEquals("buy", scanner.next());
        assertEquals(100.0, scanner.nextDouble());
        assertEquals("AAPL", scanner.next());
        assertEquals(50.0, scanner.nextDouble());
        assertFalse(scanner.hasNext());
    }

    @Test
    void scanUsesLocale() throws Exception {
        Method scanMethod = TerminalClient.class.getDeclaredMethod("scan", String.class);
        scanMethod.setAccessible(true);

        Scanner scanner = (Scanner) scanMethod.invoke(client, "1234.56");

        assertEquals(1234.56, scanner.nextDouble());
    }

    @Test
    void commandNamesArray() {
        String[] names = TerminalClient.COMMAND_NAMES;

        assertEquals(TerminalClient.COMMANDS.length, names.length);

        for (int i = 0; i < names.length; i++) {
            assertEquals(TerminalClient.COMMANDS[i].getName(), names[i]);
        }
    }

    @Test
    void localeIsUS() {
        assertEquals(Locale.US, TerminalClient.LOCALE);
    }

    @Test
    void nanosPerMilli() {
        assertEquals(1_000_000L, TerminalClient.NANOS_PER_MILLI);
    }

    @Test
    void mainWithConfig() throws Exception {
        Config config = Mockito.mock(Config.class);
        Mockito.when(config.getString("order-entry.username")).thenReturn("user");
        Mockito.when(config.getString("order-entry.password")).thenReturn("pass");

        InetAddress address = InetAddress.getLoopbackAddress();
        int port = 1234;
        Instruments mockInstruments = Mockito.mock(Instruments.class);
        TerminalClient mockClient = Mockito.mock(TerminalClient.class);

        try (MockedStatic<Configs> configsMock = Mockito.mockStatic(Configs.class);
             MockedStatic<Instruments> instrumentsMock = Mockito.mockStatic(Instruments.class, Mockito.CALLS_REAL_METHODS);
             MockedStatic<TerminalClient> terminalClientMock = Mockito.mockStatic(TerminalClient.class, Mockito.CALLS_REAL_METHODS)) {

            configsMock.when(() -> Configs.getInetAddress(config, "order-entry.address"))
                    .thenReturn(address);
            configsMock.when(() -> Configs.getPort(config, "order-entry.port"))
                    .thenReturn(port);
            instrumentsMock.when(() -> Instruments.fromConfig(config, "instruments"))
                    .thenReturn(mockInstruments);
            terminalClientMock.when(() -> TerminalClient.open(
                    Mockito.any(InetSocketAddress.class),
                    Mockito.eq("user"),
                    Mockito.eq("pass"),
                    Mockito.eq(mockInstruments)))
                    .thenReturn(mockClient);

            Method mainMethod = TerminalClient.class.getDeclaredMethod("main", Config.class);
            mainMethod.setAccessible(true);
            mainMethod.invoke(null, config);
        }

        Mockito.verify(mockClient).run();
    }
}
