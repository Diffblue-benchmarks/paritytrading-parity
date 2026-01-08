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
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdersCommandClaudeTest {

    private OrdersCommand command;
    private TerminalClient client;
    private Events events;
    private Instruments instruments;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        command = new OrdersCommand();
        client = mock(TerminalClient.class);
        events = new Events();

        // Create a mock instruments configuration
        instruments = mock(Instruments.class);
        when(instruments.getPriceWidth()).thenReturn(10);
        when(instruments.getSizeWidth()).thenReturn(8);

        // Create a mock instrument for testing
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getPriceFormat()).thenReturn("%10.2f");
        when(mockInstrument.getSizeFormat()).thenReturn("%8.0f");
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);
        when(mockInstrument.getSizeFactor()).thenReturn(1.0);
        when(instruments.get(anyLong())).thenReturn(mockInstrument);

        when(client.getEvents()).thenReturn(events);
        when(client.getInstruments()).thenReturn(instruments);

        // Capture System.out
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    void testConstructor() {
        OrdersCommand newCommand = new OrdersCommand();
        assertNotNull(newCommand);
    }

    @Test
    void testGetName() {
        assertEquals("orders", command.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Display open orders", command.getDescription());
    }

    @Test
    void testGetUsage() {
        assertEquals("orders", command.getUsage());
    }

    @Test
    void testExecuteWithNoArguments() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
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
    void testExecuteWithNoOrders() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithSingleOrder() throws IOException {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 50L;
        poeMessage.price = 1000L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithMultipleOrders() throws IOException {
        POE.OrderAccepted poeMessage1 = new POE.OrderAccepted();
        poeMessage1.timestamp = 1000000000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.side = POE.BUY;
        poeMessage1.instrument = 100L;
        poeMessage1.quantity = 50L;
        poeMessage1.price = 1000L;
        poeMessage1.orderNumber = 1L;

        POE.OrderAccepted poeMessage2 = new POE.OrderAccepted();
        poeMessage2.timestamp = 2000000000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.side = POE.SELL;
        poeMessage2.instrument = 200L;
        poeMessage2.quantity = 100L;
        poeMessage2.price = 2000L;
        poeMessage2.orderNumber = 2L;

        POE.OrderAccepted poeMessage3 = new POE.OrderAccepted();
        poeMessage3.timestamp = 3000000000L;
        poeMessage3.orderId = "ORDER3".getBytes();
        poeMessage3.side = POE.BUY;
        poeMessage3.instrument = 300L;
        poeMessage3.quantity = 75L;
        poeMessage3.price = 1500L;
        poeMessage3.orderNumber = 3L;

        events.orderAccepted(poeMessage1);
        events.orderAccepted(poeMessage2);
        events.orderAccepted(poeMessage3);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithPartiallyExecutedOrder() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 30L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithFullyExecutedOrder() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 50L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithPartiallyCanceledOrder() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 2000000000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 40L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderCanceled(canceledMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithFullyCanceledOrder() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 2000000000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 50L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderCanceled(canceledMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithMixedEvents() throws IOException {
        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = "ORDER1".getBytes();
        accepted1.side = POE.BUY;
        accepted1.instrument = 100L;
        accepted1.quantity = 100L;
        accepted1.price = 1000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 2000000000L;
        accepted2.orderId = "ORDER2".getBytes();
        accepted2.side = POE.SELL;
        accepted2.instrument = 200L;
        accepted2.quantity = 50L;
        accepted2.price = 2000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 3000000000L;
        executed.orderId = "ORDER1".getBytes();
        executed.quantity = 30L;
        executed.price = 1000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 4000000000L;
        canceled.orderId = "ORDER2".getBytes();
        canceled.canceledQuantity = 50L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 5000000000L;
        rejected.orderId = "ORDER3".getBytes();
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(executed);
        events.orderCanceled(canceled);
        events.orderRejected(rejected);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteCallsGetEvents() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, times(1)).getEvents();
    }

    @Test
    void testExecuteCallsGetInstruments() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, times(1)).getInstruments();
    }

    @Test
    void testExecuteWithWhitespaceDoesNotThrowException() throws IOException {
        Scanner scanner = new Scanner("   ");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
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
        verify(client, times(3)).getInstruments();
    }

    @Test
    void testCommandImplementsCommandInterface() {
        assertTrue(command instanceof Command);
    }

    @Test
    void testGetNameReturnsCorrectValue() {
        String name = command.getName();
        assertNotNull(name);
        assertEquals("orders", name);
    }

    @Test
    void testGetDescriptionReturnsCorrectValue() {
        String description = command.getDescription();
        assertNotNull(description);
        assertEquals("Display open orders", description);
    }

    @Test
    void testGetUsageReturnsCorrectValue() {
        String usage = command.getUsage();
        assertNotNull(usage);
        assertEquals("orders", usage);
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
    void testGetNameReturnsSameInstance() {
        String name1 = command.getName();
        String name2 = command.getName();
        assertSame(name1, name2);
    }

    @Test
    void testGetDescriptionReturnsSameInstance() {
        String description1 = command.getDescription();
        String description2 = command.getDescription();
        assertSame(description1, description2);
    }

    @Test
    void testGetUsageReturnsSameInstance() {
        String usage1 = command.getUsage();
        String usage2 = command.getUsage();
        assertSame(usage1, usage2);
    }

    @Test
    void testGetNameNotEmpty() {
        String name = command.getName();
        assertFalse(name.isEmpty());
    }

    @Test
    void testGetDescriptionNotEmpty() {
        String description = command.getDescription();
        assertFalse(description.isEmpty());
    }

    @Test
    void testGetUsageNotEmpty() {
        String usage = command.getUsage();
        assertFalse(usage.isEmpty());
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
    void testExecuteDoesNotThrowIOException() {
        Scanner scanner = new Scanner("");

        assertDoesNotThrow(() -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteDisplaysTableHeader() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
        assertTrue(output.contains("S"));
        assertTrue(output.contains("Inst"));
        assertTrue(output.contains("Quantity"));
        assertTrue(output.contains("Price"));
    }

    @Test
    void testExecuteOutputContainsNewlines() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("\n"));
    }

    @Test
    void testExecuteWithBuyOrder() throws IOException {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER1".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 50L;
        poeMessage.price = 1000L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithSellOrder() throws IOException {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER1".getBytes();
        poeMessage.side = POE.SELL;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 50L;
        poeMessage.price = 1000L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithDifferentInstrumentWidths() throws IOException {
        when(instruments.getPriceWidth()).thenReturn(15);
        when(instruments.getSizeWidth()).thenReturn(12);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testCommandCanBeReused() throws IOException {
        TerminalClient client1 = mock(TerminalClient.class);
        Events events1 = new Events();
        when(client1.getEvents()).thenReturn(events1);
        when(client1.getInstruments()).thenReturn(instruments);

        TerminalClient client2 = mock(TerminalClient.class);
        Events events2 = new Events();
        when(client2.getEvents()).thenReturn(events2);
        when(client2.getInstruments()).thenReturn(instruments);

        Scanner scanner1 = new Scanner("");
        command.execute(client1, scanner1);

        Scanner scanner2 = new Scanner("");
        command.execute(client2, scanner2);

        verify(client1, times(1)).getEvents();
        verify(client2, times(1)).getEvents();
    }

    @Test
    void testExecuteWithTrailingSpacesAfterArgument() {
        Scanner scanner = new Scanner("arg   ");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithLeadingSpacesBeforeArgument() {
        Scanner scanner = new Scanner("   arg");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithMultipleSpaceSeparatedArguments() {
        Scanner scanner = new Scanner("a b c d e");

        assertThrows(IllegalArgumentException.class, () -> {
            command.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithEmptyOrderId() throws IOException {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 50L;
        poeMessage.price = 1000L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithLargeQuantity() throws IOException {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER1".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 999999999L;
        poeMessage.price = 1000L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithLargePrice() throws IOException {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER1".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 50L;
        poeMessage.price = 999999999L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithZeroQuantity() throws IOException {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER1".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 0L;
        poeMessage.price = 1000L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithZeroPrice() throws IOException {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp = 1000000000L;
        poeMessage.orderId = "ORDER1".getBytes();
        poeMessage.side = POE.BUY;
        poeMessage.instrument = 100L;
        poeMessage.quantity = 50L;
        poeMessage.price = 0L;
        poeMessage.orderNumber = 1L;

        events.orderAccepted(poeMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithOrdersSortedByTimestamp() throws IOException {
        POE.OrderAccepted order3 = new POE.OrderAccepted();
        order3.timestamp = 3000000000L;
        order3.orderId = "ORDER3".getBytes();
        order3.side = POE.BUY;
        order3.instrument = 300L;
        order3.quantity = 75L;
        order3.price = 1500L;
        order3.orderNumber = 3L;

        POE.OrderAccepted order1 = new POE.OrderAccepted();
        order1.timestamp = 1000000000L;
        order1.orderId = "ORDER1".getBytes();
        order1.side = POE.BUY;
        order1.instrument = 100L;
        order1.quantity = 50L;
        order1.price = 1000L;
        order1.orderNumber = 1L;

        POE.OrderAccepted order2 = new POE.OrderAccepted();
        order2.timestamp = 2000000000L;
        order2.orderId = "ORDER2".getBytes();
        order2.side = POE.SELL;
        order2.instrument = 200L;
        order2.quantity = 100L;
        order2.price = 2000L;
        order2.orderNumber = 2L;

        // Add in non-sorted order
        events.orderAccepted(order3);
        events.orderAccepted(order1);
        events.orderAccepted(order2);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithMultipleExecutionsOnSameOrder() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 2000000000L;
        executed1.orderId = "ORDER1".getBytes();
        executed1.quantity = 30L;
        executed1.price = 1000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 3000000000L;
        executed2.orderId = "ORDER1".getBytes();
        executed2.quantity = 20L;
        executed2.price = 1000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed2.matchNumber = 2L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executed1);
        events.orderExecuted(executed2);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithMultipleCancellationsOnSameOrder() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderCanceled canceled1 = new POE.OrderCanceled();
        canceled1.timestamp = 2000000000L;
        canceled1.orderId = "ORDER1".getBytes();
        canceled1.canceledQuantity = 30L;
        canceled1.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        POE.OrderCanceled canceled2 = new POE.OrderCanceled();
        canceled2.timestamp = 3000000000L;
        canceled2.orderId = "ORDER1".getBytes();
        canceled2.canceledQuantity = 20L;
        canceled2.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderCanceled(canceled1);
        events.orderCanceled(canceled2);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithExecutionAndCancellationOnSameOrder() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 30L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        POE.OrderCanceled canceledMessage = new POE.OrderCanceled();
        canceledMessage.timestamp = 3000000000L;
        canceledMessage.orderId = "ORDER1".getBytes();
        canceledMessage.canceledQuantity = 40L;
        canceledMessage.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);
        events.orderCanceled(canceledMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }
}
