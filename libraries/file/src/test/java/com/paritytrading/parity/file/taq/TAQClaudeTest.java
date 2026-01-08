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
package com.paritytrading.parity.file.taq;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TAQClaudeTest {

    @Test
    void constructorInitializesFieldsToDefaults() {
        TAQ.Quote quote = new TAQ.Quote();

        assertNull(quote.date, "date should be null by default");
        assertEquals(0L, quote.timestampMillis, "timestampMillis should be 0 by default");
        assertNull(quote.instrument, "instrument should be null by default");
        assertEquals(0.0, quote.bidPrice, "bidPrice should be 0.0 by default");
        assertEquals(0.0, quote.bidSize, "bidSize should be 0.0 by default");
        assertEquals(0.0, quote.askPrice, "askPrice should be 0.0 by default");
        assertEquals(0.0, quote.askSize, "askSize should be 0.0 by default");
    }

    @Test
    void constructorCreatesNewInstance() {
        TAQ.Quote quote1 = new TAQ.Quote();
        TAQ.Quote quote2 = new TAQ.Quote();

        assertNotSame(quote1, quote2, "Each constructor call should create a new instance");
    }

    @Test
    void fieldsCanBeSetAndRetrieved() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.date = "2026-01-07";
        quote.timestampMillis = 12345678L;
        quote.instrument = "AAPL";
        quote.bidPrice = 150.25;
        quote.bidSize = 100.0;
        quote.askPrice = 150.50;
        quote.askSize = 200.0;

        assertEquals("2026-01-07", quote.date);
        assertEquals(12345678L, quote.timestampMillis);
        assertEquals("AAPL", quote.instrument);
        assertEquals(150.25, quote.bidPrice);
        assertEquals(100.0, quote.bidSize);
        assertEquals(150.50, quote.askPrice);
        assertEquals(200.0, quote.askSize);
    }

    @Test
    void fieldsCanBeSetToNull() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.date = "2026-01-07";
        quote.instrument = "AAPL";

        quote.date = null;
        quote.instrument = null;

        assertNull(quote.date);
        assertNull(quote.instrument);
    }

    @Test
    void numericFieldsCanBeSetToZero() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.timestampMillis = 100L;
        quote.bidPrice = 50.0;
        quote.bidSize = 10.0;
        quote.askPrice = 51.0;
        quote.askSize = 20.0;

        quote.timestampMillis = 0L;
        quote.bidPrice = 0.0;
        quote.bidSize = 0.0;
        quote.askPrice = 0.0;
        quote.askSize = 0.0;

        assertEquals(0L, quote.timestampMillis);
        assertEquals(0.0, quote.bidPrice);
        assertEquals(0.0, quote.bidSize);
        assertEquals(0.0, quote.askPrice);
        assertEquals(0.0, quote.askSize);
    }

    @Test
    void numericFieldsCanBeSetToNegativeValues() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.timestampMillis = -1000L;
        quote.bidPrice = -100.50;
        quote.bidSize = -50.0;
        quote.askPrice = -100.25;
        quote.askSize = -25.0;

        assertEquals(-1000L, quote.timestampMillis);
        assertEquals(-100.50, quote.bidPrice);
        assertEquals(-50.0, quote.bidSize);
        assertEquals(-100.25, quote.askPrice);
        assertEquals(-25.0, quote.askSize);
    }

    @Test
    void numericFieldsCanBeSetToMaxValues() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.timestampMillis = Long.MAX_VALUE;
        quote.bidPrice = Double.MAX_VALUE;
        quote.bidSize = Double.MAX_VALUE;
        quote.askPrice = Double.MAX_VALUE;
        quote.askSize = Double.MAX_VALUE;

        assertEquals(Long.MAX_VALUE, quote.timestampMillis);
        assertEquals(Double.MAX_VALUE, quote.bidPrice);
        assertEquals(Double.MAX_VALUE, quote.bidSize);
        assertEquals(Double.MAX_VALUE, quote.askPrice);
        assertEquals(Double.MAX_VALUE, quote.askSize);
    }

    @Test
    void numericFieldsCanBeSetToMinValues() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.timestampMillis = Long.MIN_VALUE;
        quote.bidPrice = Double.MIN_VALUE;
        quote.bidSize = Double.MIN_VALUE;
        quote.askPrice = Double.MIN_VALUE;
        quote.askSize = Double.MIN_VALUE;

        assertEquals(Long.MIN_VALUE, quote.timestampMillis);
        assertEquals(Double.MIN_VALUE, quote.bidPrice);
        assertEquals(Double.MIN_VALUE, quote.bidSize);
        assertEquals(Double.MIN_VALUE, quote.askPrice);
        assertEquals(Double.MIN_VALUE, quote.askSize);
    }

    @Test
    void doubleFieldsCanBeSetToSpecialValues() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.bidPrice = Double.NaN;
        quote.bidSize = Double.POSITIVE_INFINITY;
        quote.askPrice = Double.NEGATIVE_INFINITY;
        quote.askSize = -0.0;

        assertTrue(Double.isNaN(quote.bidPrice));
        assertEquals(Double.POSITIVE_INFINITY, quote.bidSize);
        assertEquals(Double.NEGATIVE_INFINITY, quote.askPrice);
        assertEquals(-0.0, quote.askSize);
    }

    @Test
    void stringFieldsCanBeSetToEmptyString() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.date = "";
        quote.instrument = "";

        assertEquals("", quote.date);
        assertEquals("", quote.instrument);
    }

    @Test
    void stringFieldsCanContainSpecialCharacters() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.date = "2026-01-07\t\n";
        quote.instrument = "TEST\tINSTRUMENT";

        assertEquals("2026-01-07\t\n", quote.date);
        assertEquals("TEST\tINSTRUMENT", quote.instrument);
    }

    @Test
    void multipleFieldUpdates() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.date = "2026-01-01";
        quote.timestampMillis = 1000L;
        quote.instrument = "FOO";
        quote.bidPrice = 100.0;
        quote.bidSize = 10.0;
        quote.askPrice = 101.0;
        quote.askSize = 20.0;

        assertEquals("2026-01-01", quote.date);
        assertEquals(1000L, quote.timestampMillis);

        quote.date = "2026-01-02";
        quote.timestampMillis = 2000L;
        quote.instrument = "BAR";
        quote.bidPrice = 200.0;
        quote.bidSize = 30.0;
        quote.askPrice = 201.0;
        quote.askSize = 40.0;

        assertEquals("2026-01-02", quote.date);
        assertEquals(2000L, quote.timestampMillis);
        assertEquals("BAR", quote.instrument);
        assertEquals(200.0, quote.bidPrice);
        assertEquals(30.0, quote.bidSize);
        assertEquals(201.0, quote.askPrice);
        assertEquals(40.0, quote.askSize);
    }

    @Test
    void quoteCanBeReusedAcrossMultipleUpdates() {
        TAQ.Quote quote = new TAQ.Quote();

        for (int i = 0; i < 100; i++) {
            quote.date = "2026-01-" + String.format("%02d", i % 31 + 1);
            quote.timestampMillis = i * 1000L;
            quote.instrument = "INST" + i;
            quote.bidPrice = i * 100.0;
            quote.bidSize = i * 10.0;
            quote.askPrice = i * 100.0 + 0.5;
            quote.askSize = i * 5.0;
        }

        assertEquals("2026-01-07", quote.date);
        assertEquals(99000L, quote.timestampMillis);
        assertEquals("INST99", quote.instrument);
        assertEquals(9900.0, quote.bidPrice);
        assertEquals(990.0, quote.bidSize);
        assertEquals(9900.5, quote.askPrice);
        assertEquals(495.0, quote.askSize);
    }

    @Test
    void quoteFieldsAreIndependent() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.bidPrice = 100.0;
        assertEquals(100.0, quote.bidPrice);
        assertEquals(0.0, quote.askPrice, "askPrice should not be affected");

        quote.askSize = 50.0;
        assertEquals(50.0, quote.askSize);
        assertEquals(0.0, quote.bidSize, "bidSize should not be affected");
    }

    @Test
    void longTimestampPrecision() {
        TAQ.Quote quote = new TAQ.Quote();

        long timestamp = 1704654000123L;
        quote.timestampMillis = timestamp;

        assertEquals(1704654000123L, quote.timestampMillis, "Timestamp precision should be preserved");
    }

    @Test
    void doublePricePrecision() {
        TAQ.Quote quote = new TAQ.Quote();

        quote.bidPrice = 100.123456789;
        quote.askPrice = 200.987654321;

        assertEquals(100.123456789, quote.bidPrice, 0.0000000001);
        assertEquals(200.987654321, quote.askPrice, 0.0000000001);
    }

    @Test
    void tradeConstructorInitializesFieldsToDefaults() {
        TAQ.Trade trade = new TAQ.Trade();

        assertNull(trade.date, "date should be null by default");
        assertEquals(0L, trade.timestampMillis, "timestampMillis should be 0 by default");
        assertNull(trade.instrument, "instrument should be null by default");
        assertEquals(0.0, trade.price, "price should be 0.0 by default");
        assertEquals(0.0, trade.size, "size should be 0.0 by default");
        assertEquals('\0', trade.side, "side should be null character by default");
    }

    @Test
    void tradeConstructorCreatesNewInstance() {
        TAQ.Trade trade1 = new TAQ.Trade();
        TAQ.Trade trade2 = new TAQ.Trade();

        assertNotSame(trade1, trade2, "Each constructor call should create a new instance");
    }

    @Test
    void tradeFieldsCanBeSetAndRetrieved() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.date = "2026-01-07";
        trade.timestampMillis = 12345678L;
        trade.instrument = "AAPL";
        trade.price = 150.75;
        trade.size = 100.0;
        trade.side = TAQ.BUY;

        assertEquals("2026-01-07", trade.date);
        assertEquals(12345678L, trade.timestampMillis);
        assertEquals("AAPL", trade.instrument);
        assertEquals(150.75, trade.price);
        assertEquals(100.0, trade.size);
        assertEquals(TAQ.BUY, trade.side);
    }

    @Test
    void tradeFieldsCanBeSetToNull() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.date = "2026-01-07";
        trade.instrument = "AAPL";

        trade.date = null;
        trade.instrument = null;

        assertNull(trade.date);
        assertNull(trade.instrument);
    }

    @Test
    void tradeNumericFieldsCanBeSetToZero() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.timestampMillis = 100L;
        trade.price = 50.0;
        trade.size = 10.0;

        trade.timestampMillis = 0L;
        trade.price = 0.0;
        trade.size = 0.0;

        assertEquals(0L, trade.timestampMillis);
        assertEquals(0.0, trade.price);
        assertEquals(0.0, trade.size);
    }

    @Test
    void tradeNumericFieldsCanBeSetToNegativeValues() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.timestampMillis = -1000L;
        trade.price = -100.50;
        trade.size = -50.0;

        assertEquals(-1000L, trade.timestampMillis);
        assertEquals(-100.50, trade.price);
        assertEquals(-50.0, trade.size);
    }

    @Test
    void tradeNumericFieldsCanBeSetToMaxValues() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.timestampMillis = Long.MAX_VALUE;
        trade.price = Double.MAX_VALUE;
        trade.size = Double.MAX_VALUE;

        assertEquals(Long.MAX_VALUE, trade.timestampMillis);
        assertEquals(Double.MAX_VALUE, trade.price);
        assertEquals(Double.MAX_VALUE, trade.size);
    }

    @Test
    void tradeNumericFieldsCanBeSetToMinValues() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.timestampMillis = Long.MIN_VALUE;
        trade.price = Double.MIN_VALUE;
        trade.size = Double.MIN_VALUE;

        assertEquals(Long.MIN_VALUE, trade.timestampMillis);
        assertEquals(Double.MIN_VALUE, trade.price);
        assertEquals(Double.MIN_VALUE, trade.size);
    }

    @Test
    void tradeDoubleFieldsCanBeSetToSpecialValues() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.price = Double.NaN;
        trade.size = Double.POSITIVE_INFINITY;

        assertTrue(Double.isNaN(trade.price));
        assertEquals(Double.POSITIVE_INFINITY, trade.size);

        trade.price = Double.NEGATIVE_INFINITY;
        trade.size = -0.0;

        assertEquals(Double.NEGATIVE_INFINITY, trade.price);
        assertEquals(-0.0, trade.size);
    }

    @Test
    void tradeStringFieldsCanBeSetToEmptyString() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.date = "";
        trade.instrument = "";

        assertEquals("", trade.date);
        assertEquals("", trade.instrument);
    }

    @Test
    void tradeStringFieldsCanContainSpecialCharacters() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.date = "2026-01-07\t\n";
        trade.instrument = "TEST\tINSTRUMENT";

        assertEquals("2026-01-07\t\n", trade.date);
        assertEquals("TEST\tINSTRUMENT", trade.instrument);
    }

    @Test
    void tradeSideCanBeSetToBuy() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.side = TAQ.BUY;

        assertEquals(TAQ.BUY, trade.side);
        assertEquals('B', trade.side);
    }

    @Test
    void tradeSideCanBeSetToSell() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.side = TAQ.SELL;

        assertEquals(TAQ.SELL, trade.side);
        assertEquals('S', trade.side);
    }

    @Test
    void tradeSideCanBeSetToUnknown() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.side = TAQ.UNKNOWN;

        assertEquals(TAQ.UNKNOWN, trade.side);
        assertEquals(' ', trade.side);
    }

    @Test
    void tradeSideCanBeSetToArbitraryCharacter() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.side = 'X';
        assertEquals('X', trade.side);

        trade.side = '1';
        assertEquals('1', trade.side);

        trade.side = '\t';
        assertEquals('\t', trade.side);
    }

    @Test
    void tradeMultipleFieldUpdates() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.date = "2026-01-01";
        trade.timestampMillis = 1000L;
        trade.instrument = "FOO";
        trade.price = 100.0;
        trade.size = 10.0;
        trade.side = TAQ.BUY;

        assertEquals("2026-01-01", trade.date);
        assertEquals(1000L, trade.timestampMillis);
        assertEquals(TAQ.BUY, trade.side);

        trade.date = "2026-01-02";
        trade.timestampMillis = 2000L;
        trade.instrument = "BAR";
        trade.price = 200.0;
        trade.size = 30.0;
        trade.side = TAQ.SELL;

        assertEquals("2026-01-02", trade.date);
        assertEquals(2000L, trade.timestampMillis);
        assertEquals("BAR", trade.instrument);
        assertEquals(200.0, trade.price);
        assertEquals(30.0, trade.size);
        assertEquals(TAQ.SELL, trade.side);
    }

    @Test
    void tradeCanBeReusedAcrossMultipleUpdates() {
        TAQ.Trade trade = new TAQ.Trade();

        for (int i = 0; i < 100; i++) {
            trade.date = "2026-01-" + String.format("%02d", i % 31 + 1);
            trade.timestampMillis = i * 1000L;
            trade.instrument = "INST" + i;
            trade.price = i * 100.0;
            trade.size = i * 10.0;
            trade.side = (i % 2 == 0) ? TAQ.BUY : TAQ.SELL;
        }

        assertEquals("2026-01-07", trade.date);
        assertEquals(99000L, trade.timestampMillis);
        assertEquals("INST99", trade.instrument);
        assertEquals(9900.0, trade.price);
        assertEquals(990.0, trade.size);
        assertEquals(TAQ.SELL, trade.side);
    }

    @Test
    void tradeFieldsAreIndependent() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.price = 100.0;
        assertEquals(100.0, trade.price);
        assertEquals(0.0, trade.size, "size should not be affected");

        trade.side = TAQ.BUY;
        assertEquals(TAQ.BUY, trade.side);
        assertNull(trade.instrument, "instrument should not be affected");
    }

    @Test
    void tradeLongTimestampPrecision() {
        TAQ.Trade trade = new TAQ.Trade();

        long timestamp = 1704654000123L;
        trade.timestampMillis = timestamp;

        assertEquals(1704654000123L, trade.timestampMillis, "Timestamp precision should be preserved");
    }

    @Test
    void tradeDoublePricePrecision() {
        TAQ.Trade trade = new TAQ.Trade();

        trade.price = 100.123456789;
        trade.size = 200.987654321;

        assertEquals(100.123456789, trade.price, 0.0000000001);
        assertEquals(200.987654321, trade.size, 0.0000000001);
    }

}
