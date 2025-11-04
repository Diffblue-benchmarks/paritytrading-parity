package com.paritytrading.parity.reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmr.PMR;
import com.paritytrading.parity.net.pmr.PMR.OrderEntered;
import com.paritytrading.parity.net.pmr.PMR.Version;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradeProcessorDiffblueTest {
  /**
   * Test {@link TradeProcessor#TradeProcessor(TradeListener)}.
   * <ul>
   *   <li>Given one.</li>
   *   <li>Then calls {@link Instruments#getPriceWidth()}.</li>
   * </ul>
   * <p>
   * Method under test: {@link TradeProcessor#TradeProcessor(TradeListener)}
   */
  @Test
  @DisplayName("Test new TradeProcessor(TradeListener); given one; then calls getPriceWidth()")
  @Tag("MaintainedByDiffblue")
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
   * <ul>
   *   <li>Given two.</li>
   *   <li>When {@link PMR.Version} (default constructor) {@link PMR.Version#version} is two.</li>
   *   <li>Then calls {@link Instruments#getPriceWidth()}.</li>
   * </ul>
   * <p>
   * Method under test: {@link TradeProcessor#version(PMR.Version)}
   */
  @Test
  @DisplayName("Test version(Version); given two; when Version (default constructor) version is two; then calls getPriceWidth()")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void TradeProcessor.version(PMR.Version)"})
  void testVersion_givenTwo_whenVersionVersionIsTwo_thenCallsGetPriceWidth() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    TradeProcessor tradeProcessor = new TradeProcessor(new DisplayFormat(instruments));
    Version message = new Version();
    message.version = 2L;

    // Act
    tradeProcessor.version(message);

    // Assert
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }

  /**
   * Test {@link TradeProcessor#orderEntered(OrderEntered)}.
   * <ul>
   *   <li>Given {@link Instruments} {@link Instruments#getPriceWidth()} return one.</li>
   *   <li>Then calls {@link Instruments#getPriceWidth()}.</li>
   * </ul>
   * <p>
   * Method under test: {@link TradeProcessor#orderEntered(PMR.OrderEntered)}
   */
  @Test
  @DisplayName("Test orderEntered(OrderEntered); given Instruments getPriceWidth() return one; then calls getPriceWidth()")
  @Tag("MaintainedByDiffblue")
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
