package it.gov.pagopa.pu.classification.util;

import org.slf4j.MDC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Utilities {

  private Utilities(){}

  public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
  public static final ThreadLocal<NumberFormat> NUMBER_FORMAT_IT = ThreadLocal.withInitial(() -> NumberFormat.getNumberInstance(Locale.ITALIAN));

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

  public static String amountToString(BigDecimal importo) {
    String importoString = NUMBER_FORMAT_IT.get().format(importo);
    if (!importoString.contains(",")) {
      importoString = importoString + ",00";
    } else {
      if (importoString.split(",")[1].length() == 1) {
        importoString = importoString + "0";
      }
    }
    return importoString;

  }
}
