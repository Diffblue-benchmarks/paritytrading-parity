package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class EnterCommandDiffblueTest {
  /**
   * Test {@link EnterCommand#EnterCommand(byte)}.
   * <p>
   * Method under test: {@link EnterCommand#EnterCommand(byte)}
   */
  @Test
  @DisplayName("Test new EnterCommand(byte)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void EnterCommand.<init>(byte)"})
  void testNewEnterCommand() {
    // Arrange and Act
    EnterCommand actualEnterCommand = new EnterCommand((byte) 'A');

    // Assert
    assertEquals("Enter a sell order", actualEnterCommand.getDescription());
    assertEquals("sell <quantity> <instrument> <price>", actualEnterCommand.getUsage());
    assertEquals("sell", actualEnterCommand.getName());
  }

  /**
   * Test {@link EnterCommand#execute(TerminalClient, Scanner)} with {@code client}, {@code arguments}.
   * <ul>
   *   <li>Then throw {@link IllegalArgumentException}.</li>
   * </ul>
   * <p>
   * Method under test: {@link EnterCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName("Test execute(TerminalClient, Scanner) with 'client', 'arguments'; then throw IllegalArgumentException")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void EnterCommand.execute(TerminalClient, Scanner)"})
  void testExecuteWithClientArguments_thenThrowIllegalArgumentException() throws IOException {
    // Arrange
    EnterCommand enterCommand = new EnterCommand((byte) 'A');

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> enterCommand.execute(null, new Scanner("foo")));
  }

  /**
   * Test {@link EnterCommand#execute(TerminalClient, Scanner)} with {@code client}, {@code arguments}.
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code 42}.</li>
   * </ul>
   * <p>
   * Method under test: {@link EnterCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName("Test execute(TerminalClient, Scanner) with 'client', 'arguments'; when Scanner(String) with '42'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void EnterCommand.execute(TerminalClient, Scanner)"})
  void testExecuteWithClientArguments_whenScannerWith42() throws IOException {
    // Arrange
    EnterCommand enterCommand = new EnterCommand((byte) 'A');

    // Act and Assert
    assertThrows(IllegalArgumentException.class, () -> enterCommand.execute(null, new Scanner("42")));
  }

  /**
   * Test {@link EnterCommand#getName()}.
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code A}.</li>
   *   <li>Then return {@code sell}.</li>
   * </ul>
   * <p>
   * Method under test: {@link EnterCommand#getName()}
   */
  @Test
  @DisplayName("Test getName(); given EnterCommand(byte) with side is 'A'; then return 'sell'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String EnterCommand.getName()"})
  void testGetName_givenEnterCommandWithSideIsA_thenReturnSell() {
    // Arrange, Act and Assert
    assertEquals("sell", (new EnterCommand((byte) 'A')).getName());
  }

  /**
   * Test {@link EnterCommand#getName()}.
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code B}.</li>
   *   <li>Then return {@code buy}.</li>
   * </ul>
   * <p>
   * Method under test: {@link EnterCommand#getName()}
   */
  @Test
  @DisplayName("Test getName(); given EnterCommand(byte) with side is 'B'; then return 'buy'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String EnterCommand.getName()"})
  void testGetName_givenEnterCommandWithSideIsB_thenReturnBuy() {
    // Arrange, Act and Assert
    assertEquals("buy", (new EnterCommand((byte) 'B')).getName());
  }

  /**
   * Test {@link EnterCommand#getDescription()}.
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code A}.</li>
   *   <li>Then return {@code Enter a sell order}.</li>
   * </ul>
   * <p>
   * Method under test: {@link EnterCommand#getDescription()}
   */
  @Test
  @DisplayName("Test getDescription(); given EnterCommand(byte) with side is 'A'; then return 'Enter a sell order'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String EnterCommand.getDescription()"})
  void testGetDescription_givenEnterCommandWithSideIsA_thenReturnEnterASellOrder() {
    // Arrange, Act and Assert
    assertEquals("Enter a sell order", (new EnterCommand((byte) 'A')).getDescription());
  }

  /**
   * Test {@link EnterCommand#getDescription()}.
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code B}.</li>
   *   <li>Then return {@code Enter a buy order}.</li>
   * </ul>
   * <p>
   * Method under test: {@link EnterCommand#getDescription()}
   */
  @Test
  @DisplayName("Test getDescription(); given EnterCommand(byte) with side is 'B'; then return 'Enter a buy order'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String EnterCommand.getDescription()"})
  void testGetDescription_givenEnterCommandWithSideIsB_thenReturnEnterABuyOrder() {
    // Arrange, Act and Assert
    assertEquals("Enter a buy order", (new EnterCommand((byte) 'B')).getDescription());
  }

  /**
   * Test {@link EnterCommand#getUsage()}.
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code A}.</li>
   *   <li>Then return {@code sell <quantity> <instrument> <price>}.</li>
   * </ul>
   * <p>
   * Method under test: {@link EnterCommand#getUsage()}
   */
  @Test
  @DisplayName("Test getUsage(); given EnterCommand(byte) with side is 'A'; then return 'sell <quantity> <instrument> <price>'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String EnterCommand.getUsage()"})
  void testGetUsage_givenEnterCommandWithSideIsA_thenReturnSellQuantityInstrumentPrice() {
    // Arrange, Act and Assert
    assertEquals("sell <quantity> <instrument> <price>", (new EnterCommand((byte) 'A')).getUsage());
  }

  /**
   * Test {@link EnterCommand#getUsage()}.
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code B}.</li>
   *   <li>Then return {@code buy <quantity> <instrument> <price>}.</li>
   * </ul>
   * <p>
   * Method under test: {@link EnterCommand#getUsage()}
   */
  @Test
  @DisplayName("Test getUsage(); given EnterCommand(byte) with side is 'B'; then return 'buy <quantity> <instrument> <price>'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String EnterCommand.getUsage()"})
  void testGetUsage_givenEnterCommandWithSideIsB_thenReturnBuyQuantityInstrumentPrice() {
    // Arrange, Act and Assert
    assertEquals("buy <quantity> <instrument> <price>", (new EnterCommand((byte) 'B')).getUsage());
  }
}
