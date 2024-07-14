package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @Column
    private BigDecimal unitPrice;
    @Column
    private String genres;

    public Book(String title, Author author, BigDecimal unitPrice, String genres){
        this.title = title;
        this.author = author;
        this.unitPrice = unitPrice;
        this.genres = genres;
    }
}
