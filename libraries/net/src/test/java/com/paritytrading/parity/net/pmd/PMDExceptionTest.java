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

import java.io.IOException;

import com.paritytrading.parity.net.ProtocolException;
import org.junit.jupiter.api.Test;

class PMDExceptionTest {

    @Test
    void constructWithMessage() {
        PMDException exception = new PMDException("test error");

        assertEquals("test error", exception.getMessage());
    }

    @Test
    void isProtocolException() {
        PMDException exception = new PMDException("protocol error");

        assertTrue(exception instanceof ProtocolException);
    }

    @Test
    void isIOException() {
        PMDException exception = new PMDException("io error");

        assertTrue(exception instanceof IOException);
    }

    @Test
    void constructWithNullMessage() {
        PMDException exception = new PMDException(null);

        assertNull(exception.getMessage());
    }
}
