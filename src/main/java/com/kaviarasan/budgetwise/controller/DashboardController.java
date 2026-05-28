package com.kaviarasan.budgetwise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaviarasan.budgetwise.dto.DashboardResponse;
import com.kaviarasan.budgetwise.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping
	public DashboardResponse getDashboardSummary() {
		return dashboardService.getDashboardSummary();
	}

}
