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

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

class PMRTest {

    @Test
    void privateConstructor() throws Exception {
        Constructor<PMR> constructor = PMR.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        PMR instance = constructor.newInstance();

        assertNotNull(instance);
    }

    @Test
    void versionRoundTrip() {
        PMR.Version version = new PMR.Version();
        version.version = 2;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        // skip message type byte
        buffer.get();

        PMR.Version parsed = new PMR.Version();
        parsed.get(buffer);

        assertEquals(2, parsed.version);
    }

    @Test
    void versionRoundTripLargeUnsignedValue() {
        PMR.Version version = new PMR.Version();
        version.version = 0xFFFFFFFFL;

        ByteBuffer buffer = ByteBuffer.allocate(5);
        version.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Version parsed = new PMR.Version();
        parsed.get(buffer);

        assertEquals(0xFFFFFFFFL, parsed.version);
    }

    @Test
    void tradeRoundTripUnsignedMatchNumber() {
        PMR.Trade trade = new PMR.Trade();
        trade.timestamp           = 1000L;
        trade.restingOrderNumber  = 2000L;
        trade.incomingOrderNumber = 3000L;
        trade.quantity            = 100L;
        trade.matchNumber         = 0x80000000L;

        ByteBuffer buffer = ByteBuffer.allocate(37);
        trade.put(buffer);
        buffer.flip();

        buffer.get();

        PMR.Trade parsed = new PMR.Trade();
        parsed.get(buffer);

        assertEquals(1000L, parsed.timestamp);
        assertEquals(2000L, parsed.restingOrderNumber);
        assertEquals(3000L, parsed.incomingOrderNumber);
        assertEquals(100L, parsed.quantity);
        assertEquals(0x80000000L, parsed.matchNumber);
    }
}
