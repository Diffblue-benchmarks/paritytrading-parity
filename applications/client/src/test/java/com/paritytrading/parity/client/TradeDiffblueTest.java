package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

class TradeDiffblueTest {
  /**
   * Method under test: {@link Trade#getTimestamp()}
   */
  @Test
  void testGetTimestamp() {
    // Arrange
    Order order = new Order(new Event.OrderAccepted(new POE.OrderAccepted()));

    // Act and Assert
    assertEquals(0L, (new Trade(order, new Event.OrderExecuted(new POE.OrderExecuted()))).getTimestamp());
  }

  /**
   * Method under test: {@link Trade#format(Instruments)}
   */
  @Test
  void testFormat() {
    // Arrange
    Order order = new Order(new Event.OrderAccepted(new POE.OrderAccepted()));
    Trade trade = new Trade(order, new Event.OrderExecuted(new POE.OrderExecuted()));
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

  /**
   * Method under test: {@link Trade#Trade(Order, Event.OrderExecuted)}
   */
  @Test
  void testNewTrade() {
    // Arrange
    Order order = new Order(new Event.OrderAccepted(new POE.OrderAccepted()));

    // Act and Assert
    assertEquals(0L, (new Trade(order, new Event.OrderExecuted(new POE.OrderExecuted()))).getTimestamp());
  }
}
