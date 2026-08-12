package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExpenseCategoryReportResponse {

    private String category;

    private BigDecimal amount;

}