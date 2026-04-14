package com.example.bibliotekssystem.repository;

import com.example.bibliotekssystem.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}