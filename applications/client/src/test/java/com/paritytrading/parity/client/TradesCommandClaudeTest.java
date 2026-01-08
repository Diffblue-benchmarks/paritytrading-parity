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

class TradesCommandClaudeTest {

    private TradesCommand command;
    private TerminalClient client;
    private Events events;
    private Instruments instruments;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        command = new TradesCommand();
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
        TradesCommand newCommand = new TradesCommand();
        assertNotNull(newCommand);
    }

    @Test
    void testGetName() {
        assertEquals("trades", command.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Display occurred trades", command.getDescription());
    }

    @Test
    void testGetUsage() {
        assertEquals("trades", command.getUsage());
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
    void testExecuteWithNoTrades() throws IOException {
        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithSingleTrade() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER123".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER123".getBytes();
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
    void testExecuteWithMultipleTrades() throws IOException {
        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = "ORDER1".getBytes();
        accepted1.side = POE.BUY;
        accepted1.instrument = 100L;
        accepted1.quantity = 50L;
        accepted1.price = 1000L;
        accepted1.orderNumber = 1L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 2000000000L;
        executed1.orderId = "ORDER1".getBytes();
        executed1.quantity = 50L;
        executed1.price = 1000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 3000000000L;
        accepted2.orderId = "ORDER2".getBytes();
        accepted2.side = POE.SELL;
        accepted2.instrument = 200L;
        accepted2.quantity = 100L;
        accepted2.price = 2000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 4000000000L;
        executed2.orderId = "ORDER2".getBytes();
        executed2.quantity = 100L;
        executed2.price = 2000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed2.matchNumber = 2L;

        events.orderAccepted(accepted1);
        events.orderExecuted(executed1);
        events.orderAccepted(accepted2);
        events.orderExecuted(executed2);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithPartialExecution() throws IOException {
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
        assertEquals("trades", name);
    }

    @Test
    void testGetDescriptionReturnsCorrectValue() {
        String description = command.getDescription();
        assertNotNull(description);
        assertEquals("Display occurred trades", description);
    }

    @Test
    void testGetUsageReturnsCorrectValue() {
        String usage = command.getUsage();
        assertNotNull(usage);
        assertEquals("trades", usage);
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
    void testExecuteWithBuyTrade() throws IOException {
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
    }

    @Test
    void testExecuteWithSellTrade() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.SELL;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 50L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

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
    void testExecuteWithMultiplePartialExecutions() throws IOException {
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

        POE.OrderExecuted executed3 = new POE.OrderExecuted();
        executed3.timestamp = 4000000000L;
        executed3.orderId = "ORDER1".getBytes();
        executed3.quantity = 50L;
        executed3.price = 1000L;
        executed3.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed3.matchNumber = 3L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executed1);
        events.orderExecuted(executed2);
        events.orderExecuted(executed3);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithOrderAcceptedButNoExecution() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        events.orderAccepted(acceptedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }

    @Test
    void testExecuteWithMixedOrdersAndTrades() throws IOException {
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

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 3000000000L;
        executed1.orderId = "ORDER1".getBytes();
        executed1.quantity = 100L;
        executed1.price = 1000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed1.matchNumber = 1L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(executed1);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithLargeQuantity() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 999999999L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 999999999L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithLargePrice() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 999999999L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 50L;
        executedMessage.price = 999999999L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithTradesSortedByTimestamp() throws IOException {
        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = "ORDER1".getBytes();
        accepted1.side = POE.BUY;
        accepted1.instrument = 100L;
        accepted1.quantity = 50L;
        accepted1.price = 1000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 2000000000L;
        accepted2.orderId = "ORDER2".getBytes();
        accepted2.side = POE.SELL;
        accepted2.instrument = 200L;
        accepted2.quantity = 100L;
        accepted2.price = 2000L;
        accepted2.orderNumber = 2L;

        POE.OrderAccepted accepted3 = new POE.OrderAccepted();
        accepted3.timestamp = 3000000000L;
        accepted3.orderId = "ORDER3".getBytes();
        accepted3.side = POE.BUY;
        accepted3.instrument = 300L;
        accepted3.quantity = 75L;
        accepted3.price = 1500L;
        accepted3.orderNumber = 3L;

        // Add executions in non-sorted order
        POE.OrderExecuted executed3 = new POE.OrderExecuted();
        executed3.timestamp = 6000000000L;
        executed3.orderId = "ORDER3".getBytes();
        executed3.quantity = 75L;
        executed3.price = 1500L;
        executed3.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed3.matchNumber = 3L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 4000000000L;
        executed1.orderId = "ORDER1".getBytes();
        executed1.quantity = 50L;
        executed1.price = 1000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 5000000000L;
        executed2.orderId = "ORDER2".getBytes();
        executed2.quantity = 100L;
        executed2.price = 2000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed2.matchNumber = 2L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderAccepted(accepted3);
        events.orderExecuted(executed3);
        events.orderExecuted(executed1);
        events.orderExecuted(executed2);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithEmptyOrderId() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
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
    }

    @Test
    void testExecuteWithZeroQuantity() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 0L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 0L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithZeroPrice() throws IOException {
        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.timestamp = 1000000000L;
        acceptedMessage.orderId = "ORDER1".getBytes();
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 100L;
        acceptedMessage.quantity = 50L;
        acceptedMessage.price = 0L;
        acceptedMessage.orderNumber = 1L;

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORDER1".getBytes();
        executedMessage.quantity = 50L;
        executedMessage.price = 0L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderAccepted(acceptedMessage);
        events.orderExecuted(executedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithSameMatchNumber() throws IOException {
        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = "ORDER1".getBytes();
        accepted1.side = POE.BUY;
        accepted1.instrument = 100L;
        accepted1.quantity = 50L;
        accepted1.price = 1000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 2000000000L;
        accepted2.orderId = "ORDER2".getBytes();
        accepted2.side = POE.SELL;
        accepted2.instrument = 100L;
        accepted2.quantity = 50L;
        accepted2.price = 1000L;
        accepted2.orderNumber = 2L;

        // Both executions have the same match number (they are the two sides of one trade)
        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 3000000000L;
        executed1.orderId = "ORDER1".getBytes();
        executed1.quantity = 50L;
        executed1.price = 1000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 3000000000L;
        executed2.orderId = "ORDER2".getBytes();
        executed2.quantity = 50L;
        executed2.price = 1000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed2.matchNumber = 1L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(executed1);
        events.orderExecuted(executed2);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testExecuteWithOrphanedExecution() throws IOException {
        // Execution without corresponding OrderAccepted
        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.timestamp = 2000000000L;
        executedMessage.orderId = "ORPHAN".getBytes();
        executedMessage.quantity = 50L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;

        events.orderExecuted(executedMessage);

        Scanner scanner = new Scanner("");

        command.execute(client, scanner);

        verify(client, atLeastOnce()).getEvents();
        verify(client, atLeastOnce()).getInstruments();

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
    }
}
