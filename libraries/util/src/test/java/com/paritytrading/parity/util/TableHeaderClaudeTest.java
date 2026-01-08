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

class TableHeaderClaudeTest {

    @Test
    void testConstructor() {
        // Test that constructor creates a valid instance
        TableHeader header = new TableHeader();
        assertNotNull(header);
        // Format should return empty string for new instance with no columns
        assertEquals("", header.format());
    }

    @Test
    void testAddSingleColumn() {
        TableHeader header = new TableHeader();
        header.add("Name", 10);

        String expected = "Name      \n" +
                          "----------\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testAddMultipleColumns() {
        TableHeader header = new TableHeader();
        header.add("ID", 5);
        header.add("Name", 10);
        header.add("Value", 8);

        String expected = "ID    Name       Value   \n" +
                          "----- ---------- --------\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testAddColumnWithNameLongerThanWidth() {
        // Test that column names are truncated to width
        TableHeader header = new TableHeader();
        header.add("VeryLongColumnName", 5);

        String expected = "VeryL\n" +
                          "-----\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testAddColumnWithNameExactlyWidth() {
        TableHeader header = new TableHeader();
        header.add("Exact", 5);

        String expected = "Exact\n" +
                          "-----\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testAddColumnWithZeroWidth() {
        // Zero width causes MissingFormatWidthException, which is expected behavior
        TableHeader header = new TableHeader();
        header.add("Test", 0);

        // This will throw an exception when format() is called
        assertThrows(java.util.MissingFormatWidthException.class, () -> {
            header.format();
        });
    }

    @Test
    void testAddColumnWithWidthOne() {
        TableHeader header = new TableHeader();
        header.add("T", 1);

        String expected = "T\n" +
                          "-\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testAddEmptyColumnName() {
        TableHeader header = new TableHeader();
        header.add("", 5);

        String expected = "     \n" +
                          "-----\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testAddMultipleColumnsWithVaryingWidths() {
        TableHeader header = new TableHeader();
        header.add("A", 1);
        header.add("Medium", 6);
        header.add("VeryLongName", 12);

        String expected = "A Medium VeryLongName\n" +
                          "- ------ ------------\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testFormatWithoutAddingColumns() {
        TableHeader header = new TableHeader();

        // Should return empty string when no columns added
        assertEquals("", header.format());
    }

    @Test
    void testAddingColumnsSequentially() {
        // Test that columns maintain order
        TableHeader header = new TableHeader();
        header.add("First", 5);
        header.add("Second", 6);
        header.add("Third", 5);

        String expected = "First Second Third\n" +
                          "----- ------ -----\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testColumnWithSpecialCharacters() {
        TableHeader header = new TableHeader();
        header.add("A!@#", 4);
        header.add("B$%^", 4);

        String expected = "A!@# B$%^\n" +
                          "---- ----\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testColumnWithWhitespace() {
        TableHeader header = new TableHeader();
        header.add("A B", 5);
        header.add("C  D", 5);

        String expected = "A B   C  D \n" +
                          "----- -----\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testMultipleCallsToFormat() {
        // Test that format can be called multiple times with same result
        TableHeader header = new TableHeader();
        header.add("Col1", 4);
        header.add("Col2", 4);

        String expected = "Col1 Col2\n" +
                          "---- ----\n";

        assertEquals(expected, header.format());
        assertEquals(expected, header.format());
        assertEquals(expected, header.format());
    }

    @Test
    void testAddingColumnsAfterFormat() {
        // Test that columns can be added after calling format
        TableHeader header = new TableHeader();
        header.add("First", 5);

        String firstFormat = header.format();
        assertEquals("First\n-----\n", firstFormat);

        header.add("Second", 6);

        String secondFormat = header.format();
        assertEquals("First Second\n----- ------\n", secondFormat);
    }

    @Test
    void testLargeNumberOfColumns() {
        TableHeader header = new TableHeader();
        for (int i = 1; i <= 10; i++) {
            header.add("C" + i, 3);
        }

        String expected = "C1  C2  C3  C4  C5  C6  C7  C8  C9  C10\n" +
                          "--- --- --- --- --- --- --- --- --- ---\n";
        assertEquals(expected, header.format());
    }

    @Test
    void testColumnNameWithNonAsciiCharacters() {
        TableHeader header = new TableHeader();
        header.add("Café", 6);
        header.add("日本", 6);

        // Multi-byte characters may have different display widths
        // The format method uses String.format which counts characters, not display width
        String expected = "Café   日本    \n" +
                          "------ ------\n";
        assertEquals(expected, header.format());
    }
}
