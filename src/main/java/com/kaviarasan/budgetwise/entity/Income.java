package com.kaviarasan.budgetwise.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "income_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;

    // Salary, Business, Freelance, Rental, Interest, Bonus, Gift, Other
    private String incomeType;

    // Infosys, Coffee Shop, Upwork, House Rent...
    private String sourceName;

    // Actual amount received
    private BigDecimal amount;

    // Example: 2026-08
    private String incomeMonth;

    // Date on which income was received
    private LocalDate incomeDate;

    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}