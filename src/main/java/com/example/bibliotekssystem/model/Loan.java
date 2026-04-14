package com.example.bibliotekssystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "loan", uniqueConstraints = {
        @UniqueConstraint(columnNames = "book_id")
})
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private Book book;

    private LocalDate loanDate;
    private LocalDate returnDate;

    public Loan() {
    }

    public Loan(Book book, LocalDate loanDate, LocalDate returnDate) {
        this.book = book;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    @PrePersist
    public void prePersist() {
        if (loanDate == null) {
            loanDate = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}