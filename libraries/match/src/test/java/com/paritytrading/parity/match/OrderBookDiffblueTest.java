package com.paritytrading.parity.match;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.TreeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderBookDiffblueTest {
  /**
   * Test {@link OrderBook#OrderBook(OrderBookListener)}.
   *
   * <p>Method under test: {@link OrderBook#OrderBook(OrderBookListener)}
   */
  @Test
  @DisplayName("Test new OrderBook(OrderBookListener)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBook.<init>(OrderBookListener)"})
  void testNewOrderBook() {
    // Arrange
    OrderBookEvents listener = new OrderBookEvents();

    // Act
    OrderBook actualOrderBook = new OrderBook(listener);

    // Assert
    OrderBookListener listener2 = actualOrderBook.getListener();
    assertTrue(listener2 instanceof OrderBookEvents);
    assertEquals(0L, actualOrderBook.getNextOrderNumber());
    assertTrue(actualOrderBook.getOrders().isEmpty());
    TreeSet asks = actualOrderBook.getAsks();
    assertTrue(asks.isEmpty());
    assertEquals(asks, actualOrderBook.getBids());
    assertSame(listener, listener2);
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link OrderBook#getAsks()}
   *   <li>{@link OrderBook#getBids()}
   *   <li>{@link OrderBook#getListener()}
   *   <li>{@link OrderBook#getNextOrderNumber()}
   *   <li>{@link OrderBook#getOrders()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "TreeSet OrderBook.getAsks()",
    "TreeSet OrderBook.getBids()",
    "OrderBookListener OrderBook.getListener()",
    "long OrderBook.getNextOrderNumber()",
    "Long2ObjectOpenHashMap OrderBook.getOrders()"
  })
  void testGettersAndSetters() {
    // Arrange
    OrderBookEvents listener = new OrderBookEvents();
    OrderBook orderBook = new OrderBook(listener);

    // Act
    TreeSet actualAsks = orderBook.getAsks();
    TreeSet actualBids = orderBook.getBids();
    OrderBookListener actualListener = orderBook.getListener();
    long actualNextOrderNumber = orderBook.getNextOrderNumber();

    // Assert
    assertTrue(actualListener instanceof OrderBookEvents);
    assertEquals(0L, actualNextOrderNumber);
    assertTrue(orderBook.getOrders().isEmpty());
    assertTrue(actualAsks.isEmpty());
    assertTrue(actualBids.isEmpty());
    assertSame(listener, actualListener);
  }

  /**
   * Test {@link OrderBook#enter(long, Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(OrderBookListener)} with listener is {@link
   *       OrderBookEvents#OrderBookEvents()} Orders one Number is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test enter(long, Side, long, long); then OrderBook(OrderBookListener) with listener is OrderBookEvents() Orders one Number is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBook.enter(long, Side, long, long)"})
  void testEnter_thenOrderBookWithListenerIsOrderBookEventsOrdersOneNumberIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(new OrderBookEvents());

    // Act
    orderBook.enter(1L, Side.BUY, 1L, 3L);

    // Assert
    Long2ObjectOpenHashMap orders = orderBook.getOrders();
    assertEquals(1, orders.size());
    Object getResult = orders.get((Object) 1L);
    assertEquals(0L, ((Order) getResult).getNumber());
    assertEquals(1, orderBook.getBids().size());
    assertEquals(1L, ((Order) getResult).getId());
    assertEquals(1L, ((Order) getResult).getPrice());
    assertEquals(3L, ((Order) getResult).getRemainingQuantity());
    assertEquals(Side.BUY, ((Order) getResult).getSide());
    assertTrue(orderBook.getAsks().isEmpty());
  }

  /**
   * Test {@link OrderBook#enter(long, Side, long, long)}.
   *
   * <ul>
   *   <li>When {@code SELL}.
   *   <li>Then {@link OrderBook#OrderBook(OrderBookListener)} with listener is {@link
   *       OrderBookEvents#OrderBookEvents()} Asks size is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test enter(long, Side, long, long); when 'SELL'; then OrderBook(OrderBookListener) with listener is OrderBookEvents() Asks size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBook.enter(long, Side, long, long)"})
  void testEnter_whenSell_thenOrderBookWithListenerIsOrderBookEventsAsksSizeIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(new OrderBookEvents());

    // Act
    orderBook.enter(1L, Side.SELL, 1L, 3L);

    // Assert
    Long2ObjectOpenHashMap orders = orderBook.getOrders();
    assertEquals(1, orders.size());
    assertEquals(1, orderBook.getAsks().size());
    assertEquals(Side.SELL, ((Order) orders.get((Object) 1L)).getSide());
    assertTrue(orderBook.getBids().isEmpty());
  }

  /**
   * Test {@link OrderBook#enter(long, Side, long, long)}.
   *
   * <ul>
   *   <li>When zero.
   *   <li>Then {@link OrderBook#OrderBook(OrderBookListener)} with listener is {@link
   *       OrderBookEvents#OrderBookEvents()} Orders zero Id is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test enter(long, Side, long, long); when zero; then OrderBook(OrderBookListener) with listener is OrderBookEvents() Orders zero Id is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBook.enter(long, Side, long, long)"})
  void testEnter_whenZero_thenOrderBookWithListenerIsOrderBookEventsOrdersZeroIdIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(new OrderBookEvents());

    // Act
    orderBook.enter(0L, Side.BUY, 1L, 3L);

    // Assert
    Long2ObjectOpenHashMap orders = orderBook.getOrders();
    assertEquals(1, orders.size());
    Object getResult = orders.get((Object) 0L);
    assertEquals(0L, ((Order) getResult).getId());
    assertEquals(0L, ((Order) getResult).getNumber());
    assertEquals(1L, ((Order) getResult).getPrice());
    assertEquals(3L, ((Order) getResult).getRemainingQuantity());
    assertEquals(Side.BUY, ((Order) getResult).getSide());
  }
}
