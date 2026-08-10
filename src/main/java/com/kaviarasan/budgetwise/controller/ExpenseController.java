package com.kaviarasan.budgetwise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaviarasan.budgetwise.dto.ExpenseRequest;
import com.kaviarasan.budgetwise.dto.ExpenseResponse;
import com.kaviarasan.budgetwise.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Void> saveExpense(
            @RequestBody ExpenseRequest request) {

        expenseService.saveExpense(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {

        return ResponseEntity.ok(
                expenseService.getAllExpenses());
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Void> updateExpense(
            @PathVariable Long expenseId,
            @RequestBody ExpenseRequest request) {

        expenseService.updateExpense(
                request,
                expenseId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long expenseId) {

        expenseService.deleteExpense(expenseId);

        return ResponseEntity.noContent().build();
    }
}