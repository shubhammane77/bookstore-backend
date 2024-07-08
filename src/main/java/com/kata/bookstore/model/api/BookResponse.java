package com.kata.bookstore.model.api;

import com.kata.bookstore.model.Author;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter @Setter
public class BookResponse implements Serializable {
    private int id;
    private String title;
    private Author author;
    private BigDecimal unitPrice;
    private int stockQuantity;
    private String genres;
    private String imageUrl;

}
