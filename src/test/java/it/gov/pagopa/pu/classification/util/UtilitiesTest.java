package it.gov.pagopa.pu.classification.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UtilitiesTest {

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
}
