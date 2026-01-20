package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.client.Event.OrderAccepted;
import com.paritytrading.parity.client.Event.OrderCanceled;
import com.paritytrading.parity.client.Event.OrderExecuted;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {
  /**
   * Test {@link Order#Order(OrderAccepted)}.
   *
   * <p>Method under test: {@link Order#Order(OrderAccepted)}
   */
  @Test
  @DisplayName("Test new Order(OrderAccepted)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Order.<init>(OrderAccepted)"})
  void testNewOrder() {
    // Arrange and Act
    Order actualOrder = new Order(new OrderAccepted(new POE.OrderAccepted()));

    // Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrder.getOrderId());
    assertEquals(0L, actualOrder.getInstrument());
    assertEquals(0L, actualOrder.getQuantity());
    assertEquals(0L, actualOrder.getTimestamp());
    assertEquals((byte) 0, actualOrder.getSide());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link Order#apply(OrderCanceled)}
   *   <li>{@link Order#apply(OrderExecuted)}
   *   <li>{@link Order#getInstrument()}
   *   <li>{@link Order#getOrderId()}
   *   <li>{@link Order#getQuantity()}
   *   <li>{@link Order#getSide()}
   *   <li>{@link Order#getTimestamp()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void Order.apply(OrderCanceled)",
    "void Order.apply(OrderExecuted)",
    "long Order.getInstrument()",
    "String Order.getOrderId()",
    "long Order.getQuantity()",
    "byte Order.getSide()",
    "long Order.getTimestamp()"
  })
  void testGettersAndSetters() {
    // Arrange
    Order order = new Order(new OrderAccepted(new POE.OrderAccepted()));

    // Act
    order.apply(new OrderCanceled(new POE.OrderCanceled()));
    order.apply(new OrderExecuted(new POE.OrderExecuted()));
    long actualInstrument = order.getInstrument();
    String actualOrderId = order.getOrderId();
    long actualQuantity = order.getQuantity();
    byte actualSide = order.getSide();

    // Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderId);
    assertEquals(0L, actualInstrument);
    assertEquals(0L, actualQuantity);
    assertEquals(0L, order.getTimestamp());
    assertEquals((byte) 0, actualSide);
  }

  /**
   * Test {@link Order#format(Instruments)}.
   *
   * <ul>
   *   <li>Given {@link Instrument} {@link Instrument#getPriceFormat()} return createUsername.
   *   <li>Then return a string.
   * </ul>
   *
   * <p>Method under test: {@link Order#format(Instruments)}
   */
  @Test
  @DisplayName(
      "Test format(Instruments); given Instrument getPriceFormat() return createUsername; then return a string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String Order.format(Instruments)"})
  void testFormat_givenInstrumentGetPriceFormatReturnCreateUsername_thenReturnAString() {
    // Arrange
    Order order = new Order(new OrderAccepted(new POE.OrderAccepted()));

    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.getPriceFormat()).thenReturn(TerminalClientTestFactory.createUsername());
    when(instrument.getSizeFormat()).thenReturn(TerminalClientTestFactory.createUsername());

    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);

    // Act
    String actualFormatResult = order.format(instruments);

    // Assert
    verify(instrument).getPriceFactor();
    verify(instrument).getPriceFormat();
    verify(instrument).getSizeFactor();
    verify(instrument).getSizeFormat();
    verify(instruments).get(0L);
    assertEquals(
        "00:00:00.000 \u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 \u0000 \u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 testuser testuser",
        actualFormatResult);
  }
}
