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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class ErrorsCommandTest {

    private ErrorsCommand command;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        command = new ErrorsCommand();
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void getName() {
        assertEquals("errors", command.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Display occurred errors", command.getDescription());
    }

    @Test
    void getUsage() {
        assertEquals("errors", command.getUsage());
    }

    @Test
    void executeWithNoErrors() {
        TerminalClient client = mock(TerminalClient.class);
        when(client.getEvents()).thenReturn(new Events());

        command.execute(client, new Scanner(""));

        String output = outputStream.toString();
        assertTrue(output.contains(Error.HEADER));
    }

    @Test
    void executeThrowsOnExtraArguments() {
        TerminalClient client = mock(TerminalClient.class);

        assertThrows(IllegalArgumentException.class, () ->
            command.execute(client, new Scanner("extra"))
        );
    }

    @Test
    void executeWithErrors() {
        Events events = new Events();

        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 12345L;
        ASCII.putLeft(message.orderId, "ABC123");
        message.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;
        events.orderRejected(message);

        TerminalClient client = mock(TerminalClient.class);
        when(client.getEvents()).thenReturn(events);

        command.execute(client, new Scanner(""));

        String output = outputStream.toString();
        assertTrue(output.contains(Error.HEADER));
        assertTrue(output.contains("ABC123"));
        assertTrue(output.contains("Unknown instrument"));
    }
}
