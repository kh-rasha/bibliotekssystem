package com.example.bibliotekssystem.service;

import com.example.bibliotekssystem.dto.CreateLoanRequestDto;
import com.example.bibliotekssystem.dto.LoanDto;

import java.util.List;

public interface LoanService {
    LoanDto createLoan(CreateLoanRequestDto requestDto);
    List<LoanDto> getAllActiveLoans();

}