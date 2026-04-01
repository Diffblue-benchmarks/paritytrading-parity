package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.util.Instruments;
import com.paritytrading.parity.util.OrderIDGenerator;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Scanner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TerminalClientDiffblueTest {
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
   *   <li>When {@code Name}.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#findCommand(String)}
   */
  @Test
  @DisplayName("Test findCommand(String); when 'Name'; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Command TerminalClient.findCommand(String)"})
  void testFindCommand_whenName_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(TerminalClient.findCommand("Name"));
  }

  /**
   * Test {@link TerminalClient#getOrderEntry()}.
   *
   * <ul>
   *   <li>Given {@link TerminalClient} constructed with mocked dependencies.
   *   <li>Then return the expected {@link OrderEntry}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#getOrderEntry()}
   */
  @Test
  @DisplayName("Test getOrderEntry(); given TerminalClient constructed with mocks; then return expected OrderEntry")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"OrderEntry TerminalClient.getOrderEntry()"})
  void testGetOrderEntry_givenConstructedClient_thenReturnOrderEntry() throws Exception {
    // Arrange
    Events events = new Events();
    OrderEntry orderEntry = mock(OrderEntry.class);
    Instruments instruments = mock(Instruments.class);

    Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
        Events.class, OrderEntry.class, Instruments.class);
    constructor.setAccessible(true);

    // Act
    TerminalClient client = constructor.newInstance(events, orderEntry, instruments);

    // Assert
    assertSame(orderEntry, client.getOrderEntry());
  }

  /**
   * Test {@link TerminalClient#getInstruments()}.
   *
   * <ul>
   *   <li>Given {@link TerminalClient} constructed with mocked dependencies.
   *   <li>Then return the expected {@link Instruments}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#getInstruments()}
   */
  @Test
  @DisplayName("Test getInstruments(); given TerminalClient constructed with mocks; then return expected Instruments")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Instruments TerminalClient.getInstruments()"})
  void testGetInstruments_givenConstructedClient_thenReturnInstruments() throws Exception {
    // Arrange
    Events events = new Events();
    OrderEntry orderEntry = mock(OrderEntry.class);
    Instruments instruments = mock(Instruments.class);

    Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
        Events.class, OrderEntry.class, Instruments.class);
    constructor.setAccessible(true);

    // Act
    TerminalClient client = constructor.newInstance(events, orderEntry, instruments);

    // Assert
    assertSame(instruments, client.getInstruments());
  }

  /**
   * Test {@link TerminalClient#getOrderIdGenerator()}.
   *
   * <ul>
   *   <li>Given {@link TerminalClient} constructed with mocked dependencies.
   *   <li>Then return not null {@link OrderIDGenerator}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#getOrderIdGenerator()}
   */
  @Test
  @DisplayName("Test getOrderIdGenerator(); given TerminalClient constructed with mocks; then return not null")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"OrderIDGenerator TerminalClient.getOrderIdGenerator()"})
  void testGetOrderIdGenerator_givenConstructedClient_thenReturnNotNull() throws Exception {
    // Arrange
    Events events = new Events();
    OrderEntry orderEntry = mock(OrderEntry.class);
    Instruments instruments = mock(Instruments.class);

    Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
        Events.class, OrderEntry.class, Instruments.class);
    constructor.setAccessible(true);

    // Act
    TerminalClient client = constructor.newInstance(events, orderEntry, instruments);

    // Assert
    assertNotNull(client.getOrderIdGenerator());
    assertTrue(client.getOrderIdGenerator() instanceof OrderIDGenerator);
  }

  /**
   * Test {@link TerminalClient#getEvents()}.
   *
   * <ul>
   *   <li>Given {@link TerminalClient} constructed with mocked dependencies.
   *   <li>Then return the expected {@link Events}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#getEvents()}
   */
  @Test
  @DisplayName("Test getEvents(); given TerminalClient constructed with mocks; then return expected Events")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Events TerminalClient.getEvents()"})
  void testGetEvents_givenConstructedClient_thenReturnEvents() throws Exception {
    // Arrange
    Events events = new Events();
    OrderEntry orderEntry = mock(OrderEntry.class);
    Instruments instruments = mock(Instruments.class);

    Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
        Events.class, OrderEntry.class, Instruments.class);
    constructor.setAccessible(true);

    // Act
    TerminalClient client = constructor.newInstance(events, orderEntry, instruments);

    // Assert
    assertSame(events, client.getEvents());
  }

  /**
   * Test {@link TerminalClient#close()}.
   *
   * <ul>
   *   <li>Given {@link TerminalClient} constructed with mocked {@link OrderEntry}.
   *   <li>Then {@link OrderEntry#close()} is called.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#close()}
   */
  @Test
  @DisplayName("Test close(); given TerminalClient constructed with mocked OrderEntry; then OrderEntry.close() is called")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TerminalClient.close()"})
  void testClose_givenConstructedClient_thenOrderEntryIsClosed() throws Exception {
    // Arrange
    Events events = new Events();
    OrderEntry orderEntry = mock(OrderEntry.class);
    Instruments instruments = mock(Instruments.class);

    Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
        Events.class, OrderEntry.class, Instruments.class);
    constructor.setAccessible(true);
    TerminalClient client = constructor.newInstance(events, orderEntry, instruments);

    // Act
    client.close();

    // Assert
    verify(orderEntry).close();
  }

  /**
   * Test {@code scan(String)} private method via reflection.
   *
   * <ul>
   *   <li>Given text {@code "42 FOO 10.5"}.
   *   <li>Then return a {@link Scanner} that reads the expected tokens.
   * </ul>
   *
   * <p>Method under test: {@code TerminalClient.scan(String)}
   */
  @Test
  @DisplayName("Test scan(String); given text '42 FOO 10.5'; then return Scanner with expected tokens")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Scanner TerminalClient.scan(String)"})
  void testScan_givenText_thenReturnScannerWithLocale() throws Exception {
    // Arrange
    Events events = new Events();
    OrderEntry orderEntry = mock(OrderEntry.class);
    Instruments instruments = mock(Instruments.class);

    Constructor<TerminalClient> constructor = TerminalClient.class.getDeclaredConstructor(
        Events.class, OrderEntry.class, Instruments.class);
    constructor.setAccessible(true);
    TerminalClient client = constructor.newInstance(events, orderEntry, instruments);

    Method scanMethod = TerminalClient.class.getDeclaredMethod("scan", String.class);
    scanMethod.setAccessible(true);

    // Act
    Scanner scanner = (Scanner) scanMethod.invoke(client, "42 FOO 10.5");

    // Assert
    assertEquals("42", scanner.next());
    assertEquals("FOO", scanner.next());
    assertEquals("10.5", scanner.next());
  }

  /**
   * Test {@link TerminalClient#open(InetSocketAddress, String, String, Instruments)}.
   *
   * <ul>
   *   <li>When the address is unreachable.
   *   <li>Then throw {@link IOException}.
   * </ul>
   *
   * <p>Method under test: {@link TerminalClient#open(InetSocketAddress, String, String, Instruments)}
   */
  @Test
  @DisplayName("Test open(InetSocketAddress, String, String, Instruments); when address is unreachable; then throw IOException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TerminalClient TerminalClient.open(InetSocketAddress, String, String, Instruments)"})
  void testOpen_givenUnreachableAddress_thenThrowIOException() {
    // Arrange
    InetSocketAddress unreachableAddress = new InetSocketAddress("127.0.0.1", 1);
    Instruments instruments = mock(Instruments.class);

    // Act and Assert
    assertThrows(IOException.class,
        () -> TerminalClient.open(unreachableAddress, "user", "pass", instruments));
  }
}
