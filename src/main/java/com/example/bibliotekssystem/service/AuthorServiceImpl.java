package com.example.bibliotekssystem.service;

import com.example.bibliotekssystem.dto.AuthorDto;
import com.example.bibliotekssystem.dto.BookResponseDto;
import com.example.bibliotekssystem.exception.AuthorNotFoundException;
import com.example.bibliotekssystem.model.Author;
import com.example.bibliotekssystem.model.Book;
import com.example.bibliotekssystem.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.getName());

        Author savedAuthor = authorRepository.save(author);

        return mapToDto(savedAuthor);
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " not found"));

        return mapToDto(author);
    }

    @Override
    public List<BookResponseDto> getBooksByAuthorId(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id " + authorId + " not found"));

        return author.getBooks().stream()
                .map(this::mapBookToDto)
                .toList();
    }

    private AuthorDto mapToDto(Author author) {
        int numberOfBooks = author.getBooks() != null ? author.getBooks().size() : 0;
        return new AuthorDto(author.getId(), author.getName(), numberOfBooks);
    }

    private BookResponseDto mapBookToDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear()
        );
    }
}
