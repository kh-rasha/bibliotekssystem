package com.example.bibliotekssystem.controller;

import com.example.bibliotekssystem.dto.BookV2ResponseDto;
import com.example.bibliotekssystem.dto.BookV2WrapperDto;
import com.example.bibliotekssystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/books")
public class BookControllerV2 {

    private final BookService bookService;

    public BookControllerV2(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books in API v2")
    @ApiResponse(responseCode = "200", description = "Books fetched successfully")
    @GetMapping
    public BookV2WrapperDto getAllBooksV2() {
        List<BookV2ResponseDto> books = bookService.getAllBooksV2();
        return new BookV2WrapperDto(books, "v2");
    }
}