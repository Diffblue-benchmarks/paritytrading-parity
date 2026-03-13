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

class StringsTest {

    @Test
    void testRepeatSingleCharacter() {
        String result = Strings.repeat('a', 5);
        assertEquals("aaaaa", result);
    }

    @Test
    void testRepeatZeroTimes() {
        String result = Strings.repeat('x', 0);
        assertEquals("", result);
    }

    @Test
    void testRepeatOnce() {
        String result = Strings.repeat('b', 1);
        assertEquals("b", result);
    }

    @Test
    void testRepeatSpace() {
        String result = Strings.repeat(' ', 3);
        assertEquals("   ", result);
    }

    @Test
    void testRepeatSpecialCharacter() {
        String result = Strings.repeat('-', 10);
        assertEquals("----------", result);
    }

    @Test
    void testRepeatNewline() {
        String result = Strings.repeat('\n', 2);
        assertEquals("\n\n", result);
    }

    @Test
    void testRepeatTab() {
        String result = Strings.repeat('\t', 4);
        assertEquals("\t\t\t\t", result);
    }

    @Test
    void testRepeatLargeCount() {
        String result = Strings.repeat('*', 100);
        assertEquals(100, result.length());
        assertTrue(result.chars().allMatch(c -> c == '*'));
    }

    @Test
    void testRepeatResultLength() {
        String result = Strings.repeat('z', 25);
        assertEquals(25, result.length());
    }

    @Test
    void testRepeatDigit() {
        String result = Strings.repeat('7', 8);
        assertEquals("77777777", result);
    }
}
