package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartItemRequest {
    private int bookId;
    private int quantity;
}
