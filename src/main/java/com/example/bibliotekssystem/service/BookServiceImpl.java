package com.example.bibliotekssystem.service;

import com.example.bibliotekssystem.dto.BookRequestDto;
import com.example.bibliotekssystem.dto.BookResponseDto;
import com.example.bibliotekssystem.dto.BookV2ResponseDto;
import com.example.bibliotekssystem.exception.BookNotFoundException;
import com.example.bibliotekssystem.model.Book;
import com.example.bibliotekssystem.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookResponseDto createBook(BookRequestDto requestDto) {
        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPublishedYear(requestDto.getPublishedYear());

        Book savedBook = bookRepository.save(book);
        return mapToResponseDto(savedBook);
    }

    @Override
    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return mapToResponseDto(book);
    }

    @Override
    public List<BookV2ResponseDto> getAllBooksV2() {
        return bookRepository.findAll()
                .stream()
                .map(book -> new BookV2ResponseDto(
                        book.getTitle(),
                        book.getAuthor(),
                        true
                ))
                .toList();
    }

    private BookResponseDto mapToResponseDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear()
        );
    }
}