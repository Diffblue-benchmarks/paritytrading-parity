package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MarketDiffblueTest {
  /**
   * Test {@link Market#Market(MarketListener)}.
   * <p>
   * Method under test: {@link Market#Market(MarketListener)}
   */
  @Test
  @DisplayName("Test new Market(MarketListener)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Market.<init>(MarketListener)"})
  void testNewMarket() {
    // Arrange and Act
    Market actualMarket = new Market(new MarketEvents());

    // Assert
    assertNull(actualMarket.find(1L));
    OrderBook openResult = actualMarket.open(1L);
    assertEquals(0L, openResult.getBestAskPrice());
    assertEquals(0L, openResult.getBestBidPrice());
    assertEquals(1L, openResult.getInstrument());
    LongSortedSet askPrices = openResult.getAskPrices();
    assertTrue(askPrices.isEmpty());
    assertEquals(askPrices, openResult.getBidPrices());
  }

  /**
   * Test {@link Market#open(long)}.
   * <p>
   * Method under test: {@link Market#open(long)}
   */
  @Test
  @DisplayName("Test open(long)")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"OrderBook Market.open(long)"})
  void testOpen() {
    // Arrange and Act
    OrderBook actualOpenResult = (new Market(new MarketEvents())).open(1L);

    // Assert
    assertEquals(0L, actualOpenResult.getBestAskPrice());
    assertEquals(0L, actualOpenResult.getBestBidPrice());
    assertEquals(1L, actualOpenResult.getInstrument());
    LongSortedSet askPrices = actualOpenResult.getAskPrices();
    assertTrue(askPrices.isEmpty());
    assertEquals(askPrices, actualOpenResult.getBidPrices());
  }

  /**
   * Test {@link Market#find(long)}.
   * <ul>
   *   <li>When one.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#find(long)}
   */
  @Test
  @DisplayName("Test find(long); when one")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"com.paritytrading.parity.book.Order Market.find(long)"})
  void testFind_whenOne() {
    // Arrange, Act and Assert
    assertNull((new Market(new MarketEvents())).find(1L));
  }

  /**
   * Test {@link Market#find(long)}.
   * <ul>
   *   <li>When zero.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#find(long)}
   */
  @Test
  @DisplayName("Test find(long); when zero")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"com.paritytrading.parity.book.Order Market.find(long)"})
  void testFind_whenZero() {
    // Arrange, Act and Assert
    assertNull((new Market(new MarketEvents())).find(0L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code price}.
   * <ul>
   *   <li>When four.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when four")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenFour() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(4L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code price}.
   * <ul>
   *   <li>When one.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when one")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenOne() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(1L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code price}.
   * <ul>
   *   <li>When three.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when three")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenThree() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(3L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code price}.
   * <ul>
   *   <li>When two.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when two")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenTwo() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(2L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code price}.
   * <ul>
   *   <li>When zero.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when zero")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenZero() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(0L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   * <ul>
   *   <li>When four.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when four")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenFour() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(4L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   * <ul>
   *   <li>When one.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when one")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenOne() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(1L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   * <ul>
   *   <li>When three.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when three")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenThree() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(3L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   * <ul>
   *   <li>When two.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when two")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenTwo() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(2L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   * <ul>
   *   <li>When zero.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when zero")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenZero() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).execute(0L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   * <ul>
   *   <li>When four.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when four")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenFour() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).cancel(4L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   * <ul>
   *   <li>When one.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when one")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenOne() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).cancel(1L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   * <ul>
   *   <li>When three.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when three")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenThree() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).cancel(3L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   * <ul>
   *   <li>When two.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when two")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenTwo() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).cancel(2L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   * <ul>
   *   <li>When zero.</li>
   * </ul>
   * <p>
   * Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when zero")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenZero() {
    // Arrange, Act and Assert
    assertEquals(0L, (new Market(new MarketEvents())).cancel(0L, 5L));
  }
}
