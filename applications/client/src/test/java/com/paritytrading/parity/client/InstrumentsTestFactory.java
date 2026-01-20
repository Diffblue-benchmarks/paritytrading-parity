package com.paritytrading.parity.client;

import com.diffblue.cover.annotations.InterestingTestFactory;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;

/**
 * Test factory for creating Instruments instances for unit testing.
 */
public class InstrumentsTestFactory {

    /**
     * Creates a basic Instruments instance for testing.
     * This factory creates an instance using reflection since Instruments
     * has a private constructor.
     *
     * @return an Instruments instance for testing
     */
    @InterestingTestFactory
    public static Instruments createInstruments() {
        try {
            // Create a minimal set of test instruments
            Instrument[] instruments = new Instrument[0];

            // Use reflection to access the private constructor
            java.lang.reflect.Constructor<Instruments> constructor =
                Instruments.class.getDeclaredConstructor(
                    Instrument[].class,
                    int.class,
                    int.class,
                    int.class,
                    int.class
                );
            constructor.setAccessible(true);

            // Create with reasonable default values
            return constructor.newInstance(
                instruments,  // values
                2,           // priceIntegerDigits
                2,           // maxPriceFractionDigits
                4,           // sizeIntegerDigits
                0            // maxSizeFractionDigits
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Instruments for testing", e);
        }
    }
}
