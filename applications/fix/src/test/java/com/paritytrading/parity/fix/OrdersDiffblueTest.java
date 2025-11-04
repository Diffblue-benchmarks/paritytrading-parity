package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

class OrdersDiffblueTest {
  /**
   * Method under test: {@link Orders#add(Order)}
   */
  @Test
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
   * Method under test: {@link Orders#findByClOrdID(String)}
   */
  @Test
  void testFindByClOrdID() {
    // Arrange, Act and Assert
    assertNull((new Orders()).findByClOrdID("42"));
  }

  /**
   * Method under test: {@link Orders#findByClOrdID(String)}
   */
  @Test
  void testFindByClOrdID2() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);

    orders.add(order);

    // Act and Assert
    assertSame(order, orders.findByClOrdID("42"));
  }

  /**
   * Method under test: {@link Orders#findByClOrdID(String)}
   */
  @Test
  void testFindByClOrdID3() {
    // Arrange
    Orders orders = new Orders();
    orders.add(new Order(1L, "Cl Ord Id", "3", 'A', "Symbol", 10.0d));

    // Act and Assert
    assertNull(orders.findByClOrdID("42"));
  }

  /**
   * Method under test: {@link Orders#findByOrderEntryID(long)}
   */
  @Test
  void testFindByOrderEntryID() {
    // Arrange, Act and Assert
    assertNull((new Orders()).findByOrderEntryID(1L));
  }

  /**
   * Method under test: {@link Orders#findByOrderEntryID(long)}
   */
  @Test
  void testFindByOrderEntryID2() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(1L, "42", "3", 'A', "Symbol", 10.0d);

    orders.add(order);

    // Act and Assert
    assertSame(order, orders.findByOrderEntryID(1L));
  }

  /**
   * Method under test: {@link Orders#findByOrderEntryID(long)}
   */
  @Test
  void testFindByOrderEntryID3() {
    // Arrange
    Orders orders = new Orders();
    orders.add(new Order(2L, "42", "3", 'A', "Symbol", 10.0d));

    // Act and Assert
    assertNull(orders.findByOrderEntryID(1L));
  }

  /**
   * Method under test: {@link Orders#removeByOrderEntryID(long)}
   */
  @Test
  void testRemoveByOrderEntryID() {
    // Arrange
    Orders orders = new Orders();

    // Act
    orders.removeByOrderEntryID(1L);

    // Assert that nothing has changed
    assertNull(orders.findByClOrdID("42"));
    assertNull(orders.findByOrderEntryID(1L));
  }

  /**
   * Method under test: {@link Orders#removeByOrderEntryID(long)}
   */
  @Test
  void testRemoveByOrderEntryID2() {
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
   * Method under test: {@link Orders#removeByOrderEntryID(long)}
   */
  @Test
  void testRemoveByOrderEntryID3() {
    // Arrange
    Orders orders = new Orders();
    Order order = new Order(2L, "42", "3", 'A', "Symbol", 10.0d);

    orders.add(order);

    // Act
    orders.removeByOrderEntryID(1L);

    // Assert that nothing has changed
    assertSame(order, orders.findByClOrdID("42"));
  }
}
