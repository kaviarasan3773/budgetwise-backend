package com.kaviarasan.budgetwise.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kaviarasan.budgetwise.dto.ExpenseCategoryReportResponse;
import com.kaviarasan.budgetwise.dto.IncomeSourceReportResponse;
import com.kaviarasan.budgetwise.dto.MonthlyReportResponse;
import com.kaviarasan.budgetwise.dto.ReportSummaryResponse;
import com.kaviarasan.budgetwise.dto.ReportTransactionResponse;
import com.kaviarasan.budgetwise.service.ReportService;
import com.kaviarasan.budgetwise.dto.DashboardPeriod;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;


    @GetMapping("/summary")
    public ReportSummaryResponse getReportSummary(

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


        return reportService.getReportSummary(
                period,
                fromDate,
                toDate);
    }


    @GetMapping("/expense-categories")
    public List<ExpenseCategoryReportResponse>
    getExpenseCategoryReport(

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


        return reportService.getExpenseCategoryReport(
                period,
                fromDate,
                toDate);
    }


    @GetMapping("/income-sources")
    public List<IncomeSourceReportResponse>
    getIncomeSourceReport(

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


        return reportService.getIncomeSourceReport(
                period,
                fromDate,
                toDate);
    }
    
    @GetMapping("/monthly")
    public List<MonthlyReportResponse> getMonthlyReport(

            @RequestParam(
                    defaultValue = "THIS_YEAR")
            DashboardPeriod period,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate) {

        return reportService.getMonthlyReport(
                period,
                fromDate,
                toDate);
    }
    
    @GetMapping("/transactions")
    public List<ReportTransactionResponse> getTransactionReport(

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

        return reportService.getTransactionReport(
                period,
                fromDate,
                toDate);
    }

}