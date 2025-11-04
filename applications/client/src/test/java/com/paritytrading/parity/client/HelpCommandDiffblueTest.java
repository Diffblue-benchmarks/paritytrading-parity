package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class HelpCommandDiffblueTest {
  /**
   * Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute() {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    Scanner arguments = new Scanner("foo");

    // Act
    helpCommand.execute(null, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute2() {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    Scanner arguments = new Scanner("buy");

    // Act
    helpCommand.execute(null, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute3() {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    Scanner arguments = new Scanner("cancel");

    // Act
    helpCommand.execute(null, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute4() {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    Scanner arguments = new Scanner("orders");

    // Act
    helpCommand.execute(null, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute5() {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();
    Scanner arguments = new Scanner("help");

    // Act
    helpCommand.execute(null, arguments);

    // Assert
    assertFalse(arguments.hasNext());
  }

  /**
   * Method under test: {@link HelpCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute6() {
    // Arrange
    HelpCommand helpCommand = new HelpCommand();

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> helpCommand.execute(null, new Scanner("s  %s\n")));
  }

  /**
   * Methods under test:
   * <ul>
   *   <li>default or parameterless constructor of {@link HelpCommand}
   *   <li>{@link HelpCommand#getDescription()}
   *   <li>{@link HelpCommand#getName()}
   *   <li>{@link HelpCommand#getUsage()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange and Act
    HelpCommand actualHelpCommand = new HelpCommand();
    String actualDescription = actualHelpCommand.getDescription();
    String actualName = actualHelpCommand.getName();

    // Assert
    assertEquals("Display the help", actualDescription);
    assertEquals("help [command]", actualHelpCommand.getUsage());
    assertEquals("help", actualName);
  }
}
