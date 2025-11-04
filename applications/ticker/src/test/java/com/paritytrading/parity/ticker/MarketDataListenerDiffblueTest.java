package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MarketDataListenerDiffblueTest {
  /**
   * Test {@link MarketDataListener#timestamp(long)}.
   * <ul>
   *   <li>Then calls {@link Instruments#getPricePlaceholder()}.</li>
   * </ul>
   * <p>
   * Method under test: {@link MarketDataListener#timestamp(long)}
   */
  @Test
  @DisplayName("Test timestamp(long); then calls getPricePlaceholder()")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void MarketDataListener.timestamp(long)"})
  void testTimestamp_thenCallsGetPricePlaceholder() {
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
   * Test {@link MarketDataListener#timestampMillis()}.
   * <ul>
   *   <li>Then return zero.</li>
   * </ul>
   * <p>
   * Method under test: {@link MarketDataListener#timestampMillis()}
   */
  @Test
  @DisplayName("Test timestampMillis(); then return zero")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"long MarketDataListener.timestampMillis()"})
  void testTimestampMillis_thenReturnZero() {
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
