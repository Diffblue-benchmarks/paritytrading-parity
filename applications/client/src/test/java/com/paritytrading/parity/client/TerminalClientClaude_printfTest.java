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
package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for TerminalClient.printf() method.
 *
 * The printf method is a static utility that wraps System.out.printf with Locale.US.
 * These tests verify the formatting behavior by capturing stdout.
 */
class TerminalClientClaude_printfTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testPrintfWithSimpleString() {
        TerminalClient.printf("Hello, World!");
        assertEquals("Hello, World!", outputStream.toString());
    }

    @Test
    void testPrintfWithNewline() {
        TerminalClient.printf("Hello\n");
        assertEquals("Hello\n", outputStream.toString());
    }

    @Test
    void testPrintfWithStringFormat() {
        TerminalClient.printf("Hello, %s!", "Alice");
        assertEquals("Hello, Alice!", outputStream.toString());
    }

    @Test
    void testPrintfWithMultipleStringFormats() {
        TerminalClient.printf("%s %s", "Hello", "World");
        assertEquals("Hello World", outputStream.toString());
    }

    @Test
    void testPrintfWithIntegerFormat() {
        TerminalClient.printf("Number: %d", 42);
        assertEquals("Number: 42", outputStream.toString());
    }

    @Test
    void testPrintfWithMultipleIntegers() {
        TerminalClient.printf("%d + %d = %d", 2, 3, 5);
        assertEquals("2 + 3 = 5", outputStream.toString());
    }

    @Test
    void testPrintfWithFloatFormat() {
        TerminalClient.printf("Value: %.2f", 3.14159);
        assertEquals("Value: 3.14", outputStream.toString());
    }

    @Test
    void testPrintfWithFloatFormatUsesUsLocale() {
        // In US locale, decimal separator is '.' (not ',' as in some other locales)
        TerminalClient.printf("%.2f", 1234.56);
        assertEquals("1234.56", outputStream.toString());
    }

    @Test
    void testPrintfWithLargeNumberUsesUsLocale() {
        // Test that thousands separator behavior follows US locale
        TerminalClient.printf("%,d", 1234567);
        assertEquals("1,234,567", outputStream.toString());
    }

    @Test
    void testPrintfWithMixedFormats() {
        TerminalClient.printf("User: %s, Age: %d, Balance: %.2f", "Bob", 30, 123.45);
        assertEquals("User: Bob, Age: 30, Balance: 123.45", outputStream.toString());
    }

    @Test
    void testPrintfWithEmptyString() {
        TerminalClient.printf("");
        assertEquals("", outputStream.toString());
    }

    @Test
    void testPrintfWithNoArguments() {
        TerminalClient.printf("No placeholders");
        assertEquals("No placeholders", outputStream.toString());
    }

    @Test
    void testPrintfWithEscapedPercent() {
        TerminalClient.printf("100%% complete");
        assertEquals("100% complete", outputStream.toString());
    }

    @Test
    void testPrintfWithBooleanFormat() {
        TerminalClient.printf("Success: %b, Failure: %b", true, false);
        assertEquals("Success: true, Failure: false", outputStream.toString());
    }

    @Test
    void testPrintfWithHexFormat() {
        TerminalClient.printf("Hex: %x", 255);
        assertEquals("Hex: ff", outputStream.toString());
    }

    @Test
    void testPrintfWithUppercaseHexFormat() {
        TerminalClient.printf("Hex: %X", 255);
        assertEquals("Hex: FF", outputStream.toString());
    }

    @Test
    void testPrintfWithCharFormat() {
        TerminalClient.printf("Char: %c", 'A');
        assertEquals("Char: A", outputStream.toString());
    }

    @Test
    void testPrintfWithPaddedInteger() {
        TerminalClient.printf("%05d", 42);
        assertEquals("00042", outputStream.toString());
    }

    @Test
    void testPrintfWithLeftAlignedString() {
        TerminalClient.printf("%-10s|", "test");
        assertEquals("test      |", outputStream.toString());
    }

    @Test
    void testPrintfWithRightAlignedString() {
        TerminalClient.printf("%10s|", "test");
        assertEquals("      test|", outputStream.toString());
    }

    @Test
    void testPrintfWithScientificNotation() {
        TerminalClient.printf("%.2e", 1234.5);
        assertEquals("1.23e+03", outputStream.toString());
    }

    @Test
    void testPrintfWithNullString() {
        TerminalClient.printf("Value: %s", (String) null);
        assertEquals("Value: null", outputStream.toString());
    }

    @Test
    void testPrintfWithVarargs() {
        Object[] args = {"Alice", 25, 100.50};
        TerminalClient.printf("%s: %d years, $%.2f", args);
        assertEquals("Alice: 25 years, $100.50", outputStream.toString());
    }

    @Test
    void testPrintfMultipleCalls() {
        TerminalClient.printf("Line 1\n");
        TerminalClient.printf("Line 2\n");
        assertEquals("Line 1\nLine 2\n", outputStream.toString());
    }

    @Test
    void testPrintfWithComplexFormat() {
        TerminalClient.printf("%-15s %10d %8.2f%%\n", "Item", 42, 75.5);
        assertEquals("Item                    42    75.50%\n", outputStream.toString());
    }

    @Test
    void testPrintfWithSpecialCharacters() {
        TerminalClient.printf("Tab:\t, Newline:\n, Quote: \"");
        assertEquals("Tab:\t, Newline:\n, Quote: \"", outputStream.toString());
    }

    @Test
    void testPrintfWithEmptyVarargs() {
        TerminalClient.printf("No args");
        assertEquals("No args", outputStream.toString());
    }

    @Test
    void testPrintfUsesUsLocaleForDateFormatting() {
        // Test that month names follow US locale conventions
        TerminalClient.printf("%tB", java.time.LocalDate.of(2024, 1, 1));
        // In US locale, January should be "January"
        assertEquals("January", outputStream.toString());
    }

    @Test
    void testPrintfWithPositionalArguments() {
        TerminalClient.printf("%2$s %1$s", "World", "Hello");
        assertEquals("Hello World", outputStream.toString());
    }

    @Test
    void testPrintfWithRepeatedArguments() {
        TerminalClient.printf("%1$s %1$s %1$s", "test");
        assertEquals("test test test", outputStream.toString());
    }
}
