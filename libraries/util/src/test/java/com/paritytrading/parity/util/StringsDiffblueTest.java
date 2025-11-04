package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class StringsDiffblueTest {
  /**
   * Method under test: {@link Strings#repeat(char, int)}
   */
  @Test
  void testRepeat() {
    // Arrange, Act and Assert
    assertEquals("AAA", Strings.repeat('A', 3));
  }
}
