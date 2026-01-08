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

class TradesClaudeTest {

    private static final byte[] ORDER_ID_1 = createOrderId("ORDER1");
    private static final byte[] ORDER_ID_2 = createOrderId("ORDER2");
    private static final byte[] ORDER_ID_3 = createOrderId("ORDER3");

    private static byte[] createOrderId(String id) {
        byte[] orderId = new byte[POE.ORDER_ID_LENGTH];
        byte[] bytes = id.getBytes();
        System.arraycopy(bytes, 0, orderId, 0, Math.min(bytes.length, orderId.length));
        return orderId;
    }

    @Test
    void testCollectWithEmptyEvents() {
        Events events = new Events();
        List<Trade> trades = Trades.collect(events);
        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectWithOnlyOrderAccepted() {
        Events events = new Events();

        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        accepted.orderId = ORDER_ID_1;
        accepted.side = POE.BUY;
        accepted.instrument = 1L;
        accepted.quantity = 100L;
        accepted.price = 5000L;
        accepted.orderNumber = 1L;

        events.orderAccepted(accepted);

        List<Trade> trades = Trades.collect(events);
        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectWithOrderExecutedWithoutAccepted() {
        Events events = new Events();

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 2000000000L;
        executed.orderId = ORDER_ID_1;
        executed.quantity = 50L;
        executed.price = 5000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderExecuted(executed);

        List<Trade> trades = Trades.collect(events);
        assertTrue(trades.isEmpty());
    }

    @Test
    void testCollectWithSingleTrade() {
        Events events = new Events();

        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        accepted.orderId = ORDER_ID_1;
        accepted.side = POE.BUY;
        accepted.instrument = 1L;
        accepted.quantity = 100L;
        accepted.price = 5000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted executed = new POE.OrderExecuted();
        executed.timestamp = 2000000000L;
        executed.orderId = ORDER_ID_1;
        executed.quantity = 50L;
        executed.price = 5000L;
        executed.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed.matchNumber = 1L;

        events.orderAccepted(accepted);
        events.orderExecuted(executed);

        List<Trade> trades = Trades.collect(events);
        assertEquals(1, trades.size());
        assertEquals(2000000000L, trades.get(0).getTimestamp());
    }

    @Test
    void testCollectWithMultipleTradesSameOrder() {
        Events events = new Events();

        POE.OrderAccepted accepted = new POE.OrderAccepted();
        accepted.timestamp = 1000000000L;
        accepted.orderId = ORDER_ID_1;
        accepted.side = POE.BUY;
        accepted.instrument = 1L;
        accepted.quantity = 100L;
        accepted.price = 5000L;
        accepted.orderNumber = 1L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 2000000000L;
        executed1.orderId = ORDER_ID_1;
        executed1.quantity = 30L;
        executed1.price = 5000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 3000000000L;
        executed2.orderId = ORDER_ID_1;
        executed2.quantity = 40L;
        executed2.price = 5000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed2.matchNumber = 2L;

        events.orderAccepted(accepted);
        events.orderExecuted(executed1);
        events.orderExecuted(executed2);

        List<Trade> trades = Trades.collect(events);
        assertEquals(2, trades.size());
        assertEquals(2000000000L, trades.get(0).getTimestamp());
        assertEquals(3000000000L, trades.get(1).getTimestamp());
    }

    @Test
    void testCollectWithMultipleOrders() {
        Events events = new Events();

        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = ORDER_ID_1;
        accepted1.side = POE.BUY;
        accepted1.instrument = 1L;
        accepted1.quantity = 100L;
        accepted1.price = 5000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 1500000000L;
        accepted2.orderId = ORDER_ID_2;
        accepted2.side = POE.SELL;
        accepted2.instrument = 2L;
        accepted2.quantity = 200L;
        accepted2.price = 6000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 2000000000L;
        executed1.orderId = ORDER_ID_1;
        executed1.quantity = 50L;
        executed1.price = 5000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 2500000000L;
        executed2.orderId = ORDER_ID_2;
        executed2.quantity = 100L;
        executed2.price = 6000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed2.matchNumber = 2L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(executed1);
        events.orderExecuted(executed2);

        List<Trade> trades = Trades.collect(events);
        assertEquals(2, trades.size());
        assertEquals(2000000000L, trades.get(0).getTimestamp());
        assertEquals(2500000000L, trades.get(1).getTimestamp());
    }

    @Test
    void testCollectWithSameMatchNumber() {
        Events events = new Events();

        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = ORDER_ID_1;
        accepted1.side = POE.BUY;
        accepted1.instrument = 1L;
        accepted1.quantity = 100L;
        accepted1.price = 5000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 1500000000L;
        accepted2.orderId = ORDER_ID_2;
        accepted2.side = POE.SELL;
        accepted2.instrument = 1L;
        accepted2.quantity = 100L;
        accepted2.price = 5000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 2000000000L;
        executed1.orderId = ORDER_ID_1;
        executed1.quantity = 100L;
        executed1.price = 5000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 2000000001L;
        executed2.orderId = ORDER_ID_2;
        executed2.quantity = 100L;
        executed2.price = 5000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed2.matchNumber = 1L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(executed1);
        events.orderExecuted(executed2);

        List<Trade> trades = Trades.collect(events);
        assertEquals(2, trades.size());
    }

    @Test
    void testCollectSortsTradesByTimestamp() {
        Events events = new Events();

        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = ORDER_ID_1;
        accepted1.side = POE.BUY;
        accepted1.instrument = 1L;
        accepted1.quantity = 100L;
        accepted1.price = 5000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 1500000000L;
        accepted2.orderId = ORDER_ID_2;
        accepted2.side = POE.SELL;
        accepted2.instrument = 1L;
        accepted2.quantity = 100L;
        accepted2.price = 5000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 3000000000L;
        executed1.orderId = ORDER_ID_1;
        executed1.quantity = 50L;
        executed1.price = 5000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed1.matchNumber = 2L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 2000000000L;
        executed2.orderId = ORDER_ID_2;
        executed2.quantity = 50L;
        executed2.price = 5000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed2.matchNumber = 1L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderExecuted(executed1);
        events.orderExecuted(executed2);

        List<Trade> trades = Trades.collect(events);
        assertEquals(2, trades.size());
        assertEquals(2000000000L, trades.get(0).getTimestamp());
        assertEquals(3000000000L, trades.get(1).getTimestamp());
    }

    @Test
    void testCollectWithMixedAcceptedAndExecuted() {
        Events events = new Events();

        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = ORDER_ID_1;
        accepted1.side = POE.BUY;
        accepted1.instrument = 1L;
        accepted1.quantity = 100L;
        accepted1.price = 5000L;
        accepted1.orderNumber = 1L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 2000000000L;
        executed1.orderId = ORDER_ID_1;
        executed1.quantity = 50L;
        executed1.price = 5000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 2500000000L;
        accepted2.orderId = ORDER_ID_2;
        accepted2.side = POE.SELL;
        accepted2.instrument = 1L;
        accepted2.quantity = 100L;
        accepted2.price = 5000L;
        accepted2.orderNumber = 2L;

        POE.OrderExecuted executed2 = new POE.OrderExecuted();
        executed2.timestamp = 3000000000L;
        executed2.orderId = ORDER_ID_2;
        executed2.quantity = 50L;
        executed2.price = 5000L;
        executed2.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        executed2.matchNumber = 2L;

        events.orderAccepted(accepted1);
        events.orderExecuted(executed1);
        events.orderAccepted(accepted2);
        events.orderExecuted(executed2);

        List<Trade> trades = Trades.collect(events);
        assertEquals(2, trades.size());
        assertEquals(2000000000L, trades.get(0).getTimestamp());
        assertEquals(3000000000L, trades.get(1).getTimestamp());
    }

    @Test
    void testCollectWithPartialExecutions() {
        Events events = new Events();

        POE.OrderAccepted accepted1 = new POE.OrderAccepted();
        accepted1.timestamp = 1000000000L;
        accepted1.orderId = ORDER_ID_1;
        accepted1.side = POE.BUY;
        accepted1.instrument = 1L;
        accepted1.quantity = 100L;
        accepted1.price = 5000L;
        accepted1.orderNumber = 1L;

        POE.OrderAccepted accepted2 = new POE.OrderAccepted();
        accepted2.timestamp = 1500000000L;
        accepted2.orderId = ORDER_ID_2;
        accepted2.side = POE.SELL;
        accepted2.instrument = 1L;
        accepted2.quantity = 200L;
        accepted2.price = 5000L;
        accepted2.orderNumber = 2L;

        POE.OrderAccepted accepted3 = new POE.OrderAccepted();
        accepted3.timestamp = 1600000000L;
        accepted3.orderId = ORDER_ID_3;
        accepted3.side = POE.BUY;
        accepted3.instrument = 1L;
        accepted3.quantity = 150L;
        accepted3.price = 5000L;
        accepted3.orderNumber = 3L;

        POE.OrderExecuted executed1 = new POE.OrderExecuted();
        executed1.timestamp = 2000000000L;
        executed1.orderId = ORDER_ID_1;
        executed1.quantity = 50L;
        executed1.price = 5000L;
        executed1.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed1.matchNumber = 1L;

        POE.OrderExecuted executed3 = new POE.OrderExecuted();
        executed3.timestamp = 3000000000L;
        executed3.orderId = ORDER_ID_3;
        executed3.quantity = 100L;
        executed3.price = 5000L;
        executed3.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        executed3.matchNumber = 2L;

        events.orderAccepted(accepted1);
        events.orderAccepted(accepted2);
        events.orderAccepted(accepted3);
        events.orderExecuted(executed1);
        events.orderExecuted(executed3);

        List<Trade> trades = Trades.collect(events);
        assertEquals(2, trades.size());
        assertEquals(2000000000L, trades.get(0).getTimestamp());
        assertEquals(3000000000L, trades.get(1).getTimestamp());
    }

}
