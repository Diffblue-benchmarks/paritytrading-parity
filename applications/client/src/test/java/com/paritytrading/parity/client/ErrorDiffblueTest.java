package com.paritytrading.parity.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.MethodsUnderTest;
import com.paritytrading.parity.net.poe.POE;
import com.paritytrading.parity.net.poe.POE.OrderRejected;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ErrorDiffblueTest {
  /**
   * Test {@link Error#Error(OrderRejected)}.
   * <ul>
   *   <li>Then return format is {@code <unknown>}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Error#Error(Event.OrderRejected)}
   */
  @Test
  @DisplayName("Test new Error(OrderRejected); then return format is '<unknown>'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"void Error.<init>(Event.OrderRejected)"})
  void testNewError_thenReturnFormatIsUnknown() {
    // Arrange, Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 <unknown>         ",
        (new Error(new Event.OrderRejected(new OrderRejected()))).format());
  }

  /**
   * Test {@link Error#format()}.
   * <ul>
   *   <li>Given {@link OrderRejected} (default constructor) {@link OrderRejected#reason} is {@code I}.</li>
   *   <li>Then return {@code Unknown instrument}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Error#format()}
   */
  @Test
  @DisplayName("Test format(); given OrderRejected (default constructor) reason is 'I'; then return 'Unknown instrument'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String Error.format()"})
  void testFormat_givenOrderRejectedReasonIsI_thenReturnUnknownInstrument() {
    // Arrange
    OrderRejected message = new OrderRejected();
    message.reason = (byte) 'I';

    // Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 Unknown instrument",
        (new Error(new Event.OrderRejected(message))).format());
  }

  /**
   * Test {@link Error#format()}.
   * <ul>
   *   <li>Given {@link OrderRejected} (default constructor) {@link OrderRejected#reason} is {@code P}.</li>
   *   <li>Then return {@code Invalid price}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Error#format()}
   */
  @Test
  @DisplayName("Test format(); given OrderRejected (default constructor) reason is 'P'; then return 'Invalid price'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String Error.format()"})
  void testFormat_givenOrderRejectedReasonIsP_thenReturnInvalidPrice() {
    // Arrange
    OrderRejected message = new OrderRejected();
    message.reason = (byte) 'P';

    // Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 Invalid price     ",
        (new Error(new Event.OrderRejected(message))).format());
  }

  /**
   * Test {@link Error#format()}.
   * <ul>
   *   <li>Given {@link OrderRejected} (default constructor) {@link OrderRejected#reason} is {@code Q}.</li>
   *   <li>Then return {@code Invalid quantity}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Error#format()}
   */
  @Test
  @DisplayName("Test format(); given OrderRejected (default constructor) reason is 'Q'; then return 'Invalid quantity'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String Error.format()"})
  void testFormat_givenOrderRejectedReasonIsQ_thenReturnInvalidQuantity() {
    // Arrange
    OrderRejected message = new OrderRejected();
    message.reason = (byte) 'Q';

    // Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 Invalid quantity  ",
        (new Error(new Event.OrderRejected(message))).format());
  }

  /**
   * Test {@link Error#format()}.
   * <ul>
   *   <li>Then return {@code <unknown>}.</li>
   * </ul>
   * <p>
   * Method under test: {@link Error#format()}
   */
  @Test
  @DisplayName("Test format(); then return '<unknown>'")
  @Tag("MaintainedByDiffblue")
  @MethodsUnderTest({"java.lang.String Error.format()"})
  void testFormat_thenReturnUnknown() {
    // Arrange, Act and Assert
    assertEquals(
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000 <unknown>         ",
        (new Error(new Event.OrderRejected(new OrderRejected()))).format());
  }
}
