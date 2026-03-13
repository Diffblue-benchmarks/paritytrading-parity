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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradeTest {

    @Test
    void testTradeFieldsCanBeSet() {
        Trade trade = new Trade();
        trade.timestamp = "12:34:56.789";
        trade.matchNumber = 12345L;
        trade.instrument = "AAPL";
        trade.quantity = 100L;
        trade.price = 15000L;
        trade.buyer = "BUYER1";
        trade.buyOrderNumber = 1L;
        trade.seller = "SELLER1";
        trade.sellOrderNumber = 2L;

        assertEquals("12:34:56.789", trade.timestamp);
        assertEquals(12345L, trade.matchNumber);
        assertEquals("AAPL", trade.instrument);
        assertEquals(100L, trade.quantity);
        assertEquals(15000L, trade.price);
        assertEquals("BUYER1", trade.buyer);
        assertEquals(1L, trade.buyOrderNumber);
        assertEquals("SELLER1", trade.seller);
        assertEquals(2L, trade.sellOrderNumber);
    }

    @Test
    void testTradeDefaultValues() {
        Trade trade = new Trade();
        assertNull(trade.timestamp);
        assertEquals(0L, trade.matchNumber);
        assertNull(trade.instrument);
        assertEquals(0L, trade.quantity);
        assertEquals(0L, trade.price);
        assertNull(trade.buyer);
        assertEquals(0L, trade.buyOrderNumber);
        assertNull(trade.seller);
        assertEquals(0L, trade.sellOrderNumber);
    }

    @Test
    void testTradeFieldsAreIndependent() {
        Trade trade1 = new Trade();
        Trade trade2 = new Trade();

        trade1.matchNumber = 100L;
        trade2.matchNumber = 200L;

        assertEquals(100L, trade1.matchNumber);
        assertEquals(200L, trade2.matchNumber);
    }

    @Test
    void testTradeWithLargeValues() {
        Trade trade = new Trade();
        trade.matchNumber = Long.MAX_VALUE;
        trade.quantity = 1000000L;
        trade.price = 999999999L;
        trade.buyOrderNumber = Long.MAX_VALUE - 1;
        trade.sellOrderNumber = Long.MAX_VALUE - 2;

        assertEquals(Long.MAX_VALUE, trade.matchNumber);
        assertEquals(1000000L, trade.quantity);
        assertEquals(999999999L, trade.price);
        assertEquals(Long.MAX_VALUE - 1, trade.buyOrderNumber);
        assertEquals(Long.MAX_VALUE - 2, trade.sellOrderNumber);
    }

    @Test
    void testTradeWithEmptyStrings() {
        Trade trade = new Trade();
        trade.timestamp = "";
        trade.instrument = "";
        trade.buyer = "";
        trade.seller = "";

        assertEquals("", trade.timestamp);
        assertEquals("", trade.instrument);
        assertEquals("", trade.buyer);
        assertEquals("", trade.seller);
    }
}
