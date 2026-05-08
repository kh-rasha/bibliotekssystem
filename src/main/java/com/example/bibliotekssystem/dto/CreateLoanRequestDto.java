package com.example.bibliotekssystem.dto;

import jakarta.validation.constraints.NotNull;

public class CreateLoanRequestDto {

    @NotNull(message = "Book id is required")
    private Long bookId;

    public CreateLoanRequestDto() {
    }

    public CreateLoanRequestDto(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}