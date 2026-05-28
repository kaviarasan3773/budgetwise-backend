package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseRequest {

    private String title;

    private BigDecimal amount;

    private String category;

    private LocalDate expenseDate;

    private String paymentMode;

    private String notes;
}