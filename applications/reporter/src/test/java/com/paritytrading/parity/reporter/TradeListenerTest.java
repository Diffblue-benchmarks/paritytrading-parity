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
package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TradeListenerTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream captured;

    private List<Trade> trades;
    private TradeListener listener;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        captured    = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));

        trades   = new ArrayList<>();
        listener = new TradeListener() {
            @Override
            void trade(Trade event) {
                trades.add(event);
            }
        };
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void tradeDispatchesEvent() {
        Trade event = new Trade();
        event.timestamp       = "12:00:00.000";
        event.matchNumber     = 1;
        event.instrument      = "FOO";
        event.quantity        = 100;
        event.price           = 5000;
        event.buyer           = "buyer";
        event.buyOrderNumber  = 1;
        event.seller          = "seller";
        event.sellOrderNumber = 2;

        listener.trade(event);

        assertEquals(1, trades.size());
        assertSame(event, trades.get(0));
    }

    @Test
    void printfWritesToStdout() {
        listener.printf("Hello %s", "World");

        String output = captured.toString();
        assertEquals("Hello World", output);
    }

    @Test
    void printfUsesUSLocaleFormatting() {
        listener.printf("%.2f", 1234.5);

        String output = captured.toString();
        assertEquals("1234.50", output);
    }

    @Test
    void printfWithMultipleArgs() {
        listener.printf("%s %d %s", "order", 42, "filled");

        String output = captured.toString();
        assertEquals("order 42 filled", output);
    }
}
