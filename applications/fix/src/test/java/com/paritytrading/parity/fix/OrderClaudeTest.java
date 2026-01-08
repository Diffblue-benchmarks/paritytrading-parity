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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.paritytrading.philadelphia.fix44.FIX44Enumerations.*;

/**
 * Test class for Order.
 *
 * <p>This test class covers all methods of the Order class including:
 * - Constructor
 * - orderAccepted(long)
 * - orderExecuted(double, double)
 * - orderCanceled(double)
 * - All getter methods
 * - setNextClOrdID(String)
 * - setCxlRejResponseTo(char)
 * - isInPendingStatus()
 *
 * <p>Testing approach: Tests use the public/package-private API without mocking,
 * verifying actual behavior of the Order class. The Order class is package-private,
 * so tests are in the same package.
 */
class OrderClaudeTest {

    @Test
    void testConstructorInitializesFieldsCorrectly() {
        long orderEntryId = 12345L;
        String clOrdId = "ORDER123";
        String account = "ACCT001";
        char side = SideValues.Buy;
        String symbol = "AAPL";
        double orderQty = 100.0;

        Order order = new Order(orderEntryId, clOrdId, account, side, symbol, orderQty);

        assertEquals(orderEntryId, order.getOrderEntryID());
        assertEquals(0L, order.getOrderID());
        assertEquals(clOrdId, order.getClOrdID());
        assertNull(order.getOrigClOrdID());
        assertNull(order.getNextClOrdID());
        assertEquals(OrdStatusValues.New, order.getOrdStatus());
        assertNull(order.getAccount());
        assertEquals(side, order.getSide());
        assertEquals(symbol, order.getSymbol());
        assertEquals(orderQty, order.getOrderQty());
        assertEquals(0.0, order.getCumQty());
        assertEquals(orderQty, order.getLeavesQty());
        assertEquals(0.0, order.getAvgPx());
        assertEquals(CxlRejResponseToValues.OrderCancelRequest, order.getCxlRejResponseTo());
        assertFalse(order.isInPendingStatus());
    }

