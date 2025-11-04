package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

class MarketDataListenerDiffblueTest {
  /**
   * Method under test: {@link MarketDataListener#timestamp(long)}
   */
  @Test
  void testTimestamp() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    (new DisplayFormat(instruments)).timestamp(10L);

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments).getSizeWidth();
  }

  /**
   * Method under test: {@link MarketDataListener#timestampMillis()}
   */
  @Test
  void testTimestampMillis() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    long actualTimestampMillisResult = (new DisplayFormat(instruments)).timestampMillis();

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments).getSizeWidth();
    assertEquals(0L, actualTimestampMillisResult);
  }
}
