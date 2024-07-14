package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "ShoppingCartItems")
public class ShoppingCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public ShoppingCartItem(ShoppingCart shoppingCart, Book book) {
        this.shoppingCart = shoppingCart;
        this.book = book;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
