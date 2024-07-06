package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateCartRequest {
    private int userId;
    private BigDecimal totalPrice;
    private List<CreateCartItemRequest> shoppingCartItems;
}
