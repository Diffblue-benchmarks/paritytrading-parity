package com.paritytrading.parity.client;

import com.diffblue.cover.annotations.InterestingTestFactory;

/**
 * Test factory for creating Trades instances for unit testing.
 */
public class TradesTestFactory {

    /**
     * Creates a Trades instance for testing.
     * This factory uses reflection to create an instance since Trades
     * has a private constructor.
     *
     * @return a Trades instance for testing
     */
    @InterestingTestFactory
    public static Trades createTrades() {
        try {
            // Use reflection to access the private constructor
            java.lang.reflect.Constructor<Trades> constructor =
                Trades.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Trades for testing", e);
        }
    }
}
