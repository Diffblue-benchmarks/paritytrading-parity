package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TAQFormatDiffblueTest {
  /**
   * Test {@link TAQFormat#TAQFormat(Instruments)}.
   *
   * <p>Method under test: {@link TAQFormat#TAQFormat(Instruments)}
   */
  @Test
  @DisplayName("Test new TAQFormat(Instruments)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQFormat.<init>(Instruments)"})
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
   * Test {@link TAQFormat#TAQFormat(Instruments)}.
   *
   * <ul>
   *   <li>Given instruments iterator returns one instrument.
   *   <li>Then calls getPriceFractionDigits and getSizeFractionDigits on instrument.
   * </ul>
   *
   * <p>Method under test: {@link TAQFormat#TAQFormat(Instruments)}
   */
  @Test
  @DisplayName("Test new TAQFormat(Instruments); given instruments iterator returns one instrument; then calls getPriceFractionDigits and getSizeFractionDigits")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQFormat.<init>(Instruments)"})
  void testNewTAQFormat_givenInstrumentsIteratorReturnsOneInstrument_thenCallsGetPriceFractionDigits() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.asString()).thenReturn("AAPL");
    when(instrument.getPriceFractionDigits()).thenReturn(2);
    when(instrument.getSizeFractionDigits()).thenReturn(2);

    Instruments instruments = mock(Instruments.class);
    when(instruments.iterator()).thenReturn(Collections.singletonList(instrument).iterator());

    // Act
    TAQFormat actualTaqFormat = new TAQFormat(instruments);

    // Assert
    verify(instruments).iterator();
    verify(instrument, times(2)).asString();
    verify(instrument).getPriceFractionDigits();
    verify(instrument).getSizeFractionDigits();
    assertEquals(0L, actualTaqFormat.timestampMillis());
  }

  /**
   * Test {@link TAQFormat#update(OrderBook, boolean)}.
   *
   * <ul>
   *   <li>Given {@link Instrument} {@link Instrument#getSizeFactor()} return {@code -0.5}.
   *   <li>Then calls {@link OrderBook#getAskSize(long)}.
   * </ul>
   *
   * <p>Method under test: {@link TAQFormat#update(OrderBook, boolean)}
   */
  @Test
  @DisplayName(
      "Test update(OrderBook, boolean); given Instrument getSizeFactor() return '-0.5'; then calls getAskSize(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQFormat.update(OrderBook, boolean)"})
  void testUpdate_givenInstrumentGetSizeFactorReturn05_thenCallsGetAskSize() {
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
    verify(book).getAskSize(1L);
    verify(book).getBestAskPrice();
    verify(book).getBestBidPrice();
    verify(book).getBidSize(1L);
    verify(book).getInstrument();
    verify(instrument).asString();
    verify(instrument).getPriceFactor();
    verify(instrument).getSizeFactor();
    verify(instruments).get(1L);
    verify(instruments).iterator();
  }

  /**
   * Test {@link TAQFormat#update(OrderBook, boolean)}.
   *
   * <ul>
   *   <li>Given {@link Instrument} {@link Instrument#getSizeFactor()} return ten.
   *   <li>Then calls {@link OrderBook#getAskSize(long)}.
   * </ul>
   *
   * <p>Method under test: {@link TAQFormat#update(OrderBook, boolean)}
   */
  @Test
  @DisplayName(
      "Test update(OrderBook, boolean); given Instrument getSizeFactor() return ten; then calls getAskSize(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQFormat.update(OrderBook, boolean)"})
  void testUpdate_givenInstrumentGetSizeFactorReturnTen_thenCallsGetAskSize() {
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
    verify(book).getAskSize(1L);
    verify(book).getBestAskPrice();
    verify(book).getBestBidPrice();
    verify(book).getBidSize(1L);
    verify(book).getInstrument();
    verify(instrument).asString();
    verify(instrument).getPriceFactor();
    verify(instrument).getSizeFactor();
    verify(instruments).get(1L);
    verify(instruments).iterator();
  }

  /**
   * Test {@link TAQFormat#update(OrderBook, boolean)}.
   *
   * <ul>
   *   <li>When {@code false}.
   *   <li>Then calls {@link Instruments#iterator()}.
   * </ul>
   *
   * <p>Method under test: {@link TAQFormat#update(OrderBook, boolean)}
   */
  @Test
  @DisplayName("Test update(OrderBook, boolean); when 'false'; then calls iterator()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQFormat.update(OrderBook, boolean)"})
  void testUpdate_whenFalse_thenCallsIterator() {
    // Arrange
    Instruments instruments = mock(Instruments.class);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());

    // Act
    new TAQFormat(instruments).update(mock(OrderBook.class), false);

    // Assert
    verify(instruments).iterator();
  }

  /**
   * Test {@link TAQFormat#trade(OrderBook, Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link Instrument} {@link Instrument#getPriceFactor()} return ten.
   *   <li>Then calls {@link OrderBook#getInstrument()}.
   * </ul>
   *
   * <p>Method under test: {@link TAQFormat#trade(OrderBook, Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test trade(OrderBook, Side, long, long); given Instrument getPriceFactor() return ten; then calls getInstrument()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQFormat.trade(OrderBook, Side, long, long)"})
  void testTrade_givenInstrumentGetPriceFactorReturnTen_thenCallsGetInstrument() {
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
    verify(instruments).get(1L);
    verify(instruments).iterator();
  }

  /**
   * Test {@link TAQFormat#trade(OrderBook, Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link Instrument} {@link Instrument#getPriceFactor()} return ten.
   *   <li>When {@code SELL}.
   *   <li>Then calls {@link OrderBook#getInstrument()}.
   * </ul>
   *
   * <p>Method under test: {@link TAQFormat#trade(OrderBook, Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test trade(OrderBook, Side, long, long); given Instrument getPriceFactor() return ten; when 'SELL'; then calls getInstrument()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQFormat.trade(OrderBook, Side, long, long)"})
  void testTrade_givenInstrumentGetPriceFactorReturnTen_whenSell_thenCallsGetInstrument() {
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
    verify(instruments).get(1L);
    verify(instruments).iterator();
  }
}
