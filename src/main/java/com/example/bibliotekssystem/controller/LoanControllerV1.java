package com.example.bibliotekssystem.controller;

import com.example.bibliotekssystem.dto.CreateLoanRequestDto;
import com.example.bibliotekssystem.dto.LoanDto;
import com.example.bibliotekssystem.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanControllerV1 {

    private final LoanService loanService;

    public LoanControllerV1(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@RequestBody CreateLoanRequestDto requestDto) {
        LoanDto createdLoan = loanService.createLoan(requestDto);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LoanDto>> getAllActiveLoans() {
        return ResponseEntity.ok(loanService.getAllActiveLoans());
    }
}