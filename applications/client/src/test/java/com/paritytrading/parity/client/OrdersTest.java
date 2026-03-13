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

import java.util.List;

public class OrdersTest {

    @Test
    public void testCollectWithOrderAccepted() {
        Events events = new Events();

        POE.OrderAccepted poeMessage = createPOEOrderAccepted(1000L, "ORDER001", POE.BUY, 100L, 50L, 10000L, 1L);
        events.orderAccepted(poeMessage);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertEquals("ORDER001", order.getOrderId());
        assertEquals(POE.BUY, order.getSide());
        assertEquals(100L, order.getInstrument());
        assertEquals(50L, order.getQuantity());
    }

    @Test
    public void testCollectWithMultipleOrdersOrderedByTimestamp() {
        Events events = new Events();

        POE.OrderAccepted poeMessage1 = createPOEOrderAccepted(3000L, "ORDER003", POE.BUY, 100L, 50L, 10000L, 1L);
        POE.OrderAccepted poeMessage2 = createPOEOrderAccepted(1000L, "ORDER001", POE.SELL, 200L, 30L, 20000L, 2L);
        POE.OrderAccepted poeMessage3 = createPOEOrderAccepted(2000L, "ORDER002", POE.BUY, 150L, 40L, 15000L, 3L);

        events.orderAccepted(poeMessage1);
        events.orderAccepted(poeMessage2);
        events.orderAccepted(poeMessage3);

        List<Order> orders = Orders.collect(events);

        assertEquals(3, orders.size());
        assertEquals("ORDER001", orders.get(0).getOrderId());
        assertEquals(1000L, orders.get(0).getTimestamp());
        assertEquals("ORDER002", orders.get(1).getOrderId());
        assertEquals(2000L, orders.get(1).getTimestamp());
        assertEquals("ORDER003", orders.get(2).getOrderId());
        assertEquals(3000L, orders.get(2).getTimestamp());
    }

    @Test
    public void testCollectWithOrderExecutedPartial() {
        Events events = new Events();

        POE.OrderAccepted poeAccepted = createPOEOrderAccepted(1000L, "ORDER001", POE.BUY, 100L, 50L, 10000L, 1L);
        events.orderAccepted(poeAccepted);

        POE.OrderExecuted poeExecuted = createPOEOrderExecuted(2000L, "ORDER001", 20L, 10000L, POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, 1L);
        events.orderExecuted(poeExecuted);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(30L, orders.get(0).getQuantity());
    }

    @Test
    public void testCollectWithOrderExecutedFully() {
        Events events = new Events();

        POE.OrderAccepted poeAccepted = createPOEOrderAccepted(1000L, "ORDER001", POE.BUY, 100L, 50L, 10000L, 1L);
        events.orderAccepted(poeAccepted);

        POE.OrderExecuted poeExecuted = createPOEOrderExecuted(2000L, "ORDER001", 50L, 10000L, POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, 1L);
        events.orderExecuted(poeExecuted);

        List<Order> orders = Orders.collect(events);

        assertEquals(0, orders.size());
    }

    @Test
    public void testCollectWithOrderExecutedNonExistent() {
        Events events = new Events();

        POE.OrderExecuted poeExecuted = createPOEOrderExecuted(2000L, "ORDER999", 50L, 10000L, POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, 1L);
        events.orderExecuted(poeExecuted);

        List<Order> orders = Orders.collect(events);

        assertEquals(0, orders.size());
    }

    @Test
    public void testCollectWithOrderCanceledPartial() {
        Events events = new Events();

        POE.OrderAccepted poeAccepted = createPOEOrderAccepted(1000L, "ORDER001", POE.BUY, 100L, 50L, 10000L, 1L);
        events.orderAccepted(poeAccepted);

        POE.OrderCanceled poeCanceled = createPOEOrderCanceled(2000L, "ORDER001", 20L, POE.ORDER_CANCEL_REASON_REQUEST);
        events.orderCanceled(poeCanceled);

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(30L, orders.get(0).getQuantity());
    }

    @Test
    public void testCollectWithOrderCanceledFully() {
        Events events = new Events();

        POE.OrderAccepted poeAccepted = createPOEOrderAccepted(1000L, "ORDER001", POE.BUY, 100L, 50L, 10000L, 1L);
        events.orderAccepted(poeAccepted);

        POE.OrderCanceled poeCanceled = createPOEOrderCanceled(2000L, "ORDER001", 50L, POE.ORDER_CANCEL_REASON_REQUEST);
        events.orderCanceled(poeCanceled);

        List<Order> orders = Orders.collect(events);

        assertEquals(0, orders.size());
    }

    @Test
    public void testCollectWithOrderCanceledNonExistent() {
        Events events = new Events();

        POE.OrderCanceled poeCanceled = createPOEOrderCanceled(2000L, "ORDER999", 50L, POE.ORDER_CANCEL_REASON_REQUEST);
        events.orderCanceled(poeCanceled);

        List<Order> orders = Orders.collect(events);

        assertEquals(0, orders.size());
    }

    @Test
    public void testCollectWithMultipleExecutionsAndCancellations() {
        Events events = new Events();

        POE.OrderAccepted poeAccepted1 = createPOEOrderAccepted(1000L, "ORDER001", POE.BUY, 100L, 100L, 10000L, 1L);
        POE.OrderAccepted poeAccepted2 = createPOEOrderAccepted(1500L, "ORDER002", POE.SELL, 200L, 80L, 20000L, 2L);
        events.orderAccepted(poeAccepted1);
        events.orderAccepted(poeAccepted2);

        POE.OrderExecuted poeExecuted = createPOEOrderExecuted(2000L, "ORDER001", 30L, 10000L, POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY, 1L);
        events.orderExecuted(poeExecuted);

        POE.OrderCanceled poeCanceled = createPOEOrderCanceled(2500L, "ORDER002", 20L, POE.ORDER_CANCEL_REASON_REQUEST);
        events.orderCanceled(poeCanceled);

        List<Order> orders = Orders.collect(events);

        assertEquals(2, orders.size());
        assertEquals("ORDER001", orders.get(0).getOrderId());
        assertEquals(70L, orders.get(0).getQuantity());
        assertEquals("ORDER002", orders.get(1).getOrderId());
        assertEquals(60L, orders.get(1).getQuantity());
    }

    @Test
    public void testCollectWithEmptyEvents() {
        Events events = new Events();

        List<Order> orders = Orders.collect(events);

        assertEquals(0, orders.size());
    }

    private POE.OrderAccepted createPOEOrderAccepted(long timestamp, String orderId, byte side, long instrument, long quantity, long price, long orderNumber) {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp = timestamp;
        message.orderId = orderId.getBytes();
        message.side = side;
        message.instrument = instrument;
        message.quantity = quantity;
        message.price = price;
        message.orderNumber = orderNumber;
        return message;
    }

    private POE.OrderExecuted createPOEOrderExecuted(long timestamp, String orderId, long quantity, long price, byte liquidityFlag, long matchNumber) {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp = timestamp;
        message.orderId = orderId.getBytes();
        message.quantity = quantity;
        message.price = price;
        message.liquidityFlag = liquidityFlag;
        message.matchNumber = matchNumber;
        return message;
    }

    private POE.OrderCanceled createPOEOrderCanceled(long timestamp, String orderId, long canceledQuantity, byte reason) {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp = timestamp;
        message.orderId = orderId.getBytes();
        message.canceledQuantity = canceledQuantity;
        message.reason = reason;
        return message;
    }
}
