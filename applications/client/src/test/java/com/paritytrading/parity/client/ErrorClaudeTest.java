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

import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

class ErrorClaudeTest {

    @Test
    void testConstructorWithUnknownInstrumentReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        assertNotNull(error);
    }

    @Test
    void testConstructorWithInvalidPriceReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 2000L;
        poeMessage.orderId = "ORDER456".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        assertNotNull(error);
    }

    @Test
    void testConstructorWithInvalidQuantityReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 3000L;
        poeMessage.orderId = "ORDER789".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        assertNotNull(error);
    }

    @Test
    void testConstructorWithEmptyOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        assertNotNull(error);
    }

    @Test
    void testConstructorWithUnknownReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = (byte) 'X';

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        assertNotNull(error);
    }

    @Test
    void testFormatWithUnknownInstrumentReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("Unknown instrument"));
    }

    @Test
    void testFormatWithInvalidPriceReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 2000L;
        poeMessage.orderId = "ORDER456".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER456"));
        assertTrue(formatted.contains("Invalid price"));
    }

    @Test
    void testFormatWithInvalidQuantityReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 3000L;
        poeMessage.orderId = "ORDER789".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER789"));
        assertTrue(formatted.contains("Invalid quantity"));
    }

    @Test
    void testFormatWithUnknownReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER999".getBytes();
        poeMessage.reason = (byte) 'X';

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER999"));
        assertTrue(formatted.contains("<unknown>"));
    }

    @Test
    void testFormatWithShortOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORD".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORD"));
        assertTrue(formatted.contains("Invalid price"));
    }

    @Test
    void testFormatWithLongOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "VERYLONGORDERID1".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("Unknown instrument"));
    }

    @Test
    void testFormatStructure() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();

        // The format should be: %16s %-18s with a space between them
        // This means 16 characters for order ID (right-aligned), 1 space, and 18 for reason (left-aligned)
        assertEquals(35, formatted.length());
    }

    @Test
    void testFormatWithEmptyOrderId() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = new byte[POE.ORDER_ID_LENGTH];
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("Invalid price"));
    }

    @Test
    void testFormatConsistencyAcrossMultipleInvocations() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted1 = error.format();
        String formatted2 = error.format();
        String formatted3 = error.format();

        assertEquals(formatted1, formatted2);
        assertEquals(formatted2, formatted3);
    }

    @Test
    void testMultipleErrorsWithDifferentReasons() {
        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.timestamp = 1000L;
        poeMessage1.orderId = "ORDER1".getBytes();
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.timestamp = 2000L;
        poeMessage2.orderId = "ORDER2".getBytes();
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected poeMessage3 = new POE.OrderRejected();
        poeMessage3.timestamp = 3000L;
        poeMessage3.orderId = "ORDER3".getBytes();
        poeMessage3.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event1 = new Event.OrderRejected(poeMessage1);
        Event.OrderRejected event2 = new Event.OrderRejected(poeMessage2);
        Event.OrderRejected event3 = new Event.OrderRejected(poeMessage3);

        Error error1 = new Error(event1);
        Error error2 = new Error(event2);
        Error error3 = new Error(event3);

        String formatted1 = error1.format();
        String formatted2 = error2.format();
        String formatted3 = error3.format();

        assertTrue(formatted1.contains("Unknown instrument"));
        assertTrue(formatted2.contains("Invalid price"));
        assertTrue(formatted3.contains("Invalid quantity"));
    }

    @Test
    void testHeaderConstant() {
        String header = Error.HEADER;
        assertNotNull(header);
        assertTrue(header.contains("Order ID"));
        assertTrue(header.contains("Reason"));
    }

    @Test
    void testFormatAlignmentWithHeader() {
        String header = Error.HEADER;
        String[] headerLines = header.split("\n");

        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();

        // The formatted output should align with the header
        assertTrue(headerLines.length > 0);
        assertEquals(35, formatted.length());
    }

    @Test
    void testFormatWithZeroByteReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = (byte) 0;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("<unknown>"));
    }

    @Test
    void testFormatWithNegativeByteReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.timestamp = 1000L;
        poeMessage.orderId = "ORDER123".getBytes();
        poeMessage.reason = (byte) -1;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("<unknown>"));
    }
}
