package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POE;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrdersDiffblueTest {
  /**
   * Test {@link Orders#collect(Events)}.
   *
   * <ul>
   *   <li>When {@link Events} (default constructor).
   *   <li>Then return Empty.
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when Events (default constructor); then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenEvents_thenReturnEmpty() {
    // Arrange and Act
    List<Order> actualCollectResult = Orders.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }

  /**
   * Test {@link Orders#collect(Events)} with visit(OrderAccepted).
   *
   * <ul>
   *   <li>When Events has one OrderAccepted.
   *   <li>Then return list with one Order.
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when OrderAccepted added; then return list with one Order")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenOrderAccepted_thenReturnOneOrder() {
    // Arrange
    Events events = new Events();
    POE.OrderAccepted accepted = new POE.OrderAccepted();
    accepted.quantity = 100L;
    events.orderAccepted(accepted);

    // Act
    List<Order> result = Orders.collect(events);

    // Assert
    assertEquals(1, result.size());
  }

  /**
   * Test {@link Orders#collect(Events)} with visit(OrderExecuted) when order not found.
   *
   * <ul>
   *   <li>When OrderExecuted event has no matching OrderAccepted.
   *   <li>Then return Empty.
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when OrderExecuted has no matching order; then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenOrderExecuted_givenNoMatchingOrder_thenReturnEmpty() {
    // Arrange
    Events events = new Events();
    POE.OrderExecuted executed = new POE.OrderExecuted();
    executed.quantity = 50L;
    events.orderExecuted(executed);

    // Act
    List<Order> result = Orders.collect(events);

    // Assert
    assertTrue(result.isEmpty());
  }

  /**
   * Test {@link Orders#collect(Events)} with visit(OrderExecuted) when order is partially filled.
   *
   * <ul>
   *   <li>When OrderExecuted quantity is less than accepted quantity.
   *   <li>Then order remains in result.
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when OrderExecuted partial fill; then Order remains in result")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenOrderExecuted_givenPartialFill_thenOrderRemains() {
    // Arrange
    Events events = new Events();
    POE.OrderAccepted accepted = new POE.OrderAccepted();
    accepted.quantity = 100L;
    events.orderAccepted(accepted);

    POE.OrderExecuted executed = new POE.OrderExecuted();
    executed.quantity = 50L;
    events.orderExecuted(executed);

    // Act
    List<Order> result = Orders.collect(events);

    // Assert
    assertEquals(1, result.size());
  }

  /**
   * Test {@link Orders#collect(Events)} with visit(OrderExecuted) when order is fully filled.
   *
   * <ul>
   *   <li>When OrderExecuted quantity equals accepted quantity.
   *   <li>Then return Empty (order removed).
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when OrderExecuted full fill; then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenOrderExecuted_givenFullFill_thenReturnEmpty() {
    // Arrange
    Events events = new Events();
    POE.OrderAccepted accepted = new POE.OrderAccepted();
    accepted.quantity = 100L;
    events.orderAccepted(accepted);

    POE.OrderExecuted executed = new POE.OrderExecuted();
    executed.quantity = 100L;
    events.orderExecuted(executed);

    // Act
    List<Order> result = Orders.collect(events);

    // Assert
    assertTrue(result.isEmpty());
  }

  /**
   * Test {@link Orders#collect(Events)} with visit(OrderCanceled) when order not found.
   *
   * <ul>
   *   <li>When OrderCanceled event has no matching OrderAccepted.
   *   <li>Then return Empty.
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when OrderCanceled has no matching order; then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenOrderCanceled_givenNoMatchingOrder_thenReturnEmpty() {
    // Arrange
    Events events = new Events();
    POE.OrderCanceled canceled = new POE.OrderCanceled();
    canceled.canceledQuantity = 50L;
    events.orderCanceled(canceled);

    // Act
    List<Order> result = Orders.collect(events);

    // Assert
    assertTrue(result.isEmpty());
  }

  /**
   * Test {@link Orders#collect(Events)} with visit(OrderCanceled) when order is partially canceled.
   *
   * <ul>
   *   <li>When OrderCanceled quantity is less than accepted quantity.
   *   <li>Then order remains in result.
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when OrderCanceled partial cancel; then Order remains in result")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenOrderCanceled_givenPartialCancel_thenOrderRemains() {
    // Arrange
    Events events = new Events();
    POE.OrderAccepted accepted = new POE.OrderAccepted();
    accepted.quantity = 100L;
    events.orderAccepted(accepted);

    POE.OrderCanceled canceled = new POE.OrderCanceled();
    canceled.canceledQuantity = 50L;
    events.orderCanceled(canceled);

    // Act
    List<Order> result = Orders.collect(events);

    // Assert
    assertEquals(1, result.size());
  }

  /**
   * Test {@link Orders#collect(Events)} with visit(OrderCanceled) when order is fully canceled.
   *
   * <ul>
   *   <li>When OrderCanceled quantity equals accepted quantity.
   *   <li>Then return Empty (order removed).
   * </ul>
   *
   * <p>Method under test: {@link Orders#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when OrderCanceled full cancel; then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Orders.collect(Events)"})
  void testCollect_whenOrderCanceled_givenFullCancel_thenReturnEmpty() {
    // Arrange
    Events events = new Events();
    POE.OrderAccepted accepted = new POE.OrderAccepted();
    accepted.quantity = 100L;
    events.orderAccepted(accepted);

    POE.OrderCanceled canceled = new POE.OrderCanceled();
    canceled.canceledQuantity = 100L;
    events.orderCanceled(canceled);

    // Act
    List<Order> result = Orders.collect(events);

    // Assert
    assertTrue(result.isEmpty());
  }
}
