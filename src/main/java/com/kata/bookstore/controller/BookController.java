package com.kata.bookstore.controller;

import com.kata.bookstore.mapper.MappingService;
import com.kata.bookstore.model.api.BookResponse;
import com.kata.bookstore.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    @Autowired
    BookService bookService;
    @Autowired
    MappingService mappingService;

    @Autowired
    ModelMapper modelMapper;
    @GetMapping("search")
    public Page<BookResponse> searchBook(@RequestParam String searchCriteria, int pageNo ) {
        var bookList = bookService.searchBook(searchCriteria, pageNo);
        return  bookList.map(book -> modelMapper.map(book, BookResponse.class));
    }

}