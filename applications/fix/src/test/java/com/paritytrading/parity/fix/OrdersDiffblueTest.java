package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrdersDiffblueTest {
  /**
   * Test {@link Orders#add(Order)}.
   *
   * <p>Method under test: {@link Orders#add(Order)}
   */
  @Test
  @DisplayName("Test add(Order)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Orders.add(Order)"})
  void testAdd() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);

    // Act
    orders.add(order);

    // Assert
    assertSame(order, orders.findByClOrdID("42"));
    assertSame(order, orders.findByOrderEntryID(1L));
  }

  /**
   * Test {@link Orders#findByClOrdID(String)}.
   *
   * <p>Method under test: {@link Orders#findByClOrdID(String)}
   */
  @Test
  @DisplayName("Test findByClOrdID(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Order Orders.findByClOrdID(String)"})
  void testFindByClOrdID() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order);
    Order order2 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order2);
    Order order3 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order3);
    Order order4 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order4);
    Order order5 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order5);
    Order order6 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order6);
    Order order7 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order7);
    Order order8 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order8);
    Order order9 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order9);
    Order order10 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order10);
    Order order11 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order11);
    Order order12 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order12);
    Order order13 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order13);
    Order order14 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order14);
    Order order15 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order15);
    Order order16 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order16);
    Order order17 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order17);
    Order order18 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order18);

    // Act and Assert
    assertSame(order, orders.findByClOrdID("42"));
  }

  /**
   * Test {@link Orders#findByClOrdID(String)}.
   *
   * <p>Method under test: {@link Orders#findByClOrdID(String)}
   */
  @Test
  @DisplayName("Test findByClOrdID(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Order Orders.findByClOrdID(String)"})
  void testFindByClOrdID2() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(1L, "Cl Ord Id", "3", 'A', "Symbol", 10.0d);
    orders.add(order);

    // Act and Assert
    assertNull(orders.findByClOrdID("42"));
  }

  /**
   * Test {@link Orders#findByClOrdID(String)}.
   *
   * <ul>
   *   <li>Given {@link Orders} (default constructor).
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link Orders#findByClOrdID(String)}
   */
  @Test
  @DisplayName("Test findByClOrdID(String); given Orders (default constructor); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Order Orders.findByClOrdID(String)"})
  void testFindByClOrdID_givenOrders_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(new Orders().findByClOrdID("42"));
  }

  /**
   * Test {@link Orders#findByOrderEntryID(long)}.
   *
   * <p>Method under test: {@link Orders#findByOrderEntryID(long)}
   */
  @Test
  @DisplayName("Test findByOrderEntryID(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Order Orders.findByOrderEntryID(long)"})
  void testFindByOrderEntryID() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order);
    Order order2 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order2);
    Order order3 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order3);
    Order order4 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order4);
    Order order5 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order5);
    Order order6 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order6);
    Order order7 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order7);
    Order order8 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order8);
    Order order9 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order9);
    Order order10 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order10);
    Order order11 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order11);
    Order order12 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order12);
    Order order13 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order13);
    Order order14 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order14);
    Order order15 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order15);
    Order order16 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order16);
    Order order17 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order17);
    Order order18 = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order18);

    // Act and Assert
    assertSame(order, orders.findByOrderEntryID(1L));
  }

  /**
   * Test {@link Orders#findByOrderEntryID(long)}.
   *
   * <p>Method under test: {@link Orders#findByOrderEntryID(long)}
   */
  @Test
  @DisplayName("Test findByOrderEntryID(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Order Orders.findByOrderEntryID(long)"})
  void testFindByOrderEntryID2() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(2L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order);

    // Act and Assert
    assertNull(orders.findByOrderEntryID(1L));
  }

  /**
   * Test {@link Orders#findByOrderEntryID(long)}.
   *
   * <ul>
   *   <li>Given {@link Orders} (default constructor).
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link Orders#findByOrderEntryID(long)}
   */
  @Test
  @DisplayName(
      "Test findByOrderEntryID(long); given Orders (default constructor); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Order Orders.findByOrderEntryID(long)"})
  void testFindByOrderEntryID_givenOrders_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(new Orders().findByOrderEntryID(1L));
  }

  /**
   * Test {@link Orders#removeByOrderEntryID(long)}.
   *
   * <p>Method under test: {@link Orders#removeByOrderEntryID(long)}
   */
  @Test
  @DisplayName("Test removeByOrderEntryID(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Orders.removeByOrderEntryID(long)"})
  void testRemoveByOrderEntryID() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order);

    // Act
    orders.removeByOrderEntryID(1L);

    // Assert
    assertNull(orders.findByOrderEntryID(1L));
  }

  /**
   * Test {@link Orders#removeByOrderEntryID(long)}.
   *
   * <ul>
   *   <li>Given {@link Orders} has an order with a different ID.
   *   <li>Then the order is still present.
   * </ul>
   *
   * <p>Method under test: {@link Orders#removeByOrderEntryID(long)}
   */
  @Test
  @DisplayName("Test removeByOrderEntryID(long); given Orders with non-matching order; then order remains")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Orders.removeByOrderEntryID(long)"})
  void testRemoveByOrderEntryID_givenNonMatchingOrder_thenOrderRemains() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(2L, "42", "3", 'A', "Symbol", 10.0d);
    orders.add(order);

    // Act
    orders.removeByOrderEntryID(1L);

    // Assert
    assertSame(order, orders.findByOrderEntryID(2L));
  }
}
