package it.gov.pagopa.pu.classification.exception.custom;

public class InvalidNameException extends RuntimeException {
  public InvalidNameException(String message) {
    super(message);
  }
}
