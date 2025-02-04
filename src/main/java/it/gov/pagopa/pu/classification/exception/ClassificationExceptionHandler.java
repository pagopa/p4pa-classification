package it.gov.pagopa.pu.classification.exception;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClassificationExceptionHandler {

  @ExceptionHandler({ValidationException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<ClassificationErrorDTO> handleViolationException(RuntimeException ex, HttpServletRequest request) {
    return handleException(ex, request, HttpStatus.BAD_REQUEST, ClassificationErrorDTO.CodeEnum.BAD_REQUEST);
  }

  @ExceptionHandler({ServletException.class})
  public ResponseEntity<ClassificationErrorDTO> handleServletException(ServletException ex, HttpServletRequest request){
    HttpStatusCode httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    ClassificationErrorDTO.CodeEnum errorCode = ClassificationErrorDTO.CodeEnum.GENERIC_ERROR;
    if(ex instanceof ErrorResponse errorResponse){
      httpStatus = errorResponse.getStatusCode();
      if(httpStatus.is4xxClientError()){
        errorCode = ClassificationErrorDTO.CodeEnum.BAD_REQUEST;
      }
    }
    return handleException(ex, request, httpStatus, errorCode);
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<ClassificationErrorDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request){
    return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ClassificationErrorDTO.CodeEnum.GENERIC_ERROR);
  }

  static ResponseEntity<ClassificationErrorDTO> handleException(Throwable ex, HttpServletRequest request, HttpStatusCode httpStatus, ClassificationErrorDTO.CodeEnum errorEnum) {
    String message = logException(ex, request, httpStatus);

    return ResponseEntity
      .status(httpStatus)
      .body(ClassificationErrorDTO.builder().code(errorEnum).message(message).build());
  }

  private static String logException(Throwable ex, HttpServletRequest request, HttpStatusCode httpStatus) {
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
