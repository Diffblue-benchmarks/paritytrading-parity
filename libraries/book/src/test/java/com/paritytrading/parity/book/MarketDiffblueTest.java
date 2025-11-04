package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class MarketDiffblueTest {
  /**
   * Method under test: {@link Market#open(long)}
   */
  @Test
  void testOpen() {
    // Arrange and Act
    OrderBook actualOpenResult = (new Market(new MarketEvents())).open(1L);

    // Assert
    assertEquals(0L, actualOpenResult.getBestAskPrice());
    assertEquals(0L, actualOpenResult.getBestBidPrice());
    assertEquals(1L, actualOpenResult.getInstrument());
    assertTrue(actualOpenResult.getAskPrices().isEmpty());
    assertTrue(actualOpenResult.getBidPrices().isEmpty());
  }

  /**
   * Method under test: {@link Market#find(long)}
   */
  @Test
  void testFind() {
    // Arrange, Act and Assert
    assertNull((new Market(new MarketEvents())).find(1L));
    assertNull((new Market(new MarketEvents())).find(0L));
  }

  /**
   * Method under test: {@link Market#execute(long, long)}
   */
  @Test
  void testExecute() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(1L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(2L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(3L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(4L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(0L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(1L, 5L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(2L, 5L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(3L, 5L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(4L, 5L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).execute(0L, 5L, 5L));
  }

  /**
   * Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  void testCancel() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).cancel(1L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).cancel(2L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).cancel(3L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).cancel(4L, 5L));
    assertEquals(0L, (new Market(new MarketEvents())).cancel(0L, 5L));
  }

  /**
   * Method under test: {@link Market#Market(MarketListener)}
   */
  @Test
  void testNewMarket() {
    // Arrange and Act
    Market actualMarket = new Market(new MarketEvents());

    // Assert
    assertNull(actualMarket.find(1L));
    OrderBook openResult = actualMarket.open(1L);
    assertEquals(0L, openResult.getBestAskPrice());
    assertEquals(0L, openResult.getBestBidPrice());
    assertEquals(1L, openResult.getInstrument());
    assertTrue(openResult.getAskPrices().isEmpty());
    assertTrue(openResult.getBidPrices().isEmpty());
  }
}
