package it.gov.pagopa.pu.classification.exception.custom;

import it.gov.pagopa.pu.classification.exception.common.BaseBusinessException;

public class InvalidDateTimeIntervalException extends BaseBusinessException {
  public InvalidDateTimeIntervalException(String code, String message) {
    super(code, message);
  }
}
