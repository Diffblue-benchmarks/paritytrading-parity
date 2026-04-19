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
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TradesCommandTest {

    private TradesCommand command;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        command = new TradesCommand();
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
        assertEquals("trades", command.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Display occurred trades", command.getDescription());
    }

    @Test
    void getUsage() {
        assertEquals("trades", command.getUsage());
    }

    @Test
    void executeWithExtraArguments() {
        TerminalClient client = Mockito.mock(TerminalClient.class);
        Scanner arguments = new Scanner("extra");

        assertThrows(IllegalArgumentException.class, () -> command.execute(client, arguments));
    }

    @Test
    void executeWithNoTrades() {
        TerminalClient client = Mockito.mock(TerminalClient.class);
        Instruments instruments = Mockito.mock(Instruments.class);
        Events events = new Events();

        Mockito.when(client.getInstruments()).thenReturn(instruments);
        Mockito.when(client.getEvents()).thenReturn(events);
        Mockito.when(instruments.getPriceWidth()).thenReturn(10);
        Mockito.when(instruments.getSizeWidth()).thenReturn(10);

        Scanner arguments = new Scanner("");

        command.execute(client, arguments);

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("Order ID"));
        assertTrue(output.contains("Price"));
        assertTrue(output.contains("Quantity"));
    }

    @Test
    void executeWithTrades() {
        TerminalClient client = Mockito.mock(TerminalClient.class);
        Instruments instruments = Mockito.mock(Instruments.class);
        Instrument instrument = Mockito.mock(Instrument.class);
        Events events = new Events();

        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1_000_000_000L;
        ASCII.putLeft(accepted.orderId, "ORDER001");
        accepted.side = POE.BUY;
        accepted.instrument = ASCII.packLong("FOO");
        accepted.quantity = 100;
        accepted.price = 5000;
        accepted.orderNumber = 1;
        events.orderAccepted(accepted);

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 2_000_000_000L;
        ASCII.putLeft(executed.orderId, "ORDER001");
        executed.quantity = 50;
        executed.price = 5000;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1;
        events.orderExecuted(executed);

        Mockito.when(client.getInstruments()).thenReturn(instruments);
        Mockito.when(client.getEvents()).thenReturn(events);
        Mockito.when(instruments.getPriceWidth()).thenReturn(10);
        Mockito.when(instruments.getSizeWidth()).thenReturn(10);
        Mockito.when(instruments.get(Mockito.anyLong())).thenReturn(instrument);
        Mockito.when(instrument.getPriceFormat()).thenReturn("%10.2f");
        Mockito.when(instrument.getSizeFormat()).thenReturn("%10.2f");
        Mockito.when(instrument.getPriceFactor()).thenReturn(100.0);
        Mockito.when(instrument.getSizeFactor()).thenReturn(1.0);

        Scanner arguments = new Scanner("");

        command.execute(client, arguments);

        String output = outputStream.toString();
        assertTrue(output.contains("Timestamp"));
        assertTrue(output.contains("ORDER001"));
    }
}
