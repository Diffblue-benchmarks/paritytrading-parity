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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableHeaderTest {

    private TableHeader header;

    @BeforeEach
    void setUp() {
        header = new TableHeader();
    }

    @Test
    void testAddSingleColumn() {
        header.add("Name", 10);
        String result = header.format();
        assertNotNull(result);
        assertTrue(result.contains("Name"));
        assertTrue(result.contains("----------"));
    }

    @Test
    void testAddMultipleColumns() {
        header.add("Name", 10);
        header.add("Value", 8);
        header.add("Status", 6);

        String result = header.format();
        assertTrue(result.contains("Name"));
        assertTrue(result.contains("Value"));
        assertTrue(result.contains("Status"));
        assertTrue(result.contains("----------"));
        assertTrue(result.contains("--------"));
        assertTrue(result.contains("------"));
    }

    @Test
    void testFormatWithEmptyHeader() {
        String result = header.format();
        assertEquals("", result);
    }

    @Test
    void testFormatContainsNewlines() {
        header.add("Column", 10);
        String result = header.format();
        assertTrue(result.endsWith("\n"));
        assertTrue(result.contains("\n"));
    }

    @Test
    void testFormatWithSpacesBetweenColumns() {
        header.add("First", 5);
        header.add("Second", 6);

        String result = header.format();
        String[] lines = result.split("\n");
        assertTrue(lines[0].contains(" "));
        assertTrue(lines[1].contains(" "));
    }

    @Test
    void testColumnWidthRespected() {
        header.add("Test", 10);
        String result = header.format();
        String[] lines = result.split("\n");
        String dashLine = lines[1];
        assertTrue(dashLine.contains("----------"));
    }

    @Test
    void testLongColumnNameTruncated() {
        header.add("VeryLongColumnName", 10);
        String result = header.format();
        String[] lines = result.split("\n");
        assertTrue(lines[0].length() <= 11);
    }

    @Test
    void testMultipleColumnsProduceTwoLines() {
        header.add("A", 5);
        header.add("B", 5);
        String result = header.format();
        String[] lines = result.split("\n");
        assertEquals(2, lines.length);
    }

    @Test
    void testFormatStructure() {
        header.add("Col1", 8);
        header.add("Col2", 8);
        String result = header.format();
        String[] lines = result.split("\n");

        assertEquals(2, lines.length);
        assertTrue(lines[0].startsWith("Col1"));
        assertTrue(lines[1].startsWith("--------"));
    }

    @Test
    void testSingleCharacterWidth() {
        header.add("X", 1);
        String result = header.format();
        assertTrue(result.contains("X"));
        assertTrue(result.contains("-"));
    }

    @Test
    void testLargeWidth() {
        header.add("Title", 50);
        String result = header.format();
        assertTrue(result.contains("-".repeat(50)));
    }

    @Test
    void testFormatProducesConsistentOutput() {
        header.add("Test", 10);
        String result1 = header.format();
        String result2 = header.format();
        assertEquals(result1, result2);
    }
}
