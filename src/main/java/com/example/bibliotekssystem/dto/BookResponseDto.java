package com.example.bibliotekssystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for book data")
public class BookResponseDto {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;

    public BookResponseDto() {
    }

    public BookResponseDto(Long id, String title, String author, String isbn, int publishedYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }
}