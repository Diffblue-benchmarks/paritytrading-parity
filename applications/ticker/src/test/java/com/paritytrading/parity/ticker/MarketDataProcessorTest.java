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
package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.net.pmd.PMD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MarketDataProcessorTest {

    private Market market;
    private MarketDataListener listener;
    private MarketDataProcessor processor;

    @BeforeEach
    public void setUp() {
        market = mock(Market.class);
        listener = mock(MarketDataListener.class);
        processor = new MarketDataProcessor(market, listener);
    }

    @Test
    public void testConstructor() {
        assertNotNull(processor);
    }

    @Test
    public void testVersionWithCorrectVersion() {
        PMD.Version message = new PMD.Version();
        message.version = PMD.VERSION;
        processor.version(message);
    }

    @Test
    public void testOrderAdded() {
        PMD.OrderAdded message = new PMD.OrderAdded();
        message.timestamp = 1000000L;
        message.orderNumber = 123L;
        message.side = PMD.BUY;
        message.instrument = 1L;
        message.quantity = 100L;
        message.price = 1000L;

        processor.orderAdded(message);

        verify(listener).timestamp(1000000L);
        verify(market).add(1L, 123L, Side.BUY, 1000L, 100L);
    }

    @Test
    public void testOrderAddedWithSellSide() {
        PMD.OrderAdded message = new PMD.OrderAdded();
        message.timestamp = 2000000L;
        message.orderNumber = 456L;
        message.side = PMD.SELL;
        message.instrument = 2L;
        message.quantity = 200L;
        message.price = 2000L;

        processor.orderAdded(message);

        verify(listener).timestamp(2000000L);
        verify(market).add(2L, 456L, Side.SELL, 2000L, 200L);
    }

    @Test
    public void testOrderExecuted() {
        PMD.OrderExecuted message = new PMD.OrderExecuted();
        message.timestamp = 3000000L;
        message.orderNumber = 789L;
        message.quantity = 50L;

        processor.orderExecuted(message);

        verify(listener).timestamp(3000000L);
        verify(market).execute(789L, 50L);
    }

    @Test
    public void testOrderCanceled() {
        PMD.OrderCanceled message = new PMD.OrderCanceled();
        message.timestamp = 4000000L;
        message.orderNumber = 999L;
        message.canceledQuantity = 25L;

        processor.orderCanceled(message);

        verify(listener).timestamp(4000000L);
        verify(market).cancel(999L, 25L);
    }
}
