package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.nio.channels.ServerSocketChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderEntryDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link OrderEntry#OrderEntry(ServerSocketChannel, OrderBooks)}
   *   <li>{@link OrderEntry#getChannel()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OrderEntry.<init>(ServerSocketChannel, OrderBooks)",
    "ServerSocketChannel OrderEntry.getChannel()"
  })
  void testGettersAndSetters() {
    // Arrange, Act and Assert
    assertNull(new OrderEntry(null, TestFactories.createOrderBooks()).getChannel());
  }
}
