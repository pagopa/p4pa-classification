package it.gov.pagopa.pu.classification.enums;

import lombok.Getter;

@Getter
public enum BalanceDefaultAmountType {

  TOTAL("TOTALE"),
  EXTRACT_AMOUNT("estrai_importo"),
  CALCULATE_AMOUNT("calcola_importo");

  private final String type;

  BalanceDefaultAmountType(String type) {
    this.type = type;
  }

}

