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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorsTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void testCollectWithNoEvents() {
        List<Error> errors = Errors.collect(events);

        assertTrue(errors.isEmpty());
    }

    @Test
    void testCollectWithSingleRejectedOrder() {
        POE.OrderRejected message = new POE.OrderRejected();
        message.timestamp = 1000000000L;
        ASCII.putLeft(message.orderId, "ORDER-001");
        message.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(message);
        List<Error> errors = Errors.collect(events);

        assertEquals(1, errors.size());
    }

    @Test
    void testCollectWithMultipleRejectedOrders() {
        POE.OrderRejected rejected1 = new POE.OrderRejected();
        rejected1.timestamp = 1000000000L;
        ASCII.putLeft(rejected1.orderId, "ORDER-001");
        rejected1.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected rejected2 = new POE.OrderRejected();
        rejected2.timestamp = 2000000000L;
        ASCII.putLeft(rejected2.orderId, "ORDER-002");
        rejected2.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        POE.OrderRejected rejected3 = new POE.OrderRejected();
        rejected3.timestamp = 3000000000L;
        ASCII.putLeft(rejected3.orderId, "ORDER-003");
        rejected3.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(rejected1);
        events.orderRejected(rejected2);
        events.orderRejected(rejected3);
        List<Error> errors = Errors.collect(events);

        assertEquals(3, errors.size());
    }

    @Test
    void testAcceptedOrdersDoNotCreateErrors() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        events.orderAccepted(accepted);
        List<Error> errors = Errors.collect(events);

        assertTrue(errors.isEmpty());
    }

    @Test
    void testExecutedOrdersDoNotCreateErrors() {
        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 1000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.quantity = 50L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderExecuted(executed);
        List<Error> errors = Errors.collect(events);

        assertTrue(errors.isEmpty());
    }

    @Test
    void testCanceledOrdersDoNotCreateErrors() {
        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 1000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 50L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderCanceled(canceled);
        List<Error> errors = Errors.collect(events);

        assertTrue(errors.isEmpty());
    }

    @Test
    void testMixedEventsOnlyRejectedCreateErrors() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 2000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-002");
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 3000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.quantity = 50L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderAccepted(accepted);
        events.orderRejected(rejected);
        events.orderExecuted(executed);
        List<Error> errors = Errors.collect(events);

        assertEquals(1, errors.size());
    }

    @Test
    void testErrorsPreserveInsertionOrder() {
        POE.OrderRejected rejected1 = new POE.OrderRejected();
        rejected1.timestamp = 3000000000L;
        ASCII.putLeft(rejected1.orderId, "ORDER-003");
        rejected1.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected rejected2 = new POE.OrderRejected();
        rejected2.timestamp = 1000000000L;
        ASCII.putLeft(rejected2.orderId, "ORDER-001");
        rejected2.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        POE.OrderRejected rejected3 = new POE.OrderRejected();
        rejected3.timestamp = 2000000000L;
        ASCII.putLeft(rejected3.orderId, "ORDER-002");
        rejected3.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        events.orderRejected(rejected1);
        events.orderRejected(rejected2);
        events.orderRejected(rejected3);
        List<Error> errors = Errors.collect(events);

        assertEquals(3, errors.size());
    }

    @Test
    void testCollectMultipleTimes() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-001");
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(rejected);
        List<Error> errors1 = Errors.collect(events);
        List<Error> errors2 = Errors.collect(events);

        assertEquals(1, errors1.size());
        assertEquals(1, errors2.size());
    }

    @Test
    void testDifferentRejectReasons() {
        POE.OrderRejected rejected1 = new POE.OrderRejected();
        rejected1.timestamp = 1000000000L;
        ASCII.putLeft(rejected1.orderId, "ORDER-001");
        rejected1.reason = POE.ORDER_REJECT_REASON_UNKNOWN_INSTRUMENT;

        POE.OrderRejected rejected2 = new POE.OrderRejected();
        rejected2.timestamp = 2000000000L;
        ASCII.putLeft(rejected2.orderId, "ORDER-002");
        rejected2.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        POE.OrderRejected rejected3 = new POE.OrderRejected();
        rejected3.timestamp = 3000000000L;
        ASCII.putLeft(rejected3.orderId, "ORDER-003");
        rejected3.reason = POE.ORDER_REJECT_REASON_INVALID_QUANTITY;

        events.orderRejected(rejected1);
        events.orderRejected(rejected2);
        events.orderRejected(rejected3);
        List<Error> errors = Errors.collect(events);

        assertEquals(3, errors.size());
    }
}
