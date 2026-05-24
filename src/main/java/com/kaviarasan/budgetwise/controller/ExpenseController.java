package com.kaviarasan.budgetwise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaviarasan.budgetwise.dto.ExpenseRequest;
import com.kaviarasan.budgetwise.dto.ExpenseResponse;
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
    
    @GetMapping
    public List<ExpenseResponse> getUserExpenses(){
    	return expenseService.fetchExpenses();
    }
    
    @DeleteMapping("/{expenseId}")
    public String deleteExpense(@PathVariable Long expenseId) {
    	return expenseService.deleteExpense(expenseId);
    }
    
    @PutMapping("/{expenseId}")
    public String updateExpense(@RequestBody ExpenseRequest expenseRequest,@PathVariable Long expenseId) {
    	return expenseService.updateExpense(expenseRequest,expenseId);
    }
}