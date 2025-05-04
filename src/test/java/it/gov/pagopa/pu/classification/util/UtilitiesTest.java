package it.gov.pagopa.pu.classification.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.MDC;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UtilitiesTest {

  @Test
  void testBigDecimalEuroToLongCentsAmount(){
    // Given
    BigDecimal amount = BigDecimal.valueOf(123.45);
    // When
    long result = Utilities.bigDecimalEuroToLongCentsAmount(amount);
    // Then
    assertEquals(12345, result);
  }
  @Test
  void testBigDecimalEuroToLongCentsAmountNull(){
    // When
    Long result = Utilities.bigDecimalEuroToLongCentsAmount(null);
    // Then
    assertNull(result);
  }

  @ParameterizedTest
  @MethodSource("valueSource")
  void testIsValidIntervalBetweenOffsetDateTime(OffsetDateTime dateFrom, OffsetDateTime dateTo, ChronoUnit chronoUnit, Long maxInterval, Boolean expectedResult){

    boolean result = Utilities.isValidIntervalBetweenOffsetDateTime(dateFrom, dateTo, chronoUnit, maxInterval);

    assertEquals(expectedResult, result);
  }

  static Stream<Arguments> valueSource() {
    OffsetDateTime now = OffsetDateTime.now();
    return Stream.of(
      Arguments.of(now, now.plusMinutes(24), ChronoUnit.MINUTES, 24L, true),
      Arguments.of(now, now.plusHours(20), ChronoUnit.HOURS, 20L, true),
      Arguments.of(now, now.plusDays(60), ChronoUnit.DAYS, 60L, true),
      Arguments.of(now, now.plusWeeks(4), ChronoUnit.WEEKS, 4L, true),
      Arguments.of(now, now.plusMonths(5), ChronoUnit.MONTHS, 5L, true),
      Arguments.of(now, now.plusYears(3), ChronoUnit.YEARS,3L, true),
      Arguments.of(now, now.plusHours(20), ChronoUnit.HOURS, 10L, false),
      Arguments.of(now, now.plusDays(60), ChronoUnit.DAYS, 30L, false),
      Arguments.of(now, now.plusWeeks(4), ChronoUnit.WEEKS, 3L, false),
      Arguments.of(now, now.plusMonths(5), ChronoUnit.MONTHS, 2L, false),
      Arguments.of(now, now.plusYears(3), ChronoUnit.YEARS,2L, false)
    );
  }

  @Test
  void testIsValidIntervalBetweenLocalDateTime(){
    LocalDateTime dateFrom = LocalDateTime.now();
    LocalDateTime dateTo = dateFrom.plusMinutes(2);

    Assertions.assertFalse(Utilities.isValidIntervalBetweenLocalDateTime(dateFrom, dateTo, ChronoUnit.MINUTES, 1));
    Assertions.assertTrue(Utilities.isValidIntervalBetweenLocalDateTime(dateFrom, dateTo, ChronoUnit.MINUTES, 2));
  }

  @Test
  void testIsValidIntervalBetweenLocalDate(){
    LocalDate dateFrom = LocalDate.now();
    LocalDate dateTo = dateFrom.plusDays(2);

    Assertions.assertFalse(Utilities.isValidIntervalBetweenLocalDate(dateFrom, dateTo, ChronoUnit.DAYS, 1));
    Assertions.assertTrue(Utilities.isValidIntervalBetweenLocalDate(dateFrom, dateTo, ChronoUnit.DAYS, 2));
  }

  @Test
  void testGetTraceId(){
    // Given
    String expectedResult = "TRACEID";
    setTraceId(expectedResult);

    // When
    String result = Utilities.getTraceId();

    // Then
    Assertions.assertSame(expectedResult, result);
    clearTraceIdContext();
  }

  public static void setTraceId(String traceId) {
    MDC.put("traceId", traceId);
  }
  public static void clearTraceIdContext(){
    MDC.clear();
  }
}
