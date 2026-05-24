package com.kaviarasan.budgetwise.service;

import java.time.LocalDateTime;
import java.util.List;
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

@Service
public class ExpenseService {


	@Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public String saveExpense(ExpenseRequest expenseRequest) {

    	User loggedInUser = getCurrentLoggedInUser();

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

		User loggedInUser = getCurrentLoggedInUser();
		
		List<ExpenseResponse> userExpenses =
	            expenseRepository
	                    .findByUser(loggedInUser);
		return userExpenses;
		
	}

	
	public String deleteExpense(Long expenseId) {

		User loggedInUser = getCurrentLoggedInUser();

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

	    User loggedInUser = getCurrentLoggedInUser();

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
	
	private User getCurrentLoggedInUser() {
		
		Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
		
		String loggedInUserEmail = authentication.getName();
		
		User loggedInUser =userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() ->new RuntimeException("User not found"));
		
		return loggedInUser;
	}
}