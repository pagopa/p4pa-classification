package it.gov.pagopa.pu.classification.exception;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationErrorDTO;
import it.gov.pagopa.pu.classification.exception.common.CommonExceptionHandler;
import it.gov.pagopa.pu.classification.exception.custom.AssessmentConflictException;
import it.gov.pagopa.pu.classification.exception.custom.ExportTooManyRecordsException;
import it.gov.pagopa.pu.classification.exception.custom.InvalidDateTimeIntervalException;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClassificationExceptionHandler extends CommonExceptionHandler {

  @ExceptionHandler({InvalidRequestBodyException.class})
  public ResponseEntity<ClassificationErrorDTO> handleInvalidRequestBodyException(InvalidRequestBodyException ex, HttpServletRequest request){
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ClassificationErrorDTO.CategoryEnum.CLASSIFICATION_BAD_REQUEST);
  }

  @ExceptionHandler({ExportTooManyRecordsException.class})
  public ResponseEntity<ClassificationErrorDTO> handleExportTooManyRecordsException(RuntimeException ex, HttpServletRequest request){
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ClassificationErrorDTO.CategoryEnum.CLASSIFICATION_BAD_REQUEST);
  }

  @ExceptionHandler({InvalidDateTimeIntervalException.class})
  public ResponseEntity<ClassificationErrorDTO> handleInvalidDateTimeIntervalException(RuntimeException ex, HttpServletRequest request){
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ClassificationErrorDTO.CategoryEnum.CLASSIFICATION_BAD_REQUEST);
  }

  @ExceptionHandler({AssessmentConflictException.class})
  public ResponseEntity<ClassificationErrorDTO> handleAssessmentConflictException(RuntimeException ex, HttpServletRequest request){
    return handleException(ex, request, HttpStatus.CONFLICT, ClassificationErrorDTO.CategoryEnum.CLASSIFICATION_CONFLICT);
  }

}
