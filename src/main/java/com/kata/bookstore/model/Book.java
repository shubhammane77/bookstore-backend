package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name="Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @OneToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @Column
    private BigDecimal unitPrice;
    @Column
    private int stockQuantity;
    @Column
    private String genres;
    @Column
    private String imageUrl;

}
