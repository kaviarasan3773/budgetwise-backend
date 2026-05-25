package com.kaviarasan.budgetwise.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaviarasan.budgetwise.dto.SalaryRequest;
import com.kaviarasan.budgetwise.dto.SalaryResponse;
import com.kaviarasan.budgetwise.entity.SalaryDetails;
import com.kaviarasan.budgetwise.entity.User;
import com.kaviarasan.budgetwise.repository.SalaryRepository;
import com.kaviarasan.budgetwise.security.SecurityUtil;

@Service
public class SalaryService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private SalaryRepository salaryRepository;

    public String saveSalary(SalaryRequest request) {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        Optional<SalaryDetails> existingSalary =
                salaryRepository.findByUserAndSalaryMonth(
                        loggedInUser,
                        request.getSalaryMonth());

        if (existingSalary.isPresent()) {
            return "Salary for this month already exists";
        }

        BigDecimal inHandSalary =
                request.getGrossSalary()
                        .subtract(request.getTotalDeductions());

        SalaryDetails salaryDetails =
                new SalaryDetails();

        salaryDetails.setSalaryMonth(
                request.getSalaryMonth());

        salaryDetails.setGrossSalary(
                request.getGrossSalary());

        salaryDetails.setTotalDeductions(
                request.getTotalDeductions());

        salaryDetails.setInHandSalary(
                inHandSalary);

        salaryDetails.setCreatedAt(
                LocalDateTime.now());

        salaryDetails.setUser(
                loggedInUser);

        salaryRepository.save(salaryDetails);

        return "Salary details saved successfully";
    }

    public List<SalaryResponse> fetchSalaryDetails() {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        List<SalaryDetails> userSalaries =
                salaryRepository.findByUser(loggedInUser);

        return userSalaries.stream()
                .map(this::mapToSalaryResponse)
                .collect(Collectors.toList());
    }

    public String updateSalary(SalaryRequest salaryRequest,Long salaryId) {

        User loggedInUser =securityUtil.getCurrentLoggedInUser();

        SalaryDetails salary =salaryRepository.findById(salaryId)
                        .orElseThrow(() ->new RuntimeException("Salary not found"));

        if (!salary.getUser().getUserId()
                .equals(loggedInUser.getUserId())) {
            return "You are not authorized to update this salary";
        }

        Optional<SalaryDetails> existingSalary =salaryRepository
        		.findByUserAndSalaryMonth(loggedInUser,salaryRequest.getSalaryMonth());

        if (existingSalary.isPresent() && !existingSalary.get().getSalaryId()
                .equals(salaryId)) {
            return "Salary for this month already exists";
        }

        BigDecimal inHandSalary =salaryRequest.getGrossSalary()
                        .subtract(salaryRequest.getTotalDeductions());

        salary.setSalaryMonth(
                salaryRequest.getSalaryMonth());

        salary.setGrossSalary(
                salaryRequest.getGrossSalary());

        salary.setTotalDeductions(
                salaryRequest.getTotalDeductions());

        salary.setInHandSalary(
                inHandSalary);

        salaryRepository.save(salary);

        return "Salary updated successfully";
    }

    public String deleteSalary(Long salaryId) {

        User loggedInUser =
                securityUtil.getCurrentLoggedInUser();

        SalaryDetails salary =
                salaryRepository.findById(salaryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salary not found"));

        if (!salary.getUser().getUserId()
                .equals(loggedInUser.getUserId())) {
            return "You are not authorized to delete this salary";
        }
        salaryRepository.delete(salary);

        return "Salary deleted successfully";
    }

    private SalaryResponse mapToSalaryResponse(SalaryDetails salaryDetails) {

        SalaryResponse response =new SalaryResponse();

        response.setSalaryId(
                salaryDetails.getSalaryId());

        response.setSalaryMonth(
                salaryDetails.getSalaryMonth());

        response.setGrossSalary(
                salaryDetails.getGrossSalary());

        response.setTotalDeductions(
                salaryDetails.getTotalDeductions());

        response.setInHandSalary(
                salaryDetails.getInHandSalary());

        return response;
    }
}