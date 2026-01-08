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

class SideClaudeTest {

    @Test
    void testValuesReturnsAllEnumConstants() {
        Side[] sides = Side.values();

        assertNotNull(sides);
        assertEquals(2, sides.length);
        assertEquals(Side.BUY, sides[0]);
        assertEquals(Side.SELL, sides[1]);
    }

    @Test
    void testValuesReturnsNewArrayEachTime() {
        Side[] sides1 = Side.values();
        Side[] sides2 = Side.values();

        assertNotSame(sides1, sides2);
    }

    @Test
    void testValueOfBuy() {
        Side side = Side.valueOf("BUY");

        assertNotNull(side);
        assertEquals(Side.BUY, side);
        assertSame(Side.BUY, side);
    }

    @Test
    void testValueOfSell() {
        Side side = Side.valueOf("SELL");

        assertNotNull(side);
        assertEquals(Side.SELL, side);
        assertSame(Side.SELL, side);
    }

    @Test
    void testValueOfWithNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            Side.valueOf(null);
        });
    }

    @Test
    void testValueOfWithInvalidNameThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Side.valueOf("INVALID");
        });
    }

    @Test
    void testValueOfWithEmptyStringThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Side.valueOf("");
        });
    }

    @Test
    void testValueOfWithLowercaseThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Side.valueOf("buy");
        });
    }

    @Test
    void testEnumConstantsBuyAndSell() {
        assertNotNull(Side.BUY);
        assertNotNull(Side.SELL);
        assertNotSame(Side.BUY, Side.SELL);
    }

    @Test
    void testEnumToString() {
        assertEquals("BUY", Side.BUY.toString());
        assertEquals("SELL", Side.SELL.toString());
    }

    @Test
    void testEnumName() {
        assertEquals("BUY", Side.BUY.name());
        assertEquals("SELL", Side.SELL.name());
    }

    @Test
    void testEnumOrdinal() {
        assertEquals(0, Side.BUY.ordinal());
        assertEquals(1, Side.SELL.ordinal());
    }

    @Test
    void testValueOfMatchesEnumConstant() {
        assertSame(Side.BUY, Side.valueOf("BUY"));
        assertSame(Side.SELL, Side.valueOf("SELL"));
    }
}
