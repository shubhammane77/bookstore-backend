package com.kata.bookstore.model;

import com.kata.bookstore.exception.InvalidInputException;
import com.kata.bookstore.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private BigDecimal totalPrice;
    @Column
    private OrderStatus status;
    @Column
    private Date orderDate;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public Order(User user, BigDecimal totalPrice, OrderStatus status, Date orderDate) {
        if(user == null){
            throw new InvalidInputException("Empty user");
        }
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
        this.orderItems = new ArrayList<>();
    }

    public void addOrderItem(ShoppingCartItem shoppingCartItem){
        OrderItem orderItem = new OrderItem(this, shoppingCartItem.getBook(), shoppingCartItem.getQuantity());
        this.orderItems.add(orderItem);
    }
}
