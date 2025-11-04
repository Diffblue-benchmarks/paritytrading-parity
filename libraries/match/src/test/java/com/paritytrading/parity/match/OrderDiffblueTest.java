package com.paritytrading.parity.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {
  /**
   * Methods under test:
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

    // Assert that nothing has changed
    assertEquals(1L, actualId);
    assertEquals(1L, actualNumber);
    assertEquals(1L, actualPrice);
    assertEquals(3L, actualRemainingQuantity);
    assertEquals(Side.BUY, order.getSide());
  }

  /**
   * Method under test: {@link Order#Order(long, long, Side, long, long)}
   */
  @Test
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
}
