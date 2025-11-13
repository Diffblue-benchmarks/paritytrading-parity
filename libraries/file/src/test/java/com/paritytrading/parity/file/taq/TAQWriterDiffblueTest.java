package com.paritytrading.parity.file.taq;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TAQWriterDiffblueTest {
  /**
   * Test {@link TAQWriter#TAQWriter(OutputStream, TAQConfig)}.
   *
   * <ul>
   *   <li>Given forName {@code UTF-8}.
   *   <li>Then calls {@link TAQConfig#getEncoding()}.
   * </ul>
   *
   * <p>Method under test: {@link TAQWriter#TAQWriter(OutputStream, TAQConfig)}
   */
  @Test
  @DisplayName(
      "Test new TAQWriter(OutputStream, TAQConfig); given forName 'UTF-8'; then calls getEncoding()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TAQWriter.<init>(OutputStream, TAQConfig)"})
  void testNewTAQWriter_givenForNameUtf8_thenCallsGetEncoding() {
    // Arrange
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    TAQConfig config = mock(TAQConfig.class);
    when(config.getEncoding()).thenReturn(Charset.forName("UTF-8"));

    // Act
    new TAQWriter(out, config);

    // Assert
    verify(config).getEncoding();
  }
}
