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
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instruments;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class CancelCommandTest {

    private TerminalClient createTestClient(Events events) throws Exception {
        Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
            Events.class, OrderEntry.class, Instruments.class);
        constructor.setAccessible(true);
        return constructor.newInstance(events, null, null);
    }

    @Test
    public void testConstructor() {
        CancelCommand command = new CancelCommand();
        assertNotNull(command);
    }

    @Test
    public void testGetName() {
        CancelCommand command = new CancelCommand();
        assertEquals("cancel", command.getName());
    }

    @Test
    public void testGetDescription() {
        CancelCommand command = new CancelCommand();
        assertEquals("Cancel an order", command.getDescription());
    }

    @Test
    public void testGetUsage() {
        CancelCommand command = new CancelCommand();
        assertEquals("cancel <order-id>", command.getUsage());
    }


    @Test
    public void testExecuteWithNoArguments() throws Exception {
        CancelCommand command = new CancelCommand();
        Events events = new Events();
        TerminalClient client = createTestClient(events);
        Scanner scanner = new Scanner("");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    public void testExecuteWithExtraArguments() throws Exception {
        CancelCommand command = new CancelCommand();
        Events events = new Events();
        TerminalClient client = createTestClient(events);
        Scanner scanner = new Scanner("ORDER123 extraArg");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    public void testExecuteWithValidArgument() throws Exception {
        CancelCommand command = new CancelCommand();

        Field messageField = CancelCommand.class.getDeclaredField("message");
        messageField.setAccessible(true);
        POE.CancelOrder message = (POE.CancelOrder) messageField.get(command);

        Method executeMethod = CancelCommand.class.getDeclaredMethod("execute", TerminalClient.class, String.class);
        executeMethod.setAccessible(true);

        Events events = new Events();
        TerminalClient client = createTestClient(events);

        try {
            executeMethod.invoke(command, client, "ORDER123");
        } catch (Exception e) {
        }

        assertEquals(0, message.quantity);
        assertEquals("ORDER123", ASCII.get(message.orderId).trim());
    }
}
