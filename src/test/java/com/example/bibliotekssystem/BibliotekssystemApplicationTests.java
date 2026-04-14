package com.example.bibliotekssystem;

import com.example.bibliotekssystem.dto.AuthorDto;
import com.example.bibliotekssystem.dto.BookRequestDto;
import com.example.bibliotekssystem.dto.BookResponseDto;
import com.example.bibliotekssystem.dto.CreateLoanRequestDto;
import com.example.bibliotekssystem.dto.LoanDto;
import com.example.bibliotekssystem.repository.AuthorRepository;
import com.example.bibliotekssystem.repository.BookRepository;
import com.example.bibliotekssystem.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BibliotekssystemApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    void cleanDatabase() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void shouldCreateBook() {
        BookRequestDto request = new BookRequestDto(
                "Java Basics",
                "Ali",
                "123456",
                2024
        );

        ResponseEntity<BookResponseDto> response = restTemplate.postForEntity(
                baseUrl() + "/api/v1/books",
                request,
                BookResponseDto.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Java Basics", response.getBody().getTitle());
        assertEquals("Ali", response.getBody().getAuthor());
    }

    @Test
    void shouldGetBookById() {
        BookRequestDto request = new BookRequestDto(
                "Spring Boot",
                "Sara",
                "999999",
                2025
        );

        ResponseEntity<BookResponseDto> createResponse = restTemplate.postForEntity(
                baseUrl() + "/api/v1/books",
                request,
                BookResponseDto.class
        );

        assertNotNull(createResponse.getBody());
        Long id = createResponse.getBody().getId();

        ResponseEntity<BookResponseDto> getResponse = restTemplate.getForEntity(
                baseUrl() + "/api/v1/books/" + id,
                BookResponseDto.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Spring Boot", getResponse.getBody().getTitle());
        assertEquals("Sara", getResponse.getBody().getAuthor());
    }

    @Test
    void shouldReturn404WhenBookNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl() + "/api/v1/books/99999",
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateAuthor() {
        AuthorDto request = new AuthorDto();
        request.setName("J.K. Rowling");

        ResponseEntity<AuthorDto> response = restTemplate.postForEntity(
                baseUrl() + "/api/v1/authors",
                request,
                AuthorDto.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("J.K. Rowling", response.getBody().getName());
    }

    @Test
    void shouldCreateLoan() {
        BookRequestDto bookRequest = new BookRequestDto(
                "Clean Code",
                "Robert Martin",
                "111111",
                2008
        );

        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity(
                baseUrl() + "/api/v1/books",
                bookRequest,
                BookResponseDto.class
        );

        assertNotNull(bookResponse.getBody());
        Long bookId = bookResponse.getBody().getId();

        CreateLoanRequestDto loanRequest = new CreateLoanRequestDto();
        loanRequest.setBookId(bookId);

        ResponseEntity<LoanDto> loanResponse = restTemplate.postForEntity(
                baseUrl() + "/api/v1/loans",
                loanRequest,
                LoanDto.class
        );

        assertEquals(HttpStatus.CREATED, loanResponse.getStatusCode());
        assertNotNull(loanResponse.getBody());
        assertNotNull(loanResponse.getBody().getId());
    }
    @Test
    void shouldCreateAuthorAndBook() {
        AuthorDto authorRequest = new AuthorDto();
        authorRequest.setName("George Orwell");

        ResponseEntity<AuthorDto> authorResponse = restTemplate.postForEntity(
                baseUrl() + "/api/v1/authors",
                authorRequest,
                AuthorDto.class
        );

        assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());

        BookRequestDto bookRequest = new BookRequestDto(
                "1984",
                "George Orwell",
                "111111",
                1949
        );

        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity(
                baseUrl() + "/api/v1/books",
                bookRequest,
                BookResponseDto.class
        );

        assertEquals(HttpStatus.CREATED, bookResponse.getStatusCode());
    }

    @Test
    void shouldReturn400WhenBookInputInvalid() {
        BookRequestDto request = new BookRequestDto(
                "",
                "",
                "123456",
                2024
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl() + "/api/v1/books",
                request,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturn400WhenBookAlreadyOnLoan() {
        BookRequestDto bookRequest = new BookRequestDto(
                "DDD",
                "Eric Evans",
                "222222",
                2003
        );

        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity(
                baseUrl() + "/api/v1/books",
                bookRequest,
                BookResponseDto.class
        );

        assertNotNull(bookResponse.getBody());
        Long bookId = bookResponse.getBody().getId();

        CreateLoanRequestDto loanRequest = new CreateLoanRequestDto();
        loanRequest.setBookId(bookId);

        ResponseEntity<LoanDto> firstLoan = restTemplate.postForEntity(
                baseUrl() + "/api/v1/loans",
                loanRequest,
                LoanDto.class
        );

        assertEquals(HttpStatus.CREATED, firstLoan.getStatusCode());

        ResponseEntity<String> secondLoan = restTemplate.postForEntity(
                baseUrl() + "/api/v1/loans",
                loanRequest,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, secondLoan.getStatusCode());
    }
    @Test
    void shouldCreateOnlyOneLoanWhen100RequestsRunConcurrently() throws InterruptedException {
        BookRequestDto bookRequest = new BookRequestDto(
                "Concurrency in Java",
                "Test Author",
                "555555",
                2024
        );

        ResponseEntity<BookResponseDto> bookResponse = restTemplate.postForEntity(
                baseUrl() + "/api/v1/books",
                bookRequest,
                BookResponseDto.class
        );

        assertNotNull(bookResponse.getBody());
        Long bookId = bookResponse.getBody().getId();

        int numberOfRequests = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch readyLatch = new CountDownLatch(numberOfRequests);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfRequests);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger badRequestCount = new AtomicInteger(0);
        AtomicInteger otherCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfRequests; i++) {
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    CreateLoanRequestDto loanRequest = new CreateLoanRequestDto();
                    loanRequest.setBookId(bookId);

                    ResponseEntity<String> response = restTemplate.postForEntity(
                            baseUrl() + "/api/v1/loans",
                            loanRequest,
                            String.class
                    );

                    if (response.getStatusCode() == HttpStatus.CREATED) {
                        successCount.incrementAndGet();
                    } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        badRequestCount.incrementAndGet();
                    } else {
                        otherCount.incrementAndGet();
                    }

                } catch (Exception e) {
                    otherCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executorService.shutdown();

        assertEquals(1, successCount.get());
        assertEquals(1, loanRepository.count());
    }
}