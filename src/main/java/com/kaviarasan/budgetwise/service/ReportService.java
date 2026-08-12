package com.kaviarasan.budgetwise.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.ExpenseCategoryReportResponse;
import com.kaviarasan.budgetwise.dto.IncomeSourceReportResponse;
import com.kaviarasan.budgetwise.dto.ReportSummaryResponse;
import com.kaviarasan.budgetwise.dto.ReportTransactionResponse;
import com.kaviarasan.budgetwise.entity.Expense;
import com.kaviarasan.budgetwise.entity.Income;
import com.kaviarasan.budgetwise.entity.Investment;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.repository.ExpenseRepository;
import com.kaviarasan.budgetwise.repository.IncomeRepository;
import com.kaviarasan.budgetwise.repository.InvestmentRepository;
import com.kaviarasan.budgetwise.security.SecurityUtil;
import com.kaviarasan.budgetwise.util.DashboardDateRangeUtil;
import com.kaviarasan.budgetwise.dto.DashboardPeriod;
import com.kaviarasan.budgetwise.util.DateRange;
import com.kaviarasan.budgetwise.dto.MonthlyReportResponse;

@Service
public class ReportService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private SecurityUtil securityUtil;


    // =====================================================
    // REPORT SUMMARY
    // =====================================================

    public ReportSummaryResponse getReportSummary(
            DashboardPeriod period,
            LocalDate fromDate,
            LocalDate toDate) {


        User currentLoggedInUser =
                securityUtil.getCurrentLoggedInUser();


        DateRange dateRange =
                DashboardDateRangeUtil.getDateRange(
                        period,
                        fromDate,
                        toDate);

        LocalDate startDate =
                dateRange.getStartDate();

        LocalDate endDate =
                dateRange.getEndDate();


        List<Income> incomeData =
                incomeRepository.findByUser(
                        currentLoggedInUser);


        List<Expense> expenseData =
                expenseRepository.findByUser(
                        currentLoggedInUser);


        List<Investment> investmentData =
                investmentRepository
                        .findByUserOrderByInvestmentDateDesc(
                                currentLoggedInUser);


        BigDecimal totalIncome =
                incomeData.stream()

                        .filter(income ->
                                income.getIncomeDate() != null
                                && !income.getIncomeDate()
                                        .isBefore(startDate)
                                && !income.getIncomeDate()
                                        .isAfter(endDate))

                        .map(Income::getAmount)

                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);


        BigDecimal totalExpenses =
                expenseData.stream()

                        .filter(expense ->
                                expense.getExpenseDate() != null
                                && !expense.getExpenseDate()
                                        .isBefore(startDate)
                                && !expense.getExpenseDate()
                                        .isAfter(endDate))

                        .map(Expense::getAmount)

                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);


        BigDecimal totalInvestments =
                investmentData.stream()

                        .filter(investment ->
                                investment.getInvestmentDate() != null
                                && !investment.getInvestmentDate()
                                        .isBefore(startDate)
                                && !investment.getInvestmentDate()
                                        .isAfter(endDate))

                        .map(Investment::getAmount)

                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);


        BigDecimal netSavings =
                totalIncome.subtract(totalExpenses);


        BigDecimal netCashFlowAfterInvestments =
                netSavings.subtract(totalInvestments);


        BigDecimal savingsPercentage =
                totalIncome.compareTo(BigDecimal.ZERO) > 0

                        ? netSavings
                                .multiply(
                                        BigDecimal.valueOf(100))
                                .divide(
                                        totalIncome,
                                        2,
                                        java.math.RoundingMode.HALF_UP)

                        : BigDecimal.ZERO;


        BigDecimal investmentPercentage =
                totalIncome.compareTo(BigDecimal.ZERO) > 0

                        ? totalInvestments
                                .multiply(
                                        BigDecimal.valueOf(100))
                                .divide(
                                        totalIncome,
                                        2,
                                        java.math.RoundingMode.HALF_UP)

                        : BigDecimal.ZERO;


        ReportSummaryResponse response =
                new ReportSummaryResponse();


        response.setTotalIncome(totalIncome);

        response.setTotalExpenses(totalExpenses);

        response.setTotalInvestments(
                totalInvestments);

        response.setNetSavings(
                netSavings);

        response.setNetCashFlowAfterInvestments(
                netCashFlowAfterInvestments);

        response.setSavingsPercentage(
                savingsPercentage);

        response.setInvestmentPercentage(
                investmentPercentage);


        return response;
    }


    // =====================================================
    // EXPENSE CATEGORY REPORT
    // =====================================================

    public List<ExpenseCategoryReportResponse>
    getExpenseCategoryReport(
            DashboardPeriod period,
            LocalDate fromDate,
            LocalDate toDate) {


        User currentLoggedInUser =
                securityUtil.getCurrentLoggedInUser();


        DateRange dateRange =
                DashboardDateRangeUtil.getDateRange(
                        period,
                        fromDate,
                        toDate);

        LocalDate startDate =
                dateRange.getStartDate();

        LocalDate endDate =
                dateRange.getEndDate();


        List<Expense> expenseData =
                expenseRepository.findByUser(
                        currentLoggedInUser);


        Map<String, BigDecimal> categoryTotals =
                expenseData.stream()

                        .filter(expense ->
                                expense.getExpenseDate() != null
                                && !expense.getExpenseDate()
                                        .isBefore(startDate)
                                && !expense.getExpenseDate()
                                        .isAfter(endDate))

                        .collect(
                                Collectors.groupingBy(

                                        Expense::getCategory,

                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                Expense::getAmount,
                                                BigDecimal::add)
                                )
                        );


        return categoryTotals.entrySet()
                .stream()

                .sorted(
                        Map.Entry
                                .<String, BigDecimal>
                                comparingByValue()
                                .reversed())

                .map(entry ->
                        new ExpenseCategoryReportResponse(
                                entry.getKey(),
                                entry.getValue()))

                .collect(Collectors.toList());
    }


    // =====================================================
    // INCOME SOURCE REPORT
    // =====================================================

    public List<IncomeSourceReportResponse>
    getIncomeSourceReport(
            DashboardPeriod period,
            LocalDate fromDate,
            LocalDate toDate) {


        User currentLoggedInUser =
                securityUtil.getCurrentLoggedInUser();


        DateRange dateRange =
                DashboardDateRangeUtil.getDateRange(
                        period,
                        fromDate,
                        toDate);

        LocalDate startDate =
                dateRange.getStartDate();

        LocalDate endDate =
                dateRange.getEndDate();


        List<Income> incomeData =
                incomeRepository.findByUser(
                        currentLoggedInUser);


        Map<String, BigDecimal> sourceTotals =
                incomeData.stream()

                        .filter(income ->
                                income.getIncomeDate() != null
                                && !income.getIncomeDate()
                                        .isBefore(startDate)
                                && !income.getIncomeDate()
                                        .isAfter(endDate))

                        .collect(
                                Collectors.groupingBy(

                                        Income::getSourceName,

                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                Income::getAmount,
                                                BigDecimal::add)
                                )
                        );


        return sourceTotals.entrySet()
                .stream()

                .sorted(
                        Map.Entry
                                .<String, BigDecimal>
                                comparingByValue()
                                .reversed())

                .map(entry ->
                        new IncomeSourceReportResponse(
                                entry.getKey(),
                                entry.getValue()))

                .collect(Collectors.toList());
    }
    
 // =====================================================
 // MONTHLY REPORT
 // =====================================================

 public List<MonthlyReportResponse> getMonthlyReport(
         DashboardPeriod period,
         LocalDate fromDate,
         LocalDate toDate) {

     User currentLoggedInUser =
             securityUtil.getCurrentLoggedInUser();

     DateRange dateRange =
             DashboardDateRangeUtil.getDateRange(
                     period,
                     fromDate,
                     toDate);

     LocalDate startDate =
             dateRange.getStartDate();

     LocalDate endDate =
             dateRange.getEndDate();

     List<Income> incomeData =
             incomeRepository.findByUser(
                     currentLoggedInUser);

     List<Expense> expenseData =
             expenseRepository.findByUser(
                     currentLoggedInUser);

     List<Investment> investmentData =
             investmentRepository.findByUserOrderByInvestmentDateDesc(
                     currentLoggedInUser);


     Map<YearMonth, BigDecimal> incomeByMonth =
             new LinkedHashMap<>();

     Map<YearMonth, BigDecimal> expenseByMonth =
             new LinkedHashMap<>();

     Map<YearMonth, BigDecimal> investmentByMonth =
             new LinkedHashMap<>();


     // =====================================================
     // INCOME
     // =====================================================

     incomeData.stream()

             .filter(income ->
                     income.getIncomeDate() != null
                     && !income.getIncomeDate().isBefore(startDate)
                     && !income.getIncomeDate().isAfter(endDate))

             .forEach(income -> {

                 YearMonth month =
                         YearMonth.from(
                                 income.getIncomeDate());

                 incomeByMonth.merge(
                         month,
                         income.getAmount(),
                         BigDecimal::add);
             });


     // =====================================================
     // EXPENSE
     // =====================================================

     expenseData.stream()

             .filter(expense ->
                     expense.getExpenseDate() != null
                     && !expense.getExpenseDate().isBefore(startDate)
                     && !expense.getExpenseDate().isAfter(endDate))

             .forEach(expense -> {

                 YearMonth month =
                         YearMonth.from(
                                 expense.getExpenseDate());

                 expenseByMonth.merge(
                         month,
                         expense.getAmount(),
                         BigDecimal::add);
             });


     // =====================================================
     // INVESTMENT
     // =====================================================

     investmentData.stream()

             .filter(investment ->
                     investment.getInvestmentDate() != null
                     && !investment.getInvestmentDate().isBefore(startDate)
                     && !investment.getInvestmentDate().isAfter(endDate))

             .forEach(investment -> {

                 YearMonth month =
                         YearMonth.from(
                                 investment.getInvestmentDate());

                 investmentByMonth.merge(
                         month,
                         investment.getAmount(),
                         BigDecimal::add);
             });


     // =====================================================
     // CREATE MONTHLY RESPONSE
     // =====================================================

     List<MonthlyReportResponse> response =
             new ArrayList<>();


     YearMonth currentMonth =
             YearMonth.from(startDate);

     YearMonth lastMonth =
             YearMonth.from(endDate);


     DateTimeFormatter formatter =
             DateTimeFormatter.ofPattern("MMM-yyyy");


     while (!currentMonth.isAfter(lastMonth)) {

         BigDecimal income =
                 incomeByMonth.getOrDefault(
                         currentMonth,
                         BigDecimal.ZERO);

         BigDecimal expenses =
                 expenseByMonth.getOrDefault(
                         currentMonth,
                         BigDecimal.ZERO);

         BigDecimal investments =
                 investmentByMonth.getOrDefault(
                         currentMonth,
                         BigDecimal.ZERO);

         BigDecimal savings =
                 income.subtract(expenses);


         response.add(
                 new MonthlyReportResponse(
                         currentMonth.format(formatter),
                         income,
                         expenses,
                         investments,
                         savings));


         currentMonth =
                 currentMonth.plusMonths(1);
     }


     return response;
 }
 
