package com.paritytrading.parity.ticker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.net.pmd.PMD;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

class MarketDataProcessorDiffblueTest {
  /**
   * Method under test: {@link MarketDataProcessor#orderAdded(PMD.OrderAdded)}
   */
  @Test
  void testOrderAdded() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    Market market = new Market(new DisplayFormat(instruments));
    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    MarketDataProcessor marketDataProcessor = new MarketDataProcessor(market, new DisplayFormat(instruments2));

    // Act
    marketDataProcessor.orderAdded(new PMD.OrderAdded());

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments2).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments2).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments2).getSizePlaceholder();
    verify(instruments).getSizeWidth();
    verify(instruments2).getSizeWidth();
  }

  /**
   * Method under test:
   * {@link MarketDataProcessor#orderExecuted(PMD.OrderExecuted)}
   */
  @Test
  void testOrderExecuted() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    Market market = new Market(new DisplayFormat(instruments));
    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    MarketDataProcessor marketDataProcessor = new MarketDataProcessor(market, new DisplayFormat(instruments2));

    // Act
    marketDataProcessor.orderExecuted(new PMD.OrderExecuted());

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments2).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments2).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments2).getSizePlaceholder();
    verify(instruments).getSizeWidth();
    verify(instruments2).getSizeWidth();
  }

  /**
   * Method under test:
   * {@link MarketDataProcessor#orderCanceled(PMD.OrderCanceled)}
   */
  @Test
  void testOrderCanceled() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    Market market = new Market(new DisplayFormat(instruments));
    Instruments instruments2 = mock(Instruments.class);
    when(instruments2.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments2.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments2.getPriceWidth()).thenReturn(1);
    when(instruments2.getSizeWidth()).thenReturn(1);
    MarketDataProcessor marketDataProcessor = new MarketDataProcessor(market, new DisplayFormat(instruments2));

    // Act
    marketDataProcessor.orderCanceled(new PMD.OrderCanceled());

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments2).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments2).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments2).getSizePlaceholder();
    verify(instruments).getSizeWidth();
    verify(instruments2).getSizeWidth();
  }
}
