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

import java.nio.ByteBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PMDVersionTest {

    private PMD.Version version;

    @BeforeEach
    void setUp() {
        version = new PMD.Version();
    }

    @Test
    void getReadsVersionFromBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(2);
        buffer.flip();

        version.get(buffer);

        assertEquals(2, version.version);
    }

    @Test
    void getReadsLargeUnsignedValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt((int) 0xFFFFFFFFL);
        buffer.flip();

        version.get(buffer);

        assertEquals(0xFFFFFFFFL, version.version);
    }

    @Test
    void putWritesMessageTypeAndVersion() {
        version.version = 2;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals(2, buffer.getInt() & 0xFFFFFFFFL);
    }

    @Test
    void putWritesLargeUnsignedVersion() {
        version.version = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals(0xFFFFFFFFL, buffer.getInt() & 0xFFFFFFFFL);
    }

    @Test
    void roundTrip() {
        version.version = PMD.VERSION;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        // skip message type byte
        buffer.get();

        PMD.Version restored = new PMD.Version();
        restored.get(buffer);

        assertEquals(PMD.VERSION, restored.version);
    }
}
