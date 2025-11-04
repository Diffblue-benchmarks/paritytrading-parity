package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class OrdersCommandDiffblueTest {
  /**
   * Method under test: {@link OrdersCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute() {
    // Arrange
    OrdersCommand ordersCommand = new OrdersCommand();

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> ordersCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Methods under test:
   * <ul>
   *   <li>default or parameterless constructor of {@link OrdersCommand}
   *   <li>{@link OrdersCommand#getDescription()}
   *   <li>{@link OrdersCommand#getName()}
   *   <li>{@link OrdersCommand#getUsage()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange and Act
    OrdersCommand actualOrdersCommand = new OrdersCommand();
    String actualDescription = actualOrdersCommand.getDescription();
    String actualName = actualOrdersCommand.getName();

    // Assert
    assertEquals("Display open orders", actualDescription);
    assertEquals("orders", actualName);
    assertEquals("orders", actualOrdersCommand.getUsage());
  }
}
