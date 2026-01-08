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
package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TimestampsClaudeTest {

    @Test
    void testFormatAtMidnight() {
        long timestampMillis = 0L;
        assertEquals("00:00:00.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithMilliseconds() {
        long timestampMillis = 123L;
        assertEquals("00:00:00.123", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithSeconds() {
        long timestampMillis = 5 * 1000L;
        assertEquals("00:00:05.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithSecondsAndMilliseconds() {
        long timestampMillis = 5 * 1000L + 456L;
        assertEquals("00:00:05.456", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithMinutes() {
        long timestampMillis = 10 * 60 * 1000L;
        assertEquals("00:10:00.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithMinutesAndSeconds() {
        long timestampMillis = 10 * 60 * 1000L + 30 * 1000L;
        assertEquals("00:10:30.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithMinutesSecondsAndMilliseconds() {
        long timestampMillis = 10 * 60 * 1000L + 30 * 1000L + 789L;
        assertEquals("00:10:30.789", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithHours() {
        long timestampMillis = 5 * 60 * 60 * 1000L;
        assertEquals("05:00:00.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithFullTime() {
        long timestampMillis = 13 * 60 * 60 * 1000L + 45 * 60 * 1000L + 30 * 1000L + 999L;
        assertEquals("13:45:30.999", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatAtMidday() {
        long timestampMillis = 12 * 60 * 60 * 1000L;
        assertEquals("12:00:00.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatBeforeMidnight() {
        long timestampMillis = 23 * 60 * 60 * 1000L + 59 * 60 * 1000L + 59 * 1000L + 999L;
        assertEquals("23:59:59.999", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWrapsAfter24Hours() {
        long timestampMillis = 24 * 60 * 60 * 1000L;
        assertEquals("00:00:00.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWrapsAfter24HoursWithOffset() {
        long timestampMillis = 24 * 60 * 60 * 1000L + 1 * 60 * 60 * 1000L + 30 * 60 * 1000L;
        assertEquals("01:30:00.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWrapsAfter48Hours() {
        long timestampMillis = 48 * 60 * 60 * 1000L;
        assertEquals("00:00:00.000", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatWithLargeTimestamp() {
        long timestampMillis = 100 * 24 * 60 * 60 * 1000L + 15 * 60 * 60 * 1000L + 20 * 60 * 1000L + 30 * 1000L + 500L;
        assertEquals("15:20:30.500", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatEarlyMorning() {
        long timestampMillis = 1 * 60 * 60 * 1000L + 15 * 60 * 1000L + 45 * 1000L + 100L;
        assertEquals("01:15:45.100", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatLateEvening() {
        long timestampMillis = 21 * 60 * 60 * 1000L + 30 * 60 * 1000L + 15 * 1000L + 250L;
        assertEquals("21:30:15.250", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatSingleMillisecond() {
        long timestampMillis = 1L;
        assertEquals("00:00:00.001", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatTenMilliseconds() {
        long timestampMillis = 10L;
        assertEquals("00:00:00.010", Timestamps.format(timestampMillis));
    }

    @Test
    void testFormatHundredMilliseconds() {
        long timestampMillis = 100L;
        assertEquals("00:00:00.100", Timestamps.format(timestampMillis));
    }
}
