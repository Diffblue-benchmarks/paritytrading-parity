package com.paritytrading.parity.file.taq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class TAQDiffblueTest {
  /**
   * Method under test: default or parameterless constructor of {@link TAQ.Quote}
   */
  @Test
  void testQuoteNewQuote() {
    // Arrange and Act
    TAQ.Quote actualQuote = new TAQ.Quote();

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
   * Method under test: default or parameterless constructor of {@link TAQ.Trade}
   */
  @Test
  void testTradeNewTrade() {
    // Arrange and Act
    TAQ.Trade actualTrade = new TAQ.Trade();

    // Assert
    assertEquals('\u0000', actualTrade.side);
    assertNull(actualTrade.date);
    assertNull(actualTrade.instrument);
    assertEquals(0.0d, actualTrade.price);
    assertEquals(0.0d, actualTrade.size);
    assertEquals(0L, actualTrade.timestampMillis);
  }
}
