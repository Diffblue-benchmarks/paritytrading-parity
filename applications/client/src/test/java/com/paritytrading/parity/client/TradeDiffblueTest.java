package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
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

    // Act and Assert
    assertEquals(0L, new Trade(order, new OrderExecuted(new POE.OrderExecuted())).getTimestamp());
  }

  /**
   * Test {@link Trade#getTimestamp()}.
   *
   * <p>Method under test: {@link Trade#getTimestamp()}
   */
  @Test
  @DisplayName("Test getTimestamp()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Trade.getTimestamp()"})
  void testGetTimestamp() {
    // Arrange
    Order order = new Order(new Event.OrderAccepted(new OrderAccepted()));

    // Act and Assert
    assertEquals(0L, new Trade(order, new OrderExecuted(new POE.OrderExecuted())).getTimestamp());
  }

  /**
   * Test {@link Trade#format(Instruments)}.
   *
   * <ul>
   *   <li>Given {@link Event.OrderAccepted#OrderAccepted(OrderAccepted)} with message is {@link
   *       POE.OrderAccepted} (default constructor).
   *   <li>Then return a string.
   * </ul>
   *
   * <p>Method under test: {@link Trade#format(Instruments)}
   */
  @Test
  @DisplayName(
      "Test format(Instruments); given OrderAccepted(OrderAccepted) with message is OrderAccepted (default constructor); then return a string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String Trade.format(Instruments)"})
  void testFormat_givenOrderAcceptedWithMessageIsOrderAccepted_thenReturnAString() {
    // Arrange
    Order order = new Order(new Event.OrderAccepted(new OrderAccepted()));
    Trade trade = new Trade(order, new OrderExecuted(new POE.OrderExecuted()));
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.getPriceFormat()).thenReturn("Price Format");
    when(instrument.getSizeFormat()).thenReturn("Size Format");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);

    // Act
    String actualFormatResult = trade.format(instruments);

    // Assert
    verify(instrument).getPriceFactor();
    verify(instrument).getPriceFormat();
    verify(instrument).getSizeFactor();
    verify(instrument).getSizeFormat();
    verify(instruments).get(eq(0L));
    assertEquals(
        "00:00:00.000 \u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 \u0000 \u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 Size Format Price Format",
        actualFormatResult);
  }
}
