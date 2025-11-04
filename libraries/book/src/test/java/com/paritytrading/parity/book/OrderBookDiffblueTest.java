package com.paritytrading.parity.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class OrderBookDiffblueTest {
  /**
   * Method under test: {@link OrderBook#getInstrument()}
   */
  @Test
  void testGetInstrument() {
    // Arrange, Act and Assert
    assertEquals(1L, (new OrderBook(1L)).getInstrument());
  }

  /**
   * Method under test: {@link OrderBook#getBestBidPrice()}
   */
  @Test
  void testGetBestBidPrice() {
    // Arrange, Act and Assert
    assertEquals(0L, (new OrderBook(1L)).getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#getBestBidPrice()}
   */
  @Test
  void testGetBestBidPrice2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#getBidPrices()}
   */
  @Test
  void testGetBidPrices() {
    // Arrange, Act and Assert
    assertTrue((new OrderBook(1L)).getBidPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  void testGetBidSize() {
    // Arrange, Act and Assert
    assertEquals(0L, (new OrderBook(1L)).getBidSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  void testGetBidSize2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getBidSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  void testGetBidSize3() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getBidSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  void testGetBidSize4() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getBidSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  void testGetBidSize5() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 0L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getBidSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getBidSize(long)}
   */
  @Test
  void testGetBidSize6() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getBidSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getBestAskPrice()}
   */
  @Test
  void testGetBestAskPrice() {
    // Arrange, Act and Assert
    assertEquals(0L, (new OrderBook(1L)).getBestAskPrice());
  }

  /**
   * Method under test: {@link OrderBook#getBestAskPrice()}
   */
  @Test
  void testGetBestAskPrice2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getBestAskPrice());
  }

  /**
   * Method under test: {@link OrderBook#getAskPrices()}
   */
  @Test
  void testGetAskPrices() {
    // Arrange, Act and Assert
    assertTrue((new OrderBook(1L)).getAskPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  void testGetAskSize() {
    // Arrange, Act and Assert
    assertEquals(0L, (new OrderBook(1L)).getAskSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  void testGetAskSize2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getAskSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  void testGetAskSize3() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 0L, 0L);
    orderBook.add(Side.SELL, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getAskSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  void testGetAskSize4() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, Long.MAX_VALUE, 0L);
    orderBook.add(Side.SELL, 1L, 1L);

    // Act and Assert
    assertEquals(1L, orderBook.getAskSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  void testGetAskSize5() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, 0L, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getAskSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#getAskSize(long)}
   */
  @Test
  void testGetAskSize6() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.SELL, Long.MAX_VALUE, 1L);

    // Act and Assert
    assertEquals(0L, orderBook.getAskSize(1L));
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(1, orderBook.getBidPrices().size());
    assertEquals(1L, orderBook.getBestBidPrice());
    assertTrue(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd2() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(1, orderBook.getBidPrices().size());
    assertEquals(1L, orderBook.getBestBidPrice());
    assertTrue(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd3() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(1L, orderBook.getBestBidPrice());
    assertEquals(2, orderBook.getBidPrices().size());
    assertTrue(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd4() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(2, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd5() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 0L);
    orderBook.add(Side.BUY, 1L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(1L, orderBook.getBestBidPrice());
    assertEquals(2, orderBook.getBidPrices().size());
    assertTrue(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd6() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.SELL, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestBidPrice());
    assertEquals(1, orderBook.getAskPrices().size());
    assertEquals(1L, orderBook.getBestAskPrice());
    assertTrue(actualAddResult);
    assertTrue(orderBook.getBidPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd7() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(1L, orderBook.getBestBidPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertTrue(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd8() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd9() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd10() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, -1L);
    orderBook.add(Side.BUY, 0L, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, -1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(3, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd11() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, 0L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(4, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd12() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, Long.MIN_VALUE, 1L);
    orderBook.add(Side.BUY, -1L, 0L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(5, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd13() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, 0L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, Long.MIN_VALUE, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(4, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#add(Side, long, long)}
   */
  @Test
  void testAdd14() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, -1L, 0L);
    orderBook.add(Side.BUY, 0L, 1L);
    orderBook.add(Side.BUY, Long.MAX_VALUE, 1L);

    // Act
    boolean actualAddResult = orderBook.add(Side.BUY, Long.MIN_VALUE, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestAskPrice());
    assertEquals(5, orderBook.getBidPrices().size());
    assertFalse(actualAddResult);
    assertTrue(orderBook.getAskPrices().isEmpty());
    assertEquals(Long.MAX_VALUE, orderBook.getBestBidPrice());
  }

  /**
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate2() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate3() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate4() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(0L, orderBook.getBestBidPrice());
    assertTrue(actualUpdateResult);
    assertTrue(orderBook.getBidPrices().isEmpty());
  }

  /**
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate5() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate6() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate7() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate8() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate9() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate10() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate11() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate12() {
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

  /**
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate13() {
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate14() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 2L, 2L);
    orderBook.add(Side.BUY, 1L, -1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(1, orderBook.getBidPrices().size());
    assertEquals(2L, orderBook.getBestBidPrice());
    assertFalse(actualUpdateResult);
  }

  /**
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate15() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, -1L, -1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, 2L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(2, orderBook.getBidPrices().size());
    assertEquals(2L, orderBook.getBestBidPrice());
    assertFalse(actualUpdateResult);
  }

  /**
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate16() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 1L, -1L);
    orderBook.add(Side.BUY, -1L, 1L);

    // Act
    boolean actualUpdateResult = orderBook.update(Side.BUY, 1L, 1L);

    // Assert
    assertEquals(-1L, orderBook.getBestBidPrice());
    assertEquals(1, orderBook.getBidPrices().size());
    assertTrue(actualUpdateResult);
  }

  /**
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate17() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 1L);
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
   * Method under test: {@link OrderBook#update(Side, long, long)}
   */
  @Test
  void testUpdate18() {
    // Arrange
    OrderBook orderBook = new OrderBook(1L);
    orderBook.add(Side.BUY, 0L, 2L);
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
   * Method under test: {@link OrderBook#OrderBook(long)}
   */
  @Test
  void testNewOrderBook() {
    // Arrange and Act
    OrderBook actualOrderBook = new OrderBook(1L);

    // Assert
    assertEquals(0L, actualOrderBook.getBestAskPrice());
    assertEquals(0L, actualOrderBook.getBestBidPrice());
    assertEquals(1L, actualOrderBook.getInstrument());
    assertTrue(actualOrderBook.getAskPrices().isEmpty());
    assertTrue(actualOrderBook.getBidPrices().isEmpty());
  }
}
