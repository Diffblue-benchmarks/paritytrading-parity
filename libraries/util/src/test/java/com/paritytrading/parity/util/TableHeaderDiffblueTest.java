package com.paritytrading.parity.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TableHeaderDiffblueTest {
  /**
   * Method under test: {@link TableHeader#add(String, int)}
   */
  @Test
  void testAdd() {
    // Arrange
    TableHeader tableHeader = new TableHeader();

    // Act
    tableHeader.add("Name", 1);

    // Assert
    assertEquals("N\n-\n", tableHeader.format());
  }

  /**
   * Method under test: {@link TableHeader#format()}
   */
  @Test
  void testFormat() {
    // Arrange, Act and Assert
    assertEquals("", (new TableHeader()).format());
  }

  /**
   * Method under test: {@link TableHeader#format()}
   */
  @Test
  void testFormat2() {
    // Arrange
    TableHeader tableHeader = new TableHeader();
    tableHeader.add("Name", 1);

    // Act and Assert
    assertEquals("N\n-\n", tableHeader.format());
  }

  /**
   * Method under test: {@link TableHeader#format()}
   */
  @Test
  void testFormat3() {
    // Arrange
    TableHeader tableHeader = new TableHeader();
    tableHeader.add(".", 2);
    tableHeader.add("Name", 1);

    // Act and Assert
    assertEquals(".  N\n-- -\n", tableHeader.format());
  }
}
