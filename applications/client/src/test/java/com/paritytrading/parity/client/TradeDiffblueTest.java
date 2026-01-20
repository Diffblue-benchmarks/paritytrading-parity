package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.client.Event.OrderExecuted;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POE.OrderAccepted;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradeDiffblueTest {
  /**
   * Test {@link Trade#Trade(Order, OrderExecuted)}.
   *
   * <ul>
   *   <li>Then return Timestamp is zero.
   * </ul>
   *
   * <p>Method under test: {@link Trade#Trade(Order, OrderExecuted)}
   */
  @Test
  @DisplayName("Test new Trade(Order, OrderExecuted); then return Timestamp is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Trade.<init>(Order, OrderExecuted)"})
  void testNewTrade_thenReturnTimestampIsZero() {
    // Arrange
    Order order = new Order(new Event.OrderAccepted(new OrderAccepted()));

    // Act
    Trade actualTrade = new Trade(order, new OrderExecuted(new POE.OrderExecuted()));

    // Assert
    assertEquals(0L, actualTrade.getTimestamp());
  }

  /**
   * Test {@link Trade#format(Instruments)}.
   *
   * <p>Method under test: {@link Trade#format(Instruments)}
   */
  @Test
  @DisplayName("Test format(Instruments)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String Trade.format(Instruments)"})
  void testFormat() {
    // Arrange
    Trade createTradeWithTimestampResult =
        TradeTestFactory.createTradeWithTimestamp(
            TerminalClientTestFactory.createUsername(), (byte) 'A', 1L, 1L, 1L, 10L);

    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.getPriceFormat()).thenReturn(TerminalClientTestFactory.createUsername());
    when(instrument.getSizeFormat()).thenReturn(TerminalClientTestFactory.createUsername());

    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);

    // Act
    String actualFormatResult = createTradeWithTimestampResult.format(instruments);

    // Assert
    verify(instrument).getPriceFactor();
    verify(instrument).getPriceFormat();
    verify(instrument).getSizeFactor();
    verify(instrument).getSizeFormat();
    verify(instruments).get(1L);
    assertEquals(
        "00:00:00.000 testuser         A \u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001 testuser testuser",
        actualFormatResult);
  }
}
