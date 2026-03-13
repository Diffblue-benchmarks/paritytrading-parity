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

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderIDGeneratorTest {

    @Test
    void testNextGeneratesValidId() {
        OrderIDGenerator generator = new OrderIDGenerator();
        String id = generator.next();
        assertNotNull(id);
        assertFalse(id.isEmpty());
    }

    @Test
    void testNextGeneratesSequentialIds() {
        OrderIDGenerator generator = new OrderIDGenerator();
        String id1 = generator.next();
        String id2 = generator.next();
        assertNotEquals(id1, id2);
    }

    @Test
    void testIdFormatWithFixedTime() {
        LocalTime time = LocalTime.of(14, 30, 45);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        String id = generator.next();
        assertTrue(id.startsWith("14:30:45-"));
    }

    @Test
    void testIdFormatWithPaddedNumbers() {
        LocalTime time = LocalTime.of(9, 5, 3);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        String id = generator.next();
        assertTrue(id.startsWith("09:05:03-"));
    }

    @Test
    void testIdSequenceStartsAtOne() {
        LocalTime time = LocalTime.of(12, 0, 0);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        String id = generator.next();
        assertEquals("12:00:00-0000001", id);
    }

    @Test
    void testIdSequenceIncrementsCorrectly() {
        LocalTime time = LocalTime.of(12, 0, 0);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        assertEquals("12:00:00-0000001", generator.next());
        assertEquals("12:00:00-0000002", generator.next());
        assertEquals("12:00:00-0000003", generator.next());
    }

    @Test
    void testIdSequenceHandlesLargeNumbers() {
        LocalTime time = LocalTime.of(12, 0, 0);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        for (int i = 1; i < 1000; i++) {
            generator.next();
        }
        String id = generator.next();
        assertEquals("12:00:00-0001000", id);
    }

    @Test
    void testIdSequenceHandlesVeryLargeNumbers() {
        LocalTime time = LocalTime.of(12, 0, 0);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        for (int i = 1; i < 9999999; i++) {
            generator.next();
        }
        String id = generator.next();
        assertEquals("12:00:00-9999999", id);
    }

    @Test
    void testGenerateThousandUniqueIds() {
        OrderIDGenerator generator = new OrderIDGenerator();
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            ids.add(generator.next());
        }
        assertEquals(1000, ids.size());
    }

    @Test
    void testDefaultConstructorGeneratesValidIds() {
        OrderIDGenerator generator = new OrderIDGenerator();
        String id1 = generator.next();
        String id2 = generator.next();

        assertTrue(id1.matches("\\d{2}:\\d{2}:\\d{2}-\\d{7}"));
        assertTrue(id2.matches("\\d{2}:\\d{2}:\\d{2}-\\d{7}"));
    }

    @Test
    void testIdFormatAtMidnight() {
        LocalTime time = LocalTime.of(0, 0, 0);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        String id = generator.next();
        assertEquals("00:00:00-0000001", id);
    }

    @Test
    void testIdFormatAtEndOfDay() {
        LocalTime time = LocalTime.of(23, 59, 59);
        OrderIDGenerator generator = new OrderIDGenerator(time);
        String id = generator.next();
        assertEquals("23:59:59-0000001", id);
    }

    @Test
    void testMultipleGeneratorsAreIndependent() {
        LocalTime time = LocalTime.of(12, 0, 0);
        OrderIDGenerator generator1 = new OrderIDGenerator(time);
        OrderIDGenerator generator2 = new OrderIDGenerator(time);

        String id1 = generator1.next();
        String id2 = generator2.next();

        assertEquals(id1, id2);

        String id3 = generator1.next();
        String id4 = generator2.next();

        assertEquals(id3, id4);
    }
}
