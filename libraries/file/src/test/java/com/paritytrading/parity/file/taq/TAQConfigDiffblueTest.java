package com.paritytrading.parity.file.taq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.file.taq.TAQConfig.Builder;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TAQConfigDiffblueTest {
  /**
   * Test Builder {@link Builder#build()}.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link Builder#build()}
   *   <li>{@link Builder#setEncoding(Charset)}
   * </ul>
   */
  @Test
  @DisplayName("Test Builder build()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TAQConfig Builder.build()", "Builder Builder.setEncoding(Charset)"})
  void testBuilderBuild() {
    // Arrange
    Builder builder = new Builder();
    Charset encoding = Charset.forName("UTF-8");

    // Act and Assert
    Charset encoding2 = builder.setEncoding(encoding).build().getEncoding();
    assertEquals("UTF-8", encoding2.name());
    assertSame(encoding, encoding2);
  }

  /**
   * Test Builder new {@link Builder} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link Builder}
   */
  @Test
  @DisplayName("Test Builder new Builder (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void Builder.<init>()"})
  void testBuilderNewBuilder() {
    // Arrange, Act and Assert
    assertEquals("US-ASCII", new Builder().build().getEncoding().name());
  }

  /**
   * Test Builder {@link Builder#setPriceFractionDigits(int)} with {@code fractionDigits}.
   *
   * <p>Method under test: {@link Builder#setPriceFractionDigits(int)}
   */
  @Test
  @DisplayName("Test Builder setPriceFractionDigits(int) with 'fractionDigits'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Builder Builder.setPriceFractionDigits(int)"})
  void testBuilderSetPriceFractionDigitsWithFractionDigits() {
    // Arrange
    Builder builder = new Builder();

    // Act
    Builder actualSetPriceFractionDigitsResult = builder.setPriceFractionDigits(1);

    // Assert
    assertEquals("US-ASCII", actualSetPriceFractionDigitsResult.build().getEncoding().name());
    assertSame(builder, actualSetPriceFractionDigitsResult);
  }

  /**
   * Test Builder {@link Builder#setPriceFractionDigits(String, int)} with {@code instrument},
   * {@code fractionDigits}.
   *
   * <p>Method under test: {@link Builder#setPriceFractionDigits(String, int)}
   */
  @Test
  @DisplayName(
      "Test Builder setPriceFractionDigits(String, int) with 'instrument', 'fractionDigits'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Builder Builder.setPriceFractionDigits(String, int)"})
  void testBuilderSetPriceFractionDigitsWithInstrumentFractionDigits() {
    // Arrange
    Builder builder = new Builder();

    // Act
    Builder actualSetPriceFractionDigitsResult = builder.setPriceFractionDigits("Instrument", 1);

    // Assert
    assertEquals("US-ASCII", actualSetPriceFractionDigitsResult.build().getEncoding().name());
    assertSame(builder, actualSetPriceFractionDigitsResult);
  }

  /**
   * Test Builder {@link Builder#setSizeFractionDigits(int)} with {@code fractionDigits}.
   *
   * <p>Method under test: {@link Builder#setSizeFractionDigits(int)}
   */
  @Test
  @DisplayName("Test Builder setSizeFractionDigits(int) with 'fractionDigits'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Builder Builder.setSizeFractionDigits(int)"})
  void testBuilderSetSizeFractionDigitsWithFractionDigits() {
    // Arrange
    Builder builder = new Builder();

    // Act
    Builder actualSetSizeFractionDigitsResult = builder.setSizeFractionDigits(1);

    // Assert
    assertEquals("US-ASCII", actualSetSizeFractionDigitsResult.build().getEncoding().name());
    assertSame(builder, actualSetSizeFractionDigitsResult);
  }

  /**
   * Test Builder {@link Builder#setSizeFractionDigits(String, int)} with {@code instrument}, {@code
   * fractionDigits}.
   *
   * <p>Method under test: {@link Builder#setSizeFractionDigits(String, int)}
   */
  @Test
  @DisplayName(
      "Test Builder setSizeFractionDigits(String, int) with 'instrument', 'fractionDigits'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Builder Builder.setSizeFractionDigits(String, int)"})
  void testBuilderSetSizeFractionDigitsWithInstrumentFractionDigits() {
    // Arrange
    Builder builder = new Builder();

    // Act
    Builder actualSetSizeFractionDigitsResult = builder.setSizeFractionDigits("Instrument", 1);

    // Assert
    assertEquals("US-ASCII", actualSetSizeFractionDigitsResult.build().getEncoding().name());
    assertSame(builder, actualSetSizeFractionDigitsResult);
  }

  /**
   * Test {@link TAQConfig#getEncoding()}.
   *
   * <p>Method under test: {@link TAQConfig#getEncoding()}
   */
  @Test
  @DisplayName("Test getEncoding()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Charset TAQConfig.getEncoding()"})
  void testGetEncoding() {
    // Arrange, Act and Assert
    assertEquals("US-ASCII", TAQConfig.DEFAULTS.getEncoding().name());
  }

  /**
   * Test {@link TAQConfig#getPriceFormat(String)}.
   *
   * <p>Method under test: {@link TAQConfig#getPriceFormat(String)}
   */
  @Test
  @DisplayName("Test getPriceFormat(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"DecimalFormat TAQConfig.getPriceFormat(String)"})
  void testGetPriceFormat() {
    // Arrange and Act
    DecimalFormat actualPriceFormat = TAQConfig.DEFAULTS.getPriceFormat("Instrument");

    // Assert
    assertEquals("", actualPriceFormat.getNegativeSuffix());
    assertEquals("", actualPriceFormat.getPositivePrefix());
    assertEquals("", actualPriceFormat.getPositiveSuffix());
    assertEquals("#0.00", actualPriceFormat.toLocalizedPattern());
    assertEquals("#0.00", actualPriceFormat.toPattern());
    assertEquals("-", actualPriceFormat.getNegativePrefix());
    assertEquals(0, actualPriceFormat.getGroupingSize());
    assertEquals(1, actualPriceFormat.getMinimumIntegerDigits());
    assertEquals(1, actualPriceFormat.getMultiplier());
    assertEquals(2, actualPriceFormat.getMaximumFractionDigits());
    assertEquals(2, actualPriceFormat.getMinimumFractionDigits());
    assertEquals(RoundingMode.HALF_EVEN, actualPriceFormat.getRoundingMode());
    assertFalse(actualPriceFormat.isDecimalSeparatorAlwaysShown());
    assertFalse(actualPriceFormat.isParseBigDecimal());
    assertFalse(actualPriceFormat.isGroupingUsed());
    assertFalse(actualPriceFormat.isParseIntegerOnly());
    assertEquals(Integer.MAX_VALUE, actualPriceFormat.getMaximumIntegerDigits());
  }

  /**
   * Test {@link TAQConfig#getSizeFormat(String)}.
   *
   * <p>Method under test: {@link TAQConfig#getSizeFormat(String)}
   */
  @Test
  @DisplayName("Test getSizeFormat(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"DecimalFormat TAQConfig.getSizeFormat(String)"})
  void testGetSizeFormat() {
    // Arrange and Act
    DecimalFormat actualSizeFormat = TAQConfig.DEFAULTS.getSizeFormat("Instrument");

    // Assert
    assertEquals("", actualSizeFormat.getNegativeSuffix());
    assertEquals("", actualSizeFormat.getPositivePrefix());
    assertEquals("", actualSizeFormat.getPositiveSuffix());
    assertEquals("#0", actualSizeFormat.toLocalizedPattern());
    assertEquals("#0", actualSizeFormat.toPattern());
    assertEquals("-", actualSizeFormat.getNegativePrefix());
    assertEquals(0, actualSizeFormat.getGroupingSize());
    assertEquals(0, actualSizeFormat.getMaximumFractionDigits());
    assertEquals(0, actualSizeFormat.getMinimumFractionDigits());
    assertEquals(1, actualSizeFormat.getMinimumIntegerDigits());
    assertEquals(1, actualSizeFormat.getMultiplier());
    assertEquals(RoundingMode.HALF_EVEN, actualSizeFormat.getRoundingMode());
    assertFalse(actualSizeFormat.isDecimalSeparatorAlwaysShown());
    assertFalse(actualSizeFormat.isParseBigDecimal());
    assertFalse(actualSizeFormat.isGroupingUsed());
    assertFalse(actualSizeFormat.isParseIntegerOnly());
    assertEquals(Integer.MAX_VALUE, actualSizeFormat.getMaximumIntegerDigits());
  }
}
