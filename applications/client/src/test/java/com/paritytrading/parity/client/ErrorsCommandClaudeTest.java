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

class ErrorsCommandClaudeTest {

    private ErrorsCommand command;
    private TerminalClient client;
    private Events events;

    @BeforeEach
    void setUp() {
        command = new ErrorsCommand();
        client = mock(TerminalClient.class);
        events = new Events();
        when(client.getEvents()).thenReturn(events);
    }

    @Test
    void testConstructor() {
        ErrorsCommand newCommand = new ErrorsCommand();
        assertNotNull(newCommand);
    }

    @Test
    void testGetName() {
        assertEquals("errors", command.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Display occurred errors", command.getDescription());
    }

    @Test
    void testGetUsage() {
        assertEquals("errors", command.getUsage());
    }

    @Test
    void testExecuteWithNoArguments() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteWithArguments() {
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
    void testExecuteWithNoErrors() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteWithSingleError() throws IOException {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteWithMultipleErrors() throws IOException {
        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected poeMessage3 = new POE.OrderRejected();
        poeMessage3.timestamp = 3000L;
        poeMessage3.orderId = "ORDER3".getBytes();
        poeMessage3.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        events.orderRejected(poeMessage1);
        events.orderRejected(poeMessage2);
        events.orderRejected(poeMessage3);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteWithMixedEvents() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderRejected rejectedMessage = new POE.OrderRejected();
        rejectedMessage.timestamp = 2000L;
        rejectedMessage.orderId = "ORDER2".getBytes();
        rejectedMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderAccepted(acceptedMessage);
        events.orderRejected(rejectedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteCallsGetEvents() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, times(1)).getEvents();
    }

    @Test
    void testExecuteWithDifferentRejectionReasons() throws IOException {
        POE.OrderRejected unknownInstrument = new POE.OrderRejected();
        unknownInstrument.timestamp = 1000L;
        unknownInstrument.orderId = "ORDER1".getBytes();
        unknownInstrument.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected invalidPrice = new POE.OrderRejected();
        invalidPrice.timestamp = 2000L;
        invalidPrice.orderId = "ORDER2".getBytes();
        invalidPrice.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected invalidQuantity = new POE.OrderRejected();
        invalidQuantity.timestamp = 3000L;
        invalidQuantity.orderId = "ORDER3".getBytes();
        invalidQuantity.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        events.orderRejected(unknownInstrument);
        events.orderRejected(invalidPrice);
        events.orderRejected(invalidQuantity);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteWithUnknownRejectionReason() throws IOException {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = (byte) 'X';

        events.orderRejected(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteMultipleTimes() throws IOException {
        Scanner scanner1 = new Scanner("");
        command.execute(client, scanner1);

        Scanner scanner2 = new Scanner("");
        command.execute(client, scanner2);

        Scanner scanner3 = new Scanner("");
        command.execute(client, scanner3);

        verify(client, times(3)).getEvents();
    }

    @Test
    void testExecuteWithEmptyOrderId() throws IOException {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteWithLongOrderId() throws IOException {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "VERYLONGORDERID1".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testCommandImplementsCommandInterface() {
        assertTrue(command instanceof Command);
    }

    @Test
    void testExecuteDoesNotThrowIOException() {
        Scanner scanner = new Scanner("");

        assertDoesNotThrow(() -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithWhitespaceDoesNotThrowException() throws IOException {
        // Whitespace-only input is treated as no arguments (hasNext() returns false)
        Scanner scanner = new Scanner("   ");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testExecuteWithDuplicateOrderIds() throws IOException {
        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER123".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER123".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(poeMessage1);
        events.orderRejected(poeMessage2);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }

    @Test
    void testGetNameReturnsCorrectValue() {
        String name = command.getName();
        assertNotNull(name);
        assertEquals("errors", name);
    }

    @Test
    void testGetDescriptionReturnsCorrectValue() {
        String description = command.getDescription();
        assertNotNull(description);
        assertEquals("Display occurred errors", description);
    }

    @Test
    void testGetUsageReturnsCorrectValue() {
        String usage = command.getUsage();
        assertNotNull(usage);
        assertEquals("errors", usage);
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
    void testExecuteWithAllEventTypes() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderRejected rejectedMessage = new POE.OrderRejected();
        rejectedMessage.timestamp = 2000L;
        rejectedMessage.orderId = "ORDER2".getBytes();
        rejectedMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 3000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 25L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 4000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 25L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderRejected(rejectedMessage);
        events.orderExecuted(executedMessage);
        events.orderCanceled(canceledMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
    }
}
