package com.kaviarasan.budgetwise.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.DashboardResponse;
import com.kaviarasan.budgetwise.entity.Expense;
import com.kaviarasan.budgetwise.entity.SalaryDetails;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.repository.ExpenseRepository;
import com.kaviarasan.budgetwise.repository.SalaryRepository;
import com.kaviarasan.budgetwise.security.SecurityUtil;

@Service
public class DashboardService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private SecurityUtil securityUtil;

    public DashboardResponse getDashboardSummary() {

        User currentLoggedInUser =securityUtil.getCurrentLoggedInUser();

        int currentYear =LocalDate.now().getYear();

        List<SalaryDetails> salaryData =salaryRepository.findByUser(currentLoggedInUser);

        List<Expense> expenseData =expenseRepository.findByUser(currentLoggedInUser);

        List<SalaryDetails> currentYearSalaryData =salaryData.stream()
                        							.filter(salary ->salary.getSalaryMonth()	// filtering the salary month based on  starting of current Year only
                        									.startsWith(String.valueOf(currentYear)))
                        							.collect(Collectors.toList());

        List<Expense> currentYearExpenseData =expenseData.stream()
                        						.filter(expense ->expense.getExpenseDate().getYear() == currentYear)
                        						.collect(Collectors.toList());

        BigDecimal totalInHandSalary =currentYearSalaryData.stream()
                        				.map(SalaryDetails::getInHandSalary)   // from salary details we are getting value of only inhandsalary
                        				.reduce(BigDecimal.ZERO,BigDecimal::add);  // reduce means adding each values of fetched values from 0

        BigDecimal totalExpenses = currentYearExpenseData.stream()
                        				.map(Expense::getAmount)
                        				.reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal totalSavings =totalInHandSalary.subtract(totalExpenses);

        Integer totalMonthsTracked =currentYearSalaryData.size();

        BigDecimal avgMonthlyExpense =totalMonthsTracked > 0 // if there are entries for current year then calculate avgmonthlyexpense for current year else 0
        				? totalExpenses.divide(BigDecimal.valueOf(totalMonthsTracked),2,java.math.RoundingMode.HALF_UP) // here divding totalexpense/no of months expense entered
                        : BigDecimal.ZERO;

        BigDecimal avgMonthlySavings =totalMonthsTracked > 0
                        ? totalSavings.divide(BigDecimal.valueOf(totalMonthsTracked),2,java.math.RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

        BigDecimal savingsPercentage =totalInHandSalary.compareTo(BigDecimal.ZERO) > 0  
                        ? totalSavings.multiply(BigDecimal.valueOf(100)).divide(totalInHandSalary,2,java.math.RoundingMode.HALF_UP)  // here formula is totalsavings*100/totalinhandsalary
                        : BigDecimal.ZERO;
        
        Map<String, BigDecimal> aggregatedCategoryExpense =currentYearExpenseData.stream() 
                        .collect(Collectors.groupingBy( 
                                Expense::getCategory, 
                                Collectors.reducing( 
                                        BigDecimal.ZERO, 
                                        Expense::getAmount, 
                                        BigDecimal::add 	
                                )
                        ));

        Optional<Entry<String, BigDecimal>> highestCategoryExpenseEntry =aggregatedCategoryExpense.entrySet() // converts map entries into stream
                        											.stream()
                        											.max(Map.Entry.comparingByValue());// finds entry with highest amount value

        String highestExpenseCategory = "";
        BigDecimal highestExpenseCategoryAmount = BigDecimal.ZERO;

        if (highestCategoryExpenseEntry.isPresent()) {

            highestExpenseCategory =highestCategoryExpenseEntry.get().getKey();
            highestExpenseCategoryAmount =highestCategoryExpenseEntry.get().getValue();
        }
        
        Map<String, BigDecimal> aggregatedMonthlyExpense = currentYearExpenseData.stream()
        	.collect(Collectors.groupingBy(
        			expense -> expense.getExpenseDate().getYear() + "-" + expense.getExpenseDate().getMonthValue(),
        			Collectors.reducing(
        					BigDecimal.ZERO,
        					Expense::getAmount,
        					BigDecimal::add)));
        Optional<Entry<String, BigDecimal>> highestMonthlyExpenseEntry = aggregatedMonthlyExpense.entrySet()
        																	.stream()
        																	.max(Map.Entry.comparingByValue());
        
        String highestExpenseMonth = "";
        BigDecimal highestExpenseMonthlyAmount = BigDecimal.ZERO;
        
        if(highestMonthlyExpenseEntry.isPresent()) {
        	highestExpenseMonth = highestMonthlyExpenseEntry.get().getKey();
        	highestExpenseMonthlyAmount = highestMonthlyExpenseEntry.get().getValue();
        }
                
        
        Map<String, BigDecimal> aggregatedMonthlySalary = currentYearSalaryData.stream() 
        .collect(Collectors.groupingBy( 
                SalaryDetails::getSalaryMonth, 
                Collectors.reducing( 
                        BigDecimal.ZERO, 
                        SalaryDetails::getInHandSalary, 
                        BigDecimal::add 	
                )
        ));
        
        Set<Entry<String, BigDecimal>> salaryEntrySet = aggregatedMonthlySalary.entrySet();
        
        String topSavingMonth = "";
        BigDecimal topSavingAmount = BigDecimal.ZERO;
        
        for(Entry<String, BigDecimal> entry : salaryEntrySet) {
        	String key = entry.getKey();
        	BigDecimal salaryAmount = entry.getValue();
        	BigDecimal expenseValue = aggregatedMonthlyExpense.getOrDefault(key, BigDecimal.ZERO);
        	BigDecimal currentSavings = salaryAmount.subtract(expenseValue);
        	if(currentSavings.compareTo(topSavingAmount)>0) {
        		topSavingMonth=key;
        		topSavingAmount = currentSavings;
        	}
        }

        
        DashboardResponse dashboardResponse = new DashboardResponse();

        dashboardResponse.setTotalInHandSalary(totalInHandSalary);

        dashboardResponse.setTotalExpenses(totalExpenses);

        dashboardResponse.setTotalSavings(totalSavings);

        dashboardResponse.setTotalMonthsTracked(totalMonthsTracked);

        dashboardResponse.setAvgMonthlyExpense(avgMonthlyExpense);

        dashboardResponse.setAvgMonthlySavings(avgMonthlySavings);

        dashboardResponse.setSavingsPercentage(savingsPercentage);
        
        dashboardResponse.setHighestExpenseCategory(highestExpenseCategory);
        
        dashboardResponse.setHighestCategoryExpenseAmount(highestExpenseCategoryAmount);
        
        dashboardResponse.setHighestExpenseMonth(highestExpenseMonth);
        
        dashboardResponse.setHighestExpenseMonthlyAmount(highestExpenseMonthlyAmount);
        
        dashboardResponse.setTopSavingMonth(topSavingMonth);
        
        dashboardResponse.setTopSavingAmount(topSavingAmount);

        return dashboardResponse;
    }
}