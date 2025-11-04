package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import java.util.HashSet;
import java.util.Iterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InstrumentsDiffblueTest {
  /**
   * Test {@link Instruments#get(long)} with {@code long}.
   * <ul>
   *   <li>Given {@link Config} {@link Config#getInt(String)} return one.</li>
   *   <li>Then return {@code null}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#get(long)}
   */
  @Test
  @DisplayName("Test get(long) with 'long'; given Config getInt(String) return one; then return 'null'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instrument Instruments.get(long)"})
  void testGetWithLong_givenConfigGetIntReturnOne_thenReturnNull() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    when(config.hasPath(Mockito.<String>any())).thenReturn(true);
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(new HashSet<>());
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instrument actualGetResult = Instruments.fromConfig(config2, "Path").get(1L);

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertNull(actualGetResult);
  }

  /**
   * Test {@link Instruments#get(long)} with {@code long}.
   * <ul>
   *   <li>Given {@link HashSet#HashSet()} add {@code foo}.</li>
   *   <li>Then return {@code null}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#get(long)}
   */
  @Test
  @DisplayName("Test get(long) with 'long'; given HashSet() add 'foo'; then return 'null'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instrument Instruments.get(long)"})
  void testGetWithLong_givenHashSetAddFoo_thenReturnNull() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    when(config.hasPath(Mockito.<String>any())).thenReturn(true);

    HashSet<String> stringSet = new HashSet<>();
    stringSet.add("foo");
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(stringSet);
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instrument actualGetResult = Instruments.fromConfig(config2, "Path").get(1L);

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertNull(actualGetResult);
  }

  /**
   * Test {@link Instruments#get(String)} with {@code String}.
   * <ul>
   *   <li>Given {@link Config} {@link Config#getInt(String)} return one.</li>
   *   <li>When {@code Instrument}.</li>
   *   <li>Then return {@code null}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#get(String)}
   */
  @Test
  @DisplayName("Test get(String) with 'String'; given Config getInt(String) return one; when 'Instrument'; then return 'null'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instrument Instruments.get(String)"})
  void testGetWithString_givenConfigGetIntReturnOne_whenInstrument_thenReturnNull() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    when(config.hasPath(Mockito.<String>any())).thenReturn(true);
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(new HashSet<>());
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instrument actualGetResult = Instruments.fromConfig(config2, "Path").get("Instrument");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertNull(actualGetResult);
  }

  /**
   * Test {@link Instruments#get(String)} with {@code String}.
   * <ul>
   *   <li>Given {@link HashSet#HashSet()} add {@code foo}.</li>
   *   <li>When {@code foo}.</li>
   *   <li>Then return PriceFormat is {@code %3.1f}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#get(String)}
   */
  @Test
  @DisplayName("Test get(String) with 'String'; given HashSet() add 'foo'; when 'foo'; then return PriceFormat is '%3.1f'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instrument Instruments.get(String)"})
  void testGetWithString_givenHashSetAddFoo_whenFoo_thenReturnPriceFormatIs31f() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    when(config.hasPath(Mockito.<String>any())).thenReturn(true);

    HashSet<String> stringSet = new HashSet<>();
    stringSet.add("foo");
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(stringSet);
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instrument actualGetResult = Instruments.fromConfig(config2, "Path").get("foo");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertEquals("%3.1f", actualGetResult.getPriceFormat());
    assertEquals("%3.1f", actualGetResult.getSizeFormat());
    assertEquals("foo", actualGetResult.asString());
    assertEquals(1, actualGetResult.getPriceFractionDigits());
    assertEquals(1, actualGetResult.getSizeFractionDigits());
    assertEquals(10.0d, actualGetResult.getPriceFactor());
    assertEquals(10.0d, actualGetResult.getSizeFactor());
    assertEquals(7381240498052145184L, actualGetResult.asLong());
  }

  /**
   * Test {@link Instruments#get(String)} with {@code String}.
   * <ul>
   *   <li>Given {@link HashSet#HashSet()} add {@code foo}.</li>
   *   <li>When {@code Instrument}.</li>
   *   <li>Then return {@code null}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#get(String)}
   */
  @Test
  @DisplayName("Test get(String) with 'String'; given HashSet() add 'foo'; when 'Instrument'; then return 'null'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instrument Instruments.get(String)"})
  void testGetWithString_givenHashSetAddFoo_whenInstrument_thenReturnNull() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    when(config.hasPath(Mockito.<String>any())).thenReturn(true);

    HashSet<String> stringSet = new HashSet<>();
    stringSet.add("foo");
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(stringSet);
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instrument actualGetResult = Instruments.fromConfig(config2, "Path").get("Instrument");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertNull(actualGetResult);
  }

  /**
   * Test {@link Instruments#iterator()}.
   * <ul>
   *   <li>Given {@link Config} {@link Config#getInt(String)} return one.</li>
   *   <li>Then return not hasNext.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#iterator()}
   */
  @Test
  @DisplayName("Test iterator(); given Config getInt(String) return one; then return not hasNext")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Iterator Instruments.iterator()"})
  void testIterator_givenConfigGetIntReturnOne_thenReturnNotHasNext() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    when(config.hasPath(Mockito.<String>any())).thenReturn(true);
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(new HashSet<>());
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Iterator<Instrument> actualIteratorResult = Instruments.fromConfig(config2, "Path").iterator();

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertFalse(actualIteratorResult.hasNext());
  }

  /**
   * Test {@link Instruments#fromConfig(Config, String)}.
   * <ul>
   *   <li>Given {@link Config} {@link Config#getInt(String)} return one.</li>
   *   <li>Then return MaxPriceFractionDigits is one.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  @DisplayName("Test fromConfig(Config, String); given Config getInt(String) return one; then return MaxPriceFractionDigits is one")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instruments Instruments.fromConfig(Config, String)"})
  void testFromConfig_givenConfigGetIntReturnOne_thenReturnMaxPriceFractionDigitsIsOne() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    when(config.hasPath(Mockito.<String>any())).thenReturn(false);

    HashSet<String> stringSet = new HashSet<>();
    stringSet.add("foo");
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(stringSet);
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instruments actualFromConfigResult = Instruments.fromConfig(config2, "Path");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertEquals(" - ", actualFromConfigResult.getPricePlaceholder());
    assertEquals(" - ", actualFromConfigResult.getSizePlaceholder());
    assertEquals(1, actualFromConfigResult.getMaxPriceFractionDigits());
    assertEquals(1, actualFromConfigResult.getMaxSizeFractionDigits());
    assertEquals(3, actualFromConfigResult.getPriceWidth());
    assertEquals(3, actualFromConfigResult.getSizeWidth());
  }

  /**
   * Test {@link Instruments#fromConfig(Config, String)}.
   * <ul>
   *   <li>Given {@link Config} {@link Config#getInt(String)} return one.</li>
   *   <li>Then return MaxPriceFractionDigits is zero.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  @DisplayName("Test fromConfig(Config, String); given Config getInt(String) return one; then return MaxPriceFractionDigits is zero")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instruments Instruments.fromConfig(Config, String)"})
  void testFromConfig_givenConfigGetIntReturnOne_thenReturnMaxPriceFractionDigitsIsZero() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(1);
    when(config.hasPath(Mockito.<String>any())).thenReturn(true);
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(new HashSet<>());
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instruments actualFromConfigResult = Instruments.fromConfig(config2, "Path");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertEquals("-", actualFromConfigResult.getPricePlaceholder());
    assertEquals("-", actualFromConfigResult.getSizePlaceholder());
    assertEquals(0, actualFromConfigResult.getMaxPriceFractionDigits());
    assertEquals(0, actualFromConfigResult.getMaxSizeFractionDigits());
    assertEquals(1, actualFromConfigResult.getPriceWidth());
    assertEquals(1, actualFromConfigResult.getSizeWidth());
  }

  /**
   * Test {@link Instruments#fromConfig(Config, String)}.
   * <ul>
   *   <li>Given {@link Config} {@link Config#getInt(String)} return zero.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  @DisplayName("Test fromConfig(Config, String); given Config getInt(String) return zero")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instruments Instruments.fromConfig(Config, String)"})
  void testFromConfig_givenConfigGetIntReturnZero() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(0);
    when(config.hasPath(Mockito.<String>any())).thenReturn(false);

    HashSet<String> stringSet = new HashSet<>();
    stringSet.add("foo");
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(stringSet);
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instruments actualFromConfigResult = Instruments.fromConfig(config2, "Path");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertEquals("-", actualFromConfigResult.getPricePlaceholder());
    assertEquals("-", actualFromConfigResult.getSizePlaceholder());
    assertEquals(0, actualFromConfigResult.getMaxPriceFractionDigits());
    assertEquals(0, actualFromConfigResult.getMaxSizeFractionDigits());
    assertEquals(1, actualFromConfigResult.getPriceWidth());
    assertEquals(1, actualFromConfigResult.getSizeWidth());
  }

  /**
   * Test {@link Instruments#fromConfig(Config, String)}.
   * <ul>
   *   <li>Given {@link HashSet#HashSet()} add {@code .price-fraction-digits}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  @DisplayName("Test fromConfig(Config, String); given HashSet() add '.price-fraction-digits'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instruments Instruments.fromConfig(Config, String)"})
  void testFromConfig_givenHashSetAddPriceFractionDigits() {
    // Arrange
    Config config = mock(Config.class);
    when(config.getInt(Mockito.<String>any())).thenReturn(0);
    when(config.hasPath(Mockito.<String>any())).thenReturn(false);

    HashSet<String> stringSet = new HashSet<>();
    stringSet.add(".price-fraction-digits");
    stringSet.add("foo");
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(stringSet);
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instruments actualFromConfigResult = Instruments.fromConfig(config2, "Path");

    // Assert
    verify(config, atLeast(1)).getInt(Mockito.<String>any());
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertEquals("-", actualFromConfigResult.getPricePlaceholder());
    assertEquals("-", actualFromConfigResult.getSizePlaceholder());
    assertEquals(0, actualFromConfigResult.getMaxPriceFractionDigits());
    assertEquals(0, actualFromConfigResult.getMaxSizeFractionDigits());
    assertEquals(1, actualFromConfigResult.getPriceWidth());
    assertEquals(1, actualFromConfigResult.getSizeWidth());
  }

  /**
   * Test {@link Instruments#fromConfig(Config, String)}.
   * <ul>
   *   <li>Then return MaxPriceFractionDigits is zero.</li>
   * </ul>
   * <p>
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  @DisplayName("Test fromConfig(Config, String); then return MaxPriceFractionDigits is zero")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"Instruments Instruments.fromConfig(Config, String)"})
  void testFromConfig_thenReturnMaxPriceFractionDigitsIsZero() {
    // Arrange
    Config config = mock(Config.class);
    when(config.hasPath(Mockito.<String>any())).thenReturn(false);
    ConfigObject configObject = mock(ConfigObject.class);
    when(configObject.keySet()).thenReturn(new HashSet<>());
    when(configObject.toConfig()).thenReturn(config);
    Config config2 = mock(Config.class);
    when(config2.getObject(Mockito.<String>any())).thenReturn(configObject);

    // Act
    Instruments actualFromConfigResult = Instruments.fromConfig(config2, "Path");

    // Assert
    verify(config2).getObject(eq("Path"));
    verify(config, atLeast(1)).hasPath(Mockito.<String>any());
    verify(configObject).toConfig();
    verify(configObject).keySet();
    assertEquals("-", actualFromConfigResult.getPricePlaceholder());
    assertEquals("-", actualFromConfigResult.getSizePlaceholder());
    assertEquals(0, actualFromConfigResult.getMaxPriceFractionDigits());
    assertEquals(0, actualFromConfigResult.getMaxSizeFractionDigits());
    assertEquals(1, actualFromConfigResult.getPriceWidth());
    assertEquals(1, actualFromConfigResult.getSizeWidth());
  }
}
