package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TableHeaderDiffblueTest {
  /**
   * Test {@link TableHeader#add(String, int)}.
   *
   * <p>Method under test: {@link TableHeader#add(String, int)}
   */
  @Test
  @DisplayName("Test add(String, int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TableHeader.add(String, int)"})
  void testAdd() {
    // Arrange
    TableHeader tableHeader = new TableHeader();

    // Act
    tableHeader.add("Name", 1);

    // Assert
    assertEquals("N\n-\n", tableHeader.format());
  }

  /**
   * Test {@link TableHeader#format()}.
   *
   * <ul>
   *   <li>Given {@link TableHeader} (default constructor) add {@code .} and two.
   *   <li>Then return {@code . N -- -}.
   * </ul>
   *
   * <p>Method under test: {@link TableHeader#format()}
   */
  @Test
  @DisplayName(
      "Test format(); given TableHeader (default constructor) add '.' and two; then return '. N -- -'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String TableHeader.format()"})
  void testFormat_givenTableHeaderAddDotAndTwo_thenReturnN() {
    // Arrange
    TableHeader tableHeader = new TableHeader();
    tableHeader.add(".", 2);
    tableHeader.add("Name", 1);

    // Act and Assert
    assertEquals(".  N\n-- -\n", tableHeader.format());
  }

  /**
   * Test {@link TableHeader#format()}.
   *
   * <ul>
   *   <li>Given {@link TableHeader} (default constructor) add {@code Name} and one.
   *   <li>Then return {@code N -}.
   * </ul>
   *
   * <p>Method under test: {@link TableHeader#format()}
   */
  @Test
  @DisplayName(
      "Test format(); given TableHeader (default constructor) add 'Name' and one; then return 'N -'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String TableHeader.format()"})
  void testFormat_givenTableHeaderAddNameAndOne_thenReturnN() {
    // Arrange
    TableHeader tableHeader = new TableHeader();
    tableHeader.add("Name", 1);

    // Act and Assert
    assertEquals("N\n-\n", tableHeader.format());
  }

  /**
   * Test {@link TableHeader#format()}.
   *
   * <ul>
   *   <li>Given {@link TableHeader} (default constructor).
   *   <li>Then return empty string.
   * </ul>
   *
   * <p>Method under test: {@link TableHeader#format()}
   */
  @Test
  @DisplayName("Test format(); given TableHeader (default constructor); then return empty string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String TableHeader.format()"})
  void testFormat_givenTableHeader_thenReturnEmptyString() {
    // Arrange, Act and Assert
    assertEquals("", new TableHeader().format());
  }
}
