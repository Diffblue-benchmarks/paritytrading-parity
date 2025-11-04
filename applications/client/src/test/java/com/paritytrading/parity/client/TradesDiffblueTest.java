package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;

class TradesDiffblueTest {
  /**
   * Method under test: {@link Trades#collect(Events)}
   */
  @Test
  void testCollect() {
    // Arrange and Act
    List<Trade> actualCollectResult = Trades.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }
}
