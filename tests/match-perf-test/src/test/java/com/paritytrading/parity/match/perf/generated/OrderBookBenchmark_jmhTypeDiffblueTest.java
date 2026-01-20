package com.paritytrading.parity.match.perf.generated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderBookBenchmark_jmhTypeDiffblueTest {
  /**
   * Test new {@link OrderBookBenchmark_jmhType} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link
   * OrderBookBenchmark_jmhType}
   */
  @Test
  @DisplayName("Test new OrderBookBenchmark_jmhType (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBookBenchmark_jmhType.<init>()"})
  void testNewOrderBookBenchmark_jmhType() {
    // Arrange and Act
    OrderBookBenchmark_jmhType actualOrderBookBenchmark_jmhType = new OrderBookBenchmark_jmhType();

    // Assert
    assertNull(actualOrderBookBenchmark_jmhType.getBook());
    assertEquals(0L, actualOrderBookBenchmark_jmhType.getNextOrderId());
  }
}
