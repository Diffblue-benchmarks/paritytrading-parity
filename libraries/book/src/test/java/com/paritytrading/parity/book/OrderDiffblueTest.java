package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {
  /**
   * Methods under test:
   * <ul>
   *   <li>{@link Order#reduce(long)}
   *   <li>{@link Order#setRemainingQuantity(long)}
   *   <li>{@link Order#getOrderBook()}
   *   <li>{@link Order#getPrice()}
   *   <li>{@link Order#getRemainingQuantity()}
   *   <li>{@link Order#getSide()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange
    OrderBook book = new OrderBook(1L);
    Order order = new Order(book, Side.BUY, 1L, 3L);

    // Act
    order.reduce(1L);
    order.setRemainingQuantity(1L);
    OrderBook actualOrderBook = order.getOrderBook();
    long actualPrice = order.getPrice();
    long actualRemainingQuantity = order.getRemainingQuantity();

    // Assert that nothing has changed
    assertEquals(1L, actualPrice);
    assertEquals(1L, actualRemainingQuantity);
    assertEquals(Side.BUY, order.getSide());
    assertSame(book, actualOrderBook);
  }

  /**
   * Method under test: {@link Order#Order(OrderBook, Side, long, long)}
   */
  @Test
  void testNewOrder() {
    // Arrange
    OrderBook book = new OrderBook(1L);

    // Act
    Order actualOrder = new Order(book, Side.BUY, 1L, 3L);

    // Assert
    assertEquals(1L, actualOrder.getPrice());
    assertEquals(3L, actualOrder.getRemainingQuantity());
    assertEquals(Side.BUY, actualOrder.getSide());
    assertSame(book, actualOrder.getOrderBook());
  }
}
