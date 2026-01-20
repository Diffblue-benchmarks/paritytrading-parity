package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.client.Event.OrderRejected;
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
   * Test {@link Errors#getErrors()}.
   *
   * <p>Method under test: {@link Errors#getErrors()}
   */
  @Test
  @DisplayName("Test getErrors()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"List Errors.getErrors()"})
  void testGetErrors() {
    // Arrange, Act and Assert
    assertTrue(ErrorsTestFactory.createErrors().getErrors().isEmpty());
  }

  /**
   * Test {@link Errors#visit(OrderRejected)} with {@code OrderRejected}.
   *
   * <ul>
   *   <li>Then createErrors Errors size is one.
   * </ul>
   *
   * <p>Method under test: {@link Errors#visit(OrderRejected)}
   */
  @Test
  @DisplayName(
      "Test visit(OrderRejected) with 'OrderRejected'; then createErrors Errors size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Errors.visit(OrderRejected)"})
  void testVisitWithOrderRejected_thenCreateErrorsErrorsSizeIsOne() {
    // Arrange
    Errors createErrorsResult = ErrorsTestFactory.createErrors();

    // Act
    createErrorsResult.visit(new OrderRejected(new POE.OrderRejected()));

    // Assert
    assertEquals(1, createErrorsResult.getErrors().size());
  }
}
