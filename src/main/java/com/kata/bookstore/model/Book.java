package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name="Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String isbn;
    @OneToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @Column
    private String description;
    @Column
    private BigDecimal unitPrice;
    @Column
    private Date publishDate;
    @Column
    private int stockQuantity;
    @Column
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
