package com.kaviarasan.budgetwise.util;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DateRange {

    private LocalDate startDate;

    private LocalDate endDate;
}