package com.paritytrading.parity.net.pmd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PMDExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        PMDException ex = new PMDException("test message");
        assertNotNull(ex);
        assertEquals("test message", ex.getMessage());
    }
}
