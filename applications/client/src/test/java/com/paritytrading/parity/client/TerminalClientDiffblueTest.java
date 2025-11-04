package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TerminalClientDiffblueTest {
  /**
   * Test {@link TerminalClient#findCommand(String)}.
   * <ul>
   *   <li>When {@code buy}.</li>
   *   <li>Then return {@link EnterCommand}.</li>
   * </ul>
   * <p>
   * Method under test: {@link TerminalClient#findCommand(String)}
   */
  @Test
  @DisplayName("Test findCommand(String); when 'buy'; then return EnterCommand")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Command TerminalClient.findCommand(String)"})
  void testFindCommand_whenBuy_thenReturnEnterCommand() {
    // Arrange and Act
    Command actualFindCommandResult = TerminalClient.findCommand("buy");

    // Assert
    assertTrue(actualFindCommandResult instanceof EnterCommand);
    assertEquals("Enter a buy order", actualFindCommandResult.getDescription());
    assertEquals("buy <quantity> <instrument> <price>", actualFindCommandResult.getUsage());
    assertEquals("buy", actualFindCommandResult.getName());
  }

  /**
   * Test {@link TerminalClient#findCommand(String)}.
   * <ul>
   *   <li>When {@code Name}.</li>
   *   <li>Then return {@code null}.</li>
   * </ul>
   * <p>
   * Method under test: {@link TerminalClient#findCommand(String)}
   */
  @Test
  @DisplayName("Test findCommand(String); when 'Name'; then return 'null'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Command TerminalClient.findCommand(String)"})
  void testFindCommand_whenName_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(TerminalClient.findCommand("Name"));
  }
}
