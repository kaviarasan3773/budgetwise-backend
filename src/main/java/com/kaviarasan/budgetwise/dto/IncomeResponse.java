package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeResponse {
	
	private Long incomeId;

	private String incomeType;

	private String sourceName;

	private BigDecimal amount;

	private String incomeMonth;

	private LocalDate incomeDate;

	private String remarks;
	
}