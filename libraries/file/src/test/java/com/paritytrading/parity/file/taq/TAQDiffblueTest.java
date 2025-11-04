package com.paritytrading.parity.file.taq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.file.taq.TAQ.Quote;
import com.paritytrading.parity.file.taq.TAQ.Trade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TAQDiffblueTest {
  /**
   * Test Quote new {@link Quote} (default constructor).
   * <p>
   * Method under test: default or parameterless constructor of {@link Quote}
   */
  @Test
  @DisplayName("Test Quote new Quote (default constructor)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Quote.<init>()"})
  void testQuoteNewQuote() {
    // Arrange and Act
    Quote actualQuote = new Quote();

    // Assert
    assertNull(actualQuote.date);
    assertNull(actualQuote.instrument);
    assertEquals(0.0d, actualQuote.askPrice);
    assertEquals(0.0d, actualQuote.askSize);
    assertEquals(0.0d, actualQuote.bidPrice);
    assertEquals(0.0d, actualQuote.bidSize);
    assertEquals(0L, actualQuote.timestampMillis);
  }

  /**
   * Test Trade new {@link Trade} (default constructor).
   * <p>
   * Method under test: default or parameterless constructor of {@link Trade}
   */
  @Test
  @DisplayName("Test Trade new Trade (default constructor)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Trade.<init>()"})
  void testTradeNewTrade() {
    // Arrange and Act
    Trade actualTrade = new Trade();

    // Assert
    assertEquals('\u0000', actualTrade.side);
    assertNull(actualTrade.date);
    assertNull(actualTrade.instrument);
    assertEquals(0.0d, actualTrade.price);
    assertEquals(0.0d, actualTrade.size);
    assertEquals(0L, actualTrade.timestampMillis);
  }
}
