package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateShoppingCartRequest {
    private int cartId;
    private int bookId;
    private BigDecimal totalPrice;
    private int quantity;
}
