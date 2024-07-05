package com.kata.bookstore.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItems {
    private int id;
    private int orderId;
    private int bookId;
    private int quantity;

    public OrderItems(int orderId, int bookId, int quantity) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
