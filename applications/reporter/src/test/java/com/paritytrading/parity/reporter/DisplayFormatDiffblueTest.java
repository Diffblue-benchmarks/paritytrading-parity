package com.paritytrading.parity.reporter;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DisplayFormatDiffblueTest {
  /**
   * Method under test: {@link DisplayFormat#DisplayFormat(Instruments)}
   */
  @Test
  void testNewDisplayFormat() {
    // Arrange
    Instruments instruments = mock(Instruments.class);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);

    // Act
    new DisplayFormat(instruments);

    // Assert
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }

  /**
   * Method under test: {@link DisplayFormat#trade(Trade)}
   */
  @Test
  void testTrade() {
    // Arrange
    Instrument instrument = mock(Instrument.class);
    when(instrument.getPriceFactor()).thenReturn(10.0d);
    when(instrument.getSizeFactor()).thenReturn(10.0d);
    when(instrument.getPriceFormat()).thenReturn("Price Format");
    when(instrument.getSizeFormat()).thenReturn("Size Format");
    Instruments instruments = mock(Instruments.class);
    when(instruments.get(Mockito.<String>any())).thenReturn(instrument);
    when(instruments.getPriceWidth()).thenReturn(1);
    when(instruments.getSizeWidth()).thenReturn(1);
    DisplayFormat displayFormat = new DisplayFormat(instruments);

    // Act
    displayFormat.trade(new Trade());

    // Assert
    verify(instrument).getPriceFactor();
    verify(instrument).getPriceFormat();
    verify(instrument).getSizeFactor();
    verify(instrument).getSizeFormat();
    verify(instruments).get((String) isNull());
    verify(instruments).getPriceWidth();
    verify(instruments).getSizeWidth();
  }
}
