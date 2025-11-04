package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class CancelCommandDiffblueTest {
  /**
   * Method under test: {@link CancelCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute() throws IOException {
    // Arrange
    CancelCommand cancelCommand = new CancelCommand();

    Scanner arguments = new Scanner("foo");
    arguments.useDelimiter("foo");

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> cancelCommand.execute(null, arguments));
  }

  /**
   * Method under test: {@link CancelCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute2() throws IOException {
    // Arrange
    CancelCommand cancelCommand = new CancelCommand();

    Scanner arguments = new Scanner("foo");
    arguments.useDelimiter("");

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> cancelCommand.execute(null, arguments));
  }

  /**
   * Methods under test:
   * <ul>
   *   <li>{@link CancelCommand#getDescription()}
   *   <li>{@link CancelCommand#getName()}
   *   <li>{@link CancelCommand#getUsage()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange
    CancelCommand cancelCommand = new CancelCommand();

    // Act
    String actualDescription = cancelCommand.getDescription();
    String actualName = cancelCommand.getName();

    // Assert
    assertEquals("Cancel an order", actualDescription);
    assertEquals("cancel <order-id>", cancelCommand.getUsage());
    assertEquals("cancel", actualName);
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link CancelCommand}
   */
  @Test
  void testNewCancelCommand() {
    // Arrange and Act
    CancelCommand actualCancelCommand = new CancelCommand();

    // Assert
    assertEquals("Cancel an order", actualCancelCommand.getDescription());
    assertEquals("cancel <order-id>", actualCancelCommand.getUsage());
    assertEquals("cancel", actualCancelCommand.getName());
  }
}
