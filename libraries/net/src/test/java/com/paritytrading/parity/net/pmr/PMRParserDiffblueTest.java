package com.paritytrading.parity.net.pmr;

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
   *   <li>When {@link PMRListener} {@link PMRListener#orderAdded(OrderAdded)} does nothing.
   *   <li>Then calls {@link PMRListener#orderAdded(OrderAdded)}.
   * </ul>
   *
   * <p>Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  @DisplayName(
      "Test new PMRParser(PMRListener); when PMRListener orderAdded(OrderAdded) does nothing; then calls orderAdded(OrderAdded)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.<init>(PMRListener)"})
  void testNewPMRParser_whenPMRListenerOrderAddedDoesNothing_thenCallsOrderAdded()
      throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).orderAdded(Mockito.<OrderAdded>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    actualPmrParser.message(
        ByteBuffer.wrap(
            new byte[] {
              'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A', 1, 'A',
              1, 'A', 1
            }));

    // Assert
    verify(listener).orderAdded(isA(OrderAdded.class));
  }

  /**
   * Test {@link PMRParser#PMRParser(PMRListener)}.
   *
   * <ul>
   *   <li>When {@link PMRListener} {@link PMRListener#version(Version)} does nothing.
   *   <li>Then calls {@link PMRListener#version(Version)}.
   * </ul>
   *
   * <p>Method under test: {@link PMRParser#PMRParser(PMRListener)}
   */
  @Test
  @DisplayName(
      "Test new PMRParser(PMRListener); when PMRListener version(Version) does nothing; then calls version(Version)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PMRParser.<init>(PMRListener)"})
  void testNewPMRParser_whenPMRListenerVersionDoesNothing_thenCallsVersion() throws IOException {
    // Arrange
    PMRListener listener = mock(PMRListener.class);
    doNothing().when(listener).version(Mockito.<Version>any());

    // Act
    PMRParser actualPmrParser = new PMRParser(listener);
    actualPmrParser.message(ByteBuffer.wrap("VXAXAXAX".getBytes("UTF-8")));

    // Assert
    verify(listener).version(isA(Version.class));
  }
}
