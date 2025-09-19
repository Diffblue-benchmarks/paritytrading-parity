package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class PMDExceptionDiffblueTest {
  /**
   * Test {@link PMDException#PMDException(String)}.
   *
   * <p>Method under test: {@link PMDException#PMDException(String)}
   */
  @Test
  @DisplayName("Test new PMDException(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDException.<init>(String)"})
  void testNewPMDException() {
    // Arrange and Act
    PMDException actualPmdException = new PMDException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualPmdException.getMessage());
    assertNull(actualPmdException.getCause());
    assertEquals(0, actualPmdException.getSuppressed().length);
  }
}
