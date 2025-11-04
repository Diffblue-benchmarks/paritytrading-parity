package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TerminalClientDiffblueTest {
  /**
   * Method under test: {@link TerminalClient#findCommand(String)}
   */
  @Test
  void testFindCommand() {
    // Arrange, Act and Assert
    assertNull(TerminalClient.findCommand("Name"));
  }

  /**
   * Method under test: {@link TerminalClient#findCommand(String)}
   */
  @Test
  void testFindCommand2() {
    // Arrange and Act
    Command actualFindCommandResult = TerminalClient.findCommand("buy");

    // Assert
    assertTrue(actualFindCommandResult instanceof EnterCommand);
    assertEquals("Enter a buy order", actualFindCommandResult.getDescription());
    assertEquals("buy <quantity> <instrument> <price>", actualFindCommandResult.getUsage());
    assertEquals("buy", actualFindCommandResult.getName());
  }
}