    @Test
    void testConstructorWithSellSide() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Sell, "MSFT", 50.0);
        assertEquals(SideValues.Sell, order.getSide());
    }

    @Test
    void testOrderAcceptedSetsOrderID() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(0L, order.getOrderID());

        long orderNumber = 999L;
        order.orderAccepted(orderNumber);

        assertEquals(orderNumber, order.getOrderID());
    }

    @Test
    void testOrderExecutedPartialFill() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderAccepted(123L);

        double quantity = 30.0;
        double price = 150.0;
        order.orderExecuted(quantity, price);

        assertEquals(30.0, order.getCumQty());
        assertEquals(70.0, order.getLeavesQty());
        assertEquals(150.0, order.getAvgPx());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
    }

    @Test
    void testOrderExecutedFullFill() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);

        order.orderExecuted(100.0, 150.0);

        assertEquals(100.0, order.getCumQty());
        assertEquals(0.0, order.getLeavesQty());
        assertEquals(150.0, order.getAvgPx());
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void testOrderExecutedMultiplePartialFills() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);

        order.orderExecuted(30.0, 150.0);
        assertEquals(30.0, order.getCumQty());
        assertEquals(70.0, order.getLeavesQty());
        assertEquals(150.0, order.getAvgPx());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());

        order.orderExecuted(20.0, 155.0);
        assertEquals(50.0, order.getCumQty());
        assertEquals(50.0, order.getLeavesQty());
        double expectedAvgPx = (30.0 * 150.0 + 20.0 * 155.0) / 50.0;
        assertEquals(expectedAvgPx, order.getAvgPx(), 0.0001);
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
    }

    @Test
    void testOrderExecutedCalculatesAveragePriceCorrectly() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);

        order.orderExecuted(40.0, 100.0);
        order.orderExecuted(30.0, 110.0);
        order.orderExecuted(30.0, 120.0);

        double expectedAvgPx = (40.0 * 100.0 + 30.0 * 110.0 + 30.0 * 120.0) / 100.0;
        assertEquals(expectedAvgPx, order.getAvgPx(), 0.0001);
        assertEquals(100.0, order.getCumQty());
        assertEquals(0.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void testOrderCanceledReducesOrderQty() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("ORDER2");

        order.orderCanceled(50.0);

        assertEquals(50.0, order.getOrderQty());
        assertEquals(50.0, order.getLeavesQty());
        assertEquals("ORDER1", order.getOrigClOrdID());
        assertEquals("ORDER2", order.getClOrdID());
        assertNull(order.getNextClOrdID());
    }

    @Test
    void testOrderCanceledUpdatesClOrdIDs() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("ORDER2");

        order.orderCanceled(30.0);

        assertEquals("ORDER1", order.getOrigClOrdID());
        assertEquals("ORDER2", order.getClOrdID());
        assertNull(order.getNextClOrdID());
    }

    @Test
    void testOrderCanceledFullCancel() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("ORDER2");

        order.orderCanceled(100.0);

        assertEquals(0.0, order.getOrderQty());
        assertEquals(0.0, order.getLeavesQty());
    }

    @Test
    void testOrderCanceledAfterPartialFill() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(40.0, 150.0);
        order.setNextClOrdID("ORDER2");

        order.orderCanceled(60.0);

        assertEquals(40.0, order.getOrderQty());
        assertEquals(40.0, order.getCumQty());
        assertEquals(0.0, order.getLeavesQty());
    }

    @Test
    void testGetOrderEntryID() {
        long orderEntryId = 54321L;
        Order order = new Order(orderEntryId, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(orderEntryId, order.getOrderEntryID());
    }

    @Test
    void testGetOrderIDBeforeAcceptance() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(0L, order.getOrderID());
    }

    @Test
    void testGetOrderIDAfterAcceptance() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderAccepted(789L);
        assertEquals(789L, order.getOrderID());
    }

    @Test
    void testGetClOrdID() {
        String clOrdId = "TESTORDER123";
        Order order = new Order(1L, clOrdId, "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(clOrdId, order.getClOrdID());
    }

    @Test
    void testGetOrigClOrdIDBeforeCancel() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertNull(order.getOrigClOrdID());
    }

    @Test
    void testGetOrigClOrdIDAfterCancel() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("ORDER2");
        order.orderCanceled(50.0);
        assertEquals("ORDER1", order.getOrigClOrdID());
    }

    @Test
    void testSetAndGetNextClOrdID() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertNull(order.getNextClOrdID());

        String nextClOrdId = "NEXTORDER";
        order.setNextClOrdID(nextClOrdId);
        assertEquals(nextClOrdId, order.getNextClOrdID());
    }

    @Test
    void testGetOrdStatusNew() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(OrdStatusValues.New, order.getOrdStatus());
    }

    @Test
    void testGetOrdStatusPartiallyFilled() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(50.0, 150.0);
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());
    }

    @Test
    void testGetOrdStatusFilled() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(100.0, 150.0);
        assertEquals(OrdStatusValues.Filled, order.getOrdStatus());
    }

    @Test
    void testGetAccount() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertNull(order.getAccount());
    }

    @Test
    void testGetSideBuy() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(SideValues.Buy, order.getSide());
    }

    @Test
    void testGetSideSell() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Sell, "AAPL", 100.0);
        assertEquals(SideValues.Sell, order.getSide());
    }

    @Test
    void testGetSymbol() {
        String symbol = "GOOGL";
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, symbol, 100.0);
        assertEquals(symbol, order.getSymbol());
    }

    @Test
    void testGetOrderQty() {
        double orderQty = 250.5;
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", orderQty);
        assertEquals(orderQty, order.getOrderQty());
    }

    @Test
    void testGetCumQtyInitiallyZero() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(0.0, order.getCumQty());
    }

    @Test
    void testGetCumQtyAfterExecution() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(30.0, 150.0);
        order.orderExecuted(20.0, 155.0);
        assertEquals(50.0, order.getCumQty());
    }

    @Test
    void testGetLeavesQtyInitial() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(100.0, order.getLeavesQty());
    }

    @Test
    void testGetLeavesQtyAfterPartialFill() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(40.0, 150.0);
        assertEquals(60.0, order.getLeavesQty());
    }

    @Test
    void testGetLeavesQtyAfterFullFill() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(100.0, 150.0);
        assertEquals(0.0, order.getLeavesQty());
    }

    @Test
    void testGetLeavesQtyAfterCancel() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("ORDER2");
        order.orderCanceled(30.0);
        assertEquals(70.0, order.getLeavesQty());
    }

    @Test
    void testGetAvgPxInitiallyZero() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(0.0, order.getAvgPx());
    }

    @Test
    void testGetAvgPxAfterSingleExecution() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(50.0, 120.0);
        assertEquals(120.0, order.getAvgPx());
    }

    @Test
    void testSetAndGetCxlRejResponseTo() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertEquals(CxlRejResponseToValues.OrderCancelRequest, order.getCxlRejResponseTo());

        char newValue = CxlRejResponseToValues.OrderCancelReplaceRequest;
        order.setCxlRejResponseTo(newValue);
        assertEquals(newValue, order.getCxlRejResponseTo());
    }

    @Test
    void testIsInPendingStatusInitiallyFalse() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        assertFalse(order.isInPendingStatus());
    }

    @Test
    void testIsInPendingStatusTrueWhenNextClOrdIDSet() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("NEXTORDER");
        assertTrue(order.isInPendingStatus());
    }

    @Test
    void testIsInPendingStatusFalseAfterCancel() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("ORDER2");
        assertTrue(order.isInPendingStatus());

        order.orderCanceled(50.0);
        assertFalse(order.isInPendingStatus());
    }

    @Test
    void testComplexScenarioPartialFillThenCancel() {
        Order order = new Order(123L, "ORDER1", "ACCT1", SideValues.Buy, "TSLA", 200.0);
        order.orderAccepted(456L);

        order.orderExecuted(50.0, 100.0);
        assertEquals(50.0, order.getCumQty());
        assertEquals(150.0, order.getLeavesQty());
        assertEquals(OrdStatusValues.PartiallyFilled, order.getOrdStatus());

        order.orderExecuted(30.0, 105.0);
        assertEquals(80.0, order.getCumQty());
        assertEquals(120.0, order.getLeavesQty());

        order.setNextClOrdID("ORDER2");
        order.orderCanceled(50.0);

        assertEquals(150.0, order.getOrderQty());
        assertEquals(80.0, order.getCumQty());
        assertEquals(70.0, order.getLeavesQty());
        assertEquals("ORDER2", order.getClOrdID());
        assertEquals("ORDER1", order.getOrigClOrdID());
    }

    @Test
    void testMultipleCancelsScenario() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Sell, "NFLX", 300.0);

        order.setNextClOrdID("ORDER2");
        order.orderCanceled(100.0);
        assertEquals(200.0, order.getOrderQty());
        assertEquals("ORDER2", order.getClOrdID());

        order.setNextClOrdID("ORDER3");
        order.orderCanceled(50.0);
        assertEquals(150.0, order.getOrderQty());
        assertEquals("ORDER3", order.getClOrdID());
        assertEquals("ORDER2", order.getOrigClOrdID());
    }

    @Test
    void testZeroQuantityOrder() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 0.0);
        assertEquals(0.0, order.getOrderQty());
        assertEquals(0.0, order.getLeavesQty());
    }

    @Test
    void testZeroQuantityExecution() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.orderExecuted(0.0, 150.0);
        assertEquals(0.0, order.getCumQty());
        assertEquals(100.0, order.getLeavesQty());
    }

    @Test
    void testZeroQuantityCancel() {
        Order order = new Order(1L, "ORDER1", "ACCT1", SideValues.Buy, "AAPL", 100.0);
        order.setNextClOrdID("ORDER2");
        order.orderCanceled(0.0);
        assertEquals(100.0, order.getOrderQty());
    }
}
