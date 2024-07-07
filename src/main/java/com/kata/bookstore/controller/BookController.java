package com.kata.bookstore.controller;

import com.kata.bookstore.mapper.MappingService;
import com.kata.bookstore.model.api.BookResponse;
import com.kata.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    @Autowired
    BookService bookService;
    @Autowired
    MappingService mappingService;

    @GetMapping("getAllBooks")
    public List<BookResponse> getAllBooks() {
        var bookList = bookService.getAllBooks();
        return mappingService.mapList(bookList, BookResponse.class);
    }

    @GetMapping("searchBook")
    public List<BookResponse> searchBook(@RequestParam String searchCriteria) {
        var bookList = bookService.searchBook(searchCriteria);
        return mappingService.mapList(bookList, BookResponse.class);
    }

}