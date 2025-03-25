package it.gov.pagopa.pu.classification.exception.custom;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
            super(message);
        }
}
