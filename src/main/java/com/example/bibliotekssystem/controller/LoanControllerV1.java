package com.example.bibliotekssystem.controller;

import com.example.bibliotekssystem.dto.CreateLoanRequestDto;
import com.example.bibliotekssystem.dto.LoanDto;
import com.example.bibliotekssystem.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanControllerV1 {

    private final LoanService loanService;

    public LoanControllerV1(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@Valid @RequestBody CreateLoanRequestDto request) {
        LoanDto loan = loanService.createLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @GetMapping
    public ResponseEntity<Page<LoanDto>> getAllLoans(Pageable pageable) {
        return ResponseEntity.ok(loanService.getAllLoans(pageable));
    }
}