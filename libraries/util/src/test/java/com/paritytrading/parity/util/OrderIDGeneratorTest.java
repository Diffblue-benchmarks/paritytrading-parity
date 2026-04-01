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

    private static final LocalTime FIXED_TIME = LocalTime.of(10, 30, 0);

    @Test
    void defaultConstructorCreatesInstance() {
        OrderIDGenerator generator = new OrderIDGenerator();

        assertNotNull(generator.next());
    }

    @Test
    void nextReturnsFormattedOrderId() {
        OrderIDGenerator generator = new OrderIDGenerator(FIXED_TIME);

        assertEquals("10:30:00-0000001", generator.next());
    }

    @Test
    void nextIncrementsCount() {
        OrderIDGenerator generator = new OrderIDGenerator(FIXED_TIME);

        generator.next();
        assertEquals("10:30:00-0000002", generator.next());
    }

}
