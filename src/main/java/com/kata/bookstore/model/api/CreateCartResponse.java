package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class CreateCartResponse {
    private int cartId;
    private BigDecimal totalPrice;
    private String ErrorMessage;
}
