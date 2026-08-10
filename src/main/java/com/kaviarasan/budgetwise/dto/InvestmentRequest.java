package com.kaviarasan.budgetwise.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestmentRequest {

    private String investmentType;

    private String investmentName;

    private BigDecimal amount;

    private LocalDate investmentDate;

    private String remarks;
}