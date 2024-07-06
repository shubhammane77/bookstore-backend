package com.kata.bookstore.service;

import com.kata.bookstore.dao.BookRepository;
import com.kata.bookstore.model.Author;
import com.kata.bookstore.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        Author author = new Author("Author1");
        Book book1 = new Book();
        book1.setTitle("Book1");
        book1.setAuthor(author);
        List<Book> mockBooks = Arrays.asList(book1);

        when(bookRepository.findAll()).thenReturn(mockBooks);

        // Act
        List<Book> result = bookService.getAllBooks();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Book1", result.get(0).getTitle());
    }

    @Test
    void testSearchBook() {
        // Arrange
        String searchCriteria = "Book";
        Author author = new Author("Author1");
        Book book1 = new Book();
        book1.setTitle("Book1");
        book1.setAuthor(author);
        List<Book> mockBooks = Arrays.asList(book1);

        when(bookRepository.findByTitleContainingIgnoreCase(searchCriteria)).thenReturn(mockBooks);

        // Act
        List<Book> result = bookService.searchBook(searchCriteria);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Book1", result.get(0).getTitle());
    }
}