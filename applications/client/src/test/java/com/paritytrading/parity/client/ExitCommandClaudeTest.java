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

import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExitCommandClaudeTest {

    private ExitCommand command;
    private TerminalClient client;

    @BeforeEach
    void setUp() {
        command = new ExitCommand();
        client = mock(TerminalClient.class);
    }

    @Test
    void testConstructor() {
        ExitCommand newCommand = new ExitCommand();
        assertNotNull(newCommand);
    }

    @Test
    void testGetName() {
        assertEquals("exit", command.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Exit the client", command.getDescription());
    }

    @Test
    void testGetUsage() {
        assertEquals("exit", command.getUsage());
    }

    @Test
    void testExecuteWithNoArguments() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, times(1)).close();
    }

    @Test
    void testExecuteWithSingleArgument() {
        Scanner scanner = new Scanner("extraArg");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithMultipleArguments() {
        Scanner scanner = new Scanner("arg1 arg2");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteCallsClose() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, times(1)).close();
    }

    @Test
    void testExecuteDoesNotCallCloseWithArguments() {
        Scanner scanner = new Scanner("arg1");

        try {
            command.execute(client, scanner);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected behavior
        }

        verify(client, never()).close();
    }

    @Test
    void testCommandImplementsCommandInterface() {
        assertTrue(command instanceof Command);
    }

    @Test
    void testGetNameReturnsCorrectValue() {
        String name = command.getName();
        assertNotNull(name);
        assertEquals("exit", name);
    }

    @Test
    void testGetDescriptionReturnsCorrectValue() {
        String description = command.getDescription();
        assertNotNull(description);
        assertEquals("Exit the client", description);
    }

    @Test
    void testGetUsageReturnsCorrectValue() {
        String usage = command.getUsage();
        assertNotNull(usage);
        assertEquals("exit", usage);
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
    void testExecuteWithNumericArgument() {
        Scanner scanner = new Scanner("123");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithSpecialCharacterArgument() {
        Scanner scanner = new Scanner("!@#$%");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithWhitespaceDoesNotThrowException() throws IOException {
        Scanner scanner = new Scanner("   ");

        command.execute(client, scanner);

        verify(client, times(1)).close();
    }

    @Test
    void testExecuteMultipleTimes() throws IOException {
        Scanner scanner1 = new Scanner("");
        command.execute(client, scanner1);

        Scanner scanner2 = new Scanner("");
        command.execute(client, scanner2);

        Scanner scanner3 = new Scanner("");
        command.execute(client, scanner3);

        verify(client, times(3)).close();
    }

    @Test
    void testExecuteDoesNotThrowIOException() {
        Scanner scanner = new Scanner("");

        assertDoesNotThrow(() -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithArgumentsDoesNotCallClose() {
        Scanner scanner = new Scanner("argument");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });

        verify(client, never()).close();
    }

    @Test
    void testExecuteWithMultipleSpaceSeparatedArguments() {
        Scanner scanner = new Scanner("a b c d e");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });

        verify(client, never()).close();
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
    void testExecuteWithTrailingWhitespaceAfterArgument() {
        Scanner scanner = new Scanner("arg   ");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithLeadingWhitespaceBeforeArgument() {
        Scanner scanner = new Scanner("   arg");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testCommandCanBeReused() throws IOException {
        TerminalClient client1 = mock(TerminalClient.class);
        TerminalClient client2 = mock(TerminalClient.class);

        Scanner scanner1 = new Scanner("");
        command.execute(client1, scanner1);

        Scanner scanner2 = new Scanner("");
        command.execute(client2, scanner2);

        verify(client1, times(1)).close();
        verify(client2, times(1)).close();
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
    void testExecuteVerifyNoOtherClientInteractions() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, times(1)).close();
        verifyNoMoreInteractions(client);
    }
}
