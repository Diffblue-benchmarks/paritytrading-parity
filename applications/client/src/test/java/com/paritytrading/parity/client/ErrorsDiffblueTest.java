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

class ErrorsDiffblueTest {
  /**
   * Test {@link Errors#collect(Events)}.
   *
   * <ul>
   *   <li>When {@link Events} (default constructor).
   *   <li>Then return Empty.
   * </ul>
   *
   * <p>Method under test: {@link Errors#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when Events (default constructor); then return Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Errors.collect(Events)"})
  void testCollect_whenEvents_thenReturnEmpty() {
    // Arrange and Act
    List<Error> actualCollectResult = Errors.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }

  /**
   * Test {@link Errors#collect(Events)}.
   *
   * <ul>
   *   <li>When {@link Events} has an {@link POE.OrderRejected} event.
   *   <li>Then return list with one element.
   * </ul>
   *
   * <p>Method under test: {@link Errors#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when Events has an OrderRejected event; then return list with one element")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Errors.collect(Events)"})
  void testCollect_whenEventsHasOrderRejected_thenReturnOneElement() {
    // Arrange
    Events events = new Events();
    events.orderRejected(new POE.OrderRejected());

    // Act
    List<Error> actualCollectResult = Errors.collect(events);

    // Assert
    assertEquals(1, actualCollectResult.size());
  }
}
