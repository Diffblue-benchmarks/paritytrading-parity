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

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdersTest {

    private Events events;

    @BeforeEach
    void setUp() {
        events = new Events();
    }

    @Test
    void emptyEvents() {
        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void singleOrderAccepted() {
        events.orderAccepted(newOrderAccepted(1000L, "order1          ", (byte) 'B', 100, 5000));

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals("order1          ", orders.get(0).getOrderId());
        assertEquals(100, orders.get(0).getQuantity());
        assertEquals((byte) 'B', orders.get(0).getSide());
    }

    @Test
    void multipleOrdersSortedByTimestamp() {
        events.orderAccepted(newOrderAccepted(3000L, "order3          ", (byte) 'S', 300, 7000));
        events.orderAccepted(newOrderAccepted(1000L, "order1          ", (byte) 'B', 100, 5000));
        events.orderAccepted(newOrderAccepted(2000L, "order2          ", (byte) 'B', 200, 6000));

        List<Order> orders = Orders.collect(events);

        assertEquals(3, orders.size());
        assertEquals("order1          ", orders.get(0).getOrderId());
        assertEquals("order2          ", orders.get(1).getOrderId());
        assertEquals("order3          ", orders.get(2).getOrderId());
    }

    @Test
    void partialExecution() {
        events.orderAccepted(newOrderAccepted(1000L, "order1          ", (byte) 'B', 100, 5000));
        events.orderExecuted(newOrderExecuted(2000L, "order1          ", 40, 5000));

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(60, orders.get(0).getQuantity());
    }

    @Test
    void fullExecution() {
        events.orderAccepted(newOrderAccepted(1000L, "order1          ", (byte) 'B', 100, 5000));
        events.orderExecuted(newOrderExecuted(2000L, "order1          ", 100, 5000));

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void partialCancellation() {
        events.orderAccepted(newOrderAccepted(1000L, "order1          ", (byte) 'S', 100, 5000));
        events.orderCanceled(newOrderCanceled(2000L, "order1          ", 30));

        List<Order> orders = Orders.collect(events);

        assertEquals(1, orders.size());
        assertEquals(70, orders.get(0).getQuantity());
    }

    @Test
    void fullCancellation() {
        events.orderAccepted(newOrderAccepted(1000L, "order1          ", (byte) 'S', 100, 5000));
        events.orderCanceled(newOrderCanceled(2000L, "order1          ", 100));

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void executeUnknownOrder() {
        events.orderExecuted(newOrderExecuted(2000L, "unknown         ", 50, 5000));

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void cancelUnknownOrder() {
        events.orderCanceled(newOrderCanceled(2000L, "unknown         ", 50));

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    @Test
    void executionAndCancellationCombined() {
        events.orderAccepted(newOrderAccepted(1000L, "order1          ", (byte) 'B', 100, 5000));
        events.orderExecuted(newOrderExecuted(2000L, "order1          ", 30, 5000));
        events.orderCanceled(newOrderCanceled(3000L, "order1          ", 70));

        List<Order> orders = Orders.collect(events);

        assertTrue(orders.isEmpty());
    }

    private static POE.OrderAccepted newOrderAccepted(long timestamp, String orderId,
            byte side, long quantity, long price) {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp   = timestamp;
        message.orderId     = ASCII.put(orderId);
        message.side        = side;
        message.instrument  = ASCII.packLong("FOO     ");
        message.quantity    = quantity;
        message.price       = price;
        message.orderNumber = 1;
        return message;
    }

    private static POE.OrderExecuted newOrderExecuted(long timestamp, String orderId,
            long quantity, long price) {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp     = timestamp;
        message.orderId       = ASCII.put(orderId);
        message.quantity      = quantity;
        message.price         = price;
        message.liquidityFlag = (byte) 'A';
        message.matchNumber   = 1;
        return message;
    }

    private static POE.OrderCanceled newOrderCanceled(long timestamp, String orderId,
            long canceledQuantity) {
        POE.OrderCanceled message = new POE.OrderCanceled();
        message.timestamp        = timestamp;
        message.orderId          = ASCII.put(orderId);
        message.canceledQuantity = canceledQuantity;
        message.reason           = (byte) 'R';
        return message;
    }
}
