package com.example.bibliotekssystem.service;

import com.example.bibliotekssystem.dto.CreateLoanRequestDto;
import com.example.bibliotekssystem.dto.LoanDto;
import com.example.bibliotekssystem.exception.BookAlreadyOnLoanException;
import com.example.bibliotekssystem.exception.BookNotFoundException;
import com.example.bibliotekssystem.model.Book;
import com.example.bibliotekssystem.model.Loan;
import com.example.bibliotekssystem.repository.BookRepository;
import com.example.bibliotekssystem.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public LoanDto createLoan(CreateLoanRequestDto requestDto) {
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException(requestDto.getBookId()));

        boolean alreadyOnLoan = loanRepository.findByBookIdAndReturnDateIsNull(book.getId()).isPresent();
        if (alreadyOnLoan) {
            throw new BookAlreadyOnLoanException("Book is already on loan");
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(null);

        Loan savedLoan = loanRepository.save(loan);

        return mapToDto(savedLoan);
    }

    @Override
    public List<LoanDto> getAllActiveLoans() {
        return loanRepository.findAllByReturnDateIsNull()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private LoanDto mapToDto(Loan loan) {
        return new LoanDto(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                loan.getReturnDate()
        );
    }
}