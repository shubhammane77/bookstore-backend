package com.kata.bookstore.model.api;

import com.kata.bookstore.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class ShoppingCartItemResponse implements Serializable {
    private int quantity;
    private Book book;

}
