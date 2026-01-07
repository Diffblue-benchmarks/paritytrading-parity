package com.paritytrading.parity.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {
  /**
   * Test {@link Order#Order(long, long, Side, long, long)}.
   *
   * <p>Method under test: {@link Order#Order(long, long, Side, long, long)}
   */
  @Test
  @DisplayName("Test new Order(long, long, Side, long, long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Order.<init>(long, long, Side, long, long)"})
  void testNewOrder() {
    // Arrange and Act
    Order actualOrder = new Order(1L, 1L, Side.BUY, 1L, 3L);

    // Assert
    assertEquals(1L, actualOrder.getId());
    assertEquals(1L, actualOrder.getNumber());
    assertEquals(1L, actualOrder.getPrice());
    assertEquals(3L, actualOrder.getRemainingQuantity());
    assertEquals(Side.BUY, actualOrder.getSide());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link Order#reduce(long)}
   *   <li>{@link Order#resize(long)}
   *   <li>{@link Order#getId()}
   *   <li>{@link Order#getNumber()}
   *   <li>{@link Order#getPrice()}
   *   <li>{@link Order#getRemainingQuantity()}
   *   <li>{@link Order#getSide()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "long Order.getId()",
    "long Order.getNumber()",
    "long Order.getPrice()",
    "long Order.getRemainingQuantity()",
    "Side Order.getSide()",
    "void Order.reduce(long)",
    "void Order.resize(long)"
  })
  void testGettersAndSetters() {
    // Arrange
    Order order = new Order(1L, 1L, Side.BUY, 1L, 3L);

    // Act
    order.reduce(1L);
    order.resize(3L);
    long actualId = order.getId();
    long actualNumber = order.getNumber();
    long actualPrice = order.getPrice();
    long actualRemainingQuantity = order.getRemainingQuantity();

    // Assert
    assertEquals(1L, actualId);
    assertEquals(1L, actualNumber);
    assertEquals(1L, actualPrice);
    assertEquals(3L, actualRemainingQuantity);
    assertEquals(Side.BUY, order.getSide());
  }
}
