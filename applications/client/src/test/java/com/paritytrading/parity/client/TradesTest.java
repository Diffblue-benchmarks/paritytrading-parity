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

class TradesTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void testCollectWithNoEvents() {
        List<Trade> trades = Trades.collect(events);

        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectWithAcceptedButNoExecutions() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        events.orderAccepted(accepted);
        List<Trade> trades = Trades.collect(events);

        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectWithSingleTrade() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 2000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.quantity = 50L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderAccepted(accepted);
        events.orderExecuted(executed);
        List<Trade> trades = Trades.collect(events);

        assertEquals(1, trades.size());
        assertEquals(2000000000L, trades.get(0).getTimestamp());
    }

    @Test
    void testCollectWithMultipleTrades() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted exec1 = new POE.OrderExecuted();
        exec1.timestamp = 2000000000L;
        ASCII.putLeft(exec1.orderId, "ORDER-001");
        exec1.quantity = 30L;
        exec1.price = 15000L;
        exec1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec1.matchNumber = 1L;

        POE.OrderExecuted exec2 = new POE.OrderExecuted();
        exec2.timestamp = 3000000000L;
        ASCII.putLeft(exec2.orderId, "ORDER-001");
        exec2.quantity = 20L;
        exec2.price = 15000L;
        exec2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec2.matchNumber = 2L;

        events.orderAccepted(accepted);
        events.orderExecuted(exec1);
        events.orderExecuted(exec2);
        List<Trade> trades = Trades.collect(events);

        assertEquals(2, trades.size());
    }

    @Test
    void testExecutionWithoutAcceptedOrderIgnored() {
        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 1000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-UNKNOWN");
        executed.quantity = 50L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderExecuted(executed);
        List<Trade> trades = Trades.collect(events);

        assertTrue(trades.isEmpty());
    }

    @Test
    void testTradesFromMultipleOrders() {
        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        ASCII.putLeft(accepted1.orderId, "ORDER-001");
        accepted1.side = (byte) 'B';
        accepted1.instrument = ASCII.packLong("AAPL");
        accepted1.quantity = 100L;
        accepted1.price = 15000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 2000000000L;
        ASCII.putLeft(accepted2.orderId, "ORDER-002");
        accepted2.side = (byte) 'S';
        accepted2.instrument = ASCII.packLong("MSFT");
        accepted2.quantity = 200L;
        accepted2.price = 25000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted exec1 = new POE.OrderExecuted();
        exec1.timestamp = 3000000000L;
        ASCII.putLeft(exec1.orderId, "ORDER-001");
        exec1.quantity = 50L;
        exec1.price = 15000L;
        exec1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec1.matchNumber = 1L;

        POE.OrderExecuted exec2 = new POE.OrderExecuted();
        exec2.timestamp = 4000000000L;
        ASCII.putLeft(exec2.orderId, "ORDER-002");
        exec2.quantity = 100L;
        exec2.price = 25000L;
        exec2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        exec2.matchNumber = 2L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(exec1);
        events.orderExecuted(exec2);
        List<Trade> trades = Trades.collect(events);

        assertEquals(2, trades.size());
    }

    @Test
    void testTradesSortedByTimestamp() {
        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        ASCII.putLeft(accepted1.orderId, "ORDER-001");
        accepted1.side = (byte) 'B';
        accepted1.instrument = ASCII.packLong("AAPL");
        accepted1.quantity = 100L;
        accepted1.price = 15000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 2000000000L;
        ASCII.putLeft(accepted2.orderId, "ORDER-002");
        accepted2.side = (byte) 'S';
        accepted2.instrument = ASCII.packLong("MSFT");
        accepted2.quantity = 200L;
        accepted2.price = 25000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted exec1 = new POE.OrderExecuted();
        exec1.timestamp = 5000000000L;
        ASCII.putLeft(exec1.orderId, "ORDER-001");
        exec1.quantity = 50L;
        exec1.price = 15000L;
        exec1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec1.matchNumber = 1L;

        POE.OrderExecuted exec2 = new POE.OrderExecuted();
        exec2.timestamp = 3000000000L;
        ASCII.putLeft(exec2.orderId, "ORDER-002");
        exec2.quantity = 100L;
        exec2.price = 25000L;
        exec2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        exec2.matchNumber = 2L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(exec1);
        events.orderExecuted(exec2);
        List<Trade> trades = Trades.collect(events);

        assertEquals(2, trades.size());
        assertEquals(3000000000L, trades.get(0).getTimestamp());
        assertEquals(5000000000L, trades.get(1).getTimestamp());
    }

    @Test
    void testMultipleTradesSameMatchNumber() {
        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        ASCII.putLeft(accepted1.orderId, "ORDER-001");
        accepted1.side = (byte) 'B';
        accepted1.instrument = ASCII.packLong("AAPL");
        accepted1.quantity = 100L;
        accepted1.price = 15000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 2000000000L;
        ASCII.putLeft(accepted2.orderId, "ORDER-002");
        accepted2.side = (byte) 'S';
        accepted2.instrument = ASCII.packLong("AAPL");
        accepted2.quantity = 200L;
        accepted2.price = 15000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted exec1 = new POE.OrderExecuted();
        exec1.timestamp = 3000000000L;
        ASCII.putLeft(exec1.orderId, "ORDER-001");
        exec1.quantity = 50L;
        exec1.price = 15000L;
        exec1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec1.matchNumber = 1L;

        POE.OrderExecuted exec2 = new POE.OrderExecuted();
        exec2.timestamp = 3000000001L;
        ASCII.putLeft(exec2.orderId, "ORDER-002");
        exec2.quantity = 50L;
        exec2.price = 15000L;
        exec2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        exec2.matchNumber = 1L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(exec1);
        events.orderExecuted(exec2);
        List<Trade> trades = Trades.collect(events);

        assertEquals(2, trades.size());
    }

    @Test
    void testRejectedOrdersDoNotCreateTrades() {
        POE.OrderRejected rejected = new POE.OrderRejected();
        rejected.timestamp = 1000000000L;
        ASCII.putLeft(rejected.orderId, "ORDER-BAD");
        rejected.reason = POE.ORDER_REJECT_REASON_INVALID_PRICE;

        events.orderRejected(rejected);
        List<Trade> trades = Trades.collect(events);

        assertTrue(trades.isEmpty());
    }

    @Test
    void testCanceledOrdersDoNotCreateTrades() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 2000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 100L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(accepted);
        events.orderCanceled(canceled);
        List<Trade> trades = Trades.collect(events);

        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectMultipleTimes() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 2000000000L;
        ASCII.putLeft(executed.orderId, "ORDER-001");
        executed.quantity = 50L;
        executed.price = 15000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderAccepted(accepted);
        events.orderExecuted(executed);
        List<Trade> trades1 = Trades.collect(events);
        List<Trade> trades2 = Trades.collect(events);

        assertEquals(1, trades1.size());
        assertEquals(1, trades2.size());
    }

    @Test
    void testMixedEventTypesOnlyExecutionsCreateTrades() {
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

        POE.OrderCanceled canceled = new POE.OrderCanceled();
        canceled.timestamp = 4000000000L;
        ASCII.putLeft(canceled.orderId, "ORDER-001");
        canceled.canceledQuantity = 50L;
        canceled.reason = POE.ORDER_CANCEL_REASON_REQUEST;

        events.orderAccepted(accepted);
        events.orderRejected(rejected);
        events.orderExecuted(executed);
        events.orderCanceled(canceled);
        List<Trade> trades = Trades.collect(events);

        assertEquals(1, trades.size());
    }

    @Test
    void testPartialExecutionsCreateMultipleTrades() {
        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        ASCII.putLeft(accepted.orderId, "ORDER-001");
        accepted.side = (byte) 'B';
        accepted.instrument = ASCII.packLong("AAPL");
        accepted.quantity = 100L;
        accepted.price = 15000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted exec1 = new POE.OrderExecuted();
        exec1.timestamp = 2000000000L;
        ASCII.putLeft(exec1.orderId, "ORDER-001");
        exec1.quantity = 30L;
        exec1.price = 15000L;
        exec1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec1.matchNumber = 1L;

        POE.OrderExecuted exec2 = new POE.OrderExecuted();
        exec2.timestamp = 3000000000L;
        ASCII.putLeft(exec2.orderId, "ORDER-001");
        exec2.quantity = 30L;
        exec2.price = 15000L;
        exec2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec2.matchNumber = 2L;

        POE.OrderExecuted exec3 = new POE.OrderExecuted();
        exec3.timestamp = 4000000000L;
        ASCII.putLeft(exec3.orderId, "ORDER-001");
        exec3.quantity = 40L;
        exec3.price = 15000L;
        exec3.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        exec3.matchNumber = 3L;

        events.orderAccepted(accepted);
        events.orderExecuted(exec1);
        events.orderExecuted(exec2);
        events.orderExecuted(exec3);
        List<Trade> trades = Trades.collect(events);

        assertEquals(3, trades.size());
        assertEquals(2000000000L, trades.get(0).getTimestamp());
        assertEquals(3000000000L, trades.get(1).getTimestamp());
        assertEquals(4000000000L, trades.get(2).getTimestamp());
    }
}
