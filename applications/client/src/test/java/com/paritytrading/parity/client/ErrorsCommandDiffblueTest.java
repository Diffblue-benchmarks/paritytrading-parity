package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class ErrorsCommandDiffblueTest {
  /**
   * Method under test: {@link ErrorsCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute() {
    // Arrange
    ErrorsCommand errorsCommand = new ErrorsCommand();

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> errorsCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Methods under test:
   * <ul>
   *   <li>default or parameterless constructor of {@link ErrorsCommand}
   *   <li>{@link ErrorsCommand#getDescription()}
   *   <li>{@link ErrorsCommand#getName()}
   *   <li>{@link ErrorsCommand#getUsage()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange and Act
    ErrorsCommand actualErrorsCommand = new ErrorsCommand();
    String actualDescription = actualErrorsCommand.getDescription();
    String actualName = actualErrorsCommand.getName();

    // Assert
    assertEquals("Display occurred errors", actualDescription);
    assertEquals("errors", actualName);
    assertEquals("errors", actualErrorsCommand.getUsage());
  }
}
