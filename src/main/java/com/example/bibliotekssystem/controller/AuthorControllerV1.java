package com.example.bibliotekssystem.controller;

import com.example.bibliotekssystem.dto.AuthorDto;
import com.example.bibliotekssystem.dto.BookResponseDto;
import com.example.bibliotekssystem.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorControllerV1 {

    private final AuthorService authorService;

    public AuthorControllerV1(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto authorDto) {
        AuthorDto createdAuthor = authorService.createAuthor(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        AuthorDto author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthorId(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getBooksByAuthorId(id));
    }
}
