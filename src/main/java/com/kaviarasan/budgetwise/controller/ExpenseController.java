package com.kaviarasan.budgetwise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaviarasan.budgetwise.dto.ExpenseRequest;
import com.kaviarasan.budgetwise.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public String createExpense(@RequestBody ExpenseRequest expenseRequest) {
        return expenseService.saveExpense(expenseRequest);
    }
}