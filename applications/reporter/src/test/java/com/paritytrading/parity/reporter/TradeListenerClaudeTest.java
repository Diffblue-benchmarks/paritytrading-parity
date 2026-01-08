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
package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TradeListenerClaudeTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    // Concrete implementation of TradeListener for testing
    private static class TestTradeListener extends TradeListener {
        @Override
        void trade(Trade event) {
            // No-op implementation for testing
        }

        // Public method to expose printf for testing
        public void testPrintf(String format, Object... args) {
            printf(format, args);
        }
    }

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void printfWithSimpleString() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("Hello, World!");

        assertEquals("Hello, World!", outputStream.toString());
    }

    @Test
    void printfWithStringFormatting() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("Hello, %s!", "Java");

        assertEquals("Hello, Java!", outputStream.toString());
    }

    @Test
    void printfWithMultipleArguments() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("%s %s %d", "Hello", "World", 123);

        assertEquals("Hello World 123", outputStream.toString());
    }

    @Test
    void printfWithIntegerFormatting() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("Value: %d", 42);

        assertEquals("Value: 42", outputStream.toString());
    }

    @Test
    void printfWithFloatingPointFormatting() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("Price: %.2f", 123.456);

        assertEquals("Price: 123.46", outputStream.toString());
    }

    @Test
    void printfWithPaddedFormatting() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("%10s", "test");

        assertEquals("      test", outputStream.toString());
    }

    @Test
    void printfWithLeftAlignedFormatting() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("%-10s", "test");

        assertEquals("test      ", outputStream.toString());
    }

    @Test
    void printfWithNoArguments() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("No args");

        assertEquals("No args", outputStream.toString());
    }

    @Test
    void printfWithEmptyString() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("");

        assertEquals("", outputStream.toString());
    }

    @Test
    void printfUsesUSLocale() {
        TestTradeListener listener = new TestTradeListener();

        // Large numbers with thousand separators - US locale uses comma
        listener.testPrintf("%,d", 1000000);

        assertEquals("1,000,000", outputStream.toString());
    }

    @Test
    void printfWithFloatingPointUSLocale() {
        TestTradeListener listener = new TestTradeListener();

        // US locale uses period as decimal separator
        listener.testPrintf("%.3f", 1234.5678);

        assertEquals("1234.568", outputStream.toString());
    }

    @Test
    void printfMultipleCalls() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("First ");
        listener.testPrintf("Second");

        assertEquals("First Second", outputStream.toString());
    }

    @Test
    void printfWithComplexFormatting() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("%12s %-8s %10d", "timestamp", "AAPL", 1000);

        assertEquals("   timestamp AAPL           1000", outputStream.toString());
    }

    @Test
    void printfWithNewlineCharacter() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("Line 1\nLine 2\n");

        assertEquals("Line 1\nLine 2\n", outputStream.toString());
    }

    @Test
    void printfWithPercentEscape() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("100%% complete");

        assertEquals("100% complete", outputStream.toString());
    }

    @Test
    void printfWithLongValue() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("Long: %d", Long.MAX_VALUE);

        assertEquals("Long: 9223372036854775807", outputStream.toString());
    }

    @Test
    void printfWithZeroPadding() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("%08d", 42);

        assertEquals("00000042", outputStream.toString());
    }

    @Test
    void printfWithHexFormatting() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("Hex: %x", 255);

        assertEquals("Hex: ff", outputStream.toString());
    }

    @Test
    void printfWithUppercaseHexFormatting() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("Hex: %X", 255);

        assertEquals("Hex: FF", outputStream.toString());
    }

    @Test
    void printfWithScientificNotation() {
        TestTradeListener listener = new TestTradeListener();

        listener.testPrintf("%.2e", 12345.6789);

        assertEquals("1.23e+04", outputStream.toString());
    }
}
