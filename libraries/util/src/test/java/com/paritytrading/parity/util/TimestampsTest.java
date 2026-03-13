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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimestampsTest {

    @Test
    void testFormatZeroTimestamp() {
        String result = Timestamps.format(0);
        assertEquals("00:00:00.000", result);
    }

    @Test
    void testFormatOneSecond() {
        String result = Timestamps.format(1000);
        assertEquals("00:00:01.000", result);
    }

    @Test
    void testFormatOneMinute() {
        String result = Timestamps.format(60 * 1000);
        assertEquals("00:01:00.000", result);
    }

    @Test
    void testFormatOneHour() {
        String result = Timestamps.format(60 * 60 * 1000);
        assertEquals("01:00:00.000", result);
    }

    @Test
    void testFormatWithMilliseconds() {
        String result = Timestamps.format(1234);
        assertEquals("00:00:01.234", result);
    }

    @Test
    void testFormatMidDay() {
        long timestamp = 12 * 60 * 60 * 1000 + 30 * 60 * 1000 + 45 * 1000 + 678;
        String result = Timestamps.format(timestamp);
        assertEquals("12:30:45.678", result);
    }

    @Test
    void testFormatEndOfDay() {
        long timestamp = 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000 + 999;
        String result = Timestamps.format(timestamp);
        assertEquals("23:59:59.999", result);
    }

    @Test
    void testFormatEarlyMorning() {
        long timestamp = 8 * 60 * 60 * 1000 + 5 * 60 * 1000 + 3 * 1000 + 100;
        String result = Timestamps.format(timestamp);
        assertEquals("08:05:03.100", result);
    }

    @Test
    void testFormatWrapsAfter24Hours() {
        long timestamp = 25 * 60 * 60 * 1000;
        String result = Timestamps.format(timestamp);
        assertEquals("01:00:00.000", result);
    }

    @Test
    void testFormatWithSmallMilliseconds() {
        String result = Timestamps.format(42);
        assertEquals("00:00:00.042", result);
    }

    @Test
    void testFormatWithVariousMilliseconds() {
        assertEquals("00:00:00.001", Timestamps.format(1));
        assertEquals("00:00:00.010", Timestamps.format(10));
        assertEquals("00:00:00.100", Timestamps.format(100));
    }

    @Test
    void testFormatComplexTimestamp() {
        long timestamp = 14 * 60 * 60 * 1000 + 23 * 60 * 1000 + 56 * 1000 + 789;
        String result = Timestamps.format(timestamp);
        assertEquals("14:23:56.789", result);
    }

    @Test
    void testFormatAlwaysProducesCorrectLength() {
        String result = Timestamps.format(12345678);
        assertEquals(12, result.length());
        assertTrue(result.matches("\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
    }
}
