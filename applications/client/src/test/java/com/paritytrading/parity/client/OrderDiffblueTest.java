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

class OrderDiffblueTest {
  /**
   * Method under test: {@link Order#format(Instruments)}
   */
  @Test
  void testFormat() {
    // Arrange
    Order order = new Order(new Event.OrderAccepted(new POE.OrderAccepted()));
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.getPriceFormat()).thenReturn("Price Format");
    when(instrument.getSizeFormat()).thenReturn("Size Format");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);

    // Act
    String actualFormatResult = order.format(instruments);

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
   * Methods under test:
   * <ul>
   *   <li>{@link Order#apply(Event.OrderCanceled)}
   *   <li>{@link Order#apply(Event.OrderExecuted)}
   *   <li>{@link Order#getInstrument()}
   *   <li>{@link Order#getOrderId()}
   *   <li>{@link Order#getQuantity()}
   *   <li>{@link Order#getSide()}
   *   <li>{@link Order#getTimestamp()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange
    Order order = new Order(new Event.OrderAccepted(new POE.OrderAccepted()));

    // Act
    order.apply(new Event.OrderCanceled(new POE.OrderCanceled()));
    order.apply(new Event.OrderExecuted(new POE.OrderExecuted()));
    long actualInstrument = order.getInstrument();
    String actualOrderId = order.getOrderId();
    long actualQuantity = order.getQuantity();
    byte actualSide = order.getSide();

    // Assert that nothing has changed
    assertEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderId);
    assertEquals(0L, actualInstrument);
    assertEquals(0L, actualQuantity);
    assertEquals(0L, order.getTimestamp());
    assertEquals((byte) 0, actualSide);
  }

  /**
   * Method under test: {@link Order#Order(Event.OrderAccepted)}
   */
  @Test
  void testNewOrder() {
    // Arrange and Act
    Order actualOrder = new Order(new Event.OrderAccepted(new POE.OrderAccepted()));

    // Assert
    assertEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrder.getOrderId());
    assertEquals(0L, actualOrder.getInstrument());
    assertEquals(0L, actualOrder.getQuantity());
    assertEquals(0L, actualOrder.getTimestamp());
    assertEquals((byte) 0, actualOrder.getSide());
  }
}
