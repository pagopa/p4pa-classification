package it.gov.pagopa.pu.classification.exception.custom;

import it.gov.pagopa.pu.classification.exception.common.BaseBusinessException;

public class InvalidRequestBodyException extends BaseBusinessException {

    public InvalidRequestBodyException(String code, String message) {
            super(code, message);
        }
}
