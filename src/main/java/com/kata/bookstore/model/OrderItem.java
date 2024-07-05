package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "OrderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "order_id")
    private int orderId;
    @Column
    private int quantity;
    @Column(name = "book_id")
    private int bookId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, updatable = false, insertable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false, updatable = false, insertable = false)
    private Book book;

    public OrderItem(int orderId, int bookId, int quantity) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
