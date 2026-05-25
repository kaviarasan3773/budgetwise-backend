package com.kaviarasan.budgetwise.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.ExpenseRequest;
import com.kaviarasan.budgetwise.dto.ExpenseResponse;
import com.kaviarasan.budgetwise.entity.Expense;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.repository.ExpenseRepository;
import com.kaviarasan.budgetwise.repository.UserRepository;
import com.kaviarasan.budgetwise.security.SecurityUtil;

@Service
public class ExpenseService {


	@Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private SecurityUtil securityUtil;

    public String saveExpense(ExpenseRequest expenseRequest) {

    	User loggedInUser = securityUtil.getCurrentLoggedInUser();

        Expense expense = new Expense();
        expense.setTitle(expenseRequest.getTitle());
        expense.setAmount(expenseRequest.getAmount());
        expense.setCategory(expenseRequest.getCategory());
        expense.setExpenseDate(expenseRequest.getExpenseDate());
        expense.setPaymentMode(expenseRequest.getPaymentMode());
        expense.setNotes(expenseRequest.getNotes());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUser(loggedInUser);
        expenseRepository.save(expense);
        return "Expense Saved Successfully";
    }

    public List<ExpenseResponse> fetchExpenses() {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        List<Expense> userExpenses =
                expenseRepository.findByUser(loggedInUser);

        return userExpenses.stream()
                .map(this::mapToExpenseResponse)
                .collect(Collectors.toList());
    }
	
	public String deleteExpense(Long expenseId) {

		User loggedInUser = securityUtil.getCurrentLoggedInUser();

	    Expense expense =expenseRepository.findById(expenseId)
	                    .orElseThrow(() -> new RuntimeException("Expense not found"));

	    if (!expense.getUser().getUserId()
	            .equals(loggedInUser.getUserId())) {
	        return "You are not authorized to delete this expense";
	    }

	    expenseRepository.delete(expense);
	    return "Expense deleted successfully";
	}

	
	public String updateExpense(ExpenseRequest expenseRequest,Long expenseId) {

	    User loggedInUser = securityUtil.getCurrentLoggedInUser();

	    Expense expense =expenseRepository.findById(expenseId)
	                    .orElseThrow(() ->new RuntimeException("Expense not found"));

	    if (!expense.getUser().getUserId()
	            .equals(loggedInUser.getUserId())) {
	        return "You are not authorized to update this expense";
	    }

	    expense.setTitle(
	            expenseRequest.getTitle());

	    expense.setAmount(
	            expenseRequest.getAmount());

	    expense.setCategory(
	            expenseRequest.getCategory());

	    expense.setExpenseDate(
	            expenseRequest.getExpenseDate());

	    expense.setPaymentMode(
	            expenseRequest.getPaymentMode());

	    expense.setNotes(
	            expenseRequest.getNotes());

	    expenseRepository.save(expense);

	    return "Expense updated successfully";
	}
	
	 private ExpenseResponse mapToExpenseResponse(Expense expense) {

	        ExpenseResponse response =new ExpenseResponse();

	        response.setExpenseId(
	                expense.getExpenseId());

	        response.setTitle(
	                expense.getTitle());

	        response.setAmount(
	                expense.getAmount());

	        response.setCategory(
	                expense.getCategory());

	        response.setExpenseDate(
	                expense.getExpenseDate());

	        response.setPaymentMode(
	                expense.getPaymentMode());

	        response.setNotes(
	                expense.getNotes());

	        response.setCreatedAt(
	                expense.getCreatedAt());

	        return response;
	    }
}