package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.net.pmd.PMD;
import com.paritytrading.parity.net.pmd.PMD.OrderAdded;
import com.paritytrading.parity.net.pmd.PMD.OrderCanceled;
import com.paritytrading.parity.net.pmd.PMD.OrderExecuted;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MarketDataProcessorDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link MarketDataProcessor#MarketDataProcessor(Market, MarketDataListener)}
   *   <li>{@link MarketDataProcessor#getListener()}
   *   <li>{@link MarketDataProcessor#getMarket()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MarketDataProcessor.<init>(Market, MarketDataListener)",
    "MarketDataListener MarketDataProcessor.getListener()",
    "Market MarketDataProcessor.getMarket()"
  })
  void testGettersAndSetters() {
    // Arrange
    Market market = new Market(new DisplayFormat(null));
    DisplayFormat listener = new DisplayFormat(null);

    // Act
    MarketDataProcessor actualMarketDataProcessor = new MarketDataProcessor(market, listener);
    MarketDataListener actualListener = actualMarketDataProcessor.getListener();

    // Assert
    assertTrue(actualListener instanceof DisplayFormat);
    assertSame(market, actualMarketDataProcessor.getMarket());
    assertSame(listener, actualListener);
  }

  /**
   * Test {@link MarketDataProcessor#orderAdded(OrderAdded)}.
   *
   * <ul>
   *   <li>Then calls {@link Instruments#getPricePlaceholder()}.
   * </ul>
   *
   * <p>Method under test: {@link MarketDataProcessor#orderAdded(PMD.OrderAdded)}
   */
  @Test
  @DisplayName("Test orderAdded(OrderAdded); then calls getPricePlaceholder()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketDataProcessor.orderAdded(PMD.OrderAdded)"})
  void testOrderAdded_thenCallsGetPricePlaceholder() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    DisplayFormat listener = new DisplayFormat(instruments);
    Market market = new Market(listener);

    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    DisplayFormat listener2 = new DisplayFormat(instruments2);

    MarketDataProcessor marketDataProcessor = new MarketDataProcessor(market, listener2);

    // Act
    marketDataProcessor.orderAdded(new OrderAdded());

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments2).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments2).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments2).getSizePlaceholder();
    verify(instruments).getSizeWidth();
    verify(instruments2).getSizeWidth();
  }

  /**
   * Test {@link MarketDataProcessor#orderExecuted(OrderExecuted)}.
   *
   * <ul>
   *   <li>Then calls {@link Instruments#getPricePlaceholder()}.
   * </ul>
   *
   * <p>Method under test: {@link MarketDataProcessor#orderExecuted(PMD.OrderExecuted)}
   */
  @Test
  @DisplayName("Test orderExecuted(OrderExecuted); then calls getPricePlaceholder()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketDataProcessor.orderExecuted(PMD.OrderExecuted)"})
  void testOrderExecuted_thenCallsGetPricePlaceholder() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    DisplayFormat listener = new DisplayFormat(instruments);
    Market market = new Market(listener);

    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    DisplayFormat listener2 = new DisplayFormat(instruments2);

    MarketDataProcessor marketDataProcessor = new MarketDataProcessor(market, listener2);

    // Act
    marketDataProcessor.orderExecuted(new OrderExecuted());

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments2).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments2).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments2).getSizePlaceholder();
    verify(instruments).getSizeWidth();
    verify(instruments2).getSizeWidth();
  }

  /**
   * Test {@link MarketDataProcessor#orderCanceled(OrderCanceled)}.
   *
   * <ul>
   *   <li>Then calls {@link Instruments#getPricePlaceholder()}.
   * </ul>
   *
   * <p>Method under test: {@link MarketDataProcessor#orderCanceled(PMD.OrderCanceled)}
   */
  @Test
  @DisplayName("Test orderCanceled(OrderCanceled); then calls getPricePlaceholder()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketDataProcessor.orderCanceled(PMD.OrderCanceled)"})
  void testOrderCanceled_thenCallsGetPricePlaceholder() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    DisplayFormat listener = new DisplayFormat(instruments);
    Market market = new Market(listener);

    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    DisplayFormat listener2 = new DisplayFormat(instruments2);

    MarketDataProcessor marketDataProcessor = new MarketDataProcessor(market, listener2);

    // Act
    marketDataProcessor.orderCanceled(new OrderCanceled());

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments2).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments2).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments2).getSizePlaceholder();
    verify(instruments).getSizeWidth();
    verify(instruments2).getSizeWidth();
  }
}
