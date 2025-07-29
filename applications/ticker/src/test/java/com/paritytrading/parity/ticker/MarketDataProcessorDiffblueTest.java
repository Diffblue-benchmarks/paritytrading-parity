package com.paritytrading.parity.ticker;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.net.pmd.PMD;
import com.paritytrading.parity.net.pmd.PMD.OrderAdded;
import com.paritytrading.parity.net.pmd.PMD.OrderCanceled;
import com.paritytrading.parity.net.pmd.PMD.OrderExecuted;
import com.paritytrading.parity.net.pmd.PMD.Version;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class MarketDataProcessorDiffblueTest {
  /**
   * Test {@link MarketDataProcessor#version(Version)}.
   *
   * <ul>
   *   <li>Given {@link Runtime} {@link Runtime#exit(int)} does nothing.
   *   <li>When {@link PMD.Version} (default constructor).
   *   <li>Then calls {@link Instruments#getPricePlaceholder()}.
   * </ul>
   *
   * <p>Method under test: {@link MarketDataProcessor#version(PMD.Version)}
   */
  @Test
  @DisplayName(
      "Test version(Version); given Runtime exit(int) does nothing; when Version (default constructor); then calls getPricePlaceholder()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketDataProcessor.version(PMD.Version)"})
  void testVersion_givenRuntimeExitDoesNothing_whenVersion_thenCallsGetPricePlaceholder() {
    try (MockedStatic<Runtime> mockRuntime = mockStatic(Runtime.class)) {

      // Arrange
      Runtime runtime = mock(Runtime.class);
      doNothing().when(runtime).exit(anyInt());
      mockRuntime.when(Runtime::getRuntime).thenReturn(runtime);
      Instruments instruments = mock(Instruments.class);
      when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
      when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
      when(instruments.getPriceWidth()).thenReturn(1);
      when(instruments.getSizeWidth()).thenReturn(1);
      Market market = new Market(new DisplayFormat(instruments));
      Instruments instruments2 = mock(Instruments.class);
      when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
      when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
      when(instruments2.getPriceWidth()).thenReturn(1);
      when(instruments2.getSizeWidth()).thenReturn(1);
      MarketDataProcessor marketDataProcessor =
          new MarketDataProcessor(market, new DisplayFormat(instruments2));

      // Act
      marketDataProcessor.version(new Version());

      // Assert
      verify(instruments).getPricePlaceholder();
      verify(instruments2).getPricePlaceholder();
      verify(instruments).getPriceWidth();
      verify(instruments2).getPriceWidth();
      verify(instruments).getSizePlaceholder();
      verify(instruments2).getSizePlaceholder();
      verify(instruments).getSizeWidth();
      verify(instruments2).getSizeWidth();
      verify(runtime).exit(eq(1));
      mockRuntime.verify(Runtime::getRuntime);
    }
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
    Market market = new Market(new DisplayFormat(instruments));
    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    MarketDataProcessor marketDataProcessor =
        new MarketDataProcessor(market, new DisplayFormat(instruments2));

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
    Market market = new Market(new DisplayFormat(instruments));
    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    MarketDataProcessor marketDataProcessor =
        new MarketDataProcessor(market, new DisplayFormat(instruments2));

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
    Market market = new Market(new DisplayFormat(instruments));
    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    MarketDataProcessor marketDataProcessor =
        new MarketDataProcessor(market, new DisplayFormat(instruments2));

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
