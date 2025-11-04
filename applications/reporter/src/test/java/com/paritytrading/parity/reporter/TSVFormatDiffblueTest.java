package com.paritytrading.parity.reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.paritytrading.parity.util.Instrument;
import com.paritytrading.parity.util.Instruments;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class TSVFormatDiffblueTest {
  /**
   * Method under test: {@link TSVFormat#TSVFormat(Instruments)}
   */
  @Test
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
