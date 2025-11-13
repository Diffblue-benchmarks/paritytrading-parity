package com.paritytrading.parity.book.perf;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.book.Market;
import com.paritytrading.parity.book.Side;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketBenchmarkDiffblueTest {
  @Mock private Market market;

  @InjectMocks private MarketBenchmark marketBenchmark;

  /**
   * Test {@link MarketBenchmark#add()}.
   *
   * <p>Method under test: {@link MarketBenchmark#add()}
   */
  @Test
  @DisplayName("Test add()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.add()"})
  void testAdd() {
    // Arrange
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    marketBenchmark.add();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
  }

  /**
   * Test {@link MarketBenchmark#addAndModify()}.
   *
   * <p>Method under test: {@link MarketBenchmark#addAndModify()}
   */
  @Test
  @DisplayName("Test addAndModify()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.addAndModify()"})
  void testAddAndModify() {
    // Arrange
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());
    doNothing().when(market).modify(anyLong(), anyLong());

    // Act
    marketBenchmark.addAndModify();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    verify(market).modify(0L, 0L);
  }

  /**
   * Test {@link MarketBenchmark#addAndExecute()}.
   *
   * <p>Method under test: {@link MarketBenchmark#addAndExecute()}
   */
  @Test
  @DisplayName("Test addAndExecute()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.addAndExecute()"})
  void testAddAndExecute() {
    // Arrange
    when(market.execute(anyLong(), anyLong())).thenReturn(1L);
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    marketBenchmark.addAndExecute();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    verify(market).execute(0L, 100L);
  }

  /**
   * Test {@link MarketBenchmark#addAndCancel()}.
   *
   * <p>Method under test: {@link MarketBenchmark#addAndCancel()}
   */
  @Test
  @DisplayName("Test addAndCancel()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.addAndCancel()"})
  void testAddAndCancel() {
    // Arrange
    when(market.cancel(anyLong(), anyLong())).thenReturn(1L);
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());

    // Act
    marketBenchmark.addAndCancel();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    verify(market).cancel(0L, 100L);
  }

  /**
   * Test {@link MarketBenchmark#addAndDelete()}.
   *
   * <p>Method under test: {@link MarketBenchmark#addAndDelete()}
   */
  @Test
  @DisplayName("Test addAndDelete()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MarketBenchmark.addAndDelete()"})
  void testAddAndDelete() {
    // Arrange
    doNothing().when(market).add(anyLong(), anyLong(), Mockito.<Side>any(), anyLong(), anyLong());
    doNothing().when(market).delete(anyLong());

    // Act
    marketBenchmark.addAndDelete();

    // Assert
    verify(market).add(1L, 0L, Side.BUY, 100000L, 100L);
    verify(market).delete(0L);
  }
}
