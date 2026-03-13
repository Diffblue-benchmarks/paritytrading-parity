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

import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ErrorTest {

    @Test
    void testConstructorWithOrderRejected() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.orderId = toBytes("ORDER123");
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;
        poeMessage.timestamp = 123456789L;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        assertNotNull(error);
    }

    @Test
    void testFormatWithUnknownInstrument() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.orderId = toBytes("ORDER123");
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;
        poeMessage.timestamp = 123456789L;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertTrue(formatted.contains("ORDER123"));
        assertTrue(formatted.contains("Unknown instrument"));
    }

    @Test
    void testFormatWithInvalidPrice() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.orderId = toBytes("ORDER456");
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;
        poeMessage.timestamp = 123456789L;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertTrue(formatted.contains("ORDER456"));
        assertTrue(formatted.contains("Invalid price"));
    }

    @Test
    void testFormatWithInvalidQuantity() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.orderId = toBytes("ORDER789");
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;
        poeMessage.timestamp = 123456789L;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertTrue(formatted.contains("ORDER789"));
        assertTrue(formatted.contains("Invalid quantity"));
    }

    @Test
    void testDescribeWithUnknownReason() throws Exception {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.orderId = toBytes("ORDER999");
        poeMessage.reason = (byte) 'X';
        poeMessage.timestamp = 123456789L;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        Method describeMethod = Error.class.getDeclaredMethod("describe", byte.class);
        describeMethod.setAccessible(true);
        String result = (String) describeMethod.invoke(error, (byte) 'X');

        assertEquals("<unknown>", result);
    }

    @Test
    void testFormatWithUnknownReason() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.orderId = toBytes("ORDER999");
        poeMessage.reason = (byte) 'Z';
        poeMessage.timestamp = 123456789L;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertTrue(formatted.contains("ORDER999"));
        assertTrue(formatted.contains("<unknown>"));
    }

    @Test
    void testFormatOutputFormatting() {
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.orderId = toBytes("TEST");
        poeMessage.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;
        poeMessage.timestamp = 123456789L;

        Event.OrderRejected event = new Event.OrderRejected(poeMessage);
        Error error = new Error(event);

        String formatted = error.format();
        assertNotNull(formatted);
        assertTrue(formatted.contains("TEST"));
        assertTrue(formatted.contains("Invalid price"));
    }

    private byte[] toBytes(String s) {
        byte[] bytes = new byte[16];
        for (int i = 0; i < Math.min(s.length(), bytes.length); i++) {
            bytes[i] = (byte) s.charAt(i);
        }
        for (int i = s.length(); i < bytes.length; i++) {
            bytes[i] = ' ';
        }
        return bytes;
    }
}
