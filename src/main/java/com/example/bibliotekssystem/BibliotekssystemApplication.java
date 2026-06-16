package com.example.bibliotekssystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;



    @EnableCaching
    @SpringBootApplication
    public class BibliotekssystemApplication {
        public static void main(String[] args) {
            SpringApplication.run(BibliotekssystemApplication.class, args);
        }
    }
