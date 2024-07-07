package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "ShoppingCarts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private BigDecimal totalPrice;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCartItem> shoppingCartItems;

    public ShoppingCart() {
        this.totalPrice = BigDecimal.ZERO;
    }

    public BigDecimal calculateTotalPrice() {
        var totalPriceList = shoppingCartItems.stream()
                .map(x -> BigDecimal.valueOf(x.getQuantity()).multiply(x.getBook().getUnitPrice()))
                .collect(Collectors.toList());
        return totalPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        if (this.shoppingCartItems == null)
            this.shoppingCartItems = new ArrayList<>();
        this.shoppingCartItems.add(shoppingCartItem);

    }
}
