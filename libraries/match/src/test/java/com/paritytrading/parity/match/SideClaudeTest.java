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

class SideClaudeTest {

    @Test
    void valuesReturnsAllEnumConstants() {
        Side[] sides = Side.values();

        assertEquals(2, sides.length);
        assertEquals(Side.BUY, sides[0]);
        assertEquals(Side.SELL, sides[1]);
    }

    @Test
    void valuesReturnsNewArrayEachTime() {
        Side[] sides1 = Side.values();
        Side[] sides2 = Side.values();

        assertNotSame(sides1, sides2);
    }

    @Test
    void valuesArrayCanBeModifiedWithoutAffectingEnum() {
        Side[] sides = Side.values();
        sides[0] = Side.SELL;

        Side[] freshSides = Side.values();
        assertEquals(Side.BUY, freshSides[0]);
    }

    @Test
    void valueOfReturnsCorrectEnumForBuy() {
        Side side = Side.valueOf("BUY");

        assertEquals(Side.BUY, side);
        assertSame(Side.BUY, side);
    }

    @Test
    void valueOfReturnsCorrectEnumForSell() {
        Side side = Side.valueOf("SELL");

        assertEquals(Side.SELL, side);
        assertSame(Side.SELL, side);
    }

    @Test
    void valueOfThrowsExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Side.valueOf("INVALID");
        });
    }

    @Test
    void valueOfThrowsExceptionForNull() {
        assertThrows(NullPointerException.class, () -> {
            Side.valueOf(null);
        });
    }

    @Test
    void valueOfThrowsExceptionForLowercaseName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Side.valueOf("buy");
        });
    }

    @Test
    void valueOfThrowsExceptionForMixedCaseName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Side.valueOf("Buy");
        });
    }

    @Test
    void valueOfThrowsExceptionForEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> {
            Side.valueOf("");
        });
    }

    @Test
    void valueOfThrowsExceptionForWhitespace() {
        assertThrows(IllegalArgumentException.class, () -> {
            Side.valueOf(" BUY ");
        });
    }

    @Test
    void enumConstantsAreSingletons() {
        assertSame(Side.BUY, Side.valueOf("BUY"));
        assertSame(Side.SELL, Side.valueOf("SELL"));
    }

    @Test
    void buyAndSellAreDistinct() {
        assertNotEquals(Side.BUY, Side.SELL);
        assertNotSame(Side.BUY, Side.SELL);
    }

    @Test
    void toStringReturnsBuyForBuy() {
        assertEquals("BUY", Side.BUY.toString());
    }

    @Test
    void toStringReturnsSellForSell() {
        assertEquals("SELL", Side.SELL.toString());
    }

    @Test
    void nameReturnsBuyForBuy() {
        assertEquals("BUY", Side.BUY.name());
    }

    @Test
    void nameReturnsSellForSell() {
        assertEquals("SELL", Side.SELL.name());
    }

    @Test
    void ordinalReturnsBuyAsFirst() {
        assertEquals(0, Side.BUY.ordinal());
    }

    @Test
    void ordinalReturnsSellAsSecond() {
        assertEquals(1, Side.SELL.ordinal());
    }

}
