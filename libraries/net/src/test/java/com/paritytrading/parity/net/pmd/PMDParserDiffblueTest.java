package com.paritytrading.parity.net.pmd;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmd.PMD.Version;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PMDParserDiffblueTest {
  /**
   * Test {@link PMDParser#PMDParser(PMDListener)}.
   *
   * <ul>
   *   <li>When {@link PMDListener} {@link PMDListener#version(Version)} does nothing.
   *   <li>Then calls {@link PMDListener#version(Version)}.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#PMDParser(PMDListener)}
   */
  @Test
  @DisplayName(
      "Test new PMDParser(PMDListener); when PMDListener version(Version) does nothing; then calls version(Version)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.<init>(PMDListener)"})
  void testNewPMDParser_whenPMDListenerVersionDoesNothing_thenCallsVersion() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).version(Mockito.<Version>any());

    // Act
    PMDParser actualPmdParser = new PMDParser(listener);
    actualPmdParser.message(ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8")));

    // Assert
    verify(listener).version(isA(Version.class));
  }
}
