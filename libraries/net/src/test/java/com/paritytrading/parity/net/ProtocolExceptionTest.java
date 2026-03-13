package com.paritytrading.parity.net;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ProtocolExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        ProtocolException ex = new ProtocolException("test message");
        assertEquals("test message", ex.getMessage());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        IOException cause = new IOException("cause");
        ProtocolException ex = new ProtocolException("test message", cause);
        assertEquals("test message", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testConstructorWithCause() {
        IOException cause = new IOException("cause");
        ProtocolException ex = new ProtocolException(cause);
        assertNotNull(ex);
        assertSame(cause, ex.getCause());
    }
}
