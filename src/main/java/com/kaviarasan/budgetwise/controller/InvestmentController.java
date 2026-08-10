package com.kaviarasan.budgetwise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaviarasan.budgetwise.dto.InvestmentRequest;
import com.kaviarasan.budgetwise.dto.InvestmentResponse;
import com.kaviarasan.budgetwise.service.InvestmentService;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @PostMapping
    public ResponseEntity<Void> saveInvestment(
            @RequestBody InvestmentRequest request) {

        investmentService.saveInvestment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<InvestmentResponse>>
            getAllInvestments() {

        return ResponseEntity.ok(
                investmentService.getAllInvestments());
    }

    @PutMapping("/{investmentId}")
    public ResponseEntity<Void> updateInvestment(
            @PathVariable Long investmentId,
            @RequestBody InvestmentRequest request) {

        investmentService.updateInvestment(
                request,
                investmentId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{investmentId}")
    public ResponseEntity<Void> deleteInvestment(
            @PathVariable Long investmentId) {

        investmentService.deleteInvestment(
                investmentId);

        return ResponseEntity.noContent().build();
    }
}