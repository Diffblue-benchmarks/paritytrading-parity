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
    // Arrange, Act and Assert
    assertEquals(10000.0d, InstrumentFactory.createInstrumentWithFractions().getPriceFactor());
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
    // Arrange, Act and Assert
    assertEquals(100.0d, InstrumentFactory.createInstrumentWithFractions().getSizeFactor());
  }

  /**
   * Test {@link Instrument#setPriceFormat(int, int)}.
   *
   * <ul>
   *   <li>Given createInstrument.
   *   <li>Then createInstrument PriceFormat is {@code %4.2f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setPriceFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setPriceFormat(int, int); given createInstrument; then createInstrument PriceFormat is '%4.2f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setPriceFormat(int, int)"})
  void testSetPriceFormat_givenCreateInstrument_thenCreateInstrumentPriceFormatIs42f() {
    // Arrange
    Instrument createInstrumentResult = InstrumentFactory.createInstrument();

    // Act
    createInstrumentResult.setPriceFormat(1, 3);

    // Assert
    assertEquals("%4.2f ", createInstrumentResult.getPriceFormat());
  }

  /**
   * Test {@link Instrument#setSizeFormat(int, int)}.
   *
   * <ul>
   *   <li>Given createInstrument.
   *   <li>Then createInstrument SizeFormat is {@code %1.0f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setSizeFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setSizeFormat(int, int); given createInstrument; then createInstrument SizeFormat is '%1.0f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setSizeFormat(int, int)"})
  void testSetSizeFormat_givenCreateInstrument_thenCreateInstrumentSizeFormatIs10f() {
    // Arrange
    Instrument createInstrumentResult = InstrumentFactory.createInstrument();

    // Act
    createInstrumentResult.setSizeFormat(1, 3);

    // Assert
    assertEquals("%1.0f    ", createInstrumentResult.getSizeFormat());
  }

  /**
   * Test {@link Instrument#setSizeFormat(int, int)}.
   *
   * <ul>
   *   <li>Given createInstrument.
   *   <li>Then createInstrument SizeFormat is {@code %1.0f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setSizeFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setSizeFormat(int, int); given createInstrument; then createInstrument SizeFormat is '%1.0f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setSizeFormat(int, int)"})
  void testSetSizeFormat_givenCreateInstrument_thenCreateInstrumentSizeFormatIs10f2() {
    // Arrange
    Instrument createInstrumentResult = InstrumentFactory.createInstrument();

    // Act
    createInstrumentResult.setSizeFormat(1, 0);

    // Assert that nothing has changed
    assertEquals("%1.0f", createInstrumentResult.getSizeFormat());
  }

  /**
   * Test {@link Instrument#setSizeFormat(int, int)}.
   *
   * <ul>
   *   <li>Then createInstrumentWithFractions SizeFormat is {@code %4.2f}.
   * </ul>
   *
   * <p>Method under test: {@link Instrument#setSizeFormat(int, int)}
   */
  @Test
  @DisplayName(
      "Test setSizeFormat(int, int); then createInstrumentWithFractions SizeFormat is '%4.2f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Instrument.setSizeFormat(int, int)"})
  void testSetSizeFormat_thenCreateInstrumentWithFractionsSizeFormatIs42f() {
    // Arrange
    Instrument createInstrumentWithFractionsResult =
        InstrumentFactory.createInstrumentWithFractions();

    // Act
    createInstrumentWithFractionsResult.setSizeFormat(1, 3);

    // Assert
    assertEquals("%4.2f ", createInstrumentWithFractionsResult.getSizeFormat());
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
