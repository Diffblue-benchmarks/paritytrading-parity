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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.parity.util.OrderIDGenerator;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class EnterCommandClaudeTest {

    private EnterCommand buyCommand;
    private EnterCommand sellCommand;
    private TerminalClient client;
    private OrderEntry orderEntry;
    private Instruments instruments;
    private OrderIDGenerator orderIdGenerator;

    @BeforeEach
    void setUp() {
        buyCommand = new EnterCommand(POE.BUY);
        sellCommand = new EnterCommand(POE.SELL);
        client = mock(TerminalClient.class);
        orderEntry = mock(OrderEntry.class);
        instruments = mock(Instruments.class);
        orderIdGenerator = new OrderIDGenerator(LocalTime.of(10, 30, 45));

        when(client.getOrderEntry()).thenReturn(orderEntry);
        when(client.getInstruments()).thenReturn(instruments);
        when(client.getOrderIdGenerator()).thenReturn(orderIdGenerator);
    }

    @Test
    void testConstructorWithBuySide() {
        EnterCommand command = new EnterCommand(POE.BUY);
        assertNotNull(command);
        assertEquals("buy", command.getName());
    }

    @Test
    void testConstructorWithSellSide() {
        EnterCommand command = new EnterCommand(POE.SELL);
        assertNotNull(command);
        assertEquals("sell", command.getName());
    }

    @Test
    void testGetNameForBuy() {
        assertEquals("buy", buyCommand.getName());
    }

    @Test
    void testGetNameForSell() {
        assertEquals("sell", sellCommand.getName());
    }

    @Test
    void testGetDescriptionForBuy() {
        assertEquals("Enter a buy order", buyCommand.getDescription());
    }

    @Test
    void testGetDescriptionForSell() {
        assertEquals("Enter a sell order", sellCommand.getDescription());
    }

    @Test
    void testGetUsageForBuy() {
        assertEquals("buy <quantity> <instrument> <price>", buyCommand.getUsage());
    }

    @Test
    void testGetUsageForSell() {
        assertEquals("sell <quantity> <instrument> <price>", sellCommand.getUsage());
    }

    @Test
    void testExecuteWithValidArguments() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(10000.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10.5 FOO 150.25");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry, times(1)).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(POE.BUY, sentMessage.side);
        assertEquals(1050, sentMessage.quantity);
        assertEquals(instrumentId, sentMessage.instrument);
        assertEquals(1502500, sentMessage.price);

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
    void testExecuteWithSellSide() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(10000.0);

        long instrumentId = ASCII.packLong("BAR");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("5.0 BAR 200.50");

        sellCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry, times(1)).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(POE.SELL, sentMessage.side);
        assertEquals(500, sentMessage.quantity);
        assertEquals(instrumentId, sentMessage.instrument);
        assertEquals(2005000, sentMessage.price);
    }

    @Test
    void testExecuteWithUnknownInstrument() throws IOException {
        long instrumentId = ASCII.packLong("UNKNOWN");
        when(instruments.get(instrumentId)).thenReturn(null);

        Scanner scanner = new Scanner("10 UNKNOWN 100");

        assertThrows(IllegalArgumentException.class, () -> {
            buyCommand.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteWithMissingQuantity() throws IOException {
        Scanner scanner = new Scanner("");

        assertThrows(IllegalArgumentException.class, () -> {
            buyCommand.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteWithMissingInstrument() throws IOException {
        Scanner scanner = new Scanner("10");

        assertThrows(IllegalArgumentException.class, () -> {
            buyCommand.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteWithMissingPrice() throws IOException {
        Scanner scanner = new Scanner("10 FOO");

        assertThrows(IllegalArgumentException.class, () -> {
            buyCommand.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteWithExtraArguments() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(10000.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO 100 extra");

        assertThrows(IllegalArgumentException.class, () -> {
            buyCommand.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteWithInvalidQuantityType() throws IOException {
        Scanner scanner = new Scanner("invalid FOO 100");

        assertThrows(IllegalArgumentException.class, () -> {
            buyCommand.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteWithInvalidPriceType() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(10000.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO invalid");

        assertThrows(IllegalArgumentException.class, () -> {
            buyCommand.execute(client, scanner);
        });

        verify(orderEntry, never()).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteThrowsIOException() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(10000.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO 100");
        doThrow(new IOException("Network error")).when(orderEntry).send(any(POE.EnterOrder.class));

        assertThrows(IOException.class, () -> {
            buyCommand.execute(client, scanner);
        });
    }

    @Test
    void testExecuteWithWholeNumberQuantity() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(1.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("XYZ");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("100 XYZ 50.00");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(100, sentMessage.quantity);
        assertEquals(5000, sentMessage.price);
    }

    @Test
    void testExecuteWithDecimalQuantity() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(1000.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("ABC");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("1.234 ABC 99.99");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(1234, sentMessage.quantity);
        assertEquals(9999, sentMessage.price);
    }

    @Test
    void testExecuteWithDecimalPrice() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(1.0);
        when(mockInstrument.getPriceFactor()).thenReturn(10000.0);

        long instrumentId = ASCII.packLong("TEST");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("50 TEST 123.4567");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(50, sentMessage.quantity);
        assertEquals(1234567, sentMessage.price);
    }

    @Test
    void testExecuteMultipleTimes() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner1 = new Scanner("10 FOO 100");
        buyCommand.execute(client, scanner1);

        Scanner scanner2 = new Scanner("20 FOO 200");
        buyCommand.execute(client, scanner2);

        Scanner scanner3 = new Scanner("30 FOO 300");
        buyCommand.execute(client, scanner3);

        verify(orderEntry, times(3)).send(any(POE.EnterOrder.class));
    }

    @Test
    void testCommandImplementsCommandInterface() {
        assertTrue(buyCommand instanceof Command);
        assertTrue(sellCommand instanceof Command);
    }

    @Test
    void testGetInstrumentsCalledDuringExecution() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO 100");
        buyCommand.execute(client, scanner);

        verify(client, atLeastOnce()).getInstruments();
    }

    @Test
    void testGetOrderIdGeneratorCalledDuringExecution() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO 100");
        buyCommand.execute(client, scanner);

        verify(client, atLeastOnce()).getOrderIdGenerator();
    }

    @Test
    void testGetOrderEntryCalledDuringExecution() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO 100");
        buyCommand.execute(client, scanner);

        verify(client, atLeastOnce()).getOrderEntry();
    }

    @Test
    void testExecuteWithZeroQuantity() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("0 FOO 100");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(0, sentMessage.quantity);
    }

    @Test
    void testExecuteWithZeroPrice() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO 0");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(0, sentMessage.price);
    }

    @Test
    void testExecuteWithNegativeQuantity() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("-10 FOO 100");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(-1000, sentMessage.quantity);
    }

    @Test
    void testExecuteWithNegativePrice() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO -100");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(-10000, sentMessage.price);
    }

    @Test
    void testExecuteWithLargeQuantity() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(1.0);
        when(mockInstrument.getPriceFactor()).thenReturn(1.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("999999999 FOO 100");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(999999999, sentMessage.quantity);
    }

    @Test
    void testExecuteWithLargePrice() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(1.0);
        when(mockInstrument.getPriceFactor()).thenReturn(1.0);

        long instrumentId = ASCII.packLong("FOO");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 FOO 999999999");

        buyCommand.execute(client, scanner);

        ArgumentCaptor<POE.EnterOrder> messageCaptor = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry).send(messageCaptor.capture());

        POE.EnterOrder sentMessage = messageCaptor.getValue();
        assertEquals(999999999, sentMessage.price);
    }

    @Test
    void testExecuteWithShortInstrumentName() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        long instrumentId = ASCII.packLong("A");
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 A 100");

        buyCommand.execute(client, scanner);

        verify(orderEntry).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteWithLongInstrumentName() throws IOException {
        Instrument mockInstrument = mock(Instrument.class);
        when(mockInstrument.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument.getPriceFactor()).thenReturn(100.0);

        String longName = "ABCDEFGH";
        long instrumentId = ASCII.packLong(longName);
        when(instruments.get(instrumentId)).thenReturn(mockInstrument);

        Scanner scanner = new Scanner("10 " + longName + " 100");

        buyCommand.execute(client, scanner);

        verify(orderEntry).send(any(POE.EnterOrder.class));
    }

    @Test
    void testExecuteWithDifferentSizeFactors() throws IOException {
        Instrument mockInstrument1 = mock(Instrument.class);
        when(mockInstrument1.getSizeFactor()).thenReturn(1.0);
        when(mockInstrument1.getPriceFactor()).thenReturn(100.0);

        long instrumentId1 = ASCII.packLong("FOO");
        when(instruments.get(instrumentId1)).thenReturn(mockInstrument1);

        Scanner scanner1 = new Scanner("10 FOO 100");
        buyCommand.execute(client, scanner1);

        ArgumentCaptor<POE.EnterOrder> messageCaptor1 = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry, times(1)).send(messageCaptor1.capture());
        assertEquals(10, messageCaptor1.getValue().quantity);

        Instrument mockInstrument2 = mock(Instrument.class);
        when(mockInstrument2.getSizeFactor()).thenReturn(1000.0);
        when(mockInstrument2.getPriceFactor()).thenReturn(100.0);

        long instrumentId2 = ASCII.packLong("BAR");
        when(instruments.get(instrumentId2)).thenReturn(mockInstrument2);

        Scanner scanner2 = new Scanner("10 BAR 100");
        buyCommand.execute(client, scanner2);

        ArgumentCaptor<POE.EnterOrder> messageCaptor2 = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry, times(2)).send(messageCaptor2.capture());
        assertEquals(10000, messageCaptor2.getAllValues().get(1).quantity);
    }

    @Test
    void testExecuteWithDifferentPriceFactors() throws IOException {
        Instrument mockInstrument1 = mock(Instrument.class);
        when(mockInstrument1.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument1.getPriceFactor()).thenReturn(1.0);

        long instrumentId1 = ASCII.packLong("FOO");
        when(instruments.get(instrumentId1)).thenReturn(mockInstrument1);

        Scanner scanner1 = new Scanner("10 FOO 100");
        buyCommand.execute(client, scanner1);

        ArgumentCaptor<POE.EnterOrder> messageCaptor1 = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry, times(1)).send(messageCaptor1.capture());
        assertEquals(100, messageCaptor1.getValue().price);

        Instrument mockInstrument2 = mock(Instrument.class);
        when(mockInstrument2.getSizeFactor()).thenReturn(100.0);
        when(mockInstrument2.getPriceFactor()).thenReturn(10000.0);

        long instrumentId2 = ASCII.packLong("BAR");
        when(instruments.get(instrumentId2)).thenReturn(mockInstrument2);

        Scanner scanner2 = new Scanner("10 BAR 100");
        buyCommand.execute(client, scanner2);

        ArgumentCaptor<POE.EnterOrder> messageCaptor2 = ArgumentCaptor.forClass(POE.EnterOrder.class);
        verify(orderEntry, times(2)).send(messageCaptor2.capture());
        assertEquals(1000000, messageCaptor2.getAllValues().get(1).price);
    }
}
