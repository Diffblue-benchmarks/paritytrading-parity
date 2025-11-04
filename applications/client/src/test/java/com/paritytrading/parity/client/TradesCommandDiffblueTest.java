package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class TradesCommandDiffblueTest {
  /**
   * Method under test: {@link TradesCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute() {
    // Arrange
    TradesCommand tradesCommand = new TradesCommand();

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> tradesCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Methods under test:
   * <ul>
   *   <li>default or parameterless constructor of {@link TradesCommand}
   *   <li>{@link TradesCommand#getDescription()}
   *   <li>{@link TradesCommand#getName()}
   *   <li>{@link TradesCommand#getUsage()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange and Act
    TradesCommand actualTradesCommand = new TradesCommand();
    String actualDescription = actualTradesCommand.getDescription();
    String actualName = actualTradesCommand.getName();

    // Assert
    assertEquals("Display occurred trades", actualDescription);
    assertEquals("trades", actualName);
    assertEquals("trades", actualTradesCommand.getUsage());
  }
}
