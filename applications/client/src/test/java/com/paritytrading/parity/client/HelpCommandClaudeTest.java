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
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpCommandClaudeTest {

    private HelpCommand command;
    private TerminalClient client;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        command = new HelpCommand();
        client = mock(TerminalClient.class);

        // Capture System.out
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    void testConstructor() {
        HelpCommand newCommand = new HelpCommand();
        assertNotNull(newCommand);
    }

    @Test
    void testGetName() {
        assertEquals("help", command.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Display the help", command.getDescription());
    }

    @Test
    void testGetUsage() {
        assertEquals("help [command]", command.getUsage());
    }

    @Test
    void testExecuteWithNoArguments() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Type 'help <command>' for command specific help."));
    }

    @Test
    void testExecuteWithValidCommandArgument() throws IOException {
        Scanner scanner = new Scanner("exit");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("exit"));
    }

    @Test
    void testExecuteWithInvalidCommandArgument() throws IOException {
        Scanner scanner = new Scanner("invalidcommand");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Type 'help <command>' for command specific help."));
    }

    @Test
    void testExecuteWithTooManyArguments() {
        Scanner scanner = new Scanner("exit extraArg");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testCommandImplementsCommandInterface() {
        assertTrue(command instanceof Command);
    }

    @Test
    void testGetNameReturnsCorrectValue() {
        String name = command.getName();
        assertNotNull(name);
        assertEquals("help", name);
    }

    @Test
    void testGetDescriptionReturnsCorrectValue() {
        String description = command.getDescription();
        assertNotNull(description);
        assertEquals("Display the help", description);
    }

    @Test
    void testGetUsageReturnsCorrectValue() {
        String usage = command.getUsage();
        assertNotNull(usage);
        assertEquals("help [command]", usage);
    }

    @Test
    void testGetNameConsistency() {
        String name1 = command.getName();
        String name2 = command.getName();
        assertEquals(name1, name2);
    }

    @Test
    void testGetDescriptionConsistency() {
        String description1 = command.getDescription();
        String description2 = command.getDescription();
        assertEquals(description1, description2);
    }

    @Test
    void testGetUsageConsistency() {
        String usage1 = command.getUsage();
        String usage2 = command.getUsage();
        assertEquals(usage1, usage2);
    }

    @Test
    void testGetNameReturnsSameInstance() {
        String name1 = command.getName();
        String name2 = command.getName();
        assertSame(name1, name2);
    }

    @Test
    void testGetDescriptionReturnsSameInstance() {
        String description1 = command.getDescription();
        String description2 = command.getDescription();
        assertSame(description1, description2);
    }

    @Test
    void testGetUsageReturnsSameInstance() {
        String usage1 = command.getUsage();
        String usage2 = command.getUsage();
        assertSame(usage1, usage2);
    }

    @Test
    void testGetNameNotEmpty() {
        String name = command.getName();
        assertFalse(name.isEmpty());
    }

    @Test
    void testGetDescriptionNotEmpty() {
        String description = command.getDescription();
        assertFalse(description.isEmpty());
    }

    @Test
    void testGetUsageNotEmpty() {
        String usage = command.getUsage();
        assertFalse(usage.isEmpty());
    }

    @Test
    void testExecuteWithWhitespaceOnlyArguments() throws IOException {
        Scanner scanner = new Scanner("   ");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
    }

    @Test
    void testExecuteDisplaysAllCommands() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

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

    @Test
    void testExecuteWithHelpCommand() throws IOException {
        Scanner scanner = new Scanner("help");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage: help [command]"));
        assertTrue(output.contains("Display the help"));
    }

    @Test
    void testExecuteWithBuyCommand() throws IOException {
        Scanner scanner = new Scanner("buy");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("buy"));
    }

    @Test
    void testExecuteWithSellCommand() throws IOException {
        Scanner scanner = new Scanner("sell");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("sell"));
    }

    @Test
    void testExecuteWithCancelCommand() throws IOException {
        Scanner scanner = new Scanner("cancel");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("cancel"));
    }

    @Test
    void testExecuteWithOrdersCommand() throws IOException {
        Scanner scanner = new Scanner("orders");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("orders"));
    }

    @Test
    void testExecuteWithTradesCommand() throws IOException {
        Scanner scanner = new Scanner("trades");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("trades"));
    }

    @Test
    void testExecuteWithErrorsCommand() throws IOException {
        Scanner scanner = new Scanner("errors");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("errors"));
    }

    @Test
    void testExecuteWithExitCommand() throws IOException {
        Scanner scanner = new Scanner("exit");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("exit"));
    }

    @Test
    void testExecuteWithMultipleArgumentsThrowsException() {
        Scanner scanner = new Scanner("help extra");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithThreeArgumentsThrowsException() {
        Scanner scanner = new Scanner("help extra another");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteDoesNotThrowWithNoArguments() {
        Scanner scanner = new Scanner("");

        assertDoesNotThrow(() -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteDoesNotThrowWithValidCommand() {
        Scanner scanner = new Scanner("exit");

        assertDoesNotThrow(() -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteDoesNotThrowWithInvalidCommand() {
        Scanner scanner = new Scanner("invalid");

        assertDoesNotThrow(() -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testCommandCanBeReused() throws IOException {
        TerminalClient client1 = mock(TerminalClient.class);
        TerminalClient client2 = mock(TerminalClient.class);

        Scanner scanner1 = new Scanner("");
        command.execute(client1, scanner1);

        Scanner scanner2 = new Scanner("exit");
        command.execute(client2, scanner2);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Usage:"));
    }

    @Test
    void testExecuteWithCaseVariations() throws IOException {
        // Command names are case-sensitive, so this should show general help
        Scanner scanner = new Scanner("EXIT");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
    }

    @Test
    void testExecuteWithNumericArgument() throws IOException {
        Scanner scanner = new Scanner("123");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
    }

    @Test
    void testExecuteWithSpecialCharacterArgument() throws IOException {
        Scanner scanner = new Scanner("!@#$%");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
    }

    @Test
    void testExecuteGeneralHelpContainsFormattedOutput() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("\n"));
        assertTrue(output.contains("Type 'help <command>' for command specific help."));
    }

    @Test
    void testExecuteCommandSpecificHelpContainsUsageAndDescription() throws IOException {
        Scanner scanner = new Scanner("exit");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("Exit the client"));
    }

    @Test
    void testExecuteWithTrailingSpacesAfterCommand() throws IOException {
        Scanner scanner = new Scanner("exit   ");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("exit"));
    }

    @Test
    void testExecuteWithLeadingSpacesBeforeCommand() throws IOException {
        Scanner scanner = new Scanner("   exit");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Usage:"));
        assertTrue(output.contains("exit"));
    }

    @Test
    void testExecuteMultipleTimes() throws IOException {
        Scanner scanner1 = new Scanner("");
        command.execute(client, scanner1);

        Scanner scanner2 = new Scanner("exit");
        command.execute(client, scanner2);

        Scanner scanner3 = new Scanner("");
        command.execute(client, scanner3);

        String output = outputStream.toString();
        // Should contain general help output twice and command specific help once
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Usage:"));
    }

    @Test
    void testExecuteWithEmptyStringArgument() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Commands:"));
        assertFalse(output.isEmpty());
    }
}
