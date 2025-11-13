package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.client.Event.OrderAccepted;
import com.paritytrading.parity.client.Event.OrderCanceled;
import com.paritytrading.parity.client.Event.OrderExecuted;
import com.paritytrading.parity.client.Event.OrderRejected;
import com.paritytrading.parity.net.poe.POE;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class EventDiffblueTest {
  /**
   * Test OrderAccepted {@link OrderAccepted#OrderAccepted(OrderAccepted)}.
   *
   * <p>Method under test: {@link OrderAccepted#OrderAccepted(POE.OrderAccepted)}
   */
  @Test
  @DisplayName("Test OrderAccepted new OrderAccepted(OrderAccepted)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderAccepted.<init>(POE.OrderAccepted)"})
  void testOrderAcceptedNewOrderAccepted() {
    // Arrange and Act
    OrderAccepted actualOrderAccepted = new OrderAccepted(new POE.OrderAccepted());
    actualOrderAccepted.accept(new DefaultEventVisitor());

    // Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderAccepted.orderId);
    assertEquals(0L, actualOrderAccepted.instrument);
    assertEquals(0L, actualOrderAccepted.orderNumber);
    assertEquals(0L, actualOrderAccepted.price);
    assertEquals(0L, actualOrderAccepted.quantity);
    assertEquals(0L, actualOrderAccepted.timestamp);
    assertEquals((byte) 0, actualOrderAccepted.side);
  }

  /**
   * Test OrderCanceled {@link OrderCanceled#OrderCanceled(OrderCanceled)}.
   *
   * <p>Method under test: {@link OrderCanceled#OrderCanceled(POE.OrderCanceled)}
   */
  @Test
  @DisplayName("Test OrderCanceled new OrderCanceled(OrderCanceled)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderCanceled.<init>(POE.OrderCanceled)"})
  void testOrderCanceledNewOrderCanceled() {
    // Arrange and Act
    OrderCanceled actualOrderCanceled = new OrderCanceled(new POE.OrderCanceled());
    actualOrderCanceled.accept(new DefaultEventVisitor());

    // Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderCanceled.orderId);
    assertEquals(0L, actualOrderCanceled.canceledQuantity);
    assertEquals(0L, actualOrderCanceled.timestamp);
    assertEquals((byte) 0, actualOrderCanceled.reason);
  }

  /**
   * Test OrderExecuted {@link OrderExecuted#OrderExecuted(OrderExecuted)}.
   *
   * <p>Method under test: {@link OrderExecuted#OrderExecuted(POE.OrderExecuted)}
   */
  @Test
  @DisplayName("Test OrderExecuted new OrderExecuted(OrderExecuted)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderExecuted.<init>(POE.OrderExecuted)"})
  void testOrderExecutedNewOrderExecuted() {
    // Arrange and Act
    OrderExecuted actualOrderExecuted = new OrderExecuted(new POE.OrderExecuted());
    actualOrderExecuted.accept(new DefaultEventVisitor());

    // Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderExecuted.orderId);
    assertEquals(0L, actualOrderExecuted.matchNumber);
    assertEquals(0L, actualOrderExecuted.price);
    assertEquals(0L, actualOrderExecuted.quantity);
    assertEquals(0L, actualOrderExecuted.timestamp);
    assertEquals((byte) 0, actualOrderExecuted.liquidityFlag);
  }

  /**
   * Test OrderRejected {@link OrderRejected#OrderRejected(OrderRejected)}.
   *
   * <p>Method under test: {@link OrderRejected#OrderRejected(POE.OrderRejected)}
   */
  @Test
  @DisplayName("Test OrderRejected new OrderRejected(OrderRejected)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderRejected.<init>(POE.OrderRejected)"})
  void testOrderRejectedNewOrderRejected() {
    // Arrange and Act
    OrderRejected actualOrderRejected = new OrderRejected(new POE.OrderRejected());
    actualOrderRejected.accept(new DefaultEventVisitor());

    // Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000",
        actualOrderRejected.orderId);
    assertEquals(0L, actualOrderRejected.timestamp);
    assertEquals((byte) 0, actualOrderRejected.reason);
  }
}
