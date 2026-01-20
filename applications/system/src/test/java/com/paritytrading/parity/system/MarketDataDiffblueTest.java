package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MarketDataDiffblueTest {
  /**
   * Test {@link MarketData#MarketData(MoldUDP64Server, MoldUDP64RequestServer)}.
   *
   * <p>Method under test: {@link MarketData#MarketData(MoldUDP64Server, MoldUDP64RequestServer)}
   */
  @Test
  @DisplayName("Test new MarketData(MoldUDP64Server, MoldUDP64RequestServer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketData.<init>(MoldUDP64Server, MoldUDP64RequestServer)"})
  void testNewMarketData() {
    // Arrange
    MoldUDP64Server transport = new MoldUDP64Server(null, "Session");
    MoldUDP64RequestServer requestTransport = new MoldUDP64RequestServer(null);

    // Act
    MarketData actualMarketData = new MarketData(transport, requestTransport);

    // Assert
    assertSame(requestTransport, actualMarketData.getRequestTransport());
    assertSame(transport, actualMarketData.getTransport());
  }
}
