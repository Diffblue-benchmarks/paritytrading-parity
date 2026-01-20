package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ErrorsCommandDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link ErrorsCommand}
   *   <li>{@link ErrorsCommand#getDescription()}
   *   <li>{@link ErrorsCommand#getName()}
   *   <li>{@link ErrorsCommand#getUsage()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ErrorsCommand.<init>()",
    "String ErrorsCommand.getDescription()",
    "String ErrorsCommand.getName()",
    "String ErrorsCommand.getUsage()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    ErrorsCommand actualErrorsCommand = new ErrorsCommand();
    String actualDescription = actualErrorsCommand.getDescription();
    String actualName = actualErrorsCommand.getName();

    // Assert
    assertEquals("Display occurred errors", actualDescription);
    assertEquals("errors", actualName);
    assertEquals("errors", actualErrorsCommand.getUsage());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link ErrorsCommand}
   *   <li>{@link ErrorsCommand#getDescription()}
   *   <li>{@link ErrorsCommand#getName()}
   *   <li>{@link ErrorsCommand#getUsage()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ErrorsCommand.<init>()",
    "String ErrorsCommand.getDescription()",
    "String ErrorsCommand.getName()",
    "String ErrorsCommand.getUsage()"
  })
  void testGettersAndSetters2() {
    // Arrange and Act
    ErrorsCommand actualErrorsCommand = new ErrorsCommand();
    String actualDescription = actualErrorsCommand.getDescription();
    String actualName = actualErrorsCommand.getName();

    // Assert
    assertEquals("Display occurred errors", actualDescription);
    assertEquals("errors", actualName);
    assertEquals("errors", actualErrorsCommand.getUsage());
  }
}
