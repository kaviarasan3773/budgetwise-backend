package com.kaviarasan.budgetwise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.DashboardPeriod;
import com.kaviarasan.budgetwise.dto.DashboardResponse;
import com.kaviarasan.budgetwise.dto.ExpenseCategoryResponse;
import com.kaviarasan.budgetwise.dto.FinancialOverviewResponse;
import com.kaviarasan.budgetwise.dto.RecentTransactionResponse;
import com.kaviarasan.budgetwise.entity.Expense;
import com.kaviarasan.budgetwise.entity.Income;
import com.kaviarasan.budgetwise.entity.Investment;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.repository.ExpenseRepository;
import com.kaviarasan.budgetwise.repository.IncomeRepository;
import com.kaviarasan.budgetwise.repository.InvestmentRepository;
import com.kaviarasan.budgetwise.security.SecurityUtil;
import com.kaviarasan.budgetwise.util.DashboardDateRangeUtil;
import com.kaviarasan.budgetwise.util.DateRange;
import com.kaviarasan.budgetwise.util.DateUtil;

@Service
public class DashboardService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private SecurityUtil securityUtil;

    public DashboardResponse getDashboardSummary(
            DashboardPeriod period, LocalDate fromDate,LocalDate toDate) {

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

        /*
         * Get user's data
         */
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

        /*
         * Filter Income
         *
         * IMPORTANT:
         * Use incomeDate for general dashboard reporting.
         */
        List<Income> filteredIncomeData =
                incomeData.stream()
                        .filter(income ->
                                income.getIncomeDate() != null
                                && !income.getIncomeDate()
                                        .isBefore(startDate)
                                && !income.getIncomeDate()
                                        .isAfter(endDate))
                        .collect(Collectors.toList());

        /*
         * Filter Expenses
         */
        List<Expense> filteredExpenseData =
                expenseData.stream()
                        .filter(expense ->
                                expense.getExpenseDate() != null
                                && !expense.getExpenseDate()
                                        .isBefore(startDate)
                                && !expense.getExpenseDate()
                                        .isAfter(endDate))
                        .collect(Collectors.toList());

        /*
         * Filter Investments
         */
        List<Investment> filteredInvestmentData =
                investmentData.stream()
                        .filter(investment ->
                                investment.getInvestmentDate() != null
                                && !investment.getInvestmentDate()
                                        .isBefore(startDate)
                                && !investment.getInvestmentDate()
                                        .isAfter(endDate))
                        .collect(Collectors.toList());
        
        /*
         * TOTAL EXPENSE TRANSACTION COUNT
         */
        
        Integer totalExpenseTransactions = filteredExpenseData.size();

        /*
         * TOTAL INCOME
         */
        BigDecimal totalIncome =
                filteredIncomeData.stream()
                        .map(Income::getAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        /*
         * TOTAL EXPENSES
         */
        BigDecimal totalExpenses =
                filteredExpenseData.stream()
                        .map(Expense::getAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        /*
         * TOTAL INVESTMENTS
         */
        BigDecimal totalInvestments =
                filteredInvestmentData.stream()
                        .map(Investment::getAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        /*
         * NET SAVINGS
         *
         * Income - Expenses
         */
        BigDecimal totalSavings =
                totalIncome.subtract(totalExpenses);

        /*
         * AVAILABLE BALANCE
         *
         * Income - Expenses - Investments
         */
        BigDecimal netCashFlowAfterInvestments =
                totalSavings.subtract(
                        totalInvestments);

        /*
         * SAVINGS %
         */
        BigDecimal savingsPercentage =
                totalIncome.compareTo(
                        BigDecimal.ZERO) > 0
                        ? totalSavings
                                .multiply(
                                        BigDecimal.valueOf(100))
                                .divide(
                                        totalIncome,
                                        2,
                                        RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

        /*
         * INVESTMENT %
         */
        BigDecimal investmentPercentage =
                totalIncome.compareTo(
                        BigDecimal.ZERO) > 0
                        ? totalInvestments
                                .multiply(
                                        BigDecimal.valueOf(100))
                                .divide(
                                        totalIncome,
                                        2,
                                        RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

        /*
         * TRACKED MONTHS
         *
         * This is mainly useful for THIS_YEAR.
         */
        Integer totalMonthsTracked =
                (int) filteredIncomeData.stream()
                        .filter(income ->
                                income.getIncomeDate() != null)
                        .map(income ->
                                income.getIncomeDate()
                                        .getYear()
                                        + "-"
                                        + income.getIncomeDate()
                                                .getMonthValue())
                        .distinct()
                        .count();

        /*
         * AVERAGE MONTHLY EXPENSE
         */
        BigDecimal avgMonthlyExpense =
                totalMonthsTracked > 0
                        ? totalExpenses.divide(
                                BigDecimal.valueOf(
                                        totalMonthsTracked),
                                2,
                                RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

        /*
         * AVERAGE MONTHLY SAVINGS
         */
        BigDecimal avgMonthlySavings =
                totalMonthsTracked > 0
                        ? totalSavings.divide(
                                BigDecimal.valueOf(
                                        totalMonthsTracked),
                                2,
                                RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

        /*
         * HIGHEST EXPENSE CATEGORY
         */
        Map<String, BigDecimal>
                aggregatedCategoryExpense =
                        filteredExpenseData.stream()
                                .collect(
                                        Collectors.groupingBy(
                                                Expense::getCategory,
                                                Collectors.reducing(
                                                        BigDecimal.ZERO,
                                                        Expense::getAmount,
                                                        BigDecimal::add)));

        Optional<Entry<String, BigDecimal>>
                highestCategoryExpenseEntry =
                        aggregatedCategoryExpense
                                .entrySet()
                                .stream()
                                .max(
                                        Map.Entry
                                                .comparingByValue());

        String highestExpenseCategory = "";

        BigDecimal highestExpenseCategoryAmount =
                BigDecimal.ZERO;

        if (highestCategoryExpenseEntry.isPresent()) {

            highestExpenseCategory =
                    highestCategoryExpenseEntry
                            .get()
                            .getKey();

            highestExpenseCategoryAmount =
                    highestCategoryExpenseEntry
                            .get()
                            .getValue();
        }

        /*
         * HIGHEST EXPENSE MONTH
         */
        Map<String, BigDecimal>
                aggregatedMonthlyExpense =
                        filteredExpenseData.stream()
                                .collect(
                                        Collectors.groupingBy(
                                                expense ->
                                                        expense
                                                                .getExpenseDate()
                                                                .getYear()
                                                                + "-"
                                                                + expense
                                                                        .getExpenseDate()
                                                                        .getMonthValue(),
                                                Collectors.reducing(
                                                        BigDecimal.ZERO,
                                                        Expense::getAmount,
                                                        BigDecimal::add)));

        Optional<Entry<String, BigDecimal>>
                highestMonthlyExpenseEntry =
                        aggregatedMonthlyExpense
                                .entrySet()
                                .stream()
                                .max(
                                        Map.Entry
                                                .comparingByValue());

        String highestExpenseMonth = "";

        BigDecimal highestExpenseMonthlyAmount =
                BigDecimal.ZERO;

        if (highestMonthlyExpenseEntry.isPresent()) {

            highestExpenseMonth =
                    highestMonthlyExpenseEntry
                            .get()
                            .getKey();

            highestExpenseMonthlyAmount =
                    highestMonthlyExpenseEntry
                            .get()
                            .getValue();
        }

        /*
         * MONTHLY INCOME
         */
        Map<String, BigDecimal>
                aggregatedMonthlyIncome =
                        filteredIncomeData.stream()
                                .collect(
                                        Collectors.groupingBy(
                                                income ->
                                                        income
                                                                .getIncomeDate()
                                                                .getYear()
                                                                + "-"
                                                                + income
                                                                        .getIncomeDate()
                                                                        .getMonthValue(),
                                                Collectors.reducing(
                                                        BigDecimal.ZERO,
                                                        Income::getAmount,
                                                        BigDecimal::add)));

        Set<Entry<String, BigDecimal>>
                incomeEntrySet =
                        aggregatedMonthlyIncome.entrySet();

        /*
         * TOP SAVING MONTH
         */
        String topSavingMonth = "";

        BigDecimal topSavingAmount =
                BigDecimal.ZERO;

        for (Entry<String, BigDecimal> entry :
                incomeEntrySet) {

            String key =
                    entry.getKey();

            BigDecimal incomeAmount =
                    entry.getValue();

            BigDecimal expenseValue =
                    aggregatedMonthlyExpense
                            .getOrDefault(
                                    key,
                                    BigDecimal.ZERO);

            BigDecimal currentSavings =
                    incomeAmount.subtract(
                            expenseValue);

            if (currentSavings.compareTo(
                    topSavingAmount) > 0) {

                topSavingMonth = key;

                topSavingAmount =
                        currentSavings;
            }
        }

        /*
         * RESPONSE
         */
        DashboardResponse response =
                new DashboardResponse();

        response.setTotalIncome(
                totalIncome);

        response.setTotalExpenses(
                totalExpenses);

        response.setTotalSavings(
                totalSavings);

        response.setTotalInvestments(
                totalInvestments);

        response.setNetCashFlowAfterInvestments(
                netCashFlowAfterInvestments);

        response.setSavingsPercentage(
                savingsPercentage);

        response.setInvestmentPercentage(
                investmentPercentage);

        response.setTotalMonthsTracked(
                totalMonthsTracked);

        response.setAvgMonthlyExpense(
                avgMonthlyExpense);

        response.setAvgMonthlySavings(
                avgMonthlySavings);

        response.setHighestExpenseCategory(
                highestExpenseCategory);

        response.setHighestCategoryExpenseAmount(
                highestExpenseCategoryAmount);

        response.setHighestExpenseMonth(
        		DateUtil.formatMonth(highestExpenseMonth));

        response.setHighestExpenseMonthlyAmount(
                highestExpenseMonthlyAmount);

        response.setTopSavingMonth(
        		DateUtil.formatMonth(topSavingMonth));

        response.setTopSavingAmount(
                topSavingAmount);
        
        response.setTotalExpenseTransactions(
                totalExpenseTransactions);

        return response;
    }
    
    public List<ExpenseCategoryResponse> getExpenseCategorySummary(
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


        // =========================
        // GET USER EXPENSES
        // =========================

        List<Expense> expenseData =
                expenseRepository.findByUser(
                        currentLoggedInUser);


        // =========================
        // FILTER EXPENSES
        // =========================

        List<Expense> filteredExpenseData =
                expenseData.stream()
                        .filter(expense ->
                                expense.getExpenseDate() != null
                                && !expense.getExpenseDate()
                                        .isBefore(startDate)
                                && !expense.getExpenseDate()
                                        .isAfter(endDate))
                        .collect(Collectors.toList());


        // =========================
        // GROUP BY CATEGORY
        // =========================

        Map<String, BigDecimal> categoryTotals =
                filteredExpenseData.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Expense::getCategory,
                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                Expense::getAmount,
                                                BigDecimal::add
                                        )
                                )
                        );


        // =========================
        // CONVERT MAP TO RESPONSE
        // =========================

        return categoryTotals.entrySet()
                .stream()
                .map(entry ->
                        new ExpenseCategoryResponse(
                                entry.getKey(),
                                entry.getValue()
                        )
                )
                .sorted(
                        (first, second) ->
                                second.getAmount()
                                        .compareTo(
                                                first.getAmount())
                )
                .collect(Collectors.toList());
    }
    
    public List<FinancialOverviewResponse> getFinancialOverview(
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


        // =========================
        // FETCH DATA
        // =========================

        List<Income> incomeData =
                incomeRepository.findByUser(
                        currentLoggedInUser);

        List<Expense> expenseData =
                expenseRepository.findByUser(
                        currentLoggedInUser);

        List<Investment> investmentData =
                investmentRepository.findByUserOrderByInvestmentDateDesc(
                        currentLoggedInUser);


        // =========================
        // FILTER INCOME
        // =========================

        List<Income> filteredIncomeData =
                incomeData.stream()
                        .filter(income ->
                                income.getIncomeDate() != null
                                && !income.getIncomeDate()
                                        .isBefore(startDate)
                                && !income.getIncomeDate()
                                        .isAfter(endDate))
                        .collect(Collectors.toList());


        // =========================
        // FILTER EXPENSE
        // =========================

        List<Expense> filteredExpenseData =
                expenseData.stream()
                        .filter(expense ->
                                expense.getExpenseDate() != null
                                && !expense.getExpenseDate()
                                        .isBefore(startDate)
                                && !expense.getExpenseDate()
                                        .isAfter(endDate))
                        .collect(Collectors.toList());


        // =========================
        // FILTER INVESTMENT
        // =========================

        List<Investment> filteredInvestmentData =
                investmentData.stream()
                        .filter(investment ->
                                investment.getInvestmentDate() != null
                                && !investment.getInvestmentDate()
                                        .isBefore(startDate)
                                && !investment.getInvestmentDate()
                                        .isAfter(endDate))
                        .collect(Collectors.toList());


        // =========================
        // MONTHLY INCOME
        // =========================

        Map<String, BigDecimal> monthlyIncome =
                filteredIncomeData.stream()
                        .collect(
                                Collectors.groupingBy(
                                        income ->
                                                income.getIncomeDate()
                                                        .getYear()
                                                        + "-"
                                                        + String.format(
                                                                "%02d",
                                                                income.getIncomeDate()
                                                                        .getMonthValue()),

                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                Income::getAmount,
                                                BigDecimal::add
                                        )
                                )
                        );


        // =========================
        // MONTHLY EXPENSE
        // =========================

        Map<String, BigDecimal> monthlyExpense =
                filteredExpenseData.stream()
                        .collect(
                                Collectors.groupingBy(
                                        expense ->
                                                expense.getExpenseDate()
                                                        .getYear()
                                                        + "-"
                                                        + String.format(
                                                                "%02d",
                                                                expense.getExpenseDate()
                                                                        .getMonthValue()),

                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                Expense::getAmount,
                                                BigDecimal::add
                                        )
                                )
                        );


        // =========================
        // MONTHLY INVESTMENT
        // =========================

        Map<String, BigDecimal> monthlyInvestment =
                filteredInvestmentData.stream()
                        .collect(
                                Collectors.groupingBy(
                                        investment ->
                                                investment.getInvestmentDate()
                                                        .getYear()
                                                        + "-"
                                                        + String.format(
                                                                "%02d",
                                                                investment.getInvestmentDate()
                                                                        .getMonthValue()),

                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                Investment::getAmount,
                                                BigDecimal::add
                                        )
                                )
                        );


        // =========================
        // COMBINE MONTHS
        // =========================

        Set<String> months = new HashSet<>();

        months.addAll(monthlyIncome.keySet());
        months.addAll(monthlyExpense.keySet());
        months.addAll(monthlyInvestment.keySet());


        // =========================
        // CREATE RESPONSE
        // =========================

        return months.stream()
                .sorted()
                .map(month -> {

                    BigDecimal income =
                            monthlyIncome.getOrDefault(
                                    month,
                                    BigDecimal.ZERO);

                    BigDecimal expense =
                            monthlyExpense.getOrDefault(
                                    month,
                                    BigDecimal.ZERO);

                    BigDecimal investment =
                            monthlyInvestment.getOrDefault(
                                    month,
                                    BigDecimal.ZERO);

                    return new FinancialOverviewResponse(
                            month,
                            income,
                            expense,
                            investment
                    );

                })
                .collect(Collectors.toList());
    }
    
    public List<RecentTransactionResponse> getRecentTransactions(
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


        // =========================
        // FETCH DATA
        // =========================

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


        List<RecentTransactionResponse> transactions =
                new ArrayList<>();


        // =========================
        // INCOME
        // =========================

        incomeData.stream()

                .filter(income ->
                        income.getIncomeDate() != null
                        && !income.getIncomeDate()
                                .isBefore(startDate)
                        && !income.getIncomeDate()
                                .isAfter(endDate))

                .forEach(income -> {

                    transactions.add(
                            new RecentTransactionResponse(
                                    income.getSourceName(),
                                    "Income",
                                    income.getAmount(),
                                    income.getIncomeDate()
                            )
                    );
                });


        // =========================
        // EXPENSE
        // =========================

        expenseData.stream()

                .filter(expense ->
                        expense.getExpenseDate() != null
                        && !expense.getExpenseDate()
                                .isBefore(startDate)
                        && !expense.getExpenseDate()
                                .isAfter(endDate))

                .forEach(expense -> {

                    transactions.add(
                            new RecentTransactionResponse(
                                    expense.getCategory(),
                                    "Expense",
                                    expense.getAmount(),
                                    expense.getExpenseDate()
                            )
                    );
                });


        // =========================
        // INVESTMENT
        // =========================

        investmentData.stream()

                .filter(investment ->
                        investment.getInvestmentDate() != null
                        && !investment.getInvestmentDate()
                                .isBefore(startDate)
                        && !investment.getInvestmentDate()
                                .isAfter(endDate))

                .forEach(investment -> {

                    transactions.add(
                            new RecentTransactionResponse(
                                    investment.getInvestmentName(),
                                    "Investment",
                                    investment.getAmount(),
                                    investment.getInvestmentDate()
                            )
                    );
                });


        // =========================
        // SORT NEWEST FIRST
        // =========================

        transactions.sort(
                Comparator.comparing(
                        RecentTransactionResponse::getDate
                ).reversed()
        );


        // =========================
        // ONLY 5 RECENT TRANSACTIONS
        // =========================

        return transactions.stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}