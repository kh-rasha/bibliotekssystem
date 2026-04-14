package com.example.bibliotekssystem.dto;

public class CreateLoanRequestDto {
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