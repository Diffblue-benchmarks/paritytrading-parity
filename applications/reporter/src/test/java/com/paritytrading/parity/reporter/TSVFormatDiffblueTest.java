package com.paritytrading.parity.reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TSVFormatDiffblueTest {
  /**
   * Test {@link TSVFormat#TSVFormat(Instruments)}.
   *
   * <p>Method under test: {@link TSVFormat#TSVFormat(Instruments)}
   */
  @Test
  @DisplayName("Test new TSVFormat(Instruments)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TSVFormat.<init>(Instruments)"})
  void testNewTSVFormat() {
    // Arrange
    Instruments instruments = mock(Instruments.class);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());

    // Act
    new TSVFormat(instruments);

    // Assert
    verify(instruments).iterator();
  }

  /**
   * Test {@link TSVFormat#TSVFormat(Instruments)}.
   *
   * <ul>
   *   <li>Given {@link Instruments} iterator returns an {@link Instrument}.
   *   <li>Then calls {@link Instrument#getPriceFractionDigits()}.
   * </ul>
   *
   * <p>Method under test: {@link TSVFormat#TSVFormat(Instruments)}
   */
  @Test
  @DisplayName(
      "Test new TSVFormat(Instruments); given Instruments iterator returns an Instrument; then calls getPriceFractionDigits()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TSVFormat.<init>(Instruments)"})
  void testNewTSVFormat_givenInstrumentsIteratorReturnsInstrument_thenCallsGetPriceFractionDigits() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFractionDigits()).thenReturn(2);
    when(instrument.getSizeFractionDigits()).thenReturn(2);
    when(instrument.asString()).thenReturn("FOO");

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    instrumentList.add(instrument);

    Instruments instruments = mock(Instruments.class);
    when(instruments.iterator()).thenReturn(instrumentList.iterator());

    // Act
    new TSVFormat(instruments);

    // Assert
    verify(instrument).getPriceFractionDigits();
    verify(instrument).getSizeFractionDigits();
    verify(instrument).asString();
  }

  /**
   * Test {@link TSVFormat#trade(Trade)}.
   *
   * <ul>
   *   <li>Given {@link Instrument} {@link Instrument#getPriceFactor()} return ten.
   *   <li>Then calls {@link Instrument#getPriceFactor()}.
   * </ul>
   *
   * <p>Method under test: {@link TSVFormat#trade(Trade)}
   */
  @Test
  @DisplayName(
      "Test trade(Trade); given Instrument getPriceFactor() return ten; then calls getPriceFactor()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TSVFormat.trade(Trade)"})
  void testTrade_givenInstrumentGetPriceFactorReturnTen_thenCallsGetPriceFactor() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.getPriceFractionDigits()).thenReturn(2);
    when(instrument.getSizeFractionDigits()).thenReturn(2);
    when(instrument.asString()).thenReturn("FOO");

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    instrumentList.add(instrument);

    Instruments instruments = mock(Instruments.class);
    when(instruments.get(Mockito.<String>any())).thenReturn(instrument);
    when(instruments.iterator()).thenReturn(instrumentList.iterator());

    TSVFormat tsvFormat = new TSVFormat(instruments);

    Trade event = new Trade();
    event.instrument = "FOO";

    // Act
    tsvFormat.trade(event);

    // Assert
    verify(instrument).getPriceFactor();
    verify(instrument).getSizeFactor();
    verify(instruments).get("FOO");
  }
}
