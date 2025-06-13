package it.gov.pagopa.pu.classification.exception.custom;

public class InvalidRequestBodyException extends RuntimeException {

    public InvalidRequestBodyException(String message) {
            super(message);
        }
}
