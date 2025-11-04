package com.paritytrading.parity.match;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OrderBookDiffblueTest {
  /**
   * Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  void testEnter() {
    // Arrange
    OrderBookListener listener = mock(OrderBookListener.class);
    doNothing().when(listener).add(anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    (new OrderBook(listener)).enter(1L, Side.BUY, 1L, 3L);

    // Assert
    verify(listener).add(eq(1L), eq(Side.BUY), eq(1L), eq(3L));
  }

  /**
   * Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  void testEnter2() {
    // Arrange
    OrderBookListener listener = mock(OrderBookListener.class);
    doNothing().when(listener).add(anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    (new OrderBook(listener)).enter(0L, Side.BUY, 1L, 3L);

    // Assert
    verify(listener).add(eq(0L), eq(Side.BUY), eq(1L), eq(3L));
  }

  /**
   * Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  void testEnter3() {
    // Arrange
    OrderBookListener listener = mock(OrderBookListener.class);
    doNothing().when(listener).add(anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    (new OrderBook(listener)).enter(1L, Side.SELL, 1L, 3L);

    // Assert
    verify(listener).add(eq(1L), eq(Side.SELL), eq(1L), eq(3L));
  }
}
