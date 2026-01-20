package com.paritytrading.parity.net.pmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
   *   <li>Then wrap {@code VXAXAXAX} Bytes is {@code UTF-8} position is five.
   * </ul>
   *
   * <p>Method under test: {@link PMDParser#PMDParser(PMDListener)}
   */
  @Test
  @DisplayName(
      "Test new PMDParser(PMDListener); then wrap 'VXAXAXAX' Bytes is 'UTF-8' position is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMDParser.<init>(PMDListener)"})
  void testNewPMDParser_thenWrapVxaxaxaxBytesIsUtf8PositionIsFive() throws IOException {
    // Arrange
    PMDListener listener = mock(PMDListener.class);
    doNothing().when(listener).version(Mockito.<Version>any());

    // Act
    PMDParser actualPmdParser = new PMDParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));
    actualPmdParser.message(buffer);

    // Assert
    verify(listener).version(isA(Version.class));
    assertEquals(5, buffer.position());
  }
}
