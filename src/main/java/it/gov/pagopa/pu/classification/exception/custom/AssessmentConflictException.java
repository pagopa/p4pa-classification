package it.gov.pagopa.pu.classification.exception.custom;

import it.gov.pagopa.pu.classification.exception.common.BaseBusinessException;

public class AssessmentConflictException extends BaseBusinessException {
  public AssessmentConflictException(String code, String message) {
    super(code, message);
  }
}
