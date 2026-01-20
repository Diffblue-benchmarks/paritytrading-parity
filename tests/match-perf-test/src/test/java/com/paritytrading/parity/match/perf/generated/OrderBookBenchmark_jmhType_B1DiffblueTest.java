package com.paritytrading.parity.match.perf.generated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderBookBenchmark_jmhType_B1DiffblueTest {
  /**
   * Test new {@link OrderBookBenchmark_jmhType_B1} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link
   * OrderBookBenchmark_jmhType_B1}
   */
  @Test
  @DisplayName("Test new OrderBookBenchmark_jmhType_B1 (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBookBenchmark_jmhType_B1.<init>()"})
  void testNewOrderBookBenchmark_jmhType_B1() {
    // Arrange and Act
    OrderBookBenchmark_jmhType_B1 actualOrderBookBenchmark_jmhType_B1 =
        new OrderBookBenchmark_jmhType_B1();

    // Assert
    assertNull(actualOrderBookBenchmark_jmhType_B1.getBook());
    assertEquals(0L, actualOrderBookBenchmark_jmhType_B1.getNextOrderId());
  }
}
