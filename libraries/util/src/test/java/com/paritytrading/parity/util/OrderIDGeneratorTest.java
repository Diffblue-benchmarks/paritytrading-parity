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

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class OrderIDGeneratorTest {

    @Test
    void defaultConstructor() {
        OrderIDGenerator generator = new OrderIDGenerator();

        String id = generator.next();

        assertNotNull(id);
        assertTrue(id.matches("\\d{2}:\\d{2}:\\d{2}-0000001"));
    }

    @Test
    void constructorWithMidnight() {
        OrderIDGenerator generator = new OrderIDGenerator(LocalTime.MIDNIGHT);

        String id = generator.next();

        assertEquals("00:00:00-0000001", id);
    }

    @Test
    void constructorWithMidday() {
        OrderIDGenerator generator = new OrderIDGenerator(LocalTime.NOON);

        String id = generator.next();

        assertEquals("12:00:00-0000001", id);
    }

    @Test
    void constructorWithSpecificTime() {
        OrderIDGenerator generator = new OrderIDGenerator(LocalTime.of(9, 30, 15));

        String id = generator.next();

        assertEquals("09:30:15-0000001", id);
    }

    @Test
    void nextIncrementsCounter() {
        OrderIDGenerator generator = new OrderIDGenerator(LocalTime.MIDNIGHT);

        assertEquals("00:00:00-0000001", generator.next());
        assertEquals("00:00:00-0000002", generator.next());
        assertEquals("00:00:00-0000003", generator.next());
    }
}
