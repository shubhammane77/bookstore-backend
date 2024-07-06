package com.kata.bookstore.service;

import com.kata.bookstore.dao.AuthorRepository;
import com.kata.bookstore.dao.BookRepository;
import com.kata.bookstore.model.Author;
import com.kata.bookstore.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookServiceTests {

    @InjectMocks
    BookRepository bookRepository;

    @InjectMocks
    AuthorRepository authorRepository;
    @Autowired
    BookService bookService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        Author author = new Author("Test");
        authorRepository.save(author);
        var book1 = new Book();
        book1.setAuthor(author);
        book1.setTitle("TestTitle");
        book1.setStockQuantity(20);
        book1.setUnitPrice(BigDecimal.TEN);
        bookRepository.save(book1);
    }

    @Test
    public void whenGetAllBooksCalled_expectedCountShouldBe1(){
        var result = bookService.getAllBooks();
        assertEquals(1, result.size());
    }
}
