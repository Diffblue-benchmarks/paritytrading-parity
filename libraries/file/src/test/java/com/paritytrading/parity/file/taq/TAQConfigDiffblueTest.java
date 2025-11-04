package com.paritytrading.parity.file.taq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class TAQConfigDiffblueTest {
  /**
   * Methods under test:
   * <ul>
   *   <li>{@link TAQConfig.Builder#build()}
   *   <li>{@link TAQConfig.Builder#setEncoding(Charset)}
   * </ul>
   */
  @Test
  void testBuilderBuild() {
    // Arrange, Act and Assert
    assertNull((new TAQConfig.Builder()).setEncoding(null).build().getEncoding());
  }

  /**
   * Method under test: default or parameterless constructor of
   * {@link TAQConfig.Builder}
   */
  @Test
  void testBuilderNewBuilder() {
    // Arrange, Act and Assert
    assertEquals("US-ASCII", (new TAQConfig.Builder()).build().getEncoding().name());
  }

  /**
   * Method under test: {@link TAQConfig.Builder#setPriceFractionDigits(int)}
   */
  @Test
  void testBuilderSetPriceFractionDigits() {
    // Arrange
    TAQConfig.Builder builder = new TAQConfig.Builder();

    // Act
    TAQConfig.Builder actualSetPriceFractionDigitsResult = builder.setPriceFractionDigits(1);

    // Assert
    assertEquals("US-ASCII", actualSetPriceFractionDigitsResult.build().getEncoding().name());
    assertSame(builder, actualSetPriceFractionDigitsResult);
  }

  /**
   * Method under test:
   * {@link TAQConfig.Builder#setPriceFractionDigits(String, int)}
   */
  @Test
  void testBuilderSetPriceFractionDigits2() {
    // Arrange
    TAQConfig.Builder builder = new TAQConfig.Builder();

    // Act
    TAQConfig.Builder actualSetPriceFractionDigitsResult = builder.setPriceFractionDigits("Instrument", 1);

    // Assert
    assertEquals("US-ASCII", actualSetPriceFractionDigitsResult.build().getEncoding().name());
    assertSame(builder, actualSetPriceFractionDigitsResult);
  }

  /**
   * Method under test: {@link TAQConfig.Builder#setSizeFractionDigits(int)}
   */
  @Test
  void testBuilderSetSizeFractionDigits() {
    // Arrange
    TAQConfig.Builder builder = new TAQConfig.Builder();

    // Act
    TAQConfig.Builder actualSetSizeFractionDigitsResult = builder.setSizeFractionDigits(1);

    // Assert
    assertEquals("US-ASCII", actualSetSizeFractionDigitsResult.build().getEncoding().name());
    assertSame(builder, actualSetSizeFractionDigitsResult);
  }

  /**
   * Method under test:
   * {@link TAQConfig.Builder#setSizeFractionDigits(String, int)}
   */
  @Test
  void testBuilderSetSizeFractionDigits2() {
    // Arrange
    TAQConfig.Builder builder = new TAQConfig.Builder();

    // Act
    TAQConfig.Builder actualSetSizeFractionDigitsResult = builder.setSizeFractionDigits("Instrument", 1);

    // Assert
    assertEquals("US-ASCII", actualSetSizeFractionDigitsResult.build().getEncoding().name());
    assertSame(builder, actualSetSizeFractionDigitsResult);
  }

  /**
   * Method under test: {@link TAQConfig#getEncoding()}
   */
  @Test
  void testGetEncoding() {
    // Arrange, Act and Assert
    assertEquals("US-ASCII", TAQConfig.DEFAULTS.getEncoding().name());
  }

  /**
   * Method under test: {@link TAQConfig#getPriceFormat(String)}
   */
  @Test
  void testGetPriceFormat() {
    // Arrange and Act
    DecimalFormat actualPriceFormat = TAQConfig.DEFAULTS.getPriceFormat("Instrument");

    // Assert
    assertEquals("", actualPriceFormat.getNegativeSuffix());
    assertEquals("", actualPriceFormat.getPositivePrefix());
    assertEquals("", actualPriceFormat.getPositiveSuffix());
    assertEquals("#0.00", actualPriceFormat.toLocalizedPattern());
    assertEquals("#0.00", actualPriceFormat.toPattern());
    DecimalFormatSymbols decimalFormatSymbols = actualPriceFormat.getDecimalFormatSymbols();
    assertEquals("$", decimalFormatSymbols.getCurrencySymbol());
    assertEquals("-", actualPriceFormat.getNegativePrefix());
    assertEquals("E", decimalFormatSymbols.getExponentSeparator());
    Currency currency = actualPriceFormat.getCurrency();
    assertEquals("US Dollar", currency.getDisplayName());
    assertEquals("USD", decimalFormatSymbols.getInternationalCurrencySymbol());
    assertEquals("USD", currency.getCurrencyCode());
    assertEquals("USD", currency.getSymbol());
    assertEquals("USD", currency.toString());
    assertEquals("∞", decimalFormatSymbols.getInfinity());
    assertEquals("�", decimalFormatSymbols.getNaN());
    assertEquals('#', decimalFormatSymbols.getDigit());
    assertEquals('%', decimalFormatSymbols.getPercent());
    assertEquals(',', decimalFormatSymbols.getGroupingSeparator());
    assertEquals('-', decimalFormatSymbols.getMinusSign());
    assertEquals('.', decimalFormatSymbols.getDecimalSeparator());
    assertEquals('.', decimalFormatSymbols.getMonetaryDecimalSeparator());
    assertEquals('0', decimalFormatSymbols.getZeroDigit());
    assertEquals(';', decimalFormatSymbols.getPatternSeparator());
    assertEquals('‰', decimalFormatSymbols.getPerMill());
    assertEquals(0, actualPriceFormat.getGroupingSize());
    assertEquals(1, actualPriceFormat.getMinimumIntegerDigits());
    assertEquals(1, actualPriceFormat.getMultiplier());
    assertEquals(2, actualPriceFormat.getMaximumFractionDigits());
    assertEquals(2, actualPriceFormat.getMinimumFractionDigits());
    assertEquals(2, currency.getDefaultFractionDigits());
    assertEquals(840, currency.getNumericCode());
    assertEquals(RoundingMode.HALF_EVEN, actualPriceFormat.getRoundingMode());
    assertFalse(actualPriceFormat.isDecimalSeparatorAlwaysShown());
    assertFalse(actualPriceFormat.isParseBigDecimal());
    assertFalse(actualPriceFormat.isGroupingUsed());
    assertFalse(actualPriceFormat.isParseIntegerOnly());
    assertEquals(Integer.MAX_VALUE, actualPriceFormat.getMaximumIntegerDigits());
    assertSame(currency, decimalFormatSymbols.getCurrency());
  }

  /**
   * Method under test: {@link TAQConfig#getSizeFormat(String)}
   */
  @Test
  void testGetSizeFormat() {
    // Arrange and Act
    DecimalFormat actualSizeFormat = TAQConfig.DEFAULTS.getSizeFormat("Instrument");

    // Assert
    assertEquals("", actualSizeFormat.getNegativeSuffix());
    assertEquals("", actualSizeFormat.getPositivePrefix());
    assertEquals("", actualSizeFormat.getPositiveSuffix());
    assertEquals("#0", actualSizeFormat.toLocalizedPattern());
    assertEquals("#0", actualSizeFormat.toPattern());
    DecimalFormatSymbols decimalFormatSymbols = actualSizeFormat.getDecimalFormatSymbols();
    assertEquals("$", decimalFormatSymbols.getCurrencySymbol());
    assertEquals("-", actualSizeFormat.getNegativePrefix());
    assertEquals("E", decimalFormatSymbols.getExponentSeparator());
    Currency currency = actualSizeFormat.getCurrency();
    assertEquals("US Dollar", currency.getDisplayName());
    assertEquals("USD", decimalFormatSymbols.getInternationalCurrencySymbol());
    assertEquals("USD", currency.getCurrencyCode());
    assertEquals("USD", currency.getSymbol());
    assertEquals("USD", currency.toString());
    assertEquals("∞", decimalFormatSymbols.getInfinity());
    assertEquals("�", decimalFormatSymbols.getNaN());
    assertEquals('#', decimalFormatSymbols.getDigit());
    assertEquals('%', decimalFormatSymbols.getPercent());
    assertEquals(',', decimalFormatSymbols.getGroupingSeparator());
    assertEquals('-', decimalFormatSymbols.getMinusSign());
    assertEquals('.', decimalFormatSymbols.getDecimalSeparator());
    assertEquals('.', decimalFormatSymbols.getMonetaryDecimalSeparator());
    assertEquals('0', decimalFormatSymbols.getZeroDigit());
    assertEquals(';', decimalFormatSymbols.getPatternSeparator());
    assertEquals('‰', decimalFormatSymbols.getPerMill());
    assertEquals(0, actualSizeFormat.getGroupingSize());
    assertEquals(0, actualSizeFormat.getMaximumFractionDigits());
    assertEquals(0, actualSizeFormat.getMinimumFractionDigits());
    assertEquals(1, actualSizeFormat.getMinimumIntegerDigits());
    assertEquals(1, actualSizeFormat.getMultiplier());
    assertEquals(2, currency.getDefaultFractionDigits());
    assertEquals(840, currency.getNumericCode());
    assertEquals(RoundingMode.HALF_EVEN, actualSizeFormat.getRoundingMode());
    assertFalse(actualSizeFormat.isDecimalSeparatorAlwaysShown());
    assertFalse(actualSizeFormat.isParseBigDecimal());
    assertFalse(actualSizeFormat.isGroupingUsed());
    assertFalse(actualSizeFormat.isParseIntegerOnly());
    assertEquals(Integer.MAX_VALUE, actualSizeFormat.getMaximumIntegerDigits());
    assertSame(currency, decimalFormatSymbols.getCurrency());
  }
}
