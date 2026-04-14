package com.example.bibliotekssystem.repository;

import com.example.bibliotekssystem.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findAllByReturnDateIsNull();
}