package com.kaviarasan.budgetwise.exception;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}