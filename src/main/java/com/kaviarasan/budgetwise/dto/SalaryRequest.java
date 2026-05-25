package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryRequest {

    // Example: 2026-05
    private String salaryMonth;

    private BigDecimal grossSalary;

    private BigDecimal totalDeductions;

}