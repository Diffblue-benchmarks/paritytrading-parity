package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class PMRExceptionDiffblueTest {
  /**
   * Method under test: {@link PMRException#PMRException(String)}
   */
  @Test
  void testNewPMRException() {
    // Arrange and Act
    PMRException actualPmrException = new PMRException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualPmrException.getMessage());
    assertNull(actualPmrException.getCause());
    assertEquals(0, actualPmrException.getSuppressed().length);
  }
}
