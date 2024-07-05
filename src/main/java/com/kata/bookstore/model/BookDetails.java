package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "BookDetails")
public class BookDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String isbn;
    @Column
    private String description;
    @Column
    private Date publishDate;
    @Column
    private String genres;

    @OneToOne
    @MapsId
    private Book book;

    public BookDetails(String isbn, String description, Date publishDate, String genres) {
        this.isbn = isbn;
        this.description = description;
        this.publishDate = publishDate;
        this.genres = genres;
    }
}
