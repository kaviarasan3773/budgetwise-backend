package com.kaviarasan.budgetwise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaviarasan.budgetwise.dto.SalaryResponse;
import com.kaviarasan.budgetwise.entity.SalaryDetails;
import com.kaviarasan.budgetwise.entity.User;

public interface SalaryRepository
        extends JpaRepository<SalaryDetails, Long> {

    List<SalaryDetails> findByUser(User user);

    Optional<SalaryDetails> findByUserAndSalaryMonth(
            User user,
            String salaryMonth);
}