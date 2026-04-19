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

class ProtocolExceptionTest {

    @Test
    void constructWithMessage() {
        ProtocolException exception = new ProtocolException("test message");

        assertEquals("test message", exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof IOException);
    }

    @Test
    void constructWithMessageAndCause() {
        Throwable cause = new RuntimeException("root cause");

        ProtocolException exception = new ProtocolException("test message", cause);

        assertEquals("test message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void constructWithCause() {
        Throwable cause = new RuntimeException("root cause");

        ProtocolException exception = new ProtocolException(cause);

        assertSame(cause, exception.getCause());
        assertEquals("java.lang.RuntimeException: root cause", exception.getMessage());
    }
}
