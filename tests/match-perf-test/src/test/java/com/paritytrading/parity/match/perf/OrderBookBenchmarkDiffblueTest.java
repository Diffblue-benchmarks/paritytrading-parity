package com.paritytrading.parity.match.perf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.match.OrderBook;
import com.paritytrading.parity.match.Side;
import java.util.TreeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderBookBenchmarkDiffblueTest {
  @Mock private OrderBook orderBook;

  @InjectMocks private OrderBookBenchmark orderBookBenchmark;

  /**
   * Test {@link OrderBookBenchmark#prepare()}.
   *
   * <p>Method under test: {@link OrderBookBenchmark#prepare()}
   */
  @Test
  @DisplayName("Test prepare()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBookBenchmark.prepare()"})
  void testPrepare() {
    // Arrange
    OrderBookBenchmark orderBookBenchmark = new OrderBookBenchmark();

    // Act
    orderBookBenchmark.prepare();

    // Assert
    OrderBook book = orderBookBenchmark.getBook();
    assertEquals(0L, book.getNextOrderNumber());
    assertTrue(book.getOrders().isEmpty());
    TreeSet asks = book.getAsks();
    assertTrue(asks.isEmpty());
    assertEquals(asks, book.getBids());
  }

  /**
   * Test {@link OrderBookBenchmark#enter()}.
   *
   * <p>Method under test: {@link OrderBookBenchmark#enter()}
   */
  @Test
  @DisplayName("Test enter()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBookBenchmark.enter()"})
  void testEnter() {
    // Arrange
    doNothing().when(orderBook).enter(anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    orderBookBenchmark.enter();

    // Assert
    verify(orderBook).enter(0L, Side.BUY, 100000L, 100L);
    assertEquals(1L, orderBookBenchmark.getNextOrderId());
  }

  /**
   * Test {@link OrderBookBenchmark#enterAndCancel()}.
   *
   * <p>Method under test: {@link OrderBookBenchmark#enterAndCancel()}
   */
  @Test
  @DisplayName("Test enterAndCancel()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBookBenchmark.enterAndCancel()"})
  void testEnterAndCancel() {
    // Arrange
    doNothing().when(orderBook).cancel(anyLong(), anyLong());
    doNothing().when(orderBook).enter(anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    orderBookBenchmark.enterAndCancel();

    // Assert
    verify(orderBook).cancel(0L, 0L);
    verify(orderBook).enter(0L, Side.BUY, 100000L, 100L);
    assertEquals(1L, orderBookBenchmark.getNextOrderId());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link OrderBookBenchmark}
   *   <li>{@link OrderBookBenchmark#getBook()}
   *   <li>{@link OrderBookBenchmark#getNextOrderId()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OrderBookBenchmark.<init>()",
    "OrderBook OrderBookBenchmark.getBook()",
    "long OrderBookBenchmark.getNextOrderId()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    OrderBookBenchmark actualOrderBookBenchmark = new OrderBookBenchmark();
    OrderBook actualBook = actualOrderBookBenchmark.getBook();

    // Assert
    assertNull(actualBook);
    assertEquals(0L, actualOrderBookBenchmark.getNextOrderId());
  }
}
