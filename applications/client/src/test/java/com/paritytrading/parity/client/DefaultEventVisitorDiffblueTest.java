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

class DefaultEventVisitorDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link DefaultEventVisitor}
   *   <li>{@link DefaultEventVisitor#visit(OrderAccepted)}
   *   <li>{@link DefaultEventVisitor#visit(OrderCanceled)}
   *   <li>{@link DefaultEventVisitor#visit(OrderExecuted)}
   *   <li>{@link DefaultEventVisitor#visit(OrderRejected)}
   *   <li>{@link DefaultEventVisitor#getVisitCount()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DefaultEventVisitor.<init>()",
    "int DefaultEventVisitor.getVisitCount()",
    "void DefaultEventVisitor.visit(OrderAccepted)",
    "void DefaultEventVisitor.visit(OrderCanceled)",
    "void DefaultEventVisitor.visit(OrderExecuted)",
    "void DefaultEventVisitor.visit(OrderRejected)"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    DefaultEventVisitor actualDefaultEventVisitor = new DefaultEventVisitor();
    actualDefaultEventVisitor.visit(new OrderAccepted(new POE.OrderAccepted()));
    actualDefaultEventVisitor.visit(new OrderCanceled(new POE.OrderCanceled()));
    actualDefaultEventVisitor.visit(new OrderExecuted(new POE.OrderExecuted()));
    actualDefaultEventVisitor.visit(new OrderRejected(new POE.OrderRejected()));

    // Assert
    assertEquals(4, actualDefaultEventVisitor.getVisitCount());
  }
}
