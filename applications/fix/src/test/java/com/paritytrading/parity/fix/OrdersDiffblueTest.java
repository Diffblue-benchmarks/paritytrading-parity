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
    orders.add(new Order(1L, "Cl Ord Id", "3", 'A', "Symbol", 10.0d));

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
    orders.add(new Order(2L, "42", "3", 'A', "Symbol", 10.0d));

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
    orders.add(new Order(1L, "42", "3", 'A', "Symbol", 10.0d));

    // Act
    orders.removeByOrderEntryID(1L);

    // Assert
    assertNull(orders.findByClOrdID("42"));
    assertNull(orders.findByOrderEntryID(1L));
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
  void testRemoveByOrderEntryID2() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(2L, "42", "3", 'A', "Symbol", 10.0d);

    orders.add(order);

    // Act
    orders.removeByOrderEntryID(1L);

    // Assert that nothing has changed
    assertSame(order, orders.findByClOrdID("42"));
  }

  /**
   * Test {@link Orders#removeByOrderEntryID(long)}.
   *
   * <ul>
   *   <li>Given {@link Orders} (default constructor).
   *   <li>Then {@link Orders} (default constructor) findByClOrdID {@code 42} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link Orders#removeByOrderEntryID(long)}
   */
  @Test
  @DisplayName(
      "Test removeByOrderEntryID(long); given Orders (default constructor); then Orders (default constructor) findByClOrdID '42' is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Orders.removeByOrderEntryID(long)"})
  void testRemoveByOrderEntryID_givenOrders_thenOrdersFindByClOrdID42IsNull() {
    // Arrange
    Orders orders = new Orders();

    // Act
    orders.removeByOrderEntryID(1L);

    // Assert that nothing has changed
    assertNull(orders.findByClOrdID("42"));
    assertNull(orders.findByOrderEntryID(1L));
  }
}
