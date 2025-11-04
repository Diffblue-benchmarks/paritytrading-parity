package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class PMDExceptionDiffblueTest {
  /**
   * Method under test: {@link PMDException#PMDException(String)}
   */
  @Test
  void testNewPMDException() {
    // Arrange and Act
    PMDException actualPmdException = new PMDException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualPmdException.getMessage());
    assertNull(actualPmdException.getCause());
    assertEquals(0, actualPmdException.getSuppressed().length);
  }
}
