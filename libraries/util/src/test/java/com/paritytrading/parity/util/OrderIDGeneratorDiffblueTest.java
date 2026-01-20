package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderIDGeneratorDiffblueTest {
  /**
   * Test {@link OrderIDGenerator#OrderIDGenerator()}.
   *
   * <p>Method under test: {@link OrderIDGenerator#OrderIDGenerator()}
   */
  @Test
  @DisplayName("Test new OrderIDGenerator()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderIDGenerator.<init>()"})
  void testNewOrderIDGenerator() {
    // Arrange, Act and Assert
    assertEquals(1, new OrderIDGenerator().getCount());
  }

  /**
   * Test {@link OrderIDGenerator#OrderIDGenerator(LocalTime)}.
   *
   * <ul>
   *   <li>When {@link LocalTime#MIDNIGHT}.
   *   <li>Then return Prefix is {@code 00:00:00}.
   * </ul>
   *
   * <p>Method under test: {@link OrderIDGenerator#OrderIDGenerator(LocalTime)}
   */
  @Test
  @DisplayName(
      "Test new OrderIDGenerator(LocalTime); when MIDNIGHT; then return Prefix is '00:00:00'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OrderIDGenerator.<init>(LocalTime)"})
  void testNewOrderIDGenerator_whenMidnight_thenReturnPrefixIs000000() {
    // Arrange and Act
    OrderIDGenerator actualOrderIDGenerator = new OrderIDGenerator(LocalTime.MIDNIGHT);

    // Assert
    assertEquals("00:00:00", actualOrderIDGenerator.getPrefix());
    assertEquals(1, actualOrderIDGenerator.getCount());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link OrderIDGenerator#getCount()}
   *   <li>{@link OrderIDGenerator#getPrefix()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int OrderIDGenerator.getCount()",
    "java.lang.String OrderIDGenerator.getPrefix()"
  })
  void testGettersAndSetters() {
    // Arrange
    OrderIDGenerator orderIDGenerator = new OrderIDGenerator();

    // Act
    int actualCount = orderIDGenerator.getCount();
    orderIDGenerator.getPrefix();

    // Assert
    assertEquals(1, actualCount);
  }

  /**
   * Test {@link OrderIDGenerator#next()}.
   *
   * <p>Method under test: {@link OrderIDGenerator#next()}
   */
  @Test
  @DisplayName("Test next()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String OrderIDGenerator.next()"})
  void testNext() {
    // Arrange
    OrderIDGenerator orderIDGenerator = new OrderIDGenerator();

    // Act
    orderIDGenerator.next();

    // Assert
    assertEquals(2, orderIDGenerator.getCount());
  }
}
