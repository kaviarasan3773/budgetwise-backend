package com.kaviarasan.budgetwise.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kaviarasan.budgetwise.dto.DashboardPeriod;
import com.kaviarasan.budgetwise.dto.DashboardResponse;
import com.kaviarasan.budgetwise.dto.ExpenseCategoryResponse;
import com.kaviarasan.budgetwise.dto.FinancialOverviewResponse;
import com.kaviarasan.budgetwise.dto.RecentTransactionResponse;
import com.kaviarasan.budgetwise.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;


    @GetMapping
    public DashboardResponse getDashboardSummary(

            @RequestParam(
                    defaultValue = "THIS_MONTH")
            DashboardPeriod period,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate) {

        return dashboardService.getDashboardSummary(
                period,
                fromDate,
                toDate);
    }


    @GetMapping("/expense-categories")
    public List<ExpenseCategoryResponse>
            getExpenseCategorySummary(

            @RequestParam(
                    defaultValue = "THIS_MONTH")
            DashboardPeriod period,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate) {

        return dashboardService
                .getExpenseCategorySummary(
                        period,
                        fromDate,
                        toDate);
    }
    
    @GetMapping("/financial-overview")
    public List<FinancialOverviewResponse> getFinancialOverview(

            @RequestParam(
                    defaultValue = "THIS_MONTH")
            DashboardPeriod period,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate) {

        return dashboardService.getFinancialOverview(
                period,
                fromDate,
                toDate);
    }
    
    @GetMapping("/recent-transactions")
    public List<RecentTransactionResponse> getRecentTransactions(

            @RequestParam(
                    defaultValue = "THIS_MONTH")
            DashboardPeriod period,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate) {

        return dashboardService.getRecentTransactions(
                period,
                fromDate,
                toDate);
    }
}