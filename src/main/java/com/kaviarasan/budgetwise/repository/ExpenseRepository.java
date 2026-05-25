package com.kaviarasan.budgetwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaviarasan.budgetwise.dto.ExpenseResponse;
import com.kaviarasan.budgetwise.entity.Expense;
import com.kaviarasan.budgetwise.entity.User;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

	List<Expense> findByUser(User user);

}