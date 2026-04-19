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
import org.junit.jupiter.api.Test;

class TradeTest {

    @Test
    void timestampFromExecutedEvent() {
        Order order = newOrder(1_000_000_000L, "order1          ", (byte) 'B', 100, 5000);

        Event.OrderExecuted event = newExecutedEvent(2_000_000_000L, "order1          ", 50, 5000);

        Trade trade = new Trade(order, event);

        assertEquals(2_000_000_000L, trade.getTimestamp());
    }

    @Test
    void formatBuyOrder() {
        Order order = newOrder(34_200_000_000_000L, "order1          ", (byte) 'B', 100, 5000);

        Event.OrderExecuted event = newExecutedEvent(34_201_000_000_000L, "order1          ", 50, 5000);

        Trade trade = new Trade(order, event);

        Instruments instruments = mock(Instruments.class);
        Instrument instrument = mock(Instrument.class);
        when(instruments.get(ASCII.packLong("FOO     "))).thenReturn(instrument);
        when(instrument.getPriceFormat()).thenReturn("%10.2f");
        when(instrument.getSizeFormat()).thenReturn("%10.0f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        String result = trade.format(instruments);

        assertNotNull(result);
        assertTrue(result.contains("09:30:01.000"));
        assertTrue(result.contains("order1"));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("FOO"));
    }

    @Test
    void formatSellOrder() {
        Order order = newOrder(34_200_000_000_000L, "order2          ", (byte) 'S', 200, 10000);

        Event.OrderExecuted event = newExecutedEvent(34_202_000_000_000L, "order2          ", 100, 10000);

        Trade trade = new Trade(order, event);

        Instruments instruments = mock(Instruments.class);
        Instrument instrument = mock(Instrument.class);
        when(instruments.get(ASCII.packLong("FOO     "))).thenReturn(instrument);
        when(instrument.getPriceFormat()).thenReturn("%10.2f");
        when(instrument.getSizeFormat()).thenReturn("%10.0f");
        when(instrument.getPriceFactor()).thenReturn(100.0);
        when(instrument.getSizeFactor()).thenReturn(1.0);

        String result = trade.format(instruments);

        assertNotNull(result);
        assertTrue(result.contains("09:30:02.000"));
        assertTrue(result.contains("order2"));
        assertTrue(result.contains("S"));
    }

    private static Order newOrder(long timestamp, String orderId, byte side,
            long quantity, long price) {
        POE.OrderAccepted message = new POE.OrderAccepted();
        message.timestamp   = timestamp;
        message.orderId     = ASCII.put(orderId);
        message.side        = side;
        message.instrument  = ASCII.packLong("FOO     ");
        message.quantity    = quantity;
        message.price       = price;
        message.orderNumber = 1;

        return new Order(new Event.OrderAccepted(message));
    }

    private static Event.OrderExecuted newExecutedEvent(long timestamp, String orderId,
            long quantity, long price) {
        POE.OrderExecuted message = new POE.OrderExecuted();
        message.timestamp     = timestamp;
        message.orderId       = ASCII.put(orderId);
        message.quantity      = quantity;
        message.price         = price;
        message.liquidityFlag = (byte) 'A';
        message.matchNumber   = 1;

        return new Event.OrderExecuted(message);
    }
}
