package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseRequest {

    private String category;

    private BigDecimal amount;

    private LocalDate expenseDate;

    private String remarks;
}