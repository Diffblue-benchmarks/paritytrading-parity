package com.paritytrading.parity.reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TradeListenerDiffblueTest {
  /**
   * Test {@link TradeListener#printf(String, Object[])}.
   * <ul>
   *   <li>Given {@link Instruments} {@link Instruments#getPriceWidth()} return one.</li>
   *   <li>Then calls {@link Instruments#getPriceWidth()}.</li>
   * </ul>
   * <p>
   * Method under test: {@link TradeListener#printf(String, Object[])}
   */
  @Test
  @DisplayName("Test printf(String, Object[]); given Instruments getPriceWidth() return one; then calls getPriceWidth()")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void TradeListener.printf(String, Object[])"})
  void testPrintf_givenInstrumentsGetPriceWidthReturnOne_thenCallsGetPriceWidth() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    (new DisplayFormat(instruments)).printf("%s", "Args");

    // Assert
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }
}
