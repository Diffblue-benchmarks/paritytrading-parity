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

import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExitCommandTest {

    private ExitCommand command;

    @BeforeEach
    void setUp() {
        command = new ExitCommand();
    }

    @Test
    void getName() {
        assertEquals("exit", command.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Exit the client", command.getDescription());
    }

    @Test
    void getUsage() {
        assertEquals("exit", command.getUsage());
    }

    @Test
    void executeClosesClient() throws Exception {
        TerminalClient client = Mockito.mock(TerminalClient.class);
        Scanner arguments     = new Scanner("");

        command.execute(client, arguments);

        Mockito.verify(client).close();
    }

    @Test
    void executeWithArgumentsThrows() {
        TerminalClient client = Mockito.mock(TerminalClient.class);
        Scanner arguments     = new Scanner("extra");

        assertThrows(IllegalArgumentException.class, () -> command.execute(client, arguments));
    }
}
