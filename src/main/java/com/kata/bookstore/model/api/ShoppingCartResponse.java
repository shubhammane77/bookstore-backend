package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class ShoppingCartResponse implements Serializable {
    private int id;
    private BigDecimal totalPrice;
    private List<ShoppingCartItemResponse> shoppingCartItems;
}
