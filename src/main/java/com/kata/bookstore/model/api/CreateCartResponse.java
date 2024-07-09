package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateCartResponse implements Serializable {
    private int cartId;
    private BigDecimal totalPrice;
    private String ErrorMessage;
}
