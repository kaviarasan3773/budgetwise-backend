package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryResponse {

    private Long salaryId;

    private String salaryMonth;

    private BigDecimal grossSalary;

    private BigDecimal totalDeductions;

    private BigDecimal inHandSalary;
}