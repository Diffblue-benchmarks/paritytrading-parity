package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderDiffblueTest {
  /**
   * Test {@link Order#Order(long, String, String, char, String, double)}.
   * <p>
   * Method under test: {@link Order#Order(long, String, String, char, String, double)}
   */
  @Test
  @DisplayName("Test new Order(long, String, String, char, String, double)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Order.<init>(long, String, String, char, String, double)"})
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

  /**
   * Test {@link Order#orderExecuted(double, double)}.
   * <p>
   * Method under test: {@link Order#orderExecuted(double, double)}
   */
  @Test
  @DisplayName("Test orderExecuted(double, double)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Order.orderExecuted(double, double)"})
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
   * Test {@link Order#orderExecuted(double, double)}.
   * <p>
   * Method under test: {@link Order#orderExecuted(double, double)}
   */
  @Test
  @DisplayName("Test orderExecuted(double, double)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Order.orderExecuted(double, double)"})
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
   * Test {@link Order#orderCanceled(double)}.
   * <p>
   * Method under test: {@link Order#orderCanceled(double)}
   */
  @Test
  @DisplayName("Test orderCanceled(double)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Order.orderCanceled(double)"})
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
   * Test getters and setters.
   * <p>
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
  @DisplayName("Test getters and setters")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"String Order.getAccount()", "double Order.getAvgPx()", "String Order.getClOrdID()",
      "double Order.getCumQty()", "char Order.getCxlRejResponseTo()", "String Order.getNextClOrdID()",
      "char Order.getOrdStatus()", "long Order.getOrderEntryID()", "long Order.getOrderID()",
      "double Order.getOrderQty()", "String Order.getOrigClOrdID()", "char Order.getSide()", "String Order.getSymbol()",
      "void Order.orderAccepted(long)", "void Order.setCxlRejResponseTo(char)", "void Order.setNextClOrdID(String)"})
  void testGettersAndSetters() {
    // Arrange
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);

    // Act
    order.orderAccepted(1L);
    order.setCxlRejResponseTo('A');
    order.setNextClOrdID("42");
    String actualAccount = order.getAccount();
    double actualAvgPx = order.getAvgPx();
    String actualClOrdID = order.getClOrdID();
    double actualCumQty = order.getCumQty();
    char actualCxlRejResponseTo = order.getCxlRejResponseTo();
    String actualNextClOrdID = order.getNextClOrdID();
    char actualOrdStatus = order.getOrdStatus();
    long actualOrderEntryID = order.getOrderEntryID();
    long actualOrderID = order.getOrderID();
    double actualOrderQty = order.getOrderQty();
    String actualOrigClOrdID = order.getOrigClOrdID();
    char actualSide = order.getSide();

    // Assert
    assertEquals("42", actualClOrdID);
    assertEquals("42", actualNextClOrdID);
    assertEquals("Symbol", order.getSymbol());
    assertEquals('0', actualOrdStatus);
    assertEquals('A', actualCxlRejResponseTo);
    assertEquals('A', actualSide);
    assertNull(actualAccount);
    assertNull(actualOrigClOrdID);
    assertEquals(0.0d, actualAvgPx);
    assertEquals(0.0d, actualCumQty);
    assertEquals(10.0d, actualOrderQty);
    assertEquals(1L, actualOrderEntryID);
    assertEquals(1L, actualOrderID);
  }

  /**
   * Test {@link Order#getLeavesQty()}.
   * <p>
   * Method under test: {@link Order#getLeavesQty()}
   */
  @Test
  @DisplayName("Test getLeavesQty()")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"double Order.getLeavesQty()"})
  void testGetLeavesQty() {
    // Arrange, Act and Assert
    assertEquals(10.0d, (new Order(1L, "42", "3", 'A', "Symbol", 10.0d)).getLeavesQty());
  }

  /**
   * Test {@link Order#isInPendingStatus()}.
   * <ul>
   *   <li>Then return {@code false}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Order#isInPendingStatus()}
   */
  @Test
  @DisplayName("Test isInPendingStatus(); then return 'false'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"boolean Order.isInPendingStatus()"})
  void testIsInPendingStatus_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse((new Order(1L, "42", "3", 'A', "Symbol", 10.0d)).isInPendingStatus());
  }

  /**
   * Test {@link Order#isInPendingStatus()}.
   * <ul>
   *   <li>Then return {@code true}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Order#isInPendingStatus()}
   */
  @Test
  @DisplayName("Test isInPendingStatus(); then return 'true'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"boolean Order.isInPendingStatus()"})
  void testIsInPendingStatus_thenReturnTrue() {
    // Arrange
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    order.setNextClOrdID("foo");

    // Act and Assert
    assertTrue(order.isInPendingStatus());
  }
}
