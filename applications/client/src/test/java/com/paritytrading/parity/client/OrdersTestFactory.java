package com.paritytrading.parity.client;

import com.diffblue.cover.annotations.InterestingTestFactory;

/**
 * Test factory for creating Orders instances for unit testing.
 */
public class OrdersTestFactory {

    /**
     * Creates an Orders instance for testing.
     * This factory uses reflection to create an instance since Orders
     * has a private constructor.
     *
     * @return an Orders instance for testing
     */
    @InterestingTestFactory
    public static Orders createOrders() {
        try {
            // Use reflection to access the private constructor
            java.lang.reflect.Constructor<Orders> constructor =
                Orders.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Orders for testing", e);
        }
    }
}
