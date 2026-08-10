package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialOverviewResponse {

    private String month;

    private BigDecimal income;

    private BigDecimal expenses;

    private BigDecimal investments;
}