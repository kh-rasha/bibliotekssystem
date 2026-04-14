package com.example.bibliotekssystem.repository;

import com.example.bibliotekssystem.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
