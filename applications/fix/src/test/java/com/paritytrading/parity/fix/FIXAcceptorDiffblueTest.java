package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.util.Instruments;
import com.typesafe.config.ConfigFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class FIXAcceptorDiffblueTest {

  private static Instruments createInstruments() {
    return Instruments.fromConfig(
        ConfigFactory.parseString(
            "instruments { FOO { price-fraction-digits = 2, size-fraction-digits = 2 } }"),
        "instruments");
  }

  /**
   * Test {@link FIXAcceptor#open(OrderEntryFactory, InetSocketAddress, String, Instruments)}.
   *
   * <p>Method under test: {@link FIXAcceptor#open(OrderEntryFactory, InetSocketAddress, String,
   * Instruments)}
   */
  @Test
  @DisplayName("Test open(OrderEntryFactory, InetSocketAddress, String, Instruments)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "FIXAcceptor FIXAcceptor.open(OrderEntryFactory, InetSocketAddress, String, Instruments)"
  })
  void testOpen() throws IOException {
    // Arrange
    OrderEntryFactory orderEntry =
        new OrderEntryFactory(new InetSocketAddress("127.0.0.1", 12345));
    InetSocketAddress address = new InetSocketAddress("127.0.0.1", 0);
    Instruments instruments = createInstruments();

    // Act
    FIXAcceptor acceptor = FIXAcceptor.open(orderEntry, address, "SENDER", instruments);

    // Assert
    assertTrue(acceptor.getServerChannel().isOpen());
    acceptor.getServerChannel().close();
  }

  /**
   * Test {@link FIXAcceptor#getServerChannel()}.
   *
   * <p>Method under test: {@link FIXAcceptor#getServerChannel()}
   */
  @Test
  @DisplayName("Test getServerChannel()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"ServerSocketChannel FIXAcceptor.getServerChannel()"})
  void testGetServerChannel() throws IOException {
    // Arrange
    OrderEntryFactory orderEntry =
        new OrderEntryFactory(new InetSocketAddress("127.0.0.1", 12345));
    InetSocketAddress address = new InetSocketAddress("127.0.0.1", 0);
    Instruments instruments = createInstruments();
    FIXAcceptor acceptor = FIXAcceptor.open(orderEntry, address, "SENDER", instruments);

    // Act
    ServerSocketChannel serverChannel = acceptor.getServerChannel();

    // Assert
    assertTrue(serverChannel.isOpen());
    serverChannel.close();
  }

  /**
   * Test {@link FIXAcceptor#accept()}.
   *
   * <ul>
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link FIXAcceptor#accept()}
   */
  @Test
  @DisplayName("Test accept(); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Session FIXAcceptor.accept()"})
  void testAccept_thenReturnNull() throws IOException {
    // Arrange
    OrderEntryFactory orderEntry =
        new OrderEntryFactory(new InetSocketAddress("127.0.0.1", 12345));
    InetSocketAddress address = new InetSocketAddress("127.0.0.1", 0);
    Instruments instruments = createInstruments();
    FIXAcceptor acceptor = FIXAcceptor.open(orderEntry, address, "SENDER", instruments);

    // Act
    Session session = acceptor.accept();

    // Assert
    assertNull(session);
    acceptor.getServerChannel().close();
  }
}