//=====================================================
//TRANSACTION REPORT
//=====================================================

public List<ReportTransactionResponse> getTransactionReport(
      DashboardPeriod period,
      LocalDate fromDate,
      LocalDate toDate) {

  User currentLoggedInUser =
          securityUtil.getCurrentLoggedInUser();

  DateRange dateRange =
          DashboardDateRangeUtil.getDateRange(
                  period,
                  fromDate,
                  toDate);

  LocalDate startDate =
          dateRange.getStartDate();

  LocalDate endDate =
          dateRange.getEndDate();


  List<Income> incomeData =
          incomeRepository.findByUser(
                  currentLoggedInUser);

  List<Expense> expenseData =
          expenseRepository.findByUser(
                  currentLoggedInUser);

  List<Investment> investmentData =
          investmentRepository
                  .findByUserOrderByInvestmentDateDesc(
                          currentLoggedInUser);


  List<ReportTransactionResponse> transactions =
          new ArrayList<>();


  // =====================================================
  // INCOME
  // =====================================================

  incomeData.stream()

          .filter(income ->
                  income.getIncomeDate() != null
                  && !income.getIncomeDate()
                          .isBefore(startDate)
                  && !income.getIncomeDate()
                          .isAfter(endDate))

          .forEach(income -> {

              transactions.add(
                      new ReportTransactionResponse(
                              income.getSourceName(),
                              "INCOME",
                              income.getAmount(),
                              income.getIncomeDate()
                      )
              );
          });


  // =====================================================
  // EXPENSE
  // =====================================================

  expenseData.stream()

          .filter(expense ->
                  expense.getExpenseDate() != null
                  && !expense.getExpenseDate()
                          .isBefore(startDate)
                  && !expense.getExpenseDate()
                          .isAfter(endDate))

          .forEach(expense -> {

              transactions.add(
                      new ReportTransactionResponse(
                              expense.getCategory(),
                              "EXPENSE",
                              expense.getAmount(),
                              expense.getExpenseDate()
                      )
              );
          });


  // =====================================================
  // INVESTMENT
  // =====================================================

  investmentData.stream()

          .filter(investment ->
                  investment.getInvestmentDate() != null
                  && !investment.getInvestmentDate()
                          .isBefore(startDate)
                  && !investment.getInvestmentDate()
                          .isAfter(endDate))

          .forEach(investment -> {

              transactions.add(
                      new ReportTransactionResponse(
                              investment.getInvestmentName(),
                              "INVESTMENT",
                              investment.getAmount(),
                              investment.getInvestmentDate()
                      )
              );
          });


  // =====================================================
  // SORT NEWEST FIRST
  // =====================================================

  transactions.sort(
          Comparator.comparing(
                  ReportTransactionResponse::getDate
          ).reversed()
  );


  return transactions;
}

}