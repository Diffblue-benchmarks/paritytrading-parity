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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.paritytrading.parity.util.Instruments;
import com.paritytrading.parity.util.OrderIDGenerator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.jvirtanen.config.Configs;
import org.jvirtanen.util.Applications;
import org.mockito.MockedStatic;

public class TerminalClientTest {

    @Test
    public void testFindCommandBuy() {
        Command command = TerminalClient.findCommand("buy");
        assertNotNull(command);
        assertEquals("buy", command.getName());
    }

    @Test
    public void testFindCommandSell() {
        Command command = TerminalClient.findCommand("sell");
        assertNotNull(command);
        assertEquals("sell", command.getName());
    }

    @Test
    public void testFindCommandCancel() {
        Command command = TerminalClient.findCommand("cancel");
        assertNotNull(command);
        assertEquals("cancel", command.getName());
    }

    @Test
    public void testFindCommandOrders() {
        Command command = TerminalClient.findCommand("orders");
        assertNotNull(command);
        assertEquals("orders", command.getName());
    }

    @Test
    public void testFindCommandTrades() {
        Command command = TerminalClient.findCommand("trades");
        assertNotNull(command);
        assertEquals("trades", command.getName());
    }

    @Test
    public void testFindCommandErrors() {
        Command command = TerminalClient.findCommand("errors");
        assertNotNull(command);
        assertEquals("errors", command.getName());
    }

    @Test
    public void testFindCommandHelp() {
        Command command = TerminalClient.findCommand("help");
        assertNotNull(command);
        assertEquals("help", command.getName());
    }

    @Test
    public void testFindCommandExit() {
        Command command = TerminalClient.findCommand("exit");
        assertNotNull(command);
        assertEquals("exit", command.getName());
    }

    @Test
    public void testFindCommandUnknown() {
        Command command = TerminalClient.findCommand("unknown");
        assertNull(command);
    }

    @Test
    public void testFindCommandNull() {
        Command command = TerminalClient.findCommand("nonexistent");
        assertNull(command);
    }

    @Test
    public void testFindCommandEmpty() {
        Command command = TerminalClient.findCommand("");
        assertNull(command);
    }

    @Test
    public void testPrintf() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            TerminalClient.printf("Test %s %d\n", "message", 42);
            String output = outputStream.toString();
            assertEquals("Test message 42\n", output);
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testPrintfSimple() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            TerminalClient.printf("Hello\n");
            String output = outputStream.toString();
            assertEquals("Hello\n", output);
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testPrintfWithMultipleArguments() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            TerminalClient.printf("%s: %d %.2f\n", "Value", 100, 3.14);
            String output = outputStream.toString();
            assertEquals("Value: 100 3.14\n", output);
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testCommandNames() {
        assertNotNull(TerminalClient.COMMAND_NAMES);
        assertTrue(TerminalClient.COMMAND_NAMES.length > 0);
        assertEquals(8, TerminalClient.COMMAND_NAMES.length);
    }

    @Test
    public void testCommands() {
        assertNotNull(TerminalClient.COMMANDS);
        assertTrue(TerminalClient.COMMANDS.length > 0);
        assertEquals(8, TerminalClient.COMMANDS.length);
    }

    @Test
    public void testLocale() {
        assertNotNull(TerminalClient.LOCALE);
        assertEquals("en", TerminalClient.LOCALE.getLanguage());
        assertEquals("US", TerminalClient.LOCALE.getCountry());
    }

    @Test
    public void testNanosPerMilli() {
        assertEquals(1_000_000, TerminalClient.NANOS_PER_MILLI);
    }

