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
package com.paritytrading.parity.net;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class ProtocolExceptionClaudeTest {

    @Test
    void constructorWithMessage() {
        String message = "Invalid protocol format";
        ProtocolException exception = new ProtocolException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructorWithNullMessage() {
        ProtocolException exception = new ProtocolException((String) null);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructorWithEmptyMessage() {
        String message = "";
        ProtocolException exception = new ProtocolException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructorWithMessageAndCause() {
        String message = "Protocol error occurred";
        Throwable cause = new IllegalArgumentException("Invalid argument");
        ProtocolException exception = new ProtocolException(message, cause);

        assertEquals(message, exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void constructorWithMessageAndNullCause() {
        String message = "Protocol error";
        ProtocolException exception = new ProtocolException(message, null);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructorWithNullMessageAndCause() {
        Throwable cause = new RuntimeException("Runtime error");
        ProtocolException exception = new ProtocolException(null, cause);

        assertNull(exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void constructorWithCause() {
        Throwable cause = new IOException("IO error");
        ProtocolException exception = new ProtocolException(cause);

        assertSame(cause, exception.getCause());
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("IOException"));
    }

    @Test
    void constructorWithNullCauseOnly() {
        ProtocolException exception = new ProtocolException((Throwable) null);

        assertNull(exception.getCause());
    }

    @Test
    void extendsIOException() {
        ProtocolException exception = new ProtocolException("test");

        assertTrue(exception instanceof IOException);
    }

    @Test
    void canBeThrown() {
        assertThrows(ProtocolException.class, () -> {
            throw new ProtocolException("Test exception");
        });
    }

    @Test
    void canBeCaught() {
        try {
            throw new ProtocolException("Test message");
        } catch (ProtocolException e) {
            assertEquals("Test message", e.getMessage());
        } catch (Exception e) {
            fail("Should have caught ProtocolException");
        }
    }

    @Test
    void canBeCaughtAsIOException() {
        try {
            throw new ProtocolException("Test message");
        } catch (IOException e) {
            assertTrue(e instanceof ProtocolException);
            assertEquals("Test message", e.getMessage());
        } catch (Exception e) {
            fail("Should have caught as IOException");
        }
    }

    @Test
    void causeChainingWorks() {
        Throwable rootCause = new IllegalStateException("Root cause");
        Throwable intermediateCause = new RuntimeException("Intermediate", rootCause);
        ProtocolException exception = new ProtocolException("Top level", intermediateCause);

        assertEquals("Top level", exception.getMessage());
        assertSame(intermediateCause, exception.getCause());
        assertSame(rootCause, exception.getCause().getCause());
    }

    @Test
    void stackTraceIsPreserved() {
        ProtocolException exception = new ProtocolException("Test");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    void differentMessagesCreateDifferentExceptions() {
        ProtocolException exception1 = new ProtocolException("Message 1");
        ProtocolException exception2 = new ProtocolException("Message 2");

        assertNotEquals(exception1.getMessage(), exception2.getMessage());
    }

    @Test
    void sameMessagesDifferentCauses() {
        String message = "Same message";
        Throwable cause1 = new IllegalArgumentException();
        Throwable cause2 = new IllegalStateException();

        ProtocolException exception1 = new ProtocolException(message, cause1);
        ProtocolException exception2 = new ProtocolException(message, cause2);

        assertEquals(exception1.getMessage(), exception2.getMessage());
        assertNotSame(exception1.getCause(), exception2.getCause());
    }
}
