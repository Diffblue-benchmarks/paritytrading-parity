package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class StringsDiffblueTest {
  /**
   * Test {@link Strings#repeat(char, int)}.
   *
   * <ul>
   *   <li>When three.
   *   <li>Then return {@code AAA}.
   * </ul>
   *
   * <p>Method under test: {@link Strings#repeat(char, int)}
   */
  @Test
  @DisplayName("Test repeat(char, int); when three; then return 'AAA'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String Strings.repeat(char, int)"})
  void testRepeat_whenThree_thenReturnAaa() {
    // Arrange, Act and Assert
    assertEquals("AAA", Strings.repeat('A', 3));
  }
}
