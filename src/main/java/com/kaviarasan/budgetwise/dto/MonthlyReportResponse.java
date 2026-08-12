package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyReportResponse {

    private String month;

    private BigDecimal income;

    private BigDecimal expenses;

    private BigDecimal investments;

    private BigDecimal savings;

}