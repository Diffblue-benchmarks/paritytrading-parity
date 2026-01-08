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
package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SideClaude_clinitTest {

    @Test
    void testStaticInitializationByAccessingBuyConstant() {
        Side buy = Side.BUY;

        assertNotNull(buy);
        assertEquals("BUY", buy.name());
    }

    @Test
    void testStaticInitializationByAccessingSellConstant() {
        Side sell = Side.SELL;

        assertNotNull(sell);
        assertEquals("SELL", sell.name());
    }

    @Test
    void testStaticInitializationByCallingValues() {
        Side[] values = Side.values();

        assertNotNull(values);
        assertEquals(2, values.length);
        assertTrue(values[0] == Side.BUY || values[0] == Side.SELL);
        assertTrue(values[1] == Side.BUY || values[1] == Side.SELL);
    }

    @Test
    void testStaticInitializationByCallingValueOf() {
        Side buy = Side.valueOf("BUY");
        Side sell = Side.valueOf("SELL");

        assertNotNull(buy);
        assertNotNull(sell);
        assertSame(Side.BUY, buy);
        assertSame(Side.SELL, sell);
    }

    @Test
    void testEnumConstantsAreInitializedCorrectly() {
        assertNotNull(Side.BUY);
        assertNotNull(Side.SELL);

        assertEquals(0, Side.BUY.ordinal());
        assertEquals(1, Side.SELL.ordinal());

        assertEquals("BUY", Side.BUY.name());
        assertEquals("SELL", Side.SELL.name());
    }

    @Test
    void testEnumClassIsLoadedAndInitialized() {
        Class<Side> sideClass = Side.class;

        assertNotNull(sideClass);
        assertTrue(sideClass.isEnum());
        assertEquals("com.paritytrading.parity.book.Side", sideClass.getName());

        Object[] enumConstants = sideClass.getEnumConstants();
        assertNotNull(enumConstants);
        assertEquals(2, enumConstants.length);
    }
}
