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

import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExitCommandTest {

    private static OrderEntry createTestOrderEntry() throws Exception {
        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();

        Constructor<OrderEntry> constructor =
            OrderEntry.class.getDeclaredConstructor(Selector.class, SocketChannel.class,
                Class.forName("com.paritytrading.parity.net.poe.POEClientListener"));
        constructor.setAccessible(true);
        OrderEntry orderEntry = constructor.newInstance(selector, channel, new Events());

        channel.close();
        selector.close();

        return orderEntry;
    }

    private static TerminalClient createTestClient() throws Exception {
        OrderEntry orderEntry = createTestOrderEntry();

        Constructor<TerminalClient> constructor =
            TerminalClient.class.getDeclaredConstructor(Events.class, OrderEntry.class, Instruments.class);
        constructor.setAccessible(true);
        return constructor.newInstance(new Events(), orderEntry, null);
    }

    @Test
    public void testExecuteWithNoArguments() throws Exception {
        ExitCommand command = new ExitCommand();
        TerminalClient client = createTestClient();
        Scanner scanner = new Scanner("");

        Field closedField = TerminalClient.class.getDeclaredField("closed");
        closedField.setAccessible(true);

        command.execute(client, scanner);

        assertTrue((Boolean) closedField.get(client));
    }

    @Test
    public void testExecuteWithArguments() throws Exception {
        ExitCommand command = new ExitCommand();
        TerminalClient client = createTestClient();
        Scanner scanner = new Scanner("extra");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    public void testGetName() {
        ExitCommand command = new ExitCommand();

        String name = command.getName();

        assertEquals("exit", name);
    }

    @Test
    public void testGetDescription() {
        ExitCommand command = new ExitCommand();

        String description = command.getDescription();

        assertEquals("Exit the client", description);
    }

    @Test
    public void testGetUsage() {
        ExitCommand command = new ExitCommand();

        String usage = command.getUsage();

        assertEquals("exit", usage);
    }

}
