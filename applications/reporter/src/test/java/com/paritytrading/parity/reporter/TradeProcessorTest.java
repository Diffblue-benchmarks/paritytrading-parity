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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.pmr.PMR;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TradeProcessorTest {

    private List<Trade> trades;
    private TradeProcessor processor;

    @BeforeEach
    void setUp() {
        trades = new ArrayList<>();

        processor = new TradeProcessor(new TradeListener() {
            @Override
            void trade(Trade event) {
                Trade copy = new Trade();
                copy.timestamp       = event.timestamp;
                copy.matchNumber     = event.matchNumber;
                copy.instrument      = event.instrument;
                copy.quantity        = event.quantity;
                copy.price           = event.price;
                copy.buyer           = event.buyer;
                copy.buyOrderNumber  = event.buyOrderNumber;
                copy.seller          = event.seller;
                copy.sellOrderNumber = event.sellOrderNumber;
                trades.add(copy);
            }
        });
    }

    @Test
    void orderEnteredStoresOrderFields() {
        PMR.OrderEntered entered = new PMR.OrderEntered();
        entered.timestamp   = 1_000_000_000L;
        entered.username    = ASCII.packLong("buyer   ");
        entered.orderNumber = 1;
        entered.side        = PMR.BUY;
        entered.instrument  = ASCII.packLong("FOO     ");
        entered.quantity    = 100;
        entered.price       = 5000;

        processor.orderEntered(entered);

        PMR.OrderEntered sell = new PMR.OrderEntered();
        sell.timestamp   = 2_000_000_000L;
        sell.username    = ASCII.packLong("seller  ");
        sell.orderNumber = 2;
        sell.side        = PMR.SELL;
        sell.instrument  = ASCII.packLong("FOO     ");
        sell.quantity    = 100;
        sell.price       = 5000;

        processor.orderEntered(sell);

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp           = 3_000_000_000L;
        trade.restingOrderNumber  = 1;
        trade.incomingOrderNumber = 2;
        trade.quantity            = 100;
        trade.matchNumber         = 1;

        processor.trade(trade);

        assertEquals(1, trades.size());

        Trade result = trades.get(0);
        assertEquals("buyer", result.buyer);
        assertEquals("seller", result.seller);
        assertEquals("FOO", result.instrument);
        assertEquals(100, result.quantity);
        assertEquals(5000, result.price);
        assertEquals(1, result.buyOrderNumber);
        assertEquals(2, result.sellOrderNumber);
    }

    @Test
    void orderEnteredWithSellSide() {
        PMR.OrderEntered entered = new PMR.OrderEntered();
        entered.timestamp   = 1_000_000_000L;
        entered.username    = ASCII.packLong("alice   ");
        entered.orderNumber = 10;
        entered.side        = PMR.SELL;
        entered.instrument  = ASCII.packLong("BAR     ");
        entered.quantity    = 50;
        entered.price       = 2500;

        processor.orderEntered(entered);

        PMR.OrderEntered buy = new PMR.OrderEntered();
        buy.timestamp   = 2_000_000_000L;
        buy.username    = ASCII.packLong("bob     ");
        buy.orderNumber = 11;
        buy.side        = PMR.BUY;
        buy.instrument  = ASCII.packLong("BAR     ");
        buy.quantity    = 50;
        buy.price       = 2500;

        processor.orderEntered(buy);

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp           = 3_000_000_000L;
        trade.restingOrderNumber  = 10;
        trade.incomingOrderNumber = 11;
        trade.quantity            = 50;
        trade.matchNumber         = 2;

        processor.trade(trade);

        assertEquals(1, trades.size());

        Trade result = trades.get(0);
        assertEquals("alice", result.seller);
        assertEquals("bob", result.buyer);
        assertEquals("BAR", result.instrument);
        assertEquals(50, result.quantity);
        assertEquals(2500, result.price);
        assertEquals(10, result.sellOrderNumber);
        assertEquals(11, result.buyOrderNumber);
    }
}
