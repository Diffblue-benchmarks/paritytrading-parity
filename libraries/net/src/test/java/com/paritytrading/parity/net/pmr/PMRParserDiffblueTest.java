package com.paritytrading.parity.net.pmr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.pmr.PMR.OrderAdded;
import com.paritytrading.parity.net.pmr.PMR.Version;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PMRParserDiffblueTest {
  /**
   * Test {@link PMRParser#PMRParser(PMRListener)}.
   *
   * <ul>
   *   <li>Then wrap array of {@code byte} with {@code A} and one position is seventeen.
   * </ul>
   *
   * <p>Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  @DisplayName(
      "Test new PMRParser(PMRListener); then wrap array of byte with 'A' and one position is seventeen")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.<init>(PMRListener)"})
  void testNewPMRParser_thenWrapArrayOfByteWithAAndOnePositionIsSeventeen() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderAdded(Mockito.<OrderAdded>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    ByteBuffer buffer =
        ByteBuffer.wrap(
            new byte[] {
              'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A',
              1, 'A', 1
            });
    actualPmrParser.message(buffer);

    // Assert
    verify(listener).orderAdded(isA(OrderAdded.class));
    assertEquals(17, buffer.position());
  }

  /**
   * Test {@link PMRParser#PMRParser(PMRListener)}.
   *
   * <ul>
   *   <li>Then wrap {@code VXAXAXAX} Bytes is {@code UTF-8} position is five.
   * </ul>
   *
   * <p>Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  @DisplayName(
      "Test new PMRParser(PMRListener); then wrap 'VXAXAXAX' Bytes is 'UTF-8' position is five")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.<init>(PMRListener)"})
  void testNewPMRParser_thenWrapVxaxaxaxBytesIsUtf8PositionIsFive() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).version(Mockito.<Version>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    ByteBuffer buffer = ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8"));
    actualPmrParser.message(buffer);

    // Assert
    verify(listener).version(isA(Version.class));
    assertEquals(5, buffer.position());
  }
}
