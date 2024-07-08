package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderRequest {
    private int cartId;
    private int userId;
}
