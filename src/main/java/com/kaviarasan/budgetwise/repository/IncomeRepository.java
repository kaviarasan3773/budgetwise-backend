package com.kaviarasan.budgetwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaviarasan.budgetwise.entity.Income;
import com.kaviarasan.budgetwise.entity.User;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);

    List<Income> findByUserOrderByIncomeMonthDescIncomeDateDesc(
            User user);

    List<Income> findByUserAndIncomeMonthOrderByIncomeDateDesc(
            User user,
            String incomeMonth);

}