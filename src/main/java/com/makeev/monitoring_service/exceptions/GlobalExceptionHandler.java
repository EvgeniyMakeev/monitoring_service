package com.makeev.monitoring_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CounterAlreadyExistsException.class)
    public ResponseEntity<String> handleCounterAlreadyExistsException(CounterAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(CounterIdException.class)
    public ResponseEntity<String> handleCounterIdException(CounterIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ResponseEntity<String> handleLoginAlreadyExistsException(LoginAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(IncorrectValuesException.class)
    public ResponseEntity<String> handleIncorrectValuesException(IncorrectValuesException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NoCounterNameException.class)
    public ResponseEntity<String> handleNoCounterNameException(NoCounterNameException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleInvalidCounterFormat() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid format for value of counter");
    }

    @ExceptionHandler(YearFormatException.class)
    public ResponseEntity<String> handleYearFormatException(YearFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MonthFormatException.class)
    public ResponseEntity<String> handleMonthFormatException(MonthFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(EmptyException.class)
    public ResponseEntity<String> handleEmptyException(EmptyException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<String> handleInternalServerError(DaoException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred " + e.getMessage());
    }
}