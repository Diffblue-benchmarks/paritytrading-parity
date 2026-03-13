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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorsTest {

    @Test
    void testCollectWithNoEvents() {
        Events events = new Events();

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    void testCollectWithSingleOrderRejected() {
        Events events = new Events();
        POE.OrderRejected poeMessage = new POE.OrderRejected();
        poeMessage.orderId = toBytes("ORDER123");
        poeMessage.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;
        poeMessage.timestamp = 123456789L;

        events.orderRejected(poeMessage);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(1, errors.size());
    }

    @Test
    void testCollectWithMultipleOrderRejected() {
        Events events = new Events();

        POE.OrderRejected poeMessage1 = new POE.OrderRejected();
        poeMessage1.orderId = toBytes("ORDER123");
        poeMessage1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;
        poeMessage1.timestamp = 123456789L;
        events.orderRejected(poeMessage1);

        POE.OrderRejected poeMessage2 = new POE.OrderRejected();
        poeMessage2.orderId = toBytes("ORDER456");
        poeMessage2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;
        poeMessage2.timestamp = 987654321L;
        events.orderRejected(poeMessage2);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(2, errors.size());
    }

    @Test
    void testCollectWithMixedEvents() {
        Events events = new Events();

        POE.OrderAccepted acceptedMessage = new POE.OrderAccepted();
        acceptedMessage.orderId = toBytes("ORDER000");
        acceptedMessage.side = POE.BUY;
        acceptedMessage.instrument = 1L;
        acceptedMessage.quantity = 100L;
        acceptedMessage.price = 1000L;
        acceptedMessage.orderNumber = 1L;
        acceptedMessage.timestamp = 100000L;
        events.orderAccepted(acceptedMessage);

        POE.OrderRejected rejectedMessage = new POE.OrderRejected();
        rejectedMessage.orderId = toBytes("ORDER123");
        rejectedMessage.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;
        rejectedMessage.timestamp = 123456789L;
        events.orderRejected(rejectedMessage);

        POE.OrderExecuted executedMessage = new POE.OrderExecuted();
        executedMessage.orderId = toBytes("ORDER000");
        executedMessage.quantity = 50L;
        executedMessage.price = 1000L;
        executedMessage.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executedMessage.matchNumber = 1L;
        executedMessage.timestamp = 200000L;
        events.orderExecuted(executedMessage);

        List<Error> errors = Errors.collect(events);

        assertNotNull(errors);
        assertEquals(1, errors.size());
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
