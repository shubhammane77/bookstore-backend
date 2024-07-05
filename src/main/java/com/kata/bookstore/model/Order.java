package com.kata.bookstore.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter @Setter
public class Order {
    private int id;
    private int userId;
    private BigDecimal totalPrice;
    private String status;
    private Date orderDate;

    public Order(int userId, BigDecimal totalPrice, String status, Date orderDate) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }
}
