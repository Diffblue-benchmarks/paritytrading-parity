package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TimestampsDiffblueTest {
  /**
   * Method under test: {@link Timestamps#format(long)}
   */
  @Test
  void testFormat() {
    // Arrange, Act and Assert
    assertEquals("00:00:00.010", Timestamps.format(10L));
  }
}
