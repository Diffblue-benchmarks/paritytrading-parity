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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TradeListenerTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testTrade() {
        Trade trade = new Trade();
        trade.timestamp = "2024-01-01 10:00:00";
        trade.matchNumber = 123;
        trade.instrument = "AAPL";
        trade.quantity = 100;
        trade.price = 15000;
        trade.buyer = "BUYER1";
        trade.buyOrderNumber = 1001;
        trade.seller = "SELLER1";
        trade.sellOrderNumber = 2001;

        TestTradeListener listener = new TestTradeListener();
        listener.trade(trade);

        assertNotNull(listener.lastTrade);
        assertEquals(trade, listener.lastTrade);
    }

    @Test
    public void testPrintf() {
        TestTradeListener listener = new TestTradeListener();
        listener.printf("Test message: %s %d", "value", 42);

        String output = outputStream.toString();
        assertEquals("Test message: value 42", output);
    }

    @Test
    public void testPrintfWithMultipleArgs() {
        TestTradeListener listener = new TestTradeListener();
        listener.printf("Trade: %s Qty: %d Price: %.2f", "AAPL", 100, 150.50);

        String output = outputStream.toString();
        assertEquals("Trade: AAPL Qty: 100 Price: 150.50", output);
    }

    private static class TestTradeListener extends TradeListener {
        Trade lastTrade;

        @Override
        void trade(Trade event) {
            this.lastTrade = event;
        }
    }
}
