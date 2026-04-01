package com.paritytrading.parity.fix;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.nassau.soupbintcp.SoupBinTCP;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClient;
import com.paritytrading.nassau.soupbintcp.SoupBinTCPClientStatusListener;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POEClientListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderEntryFactoryDiffblueTest {

  /**
   * Test {@link OrderEntryFactory#OrderEntryFactory(InetSocketAddress)}.
   *
   * <p>Method under test: {@link OrderEntryFactory#OrderEntryFactory(InetSocketAddress)}
   */
  @Test
  @DisplayName("Test new OrderEntryFactory(InetSocketAddress)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderEntryFactory.<init>(InetSocketAddress)"})
  void testNewOrderEntryFactory() {
    // Arrange and Act
    OrderEntryFactory actualOrderEntryFactory =
        new OrderEntryFactory(new InetSocketAddress("localhost", 8080));

    // Assert
    assertNotNull(actualOrderEntryFactory);
  }

  /**
   * Test {@link OrderEntryFactory#create(POEClientListener, SoupBinTCPClientStatusListener)}.
   *
   * <p>Method under test:
   * {@link OrderEntryFactory#create(POEClientListener, SoupBinTCPClientStatusListener)}
   */
  @Test
  @DisplayName("Test create(POEClientListener, SoupBinTCPClientStatusListener)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "SoupBinTCPClient OrderEntryFactory.create(POEClientListener, SoupBinTCPClientStatusListener)"
  })
  void testCreate() throws IOException {
    // Arrange
    ServerSocketChannel server = ServerSocketChannel.open();
    server.bind(new InetSocketAddress("localhost", 0));
    InetSocketAddress address = (InetSocketAddress) server.getLocalAddress();

    OrderEntryFactory factory = new OrderEntryFactory(address);

    POEClientListener listener = new POEClientListener() {
      @Override
      public void orderAccepted(POE.OrderAccepted message) throws IOException {}

      @Override
      public void orderRejected(POE.OrderRejected message) throws IOException {}

      @Override
      public void orderExecuted(POE.OrderExecuted message) throws IOException {}

      @Override
      public void orderCanceled(POE.OrderCanceled message) throws IOException {}
    };

    SoupBinTCPClientStatusListener statusListener = new SoupBinTCPClientStatusListener() {
      @Override
      public void heartbeatTimeout(SoupBinTCPClient client) throws IOException {}

      @Override
      public void loginAccepted(SoupBinTCPClient client, SoupBinTCP.LoginAccepted payload)
          throws IOException {}

      @Override
      public void loginRejected(SoupBinTCPClient client, SoupBinTCP.LoginRejected payload)
          throws IOException {}

      @Override
      public void endOfSession(SoupBinTCPClient client) throws IOException {}
    };

    // Act
    SoupBinTCPClient result = factory.create(listener, statusListener);

    // Assert
    assertNotNull(result);

    result.close();
    server.close();
  }
}
