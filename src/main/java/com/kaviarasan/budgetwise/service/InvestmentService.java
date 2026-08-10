package com.kaviarasan.budgetwise.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.InvestmentRequest;
import com.kaviarasan.budgetwise.dto.InvestmentResponse;
import com.kaviarasan.budgetwise.entity.Investment;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.exception.InvestmentNotFoundException;
import com.kaviarasan.budgetwise.exception.UnauthorizedAccessException;
import com.kaviarasan.budgetwise.repository.InvestmentRepository;
import com.kaviarasan.budgetwise.security.SecurityUtil;

@Service
public class InvestmentService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private InvestmentRepository investmentRepository;

    public void saveInvestment(InvestmentRequest request) {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        Investment investment = new Investment();

        investment.setInvestmentType(
                request.getInvestmentType());

        investment.setInvestmentName(
                request.getInvestmentName());

        investment.setAmount(
                request.getAmount());

        investment.setInvestmentDate(
                request.getInvestmentDate());

        investment.setRemarks(
                request.getRemarks());

        investment.setCreatedAt(
                LocalDateTime.now());

        investment.setUser(loggedInUser);

        investmentRepository.save(investment);
    }

    public List<InvestmentResponse> getAllInvestments() {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        List<Investment> investments =
                investmentRepository
                        .findByUserOrderByInvestmentDateDesc(
                                loggedInUser);

        return investments.stream()
                .map(this::mapToInvestmentResponse)
                .collect(Collectors.toList());
    }

    public void updateInvestment(
            InvestmentRequest request,
            Long investmentId) {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        Investment investment =
                investmentRepository.findById(investmentId)
                        .orElseThrow(() ->
                                new InvestmentNotFoundException(
                                        "Investment not found"));

        if (!investment.getUser().getUserId()
                .equals(loggedInUser.getUserId())) {

            throw new UnauthorizedAccessException(
                    "You are not authorized to update this investment");
        }

        investment.setInvestmentType(
                request.getInvestmentType());

        investment.setInvestmentName(
                request.getInvestmentName());

        investment.setAmount(
                request.getAmount());

        investment.setInvestmentDate(
                request.getInvestmentDate());

        investment.setRemarks(
                request.getRemarks());

        investment.setUpdatedAt(
                LocalDateTime.now());

        investmentRepository.save(investment);
    }

    public void deleteInvestment(Long investmentId) {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        Investment investment =
                investmentRepository.findById(investmentId)
                        .orElseThrow(() ->
                                new InvestmentNotFoundException(
                                        "Investment not found"));

        if (!investment.getUser().getUserId()
                .equals(loggedInUser.getUserId())) {

            throw new UnauthorizedAccessException(
                    "You are not authorized to delete this investment");
        }

        investmentRepository.delete(investment);
    }

    private InvestmentResponse mapToInvestmentResponse(
            Investment investment) {

        InvestmentResponse response =
                new InvestmentResponse();

        response.setInvestmentId(
                investment.getInvestmentId());

        response.setInvestmentType(
                investment.getInvestmentType());

        response.setInvestmentName(
                investment.getInvestmentName());

        response.setAmount(
                investment.getAmount());

        response.setInvestmentDate(
                investment.getInvestmentDate());

        response.setRemarks(
                investment.getRemarks());

        return response;
    }
}