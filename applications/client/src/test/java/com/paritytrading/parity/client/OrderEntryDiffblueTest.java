package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POEClientListener;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderEntryDiffblueTest {
  /**
   * Test {@link OrderEntry#OrderEntry(Selector, SocketChannel, POEClientListener)}.
   *
   * <ul>
   *   <li>When {@link Events} (default constructor).
   *   <li>Then {@link Events} (default constructor) Events Empty.
   * </ul>
   *
   * <p>Method under test: {@link OrderEntry#OrderEntry(Selector, SocketChannel, POEClientListener)}
   */
  @Test
  @DisplayName(
      "Test new OrderEntry(Selector, SocketChannel, POEClientListener); when Events (default constructor); then Events (default constructor) Events Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderEntry.<init>(Selector, SocketChannel, POEClientListener)"})
  void testNewOrderEntry_whenEvents_thenEventsEventsEmpty() {
    // Arrange
    Events listener = new Events();

    // Act
    OrderEntry actualOrderEntry = new OrderEntry(null, null, listener);
    actualOrderEntry.close();

    // Assert
    assertNull(null);
    assertNull(null);
    assertNull(actualOrderEntry.getTransport().getChannel());
    assertTrue(listener.getEvents().isEmpty());
  }

  /**
   * Test {@link OrderEntry#OrderEntry(Selector, SocketChannel, POEClientListener)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link OrderEntry#OrderEntry(Selector, SocketChannel, POEClientListener)}
   */
  @Test
  @DisplayName("Test new OrderEntry(Selector, SocketChannel, POEClientListener); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderEntry.<init>(Selector, SocketChannel, POEClientListener)"})
  void testNewOrderEntry_whenNull() {
    // Arrange and Act
    OrderEntry actualOrderEntry = new OrderEntry(null, null, null);

    // Assert
    assertNull(null);
    assertNull(null);
    assertNull(null);
    assertNull(actualOrderEntry.getTransport().getChannel());
  }
}
