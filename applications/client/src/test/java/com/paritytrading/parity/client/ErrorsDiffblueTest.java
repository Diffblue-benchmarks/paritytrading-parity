package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ErrorsDiffblueTest {
  /**
   * Test {@link Errors#collect(Events)}.
   * <ul>
   *   <li>When {@link Events} (default constructor).</li>
   *   <li>Then return Empty.</li>
   * </ul>
   * <p>
   * Method under test: {@link Errors#collect(Events)}
   */
  @Test
  @DisplayName("Test collect(Events); when Events (default constructor); then return Empty")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"List Errors.collect(Events)"})
  void testCollect_whenEvents_thenReturnEmpty() {
    // Arrange and Act
    List<Error> actualCollectResult = Errors.collect(new Events());

    // Assert
    assertTrue(actualCollectResult.isEmpty());
  }
}
