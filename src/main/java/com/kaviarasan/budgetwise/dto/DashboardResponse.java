package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardResponse {
	
	
	private BigDecimal totalGrossSalary;
	
	private BigDecimal totalInHandSalary;
	
	private BigDecimal totalExpenses;          
	
	private BigDecimal totalSavings;     
	
	private BigDecimal avgMonthlyExpense;     
	
	private BigDecimal avgMonthlySavings;     
	
	private BigDecimal savingsPercentage;    
	
	private String highestExpenseCategory;
	
	private Integer totalMonthsTracked;

	private BigDecimal highestCategoryExpenseAmount; 
	
	private String highestExpenseMonth;
	
	private BigDecimal highestExpenseMonthlyAmount;
	
	private BigDecimal topSavingAmount;
	
	private String topSavingMonth;
}
