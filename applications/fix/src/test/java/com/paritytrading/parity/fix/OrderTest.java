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
package com.paritytrading.parity.fix;

import static com.paritytrading.philadelphia.fix44.FIX44Enumerations.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(1001L, "clOrd1", "acct1", 'B', "AAPL", 100.0);
    }

    @Test
    void initialState() {
        assertEquals(1001L, order.getOrderEntryID());
        assertEquals(0L, order.getOrderID());
        assertEquals("clOrd1", order.getClOrdID());
        assertNull(order.getOrigClOrdID());
        assertNull(order.getNextClOrdID());
        assertEquals(OrdStatusValues.New, order.getOrdStatus());
        assertNull(order.getAccount());
        assertEquals('B', order.getSide());
        assertEquals("AAPL", order.getSymbol());
        assertEquals(100.0, order.getOrderQty());
        assertEquals(0.0, order.getCumQty());
        assertEquals(100.0, order.getLeavesQty());
        assertEquals(0.0, order.getAvgPx());
        assertEquals(CxlRejResponseToValues.OrderCancelRequest, order.getCxlRejResponseTo());
        assertFalse(order.isInPendingStatus());
    }

    @Test
    void orderAccepted() {
        order.orderAccepted(5001L);

        assertEquals(5001L, order.getOrderID());
    }

    @Test
    void fullExecution() {
        order.orderAccepted(5001L);
        order.orderExecuted(100.0, 50.0);

        assertEquals(100.0, order.getCumQty());
        assertEquals(0.0, order.getLeavesQty());
        assertEquals(50.0, order.getAvgPx());
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void partialExecution() {
        order.orderAccepted(5001L);
        order.orderExecuted(40.0, 50.0);

        assertEquals(40.0, order.getCumQty());
        assertEquals(60.0, order.getLeavesQty());
        assertEquals(50.0, order.getAvgPx());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
    }

    @Test
    void multipleExecutions() {
        order.orderAccepted(5001L);
        order.orderExecuted(40.0, 50.0);
        order.orderExecuted(60.0, 60.0);

        assertEquals(100.0, order.getCumQty());
        assertEquals(0.0, order.getLeavesQty());
        assertEquals(56.0, order.getAvgPx(), 0.0001);
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void orderCanceled() {
        order.setNextClOrdID("clOrd2");
        order.orderCanceled(30.0);

        assertEquals(70.0, order.getOrderQty());
        assertEquals("clOrd2", order.getClOrdID());
        assertEquals("clOrd1", order.getOrigClOrdID());
        assertNull(order.getNextClOrdID());
    }

    @Test
    void pendingStatus() {
        assertFalse(order.isInPendingStatus());

        order.setNextClOrdID("clOrd2");

        assertTrue(order.isInPendingStatus());
    }

    @Test
    void cxlRejResponseTo() {
        assertEquals(CxlRejResponseToValues.OrderCancelRequest, order.getCxlRejResponseTo());

        order.setCxlRejResponseTo('2');

        assertEquals('2', order.getCxlRejResponseTo());
    }

    @Test
    void leavesQtyAfterPartialExecutionAndCancel() {
        order.orderExecuted(30.0, 45.0);
        order.setNextClOrdID("clOrd2");
        order.orderCanceled(20.0);

        assertEquals(80.0, order.getOrderQty());
        assertEquals(30.0, order.getCumQty());
        assertEquals(50.0, order.getLeavesQty());
    }
}
