package com.paritytrading.parity.reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

class TradeListenerDiffblueTest {
  /**
   * Method under test: {@link TradeListener#printf(String, Object[])}
   */
  @Test
  void testPrintf() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    (new DisplayFormat(instruments)).printf("%s", "Args");

    // Assert that nothing has changed
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }
}
