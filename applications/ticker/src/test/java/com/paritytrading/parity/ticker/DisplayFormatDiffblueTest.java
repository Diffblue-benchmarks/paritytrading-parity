package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;

class DisplayFormatDiffblueTest {
  /**
   * Method under test: {@link DisplayFormat#update(OrderBook, boolean)}
   */
  @Test
  void testUpdate() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.asString()).thenReturn("As String");
    when(instrument.getPriceFormat()).thenReturn("Price Format");
    when(instrument.getSizeFormat()).thenReturn("Size Format");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    DisplayFormat displayFormat = new DisplayFormat(instruments);
    OrderBook book = mock(OrderBook.class);
    when(book.getAskSize(anyLong())).thenReturn(3L);
    when(book.getBestAskPrice()).thenReturn(1L);
    when(book.getBestBidPrice()).thenReturn(1L);
    when(book.getBidSize(anyLong())).thenReturn(1L);
    when(book.getInstrument()).thenReturn(1L);

    // Act
    displayFormat.update(book, true);

    // Assert
    verify(book).getAskSize(eq(1L));
    verify(book).getBestAskPrice();
    verify(book).getBestBidPrice();
    verify(book).getBidSize(eq(1L));
    verify(book).getInstrument();
    verify(instrument).asString();
    verify(instrument).getPriceFactor();
    verify(instrument).getPriceFormat();
    verify(instrument).getSizeFactor();
    verify(instrument).getSizeFormat();
    verify(instruments).get(eq(1L));
    verify(instruments).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments).getSizeWidth();
  }

  /**
   * Method under test: {@link DisplayFormat#DisplayFormat(Instruments)}
   */
  @Test
  void testNewDisplayFormat() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    DisplayFormat actualDisplayFormat = new DisplayFormat(instruments);

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments).getSizeWidth();
    assertEquals(0L, actualDisplayFormat.timestampMillis());
  }

  /**
   * Method under test: {@link DisplayFormat#trade(OrderBook, Side, long, long)}
   */
  @Test
  void testTrade() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.asString()).thenReturn("As String");
    when(instrument.getPriceFormat()).thenReturn("Price Format");
    when(instrument.getSizeFormat()).thenReturn("Size Format");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    DisplayFormat displayFormat = new DisplayFormat(instruments);
    OrderBook book = mock(OrderBook.class);
    when(book.getInstrument()).thenReturn(1L);

    // Act
    displayFormat.trade(book, Side.BUY, 1L, 3L);

    // Assert
    verify(book).getInstrument();
    verify(instrument).asString();
    verify(instrument).getPriceFactor();
    verify(instrument).getPriceFormat();
    verify(instrument).getSizeFactor();
    verify(instrument).getSizeFormat();
    verify(instruments).get(eq(1L));
    verify(instruments).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments).getSizeWidth();
  }
}
