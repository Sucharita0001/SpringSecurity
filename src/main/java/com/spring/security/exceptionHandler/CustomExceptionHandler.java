package com.spring.security.exceptionHandler;

import com.spring.security.exception.CustomerAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<String> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        return new ResponseEntity<>("Customer already exists.", NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("Email cannot be used.", FORBIDDEN);
    }
}
