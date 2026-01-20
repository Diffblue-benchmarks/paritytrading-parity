package com.paritytrading.parity.reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TSVFormatDiffblueTest {
  /**
   * Test {@link TSVFormat#TSVFormat(Instruments)}.
   *
   * <p>Method under test: {@link TSVFormat#TSVFormat(Instruments)}
   */
  @Test
  @DisplayName("Test new TSVFormat(Instruments)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TSVFormat.<init>(Instruments)"})
  void testNewTSVFormat() {
    // Arrange
    Instruments instruments = mock(Instruments.class);

    ArrayList<Instrument> instrumentList = new ArrayList<>();
    when(instruments.iterator()).thenReturn(instrumentList.iterator());

    // Act
    new TSVFormat(instruments);

    // Assert
    verify(instruments).iterator();
  }
}
