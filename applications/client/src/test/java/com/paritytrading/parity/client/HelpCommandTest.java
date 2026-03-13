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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class HelpCommandTest {

    private HelpCommand helpCommand;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        helpCommand = new HelpCommand();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testGetName() {
        assertEquals("help", helpCommand.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Display the help", helpCommand.getDescription());
    }

    @Test
    public void testGetUsage() {
        assertEquals("help [command]", helpCommand.getUsage());
    }

    @Test
    public void testExecuteWithNoArguments() {
        Scanner scanner = new Scanner("");

        helpCommand.execute(null, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Type 'help <command>' for command specific help."));
    }

    @Test
    public void testExecuteWithValidCommandArgument() {
        Scanner scanner = new Scanner("help");

        helpCommand.execute(null, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage: help [command]"));
        assertTrue(output.contains("Display the help"));
    }

    @Test
    public void testExecuteWithInvalidCommandArgument() {
        Scanner scanner = new Scanner("invalid");

        helpCommand.execute(null, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Type 'help <command>' for command specific help."));
    }

    @Test
    public void testExecuteWithTooManyArguments() {
        Scanner scanner = new Scanner("help extra");

        assertThrows(IllegalArgumentException.class, () -> {
            helpCommand.execute(null, scanner);
        });
    }

    @Test
    public void testExecuteDisplaysAllCommands() {
        Scanner scanner = new Scanner("");

        helpCommand.execute(null, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("buy"));
        assertTrue(output.contains("sell"));
        assertTrue(output.contains("cancel"));
        assertTrue(output.contains("orders"));
        assertTrue(output.contains("trades"));
        assertTrue(output.contains("errors"));
        assertTrue(output.contains("help"));
        assertTrue(output.contains("exit"));
    }
}
