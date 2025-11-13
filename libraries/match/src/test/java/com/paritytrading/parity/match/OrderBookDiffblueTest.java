package com.paritytrading.parity.match;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderBookDiffblueTest {
  @InjectMocks private OrderBook orderBook;

  @Mock private OrderBookListener orderBookListener;

  /**
   * Test {@link OrderBook#enter(long, Side, long, long)}.
   *
   * <ul>
   *   <li>When {@code BUY}.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  @DisplayName("Test enter(long, Side, long, long); when 'BUY'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBook.enter(long, Side, long, long)"})
  void testEnter_whenBuy() {
    // Arrange
    doNothing().when(orderBookListener).add(anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    orderBook.enter(1L, Side.BUY, 1L, 3L);

    // Assert
    verify(orderBookListener).add(1L, Side.BUY, 1L, 3L);
  }

  /**
   * Test {@link OrderBook#enter(long, Side, long, long)}.
   *
   * <ul>
   *   <li>When {@code SELL}.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  @DisplayName("Test enter(long, Side, long, long); when 'SELL'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBook.enter(long, Side, long, long)"})
  void testEnter_whenSell() {
    // Arrange
    doNothing().when(orderBookListener).add(anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    orderBook.enter(1L, Side.SELL, 1L, 3L);

    // Assert
    verify(orderBookListener).add(1L, Side.SELL, 1L, 3L);
  }

  /**
   * Test {@link OrderBook#enter(long, Side, long, long)}.
   *
   * <ul>
   *   <li>When zero.
   * </ul>
   *
   * <p>Method under test: {@link OrderBook#enter(long, Side, long, long)}
   */
  @Test
  @DisplayName("Test enter(long, Side, long, long); when zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderBook.enter(long, Side, long, long)"})
  void testEnter_whenZero() {
    // Arrange
    doNothing().when(orderBookListener).add(anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    orderBook.enter(0L, Side.BUY, 1L, 3L);

    // Assert
    verify(orderBookListener).add(0L, Side.BUY, 1L, 3L);
  }
}
