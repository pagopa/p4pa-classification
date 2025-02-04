package it.gov.pagopa.pu.classification.exception;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClassificationExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ClassificationErrorDTO> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ClassificationErrorDTO.CodeEnum.BAD_REQUEST);
  }

  @ExceptionHandler(MissingRequestValueException.class)
  public ResponseEntity<ClassificationErrorDTO> handleMissingRequestValueException(MissingRequestValueException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ClassificationErrorDTO.CodeEnum.BAD_REQUEST);
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<ClassificationErrorDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request){
    return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ClassificationErrorDTO.CodeEnum.GENERIC_ERROR);
  }

  static ResponseEntity<ClassificationErrorDTO> handleException(Exception ex, HttpServletRequest request, HttpStatus httpStatus, ClassificationErrorDTO.CodeEnum errorEnum) {
    String message = logException(ex, request, httpStatus);

    return ResponseEntity
      .status(httpStatus)
      .body(ClassificationErrorDTO.builder().code(errorEnum).message(message).build());
  }

  private static String logException(Exception ex, HttpServletRequest request, HttpStatus httpStatus) {
    String message = ex.getMessage();
    log.info("A {} occurred handling request {}: HttpStatus {} - {}",
      ex.getClass(),
      getRequestDetails(request),
      httpStatus.value(),
      message);
    return message;
  }

  static String getRequestDetails(HttpServletRequest request) {
    return "%s %s".formatted(request.getMethod(), request.getRequestURI());
  }
}
