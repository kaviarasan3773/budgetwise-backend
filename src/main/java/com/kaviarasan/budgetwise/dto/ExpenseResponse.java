package com.kaviarasan.budgetwise.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseResponse {

	private Long expenseId;
	
    private String title;

    private double amount;

    private String category;

    private LocalDate expenseDate;

    private String paymentMode;

    private String notes;
    
    private LocalDateTime createdAt;
}