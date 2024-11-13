package com.andreystrinski.employee.manager.config;

import com.andreystrinski.employee.manager.employee.exception.SameStatusException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(SameStatusException.class)
  public ResponseEntity<String> handleSameStatus(SameStatusException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    if (exception.getFieldError() != null) {
      return new ResponseEntity<>(exception.getFieldError().getDefaultMessage(),
          exception.getStatusCode());
    }
    return new ResponseEntity<>("Bad arguments!", exception.getStatusCode());
  }

}
