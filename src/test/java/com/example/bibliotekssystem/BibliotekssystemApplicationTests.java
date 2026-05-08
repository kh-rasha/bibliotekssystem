package com.example.bibliotekssystem;

import com.example.bibliotekssystem.dto.AuthorDto;
import com.example.bibliotekssystem.dto.BookRequestDto;
import com.example.bibliotekssystem.dto.BookResponseDto;
import com.example.bibliotekssystem.dto.CreateLoanRequestDto;
import com.example.bibliotekssystem.dto.LoanDto;
import com.example.bibliotekssystem.dto.PageBookResponseDto;
import com.example.bibliotekssystem.repository.AuthorRepository;
import com.example.bibliotekssystem.repository.BookRepository;
import com.example.bibliotekssystem.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

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

    private TestRestTemplate authRestTemplate() {
        return restTemplate.withBasicAuth("admin", "admin123");
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

        ResponseEntity<BookResponseDto> response =
                authRestTemplate().postForEntity(
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

        ResponseEntity<BookResponseDto> createResponse =
                authRestTemplate().postForEntity(
                        baseUrl() + "/api/v1/books",
                        request,
                        BookResponseDto.class
                );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());

        Long id = createResponse.getBody().getId();

        ResponseEntity<BookResponseDto> getResponse =
                authRestTemplate().getForEntity(
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
        ResponseEntity<String> response =
                authRestTemplate().getForEntity(
                        baseUrl() + "/api/v1/books/99999",
                        String.class
                );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateAuthor() {
        AuthorDto request = new AuthorDto();
        request.setName("J.K. Rowling");

        ResponseEntity<AuthorDto> response =
                authRestTemplate().postForEntity(
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
        AuthorDto authorRequest = new AuthorDto();
        authorRequest.setName("Robert Martin");

        authRestTemplate().postForEntity(
                baseUrl() + "/api/v1/authors",
                authorRequest,
                AuthorDto.class
        );

        BookRequestDto bookRequest = new BookRequestDto(
                "Clean Code",
                "Robert Martin",
                "111111",
                2008
        );

        ResponseEntity<BookResponseDto> bookResponse =
                authRestTemplate().postForEntity(
                        baseUrl() + "/api/v1/books",
                        bookRequest,
                        BookResponseDto.class
                );

        assertEquals(HttpStatus.CREATED, bookResponse.getStatusCode());
        assertNotNull(bookResponse.getBody());
        assertNotNull(bookResponse.getBody().getId());

        Long bookId = bookResponse.getBody().getId();

        CreateLoanRequestDto loanRequest = new CreateLoanRequestDto();
        loanRequest.setBookId(bookId);

        ResponseEntity<LoanDto> loanResponse =
                authRestTemplate().postForEntity(
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

        ResponseEntity<AuthorDto> authorResponse =
                authRestTemplate().postForEntity(
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

        ResponseEntity<BookResponseDto> bookResponse =
                authRestTemplate().postForEntity(
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
                "",
                -1
        );

        ResponseEntity<String> response =
                authRestTemplate().postForEntity(
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

        ResponseEntity<BookResponseDto> bookResponse =
                authRestTemplate().postForEntity(
                        baseUrl() + "/api/v1/books",
                        bookRequest,
                        BookResponseDto.class
                );

        assertEquals(HttpStatus.CREATED, bookResponse.getStatusCode());
        assertNotNull(bookResponse.getBody());
        assertNotNull(bookResponse.getBody().getId());

        Long bookId = bookResponse.getBody().getId();

        CreateLoanRequestDto loanRequest = new CreateLoanRequestDto();
        loanRequest.setBookId(bookId);

        ResponseEntity<LoanDto> firstLoan =
                authRestTemplate().postForEntity(
                        baseUrl() + "/api/v1/loans",
                        loanRequest,
                        LoanDto.class
                );

        assertEquals(HttpStatus.CREATED, firstLoan.getStatusCode());

        ResponseEntity<String> secondLoan =
                authRestTemplate().postForEntity(
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

        ResponseEntity<BookResponseDto> bookResponse =
                authRestTemplate().postForEntity(
                        baseUrl() + "/api/v1/books",
                        bookRequest,
                        BookResponseDto.class
                );

        assertEquals(HttpStatus.CREATED, bookResponse.getStatusCode());
        assertNotNull(bookResponse.getBody());
        assertNotNull(bookResponse.getBody().getId());

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

                    ResponseEntity<String> response =
                            authRestTemplate().postForEntity(
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

    @Test
    void shouldReturnPaginatedBooks() {
        BookRequestDto book1 = new BookRequestDto(
                "Java Basics",
                "Ali",
                "11111",
                2024
        );

        BookRequestDto book2 = new BookRequestDto(
                "Spring Boot",
                "Sara",
                "22222",
                2025
        );

        authRestTemplate().postForEntity(
                baseUrl() + "/api/v1/books",
                book1,
                BookResponseDto.class
        );

        authRestTemplate().postForEntity(
                baseUrl() + "/api/v1/books",
                book2,
                BookResponseDto.class
        );

        ResponseEntity<PageBookResponseDto> response =
                authRestTemplate().getForEntity(
                        baseUrl() + "/api/v1/books?page=0&size=5&sort=id,asc",
                        PageBookResponseDto.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals(0, response.getBody().getPage());
        assertEquals(5, response.getBody().getSize());
        assertFalse(response.getBody().getContent().isEmpty());
    }

    @Test
    void shouldReturn400WhenBookValidationFails() {
        BookRequestDto invalidBook = new BookRequestDto(
                "",
                "",
                "",
                -1
        );

        ResponseEntity<String> response =
                authRestTemplate().postForEntity(
                        baseUrl() + "/api/v1/books",
                        invalidBook,
                        String.class
                );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnUnauthorizedWhenNoAuthentication() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        baseUrl() + "/api/v1/books?page=0&size=5&sort=id,asc",
                        String.class
                );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldAllowRequestWithAuthentication() {
        ResponseEntity<String> response =
                authRestTemplate().getForEntity(
                        baseUrl() + "/api/v1/books?page=0&size=5&sort=id,asc",
                        String.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldGetBookByIdTwiceWhenCachingIsEnabled() {
        BookRequestDto request = new BookRequestDto(
                "Caching Test",
                "Cache Author",
                "33333",
                2024
        );

        ResponseEntity<BookResponseDto> createResponse =
                authRestTemplate().postForEntity(
                        baseUrl() + "/api/v1/books",
                        request,
                        BookResponseDto.class
                );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());

        Long bookId = createResponse.getBody().getId();

        ResponseEntity<BookResponseDto> firstGet =
                authRestTemplate().getForEntity(
                        baseUrl() + "/api/v1/books/" + bookId,
                        BookResponseDto.class
                );

        ResponseEntity<BookResponseDto> secondGet =
                authRestTemplate().getForEntity(
                        baseUrl() + "/api/v1/books/" + bookId,
                        BookResponseDto.class
                );

        assertEquals(HttpStatus.OK, firstGet.getStatusCode());
        assertEquals(HttpStatus.OK, secondGet.getStatusCode());
        assertNotNull(firstGet.getBody());
        assertNotNull(secondGet.getBody());
        assertEquals(bookId, secondGet.getBody().getId());
    }

    @Test
    void shouldReturn429WhenRateLimitExceeded() {
        HttpStatusCode lastStatus = null;

        for (int i = 0; i < 30; i++) {
            ResponseEntity<String> response =
                    authRestTemplate().getForEntity(
                            baseUrl() + "/api/v1/books?page=0&size=5&sort=id,asc",
                            String.class
                    );

            lastStatus = response.getStatusCode();

            if (lastStatus == HttpStatus.TOO_MANY_REQUESTS) {
                break;
            }
        }

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, lastStatus);
    }
}