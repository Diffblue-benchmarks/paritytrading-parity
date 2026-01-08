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
package com.paritytrading.parity.match;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SideClaude_clinitTest {

    @Test
    void staticInitializerCreatesBuyConstant() {
        // Accessing the BUY constant triggers static initialization if not already done
        Side buy = Side.BUY;

        assertNotNull(buy);
        assertEquals("BUY", buy.name());
        assertEquals(0, buy.ordinal());
    }

    @Test
    void staticInitializerCreatesSellConstant() {
        // Accessing the SELL constant ensures static initialization has occurred
        Side sell = Side.SELL;

        assertNotNull(sell);
        assertEquals("SELL", sell.name());
        assertEquals(1, sell.ordinal());
    }

    @Test
    void staticInitializerCreatesBothConstants() {
        // Force class initialization by accessing enum constants
        Side buy = Side.BUY;
        Side sell = Side.SELL;

        assertNotNull(buy);
        assertNotNull(sell);
        assertNotSame(buy, sell);
    }

    @Test
    void valuesArrayContainsInitializedConstants() {
        // Calling values() ensures the enum is fully initialized
        Side[] values = Side.values();

        assertEquals(2, values.length);
        assertSame(Side.BUY, values[0]);
        assertSame(Side.SELL, values[1]);
    }

    @Test
    void valueOfAccessesInitializedConstants() {
        // Using valueOf ensures the enum constants were properly initialized
        Side buy = Side.valueOf("BUY");
        Side sell = Side.valueOf("SELL");

        assertSame(Side.BUY, buy);
        assertSame(Side.SELL, sell);
    }

    @Test
    void enumConstantsAreInitializedBeforeFirstUse() {
        // Direct reference to enum constants to ensure initialization
        assertNotNull(Side.BUY);
        assertNotNull(Side.SELL);

        // Verify they are properly initialized as enum constants
        assertTrue(Side.BUY instanceof Side);
        assertTrue(Side.SELL instanceof Side);
    }

    @Test
    void allEnumConstantsAreAccessible() {
        // Comprehensive check that all constants are initialized and accessible
        Side[] allValues = Side.values();

        for (Side side : allValues) {
            assertNotNull(side);
            assertNotNull(side.name());
            assertTrue(side.ordinal() >= 0);
        }
    }

    @Test
    void enumClassIsProperlyInitialized() {
        // Accessing the class and its constants ensures full initialization
        Class<Side> sideClass = Side.class;

        assertNotNull(sideClass);
        assertTrue(sideClass.isEnum());
        assertEquals(2, sideClass.getEnumConstants().length);
    }

}
