package com.example.bibliotekssystem.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "authorEntity")
    private List<Book> books = new ArrayList<>();

    public Author() {
    }

    public Author(Long id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }
        this.name = name;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}