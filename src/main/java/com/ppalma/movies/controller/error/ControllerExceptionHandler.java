package com.ppalma.movies.controller.error;

import com.ppalma.movies.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

  public static final String UNEXPECTED_ERROR = "Unexpected Error";

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
    ErrorResponse errorMessage = new ErrorResponse(ex.getMessage(),
        HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {

    log.error(ex.getMessage(), ex);

    ErrorResponse errorMessage = new ErrorResponse(UNEXPECTED_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
  }

  @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class,
      MissingServletRequestParameterException.class})
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      Exception ex) {

    ErrorResponse errorMessage = new ErrorResponse(ex.getMessage(),
        HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
  }

}