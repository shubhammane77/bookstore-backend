package com.kata.bookstore.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name="Authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;

    @OneToMany
    @JoinColumn(name = "author_id")
    private Set<Book> books;

    public Author(String name) {
        this.name = name;
    }
}
