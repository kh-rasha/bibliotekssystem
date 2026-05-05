package com.example.bibliotekssystem.service;

import com.example.bibliotekssystem.dto.AuthorDto;
import com.example.bibliotekssystem.dto.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {
    AuthorDto createAuthor(AuthorDto authorDto);
    AuthorDto getAuthorById(Long id);
    List<BookResponseDto> getBooksByAuthorId(Long authorId);
    Page<BookResponseDto> getBooksByAuthorId(Long authorId, Pageable pageable);
}