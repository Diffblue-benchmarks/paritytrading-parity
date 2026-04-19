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

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketDataListenerTest {

    private MarketDataListener listener;

    @BeforeEach
    void setUp() {
        listener = new MarketDataListener() {
            @Override
            public void update(OrderBook book, boolean bbo) {
            }

            @Override
            public void trade(OrderBook book, Side side, long price, long size) {
            }
        };
    }

    @Test
    void timestampMillisAfterTimestamp() {
        listener.timestamp(5_000_000_000L);

        assertEquals(5000, listener.timestampMillis());
    }

    @Test
    void timestampMillisDefaultsToZero() {
        assertEquals(0, listener.timestampMillis());
    }

    @Test
    void timestampOverwritesPreviousValue() {
        listener.timestamp(1_000_000_000L);
        listener.timestamp(2_000_000_000L);

        assertEquals(2000, listener.timestampMillis());
    }

    @Test
    void timestampMillisTruncatesSubMillisecond() {
        listener.timestamp(1_500_999L);

        assertEquals(1, listener.timestampMillis());
    }
}
