package com.kaviarasan.budgetwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaviarasan.budgetwise.entity.Expense;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

}