package com.paritytrading.parity.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ProtocolExceptionDiffblueTest {
  /**
   * Test {@link ProtocolException#ProtocolException(String)}.
   * <ul>
   *   <li>When {@code An error occurred}.</li>
   *   <li>Then return Cause is {@code null}.</li>
   * </ul>
   * <p>
   * Method under test: {@link ProtocolException#ProtocolException(String)}
   */
  @Test
  @DisplayName("Test new ProtocolException(String); when 'An error occurred'; then return Cause is 'null'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void ProtocolException.<init>(String)", "void ProtocolException.<init>(String, Throwable)",
      "void ProtocolException.<init>(Throwable)"})
  void testNewProtocolException_whenAnErrorOccurred_thenReturnCauseIsNull() {
    // Arrange and Act
    ProtocolException actualProtocolException = new ProtocolException("An error occurred");

    // Assert
    assertEquals("An error occurred", actualProtocolException.getMessage());
    assertNull(actualProtocolException.getCause());
    assertEquals(0, actualProtocolException.getSuppressed().length);
  }

  /**
   * Test {@link ProtocolException#ProtocolException(String, Throwable)}.
   * <ul>
   *   <li>When {@code An error occurred}.</li>
   *   <li>Then return Message is {@code An error occurred}.</li>
   * </ul>
   * <p>
   * Method under test: {@link ProtocolException#ProtocolException(String, Throwable)}
   */
  @Test
  @DisplayName("Test new ProtocolException(String, Throwable); when 'An error occurred'; then return Message is 'An error occurred'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void ProtocolException.<init>(String)", "void ProtocolException.<init>(String, Throwable)",
      "void ProtocolException.<init>(Throwable)"})
  void testNewProtocolException_whenAnErrorOccurred_thenReturnMessageIsAnErrorOccurred() {
    // Arrange
    Throwable cause = new Throwable();

    // Act
    ProtocolException actualProtocolException = new ProtocolException("An error occurred", cause);

    // Assert
    assertEquals("An error occurred", actualProtocolException.getMessage());
    assertEquals(0, actualProtocolException.getSuppressed().length);
    assertSame(cause, actualProtocolException.getCause());
  }

  /**
   * Test {@link ProtocolException#ProtocolException(Throwable)}.
   * <ul>
   *   <li>When {@link Throwable#Throwable()}.</li>
   *   <li>Then return Message is {@code Throwable}.</li>
   * </ul>
   * <p>
   * Method under test: {@link ProtocolException#ProtocolException(Throwable)}
   */
  @Test
  @DisplayName("Test new ProtocolException(Throwable); when Throwable(); then return Message is 'java.lang.Throwable'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void ProtocolException.<init>(String)", "void ProtocolException.<init>(String, Throwable)",
      "void ProtocolException.<init>(Throwable)"})
  void testNewProtocolException_whenThrowable_thenReturnMessageIsJavaLangThrowable() {
    // Arrange
    Throwable cause = new Throwable();

    // Act
    ProtocolException actualProtocolException = new ProtocolException(cause);

    // Assert
    assertEquals("java.lang.Throwable", actualProtocolException.getMessage());
    assertEquals(0, actualProtocolException.getSuppressed().length);
    assertSame(cause, actualProtocolException.getCause());
  }
}
