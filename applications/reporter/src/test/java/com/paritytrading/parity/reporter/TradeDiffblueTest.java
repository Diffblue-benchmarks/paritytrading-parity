package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class TradeDiffblueTest {
  /**
   * Method under test: default or parameterless constructor of {@link Trade}
   */
  @Test
  void testNewTrade() {
    // Arrange and Act
    Trade actualTrade = new Trade();

    // Assert
    assertNull(actualTrade.buyer);
    assertNull(actualTrade.instrument);
    assertNull(actualTrade.seller);
    assertNull(actualTrade.timestamp);
    assertEquals(0L, actualTrade.buyOrderNumber);
    assertEquals(0L, actualTrade.matchNumber);
    assertEquals(0L, actualTrade.price);
    assertEquals(0L, actualTrade.quantity);
    assertEquals(0L, actualTrade.sellOrderNumber);
  }
}
