package it.gov.pagopa.pu.classification.exception.common;

import it.gov.pagopa.pu.classification.dto.generated.ErrorFieldDTO;

import java.util.List;

public class ConflictException extends BaseBusinessException {

  public ConflictException(String code, String message) {
    this(code, message, null);
  }

  public ConflictException(String code, String message, List<ErrorFieldDTO> fieldErrors) {
    super(code, message, fieldErrors, null);
  }

}
