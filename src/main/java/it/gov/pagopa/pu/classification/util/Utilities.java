package it.gov.pagopa.pu.classification.util;

import org.slf4j.MDC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

  public static BigDecimal longCentsToBigDecimalEuro(Long centsAmount) {
    return centsAmount != null ? BigDecimal.valueOf(centsAmount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN) : null;
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

  public static String amountToString(BigDecimal amount) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setDecimalSeparator('.');
    DecimalFormat df = new DecimalFormat("#0.00", symbols);
    return df.format(amount);
  }
}
