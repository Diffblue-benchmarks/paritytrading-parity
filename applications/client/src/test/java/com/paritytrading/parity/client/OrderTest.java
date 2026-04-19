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
import static org.mockito.Mockito.*;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Order order;

    private static final long   TIMESTAMP  = 36_000_000_000_000L;
    private static final byte   SIDE       = POE.BUY;
    private static final long   INSTRUMENT = ASCII.packLong("FOO     ");
    private static final long   QUANTITY   = 1000;
    private static final long   PRICE      = 5000;
    private static final long   ORDER_NUM  = 1;
    private static final String ORDER_ID   = "ABC             ";

    @BeforeEach
    void setUp() {
        POE.OrderAccepted poeMessage = new POE.OrderAccepted();
        poeMessage.timestamp   = TIMESTAMP;
        ASCII.putLeft(poeMessage.orderId, "ABC");
        poeMessage.side        = SIDE;
        poeMessage.instrument  = INSTRUMENT;
        poeMessage.quantity    = QUANTITY;
        poeMessage.price       = PRICE;
        poeMessage.orderNumber = ORDER_NUM;

        Event.OrderAccepted event = new Event.OrderAccepted(poeMessage);
        order = new Order(event);
    }

    @Test
    void constructorSetsFields() {
        assertEquals(TIMESTAMP, order.getTimestamp());
        assertEquals(SIDE, order.getSide());
        assertEquals(INSTRUMENT, order.getInstrument());
        assertEquals(QUANTITY, order.getQuantity());
    }

    @Test
    void getOrderId() {
        assertNotNull(order.getOrderId());
    }

    @Test
    void applyOrderExecuted() {
        POE.OrderExecuted poeExec = new POE.OrderExecuted();
        poeExec.timestamp     = TIMESTAMP + 1;
        ASCII.putLeft(poeExec.orderId, "ABC");
        poeExec.quantity      = 300;
        poeExec.price         = PRICE;
        poeExec.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeExec.matchNumber   = 1;

        Event.OrderExecuted event = new Event.OrderExecuted(poeExec);
        order.apply(event);

        assertEquals(QUANTITY - 300, order.getQuantity());
    }

    @Test
    void applyMultipleExecutions() {
        POE.OrderExecuted poeExec1 = new POE.OrderExecuted();
        poeExec1.timestamp     = TIMESTAMP + 1;
        ASCII.putLeft(poeExec1.orderId, "ABC");
        poeExec1.quantity      = 200;
        poeExec1.price         = PRICE;
        poeExec1.liquidityFlag = POE.LIQUIDITY_FLAG_ADDED_LIQUIDITY;
        poeExec1.matchNumber   = 1;

        POE.OrderExecuted poeExec2 = new POE.OrderExecuted();
        poeExec2.timestamp     = TIMESTAMP + 2;
        ASCII.putLeft(poeExec2.orderId, "ABC");
        poeExec2.quantity      = 300;
        poeExec2.price         = PRICE;
        poeExec2.liquidityFlag = POE.LIQUIDITY_FLAG_REMOVED_LIQUIDITY;
        poeExec2.matchNumber   = 2;

        order.apply(new Event.OrderExecuted(poeExec1));
        order.apply(new Event.OrderExecuted(poeExec2));

        assertEquals(QUANTITY - 200 - 300, order.getQuantity());
    }

    @Test
    void applyOrderCanceled() {
        POE.OrderCanceled poeCanceled = new POE.OrderCanceled();
        poeCanceled.timestamp        = TIMESTAMP + 1;
        ASCII.putLeft(poeCanceled.orderId, "ABC");
        poeCanceled.canceledQuantity = 400;
        poeCanceled.reason           = POE.ORDER_CANCEL_REASON_REQUEST;

        Event.OrderCanceled event = new Event.OrderCanceled(poeCanceled);
        order.apply(event);

        assertEquals(QUANTITY - 400, order.getQuantity());
    }

    @Test
    void format() {
        Instrument config = mock(Instrument.class);
        when(config.getPriceFormat()).thenReturn("%5.2f");
        when(config.getSizeFormat()).thenReturn("%4.0f");
        when(config.getPriceFactor()).thenReturn(100.0);
        when(config.getSizeFactor()).thenReturn(1.0);

        Instruments instruments = mock(Instruments.class);
        when(instruments.get(INSTRUMENT)).thenReturn(config);

        String result = order.format(instruments);

        assertNotNull(result);
        assertTrue(result.contains("B"));
    }

    @Test
    void getTimestamp() {
        assertEquals(TIMESTAMP, order.getTimestamp());
    }

    @Test
    void getSide() {
        assertEquals(SIDE, order.getSide());
    }

    @Test
    void getInstrument() {
        assertEquals(INSTRUMENT, order.getInstrument());
    }

    @Test
    void getQuantity() {
        assertEquals(QUANTITY, order.getQuantity());
    }
}
