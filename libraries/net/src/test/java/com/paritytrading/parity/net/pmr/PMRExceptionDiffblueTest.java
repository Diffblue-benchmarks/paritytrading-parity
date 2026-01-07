package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class PMRExceptionDiffblueTest {
  /**
   * Test {@link PMRException#PMRException(String)}.
   *
   * <p>Method under test: {@link PMRException#PMRException(String)}
   */
  @Test
  @DisplayName("Test new PMRException(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRException.<init>(String)"})
  void testNewPMRException() {
    // Arrange and Act
    PMRException actualPmrException = new PMRException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualPmrException.getMessage());
    assertNull(actualPmrException.getCause());
    assertEquals(0, actualPmrException.getSuppressed().length);
  }
}
