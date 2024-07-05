package com.kata.bookstore.controller;

import com.kata.bookstore.model.Book;
import com.kata.bookstore.model.api.BookDto;
import com.kata.bookstore.service.BookService;
import com.kata.bookstore.service.MappingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    MappingService mappingService;
    @GetMapping("getAllBooks")
    public List<BookDto> getAllBooks(){
        var bookList = bookService.getAllBooks();
        return mappingService.mapList(bookList, BookDto.class);
    }

}