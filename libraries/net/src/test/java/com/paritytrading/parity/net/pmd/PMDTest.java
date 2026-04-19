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

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class PMDTest {

    @Test
    void privateConstructor() throws Exception {
        Constructor<PMD> constructor = PMD.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        PMD instance = constructor.newInstance();

        assertNotNull(instance);
    }

    @Test
    void versionRoundTrip() {
        PMD.Version original = new PMD.Version();
        original.version = 2;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        original.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals('V', messageType);

        PMD.Version parsed = new PMD.Version();
        parsed.get(buffer);

        assertEquals(2, parsed.version);
    }

    @Test
    void unsignedIntMaxValue() {
        PMD.Version original = new PMD.Version();
        original.version = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMD.Version parsed = new PMD.Version();
        parsed.get(buffer);

        assertEquals(0xFFFFFFFFL, parsed.version);
    }

    @Test
    void unsignedIntZeroValue() {
        PMD.Version original = new PMD.Version();
        original.version = 0;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        original.put(buffer);
        buffer.flip();

        buffer.get();

        PMD.Version parsed = new PMD.Version();
        parsed.get(buffer);

        assertEquals(0, parsed.version);
    }

    @Test
    void orderExecutedRoundTripWithUnsignedMatchNumber() {
        PMD.OrderExecuted original = new PMD.OrderExecuted();
        original.timestamp   = 1234567890L;
        original.orderNumber = 42L;
        original.quantity    = 100L;
        original.matchNumber = 0xABCDEF01L;

        ByteBuffer buffer = ByteBuffer.allocate(29);
        original.put(buffer);
        buffer.flip();

        byte messageType = buffer.get();
        assertEquals('E', messageType);

        PMD.OrderExecuted parsed = new PMD.OrderExecuted();
        parsed.get(buffer);

        assertEquals(1234567890L, parsed.timestamp);
        assertEquals(42L, parsed.orderNumber);
        assertEquals(100L, parsed.quantity);
        assertEquals(0xABCDEF01L, parsed.matchNumber);
    }
}
