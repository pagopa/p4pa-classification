package it.gov.pagopa.pu.classification.exception.custom;

public class InvalidDateTimeIntervalException extends BaseBusinessException {
  public InvalidDateTimeIntervalException(String code, String message) {
    super(code, message);
  }
}