    @Test
    public void testMainMethodExists() throws Exception {
        java.lang.reflect.Method mainMethod = TerminalClient.class.getDeclaredMethod("main", Config.class);
        assertNotNull(mainMethod);
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isPrivate(mainMethod.getModifiers()));
    }

    @Test
    public void testMainWithNullConfig() throws Exception {
        java.lang.reflect.Method mainMethod = TerminalClient.class.getDeclaredMethod("main", Config.class);
        mainMethod.setAccessible(true);

        assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            mainMethod.invoke(null, (Config) null);
        });
    }

    @Test
    public void testMainWithMissingUsernameConfig() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("order-entry.address", "127.0.0.1");
        configMap.put("order-entry.port", 8080);
        configMap.put("order-entry.password", "testpass");

        Config config = ConfigFactory.parseMap(configMap);

        java.lang.reflect.Method mainMethod = TerminalClient.class.getDeclaredMethod("main", Config.class);
        mainMethod.setAccessible(true);

        assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            mainMethod.invoke(null, config);
        });
    }

    @Test
    public void testMainWithMissingPasswordConfig() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("order-entry.address", "127.0.0.1");
        configMap.put("order-entry.port", 8080);
        configMap.put("order-entry.username", "testuser");

        Config config = ConfigFactory.parseMap(configMap);

        java.lang.reflect.Method mainMethod = TerminalClient.class.getDeclaredMethod("main", Config.class);
        mainMethod.setAccessible(true);

        assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            mainMethod.invoke(null, config);
        });
    }

    @Test
    public void testMainWithCompleteConfigButNoConnection() throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("order-entry.address", "127.0.0.1");
        configMap.put("order-entry.port", 9999);
        configMap.put("order-entry.username", "testuser");
        configMap.put("order-entry.password", "testpass");
        configMap.put("instruments", new HashMap<String, Object>());

        Config config = ConfigFactory.parseMap(configMap);

        java.lang.reflect.Method mainMethod = TerminalClient.class.getDeclaredMethod("main", Config.class);
        mainMethod.setAccessible(true);

        assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            mainMethod.invoke(null, config);
        });
    }

    @Test
    public void testMainWithNoArguments() throws Exception {
        RuntimeException exitException = new RuntimeException("usage called");

        try (MockedStatic<Applications> mockedApplications = mockStatic(Applications.class)) {
            mockedApplications.when(() -> Applications.usage(anyString())).thenThrow(exitException);

            try {
                TerminalClient.main(new String[]{});
            } catch (RuntimeException e) {
                if (e != exitException) throw e;
            }

            mockedApplications.verify(() -> Applications.usage("parity-client <configuration-file>"), times(1));
        }
    }

    @Test
    public void testMainWithMultipleArguments() throws Exception {
        RuntimeException exitException = new RuntimeException("usage called");

        try (MockedStatic<Applications> mockedApplications = mockStatic(Applications.class)) {
            mockedApplications.when(() -> Applications.usage(anyString())).thenThrow(exitException);

            try {
                TerminalClient.main(new String[]{"arg1", "arg2"});
            } catch (RuntimeException e) {
                if (e != exitException) throw e;
            }

            mockedApplications.verify(() -> Applications.usage("parity-client <configuration-file>"), times(1));
        }
    }

    @Test
    public void testMainWithFileNotFoundException() throws Exception {
        RuntimeException errorException = new RuntimeException("error called");

        try (MockedStatic<Applications> mockedApplications = mockStatic(Applications.class)) {
            mockedApplications.when(() -> Applications.config("nonexistent.conf"))
                .thenThrow(new FileNotFoundException("File not found"));
            mockedApplications.when(() -> Applications.error(any(Exception.class))).thenThrow(errorException);

            try {
                TerminalClient.main(new String[]{"nonexistent.conf"});
            } catch (RuntimeException e) {
                if (e != errorException) throw e;
            }

            mockedApplications.verify(() -> Applications.error(any(FileNotFoundException.class)), times(1));
        }
    }

    @Test
    public void testMainWithConfigException() throws Exception {
        RuntimeException errorException = new RuntimeException("error called");

        try (MockedStatic<Applications> mockedApplications = mockStatic(Applications.class)) {
            ConfigException configException = new ConfigException.Missing("test");
            mockedApplications.when(() -> Applications.config("invalid.conf"))
                .thenThrow(configException);
            mockedApplications.when(() -> Applications.error(any(Exception.class))).thenThrow(errorException);

            try {
                TerminalClient.main(new String[]{"invalid.conf"});
            } catch (RuntimeException e) {
                if (e != errorException) throw e;
            }

            mockedApplications.verify(() -> Applications.error(any(ConfigException.class)), times(1));
        }
    }

    @Test
    public void testConstructorInitializesEventsField() throws Exception {
        Events events = new Events();

        Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
            Events.class, OrderEntry.class, Instruments.class);
        constructor.setAccessible(true);

        TerminalClient client = constructor.newInstance(events, null, null);

        Field eventsField = TerminalClient.class.getDeclaredField("events");
        eventsField.setAccessible(true);
        assertEquals(events, eventsField.get(client));
    }

    @Test
    public void testConstructorInitializesOrderIdGenerator() throws Exception {
        Events events = new Events();

        Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
            Events.class, OrderEntry.class, Instruments.class);
        constructor.setAccessible(true);

        TerminalClient client = constructor.newInstance(events, null, null);

        Field orderIdGeneratorField = TerminalClient.class.getDeclaredField("orderIdGenerator");
        orderIdGeneratorField.setAccessible(true);
        Object orderIdGenerator = orderIdGeneratorField.get(client);

        assertNotNull(orderIdGenerator);
        assertTrue(orderIdGenerator instanceof OrderIDGenerator);
    }

    @Test
    public void testConstructorInitializesAllFields() throws Exception {
        Events events = new Events();

        Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
            Events.class, OrderEntry.class, Instruments.class);
        constructor.setAccessible(true);

        TerminalClient client = constructor.newInstance(events, null, null);

        Field eventsField = TerminalClient.class.getDeclaredField("events");
        eventsField.setAccessible(true);
        assertEquals(events, eventsField.get(client));

        Field orderEntryField = TerminalClient.class.getDeclaredField("orderEntry");
        orderEntryField.setAccessible(true);
        assertNull(orderEntryField.get(client));

        Field instrumentsField = TerminalClient.class.getDeclaredField("instruments");
        instrumentsField.setAccessible(true);
        assertNull(instrumentsField.get(client));

        Field orderIdGeneratorField = TerminalClient.class.getDeclaredField("orderIdGenerator");
        orderIdGeneratorField.setAccessible(true);
        assertNotNull(orderIdGeneratorField.get(client));
    }
}
