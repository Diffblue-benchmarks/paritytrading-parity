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

class StringsClaudeTest {

    @Test
    void repeatWithZeroCountReturnsEmptyString() {
        String result = Strings.repeat('a', 0);

        assertEquals("", result);
    }

    @Test
    void repeatWithOneCountReturnsSingleCharacter() {
        String result = Strings.repeat('x', 1);

        assertEquals("x", result);
    }

    @Test
    void repeatWithMultipleCountReturnsRepeatedCharacter() {
        String result = Strings.repeat('A', 5);

        assertEquals("AAAAA", result);
    }

    @Test
    void repeatWithSpaceCharacter() {
        String result = Strings.repeat(' ', 3);

        assertEquals("   ", result);
    }

    @Test
    void repeatWithSpecialCharacter() {
        String result = Strings.repeat('*', 7);

        assertEquals("*******", result);
    }

    @Test
    void repeatWithDigitCharacter() {
        String result = Strings.repeat('9', 4);

        assertEquals("9999", result);
    }

    @Test
    void repeatWithNewlineCharacter() {
        String result = Strings.repeat('\n', 2);

        assertEquals("\n\n", result);
    }

    @Test
    void repeatWithTabCharacter() {
        String result = Strings.repeat('\t', 3);

        assertEquals("\t\t\t", result);
    }

    @Test
    void repeatWithUnicodeCharacter() {
        String result = Strings.repeat('\u00A9', 2);

        assertEquals("©©", result);
    }

    @Test
    void repeatWithLargeCount() {
        String result = Strings.repeat('-', 100);

        assertEquals(100, result.length());
        assertTrue(result.chars().allMatch(c -> c == '-'));
    }

    @Test
    void repeatReturnsDifferentInstancesEachTime() {
        String result1 = Strings.repeat('a', 3);
        String result2 = Strings.repeat('a', 3);

        assertEquals(result1, result2);
        assertNotSame(result1, result2);
    }

}
