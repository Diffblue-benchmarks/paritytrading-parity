package com.paritytrading.parity.system;

import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.nassau.moldudp64.MoldUDP64RequestServer;
import com.paritytrading.nassau.moldudp64.MoldUDP64Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MarketReportingDiffblueTest {
  /**
   * Test {@link MarketReporting#MarketReporting(MoldUDP64Server, MoldUDP64RequestServer)}.
   *
   * <p>Method under test: {@link MarketReporting#MarketReporting(MoldUDP64Server,
   * MoldUDP64RequestServer)}
   */
  @Test
  @DisplayName("Test new MarketReporting(MoldUDP64Server, MoldUDP64RequestServer)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketReporting.<init>(MoldUDP64Server, MoldUDP64RequestServer)"})
  void testNewMarketReporting() {
    // Arrange
    MoldUDP64Server transport = new MoldUDP64Server(null, "Session");
    MoldUDP64RequestServer requestTransport = new MoldUDP64RequestServer(null);

    // Act
    MarketReporting actualMarketReporting = new MarketReporting(transport, requestTransport);

    // Assert
    assertSame(requestTransport, actualMarketReporting.getRequestTransport());
    assertSame(transport, actualMarketReporting.getTransport());
  }
}
