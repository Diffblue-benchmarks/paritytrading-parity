package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.typesafe.config.Config;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InstrumentDiffblueTest {
  /**
   * Method under test: {@link Instrument#getPriceFactor()}
   */
  @Test
  void testGetPriceFactor() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);

    // Act
    double actualPriceFactor = Instrument.fromConfig(config, "Path").getPriceFactor();

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals(10.0d, actualPriceFactor);
  }

  /**
   * Method under test: {@link Instrument#getSizeFactor()}
   */
  @Test
  void testGetSizeFactor() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);

    // Act
    double actualSizeFactor = Instrument.fromConfig(config, "Path").getSizeFactor();

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals(10.0d, actualSizeFactor);
  }

  /**
   * Method under test: {@link Instrument#setPriceFormat(int, int)}
   */
  @Test
  void testSetPriceFormat() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    Instrument fromConfigResult = Instrument.fromConfig(config, "Path");

    // Act
    fromConfigResult.setPriceFormat(1, 3);

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%3.1f  ", fromConfigResult.getPriceFormat());
  }

  /**
   * Method under test: {@link Instrument#setPriceFormat(int, int)}
   */
  @Test
  void testSetPriceFormat2() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(0);
    Instrument fromConfigResult = Instrument.fromConfig(config, "Path");

    // Act
    fromConfigResult.setPriceFormat(1, 3);

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%1.0f    ", fromConfigResult.getPriceFormat());
  }

  /**
   * Method under test: {@link Instrument#setSizeFormat(int, int)}
   */
  @Test
  void testSetSizeFormat() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    Instrument fromConfigResult = Instrument.fromConfig(config, "Path");

    // Act
    fromConfigResult.setSizeFormat(1, 3);

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%3.1f  ", fromConfigResult.getSizeFormat());
  }

  /**
   * Method under test: {@link Instrument#setSizeFormat(int, int)}
   */
  @Test
  void testSetSizeFormat2() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(0);
    Instrument fromConfigResult = Instrument.fromConfig(config, "Path");

    // Act
    fromConfigResult.setSizeFormat(1, 3);

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%1.0f    ", fromConfigResult.getSizeFormat());
  }

  /**
   * Method under test: {@link Instrument#fromConfig(Config, String)}
   */
  @Test
  void testFromConfig() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);

    // Act
    Instrument actualFromConfigResult = Instrument.fromConfig(config, "Path");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%3.1f", actualFromConfigResult.getPriceFormat());
    assertEquals("%3.1f", actualFromConfigResult.getSizeFormat());
    assertEquals("Path", actualFromConfigResult.asString());
    assertEquals(1, actualFromConfigResult.getPriceFractionDigits());
    assertEquals(1, actualFromConfigResult.getSizeFractionDigits());
    assertEquals(10.0d, actualFromConfigResult.getPriceFactor());
    assertEquals(10.0d, actualFromConfigResult.getSizeFactor());
    assertEquals(5792038586339565600L, actualFromConfigResult.asLong());
  }

  /**
   * Method under test: {@link Instrument#fromConfig(Config, String)}
   */
  @Test
  void testFromConfig2() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(0);

    // Act
    Instrument actualFromConfigResult = Instrument.fromConfig(config, "Path");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%1.0f", actualFromConfigResult.getPriceFormat());
    assertEquals("%1.0f", actualFromConfigResult.getSizeFormat());
    assertEquals("Path", actualFromConfigResult.asString());
    assertEquals(0, actualFromConfigResult.getPriceFractionDigits());
    assertEquals(0, actualFromConfigResult.getSizeFractionDigits());
    assertEquals(1.0d, actualFromConfigResult.getPriceFactor());
    assertEquals(1.0d, actualFromConfigResult.getSizeFactor());
    assertEquals(5792038586339565600L, actualFromConfigResult.asLong());
  }

  /**
   * Methods under test:
   * <ul>
   *   <li>{@link Instrument#asLong()}
   *   <li>{@link Instrument#asString()}
   *   <li>{@link Instrument#getPriceFormat()}
   *   <li>{@link Instrument#getPriceFractionDigits()}
   *   <li>{@link Instrument#getSizeFormat()}
   *   <li>{@link Instrument#getSizeFractionDigits()}
   * </ul>
   */
  @Test
  void testGettersAndSetters() {
    // Arrange
    Instrument fromConfigResult = Instrument.fromConfig(mock(Config.class), "Path");

    // Act
    long actualAsLongResult = fromConfigResult.asLong();
    String actualAsStringResult = fromConfigResult.asString();
    String actualPriceFormat = fromConfigResult.getPriceFormat();
    int actualPriceFractionDigits = fromConfigResult.getPriceFractionDigits();
    String actualSizeFormat = fromConfigResult.getSizeFormat();

    // Assert
    assertEquals("%1.0f", actualPriceFormat);
    assertEquals("%1.0f", actualSizeFormat);
    assertEquals("Path", actualAsStringResult);
    assertEquals(0, actualPriceFractionDigits);
    assertEquals(0, fromConfigResult.getSizeFractionDigits());
    assertEquals(5792038586339565600L, actualAsLongResult);
  }
}
