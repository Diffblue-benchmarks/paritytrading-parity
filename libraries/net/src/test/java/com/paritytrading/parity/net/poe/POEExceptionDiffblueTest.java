package com.paritytrading.parity.net.poe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class POEExceptionDiffblueTest {
  /**
   * Test {@link POEException#POEException(String)}.
   * <p>
   * Method under test: {@link POEException#POEException(String)}
   */
  @Test
  @DisplayName("Test new POEException(String)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void POEException.<init>(String)"})
  void testNewPOEException() {
    // Arrange and Act
    POEException actualPoeException = new POEException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualPoeException.getMessage());
    assertNull(actualPoeException.getCause());
    assertEquals(0, actualPoeException.getSuppressed().length);
  }
}
