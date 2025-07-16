package it.gov.pagopa.pu.classification.enums;

import lombok.Getter;

@Getter
public enum BalanceDefaultAmountType {

  BALANCE_DEFAULT_TOTAL("TOTALE"),
  BILANCE_DEFAULT_EXTRACT_AMOUNT("estrai_importo"),
  BILANCE_DEFAULT_CALCULATE_AMOUNT("calcola_importo");

  private final String type;

  BalanceDefaultAmountType(String type) {
    this.type = type;
  }

}

