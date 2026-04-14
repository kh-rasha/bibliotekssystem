package com.example.bibliotekssystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Book response DTO for API v2")
public class BookV2ResponseDto {

    private String title;
    private String author;
    private boolean available;

    public BookV2ResponseDto() {
    }

    public BookV2ResponseDto(String title, String author, boolean available) {
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}