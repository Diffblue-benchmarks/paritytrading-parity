package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import java.util.HashSet;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InstrumentsDiffblueTest {
  /**
   * Method under test: {@link Instruments#get(long)}
   */
  @Test
  void testGet() {
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
   * Method under test: {@link Instruments#get(long)}
   */
  @Test
  void testGet2() {
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
   * Method under test: {@link Instruments#get(String)}
   */
  @Test
  void testGet3() {
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
   * Method under test: {@link Instruments#get(String)}
   */
  @Test
  void testGet4() {
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
   * Method under test: {@link Instruments#get(String)}
   */
  @Test
  void testGet5() {
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
   * Method under test: {@link Instruments#iterator()}
   */
  @Test
  void testIterator() {
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
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  void testFromConfig() {
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
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  void testFromConfig2() {
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

  /**
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  void testFromConfig3() {
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
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  void testFromConfig4() {
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
   * Method under test: {@link Instruments#fromConfig(Config, String)}
   */
  @Test
  void testFromConfig5() {
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
}
