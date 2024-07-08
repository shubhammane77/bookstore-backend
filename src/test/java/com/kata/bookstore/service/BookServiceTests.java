package com.kata.bookstore.service;

import com.kata.bookstore.dao.BookPagingRepository;
import com.kata.bookstore.model.Author;
import com.kata.bookstore.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BookServiceTests {

    @Mock
    private BookPagingRepository bookPagingRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        int pageNo = 0;

        Pageable pageable = PageRequest.of(pageNo,10);
        when(bookPagingRepository.findByTitleContainingIgnoreCase(searchCriteria,pageable)).thenReturn(new PageImpl<>(mockBooks));

        Page<Book> result = bookService.searchBook(searchCriteria,pageNo);

        assertEquals(1, result.stream().toList().size());
        assertEquals("Book1", result.stream().toList().get(0).getTitle());
    }
}