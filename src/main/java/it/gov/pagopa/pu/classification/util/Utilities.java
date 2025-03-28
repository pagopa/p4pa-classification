package it.gov.pagopa.pu.classification.util;

import java.math.BigDecimal;

public class Utilities {

  private Utilities(){}

  public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  public static Long bigDecimalEuroToLongCentsAmount(BigDecimal euroAmount) {
    return euroAmount != null ? euroAmount.multiply(HUNDRED).longValue() : null;
  }

}
