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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

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
}
