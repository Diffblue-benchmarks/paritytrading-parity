package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class EnterCommandDiffblueTest {
  /**
   * Test {@link EnterCommand#EnterCommand(byte)}.
   *
   * <p>Method under test: {@link EnterCommand#EnterCommand(byte)}
   */
  @Test
  @DisplayName("Test new EnterCommand(byte)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
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
   * Test {@link EnterCommand#execute(TerminalClient, Scanner)} with {@code client}, {@code
   * arguments}.
   *
   * <ul>
   *   <li>Then throw {@link IllegalArgumentException}.
   * </ul>
   *
   * <p>Method under test: {@link EnterCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner) with 'client', 'arguments'; then throw IllegalArgumentException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EnterCommand.execute(TerminalClient, Scanner)"})
  void testExecuteWithClientArguments_thenThrowIllegalArgumentException() throws IOException {
    // Arrange
    EnterCommand enterCommand = new EnterCommand((byte) 'A');

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> enterCommand.execute(null, new Scanner("Source")));
  }

  /**
   * Test {@link EnterCommand#execute(TerminalClient, Scanner)} with {@code client}, {@code
   * arguments}.
   *
   * <ul>
   *   <li>When {@link Scanner#Scanner(String)} with {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link EnterCommand#execute(TerminalClient, Scanner)}
   */
  @Test
  @DisplayName(
      "Test execute(TerminalClient, Scanner) with 'client', 'arguments'; when Scanner(String) with '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EnterCommand.execute(TerminalClient, Scanner)"})
  void testExecuteWithClientArguments_whenScannerWith42() throws IOException {
    // Arrange
    EnterCommand enterCommand = new EnterCommand((byte) 'A');

    // Act and Assert
    assertThrows(
        IllegalArgumentException.class, () -> enterCommand.execute(null, new Scanner("42")));
  }

  /**
   * Test {@link EnterCommand#getName()}.
   *
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code A}.
   *   <li>Then return {@code sell}.
   * </ul>
   *
   * <p>Method under test: {@link EnterCommand#getName()}
   */
  @Test
  @DisplayName("Test getName(); given EnterCommand(byte) with side is 'A'; then return 'sell'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String EnterCommand.getName()"})
  void testGetName_givenEnterCommandWithSideIsA_thenReturnSell() {
    // Arrange, Act and Assert
    assertEquals("sell", new EnterCommand((byte) 'A').getName());
  }

  /**
   * Test {@link EnterCommand#getName()}.
   *
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code B}.
   *   <li>Then return {@code buy}.
   * </ul>
   *
   * <p>Method under test: {@link EnterCommand#getName()}
   */
  @Test
  @DisplayName("Test getName(); given EnterCommand(byte) with side is 'B'; then return 'buy'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String EnterCommand.getName()"})
  void testGetName_givenEnterCommandWithSideIsB_thenReturnBuy() {
    // Arrange, Act and Assert
    assertEquals("buy", new EnterCommand((byte) 'B').getName());
  }

  /**
   * Test {@link EnterCommand#getDescription()}.
   *
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code A}.
   *   <li>Then return {@code Enter a sell order}.
   * </ul>
   *
   * <p>Method under test: {@link EnterCommand#getDescription()}
   */
  @Test
  @DisplayName(
      "Test getDescription(); given EnterCommand(byte) with side is 'A'; then return 'Enter a sell order'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String EnterCommand.getDescription()"})
  void testGetDescription_givenEnterCommandWithSideIsA_thenReturnEnterASellOrder() {
    // Arrange, Act and Assert
    assertEquals("Enter a sell order", new EnterCommand((byte) 'A').getDescription());
  }

  /**
   * Test {@link EnterCommand#getDescription()}.
   *
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code B}.
   *   <li>Then return {@code Enter a buy order}.
   * </ul>
   *
   * <p>Method under test: {@link EnterCommand#getDescription()}
   */
  @Test
  @DisplayName(
      "Test getDescription(); given EnterCommand(byte) with side is 'B'; then return 'Enter a buy order'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String EnterCommand.getDescription()"})
  void testGetDescription_givenEnterCommandWithSideIsB_thenReturnEnterABuyOrder() {
    // Arrange, Act and Assert
    assertEquals("Enter a buy order", new EnterCommand((byte) 'B').getDescription());
  }

  /**
   * Test {@link EnterCommand#getUsage()}.
   *
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code A}.
   *   <li>Then return {@code sell <quantity> <instrument> <price>}.
   * </ul>
   *
   * <p>Method under test: {@link EnterCommand#getUsage()}
   */
  @Test
  @DisplayName(
      "Test getUsage(); given EnterCommand(byte) with side is 'A'; then return 'sell <quantity> <instrument> <price>'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String EnterCommand.getUsage()"})
  void testGetUsage_givenEnterCommandWithSideIsA_thenReturnSellQuantityInstrumentPrice() {
    // Arrange, Act and Assert
    assertEquals("sell <quantity> <instrument> <price>", new EnterCommand((byte) 'A').getUsage());
  }

  /**
   * Test {@link EnterCommand#getUsage()}.
   *
   * <ul>
   *   <li>Given {@link EnterCommand#EnterCommand(byte)} with side is {@code B}.
   *   <li>Then return {@code buy <quantity> <instrument> <price>}.
   * </ul>
   *
   * <p>Method under test: {@link EnterCommand#getUsage()}
   */
  @Test
  @DisplayName(
      "Test getUsage(); given EnterCommand(byte) with side is 'B'; then return 'buy <quantity> <instrument> <price>'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String EnterCommand.getUsage()"})
  void testGetUsage_givenEnterCommandWithSideIsB_thenReturnBuyQuantityInstrumentPrice() {
    // Arrange, Act and Assert
    assertEquals("buy <quantity> <instrument> <price>", new EnterCommand((byte) 'B').getUsage());
  }
}
