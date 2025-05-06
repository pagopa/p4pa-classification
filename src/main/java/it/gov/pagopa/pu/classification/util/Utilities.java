package it.gov.pagopa.pu.classification.util;

import org.slf4j.MDC;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class Utilities {

  private Utilities(){}

  public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  public static Long bigDecimalEuroToLongCentsAmount(BigDecimal euroAmount) {
    return euroAmount != null ? euroAmount.multiply(HUNDRED).longValue() : null;
  }

  public static boolean isValidIntervalBetweenOffsetDateTime(OffsetDateTime dateFrom, OffsetDateTime dateTo, ChronoUnit chronoUnit, long maxInterval) {
    long result = chronoUnit.between(dateFrom, dateTo);
    return result >= 0 && result <= maxInterval;
  }

  public static boolean isValidIntervalBetweenLocalDateTime(LocalDateTime dateFrom, LocalDateTime dateTo, ChronoUnit chronoUnit, long maxInterval) {
    long result = chronoUnit.between(dateFrom, dateTo);
    return result >= 0 && result <= maxInterval;
  }

  public static boolean isValidIntervalBetweenLocalDate(LocalDate dateFrom, LocalDate dateTo, ChronoUnit chronoUnit, long maxInterval) {
    long result = chronoUnit.between(dateFrom, dateTo);
    return result >= 0 && result <= maxInterval;
  }

  public static String getTraceId(){
    return MDC.get("traceId");
  }
}
