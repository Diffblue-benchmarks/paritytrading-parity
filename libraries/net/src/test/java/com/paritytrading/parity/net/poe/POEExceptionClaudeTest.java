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
package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class POEExceptionClaudeTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Test error message";
        POEException exception = new POEException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithNullMessage() {
        POEException exception = new POEException(null);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithEmptyMessage() {
        String message = "";
        POEException exception = new POEException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionCanBeThrown() {
        String message = "Protocol error occurred";

        assertThrows(POEException.class, () -> {
            throw new POEException(message);
        });
    }

    @Test
    void testExceptionMessagePreservedWhenThrown() {
        String message = "Invalid POE message format";

        POEException thrown = assertThrows(POEException.class, () -> {
            throw new POEException(message);
        });

        assertEquals(message, thrown.getMessage());
    }

    @Test
    void testExceptionIsInstanceOfProtocolException() {
        POEException exception = new POEException("test");

        assertTrue(exception instanceof com.paritytrading.parity.net.ProtocolException);
    }

    @Test
    void testExceptionIsInstanceOfIOException() {
        POEException exception = new POEException("test");

        assertTrue(exception instanceof java.io.IOException);
    }
}
