package com.kaviarasan.budgetwise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaviarasan.budgetwise.dto.SalaryRequest;
import com.kaviarasan.budgetwise.dto.SalaryResponse;
import com.kaviarasan.budgetwise.service.SalaryService;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
	
	@Autowired
	private SalaryService salaryService;
	
	@PostMapping
	public String saveSalary(@RequestBody SalaryRequest request) {
		return salaryService.saveSalary(request);
	}
	
	@GetMapping
	public List<SalaryResponse> getUserSalary(){
		return salaryService.fetchSalaryDetails();
	}
	
	@PutMapping("/{salaryId}")
	public String updateSalary(@RequestBody SalaryRequest salaryRequest,@PathVariable Long salaryId) {
    	return salaryService.updateSalary(salaryRequest,salaryId);
    }
	
	@DeleteMapping("/{salaryId}")
    public String deleteSalary(@PathVariable Long salaryId) {
    	return salaryService.deleteSalary(salaryId);
    }

}
