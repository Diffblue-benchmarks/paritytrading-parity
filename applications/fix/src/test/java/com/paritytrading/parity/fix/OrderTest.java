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

import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    public void testConstructor() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertEquals(123L, order.getOrderEntryID());
        assertEquals("CLO123", order.getClOrdID());
        assertEquals(SideValues.Buy, order.getSide());
        assertEquals("AAPL", order.getSymbol());
        assertEquals(100.0, order.getOrderQty());
        assertEquals(0L, order.getOrderID());
        assertNull(order.getNextClOrdID());
        assertNull(order.getOrigClOrdID());
        assertEquals(OrdStatusValues.New, order.getOrdStatus());
        assertNull(order.getAccount());
        assertEquals(0.0, order.getCumQty());
        assertEquals(0.0, order.getAvgPx());
        assertEquals(CxlRejResponseToValues.OrderCancelRequest, order.getCxlRejResponseTo());
    }

    @Test
    public void testOrderAccepted() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        order.orderAccepted(999L);

        assertEquals(999L, order.getOrderID());
    }

    @Test
    public void testOrderExecutedPartial() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        order.orderExecuted(30.0, 50.0);

        assertEquals(30.0, order.getCumQty());
        assertEquals(50.0, order.getAvgPx());
        assertEquals(70.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
    }

    @Test
    public void testOrderExecutedFull() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        order.orderExecuted(100.0, 50.0);

        assertEquals(100.0, order.getCumQty());
        assertEquals(50.0, order.getAvgPx());
        assertEquals(0.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    public void testOrderExecutedMultiple() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        order.orderExecuted(30.0, 50.0);
        order.orderExecuted(20.0, 60.0);

        assertEquals(50.0, order.getCumQty());
        assertEquals(54.0, order.getAvgPx());
        assertEquals(50.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
    }

    @Test
    public void testOrderCanceled() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("CLO124");

        order.orderCanceled(50.0);

        assertEquals(50.0, order.getOrderQty());
        assertEquals("CLO123", order.getOrigClOrdID());
        assertEquals("CLO124", order.getClOrdID());
        assertNull(order.getNextClOrdID());
    }

    @Test
    public void testGetOrderEntryID() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertEquals(123L, order.getOrderEntryID());
    }

    @Test
    public void testGetOrderID() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);
        order.orderAccepted(456L);

        assertEquals(456L, order.getOrderID());
    }

    @Test
    public void testGetClOrdID() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertEquals("CLO123", order.getClOrdID());
    }

    @Test
    public void testGetOrigClOrdID() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("CLO124");
        order.orderCanceled(50.0);

        assertEquals("CLO123", order.getOrigClOrdID());
    }

    @Test
    public void testSetAndGetNextClOrdID() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        order.setNextClOrdID("CLO124");

        assertEquals("CLO124", order.getNextClOrdID());
    }

    @Test
    public void testGetOrdStatus() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertEquals(OrdStatusValues.New, order.getOrdStatus());
    }

    @Test
    public void testGetAccount() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertNull(order.getAccount());
    }

    @Test
    public void testGetSide() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertEquals(SideValues.Buy, order.getSide());
    }

    @Test
    public void testGetSymbol() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertEquals("AAPL", order.getSymbol());
    }

    @Test
    public void testGetOrderQty() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertEquals(100.0, order.getOrderQty());
    }

    @Test
    public void testGetCumQty() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(30.0, 50.0);

        assertEquals(30.0, order.getCumQty());
    }

    @Test
    public void testGetLeavesQty() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(30.0, 50.0);

        assertEquals(70.0, order.getLeavesQty());
    }

    @Test
    public void testGetAvgPx() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(30.0, 50.0);

        assertEquals(50.0, order.getAvgPx());
    }

    @Test
    public void testSetAndGetCxlRejResponseTo() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        order.setCxlRejResponseTo(CxlRejResponseToValues.OrderCancelReplaceRequest);

        assertEquals(CxlRejResponseToValues.OrderCancelReplaceRequest, order.getCxlRejResponseTo());
    }

    @Test
    public void testIsInPendingStatusWhenNextClOrdIDIsNull() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);

        assertFalse(order.isInPendingStatus());
    }

    @Test
    public void testIsInPendingStatusWhenNextClOrdIDIsNotNull() {
        Order order = new Order(123L, "CLO123", "ACC001", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("CLO124");

        assertTrue(order.isInPendingStatus());
    }
}
