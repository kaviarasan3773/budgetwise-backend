package com.kaviarasan.budgetwise.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.ExpenseRequest;
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

        Authentication authentication =SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String loggedInUserEmail =authentication.getName();

        User loggedInUser =userRepository.findByEmail(loggedInUserEmail)
                        .orElseThrow(() ->new RuntimeException("User not found"));

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
}