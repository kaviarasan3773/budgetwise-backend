package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportSummaryResponse {

    private BigDecimal totalIncome;

    private BigDecimal totalExpenses;

    private BigDecimal totalInvestments;

    private BigDecimal netSavings;

    private BigDecimal netCashFlowAfterInvestments;

    private BigDecimal savingsPercentage;

    private BigDecimal investmentPercentage;

}