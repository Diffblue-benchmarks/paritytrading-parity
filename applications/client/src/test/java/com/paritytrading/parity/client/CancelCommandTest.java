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
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class CancelCommandTest {

    private CancelCommand command;

    @BeforeEach
    void setUp() {
        command = new CancelCommand();
    }

    @Test
    void getName() {
        assertEquals("cancel", command.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Cancel an order", command.getDescription());
    }

    @Test
    void getUsage() {
        assertEquals("cancel <order-id>", command.getUsage());
    }

    @Test
    void executeWithValidOrderId() throws IOException {
        OrderEntry orderEntry = mock(OrderEntry.class);
        TerminalClient client = mock(TerminalClient.class);
        when(client.getOrderEntry()).thenReturn(orderEntry);

        command.execute(client, new Scanner("ORDER123"));

        verify(orderEntry).send(any(POE.CancelOrder.class));
    }

    @Test
    void executeThrowsOnMissingArguments() {
        TerminalClient client = mock(TerminalClient.class);

        assertThrows(IllegalArgumentException.class, () ->
            command.execute(client, new Scanner(""))
        );
    }

    @Test
    void executeThrowsOnExtraArguments() {
        TerminalClient client = mock(TerminalClient.class);

        assertThrows(IllegalArgumentException.class, () ->
            command.execute(client, new Scanner("ORDER1 extra"))
        );
    }

    @Test
    void executeSendsMessageWithZeroQuantity() throws IOException {
        OrderEntry orderEntry = mock(OrderEntry.class);
        TerminalClient client = mock(TerminalClient.class);
        when(client.getOrderEntry()).thenReturn(orderEntry);

        command.execute(client, new Scanner("MYORDER"));

        verify(orderEntry).send(argThat((POE.CancelOrder msg) -> msg.quantity == 0));
    }

    @Test
    void executeSendsMessageWithCorrectOrderId() throws IOException {
        OrderEntry orderEntry = mock(OrderEntry.class);
        TerminalClient client = mock(TerminalClient.class);
        when(client.getOrderEntry()).thenReturn(orderEntry);

        command.execute(client, new Scanner("TESTID"));

        verify(orderEntry).send(argThat((POE.CancelOrder msg) ->
            ASCII.get(msg.orderId).trim().equals("TESTID")
        ));
    }
}
