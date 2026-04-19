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
import com.paritytrading.parity.util.OrderIDGenerator;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnterCommandTest {

    private TerminalClient   client;
    private Instruments      instruments;
    private Instrument       instrument;
    private OrderIDGenerator orderIdGenerator;
    private OrderEntry       orderEntry;

    @BeforeEach
    void setUp() {
        client           = mock(TerminalClient.class);
        instruments      = mock(Instruments.class);
        instrument       = mock(Instrument.class);
        orderIdGenerator = mock(OrderIDGenerator.class);
        orderEntry       = mock(OrderEntry.class);

        when(client.getInstruments()).thenReturn(instruments);
        when(client.getOrderIdGenerator()).thenReturn(orderIdGenerator);
        when(client.getOrderEntry()).thenReturn(orderEntry);
    }

    @Test
    void buyName() {
        EnterCommand command = new EnterCommand(POE.BUY);

        assertEquals("buy", command.getName());
    }

    @Test
    void sellName() {
        EnterCommand command = new EnterCommand(POE.SELL);

        assertEquals("sell", command.getName());
    }

    @Test
    void buyDescription() {
        EnterCommand command = new EnterCommand(POE.BUY);

        assertEquals("Enter a buy order", command.getDescription());
    }

    @Test
    void sellDescription() {
        EnterCommand command = new EnterCommand(POE.SELL);

        assertEquals("Enter a sell order", command.getDescription());
    }

    @Test
    void buyUsage() {
        EnterCommand command = new EnterCommand(POE.BUY);

        assertEquals("buy <quantity> <instrument> <price>", command.getUsage());
    }

    @Test
    void sellUsage() {
        EnterCommand command = new EnterCommand(POE.SELL);

        assertEquals("sell <quantity> <instrument> <price>", command.getUsage());
    }

    @Test
    void executeBuyOrder() throws IOException {
        EnterCommand command = new EnterCommand(POE.BUY);

        when(instrument.getSizeFactor()).thenReturn(1.0);
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instruments.get(anyLong())).thenReturn(instrument);
        when(orderIdGenerator.next()).thenReturn("12:00:00-0000001");

        Scanner scanner = new Scanner("10 AAPL 150.50");
        scanner.useLocale(Locale.US);

        command.execute(client, scanner);

        verify(orderEntry).send(any(POE.EnterOrder.class));
    }

    @Test
    void executeSellOrder() throws IOException {
        EnterCommand command = new EnterCommand(POE.SELL);

        when(instrument.getSizeFactor()).thenReturn(1.0);
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instruments.get(anyLong())).thenReturn(instrument);
        when(orderIdGenerator.next()).thenReturn("12:00:00-0000001");

        Scanner scanner = new Scanner("5 AAPL 200.25");
        scanner.useLocale(Locale.US);

        command.execute(client, scanner);

        verify(orderEntry).send(any(POE.EnterOrder.class));
    }

    @Test
    void executeThrowsOnUnknownInstrument() {
        EnterCommand command = new EnterCommand(POE.BUY);

        when(instruments.get(anyLong())).thenReturn(null);

        Scanner scanner = new Scanner("10 UNKNOWN 150.50");
        scanner.useLocale(Locale.US);

        assertThrows(IllegalArgumentException.class, () -> command.execute(client, scanner));
    }

    @Test
    void executeThrowsOnExtraArguments() {
        EnterCommand command = new EnterCommand(POE.BUY);

        when(instrument.getSizeFactor()).thenReturn(1.0);
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instruments.get(anyLong())).thenReturn(instrument);

        Scanner scanner = new Scanner("10 AAPL 150.50 extra");
        scanner.useLocale(Locale.US);

        assertThrows(IllegalArgumentException.class, () -> command.execute(client, scanner));
    }

    @Test
    void executeThrowsOnMissingArguments() {
        EnterCommand command = new EnterCommand(POE.BUY);

        Scanner scanner = new Scanner("10");
        scanner.useLocale(Locale.US);

        assertThrows(IllegalArgumentException.class, () -> command.execute(client, scanner));
    }
}
