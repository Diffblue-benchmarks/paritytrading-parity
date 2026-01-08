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
package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.*;

import com.paritytrading.parity.net.ProtocolException;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class PMDExceptionClaudeTest {

    @Test
    void constructorWithMessage() {
        String message = "Invalid PMD message format";
        PMDException exception = new PMDException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructorWithNullMessage() {
        PMDException exception = new PMDException(null);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructorWithEmptyMessage() {
        String message = "";
        PMDException exception = new PMDException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void extendsProtocolException() {
        PMDException exception = new PMDException("test");

        assertTrue(exception instanceof ProtocolException);
    }

    @Test
    void extendsIOException() {
        PMDException exception = new PMDException("test");

        assertTrue(exception instanceof IOException);
    }

    @Test
    void canBeThrown() {
        assertThrows(PMDException.class, () -> {
            throw new PMDException("Test exception");
        });
    }

    @Test
    void canBeCaughtAsPMDException() {
        try {
            throw new PMDException("Test message");
        } catch (PMDException e) {
            assertEquals("Test message", e.getMessage());
        } catch (Exception e) {
            fail("Should have caught PMDException");
        }
    }

    @Test
    void canBeCaughtAsProtocolException() {
        try {
            throw new PMDException("Test message");
        } catch (ProtocolException e) {
            assertTrue(e instanceof PMDException);
            assertEquals("Test message", e.getMessage());
        } catch (Exception e) {
            fail("Should have caught as ProtocolException");
        }
    }

    @Test
    void canBeCaughtAsIOException() {
        try {
            throw new PMDException("Test message");
        } catch (IOException e) {
            assertTrue(e instanceof PMDException);
            assertEquals("Test message", e.getMessage());
        } catch (Exception e) {
            fail("Should have caught as IOException");
        }
    }

    @Test
    void stackTraceIsPreserved() {
        PMDException exception = new PMDException("Test");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    void differentMessagesCreateDifferentExceptions() {
        PMDException exception1 = new PMDException("Message 1");
        PMDException exception2 = new PMDException("Message 2");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
    }
}
