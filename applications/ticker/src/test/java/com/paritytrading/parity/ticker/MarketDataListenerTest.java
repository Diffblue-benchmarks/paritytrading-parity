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

import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarketDataListenerTest {

    @Test
    public void testTimestamp() {
        TestMarketDataListener listener = new TestMarketDataListener();
        long expectedTimestamp = 1234567890000000L;

        listener.timestamp(expectedTimestamp);

        assertEquals(1234567890L, listener.timestampMillis());
    }

    @Test
    public void testTimestampMillis() {
        TestMarketDataListener listener = new TestMarketDataListener();

        listener.timestamp(5000000L);

        assertEquals(5L, listener.timestampMillis());
    }

    @Test
    public void testTimestampMillisZero() {
        TestMarketDataListener listener = new TestMarketDataListener();

        listener.timestamp(0L);

        assertEquals(0L, listener.timestampMillis());
    }

    private static class TestMarketDataListener extends MarketDataListener {
        @Override
        public void update(OrderBook book, boolean bbo) {
        }

        @Override
        public void trade(OrderBook book, Side side, long price, long size) {
        }
    }
}
