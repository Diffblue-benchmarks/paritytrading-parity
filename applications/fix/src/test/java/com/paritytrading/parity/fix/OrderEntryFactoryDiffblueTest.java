package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.net.InetSocketAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderEntryFactoryDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link OrderEntryFactory#OrderEntryFactory(InetSocketAddress)}
   *   <li>{@link OrderEntryFactory#getAddress()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OrderEntryFactory.<init>(InetSocketAddress)",
    "InetSocketAddress OrderEntryFactory.getAddress()"
  })
  void testGettersAndSetters() {
    // Arrange
    InetSocketAddress address = InetSocketAddress.createUnresolved("foo", 1);

    // Act and Assert
    assertSame(address, new OrderEntryFactory(address).getAddress());
  }
}
