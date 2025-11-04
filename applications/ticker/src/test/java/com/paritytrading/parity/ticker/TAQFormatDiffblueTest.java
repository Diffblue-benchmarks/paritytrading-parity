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
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class TAQFormatDiffblueTest {
  /**
   * Method under test: {@link TAQFormat#update(OrderBook, boolean)}
   */
  @Test
  void testUpdate() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.asString()).thenReturn("As String");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());
    TAQFormat taqFormat = new TAQFormat(instruments);
    OrderBook book = mock(OrderBook.class);
    when(book.getAskSize(anyLong())).thenReturn(3L);
    when(book.getBestAskPrice()).thenReturn(1L);
    when(book.getBestBidPrice()).thenReturn(1L);
    when(book.getBidSize(anyLong())).thenReturn(1L);
    when(book.getInstrument()).thenReturn(1L);

    // Act
    taqFormat.update(book, true);

    // Assert
    verify(book).getAskSize(eq(1L));
    verify(book).getBestAskPrice();
    verify(book).getBestBidPrice();
    verify(book).getBidSize(eq(1L));
    verify(book).getInstrument();
    verify(instrument).asString();
    verify(instrument).getPriceFactor();
    verify(instrument).getSizeFactor();
    verify(instruments).get(eq(1L));
    verify(instruments).iterator();
  }

  /**
   * Method under test: {@link TAQFormat#update(OrderBook, boolean)}
   */
  @Test
  void testUpdate2() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(-0.5d);
    when(instrument.asString()).thenReturn("As String");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());
    TAQFormat taqFormat = new TAQFormat(instruments);
    OrderBook book = mock(OrderBook.class);
    when(book.getAskSize(anyLong())).thenReturn(3L);
    when(book.getBestAskPrice()).thenReturn(1L);
    when(book.getBestBidPrice()).thenReturn(1L);
    when(book.getBidSize(anyLong())).thenReturn(1L);
    when(book.getInstrument()).thenReturn(1L);

    // Act
    taqFormat.update(book, true);

    // Assert
    verify(book).getAskSize(eq(1L));
    verify(book).getBestAskPrice();
    verify(book).getBestBidPrice();
    verify(book).getBidSize(eq(1L));
    verify(book).getInstrument();
    verify(instrument).asString();
    verify(instrument).getPriceFactor();
    verify(instrument).getSizeFactor();
    verify(instruments).get(eq(1L));
    verify(instruments).iterator();
  }

  /**
   * Method under test: {@link TAQFormat#update(OrderBook, boolean)}
   */
  @Test
  void testUpdate3() {
    // Arrange
    Instruments instruments = mock(Instruments.class);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());

    // Act
    (new TAQFormat(instruments)).update(mock(OrderBook.class), false);

    // Assert that nothing has changed
    verify(instruments).iterator();
  }

  /**
   * Method under test: {@link TAQFormat#TAQFormat(Instruments)}
   */
  @Test
  void testNewTAQFormat() {
    // Arrange
    Instruments instruments = mock(Instruments.class);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());

    // Act
    TAQFormat actualTaqFormat = new TAQFormat(instruments);

    // Assert
    verify(instruments).iterator();
    assertEquals(0L, actualTaqFormat.timestampMillis());
  }

  /**
   * Method under test: {@link TAQFormat#trade(OrderBook, Side, long, long)}
   */
  @Test
  void testTrade() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.asString()).thenReturn("As String");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());
    TAQFormat taqFormat = new TAQFormat(instruments);
    OrderBook book = mock(OrderBook.class);
    when(book.getInstrument()).thenReturn(1L);

    // Act
    taqFormat.trade(book, Side.BUY, 1L, 3L);

    // Assert
    verify(book).getInstrument();
    verify(instrument).asString();
    verify(instrument).getPriceFactor();
    verify(instrument).getSizeFactor();
    verify(instruments).get(eq(1L));
    verify(instruments).iterator();
  }

  /**
   * Method under test: {@link TAQFormat#trade(OrderBook, Side, long, long)}
   */
  @Test
  void testTrade2() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.asString()).thenReturn("As String");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(anyLong())).thenReturn(instrument);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());
    TAQFormat taqFormat = new TAQFormat(instruments);
    OrderBook book = mock(OrderBook.class);
    when(book.getInstrument()).thenReturn(1L);

    // Act
    taqFormat.trade(book, Side.SELL, 1L, 3L);

    // Assert
    verify(book).getInstrument();
    verify(instrument).asString();
    verify(instrument).getPriceFactor();
    verify(instrument).getSizeFactor();
    verify(instruments).get(eq(1L));
    verify(instruments).iterator();
  }
}
