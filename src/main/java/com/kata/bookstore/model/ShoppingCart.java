package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "ShoppingCarts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private int userId;
    @Column
    private BigDecimal totalPrice;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCartItem> shoppingCartItems;

    public ShoppingCart(int userId) {
        this.userId = userId;
        this.totalPrice = BigDecimal.ZERO;
    }

    public BigDecimal calculateTotalPrice(){
        var totalPriceList = shoppingCartItems.stream()
                .map(x -> BigDecimal.valueOf(x.getQuantity()).multiply(x.getBook().getUnitPrice()))
                .collect(Collectors.toList());
        return totalPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addShoppingCartItem(ShoppingCartItem shoppingCartItem){
        if(this.shoppingCartItems!=null)
            this.shoppingCartItems.add(shoppingCartItem);
    }
}
