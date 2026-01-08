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

class MarketDataListenerClaudeTest {

    private TestMarketDataListener listener;

    @BeforeEach
    void setUp() {
        listener = new TestMarketDataListener();
    }

    @Test
    void testTimestampAndTimestampMillisWithZero() {
        // Set timestamp to 0 nanoseconds
        listener.timestamp(0L);

        // Verify timestampMillis returns 0 milliseconds
        assertEquals(0L, listener.timestampMillis(),
                "Timestamp of 0 nanoseconds should convert to 0 milliseconds");
    }

    @Test
    void testTimestampAndTimestampMillisWithOneMillisecond() {
        // Set timestamp to 1,000,000 nanoseconds (1 millisecond)
        listener.timestamp(1_000_000L);

        // Verify timestampMillis returns 1 millisecond
        assertEquals(1L, listener.timestampMillis(),
                "Timestamp of 1,000,000 nanoseconds should convert to 1 millisecond");
    }

    @Test
    void testTimestampAndTimestampMillisWithOneSecond() {
        // Set timestamp to 1,000,000,000 nanoseconds (1 second = 1000 milliseconds)
        listener.timestamp(1_000_000_000L);

        // Verify timestampMillis returns 1000 milliseconds
        assertEquals(1000L, listener.timestampMillis(),
                "Timestamp of 1,000,000,000 nanoseconds should convert to 1000 milliseconds");
    }

    @Test
    void testTimestampAndTimestampMillisWithLargeValue() {
        // Set timestamp to a large value (1 hour in nanoseconds)
        long oneHourInNanos = 3600L * 1_000_000_000L; // 3600 seconds * 1 billion nanos
        listener.timestamp(oneHourInNanos);

        // Verify timestampMillis returns correct milliseconds
        long expectedMillis = 3600L * 1000L; // 3600 seconds * 1000 millis
        assertEquals(expectedMillis, listener.timestampMillis(),
                "Timestamp of one hour should convert correctly to milliseconds");
    }

    @Test
    void testTimestampAndTimestampMillisWithFractionalMilliseconds() {
        // Set timestamp to 1,500,000 nanoseconds (1.5 milliseconds)
        listener.timestamp(1_500_000L);

        // Verify timestampMillis returns 1 millisecond (integer division truncates)
        assertEquals(1L, listener.timestampMillis(),
                "Timestamp of 1,500,000 nanoseconds should truncate to 1 millisecond");
    }

    @Test
    void testTimestampAndTimestampMillisWithAlmostOneMillisecond() {
        // Set timestamp to 999,999 nanoseconds (just under 1 millisecond)
        listener.timestamp(999_999L);

        // Verify timestampMillis returns 0 milliseconds (truncation)
        assertEquals(0L, listener.timestampMillis(),
                "Timestamp of 999,999 nanoseconds should truncate to 0 milliseconds");
    }

    @Test
    void testTimestampCanBeUpdatedMultipleTimes() {
        // Set initial timestamp
        listener.timestamp(1_000_000L);
        assertEquals(1L, listener.timestampMillis(),
                "First timestamp should be 1 millisecond");

        // Update timestamp
        listener.timestamp(5_000_000L);
        assertEquals(5L, listener.timestampMillis(),
                "Second timestamp should be 5 milliseconds");

        // Update again
        listener.timestamp(10_000_000L);
        assertEquals(10L, listener.timestampMillis(),
                "Third timestamp should be 10 milliseconds");
    }

    @Test
    void testTimestampCanBeSetToSmallerValue() {
        // Set initial large timestamp
        listener.timestamp(10_000_000L);
        assertEquals(10L, listener.timestampMillis(),
                "Initial timestamp should be 10 milliseconds");

        // Set to smaller value
        listener.timestamp(2_000_000L);
        assertEquals(2L, listener.timestampMillis(),
                "Timestamp should be updated to 2 milliseconds");
    }

    @Test
    void testTimestampWithMaxLongValue() {
        // Set timestamp to maximum long value
        listener.timestamp(Long.MAX_VALUE);

        // Verify timestampMillis returns max value divided by 1,000,000
        long expectedMillis = Long.MAX_VALUE / 1_000_000;
        assertEquals(expectedMillis, listener.timestampMillis(),
                "Maximum timestamp should convert correctly");
    }

    @Test
    void testTimestampWithNegativeValue() {
        // Set timestamp to negative value (edge case, though unusual)
        listener.timestamp(-1_000_000L);

        // Verify timestampMillis returns -1 millisecond
        assertEquals(-1L, listener.timestampMillis(),
                "Negative timestamp should convert correctly");
    }

    @Test
    void testTimestampPersistsAcrossMultipleCalls() {
        // Set timestamp
        listener.timestamp(3_000_000L);

        // Call timestampMillis multiple times
        assertEquals(3L, listener.timestampMillis(), "First call should return 3");
        assertEquals(3L, listener.timestampMillis(), "Second call should return 3");
        assertEquals(3L, listener.timestampMillis(), "Third call should return 3");
    }

    /**
     * Concrete implementation of MarketDataListener for testing.
     * This is necessary because MarketDataListener is abstract.
     */
    private static class TestMarketDataListener extends MarketDataListener {
        @Override
        public void update(OrderBook book, boolean bbo) {
            // No-op implementation for testing
        }

        @Override
        public void trade(OrderBook book, Side side, long price, long size) {
            // No-op implementation for testing
        }
    }
}
