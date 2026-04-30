package it.gov.pagopa.pu.classification.exception.custom;

public class InvalidRequestBodyException extends BaseBusinessException {

    public InvalidRequestBodyException(String code, String message) {
            super(code, message);
        }
}
