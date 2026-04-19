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

import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class PMRVersionTest {

    @Test
    void getVersion() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(2);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(2, version.version);
    }

    @Test
    void getVersionLargeUnsignedValue() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt((int) 0xFFFFFFFFL);
        buffer.flip();

        PMR.Version version = new PMR.Version();
        version.get(buffer);

        assertEquals(0xFFFFFFFFL, version.version);
    }

    @Test
    void putVersion() {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        PMR.Version version = new PMR.Version();
        version.version = 2;
        version.put(buffer);

        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals(2, buffer.getInt());
    }

    @Test
    void putVersionLargeUnsignedValue() {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        PMR.Version version = new PMR.Version();
        version.version = 0xFFFFFFFFL;
        version.put(buffer);

        buffer.flip();

        assertEquals('V', buffer.get());
        assertEquals((int) 0xFFFFFFFFL, buffer.getInt());
    }

    @Test
    void roundTrip() {
        PMR.Version original = new PMR.Version();
        original.version = PMR.VERSION;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        original.put(buffer);

        buffer.flip();
        buffer.get(); // skip message type byte

        PMR.Version parsed = new PMR.Version();
        parsed.get(buffer);

        assertEquals(original.version, parsed.version);
    }
}
