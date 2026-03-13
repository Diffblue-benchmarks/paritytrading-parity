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
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instruments;
import java.lang.reflect.Constructor;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class ErrorsCommandTest {

    private TerminalClient createTestClient(Events events) throws Exception {
        Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
            Events.class, OrderEntry.class, Instruments.class);
        constructor.setAccessible(true);
        return constructor.newInstance(events, null, null);
    }

    @Test
    public void testGetName() {
        ErrorsCommand command = new ErrorsCommand();
        assertEquals("errors", command.getName());
    }

    @Test
    public void testGetDescription() {
        ErrorsCommand command = new ErrorsCommand();
        assertEquals("Display occurred errors", command.getDescription());
    }

    @Test
    public void testGetUsage() {
        ErrorsCommand command = new ErrorsCommand();
        assertEquals("errors", command.getUsage());
    }

    @Test
    public void testExecuteWithNoArguments() throws Exception {
        ErrorsCommand command = new ErrorsCommand();
        Events events = new Events();

        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER123");
        rejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;
        events.orderRejected(rejected);

        TerminalClient client = createTestClient(events);

        Scanner scanner = new Scanner("");
        command.execute(client, scanner);
    }

    @Test
    public void testExecuteWithArguments() throws Exception {
        ErrorsCommand command = new ErrorsCommand();
        Events events = new Events();
        TerminalClient client = createTestClient(events);
        Scanner scanner = new Scanner("extraArg");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }
}
