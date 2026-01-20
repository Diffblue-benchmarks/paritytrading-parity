package com.paritytrading.parity.client;

import com.diffblue.cover.annotations.InterestingTestFactory;

/**
 * Test factory for creating Errors instances for unit testing.
 */
public class ErrorsTestFactory {

    /**
     * Creates an Errors instance for testing.
     * This factory uses reflection to create an instance since Errors
     * has a private constructor.
     *
     * @return an Errors instance for testing
     */
    @InterestingTestFactory
    public static Errors createErrors() {
        try {
            // Use reflection to access the private constructor
            java.lang.reflect.Constructor<Errors> constructor =
                Errors.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Errors for testing", e);
        }
    }
}
