package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MarketDiffblueTest {
  /**
   * Test {@link Market#Market(MarketListener)}.
   *
   * <p>Method under test: {@link Market#Market(MarketListener)}
   */
  @Test
  @DisplayName("Test new Market(MarketListener)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Market.<init>(MarketListener)"})
  void testNewMarket() {
    // Arrange
    MarketEvents listener = new MarketEvents();

    // Act
    Market actualMarket = new Market(listener);

    // Assert
    MarketListener listener2 = actualMarket.getListener();
    assertTrue(listener2 instanceof MarketEvents);
    Long2ObjectArrayMap books = actualMarket.getBooks();
    assertTrue(books.isEmpty());
    assertEquals(books, actualMarket.getOrders());
    assertSame(listener, listener2);
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link Market#getBooks()}
   *   <li>{@link Market#getListener()}
   *   <li>{@link Market#getOrders()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Long2ObjectArrayMap Market.getBooks()",
    "MarketListener Market.getListener()",
    "Long2ObjectOpenHashMap Market.getOrders()"
  })
  void testGettersAndSetters() {
    // Arrange
    MarketEvents listener = new MarketEvents();
    Market market = new Market(listener);

    // Act
    Long2ObjectArrayMap actualBooks = market.getBooks();
    MarketListener actualListener = market.getListener();
    Long2ObjectOpenHashMap actualOrders = market.getOrders();

    // Assert
    assertTrue(actualListener instanceof MarketEvents);
    assertTrue(actualBooks.isEmpty());
    assertTrue(actualOrders.isEmpty());
    assertSame(listener, actualListener);
  }

  /**
   * Test {@link Market#open(long)}.
   *
   * <p>Method under test: {@link Market#open(long)}
   */
  @Test
  @DisplayName("Test open(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"OrderBook Market.open(long)"})
  void testOpen() {
    // Arrange
    Market market = new Market(new MarketEvents());

    // Act
    OrderBook actualOpenResult = market.open(1L);

    // Assert
    assertEquals(0L, actualOpenResult.getBestAskPrice());
    assertEquals(0L, actualOpenResult.getBestBidPrice());
    Long2ObjectArrayMap books = market.getBooks();
    assertEquals(1, books.size());
    assertEquals(1L, actualOpenResult.getInstrument());
    assertTrue(books.containsKey((Object) 1L));
    LongSortedSet askPrices = actualOpenResult.getAskPrices();
    assertTrue(askPrices.isEmpty());
    assertEquals(askPrices, actualOpenResult.getBidPrices());
  }

  /**
   * Test {@link Market#find(long)}.
   *
   * <ul>
   *   <li>When one.
   * </ul>
   *
   * <p>Method under test: {@link Market#find(long)}
   */
  @Test
  @DisplayName("Test find(long); when one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"com.paritytrading.parity.book.Order Market.find(long)"})
  void testFind_whenOne() {
    // Arrange, Act and Assert
    assertNull(new Market(new MarketEvents()).find(1L));
  }

  /**
   * Test {@link Market#find(long)}.
   *
   * <ul>
   *   <li>When zero.
   * </ul>
   *
   * <p>Method under test: {@link Market#find(long)}
   */
  @Test
  @DisplayName("Test find(long); when zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"com.paritytrading.parity.book.Order Market.find(long)"})
  void testFind_whenZero() {
    // Arrange, Act and Assert
    assertNull(new Market(new MarketEvents()).find(0L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code
   * price}.
   *
   * <ul>
   *   <li>When four.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenFour() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(4L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code
   * price}.
   *
   * <ul>
   *   <li>When one.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenOne() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(1L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code
   * price}.
   *
   * <ul>
   *   <li>When three.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenThree() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(3L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code
   * price}.
   *
   * <ul>
   *   <li>When two.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenTwo() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(2L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long, long)} with {@code orderId}, {@code quantity}, {@code
   * price}.
   *
   * <ul>
   *   <li>When zero.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long, long) with 'orderId', 'quantity', 'price'; when zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long, long)"})
  void testExecuteWithOrderIdQuantityPrice_whenZero() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(0L, 5L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   *
   * <ul>
   *   <li>When four.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenFour() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(4L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   *
   * <ul>
   *   <li>When one.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenOne() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(1L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   *
   * <ul>
   *   <li>When three.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenThree() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(3L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   *
   * <ul>
   *   <li>When two.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenTwo() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(2L, 5L));
  }

  /**
   * Test {@link Market#execute(long, long)} with {@code orderId}, {@code quantity}.
   *
   * <ul>
   *   <li>When zero.
   * </ul>
   *
   * <p>Method under test: {@link Market#execute(long, long)}
   */
  @Test
  @DisplayName("Test execute(long, long) with 'orderId', 'quantity'; when zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.execute(long, long)"})
  void testExecuteWithOrderIdQuantity_whenZero() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).execute(0L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   *
   * <ul>
   *   <li>When four.
   * </ul>
   *
   * <p>Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenFour() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).cancel(4L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   *
   * <ul>
   *   <li>When one.
   * </ul>
   *
   * <p>Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenOne() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).cancel(1L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   *
   * <ul>
   *   <li>When three.
   * </ul>
   *
   * <p>Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenThree() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).cancel(3L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   *
   * <ul>
   *   <li>When two.
   * </ul>
   *
   * <p>Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenTwo() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).cancel(2L, 5L));
  }

  /**
   * Test {@link Market#cancel(long, long)}.
   *
   * <ul>
   *   <li>When zero.
   * </ul>
   *
   * <p>Method under test: {@link Market#cancel(long, long)}
   */
  @Test
  @DisplayName("Test cancel(long, long); when zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long Market.cancel(long, long)"})
  void testCancel_whenZero() {
    // Arrange, Act and Assert
    assertEquals(0L, new Market(new MarketEvents()).cancel(0L, 5L));
  }
}
