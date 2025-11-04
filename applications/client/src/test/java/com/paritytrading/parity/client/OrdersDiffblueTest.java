package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrdersDiffblueTest {
  /**
   * Method under test: {@link Orders#collect(Events)}
   */
  @Test
  void testCollect() {
    // Arrange and Act
    List<Order> actualCollectResult = Orders.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }
}
