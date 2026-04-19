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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.parity.util.OrderIDGenerator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Scanner;
import java.nio.channels.ClosedChannelException;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvirtanen.config.Configs;
import org.jvirtanen.util.Applications;
import org.mockito.ArgumentCaptor;
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
    void mainWithNoArgs() throws Exception {
        try (MockedStatic<Applications> appMock = Mockito.mockStatic(Applications.class)) {
            RuntimeException exitException = new RuntimeException("exit");
            appMock.when(() -> Applications.usage(Mockito.anyString())).thenThrow(exitException);

            RuntimeException thrown = assertThrows(RuntimeException.class,
                    () -> TerminalClient.main(new String[]{}));

            assertSame(exitException, thrown);
            appMock.verify(() -> Applications.usage("parity-client <configuration-file>"));
        }
    }

    @Test
    void mainWithTooManyArgs() throws Exception {
        try (MockedStatic<Applications> appMock = Mockito.mockStatic(Applications.class)) {
            RuntimeException exitException = new RuntimeException("exit");
            appMock.when(() -> Applications.usage(Mockito.anyString())).thenThrow(exitException);

            RuntimeException thrown = assertThrows(RuntimeException.class,
                    () -> TerminalClient.main(new String[]{"a", "b"}));

            assertSame(exitException, thrown);
            appMock.verify(() -> Applications.usage("parity-client <configuration-file>"));
        }
    }

    @Test
    void mainWithConfigException() throws Exception {
        try (MockedStatic<Applications> appMock = Mockito.mockStatic(Applications.class)) {
            ConfigException configEx = new ConfigException.Missing("some.path");
            appMock.when(() -> Applications.config("bad.conf")).thenThrow(configEx);

            TerminalClient.main(new String[]{"bad.conf"});

            appMock.verify(() -> Applications.error(configEx));
        }
    }

    @Test
    void mainWithFileNotFound() throws Exception {
        try (MockedStatic<Applications> appMock = Mockito.mockStatic(Applications.class)) {
            FileNotFoundException fnfe = new FileNotFoundException("missing.conf");
            appMock.when(() -> Applications.config("missing.conf")).thenThrow(fnfe);

            TerminalClient.main(new String[]{"missing.conf"});

            appMock.verify(() -> Applications.error(fnfe));
        }
    }

    @Test
    void mainWithEndOfFileException() throws Exception {
        try (MockedStatic<Applications> appMock = Mockito.mockStatic(Applications.class)) {
            appMock.when(() -> Applications.config("test.conf"))
                    .thenThrow(new EndOfFileException());

            TerminalClient.main(new String[]{"test.conf"});

            appMock.verify(() -> Applications.error(Mockito.any(Throwable.class)), Mockito.never());
        }
    }

    @Test
    void mainWithUserInterruptException() throws Exception {
        try (MockedStatic<Applications> appMock = Mockito.mockStatic(Applications.class)) {
            appMock.when(() -> Applications.config("test.conf"))
                    .thenThrow(new UserInterruptException(""));

            TerminalClient.main(new String[]{"test.conf"});

            appMock.verify(() -> Applications.error(Mockito.any(Throwable.class)), Mockito.never());
        }
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

    @Test
    void runWithNullLineExitsLoop() throws Exception {
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(new ByteArrayOutputStream()));

            try (MockedStatic<LineReaderBuilder> builderMock = Mockito.mockStatic(LineReaderBuilder.class)) {
                LineReaderBuilder mockBuilder = Mockito.mock(LineReaderBuilder.class);
                LineReader mockReader = Mockito.mock(LineReader.class);

                builderMock.when(LineReaderBuilder::builder).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.completer(Mockito.any())).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.build()).thenReturn(mockReader);

                Mockito.when(mockReader.readLine("> ")).thenReturn(null);

                client.run();

                Mockito.verify(orderEntry).close();
            }
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void runWithEmptyLineContinues() throws Exception {
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(new ByteArrayOutputStream()));

            try (MockedStatic<LineReaderBuilder> builderMock = Mockito.mockStatic(LineReaderBuilder.class)) {
                LineReaderBuilder mockBuilder = Mockito.mock(LineReaderBuilder.class);
                LineReader mockReader = Mockito.mock(LineReader.class);

                builderMock.when(LineReaderBuilder::builder).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.completer(Mockito.any())).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.build()).thenReturn(mockReader);

                Mockito.when(mockReader.readLine("> "))
                        .thenReturn("")
                        .thenReturn(null);

                client.run();

                Mockito.verify(mockReader, Mockito.times(2)).readLine("> ");
            }
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void runWithUnknownCommandPrintsError() throws Exception {
        PrintStream original = System.out;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            try (MockedStatic<LineReaderBuilder> builderMock = Mockito.mockStatic(LineReaderBuilder.class)) {
                LineReaderBuilder mockBuilder = Mockito.mock(LineReaderBuilder.class);
                LineReader mockReader = Mockito.mock(LineReader.class);

                builderMock.when(LineReaderBuilder::builder).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.completer(Mockito.any())).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.build()).thenReturn(mockReader);

                Mockito.when(mockReader.readLine("> "))
                        .thenReturn("unknowncmd")
                        .thenReturn(null);

                client.run();

                String output = baos.toString();
                assertTrue(output.contains("error: Unknown command"));
            }
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void runWithHelpCommand() throws Exception {
        PrintStream original = System.out;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            try (MockedStatic<LineReaderBuilder> builderMock = Mockito.mockStatic(LineReaderBuilder.class)) {
                LineReaderBuilder mockBuilder = Mockito.mock(LineReaderBuilder.class);
                LineReader mockReader = Mockito.mock(LineReader.class);

                builderMock.when(LineReaderBuilder::builder).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.completer(Mockito.any())).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.build()).thenReturn(mockReader);

                Mockito.when(mockReader.readLine("> "))
                        .thenReturn("help")
                        .thenReturn(null);

                client.run();

                String output = baos.toString();
                assertTrue(output.contains("Commands:"));
            }
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void runWithExitCommand() throws Exception {
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(new ByteArrayOutputStream()));

            try (MockedStatic<LineReaderBuilder> builderMock = Mockito.mockStatic(LineReaderBuilder.class)) {
                LineReaderBuilder mockBuilder = Mockito.mock(LineReaderBuilder.class);
                LineReader mockReader = Mockito.mock(LineReader.class);

                builderMock.when(LineReaderBuilder::builder).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.completer(Mockito.any())).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.build()).thenReturn(mockReader);

                Mockito.when(mockReader.readLine("> ")).thenReturn("exit");

                client.run();

                Mockito.verify(orderEntry, Mockito.atLeastOnce()).close();
            }
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void runWithIllegalArgumentPrintsUsage() throws Exception {
        PrintStream original = System.out;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            try (MockedStatic<LineReaderBuilder> builderMock = Mockito.mockStatic(LineReaderBuilder.class)) {
                LineReaderBuilder mockBuilder = Mockito.mock(LineReaderBuilder.class);
                LineReader mockReader = Mockito.mock(LineReader.class);

                builderMock.when(LineReaderBuilder::builder).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.completer(Mockito.any())).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.build()).thenReturn(mockReader);

                Mockito.when(mockReader.readLine("> "))
                        .thenReturn("exit foo")
                        .thenReturn(null);

                client.run();

                String output = baos.toString();
                assertTrue(output.contains("Usage: exit"));
            }
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void runWithClosedChannelException() throws Exception {
        PrintStream original = System.out;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            Mockito.doThrow(new ClosedChannelException())
                    .when(orderEntry).send(Mockito.any());

            try (MockedStatic<LineReaderBuilder> builderMock = Mockito.mockStatic(LineReaderBuilder.class)) {
                LineReaderBuilder mockBuilder = Mockito.mock(LineReaderBuilder.class);
                LineReader mockReader = Mockito.mock(LineReader.class);

                builderMock.when(LineReaderBuilder::builder).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.completer(Mockito.any())).thenReturn(mockBuilder);
                Mockito.when(mockBuilder.build()).thenReturn(mockReader);

                Mockito.when(mockReader.readLine("> "))
                        .thenReturn("cancel ORDER123")
                        .thenReturn(null);

                client.run();

                String output = baos.toString();
                assertTrue(output.contains("error: Connection closed"));
            }
        } finally {
            System.setOut(original);
        }
    }

    @Test
    void openCreatesClientAndSendsLogin() throws Exception {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 4000);
        String           username = "user01";
        String           password = "pass012345";
        Instruments      mockInst = Mockito.mock(Instruments.class);

        OrderEntry       mockOrderEntry = Mockito.mock(OrderEntry.class);
        SoupBinTCPClient mockTransport  = Mockito.mock(SoupBinTCPClient.class);
        Mockito.when(mockOrderEntry.getTransport()).thenReturn(mockTransport);

        try (MockedStatic<OrderEntry> orderEntryMock = Mockito.mockStatic(OrderEntry.class)) {
            orderEntryMock.when(() -> OrderEntry.open(Mockito.eq(address), Mockito.any(Events.class)))
                    .thenReturn(mockOrderEntry);

            TerminalClient result = TerminalClient.open(address, username, password, mockInst);

            assertNotNull(result);
            assertSame(mockOrderEntry, result.getOrderEntry());
            assertSame(mockInst, result.getInstruments());
            assertNotNull(result.getEvents());
            assertNotNull(result.getOrderIdGenerator());

            ArgumentCaptor<SoupBinTCP.LoginRequest> captor =
                    ArgumentCaptor.forClass(SoupBinTCP.LoginRequest.class);
            Mockito.verify(mockTransport).login(captor.capture());

            SoupBinTCP.LoginRequest loginRequest = captor.getValue();
            assertEquals(username, ASCII.get(loginRequest.username).trim());
            assertEquals(password, ASCII.get(loginRequest.password).trim());
        }
    }

    @Test
    void openPassesEventsToOrderEntry() throws Exception {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 4001);
        Instruments      mockInst = Mockito.mock(Instruments.class);

        OrderEntry       mockOrderEntry = Mockito.mock(OrderEntry.class);
        SoupBinTCPClient mockTransport  = Mockito.mock(SoupBinTCPClient.class);
        Mockito.when(mockOrderEntry.getTransport()).thenReturn(mockTransport);

        try (MockedStatic<OrderEntry> orderEntryMock = Mockito.mockStatic(OrderEntry.class)) {
            ArgumentCaptor<Events> eventsCaptor = ArgumentCaptor.forClass(Events.class);

            orderEntryMock.when(() -> OrderEntry.open(Mockito.any(InetSocketAddress.class), Mockito.any(Events.class)))
                    .thenReturn(mockOrderEntry);

            TerminalClient result = TerminalClient.open(address, "u", "p", mockInst);

            orderEntryMock.verify(() -> OrderEntry.open(Mockito.eq(address), eventsCaptor.capture()));

            Events passedEvents = eventsCaptor.getValue();
            assertNotNull(passedEvents);
            assertSame(passedEvents, result.getEvents());
        }
    }
}
