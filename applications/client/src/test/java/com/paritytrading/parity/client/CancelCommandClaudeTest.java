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

import com.paritytrading.parity.net.poe.POE;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CancelCommandClaudeTest {

    private CancelCommand command;
    private TerminalClient client;
    private OrderEntry orderEntry;

    @BeforeEach
    void setUp() {
        command = new CancelCommand();
        client = mock(TerminalClient.class);
        orderEntry = mock(OrderEntry.class);
        when(client.getOrderEntry()).thenReturn(orderEntry);
    }

    @Test
    void testConstructor() {
        CancelCommand newCommand = new CancelCommand();
        assertNotNull(newCommand);
    }

    @Test
    void testGetName() {
        assertEquals("cancel", command.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Cancel an order", command.getDescription());
    }

    @Test
    void testGetUsage() {
        assertEquals("cancel <order-id>", command.getUsage());
    }

    @Test
    void testExecuteWithValidOrderId() throws IOException {
        Scanner scanner = new Scanner("ORDER123");

        command.execute(client, scanner);

        ArgumentCaptor<POE.CancelOrder> messageCaptor = ArgumentCaptor.forClass(POE.CancelOrder.class);
        verify(orderEntry, times(1)).send(messageCaptor.capture());

        POE.CancelOrder sentMessage = messageCaptor.getValue();
        assertEquals(0, sentMessage.quantity);

        // Verify the order ID is set (we can check the byte array is not all zeros)
        boolean hasNonZero = false;
        for (byte b : sentMessage.orderId) {
            if (b != 0) {
                hasNonZero = true;
                break;
            }
        }
        assertTrue(hasNonZero, "Order ID should be populated");
    }

    @Test
    void testExecuteWithLongOrderId() throws IOException {
        // Test with an order ID that is exactly 16 characters
        Scanner scanner = new Scanner("1234567890123456");

        command.execute(client, scanner);

        verify(orderEntry, times(1)).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteWithShortOrderId() throws IOException {
        Scanner scanner = new Scanner("ABC");

        command.execute(client, scanner);

        verify(orderEntry, times(1)).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteWithEmptyArguments() throws IOException {
        Scanner scanner = new Scanner("");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteWithExtraArguments() throws IOException {
        Scanner scanner = new Scanner("ORDER123 extraArg");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteWithMultipleExtraArguments() throws IOException {
        Scanner scanner = new Scanner("ORDER123 extra1 extra2");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteThrowsIOException() throws IOException {
        Scanner scanner = new Scanner("ORDER123");
        doThrow(new IOException("Network error")).when(orderEntry).send(any(POE.CancelOrder.class));

        assertThrows(IOException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithNumericOrderId() throws IOException {
        Scanner scanner = new Scanner("12345");

        command.execute(client, scanner);

        verify(orderEntry, times(1)).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteWithAlphanumericOrderId() throws IOException {
        Scanner scanner = new Scanner("ORD-ABC-123");

        command.execute(client, scanner);

        verify(orderEntry, times(1)).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteWithSpecialCharactersInOrderId() throws IOException {
        Scanner scanner = new Scanner("ORD_123-XYZ");

        command.execute(client, scanner);

        verify(orderEntry, times(1)).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteMultipleTimes() throws IOException {
        Scanner scanner1 = new Scanner("ORDER1");
        command.execute(client, scanner1);

        Scanner scanner2 = new Scanner("ORDER2");
        command.execute(client, scanner2);

        Scanner scanner3 = new Scanner("ORDER3");
        command.execute(client, scanner3);

        verify(orderEntry, times(3)).send(any(POE.CancelOrder.class));
    }

    @Test
    void testExecuteSetsQuantityToZero() throws IOException {
        Scanner scanner = new Scanner("ORDER123");

        command.execute(client, scanner);

        ArgumentCaptor<POE.CancelOrder> messageCaptor = ArgumentCaptor.forClass(POE.CancelOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        assertEquals(0, messageCaptor.getValue().quantity);
    }

    @Test
    void testCommandImplementsCommandInterface() {
        assertTrue(command instanceof Command);
    }

    @Test
    void testGetOrderEntryCalledDuringExecution() throws IOException {
        Scanner scanner = new Scanner("ORDER123");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getOrderEntry();
    }
}
