package com.kata.bookstore.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Author {
    private int id;
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
