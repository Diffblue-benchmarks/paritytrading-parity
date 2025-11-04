package com.paritytrading.parity.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

class ProtocolExceptionDiffblueTest {
  /**
   * Method under test: {@link ProtocolException#ProtocolException(String)}
   */
  @Test
  void testNewProtocolException() {
    // Arrange and Act
    ProtocolException actualProtocolException = new ProtocolException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualProtocolException.getMessage());
    assertNull(actualProtocolException.getCause());
    assertEquals(0, actualProtocolException.getSuppressed().length);
  }

  /**
   * Method under test:
   * {@link ProtocolException#ProtocolException(String, Throwable)}
   */
  @Test
  void testNewProtocolException2() {
    // Arrange
    Throwable cause = new Throwable();

    // Act
    ProtocolException actualProtocolException = new ProtocolException("An error occurred", cause);

    // Assert
    assertEquals("An error occurred", actualProtocolException.getMessage());
    assertEquals(0, actualProtocolException.getSuppressed().length);
    assertSame(cause, actualProtocolException.getCause());
  }

  /**
   * Method under test: {@link ProtocolException#ProtocolException(Throwable)}
   */
  @Test
  void testNewProtocolException3() {
    // Arrange
    Throwable cause = new Throwable();

    // Act
    ProtocolException actualProtocolException = new ProtocolException(cause);

    // Assert
    assertEquals("java.lang.Throwable", actualProtocolException.getMessage());
    assertEquals(0, actualProtocolException.getSuppressed().length);
    assertSame(cause, actualProtocolException.getCause());
  }
}
