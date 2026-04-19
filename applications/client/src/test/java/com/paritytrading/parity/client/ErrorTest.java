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
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ErrorTest {

    @Test
    void formatWithUnknownInstrumentReason() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 1000L;
        message.orderId   = "ABC             ".getBytes(StandardCharsets.US_ASCII);
        message.reason    = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        Event.OrderRejected event = new Event.OrderRejected(message);
        Error error = new Error(event);

        String result = error.format();

        assertEquals("ABC              Unknown instrument", result);
    }

    @Test
    void formatWithInvalidPriceReason() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 2000L;
        message.orderId   = "ORDER123        ".getBytes(StandardCharsets.US_ASCII);
        message.reason    = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        Event.OrderRejected event = new Event.OrderRejected(message);
        Error error = new Error(event);

        String result = error.format();

        assertEquals("ORDER123         Invalid price     ", result);
    }

    @Test
    void formatWithInvalidQuantityReason() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 3000L;
        message.orderId   = "ORD456          ".getBytes(StandardCharsets.US_ASCII);
        message.reason    = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        Event.OrderRejected event = new Event.OrderRejected(message);
        Error error = new Error(event);

        String result = error.format();

        assertEquals("ORD456           Invalid quantity  ", result);
    }

    @Test
    void formatWithUnknownReason() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 4000L;
        message.orderId   = "XYZ999          ".getBytes(StandardCharsets.US_ASCII);
        message.reason    = (byte) 'Z';

        Event.OrderRejected event = new Event.OrderRejected(message);
        Error error = new Error(event);

        String result = error.format();

        assertEquals("XYZ999           <unknown>         ", result);
    }
}
