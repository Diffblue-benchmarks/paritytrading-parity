package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class ExitCommandDiffblueTest {
  /**
   * Method under test: {@link ExitCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute() {
    // Arrange
    ExitCommand exitCommand = new ExitCommand();

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> exitCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Methods under test:
   * <ul>
   *   <li>default or parameterless constructor of {@link ExitCommand}
   *   <li>{@link ExitCommand#getDescription()}
   *   <li>{@link ExitCommand#getName()}
   *   <li>{@link ExitCommand#getUsage()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange and Act
    ExitCommand actualExitCommand = new ExitCommand();
    String actualDescription = actualExitCommand.getDescription();
    String actualName = actualExitCommand.getName();

    // Assert
    assertEquals("Exit the client", actualDescription);
    assertEquals("exit", actualName);
    assertEquals("exit", actualExitCommand.getUsage());
  }
}
