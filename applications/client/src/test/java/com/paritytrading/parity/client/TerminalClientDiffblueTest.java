package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.util.Instruments;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TerminalClientDiffblueTest {
  /**
   * Test {@link TerminalClient#TerminalClient(Events, OrderEntry, Instruments)}.
   *
   * <ul>
   *   <li>Then return Instruments is createInstruments.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#TerminalClient(Events, OrderEntry, Instruments)}
   */
  @Test
  @DisplayName(
      "Test new TerminalClient(Events, OrderEntry, Instruments); then return Instruments is createInstruments")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TerminalClient.<init>(Events, OrderEntry, Instruments)"})
  void testNewTerminalClient_thenReturnInstrumentsIsCreateInstruments() throws IOException {
    // Arrange
    Events events = new Events();
    OrderEntry orderEntry = OrderEntryTestFactory.createOrderEntryWithListener(new Events());
    Instruments instruments = InstrumentsTestFactory.createInstruments();

    // Act
    TerminalClient actualTerminalClient = new TerminalClient(events, orderEntry, instruments);
    actualTerminalClient.close();

    // Assert
    assertEquals(1, actualTerminalClient.getOrderIdGenerator().getCount());
    Events events2 = actualTerminalClient.getEvents();
    assertTrue(events2.getEvents().isEmpty());
    assertSame(events, events2);
    assertSame(instruments, actualTerminalClient.getInstruments());
    assertSame(orderEntry, actualTerminalClient.getOrderEntry());
  }

  /**
   * Test {@link TerminalClient#TerminalClient(Events, OrderEntry, Instruments)}.
   *
   * <ul>
   *   <li>When createMockOrderEntry.
   *   <li>Then return Instruments is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#TerminalClient(Events, OrderEntry, Instruments)}
   */
  @Test
  @DisplayName(
      "Test new TerminalClient(Events, OrderEntry, Instruments); when createMockOrderEntry; then return Instruments is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TerminalClient.<init>(Events, OrderEntry, Instruments)"})
  void testNewTerminalClient_whenCreateMockOrderEntry_thenReturnInstrumentsIsNull()
      throws IOException {
    // Arrange
    Events events = new Events();
    OrderEntry orderEntry = OrderEntryTestFactory.createMockOrderEntry();

    // Act
    TerminalClient actualTerminalClient = new TerminalClient(events, orderEntry, null);
    actualTerminalClient.close();

    // Assert
    assertNull(actualTerminalClient.getInstruments());
    assertEquals(1, actualTerminalClient.getOrderIdGenerator().getCount());
    Events events2 = actualTerminalClient.getEvents();
    assertTrue(events2.getEvents().isEmpty());
    assertSame(events, events2);
    assertSame(orderEntry, actualTerminalClient.getOrderEntry());
  }

  /**
   * Test {@link TerminalClient#findCommand(String)}.
   *
   * <ul>
   *   <li>When {@code buy}.
   *   <li>Then return {@link EnterCommand}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#findCommand(String)}
   */
  @Test
  @DisplayName("Test findCommand(String); when 'buy'; then return EnterCommand")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
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
   *
   * <ul>
   *   <li>When createUsername.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#findCommand(String)}
   */
  @Test
  @DisplayName("Test findCommand(String); when createUsername; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Command TerminalClient.findCommand(String)"})
  void testFindCommand_whenCreateUsername_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(TerminalClient.findCommand(TerminalClientTestFactory.createUsername()));
  }
}
