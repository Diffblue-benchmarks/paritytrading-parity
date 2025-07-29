package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TimestampsDiffblueTest {
  /**
   * Test {@link Timestamps#format(long)}.
   *
   * <ul>
   *   <li>When ten.
   *   <li>Then return {@code 00:00:00.010}.
   * </ul>
   *
   * <p>Method under test: {@link Timestamps#format(long)}
   */
  @Test
  @DisplayName("Test format(long); when ten; then return '00:00:00.010'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String Timestamps.format(long)"})
  void testFormat_whenTen_thenReturn000000010() {
    // Arrange, Act and Assert
    assertEquals("00:00:00.010", Timestamps.format(10L));
  }
}
