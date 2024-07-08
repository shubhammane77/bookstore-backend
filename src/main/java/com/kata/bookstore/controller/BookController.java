package com.kata.bookstore.controller;

import com.kata.bookstore.model.api.BookResponse;
import com.kata.bookstore.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("search")
    public Page<BookResponse> searchBook(@RequestParam String searchCriteria, int pageNo) {
        var bookList = bookService.searchBook(searchCriteria, pageNo);
        return bookList.map(book -> modelMapper.map(book, BookResponse.class));
    }

}