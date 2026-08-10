package com.kaviarasan.budgetwise.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.ExpenseRequest;
import com.kaviarasan.budgetwise.dto.ExpenseResponse;
import com.kaviarasan.budgetwise.entity.Expense;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.exception.ExpenseNotFoundException;
import com.kaviarasan.budgetwise.exception.UnauthorizedAccessException;
import com.kaviarasan.budgetwise.repository.ExpenseRepository;
import com.kaviarasan.budgetwise.security.SecurityUtil;

@Service
public class ExpenseService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ExpenseRepository expenseRepository;

    public void saveExpense(ExpenseRequest request) {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        Expense expense = new Expense();

        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setRemarks(request.getRemarks());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setUser(loggedInUser);

        expenseRepository.save(expense);
    }

    public List<ExpenseResponse> getAllExpenses() {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        List<Expense> expenses =
                expenseRepository
                        .findByUserOrderByExpenseDateDesc(loggedInUser);

        return expenses.stream()
                .map(this::mapToExpenseResponse)
                .collect(Collectors.toList());
    }

    public void updateExpense(
            ExpenseRequest request,
            Long expenseId) {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        Expense expense =
                expenseRepository.findById(expenseId)
                        .orElseThrow(() ->
                                new ExpenseNotFoundException(
                                        "Expense not found"));

        if (!expense.getUser().getUserId()
                .equals(loggedInUser.getUserId())) {

            throw new UnauthorizedAccessException(
                    "You are not authorized to update this expense");
        }

        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setRemarks(request.getRemarks());
        expense.setUpdatedAt(LocalDateTime.now());

        expenseRepository.save(expense);
    }

    public void deleteExpense(Long expenseId) {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        Expense expense =
                expenseRepository.findById(expenseId)
                        .orElseThrow(() ->
                                new ExpenseNotFoundException(
                                        "Expense not found"));

        if (!expense.getUser().getUserId()
                .equals(loggedInUser.getUserId())) {

            throw new UnauthorizedAccessException(
                    "You are not authorized to delete this expense");
        }

        expenseRepository.delete(expense);
    }

    private ExpenseResponse mapToExpenseResponse(
            Expense expense) {

        ExpenseResponse response =
                new ExpenseResponse();

        response.setExpenseId(
                expense.getExpenseId());

        response.setCategory(
                expense.getCategory());

        response.setAmount(
                expense.getAmount());

        response.setExpenseDate(
                expense.getExpenseDate());

        response.setRemarks(
                expense.getRemarks());

        return response;
    }
}