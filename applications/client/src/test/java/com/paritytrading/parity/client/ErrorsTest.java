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
import java.util.List;
import org.junit.jupiter.api.Test;

class ErrorsTest {

    @Test
    void collectWithNoEvents() {
        Events events = new Events();

        List<Error> errors = Errors.collect(events);

        assertTrue(errors.isEmpty());
    }

    @Test
    void collectWithOneRejectedOrder() {
        Events events = new Events();

        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 100L;
        message.orderId   = new byte[] { 'A', 'B', 'C', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message.reason    = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(message);

        List<Error> errors = Errors.collect(events);

        assertEquals(1, errors.size());
    }

    @Test
    void collectWithMultipleRejectedOrders() {
        Events events = new Events();

        POE.OrderRejected message1 = new POE.OrderRejected();
        message1.timestamp = 100L;
        message1.orderId   = new byte[] { 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message1.reason    = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected message2 = new POE.OrderRejected();
        message2.timestamp = 200L;
        message2.orderId   = new byte[] { 'B', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        message2.reason    = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(message1);
        events.orderRejected(message2);

        List<Error> errors = Errors.collect(events);

        assertEquals(2, errors.size());
    }

    @Test
    void collectIgnoresNonRejectedEvents() {
        Events events = new Events();

        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp   = 100L;
        accepted.orderId     = new byte[] { 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        accepted.side        = (byte) 'B';
        accepted.instrument  = 0L;
        accepted.quantity    = 100L;
        accepted.price       = 1000L;
        accepted.orderNumber = 1L;

        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 200L;
        rejected.orderId   = new byte[] { 'B', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        rejected.reason    = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp     = 300L;
        executed.orderId       = new byte[] { 'C', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
        executed.quantity      = 50L;
        executed.price         = 1000L;
        executed.liquidityFlag = (byte) 'A';
        executed.matchNumber   = 1L;

        events.orderAccepted(accepted);
        events.orderRejected(rejected);
        events.orderExecuted(executed);

        List<Error> errors = Errors.collect(events);

        assertEquals(1, errors.size());
    }
}
