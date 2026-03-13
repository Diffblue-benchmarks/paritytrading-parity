package com.paritytrading.parity.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringsTest {

    @Test
    public void testRepeatWithSingleCount() {
        String result = Strings.repeat('A', 1);
        assertEquals("A", result);
    }

    @Test
    public void testRepeatWithMultipleCount() {
        String result = Strings.repeat('X', 5);
        assertEquals("XXXXX", result);
    }

    @Test
    public void testRepeatWithZeroCount() {
        String result = Strings.repeat('Z', 0);
        assertEquals("", result);
    }

    @Test
    public void testRepeatWithDifferentCharacter() {
        String result = Strings.repeat('-', 3);
        assertEquals("---", result);
    }

    @Test
    public void testRepeatWithSpace() {
        String result = Strings.repeat(' ', 4);
        assertEquals("    ", result);
    }
}
