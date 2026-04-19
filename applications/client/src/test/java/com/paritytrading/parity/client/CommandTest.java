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

import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CommandTest {

    @Test
    void errorsCommandGetName() {
        Command command = new ErrorsCommand();

        assertEquals("errors", command.getName());
    }

    @Test
    void errorsCommandGetDescription() {
        Command command = new ErrorsCommand();

        assertEquals("Display occurred errors", command.getDescription());
    }

    @Test
    void errorsCommandGetUsage() {
        Command command = new ErrorsCommand();

        assertEquals("errors", command.getUsage());
    }

    @Test
    void helpCommandGetName() {
        Command command = new HelpCommand();

        assertEquals("help", command.getName());
    }

    @Test
    void helpCommandGetDescription() {
        Command command = new HelpCommand();

        assertEquals("Display the help", command.getDescription());
    }

    @Test
    void helpCommandGetUsage() {
        Command command = new HelpCommand();

        assertEquals("help [command]", command.getUsage());
    }

    @Test
    void exitCommandGetName() {
        Command command = new ExitCommand();

        assertEquals("exit", command.getName());
    }

    @Test
    void exitCommandGetDescription() {
        Command command = new ExitCommand();

        assertEquals("Exit the client", command.getDescription());
    }

    @Test
    void exitCommandGetUsage() {
        Command command = new ExitCommand();

        assertEquals("exit", command.getUsage());
    }

    @Test
    void exitCommandExecute() throws IOException {
        Command command = new ExitCommand();
        TerminalClient client = Mockito.mock(TerminalClient.class);
        Scanner arguments = new Scanner("");

        command.execute(client, arguments);

        Mockito.verify(client).close();
    }

    @Test
    void exitCommandExecuteWithArguments() {
        Command command = new ExitCommand();
        TerminalClient client = Mockito.mock(TerminalClient.class);
        Scanner arguments = new Scanner("unexpected");

        assertThrows(IllegalArgumentException.class, () -> command.execute(client, arguments));
    }
}
