package com.paritytrading.parity.book.perf.generated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MarketBenchmark_jmhTypeDiffblueTest {
  /**
   * Test new {@link MarketBenchmark_jmhType} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link MarketBenchmark_jmhType}
   */
  @Test
  @DisplayName("Test new MarketBenchmark_jmhType (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark_jmhType.<init>()"})
  void testNewMarketBenchmark_jmhType() {
    // Arrange and Act
    MarketBenchmark_jmhType actualMarketBenchmark_jmhType = new MarketBenchmark_jmhType();

    // Assert
    assertNull(actualMarketBenchmark_jmhType.getMarket());
    assertEquals(0L, actualMarketBenchmark_jmhType.getNextOrderId());
  }
}
