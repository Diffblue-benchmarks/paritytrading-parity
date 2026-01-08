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
package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.parity.net.ProtocolException;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class PMRExceptionClaudeTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Test error message";
        PMRException exception = new PMRException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithNullMessage() {
        PMRException exception = new PMRException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithEmptyMessage() {
        String message = "";
        PMRException exception = new PMRException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithLongMessage() {
        String message = "This is a very long error message that contains a lot of details about what went wrong in the protocol handling and why the error occurred";
        PMRException exception = new PMRException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithSpecialCharacters() {
        String message = "Error: Invalid protocol message\nLine 2\tTab\r\nLine 3";
        PMRException exception = new PMRException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithUnicodeCharacters() {
        String message = "Error: Protocol error 错误 エラー 🔥";
        PMRException exception = new PMRException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testIsInstanceOfProtocolException() {
        PMRException exception = new PMRException("Test");

        assertTrue(exception instanceof ProtocolException);
    }

    @Test
    void testIsInstanceOfIOException() {
        PMRException exception = new PMRException("Test");

        assertTrue(exception instanceof IOException);
    }

    @Test
    void testCanBeThrownAndCaughtAsPMRException() {
        try {
            throw new PMRException("Test exception");
        } catch (PMRException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    @Test
    void testCanBeThrownAndCaughtAsProtocolException() {
        try {
            throw new PMRException("Test exception");
        } catch (ProtocolException e) {
            assertEquals("Test exception", e.getMessage());
            assertTrue(e instanceof PMRException);
        }
    }

    @Test
    void testCanBeThrownAndCaughtAsIOException() {
        try {
            throw new PMRException("Test exception");
        } catch (IOException e) {
            assertEquals("Test exception", e.getMessage());
            assertTrue(e instanceof PMRException);
        }
    }

    @Test
    void testExceptionStackTraceContainsConstructionLocation() {
        PMRException exception = new PMRException("Test");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    void testToStringContainsClassName() {
        PMRException exception = new PMRException("Error message");
        String toString = exception.toString();

        assertTrue(toString.contains("PMRException"));
    }

    @Test
    void testToStringContainsMessage() {
        String message = "Error message";
        PMRException exception = new PMRException(message);
        String toString = exception.toString();

        assertTrue(toString.contains(message));
    }

    @Test
    void testMultipleInstancesWithSameMessage() {
        String message = "Same message";
        PMRException exception1 = new PMRException(message);
        PMRException exception2 = new PMRException(message);

        assertEquals(exception1.getMessage(), exception2.getMessage());
        assertNotSame(exception1, exception2);
    }

    @Test
    void testMultipleInstancesWithDifferentMessages() {
        PMRException exception1 = new PMRException("Message 1");
        PMRException exception2 = new PMRException("Message 2");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
    }
}
