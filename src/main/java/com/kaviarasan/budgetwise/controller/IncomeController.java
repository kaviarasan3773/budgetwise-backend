package com.kaviarasan.budgetwise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaviarasan.budgetwise.dto.IncomeRequest;
import com.kaviarasan.budgetwise.dto.IncomeResponse;
import com.kaviarasan.budgetwise.service.IncomeService;

@RestController
@RequestMapping("/api/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @PostMapping
    public ResponseEntity<Void> saveIncome(@RequestBody IncomeRequest request) {

        incomeService.saveIncome(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getAllIncome() {

        return ResponseEntity.ok(incomeService.getAllIncome());
    }

    @PutMapping("/{incomeId}")
    public ResponseEntity<Void> updateIncome(
            @PathVariable Long incomeId,
            @RequestBody IncomeRequest request) {

        incomeService.updateIncome(request, incomeId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> deleteIncome(
            @PathVariable Long incomeId) {

        incomeService.deleteIncome(incomeId);

        return ResponseEntity.noContent().build();
    }
}