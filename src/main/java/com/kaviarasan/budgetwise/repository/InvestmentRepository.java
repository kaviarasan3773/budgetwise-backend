package com.kaviarasan.budgetwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaviarasan.budgetwise.entity.Investment;
import com.kaviarasan.budgetwise.entity.User;

public interface InvestmentRepository
        extends JpaRepository<Investment, Long> {

    List<Investment> findByUserOrderByInvestmentDateDesc(
            User user);
}