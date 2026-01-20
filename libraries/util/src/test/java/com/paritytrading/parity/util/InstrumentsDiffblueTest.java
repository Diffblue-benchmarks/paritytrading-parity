package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.Iterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class InstrumentsDiffblueTest {
  /**
   * Test {@link Instruments#get(long)} with {@code long}.
   *
   * <p>Method under test: {@link Instruments#get(long)}
   */
  @Test
  @DisplayName("Test get(long) with 'long'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Instrument Instruments.get(long)"})
  void testGetWithLong() {
    // Arrange, Act and Assert
    assertNull(InstrumentsFactory.createInstruments().get(1L));
  }

  /**
   * Test {@link Instruments#get(String)} with {@code String}.
   *
   * <ul>
   *   <li>When {@code FOO}.
   *   <li>Then return PriceFormat is {@code %7.2f}.
   * </ul>
   *
   * <p>Method under test: {@link Instruments#get(String)}
   */
  @Test
  @DisplayName("Test get(String) with 'String'; when 'FOO'; then return PriceFormat is '%7.2f'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Instrument Instruments.get(String)"})
  void testGetWithString_whenFoo_thenReturnPriceFormatIs72f() {
    // Arrange and Act
    Instrument actualGetResult = InstrumentsFactory.createInstruments().get("FOO");

    // Assert
    assertEquals("%7.2f", actualGetResult.getPriceFormat());
    assertEquals("%8.0f", actualGetResult.getSizeFormat());
    assertEquals("FOO", actualGetResult.asString());
    assertEquals(0, actualGetResult.getSizeFractionDigits());
    assertEquals(1.0d, actualGetResult.getSizeFactor());
    assertEquals(100.0d, actualGetResult.getPriceFactor());
    assertEquals(2, actualGetResult.getPriceFractionDigits());
    assertEquals(5066355105211621408L, actualGetResult.asLong());
  }

  /**
   * Test {@link Instruments#get(String)} with {@code String}.
   *
   * <ul>
   *   <li>When {@code Instrument}.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link Instruments#get(String)}
   */
  @Test
  @DisplayName("Test get(String) with 'String'; when 'Instrument'; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Instrument Instruments.get(String)"})
  void testGetWithString_whenInstrument_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(InstrumentsFactory.createInstruments().get("Instrument"));
  }

  /**
   * Test {@link Instruments#iterator()}.
   *
   * <p>Method under test: {@link Instruments#iterator()}
   */
  @Test
  @DisplayName("Test iterator()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Iterator Instruments.iterator()"})
  void testIterator() {
    // Arrange and Act
    Iterator<Instrument> actualIteratorResult = InstrumentsFactory.createInstruments().iterator();

    // Assert
    Instrument nextResult = actualIteratorResult.next();
    assertEquals("%7.2f", nextResult.getPriceFormat());
    assertEquals("%8.0f", nextResult.getSizeFormat());
    assertEquals("FOO", nextResult.asString());
    assertEquals(0, nextResult.getSizeFractionDigits());
    assertEquals(1.0d, nextResult.getSizeFactor());
    assertEquals(100.0d, nextResult.getPriceFactor());
    assertEquals(2, nextResult.getPriceFractionDigits());
    assertEquals(5066355105211621408L, nextResult.asLong());
    assertFalse(actualIteratorResult.hasNext());
  }
}
