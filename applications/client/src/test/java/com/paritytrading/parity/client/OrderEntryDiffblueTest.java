package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderEntryDiffblueTest {

  /**
   * Test {@link OrderEntry.Receiver#run()}.
   *
   * <ul>
   *   <li>When {@link OrderEntry#close()} is called; then receiver loop exits and transport is
   *       closed.
   * </ul>
   *
   * <p>Method under test: {@code void OrderEntry$Receiver.run()}
   */
  @Test
  @DisplayName(
      "Test Receiver run(); when OrderEntry is closed; then receiver loop exits and transport is closed")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderEntry$Receiver.run()"})
  void testReceiverRun_whenOrderEntryClosed_thenTransportClosed() throws Exception {
    // Arrange
    ServerSocketChannel server = ServerSocketChannel.open();
    server.bind(new InetSocketAddress("localhost", 0));
    int port = ((InetSocketAddress) server.getLocalAddress()).getPort();

    POEClientListener listener = mock(POEClientListener.class);
    OrderEntry orderEntry =
        OrderEntry.open(new InetSocketAddress("localhost", port), listener);
    SocketChannel serverSide = server.accept();

    // Act
    orderEntry.close();
    Thread.sleep(500);

    // Assert - receiver has exited and closed the transport; send() should throw
    assertThrows(IOException.class, () -> orderEntry.send(new POE.EnterOrder()));

    // Cleanup
    serverSide.close();
    server.close();
  }

  /**
   * Test {@link OrderEntry.Receiver#run()}.
   *
   * <ul>
   *   <li>When server closes the connection (EOF); then receiver detects it, breaks loop, and
   *       closes transport.
   * </ul>
   *
   * <p>Method under test: {@code void OrderEntry$Receiver.run()}
   */
  @Test
  @DisplayName(
      "Test Receiver run(); when server closes connection; then receiver detects EOF and exits")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderEntry$Receiver.run()"})
  void testReceiverRun_whenServerClosesConnection_thenReceiverExits() throws Exception {
    // Arrange
    ServerSocketChannel server = ServerSocketChannel.open();
    server.bind(new InetSocketAddress("localhost", 0));
    int port = ((InetSocketAddress) server.getLocalAddress()).getPort();

    POEClientListener listener = mock(POEClientListener.class);
    OrderEntry orderEntry =
        OrderEntry.open(new InetSocketAddress("localhost", port), listener);
    SocketChannel serverSide = server.accept();

    // Act - closing server side triggers EOF on client, causing transport.receive() < 0
    serverSide.close();
    Thread.sleep(500);

    // Assert - receiver has exited and closed the transport; send() should throw
    assertThrows(IOException.class, () -> orderEntry.send(new POE.EnterOrder()));

    // Cleanup
    server.close();
  }
}
