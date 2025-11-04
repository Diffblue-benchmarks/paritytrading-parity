package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class POEExceptionDiffblueTest {
  /**
   * Method under test: {@link POEException#POEException(String)}
   */
  @Test
  void testNewPOEException() {
    // Arrange and Act
    POEException actualPoeException = new POEException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualPoeException.getMessage());
    assertNull(actualPoeException.getCause());
    assertEquals(0, actualPoeException.getSuppressed().length);
  }
}
