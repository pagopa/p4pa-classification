package it.gov.pagopa.pu.classification.exception.custom;

public class AssessmentConflictException extends BaseBusinessException {
  public AssessmentConflictException(String code, String message) {
    super(code, message);
  }
}
