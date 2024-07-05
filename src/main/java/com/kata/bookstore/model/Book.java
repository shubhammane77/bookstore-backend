package com.kata.bookstore.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter @Setter
public class Book {
    private int id;
    private String title;
    private String isbn;
    private Author author;
    private String description;
    private BigDecimal unitPrice;
    private Date publishDate;
    private int stockQuantity;
    private String genres;

    public Book(String title, String isbn, Author author, String description, BigDecimal unitPrice, Date publishDate, int stockQuantity, String genres) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.unitPrice = unitPrice;
        this.publishDate = publishDate;
        this.stockQuantity = stockQuantity;
        this.genres = genres;
    }
}
