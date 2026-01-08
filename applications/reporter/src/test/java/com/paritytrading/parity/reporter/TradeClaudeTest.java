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

import org.junit.jupiter.api.Test;

class TradeClaudeTest {

    @Test
    void defaultConstructorCreatesInstanceWithNullStringsAndZeroNumericFields() {
        Trade trade = new Trade();

        assertNotNull(trade, "Trade instance should be created");
        assertNull(trade.timestamp, "timestamp should be null by default");
        assertEquals(0L, trade.matchNumber, "matchNumber should be 0 by default");
        assertNull(trade.instrument, "instrument should be null by default");
        assertEquals(0L, trade.quantity, "quantity should be 0 by default");
        assertEquals(0L, trade.price, "price should be 0 by default");
        assertNull(trade.buyer, "buyer should be null by default");
        assertEquals(0L, trade.buyOrderNumber, "buyOrderNumber should be 0 by default");
        assertNull(trade.seller, "seller should be null by default");
        assertEquals(0L, trade.sellOrderNumber, "sellOrderNumber should be 0 by default");
    }

    @Test
    void fieldsCanBeSetAfterConstruction() {
        Trade trade = new Trade();

        trade.timestamp = "09:30:00.123";
        trade.matchNumber = 12345L;
        trade.instrument = "AAPL";
        trade.quantity = 1000L;
        trade.price = 15050L;
        trade.buyer = "BUYER1";
        trade.buyOrderNumber = 100L;
        trade.seller = "SELLER1";
        trade.sellOrderNumber = 200L;

        assertEquals("09:30:00.123", trade.timestamp);
        assertEquals(12345L, trade.matchNumber);
        assertEquals("AAPL", trade.instrument);
        assertEquals(1000L, trade.quantity);
        assertEquals(15050L, trade.price);
        assertEquals("BUYER1", trade.buyer);
        assertEquals(100L, trade.buyOrderNumber);
        assertEquals("SELLER1", trade.seller);
        assertEquals(200L, trade.sellOrderNumber);
    }

    @Test
    void fieldsCanBeModified() {
        Trade trade = new Trade();

        trade.timestamp = "09:30:00.000";
        assertEquals("09:30:00.000", trade.timestamp);

        trade.timestamp = "10:45:30.456";
        assertEquals("10:45:30.456", trade.timestamp);

        trade.matchNumber = 100L;
        assertEquals(100L, trade.matchNumber);

        trade.matchNumber = 200L;
        assertEquals(200L, trade.matchNumber);
    }

    @Test
    void multipleInstancesAreIndependent() {
        Trade trade1 = new Trade();
        Trade trade2 = new Trade();

        trade1.timestamp = "09:30:00.123";
        trade1.matchNumber = 100L;
        trade1.instrument = "AAPL";

        trade2.timestamp = "10:45:00.456";
        trade2.matchNumber = 200L;
        trade2.instrument = "GOOG";

        assertEquals("09:30:00.123", trade1.timestamp);
        assertEquals(100L, trade1.matchNumber);
        assertEquals("AAPL", trade1.instrument);

        assertEquals("10:45:00.456", trade2.timestamp);
        assertEquals(200L, trade2.matchNumber);
        assertEquals("GOOG", trade2.instrument);

        assertNotSame(trade1, trade2, "Trade instances should be different objects");
    }

    @Test
    void stringFieldsCanBeSetToEmptyString() {
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

    @Test
    void numericFieldsCanBeSetToNegativeValues() {
        Trade trade = new Trade();

        trade.matchNumber = -1L;
        trade.quantity = -100L;
        trade.price = -5000L;
        trade.buyOrderNumber = -10L;
        trade.sellOrderNumber = -20L;

        assertEquals(-1L, trade.matchNumber);
        assertEquals(-100L, trade.quantity);
        assertEquals(-5000L, trade.price);
        assertEquals(-10L, trade.buyOrderNumber);
        assertEquals(-20L, trade.sellOrderNumber);
    }

    @Test
    void numericFieldsCanBeSetToMaxLongValue() {
        Trade trade = new Trade();

        trade.matchNumber = Long.MAX_VALUE;
        trade.quantity = Long.MAX_VALUE;
        trade.price = Long.MAX_VALUE;
        trade.buyOrderNumber = Long.MAX_VALUE;
        trade.sellOrderNumber = Long.MAX_VALUE;

        assertEquals(Long.MAX_VALUE, trade.matchNumber);
        assertEquals(Long.MAX_VALUE, trade.quantity);
        assertEquals(Long.MAX_VALUE, trade.price);
        assertEquals(Long.MAX_VALUE, trade.buyOrderNumber);
        assertEquals(Long.MAX_VALUE, trade.sellOrderNumber);
    }

    @Test
    void numericFieldsCanBeSetToMinLongValue() {
        Trade trade = new Trade();

        trade.matchNumber = Long.MIN_VALUE;
        trade.quantity = Long.MIN_VALUE;
        trade.price = Long.MIN_VALUE;
        trade.buyOrderNumber = Long.MIN_VALUE;
        trade.sellOrderNumber = Long.MIN_VALUE;

        assertEquals(Long.MIN_VALUE, trade.matchNumber);
        assertEquals(Long.MIN_VALUE, trade.quantity);
        assertEquals(Long.MIN_VALUE, trade.price);
        assertEquals(Long.MIN_VALUE, trade.buyOrderNumber);
        assertEquals(Long.MIN_VALUE, trade.sellOrderNumber);
    }
}
