package com.paritytrading.parity.book.perf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketBenchmarkDiffblueTest {
  @Mock private Market market;

  @InjectMocks private MarketBenchmark marketBenchmark;

  /**
   * Test {@link MarketBenchmark#prepare()}.
   *
   * <p>Method under test: {@link MarketBenchmark#prepare()}
   */
  @Test
  @DisplayName("Test prepare()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.prepare()"})
  void testPrepare() {
    // Arrange
    MarketBenchmark marketBenchmark = new MarketBenchmark();

    // Act
    marketBenchmark.prepare();

    // Assert
    Market market = marketBenchmark.getMarket();
    Long2ObjectArrayMap books = market.getBooks();
    assertEquals(1, books.size());
    Object getResult = books.get((Object) 1L);
    assertEquals(0L, ((OrderBook) getResult).getBestAskPrice());
    assertEquals(0L, ((OrderBook) getResult).getBestBidPrice());
    assertEquals(1L, ((OrderBook) getResult).getInstrument());
    assertTrue(market.getOrders().isEmpty());
    LongSortedSet askPrices = ((OrderBook) getResult).getAskPrices();
    assertTrue(askPrices.isEmpty());
    assertEquals(askPrices, ((OrderBook) getResult).getBidPrices());
  }

  /**
   * Test {@link MarketBenchmark#add()}.
   *
   * <p>Method under test: {@link MarketBenchmark#add()}
   */
  @Test
  @DisplayName("Test add()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.add()"})
  void testAdd() {
    // Arrange
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    marketBenchmark.add();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    assertEquals(1L, marketBenchmark.getNextOrderId());
  }

  /**
   * Test {@link MarketBenchmark#addAndModify()}.
   *
   * <p>Method under test: {@link MarketBenchmark#addAndModify()}
   */
  @Test
  @DisplayName("Test addAndModify()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.addAndModify()"})
  void testAddAndModify() {
    // Arrange
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());
    doNothing().when(market).modify(anyLong(), anyLong());

    // Act
    marketBenchmark.addAndModify();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    verify(market).modify(0L, 0L);
    assertEquals(1L, marketBenchmark.getNextOrderId());
  }

  /**
   * Test {@link MarketBenchmark#addAndExecute()}.
   *
   * <p>Method under test: {@link MarketBenchmark#addAndExecute()}
   */
  @Test
  @DisplayName("Test addAndExecute()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.addAndExecute()"})
  void testAddAndExecute() {
    // Arrange
    when(market.execute(anyLong(), anyLong())).thenReturn(1L);
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    marketBenchmark.addAndExecute();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    verify(market).execute(0L, 100L);
    assertEquals(1L, marketBenchmark.getNextOrderId());
  }

  /**
   * Test {@link MarketBenchmark#addAndCancel()}.
   *
   * <p>Method under test: {@link MarketBenchmark#addAndCancel()}
   */
  @Test
  @DisplayName("Test addAndCancel()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.addAndCancel()"})
  void testAddAndCancel() {
    // Arrange
    when(market.cancel(anyLong(), anyLong())).thenReturn(1L);
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    marketBenchmark.addAndCancel();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    verify(market).cancel(0L, 100L);
    assertEquals(1L, marketBenchmark.getNextOrderId());
  }

  /**
   * Test {@link MarketBenchmark#addAndDelete()}.
   *
   * <p>Method under test: {@link MarketBenchmark#addAndDelete()}
   */
  @Test
  @DisplayName("Test addAndDelete()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.addAndDelete()"})
  void testAddAndDelete() {
    // Arrange
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());
    doNothing().when(market).delete(anyLong());

    // Act
    marketBenchmark.addAndDelete();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    verify(market).delete(0L);
    assertEquals(1L, marketBenchmark.getNextOrderId());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link MarketBenchmark}
   *   <li>{@link MarketBenchmark#getMarket()}
   *   <li>{@link MarketBenchmark#getNextOrderId()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MarketBenchmark.<init>()",
    "Market MarketBenchmark.getMarket()",
    "long MarketBenchmark.getNextOrderId()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    MarketBenchmark actualMarketBenchmark = new MarketBenchmark();
    Market actualMarket = actualMarketBenchmark.getMarket();

    // Assert
    assertNull(actualMarket);
    assertEquals(0L, actualMarketBenchmark.getNextOrderId());
  }
}
