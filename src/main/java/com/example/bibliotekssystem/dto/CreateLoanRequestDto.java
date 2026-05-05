
package com.example.bibliotekssystem.dto;
import jakarta.validation.constraints.NotNull;
public class CreateLoanRequestDto {
    private Long bookId;

    public CreateLoanRequestDto() {
    }
    @NotNull(message = "Book id is required")

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