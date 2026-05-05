package com.example.bibliotekssystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorDto {
    private Long id;
    private String name;
    private int numberOfBooks;

    public AuthorDto() {
    }

    public AuthorDto(Long id, String name, int numberOfBooks) {
        this.id = id;
        this.name = name;
        this.numberOfBooks = numberOfBooks;
    }
    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }
}
