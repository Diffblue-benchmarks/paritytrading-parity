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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpCommandTest {

    private HelpCommand command;

    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        command = new HelpCommand();

        originalOut  = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void getName() {
        assertEquals("help", command.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Display the help", command.getDescription());
    }

    @Test
    void getUsage() {
        assertEquals("help [command]", command.getUsage());
    }

    @Test
    void executeWithNoArguments() throws Exception {
        Scanner arguments = new Scanner("");

        command.execute(null, arguments);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Type 'help <command>' for command specific help."));
        for (Command cmd : TerminalClient.COMMANDS) {
            assertTrue(output.contains(cmd.getName()));
        }
    }

    @Test
    void executeWithValidCommandName() throws Exception {
        Scanner arguments = new Scanner("help");

        command.execute(null, arguments);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage: help [command]"));
        assertTrue(output.contains("Display the help"));
    }

    @Test
    void executeWithUnknownCommandName() throws Exception {
        Scanner arguments = new Scanner("nonexistent");

        command.execute(null, arguments);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Type 'help <command>' for command specific help."));
    }

    @Test
    void executeWithExtraArguments() {
        Scanner arguments = new Scanner("help extra");

        assertThrows(IllegalArgumentException.class, () -> command.execute(null, arguments));
    }

    @Test
    void generalHelpListsAllCommands() throws Exception {
        Scanner arguments = new Scanner("");

        command.execute(null, arguments);

        String output = outputStream.toString();
        for (String name : TerminalClient.COMMAND_NAMES) {
            assertTrue(output.contains(name), "Missing command: " + name);
        }
    }

    @Test
    void commandHelpShowsUsageAndDescription() throws Exception {
        Scanner arguments = new Scanner("orders");

        command.execute(null, arguments);

        String output = outputStream.toString();
        assertTrue(output.startsWith("Usage:"));
    }
}
