package com.paritytrading.parity.reporter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmr.PMR;
import com.paritytrading.parity.net.pmr.PMR.OrderEntered;
import com.paritytrading.parity.net.pmr.PMR.Version;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class TradeProcessorDiffblueTest {
  /**
   * Test {@link TradeProcessor#TradeProcessor(TradeListener)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>Then calls {@link Instruments#getPriceWidth()}.
   * </ul>
   *
   * <p>Method under test: {@link TradeProcessor#TradeProcessor(TradeListener)}
   */
  @Test
  @DisplayName("Test new TradeProcessor(TradeListener); given one; then calls getPriceWidth()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeProcessor.<init>(TradeListener)"})
  void testNewTradeProcessor_givenOne_thenCallsGetPriceWidth() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    new TradeProcessor(new DisplayFormat(instruments));

    // Assert
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }

  /**
   * Test {@link TradeProcessor#version(Version)}.
   *
   * <ul>
   *   <li>Given {@link Instruments} {@link Instruments#getPriceWidth()} return one.
   *   <li>Then calls {@link Instruments#getPriceWidth()}.
   * </ul>
   *
   * <p>Method under test: {@link TradeProcessor#version(PMR.Version)}
   */
  @Test
  @DisplayName(
      "Test version(Version); given Instruments getPriceWidth() return one; then calls getPriceWidth()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeProcessor.version(PMR.Version)"})
  void testVersion_givenInstrumentsGetPriceWidthReturnOne_thenCallsGetPriceWidth() {
    try (MockedStatic<Runtime> mockRuntime = mockStatic(Runtime.class)) {

      // Arrange
      Runtime runtime = mock(Runtime.class);
      doNothing().when(runtime).exit(anyInt());
      mockRuntime.when(Runtime::getRuntime).thenReturn(runtime);
      Instruments instruments = mock(Instruments.class);
      when(instruments.getPriceWidth()).thenReturn(1);
      when(instruments.getSizeWidth()).thenReturn(1);
      TradeProcessor tradeProcessor = new TradeProcessor(new DisplayFormat(instruments));

      // Act
      tradeProcessor.version(new Version());

      // Assert
      verify(instruments).getPriceWidth();
      verify(instruments).getSizeWidth();
      verify(runtime).exit(eq(1));
      mockRuntime.verify(Runtime::getRuntime);
    }
  }

  /**
   * Test {@link TradeProcessor#orderEntered(OrderEntered)}.
   *
   * <ul>
   *   <li>Given {@link Instruments} {@link Instruments#getPriceWidth()} return one.
   *   <li>Then calls {@link Instruments#getPriceWidth()}.
   * </ul>
   *
   * <p>Method under test: {@link TradeProcessor#orderEntered(PMR.OrderEntered)}
   */
  @Test
  @DisplayName(
      "Test orderEntered(OrderEntered); given Instruments getPriceWidth() return one; then calls getPriceWidth()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TradeProcessor.orderEntered(PMR.OrderEntered)"})
  void testOrderEntered_givenInstrumentsGetPriceWidthReturnOne_thenCallsGetPriceWidth() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    TradeProcessor tradeProcessor = new TradeProcessor(new DisplayFormat(instruments));

    // Act
    tradeProcessor.orderEntered(new OrderEntered());

    // Assert
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }
}
