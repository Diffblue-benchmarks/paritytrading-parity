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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorTest {

    @Test
    void testHeaderContainsExpectedText() {
        assertTrue(Error.HEADER.contains("Order ID"));
        assertTrue(Error.HEADER.contains("Reason"));
    }

    @Test
    void testFormatUnknownInstrument() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-001");
        rejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(rejected);
        Error error = new Error(event);

        String formatted = error.format();
        assertTrue(formatted.contains("ORDER-001"));
        assertTrue(formatted.contains("Unknown instrument"));
    }

    @Test
    void testFormatInvalidPrice() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-002");
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(rejected);
        Error error = new Error(event);

        String formatted = error.format();
        assertTrue(formatted.contains("ORDER-002"));
        assertTrue(formatted.contains("Invalid price"));
    }

    @Test
    void testFormatInvalidQuantity() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-003");
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event = new Event.OrderRejected(rejected);
        Error error = new Error(event);

        String formatted = error.format();
        assertTrue(formatted.contains("ORDER-003"));
        assertTrue(formatted.contains("Invalid quantity"));
    }

    @Test
    void testFormatUnknownReason() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-004");
        rejected.reason = (byte) 99;

        Event.OrderRejected event = new Event.OrderRejected(rejected);
        Error error = new Error(event);

        String formatted = error.format();
        assertTrue(formatted.contains("ORDER-004"));
        assertTrue(formatted.contains("<unknown>"));
    }

    @Test
    void testFormatContainsBothOrderIdAndReason() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-005");
        rejected.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(rejected);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
        assertTrue(formatted.length() > "ORDER-005".length());
    }

    @Test
    void testErrorCreatedFromEvent() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "TEST-ORDER");
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(rejected);
        Error error = new Error(event);

        assertNotNull(error);
        assertNotNull(error.format());
    }
}
