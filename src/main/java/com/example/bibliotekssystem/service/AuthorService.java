package com.example.bibliotekssystem.service;

import com.example.bibliotekssystem.dto.AuthorDto;
import com.example.bibliotekssystem.dto.BookResponseDto;

import java.util.List;

public interface AuthorService {
    AuthorDto createAuthor(AuthorDto authorDto);
    AuthorDto getAuthorById(Long id);
    List<BookResponseDto> getBooksByAuthorId(Long authorId);
}