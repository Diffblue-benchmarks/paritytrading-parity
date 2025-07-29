package com.paritytrading.parity.ticker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.book.OrderBook;
import com.paritytrading.parity.book.Side;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class DisplayFormatDiffblueTest {
  /**
   * Test {@link DisplayFormat#DisplayFormat(Instruments)}.
   *
   * <ul>
   *   <li>Then return timestampMillis is zero.
   * </ul>
   *
   * <p>Method under test: {@link DisplayFormat#DisplayFormat(Instruments)}
   */
  @Test
  @DisplayName("Test new DisplayFormat(Instruments); then return timestampMillis is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DisplayFormat.<init>(Instruments)"})
  void testNewDisplayFormat_thenReturnTimestampMillisIsZero() {
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
   * Test {@link DisplayFormat#update(OrderBook, boolean)}.
   *
   * <ul>
   *   <li>Given three.
   *   <li>When {@link OrderBook} {@link OrderBook#getAskSize(long)} return three.
   *   <li>Then calls {@link OrderBook#getAskSize(long)}.
   * </ul>
   *
   * <p>Method under test: {@link DisplayFormat#update(OrderBook, boolean)}
   */
  @Test
  @DisplayName(
      "Test update(OrderBook, boolean); given three; when OrderBook getAskSize(long) return three; then calls getAskSize(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DisplayFormat.update(OrderBook, boolean)"})
  void testUpdate_givenThree_whenOrderBookGetAskSizeReturnThree_thenCallsGetAskSize() {
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
   * Test {@link DisplayFormat#update(OrderBook, boolean)}.
   *
   * <ul>
   *   <li>Given three.
   *   <li>When {@link OrderBook} {@link OrderBook#getBidSize(long)} return zero.
   *   <li>Then calls {@link OrderBook#getAskSize(long)}.
   * </ul>
   *
   * <p>Method under test: {@link DisplayFormat#update(OrderBook, boolean)}
   */
  @Test
  @DisplayName(
      "Test update(OrderBook, boolean); given three; when OrderBook getBidSize(long) return zero; then calls getAskSize(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DisplayFormat.update(OrderBook, boolean)"})
  void testUpdate_givenThree_whenOrderBookGetBidSizeReturnZero_thenCallsGetAskSize() {
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
    when(book.getBidSize(anyLong())).thenReturn(0L);
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
   * Test {@link DisplayFormat#update(OrderBook, boolean)}.
   *
   * <ul>
   *   <li>Given zero.
   *   <li>When {@link OrderBook} {@link OrderBook#getAskSize(long)} return zero.
   *   <li>Then calls {@link OrderBook#getAskSize(long)}.
   * </ul>
   *
   * <p>Method under test: {@link DisplayFormat#update(OrderBook, boolean)}
   */
  @Test
  @DisplayName(
      "Test update(OrderBook, boolean); given zero; when OrderBook getAskSize(long) return zero; then calls getAskSize(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DisplayFormat.update(OrderBook, boolean)"})
  void testUpdate_givenZero_whenOrderBookGetAskSizeReturnZero_thenCallsGetAskSize() {
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
    when(book.getAskSize(anyLong())).thenReturn(0L);
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
   * Test {@link DisplayFormat#update(OrderBook, boolean)}.
   *
   * <ul>
   *   <li>When {@code false}.
   *   <li>Then calls {@link Instruments#getPricePlaceholder()}.
   * </ul>
   *
   * <p>Method under test: {@link DisplayFormat#update(OrderBook, boolean)}
   */
  @Test
  @DisplayName("Test update(OrderBook, boolean); when 'false'; then calls getPricePlaceholder()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DisplayFormat.update(OrderBook, boolean)"})
  void testUpdate_whenFalse_thenCallsGetPricePlaceholder() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPricePlaceholder()).thenReturn("Price Placeholder");
    when(instruments.getSizePlaceholder()).thenReturn("Size Placeholder");
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    new DisplayFormat(instruments).update(mock(OrderBook.class), false);

    // Assert
    verify(instruments).getPricePlaceholder();
    verify(instruments).getPriceWidth();
    verify(instruments).getSizePlaceholder();
    verify(instruments).getSizeWidth();
  }

  /**
   * Test {@link DisplayFormat#trade(OrderBook, Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link Instrument} {@link Instrument#getPriceFactor()} return ten.
   *   <li>Then calls {@link OrderBook#getInstrument()}.
   * </ul>
   *
   * <p>Method under test: {@link DisplayFormat#trade(OrderBook, Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test trade(OrderBook, Side, long, long); given Instrument getPriceFactor() return ten; then calls getInstrument()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DisplayFormat.trade(OrderBook, Side, long, long)"})
  void testTrade_givenInstrumentGetPriceFactorReturnTen_thenCallsGetInstrument() {
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

  /**
   * Test {@link DisplayFormat#trade(OrderBook, Side, long, long)}.
   *
   * <ul>
   *   <li>Given {@link Instrument} {@link Instrument#getPriceFactor()} return ten.
   *   <li>When {@code SELL}.
   *   <li>Then calls {@link OrderBook#getInstrument()}.
   * </ul>
   *
   * <p>Method under test: {@link DisplayFormat#trade(OrderBook, Side, long, long)}
   */
  @Test
  @DisplayName(
      "Test trade(OrderBook, Side, long, long); given Instrument getPriceFactor() return ten; when 'SELL'; then calls getInstrument()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DisplayFormat.trade(OrderBook, Side, long, long)"})
  void testTrade_givenInstrumentGetPriceFactorReturnTen_whenSell_thenCallsGetInstrument() {
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
    displayFormat.trade(book, Side.SELL, 1L, 3L);

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
