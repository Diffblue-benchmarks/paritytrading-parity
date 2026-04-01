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

class TradesDiffblueTest {
  /**
   * Test {@link Trades#collect(Events)}.
   *
   * <ul>
   *   <li>When {@link Events} (default constructor).
   *   <li>Then return Empty.
   * </ul>
   *
   * <p>Method under test: {@link Trades#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when Events (default constructor); then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Trades.collect(Events)"})
  void testCollect_whenEvents_thenReturnEmpty() {
    // Arrange and Act
    List<Trade> actualCollectResult = Trades.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }

  /**
   * Test {@link Trades#collect(Events)} via {@code visit(Event.OrderAccepted)}.
   *
   * <ul>
   *   <li>When Events has an OrderAccepted event.
   *   <li>Then return Empty (no executions).
   * </ul>
   */
  @Test
  @DisplayName("Test collect(Events); when Events has OrderAccepted; then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Trades.collect(Events)"})
  void testCollect_whenEventsHasOrderAccepted_thenReturnEmpty() {
    // Arrange
    Events events = new Events();
    events.orderAccepted(new POE.OrderAccepted());

    // Act
    List<Trade> actualCollectResult = Trades.collect(events);

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }

  /**
   * Test {@link Trades#collect(Events)} via {@code visit(Event.OrderExecuted)}.
   *
   * <ul>
   *   <li>When Events has an OrderExecuted event but no matching OrderAccepted.
   *   <li>Then return Empty.
   * </ul>
   */
  @Test
  @DisplayName("Test collect(Events); when Events has OrderExecuted with no matching order; then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Trades.collect(Events)"})
  void testCollect_whenEventsHasOrderExecutedWithNoMatchingOrder_thenReturnEmpty() {
    // Arrange
    Events events = new Events();
    events.orderExecuted(new POE.OrderExecuted());

    // Act
    List<Trade> actualCollectResult = Trades.collect(events);

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }

  /**
   * Test {@link Trades#collect(Events)} via {@code visit(Event.OrderExecuted)}.
   *
   * <ul>
   *   <li>When Events has a matching OrderAccepted and OrderExecuted.
   *   <li>Then return one Trade.
   * </ul>
   */
  @Test
  @DisplayName("Test collect(Events); when Events has matching OrderAccepted and OrderExecuted; then return one Trade")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Trades.collect(Events)"})
  void testCollect_whenEventsHasMatchingOrderAcceptedAndOrderExecuted_thenReturnOneTrade() {
    // Arrange
    Events events = new Events();
    events.orderAccepted(new POE.OrderAccepted());
    events.orderExecuted(new POE.OrderExecuted());

    // Act
    List<Trade> actualCollectResult = Trades.collect(events);

    // Assert
    assertEquals(1, actualCollectResult.size());
  }
}
