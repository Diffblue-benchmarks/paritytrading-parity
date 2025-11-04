package com.paritytrading.parity.reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.paritytrading.parity.net.pmr.PMR;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

class TradeProcessorDiffblueTest {
  /**
   * Method under test: {@link TradeProcessor#version(PMR.Version)}
   */
  @Test
  void testVersion() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    TradeProcessor tradeProcessor = new TradeProcessor(new DisplayFormat(instruments));
    PMR.Version message = new PMR.Version();
    message.version = 2L;

    // Act
    tradeProcessor.version(message);

    // Assert that nothing has changed
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }

  /**
   * Method under test: {@link TradeProcessor#TradeProcessor(TradeListener)}
   */
  @Test
  void testNewTradeProcessor() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    new TradeProcessor(new DisplayFormat(instruments));

    // Assert
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }

  /**
   * Method under test: {@link TradeProcessor#orderEntered(PMR.OrderEntered)}
   */
  @Test
  void testOrderEntered() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    TradeProcessor tradeProcessor = new TradeProcessor(new DisplayFormat(instruments));

    // Act
    tradeProcessor.orderEntered(new PMR.OrderEntered());

    // Assert
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }
}
