package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;

class ErrorsDiffblueTest {
  /**
   * Method under test: {@link Errors#collect(Events)}
   */
  @Test
  void testCollect() {
    // Arrange and Act
    List<Error> actualCollectResult = Errors.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }
}
