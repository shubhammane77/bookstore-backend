package com.kata.bookstore.model;

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
@NoArgsConstructor
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

    public ShoppingCart(User user) {
        this.totalPrice = BigDecimal.ZERO;
        this.user=user;
    }


    public void calculateTotalPrice() {
       this.totalPrice = shoppingCartItems.stream()
                .map(x -> BigDecimal.valueOf(x.getQuantity()).multiply(x.getBook().getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        if (this.shoppingCartItems == null)
            this.shoppingCartItems = new ArrayList<>();
        this.shoppingCartItems.add(shoppingCartItem);

    }

    public BigDecimal addBook(Book book, int quantity) {
        if (this.shoppingCartItems == null)
            this.shoppingCartItems = new ArrayList<>();
        int existingBookIndex = -1;
        for(int i=0;i<this.shoppingCartItems.size();i++){
            ShoppingCartItem currentItem = this.shoppingCartItems.get(i);
            if(currentItem.getBook().getId() == book.getId()){
                existingBookIndex = i;
                break;
            }
        }
        if(existingBookIndex == -1){
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem(this,book);
            shoppingCartItem.updateQuantity(quantity);
            this.shoppingCartItems.add(shoppingCartItem);
        } else {
            this.shoppingCartItems.get(existingBookIndex).updateQuantity(quantity);
        }

        calculateTotalPrice();
        return this.totalPrice;
    }

    public BigDecimal removeBook(Book book) throws Exception {
        int existingBookIndex = -1;
        for(int i=0;i<this.shoppingCartItems.size();i++){
            ShoppingCartItem currentItem = this.shoppingCartItems.get(i);
            if(currentItem.getBook().getId() == book.getId()){
                existingBookIndex = i;
                break;
            }
        }
        if(existingBookIndex == -1){
            throw new Exception("Book not found in cart");
        }
        this.shoppingCartItems.remove(existingBookIndex);
        calculateTotalPrice();
        return this.totalPrice;
    }

    public Order createOrder(User user) throws Exception {
        Order order = new Order(user, this.getTotalPrice(), OrderStatus.ORDER_PLACED, new Date());
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < this.getShoppingCartItems().size(); i++) {
            order.addOrderItem(this.getShoppingCartItems().get(i));
        }
        return order;
    }
}
