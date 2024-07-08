package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateShoppingCartResponse {
    private BigDecimal totalPrice;
    private String ErrorMessage;
}
