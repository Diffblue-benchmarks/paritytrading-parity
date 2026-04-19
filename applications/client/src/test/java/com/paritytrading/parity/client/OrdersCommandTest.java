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

import com.paritytrading.parity.util.Instruments;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdersCommandTest {

    private OrdersCommand command;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        command = new OrdersCommand();

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
        assertEquals("orders", command.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Display open orders", command.getDescription());
    }

    @Test
    void getUsage() {
        assertEquals("orders", command.getUsage());
    }

    @Test
    void executeWithNoOrders() {
        TerminalClient client = mock(TerminalClient.class);
        Instruments instruments = mock(Instruments.class);

        when(client.getInstruments()).thenReturn(instruments);
        when(instruments.getPriceWidth()).thenReturn(10);
        when(instruments.getSizeWidth()).thenReturn(10);
        when(client.getEvents()).thenReturn(new Events());

        Scanner arguments = new Scanner("");

        command.execute(client, arguments);

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
        assertTrue(output.contains("Inst"));
        assertTrue(output.contains("Quantity"));
        assertTrue(output.contains("Price"));
    }

    @Test
    void executeThrowsOnExtraArguments() {
        TerminalClient client = mock(TerminalClient.class);
        Scanner arguments = new Scanner("extra");

        assertThrows(IllegalArgumentException.class, () -> command.execute(client, arguments));
    }
}
