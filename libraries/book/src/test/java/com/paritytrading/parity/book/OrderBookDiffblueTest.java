package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderBookDiffblueTest {
  /**
   * Test {@link OrderBook#OrderBook(long)}.
   *
   * <p>Method under test: {@link OrderBook#OrderBook(long)}
   */
  @Test
  @DisplayName("Test new OrderBook(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBook.<init>(long)"})
  void testNewOrderBook() {
    // Arrange and Act
    OrderBook actualOrderBook = new OrderBook(1L);

    // Assert
    assertEquals(0L, actualOrderBook.getBestAskPrice());
    assertEquals(0L, actualOrderBook.getBestBidPrice());
    assertEquals(1L, actualOrderBook.getInstrument());
    LongSortedSet askPrices = actualOrderBook.getAskPrices();
    assertTrue(askPrices.isEmpty());
    assertEquals(askPrices, actualOrderBook.getBidPrices());
  }

  /**
   * Test {@link OrderBook#getInstrument()}.
   *
   * <p>Method under test: {@link OrderBook#getInstrument()}
   */
  @Test
  @DisplayName("Test getInstrument()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getInstrument()"})
  void testGetInstrument() {
    // Arrange, Act and Assert
    assertEquals(1L, new OrderBook(1L).getInstrument());
  }

  /**
   * Test {@link OrderBook#getBestBidPrice()}.
   *
   * <ul>
   *   <li>Then return one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBestBidPrice()}
   */
  @Test
  @DisplayName("Test getBestBidPrice(); then return one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBestBidPrice()"})
  void testGetBestBidPrice_thenReturnOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#getBestBidPrice()}.
   *
   * <ul>
   *   <li>Then return zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBestBidPrice()}
   */
  @Test
  @DisplayName("Test getBestBidPrice(); then return zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBestBidPrice()"})
  void testGetBestBidPrice_thenReturnZero() {
    // Arrange, Act and Assert
    assertEquals(0L, new OrderBook(1L).getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#getBidPrices()}.
   *
   * <p>Method under test: {@link OrderBook#getBidPrices()}
   */
  @Test
  @DisplayName("Test getBidPrices()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"LongSortedSet OrderBook.getBidPrices()"})
  void testGetBidPrices() {
    // Arrange, Act and Assert
    assertTrue(new OrderBook(1L).getBidPrices().isEmpty());
  }

  /**
   * Test {@link OrderBook#getBidSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is minus one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  @DisplayName(
      "Test getBidSize(long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBidSize(long)"})
  void testGetBidSize_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsMinusOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getBidSize(1L));
  }

  /**
   * Test {@link OrderBook#getBidSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  @DisplayName(
      "Test getBidSize(long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBidSize(long)"})
  void testGetBidSize_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getBidSize(1L));
  }

  /**
   * Test {@link OrderBook#getBidSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is one
   *       and quantity is one.
   *   <li>Then return one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  @DisplayName(
      "Test getBidSize(long); given add(Side, long, long) with side is 'BUY' and price is one and quantity is one; then return one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBidSize(long)"})
  void testGetBidSize_givenAddWithSideIsBuyAndPriceIsOneAndQuantityIsOne_thenReturnOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getBidSize(1L));
  }

  /**
   * Test {@link OrderBook#getBidSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is zero
   *       and quantity is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  @DisplayName(
      "Test getBidSize(long); given add(Side, long, long) with side is 'BUY' and price is zero and quantity is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBidSize(long)"})
  void testGetBidSize_givenAddWithSideIsBuyAndPriceIsZeroAndQuantityIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getBidSize(1L));
  }

  /**
   * Test {@link OrderBook#getBidSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is zero
   *       and quantity is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  @DisplayName(
      "Test getBidSize(long); given add(Side, long, long) with side is 'BUY' and price is zero and quantity is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBidSize(long)"})
  void testGetBidSize_givenAddWithSideIsBuyAndPriceIsZeroAndQuantityIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 0L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getBidSize(1L));
  }

  /**
   * Test {@link OrderBook#getBidSize(long)}.
   *
   * <ul>
   *   <li>Then return zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  @DisplayName("Test getBidSize(long); then return zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBidSize(long)"})
  void testGetBidSize_thenReturnZero() {
    // Arrange, Act and Assert
    assertEquals(0L, new OrderBook(1L).getBidSize(1L));
  }

  /**
   * Test {@link OrderBook#getBestAskPrice()}.
   *
   * <ul>
   *   <li>Then return one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBestAskPrice()}
   */
  @Test
  @DisplayName("Test getBestAskPrice(); then return one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBestAskPrice()"})
  void testGetBestAskPrice_thenReturnOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getBestAskPrice());
  }

  /**
   * Test {@link OrderBook#getBestAskPrice()}.
   *
   * <ul>
   *   <li>Then return zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getBestAskPrice()}
   */
  @Test
  @DisplayName("Test getBestAskPrice(); then return zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getBestAskPrice()"})
  void testGetBestAskPrice_thenReturnZero() {
    // Arrange, Act and Assert
    assertEquals(0L, new OrderBook(1L).getBestAskPrice());
  }

  /**
   * Test {@link OrderBook#getAskPrices()}.
   *
   * <p>Method under test: {@link OrderBook#getAskPrices()}
   */
  @Test
  @DisplayName("Test getAskPrices()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"LongSortedSet OrderBook.getAskPrices()"})
  void testGetAskPrices() {
    // Arrange, Act and Assert
    assertTrue(new OrderBook(1L).getAskPrices().isEmpty());
  }

  /**
   * Test {@link OrderBook#getAskSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code SELL} and price is
   *       {@link Long#MAX_VALUE} and quantity is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  @DisplayName(
      "Test getAskSize(long); given add(Side, long, long) with side is 'SELL' and price is MAX_VALUE and quantity is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getAskSize(long)"})
  void testGetAskSize_givenAddWithSideIsSellAndPriceIsMax_valueAndQuantityIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, Long.MAX_VALUE, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getAskSize(1L));
  }

  /**
   * Test {@link OrderBook#getAskSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code SELL} and price is
   *       {@link Long#MAX_VALUE} and quantity is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  @DisplayName(
      "Test getAskSize(long); given add(Side, long, long) with side is 'SELL' and price is MAX_VALUE and quantity is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getAskSize(long)"})
  void testGetAskSize_givenAddWithSideIsSellAndPriceIsMax_valueAndQuantityIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, Long.MAX_VALUE, 0L);
    orderBook.add(Side.SELL, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getAskSize(1L));
  }

  /**
   * Test {@link OrderBook#getAskSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code SELL} and price is one
   *       and quantity is one.
   *   <li>Then return one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  @DisplayName(
      "Test getAskSize(long); given add(Side, long, long) with side is 'SELL' and price is one and quantity is one; then return one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getAskSize(long)"})
  void testGetAskSize_givenAddWithSideIsSellAndPriceIsOneAndQuantityIsOne_thenReturnOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getAskSize(1L));
  }

  /**
   * Test {@link OrderBook#getAskSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code SELL} and price is zero
   *       and quantity is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  @DisplayName(
      "Test getAskSize(long); given add(Side, long, long) with side is 'SELL' and price is zero and quantity is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getAskSize(long)"})
  void testGetAskSize_givenAddWithSideIsSellAndPriceIsZeroAndQuantityIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 0L, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getAskSize(1L));
  }

  /**
   * Test {@link OrderBook#getAskSize(long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code SELL} and price is zero
   *       and quantity is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  @DisplayName(
      "Test getAskSize(long); given add(Side, long, long) with side is 'SELL' and price is zero and quantity is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getAskSize(long)"})
  void testGetAskSize_givenAddWithSideIsSellAndPriceIsZeroAndQuantityIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 0L, 0L);
    orderBook.add(Side.SELL, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getAskSize(1L));
  }

  /**
   * Test {@link OrderBook#getAskSize(long)}.
   *
   * <ul>
   *   <li>Then return zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  @DisplayName("Test getAskSize(long); then return zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long OrderBook.getAskSize(long)"})
  void testGetAskSize_thenReturnZero() {
    // Arrange, Act and Assert
    assertEquals(0L, new OrderBook(1L).getAskSize(1L));
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsOne2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is minus
   *       one and quantity is minus one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is minus one and quantity is minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_givenAddWithSideIsBuyAndPriceIsMinusOneAndQuantityIsMinusOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1L, orderBook.getBestBidPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertTrue(actualAddResult);
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is zero
   *       and quantity is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is zero and quantity is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_givenAddWithSideIsBuyAndPriceIsZeroAndQuantityIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 0L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1L, orderBook.getBestBidPrice());
    assertEquals(2, orderBook.getBidPrices().size());
    assertTrue(actualAddResult);
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is four.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_thenOrderBookWithInstrumentIsOneBidPricesSizeIsFour() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(4, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_thenOrderBookWithInstrumentIsOneBidPricesSizeIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1, orderBook.getBidPrices().size());
    assertEquals(1L, orderBook.getBestBidPrice());
    assertTrue(actualAddResult);
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is three.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_thenOrderBookWithInstrumentIsOneBidPricesSizeIsThree() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is two.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_thenOrderBookWithInstrumentIsOneBidPricesSizeIsTwo() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1L, orderBook.getBestBidPrice());
    assertEquals(2, orderBook.getBidPrices().size());
    assertTrue(actualAddResult);
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>When {@code BUY}.
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); when 'BUY'; then OrderBook(long) with instrument is one BidPrices size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_whenBuy_thenOrderBookWithInstrumentIsOneBidPricesSizeIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1, orderBook.getBidPrices().size());
    assertEquals(1L, orderBook.getBestBidPrice());
    assertTrue(actualAddResult);
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>When minus one.
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is four.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); when minus one; then OrderBook(long) with instrument is one BidPrices size is four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_whenMinusOne_thenOrderBookWithInstrumentIsOneBidPricesSizeIsFour() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, -1L, 1L);

    // Assert
    assertEquals(4, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>When minus one.
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is three.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); when minus one; then OrderBook(long) with instrument is one BidPrices size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_whenMinusOne_thenOrderBookWithInstrumentIsOneBidPricesSizeIsThree() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, -1L, 1L);

    // Assert
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#add(Side, long, long)}.
   *
   * <ul>
   *   <li>When {@code SELL}.
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one AskPrices size is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test add(Side, long, long); when 'SELL'; then OrderBook(long) with instrument is one AskPrices size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.add(Side, long, long)"})
  void testAdd_whenSell_thenOrderBookWithInstrumentIsOneAskPricesSizeIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);

    // Act
    orderBook.add(Side.SELL, 1L, 1L);

    // Assert
    assertEquals(1, orderBook.getAskPrices().size());
    assertEquals(1L, orderBook.getBestAskPrice());
    assertTrue(orderBook.getBidPrices().isEmpty());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is minus one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsMinusOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is two.
   *   <li>When minus one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is two; when minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsTwo_whenMinusOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 2L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, -1L);

    // Assert
    assertEquals(2, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is two.
   *   <li>When minus one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is two; when minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsTwo_whenMinusOne2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 2L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, -1L);

    // Assert
    assertEquals(2, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is
   *       {@link Long#MAX_VALUE} and quantity is two.
   *   <li>When minus one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is MAX_VALUE and quantity is two; when minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsMax_valueAndQuantityIsTwo_whenMinusOne3() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 0L);
    orderBook.add(Side.BUY, 1L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 2L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, -1L);

    // Assert
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is minus
   *       one and quantity is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is minus one and quantity is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsMinusOneAndQuantityIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, 0L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1L, orderBook.getBestBidPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is minus
   *       one and quantity is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is minus one and quantity is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsMinusOneAndQuantityIsZero2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, 0L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(4, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is one
   *       and quantity is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is one and quantity is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsOneAndQuantityIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2, orderBook.getBidPrices().size());
    assertEquals(2L, orderBook.getBestBidPrice());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link OrderBook#add(Side, long, long)} with side is {@code BUY} and price is one
   *       and quantity is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); given add(Side, long, long) with side is 'BUY' and price is one and quantity is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_givenAddWithSideIsBuyAndPriceIsOneAndQuantityIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 0L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 1L, -1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(-1L, orderBook.getBestBidPrice());
    assertEquals(1, orderBook.getBidPrices().size());
    assertTrue(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one AskPrices Empty.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one AskPrices Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneAskPricesEmpty() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);

    // Act
    orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestBidPrice());
    LongSortedSet askPrices = orderBook.getAskPrices();
    assertTrue(askPrices.isEmpty());
    assertEquals(askPrices, orderBook.getBidPrices());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one AskPrices size is two.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one AskPrices size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneAskPricesSizeIsTwo() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 0L, 0L);

    // Act
    orderBook.update(Side.SELL, 1L, 1L);

    // Assert
    assertEquals(2, orderBook.getAskPrices().size());
    assertTrue(orderBook.getBidPrices().isEmpty());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BestBidPrice is minus one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BestBidPrice is minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBestBidPriceIsMinusOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 1L, -1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(-1L, orderBook.getBestBidPrice());
    assertEquals(1, orderBook.getBidPrices().size());
    assertTrue(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BestBidPrice is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BestBidPrice is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBestBidPriceIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1, orderBook.getBidPrices().size());
    assertEquals(1L, orderBook.getBestBidPrice());
    assertTrue(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BestBidPrice is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BestBidPrice is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBestBidPriceIsOne2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1L, orderBook.getBestBidPrice());
    assertEquals(2, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is five.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsFive() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 0L, 0L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(5, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is five.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsFive2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(5, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is four.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsFour() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 0L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(4, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is four.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsFour2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2L, orderBook.getBestBidPrice());
    assertEquals(4, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is four.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is four")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsFour3() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(4, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1, orderBook.getBidPrices().size());
    assertEquals(2L, orderBook.getBestBidPrice());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is three.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsThree() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2L, orderBook.getBestBidPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is three.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsThree2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2L, orderBook.getBestBidPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is three.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsThree3() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2L, orderBook.getBestBidPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is three.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsThree4() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2L, orderBook.getBestBidPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is two.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsTwo() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2, orderBook.getBidPrices().size());
    assertEquals(2L, orderBook.getBestBidPrice());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is two.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); then OrderBook(long) with instrument is one BidPrices size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_thenOrderBookWithInstrumentIsOneBidPricesSizeIsTwo2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2, orderBook.getBidPrices().size());
    assertEquals(2L, orderBook.getBestBidPrice());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>When {@link Long#MAX_VALUE}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName("Test update(Side, long, long); when MAX_VALUE; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_whenMax_value_thenReturnTrue() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 0L, 0L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, Long.MAX_VALUE, 1L);

    // Assert
    assertEquals(2L, orderBook.getBestBidPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertTrue(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>When minus one.
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BidPrices size is one.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); when minus one; then OrderBook(long) with instrument is one BidPrices size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_whenMinusOne_thenOrderBookWithInstrumentIsOneBidPricesSizeIsOne() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, -1L);

    // Assert
    assertEquals(1, orderBook.getBidPrices().size());
    assertEquals(2L, orderBook.getBestBidPrice());
    assertFalse(actualUpdateResult);
  }

  /**
   * Test {@link OrderBook#update(Side, long, long)}.
   *
   * <ul>
   *   <li>When zero.
   *   <li>Then {@link OrderBook#OrderBook(long)} with instrument is one BestBidPrice is zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test update(Side, long, long); when zero; then OrderBook(long) with instrument is one BestBidPrice is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean OrderBook.update(Side, long, long)"})
  void testUpdate_whenZero_thenOrderBookWithInstrumentIsOneBestBidPriceIsZero() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 0L);

    // Assert
    assertEquals(0L, orderBook.getBestBidPrice());
    assertEquals(1, orderBook.getBidPrices().size());
    assertFalse(actualUpdateResult);
  }
}
