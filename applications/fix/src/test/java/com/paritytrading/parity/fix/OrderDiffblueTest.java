package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {
  /**
   * Method under test: {@link Order#orderExecuted(double, double)}
   */
  @Test
  void testOrderExecuted() {
    // Arrange
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);

    // Act
    order.orderExecuted(10.0d, 10.0d);

    // Assert
    assertEquals('2', order.getOrdStatus());
    assertEquals(0.0d, order.getLeavesQty());
    assertEquals(10.0d, order.getAvgPx());
    assertEquals(10.0d, order.getCumQty());
  }

  /**
   * Method under test: {@link Order#orderExecuted(double, double)}
   */
  @Test
  void testOrderExecuted2() {
    // Arrange
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 0.0d);

    // Act
    order.orderExecuted(10.0d, 10.0d);

    // Assert
    assertEquals('1', order.getOrdStatus());
    assertEquals(-10.0d, order.getLeavesQty());
    assertEquals(10.0d, order.getAvgPx());
    assertEquals(10.0d, order.getCumQty());
  }

  /**
   * Method under test: {@link Order#orderCanceled(double)}
   */
  @Test
  void testOrderCanceled() {
    // Arrange
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);

    // Act
    order.orderCanceled(10.0d);

    // Assert
    assertEquals("42", order.getOrigClOrdID());
    assertNull(order.getClOrdID());
    assertEquals(0.0d, order.getLeavesQty());
    assertEquals(0.0d, order.getOrderQty());
  }

  /**
   * Method under test: {@link Order#getLeavesQty()}
   */
  @Test
  void testGetLeavesQty() {
    // Arrange, Act and Assert
    assertEquals(10.0d, (new Order(1L, "42", "3", 'A', "Symbol", 10.0d)).getLeavesQty());
  }

  /**
   * Method under test: {@link Order#isInPendingStatus()}
   */
  @Test
  void testIsInPendingStatus() {
    // Arrange, Act and Assert
    assertFalse((new Order(1L, "42", "3", 'A', "Symbol", 10.0d)).isInPendingStatus());
  }

  /**
   * Method under test: {@link Order#isInPendingStatus()}
   */
  @Test
  void testIsInPendingStatus2() {
    // Arrange
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    order.setNextClOrdID("foo");

    // Act and Assert
    assertTrue(order.isInPendingStatus());
  }

  /**
   * Methods under test:
   * <ul>
   *   <li>{@link Order#orderAccepted(long)}
   *   <li>{@link Order#setCxlRejResponseTo(char)}
   *   <li>{@link Order#setNextClOrdID(String)}
   *   <li>{@link Order#getAccount()}
   *   <li>{@link Order#getAvgPx()}
   *   <li>{@link Order#getClOrdID()}
   *   <li>{@link Order#getCumQty()}
   *   <li>{@link Order#getCxlRejResponseTo()}
   *   <li>{@link Order#getNextClOrdID()}
   *   <li>{@link Order#getOrdStatus()}
   *   <li>{@link Order#getOrderEntryID()}
   *   <li>{@link Order#getOrderID()}
   *   <li>{@link Order#getOrderQty()}
   *   <li>{@link Order#getOrigClOrdID()}
   *   <li>{@link Order#getSide()}
   *   <li>{@link Order#getSymbol()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);

    // Act
    order.orderAccepted(1L);
    order.setCxlRejResponseTo('A');
    order.setNextClOrdID("42");
    order.getAccount();
    double actualAvgPx = order.getAvgPx();
    String actualClOrdID = order.getClOrdID();
    double actualCumQty = order.getCumQty();
    char actualCxlRejResponseTo = order.getCxlRejResponseTo();
    String actualNextClOrdID = order.getNextClOrdID();
    char actualOrdStatus = order.getOrdStatus();
    long actualOrderEntryID = order.getOrderEntryID();
    long actualOrderID = order.getOrderID();
    double actualOrderQty = order.getOrderQty();
    order.getOrigClOrdID();
    char actualSide = order.getSide();

    // Assert that nothing has changed
    assertEquals("42", actualClOrdID);
    assertEquals("42", actualNextClOrdID);
    assertEquals("Symbol", order.getSymbol());
    assertEquals('0', actualOrdStatus);
    assertEquals('A', actualCxlRejResponseTo);
    assertEquals('A', actualSide);
    assertEquals(0.0d, actualAvgPx);
    assertEquals(0.0d, actualCumQty);
    assertEquals(10.0d, actualOrderQty);
    assertEquals(1L, actualOrderEntryID);
    assertEquals(1L, actualOrderID);
  }

  /**
   * Method under test:
   * {@link Order#Order(long, String, String, char, String, double)}
   */
  @Test
  void testNewOrder() {
    // Arrange and Act
    Order actualOrder = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);

    // Assert
    assertEquals("42", actualOrder.getClOrdID());
    assertEquals("Symbol", actualOrder.getSymbol());
    assertEquals('0', actualOrder.getOrdStatus());
    assertEquals('1', actualOrder.getCxlRejResponseTo());
    assertEquals('A', actualOrder.getSide());
    assertNull(actualOrder.getAccount());
    assertNull(actualOrder.getNextClOrdID());
    assertNull(actualOrder.getOrigClOrdID());
    assertEquals(0.0d, actualOrder.getAvgPx());
    assertEquals(0.0d, actualOrder.getCumQty());
    assertEquals(0L, actualOrder.getOrderID());
    assertEquals(10.0d, actualOrder.getLeavesQty());
    assertEquals(10.0d, actualOrder.getOrderQty());
    assertEquals(1L, actualOrder.getOrderEntryID());
    assertFalse(actualOrder.isInPendingStatus());
  }
}
