package com.kata.bookstore.service;

import com.kata.bookstore.dao.BookPagingRepository;
import com.kata.bookstore.dao.BookRepository;
import com.kata.bookstore.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    @Value("${page.size:10}")
    private int PageSize=10;
    @Autowired
    BookPagingRepository bookPagingRepository;
    public Page<Book> searchBook(String searchCriteria, int pageNo) {
        PageRequest pageable = PageRequest.of(pageNo, PageSize);
        return bookPagingRepository.findByTitleContainingIgnoreCase(searchCriteria, pageable);
    }

}