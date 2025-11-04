package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class EnterCommandDiffblueTest {
  /**
   * Method under test: {@link EnterCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute() throws IOException {
    // Arrange
    EnterCommand enterCommand = new EnterCommand((byte) 'A');

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> enterCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Method under test: {@link EnterCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  void testExecute2() throws IOException {
    // Arrange
    EnterCommand enterCommand = new EnterCommand((byte) 'A');

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> enterCommand.execute(null, new Scanner("42")));
  }

  /**
   * Method under test: {@link EnterCommand#getName()}
   */
  @Test
  void testGetName() {
    // Arrange, Act and Assert
    assertEquals("sell", (new EnterCommand((byte) 'A')).getName());
    assertEquals("buy", (new EnterCommand((byte) 'B')).getName());
  }

  /**
   * Method under test: {@link EnterCommand#getDescription()}
   */
  @Test
  void testGetDescription() {
    // Arrange, Act and Assert
    assertEquals("Enter a sell order", (new EnterCommand((byte) 'A')).getDescription());
    assertEquals("Enter a buy order", (new EnterCommand((byte) 'B')).getDescription());
  }

  /**
   * Method under test: {@link EnterCommand#getUsage()}
   */
  @Test
  void testGetUsage() {
    // Arrange, Act and Assert
    assertEquals("sell <quantity> <instrument> <price>", (new EnterCommand((byte) 'A')).getUsage());
    assertEquals("buy <quantity> <instrument> <price>", (new EnterCommand((byte) 'B')).getUsage());
  }

  /**
   * Method under test: {@link EnterCommand#EnterCommand(byte)}
   */
  @Test
  void testNewEnterCommand() {
    // Arrange and Act
    EnterCommand actualEnterCommand = new EnterCommand((byte) 'A');

    // Assert
    assertEquals("Enter a sell order", actualEnterCommand.getDescription());
    assertEquals("sell <quantity> <instrument> <price>", actualEnterCommand.getUsage());
    assertEquals("sell", actualEnterCommand.getName());
  }
}
