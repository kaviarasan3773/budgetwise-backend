package com.kaviarasan.budgetwise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(
            InvalidCredentialsException exception) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }
    
    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<String> userInactiveException(
            UserInactiveException exception) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(
            EmailAlreadyExistsException exception){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());

    }
    
    @ExceptionHandler(IncomeNotFoundException.class)
    public ResponseEntity<String> handleIncomeNotFound(
            IncomeNotFoundException ex){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccess(
            UnauthorizedAccessException ex){

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }
    
    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<String> handleExpenseNotFound(
            ExpenseNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    
    @ExceptionHandler(InvestmentNotFoundException.class)
    public ResponseEntity<String> handleInvestmentNotFound(
            InvestmentNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}