package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.typesafe.config.Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InstrumentDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
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
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "long Instrument.asLong()",
    "String Instrument.asString()",
    "String Instrument.getPriceFormat()",
    "int Instrument.getPriceFractionDigits()",
    "String Instrument.getSizeFormat()",
    "int Instrument.getSizeFractionDigits()"
  })
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

  /**
   * Test {@link Instrument#getPriceFactor()}.
   *
   * <p>Method under test: {@link Instrument#getPriceFactor()}
   */
  @Test
  @DisplayName("Test getPriceFactor()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"double Instrument.getPriceFactor()"})
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
   * Test {@link Instrument#getSizeFactor()}.
   *
   * <p>Method under test: {@link Instrument#getSizeFactor()}
   */
  @Test
  @DisplayName("Test getSizeFactor()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"double Instrument.getSizeFactor()"})
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
   * Test {@link Instrument#setPriceFormat(int, int)}.
   *
   * <ul>
   *   <li>Then fromConfig {@link Config} and {@code Path} PriceFormat is {@code %1.0f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setPriceFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setPriceFormat(int, int); then fromConfig Config and 'Path' PriceFormat is '%1.0f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setPriceFormat(int, int)"})
  void testSetPriceFormat_thenFromConfigConfigAndPathPriceFormatIs10f() {
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
   * Test {@link Instrument#setPriceFormat(int, int)}.
   *
   * <ul>
   *   <li>Then fromConfig {@link Config} and {@code Path} PriceFormat is {@code %1.0f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setPriceFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setPriceFormat(int, int); then fromConfig Config and 'Path' PriceFormat is '%1.0f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setPriceFormat(int, int)"})
  void testSetPriceFormat_thenFromConfigConfigAndPathPriceFormatIs10f2() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(0);
    Instrument fromConfigResult = Instrument.fromConfig(config, "Path");

    // Act
    fromConfigResult.setPriceFormat(1, 0);

    // Assert that nothing has changed
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%1.0f", fromConfigResult.getPriceFormat());
  }

  /**
   * Test {@link Instrument#setPriceFormat(int, int)}.
   *
   * <ul>
   *   <li>Then fromConfig {@link Config} and {@code Path} PriceFormat is {@code %3.1f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setPriceFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setPriceFormat(int, int); then fromConfig Config and 'Path' PriceFormat is '%3.1f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setPriceFormat(int, int)"})
  void testSetPriceFormat_thenFromConfigConfigAndPathPriceFormatIs31f() {
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
   * Test {@link Instrument#setSizeFormat(int, int)}.
   *
   * <ul>
   *   <li>Then fromConfig {@link Config} and {@code Path} SizeFormat is {@code %1.0f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setSizeFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setSizeFormat(int, int); then fromConfig Config and 'Path' SizeFormat is '%1.0f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setSizeFormat(int, int)"})
  void testSetSizeFormat_thenFromConfigConfigAndPathSizeFormatIs10f() {
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
   * Test {@link Instrument#setSizeFormat(int, int)}.
   *
   * <ul>
   *   <li>Then fromConfig {@link Config} and {@code Path} SizeFormat is {@code %1.0f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setSizeFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setSizeFormat(int, int); then fromConfig Config and 'Path' SizeFormat is '%1.0f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setSizeFormat(int, int)"})
  void testSetSizeFormat_thenFromConfigConfigAndPathSizeFormatIs10f2() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(0);
    Instrument fromConfigResult = Instrument.fromConfig(config, "Path");

    // Act
    fromConfigResult.setSizeFormat(1, 0);

    // Assert that nothing has changed
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%1.0f", fromConfigResult.getSizeFormat());
  }

  /**
   * Test {@link Instrument#setSizeFormat(int, int)}.
   *
   * <ul>
   *   <li>Then fromConfig {@link Config} and {@code Path} SizeFormat is {@code %3.1f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setSizeFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setSizeFormat(int, int); then fromConfig Config and 'Path' SizeFormat is '%3.1f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setSizeFormat(int, int)"})
  void testSetSizeFormat_thenFromConfigConfigAndPathSizeFormatIs31f() {
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
   * Test {@link Instrument#fromConfig(Config, String)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>When {@link Config} {@link Config#getInt(String)} return one.
   *   <li>Then return PriceFormat is {@code %3.1f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#fromConfig(Config, String)}
   */
  @Test
  @DisplayName(
      "Test fromConfig(Config, String); given one; when Config getInt(String) return one; then return PriceFormat is '%3.1f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Instrument Instrument.fromConfig(Config, String)"})
  void testFromConfig_givenOne_whenConfigGetIntReturnOne_thenReturnPriceFormatIs31f() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);

    // Act
    Instrument actualFromConfigResult = Instrument.fromConfig(config, "Path");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%3.1f", actualFromConfigResult.getPriceFormat());
    assertEquals("%3.1f", actualFromConfigResult.getSizeFormat());
    assertEquals(1, actualFromConfigResult.getPriceFractionDigits());
    assertEquals(1, actualFromConfigResult.getSizeFractionDigits());
    assertEquals(10.0d, actualFromConfigResult.getPriceFactor());
    assertEquals(10.0d, actualFromConfigResult.getSizeFactor());
  }

  /**
   * Test {@link Instrument#fromConfig(Config, String)}.
   *
   * <ul>
   *   <li>Given zero.
   *   <li>When {@link Config} {@link Config#getInt(String)} return zero.
   *   <li>Then return PriceFormat is {@code %1.0f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#fromConfig(Config, String)}
   */
  @Test
  @DisplayName(
      "Test fromConfig(Config, String); given zero; when Config getInt(String) return zero; then return PriceFormat is '%1.0f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Instrument Instrument.fromConfig(Config, String)"})
  void testFromConfig_givenZero_whenConfigGetIntReturnZero_thenReturnPriceFormatIs10f() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(0);

    // Act
    Instrument actualFromConfigResult = Instrument.fromConfig(config, "Path");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    assertEquals("%1.0f", actualFromConfigResult.getPriceFormat());
    assertEquals("%1.0f", actualFromConfigResult.getSizeFormat());
    assertEquals(0, actualFromConfigResult.getPriceFractionDigits());
    assertEquals(0, actualFromConfigResult.getSizeFractionDigits());
    assertEquals(1.0d, actualFromConfigResult.getPriceFactor());
    assertEquals(1.0d, actualFromConfigResult.getSizeFactor());
  }
}
