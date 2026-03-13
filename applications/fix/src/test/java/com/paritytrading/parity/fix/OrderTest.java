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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.paritytrading.philadelphia.fix44.FIX44Enumerations.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(1L, "CLORD-001", "ACCT-001", SideValues.Buy, "AAPL", 100.0);
    }

    @Test
    void testOrderCreatedWithCorrectInitialValues() {
        assertEquals(1L, order.getOrderEntryID());
        assertEquals(0L, order.getOrderID());
        assertEquals("CLORD-001", order.getClOrdID());
        assertNull(order.getOrigClOrdID());
        assertEquals(OrdStatusValues.New, order.getOrdStatus());
        assertNull(order.getAccount());
        assertEquals(SideValues.Buy, order.getSide());
        assertEquals("AAPL", order.getSymbol());
        assertEquals(100.0, order.getOrderQty());
        assertEquals(0.0, order.getCumQty());
        assertEquals(0.0, order.getAvgPx());
        assertEquals(CxlRejResponseToValues.OrderCancelRequest, order.getCxlRejResponseTo());
    }

    @Test
    void testOrderAcceptedSetsOrderId() {
        order.orderAccepted(12345L);
        assertEquals(12345L, order.getOrderID());
    }

    @Test
    void testOrderExecutedPartially() {
        order.orderExecuted(30.0, 150.0);
        assertEquals(30.0, order.getCumQty());
        assertEquals(150.0, order.getAvgPx());
        assertEquals(70.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
    }

    @Test
    void testOrderExecutedFully() {
        order.orderExecuted(100.0, 150.0);
        assertEquals(100.0, order.getCumQty());
        assertEquals(150.0, order.getAvgPx());
        assertEquals(0.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void testOrderExecutedCalculatesWeightedAveragePrice() {
        order.orderExecuted(40.0, 150.0);
        order.orderExecuted(60.0, 160.0);

        assertEquals(100.0, order.getCumQty());
        assertEquals(156.0, order.getAvgPx(), 0.001);
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void testOrderExecutedMultiplePartialFills() {
        order.orderExecuted(20.0, 150.0);
        assertEquals(20.0, order.getCumQty());
        assertEquals(150.0, order.getAvgPx());
        assertEquals(80.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());

        order.orderExecuted(30.0, 155.0);
        assertEquals(50.0, order.getCumQty());
        assertEquals(153.0, order.getAvgPx(), 0.001);
        assertEquals(50.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());

        order.orderExecuted(50.0, 160.0);
        assertEquals(100.0, order.getCumQty());
        assertEquals(156.5, order.getAvgPx(), 0.001);
        assertEquals(0.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void testOrderCanceledReducesOrderQty() {
        order.orderCanceled(40.0);
        assertEquals(60.0, order.getOrderQty());
    }

    @Test
    void testOrderCanceledUpdatesClOrdIds() {
        order.setNextClOrdID("CLORD-002");
        order.orderCanceled(40.0);

        assertEquals("CLORD-001", order.getOrigClOrdID());
        assertEquals("CLORD-002", order.getClOrdID());
        assertNull(order.getNextClOrdID());
    }

    @Test
    void testOrderCanceledFullyCancelsOrder() {
        order.orderCanceled(100.0);
        assertEquals(0.0, order.getOrderQty());
    }

    @Test
    void testGetLeavesQtyAfterExecution() {
        order.orderExecuted(25.0, 150.0);
        assertEquals(75.0, order.getLeavesQty());
    }

    @Test
    void testGetLeavesQtyAfterCancellation() {
        order.orderCanceled(25.0);
        assertEquals(75.0, order.getLeavesQty());
    }

    @Test
    void testGetLeavesQtyWithBothExecutionAndCancellation() {
        order.orderExecuted(30.0, 150.0);
        order.orderCanceled(40.0);
        assertEquals(30.0, order.getLeavesQty());
    }

    @Test
    void testSetAndGetNextClOrdID() {
        assertNull(order.getNextClOrdID());
        order.setNextClOrdID("CLORD-NEW");
        assertEquals("CLORD-NEW", order.getNextClOrdID());
    }

    @Test
    void testSetAndGetCxlRejResponseTo() {
        assertEquals(CxlRejResponseToValues.OrderCancelRequest, order.getCxlRejResponseTo());
        order.setCxlRejResponseTo(CxlRejResponseToValues.OrderCancelReplaceRequest);
        assertEquals(CxlRejResponseToValues.OrderCancelReplaceRequest, order.getCxlRejResponseTo());
    }

    @Test
    void testIsInPendingStatusWhenNextClOrdIdIsNull() {
        assertFalse(order.isInPendingStatus());
    }

    @Test
    void testIsInPendingStatusWhenNextClOrdIdIsSet() {
        order.setNextClOrdID("CLORD-002");
        assertTrue(order.isInPendingStatus());
    }

    @Test
    void testIsNotInPendingStatusAfterCanceled() {
        order.setNextClOrdID("CLORD-002");
        assertTrue(order.isInPendingStatus());
        order.orderCanceled(50.0);
        assertFalse(order.isInPendingStatus());
    }

    @Test
    void testOrderStatusProgressionFromNewToPartiallyFilled() {
        assertEquals(OrdStatusValues.New, order.getOrdStatus());
        order.orderExecuted(50.0, 150.0);
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
    }

    @Test
    void testOrderStatusProgressionFromPartiallyFilledToFilled() {
        order.orderExecuted(50.0, 150.0);
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
        order.orderExecuted(50.0, 155.0);
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void testSellOrderCreation() {
        Order sellOrder = new Order(2L, "CLORD-002", "ACCT-002", SideValues.Sell, "MSFT", 50.0);
        assertEquals(2L, sellOrder.getOrderEntryID());
        assertEquals(SideValues.Sell, sellOrder.getSide());
        assertEquals("MSFT", sellOrder.getSymbol());
        assertEquals(50.0, sellOrder.getOrderQty());
    }

    @Test
    void testAveragePriceWithSingleExecution() {
        order.orderExecuted(100.0, 150.0);
        assertEquals(150.0, order.getAvgPx());
    }

    @Test
    void testAveragePriceWithDifferentPrices() {
        order.orderExecuted(50.0, 100.0);
        order.orderExecuted(50.0, 200.0);
        assertEquals(150.0, order.getAvgPx());
    }
}
