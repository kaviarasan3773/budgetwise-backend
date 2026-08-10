package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardResponse {

    private BigDecimal totalIncome;

    private BigDecimal totalExpenses;

    private BigDecimal totalSavings;

    private BigDecimal totalInvestments;

    private BigDecimal netCashFlowAfterInvestments;

    private BigDecimal savingsPercentage;

    private BigDecimal investmentPercentage;

    private BigDecimal avgMonthlyExpense;

    private BigDecimal avgMonthlySavings;

    private String highestExpenseCategory;

    private Integer totalMonthsTracked;

    private BigDecimal highestCategoryExpenseAmount;

    private String highestExpenseMonth;

    private BigDecimal highestExpenseMonthlyAmount;

    private BigDecimal topSavingAmount;

    private String topSavingMonth;
    
    private Integer totalExpenseTransactions;
}