package com.example.bibliotekssystem.controller;

import com.example.bibliotekssystem.dto.BookRequestDto;
import com.example.bibliotekssystem.dto.BookResponseDto;
import com.example.bibliotekssystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookControllerV1 {

    private final BookService bookService;

    public BookControllerV1(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Create a new book")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @GetMapping
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto createBook(@Valid @RequestBody BookRequestDto requestDto) {
        return bookService.createBook(requestDto);

    }

    @Operation(summary = "Get all books in API v1")
    @ApiResponse(responseCode = "200", description = "Books fetched successfully")
    @GetMapping
    public List<BookResponseDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Get one book by id in API v1")
    @ApiResponse(responseCode = "200", description = "Book found")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @GetMapping("/{id}")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
}