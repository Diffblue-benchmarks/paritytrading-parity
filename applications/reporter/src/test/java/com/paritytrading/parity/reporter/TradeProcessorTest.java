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
package com.paritytrading.parity.reporter;

import com.paritytrading.foundation.ASCII;
import com.paritytrading.parity.net.pmr.PMR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeProcessorTest {

    @Mock
    private TradeListener listener;

    @Captor
    private ArgumentCaptor<Trade> tradeCaptor;

    private TradeProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TradeProcessor(listener);
    }

    @Test
    void testOrderEnteredStoresOrder() {
        PMR.OrderEntered message = new PMR.OrderEntered();
        message.timestamp = 1000000000000L;
        message.username = ASCII.packLong("USER001");
        message.side = PMR.BUY;
        message.instrument = ASCII.packLong("AAPL");
        message.quantity = 100L;
        message.price = 15000L;
        message.orderNumber = 1L;

        processor.orderEntered(message);

        verifyNoInteractions(listener);
    }

    @Test
    void testOrderCanceledReducesQuantity() {
        PMR.OrderEntered entered = new PMR.OrderEntered();
        entered.timestamp = 1000000000000L;
        entered.username = ASCII.packLong("USER001");
        entered.side = PMR.BUY;
        entered.instrument = ASCII.packLong("AAPL");
        entered.quantity = 100L;
        entered.price = 15000L;
        entered.orderNumber = 1L;

        PMR.OrderCanceled canceled = new PMR.OrderCanceled();
        canceled.timestamp = 2000000000000L;
        canceled.orderNumber = 1L;
        canceled.canceledQuantity = 30L;

        processor.orderEntered(entered);
        processor.orderCanceled(canceled);

        verifyNoInteractions(listener);
    }

    @Test
    void testOrderFullyCanceledRemovesOrder() {
        PMR.OrderEntered entered = new PMR.OrderEntered();
        entered.timestamp = 1000000000000L;
        entered.username = ASCII.packLong("USER001");
        entered.side = PMR.BUY;
        entered.instrument = ASCII.packLong("AAPL");
        entered.quantity = 100L;
        entered.price = 15000L;
        entered.orderNumber = 1L;

        PMR.OrderCanceled canceled = new PMR.OrderCanceled();
        canceled.timestamp = 2000000000000L;
        canceled.orderNumber = 1L;
        canceled.canceledQuantity = 100L;

        processor.orderEntered(entered);
        processor.orderCanceled(canceled);

        verifyNoInteractions(listener);
    }

    @Test
    void testTradeWithBuyRestingOrder() {
        PMR.OrderEntered buyOrder = new PMR.OrderEntered();
        buyOrder.timestamp = 1000000000000L;
        buyOrder.username = ASCII.packLong("BUYER");
        buyOrder.side = PMR.BUY;
        buyOrder.instrument = ASCII.packLong("AAPL");
        buyOrder.quantity = 100L;
        buyOrder.price = 15000L;
        buyOrder.orderNumber = 1L;

        PMR.OrderEntered sellOrder = new PMR.OrderEntered();
        sellOrder.timestamp = 2000000000000L;
        sellOrder.username = ASCII.packLong("SELLER");
        sellOrder.side = PMR.SELL;
        sellOrder.instrument = ASCII.packLong("AAPL");
        sellOrder.quantity = 50L;
        sellOrder.price = 15000L;
        sellOrder.orderNumber = 2L;

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 3000000000000L;
        trade.restingOrderNumber = 1L;
        trade.incomingOrderNumber = 2L;
        trade.quantity = 50L;
        trade.matchNumber = 1L;

        processor.orderEntered(buyOrder);
        processor.orderEntered(sellOrder);
        processor.trade(trade);

        verify(listener).trade(tradeCaptor.capture());
        Trade capturedTrade = tradeCaptor.getValue();

        assertNotNull(capturedTrade.timestamp);
        assertEquals(1L, capturedTrade.matchNumber);
        assertEquals("AAPL", capturedTrade.instrument);
        assertEquals(50L, capturedTrade.quantity);
        assertEquals(15000L, capturedTrade.price);
        assertEquals("BUYER", capturedTrade.buyer);
        assertEquals(1L, capturedTrade.buyOrderNumber);
        assertEquals("SELLER", capturedTrade.seller);
        assertEquals(2L, capturedTrade.sellOrderNumber);
    }

    @Test
    void testTradeWithSellRestingOrder() {
        PMR.OrderEntered sellOrder = new PMR.OrderEntered();
        sellOrder.timestamp = 1000000000000L;
        sellOrder.username = ASCII.packLong("SELLER");
        sellOrder.side = PMR.SELL;
        sellOrder.instrument = ASCII.packLong("MSFT");
        sellOrder.quantity = 100L;
        sellOrder.price = 25000L;
        sellOrder.orderNumber = 1L;

        PMR.OrderEntered buyOrder = new PMR.OrderEntered();
        buyOrder.timestamp = 2000000000000L;
        buyOrder.username = ASCII.packLong("BUYER");
        buyOrder.side = PMR.BUY;
        buyOrder.instrument = ASCII.packLong("MSFT");
        buyOrder.quantity = 60L;
        buyOrder.price = 25000L;
        buyOrder.orderNumber = 2L;

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 3000000000000L;
        trade.restingOrderNumber = 1L;
        trade.incomingOrderNumber = 2L;
        trade.quantity = 60L;
        trade.matchNumber = 2L;

        processor.orderEntered(sellOrder);
        processor.orderEntered(buyOrder);
        processor.trade(trade);

        verify(listener).trade(tradeCaptor.capture());
        Trade capturedTrade = tradeCaptor.getValue();

        assertEquals("BUYER", capturedTrade.buyer);
        assertEquals(2L, capturedTrade.buyOrderNumber);
        assertEquals("SELLER", capturedTrade.seller);
        assertEquals(1L, capturedTrade.sellOrderNumber);
        assertEquals(60L, capturedTrade.quantity);
        assertEquals(25000L, capturedTrade.price);
    }

    @Test
    void testTradeRemovesFullyExecutedRestingOrder() {
        PMR.OrderEntered buyOrder = new PMR.OrderEntered();
        buyOrder.timestamp = 1000000000000L;
        buyOrder.username = ASCII.packLong("BUYER");
        buyOrder.side = PMR.BUY;
        buyOrder.instrument = ASCII.packLong("AAPL");
        buyOrder.quantity = 50L;
        buyOrder.price = 15000L;
        buyOrder.orderNumber = 1L;

        PMR.OrderEntered sellOrder = new PMR.OrderEntered();
        sellOrder.timestamp = 2000000000000L;
        sellOrder.username = ASCII.packLong("SELLER");
        sellOrder.side = PMR.SELL;
        sellOrder.instrument = ASCII.packLong("AAPL");
        sellOrder.quantity = 100L;
        sellOrder.price = 15000L;
        sellOrder.orderNumber = 2L;

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 3000000000000L;
        trade.restingOrderNumber = 1L;
        trade.incomingOrderNumber = 2L;
        trade.quantity = 50L;
        trade.matchNumber = 1L;

        processor.orderEntered(buyOrder);
        processor.orderEntered(sellOrder);
        processor.trade(trade);

        verify(listener).trade(any(Trade.class));
    }

    @Test
    void testTradeRemovesFullyExecutedIncomingOrder() {
        PMR.OrderEntered buyOrder = new PMR.OrderEntered();
        buyOrder.timestamp = 1000000000000L;
        buyOrder.username = ASCII.packLong("BUYER");
        buyOrder.side = PMR.BUY;
        buyOrder.instrument = ASCII.packLong("AAPL");
        buyOrder.quantity = 100L;
        buyOrder.price = 15000L;
        buyOrder.orderNumber = 1L;

        PMR.OrderEntered sellOrder = new PMR.OrderEntered();
        sellOrder.timestamp = 2000000000000L;
        sellOrder.username = ASCII.packLong("SELLER");
        sellOrder.side = PMR.SELL;
        sellOrder.instrument = ASCII.packLong("AAPL");
        sellOrder.quantity = 50L;
        sellOrder.price = 15000L;
        sellOrder.orderNumber = 2L;

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 3000000000000L;
        trade.restingOrderNumber = 1L;
        trade.incomingOrderNumber = 2L;
        trade.quantity = 50L;
        trade.matchNumber = 1L;

        processor.orderEntered(buyOrder);
        processor.orderEntered(sellOrder);
        processor.trade(trade);

        verify(listener).trade(any(Trade.class));
    }

    @Test
    void testTradeRemovesBothOrdersWhenBothFullyExecuted() {
        PMR.OrderEntered buyOrder = new PMR.OrderEntered();
        buyOrder.timestamp = 1000000000000L;
        buyOrder.username = ASCII.packLong("BUYER");
        buyOrder.side = PMR.BUY;
        buyOrder.instrument = ASCII.packLong("AAPL");
        buyOrder.quantity = 50L;
        buyOrder.price = 15000L;
        buyOrder.orderNumber = 1L;

        PMR.OrderEntered sellOrder = new PMR.OrderEntered();
        sellOrder.timestamp = 2000000000000L;
        sellOrder.username = ASCII.packLong("SELLER");
        sellOrder.side = PMR.SELL;
        sellOrder.instrument = ASCII.packLong("AAPL");
        sellOrder.quantity = 50L;
        sellOrder.price = 15000L;
        sellOrder.orderNumber = 2L;

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 3000000000000L;
        trade.restingOrderNumber = 1L;
        trade.incomingOrderNumber = 2L;
        trade.quantity = 50L;
        trade.matchNumber = 1L;

        processor.orderEntered(buyOrder);
        processor.orderEntered(sellOrder);
        processor.trade(trade);

        verify(listener).trade(any(Trade.class));
    }

    @Test
    void testMultiplePartialExecutions() {
        PMR.OrderEntered buyOrder = new PMR.OrderEntered();
        buyOrder.timestamp = 1000000000000L;
        buyOrder.username = ASCII.packLong("BUYER");
        buyOrder.side = PMR.BUY;
        buyOrder.instrument = ASCII.packLong("AAPL");
        buyOrder.quantity = 100L;
        buyOrder.price = 15000L;
        buyOrder.orderNumber = 1L;

        PMR.OrderEntered sellOrder1 = new PMR.OrderEntered();
        sellOrder1.timestamp = 2000000000000L;
        sellOrder1.username = ASCII.packLong("SELLER1");
        sellOrder1.side = PMR.SELL;
        sellOrder1.instrument = ASCII.packLong("AAPL");
        sellOrder1.quantity = 30L;
        sellOrder1.price = 15000L;
        sellOrder1.orderNumber = 2L;

        PMR.OrderEntered sellOrder2 = new PMR.OrderEntered();
        sellOrder2.timestamp = 3000000000000L;
        sellOrder2.username = ASCII.packLong("SELLER2");
        sellOrder2.side = PMR.SELL;
        sellOrder2.instrument = ASCII.packLong("AAPL");
        sellOrder2.quantity = 40L;
        sellOrder2.price = 15000L;
        sellOrder2.orderNumber = 3L;

        PMR.Trade trade1 = new PMR.Trade();
        trade1.timestamp = 4000000000000L;
        trade1.restingOrderNumber = 1L;
        trade1.incomingOrderNumber = 2L;
        trade1.quantity = 30L;
        trade1.matchNumber = 1L;

        PMR.Trade trade2 = new PMR.Trade();
        trade2.timestamp = 5000000000000L;
        trade2.restingOrderNumber = 1L;
        trade2.incomingOrderNumber = 3L;
        trade2.quantity = 40L;
        trade2.matchNumber = 2L;

        processor.orderEntered(buyOrder);
        processor.orderEntered(sellOrder1);
        processor.orderEntered(sellOrder2);
        processor.trade(trade1);
        processor.trade(trade2);

        verify(listener, times(2)).trade(any(Trade.class));
    }

    @Test
    void testOrderAddedDoesNothing() {
        PMR.OrderAdded message = new PMR.OrderAdded();
        message.timestamp = 1000000000000L;
        message.orderNumber = 1L;

        processor.orderAdded(message);

        verifyNoInteractions(listener);
    }

    @Test
    void testTradeInstrumentTrimmed() {
        PMR.OrderEntered buyOrder = new PMR.OrderEntered();
        buyOrder.timestamp = 1000000000000L;
        buyOrder.username = ASCII.packLong("BUYER");
        buyOrder.side = PMR.BUY;
        buyOrder.instrument = ASCII.packLong("A");
        buyOrder.quantity = 50L;
        buyOrder.price = 10000L;
        buyOrder.orderNumber = 1L;

        PMR.OrderEntered sellOrder = new PMR.OrderEntered();
        sellOrder.timestamp = 2000000000000L;
        sellOrder.username = ASCII.packLong("SELLER");
        sellOrder.side = PMR.SELL;
        sellOrder.instrument = ASCII.packLong("A");
        sellOrder.quantity = 50L;
        sellOrder.price = 10000L;
        sellOrder.orderNumber = 2L;

        PMR.Trade trade = new PMR.Trade();
        trade.timestamp = 3000000000000L;
        trade.restingOrderNumber = 1L;
        trade.incomingOrderNumber = 2L;
        trade.quantity = 50L;
        trade.matchNumber = 1L;

        processor.orderEntered(buyOrder);
        processor.orderEntered(sellOrder);
        processor.trade(trade);

        verify(listener).trade(tradeCaptor.capture());
        Trade capturedTrade = tradeCaptor.getValue();

        assertEquals("A", capturedTrade.instrument);
        assertEquals("BUYER", capturedTrade.buyer);
        assertEquals("SELLER", capturedTrade.seller);
    }

    @Test
    void testMultipleCancels() {
        PMR.OrderEntered entered = new PMR.OrderEntered();
        entered.timestamp = 1000000000000L;
        entered.username = ASCII.packLong("USER001");
        entered.side = PMR.BUY;
        entered.instrument = ASCII.packLong("AAPL");
        entered.quantity = 100L;
        entered.price = 15000L;
        entered.orderNumber = 1L;

        PMR.OrderCanceled canceled1 = new PMR.OrderCanceled();
        canceled1.timestamp = 2000000000000L;
        canceled1.orderNumber = 1L;
        canceled1.canceledQuantity = 30L;

        PMR.OrderCanceled canceled2 = new PMR.OrderCanceled();
        canceled2.timestamp = 3000000000000L;
        canceled2.orderNumber = 1L;
        canceled2.canceledQuantity = 40L;

        processor.orderEntered(entered);
        processor.orderCanceled(canceled1);
        processor.orderCanceled(canceled2);

        verifyNoInteractions(listener);
    }
}
