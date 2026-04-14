package com.example.bibliotekssystem.service;

import com.example.bibliotekssystem.dto.BookRequestDto;
import com.example.bibliotekssystem.dto.BookResponseDto;
import com.example.bibliotekssystem.dto.BookV2ResponseDto;

import java.util.List;

public interface BookService {

    BookResponseDto createBook(BookRequestDto requestDto);

    List<BookResponseDto> getAllBooks();

    BookResponseDto getBookById(Long id);

    List<BookV2ResponseDto> getAllBooksV2();
}