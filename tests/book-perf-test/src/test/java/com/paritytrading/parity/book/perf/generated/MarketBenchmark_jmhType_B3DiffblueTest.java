package com.paritytrading.parity.book.perf.generated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MarketBenchmark_jmhType_B3DiffblueTest {
  /**
   * Test new {@link MarketBenchmark_jmhType_B3} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link
   * MarketBenchmark_jmhType_B3}
   */
  @Test
  @DisplayName("Test new MarketBenchmark_jmhType_B3 (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark_jmhType_B3.<init>()"})
  void testNewMarketBenchmark_jmhType_B3() {
    // Arrange and Act
    MarketBenchmark_jmhType_B3 actualMarketBenchmark_jmhType_B3 = new MarketBenchmark_jmhType_B3();

    // Assert
    assertNull(actualMarketBenchmark_jmhType_B3.getMarket());
    assertEquals(0L, actualMarketBenchmark_jmhType_B3.getNextOrderId());
  }
}
