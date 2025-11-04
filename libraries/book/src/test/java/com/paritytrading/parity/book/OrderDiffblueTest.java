package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {
  /**
   * Test {@link Order#Order(OrderBook, Side, long, long)}.
   * <p>
   * Method under test: {@link Order#Order(OrderBook, Side, long, long)}
   */
  @Test
  @DisplayName("Test new Order(OrderBook, Side, long, long)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Order.<init>(OrderBook, Side, long, long)"})
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

  /**
   * Test getters and setters.
   * <p>
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
  @DisplayName("Test getters and setters")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"OrderBook Order.getOrderBook()", "long Order.getPrice()", "long Order.getRemainingQuantity()",
      "Side Order.getSide()", "void Order.reduce(long)", "void Order.setRemainingQuantity(long)"})
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

    // Assert
    assertEquals(1L, actualPrice);
    assertEquals(1L, actualRemainingQuantity);
    assertEquals(Side.BUY, order.getSide());
    assertSame(book, actualOrderBook);
  }
}
