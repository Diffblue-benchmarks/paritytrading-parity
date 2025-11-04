package com.paritytrading.parity.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradeDiffblueTest {
  /**
   * Test new {@link Trade} (default constructor).
   * <p>
   * Method under test: default or parameterless constructor of {@link Trade}
   */
  @Test
  @DisplayName("Test new Trade (default constructor)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Trade.<init>()"})
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
