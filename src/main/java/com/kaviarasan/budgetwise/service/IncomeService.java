package com.kaviarasan.budgetwise.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.IncomeRequest;
import com.kaviarasan.budgetwise.dto.IncomeResponse;
import com.kaviarasan.budgetwise.entity.Income;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.exception.IncomeNotFoundException;
import com.kaviarasan.budgetwise.exception.UnauthorizedAccessException;
import com.kaviarasan.budgetwise.repository.IncomeRepository;
import com.kaviarasan.budgetwise.security.SecurityUtil;

@Service
public class IncomeService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IncomeRepository incomeRepository;

    public void saveIncome(IncomeRequest request) {

        User loggedInUser = securityUtil.getCurrentLoggedInUser();

        Income income = new Income();

        income.setIncomeType(request.getIncomeType());
        income.setSourceName(request.getSourceName());
        income.setAmount(request.getAmount());
        income.setIncomeMonth(request.getIncomeMonth());
        income.setIncomeDate(request.getIncomeDate());
        income.setRemarks(request.getRemarks());
        income.setCreatedAt(LocalDateTime.now());
        income.setUser(loggedInUser);

        incomeRepository.save(income);
    }

    public List<IncomeResponse> getAllIncome() {

        User loggedInUser = securityUtil.getCurrentLoggedInUser();

        return incomeRepository.findByUserOrderByIncomeMonthDescIncomeDateDesc(loggedInUser)
                .stream()
                .map(this::mapToIncomeResponse)
                .collect(Collectors.toList());
    }

    public void updateIncome(IncomeRequest request, Long incomeId) {

        User loggedInUser = securityUtil.getCurrentLoggedInUser();

        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() ->
                        new IncomeNotFoundException("Income not found"));

        if (!income.getUser().getUserId().equals(loggedInUser.getUserId())) {
            throw new UnauthorizedAccessException(
                    "You are not authorized to update this income");
        }

        income.setIncomeType(request.getIncomeType());
        income.setSourceName(request.getSourceName());
        income.setAmount(request.getAmount());
        income.setIncomeMonth(request.getIncomeMonth());
        income.setIncomeDate(request.getIncomeDate());
        income.setRemarks(request.getRemarks());
        income.setUpdatedAt(LocalDateTime.now());

        incomeRepository.save(income);
    }

    public void deleteIncome(Long incomeId) {

        User loggedInUser = securityUtil.getCurrentLoggedInUser();

        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() ->
                        new IncomeNotFoundException("Income not found"));

        if (!income.getUser().getUserId().equals(loggedInUser.getUserId())) {
            throw new UnauthorizedAccessException(
                    "You are not authorized to delete this income");
        }

        incomeRepository.delete(income);
    }

    private IncomeResponse mapToIncomeResponse(Income income) {

        IncomeResponse response = new IncomeResponse();

        response.setIncomeId(income.getIncomeId());
        response.setIncomeType(income.getIncomeType());
        response.setSourceName(income.getSourceName());
        response.setAmount(income.getAmount());
        response.setIncomeMonth(income.getIncomeMonth());
        response.setIncomeDate(income.getIncomeDate());
        response.setRemarks(income.getRemarks());

        return response;
    }
}