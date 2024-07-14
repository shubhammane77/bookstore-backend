package com.kata.bookstore.unit;

import com.kata.bookstore.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PriceCalculationTest {
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldMatchTotalPriceCalculationWithEachCart() {
        User user = new User("test", "test","test");
        ShoppingCart shoppingCart = new ShoppingCart(user);
        Author author = new Author("Test");
        var book1 = new Book( "testTitle", author, BigDecimal.TEN, "Adventure");
        var book2 = new Book( "testTitle", author, BigDecimal.valueOf(5.25), "Adventure");

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem(shoppingCart, book1);
        shoppingCartItem.updateQuantity(5);
        ShoppingCartItem shoppingCartItem2 = new ShoppingCartItem(shoppingCart,book2);
        shoppingCartItem2.updateQuantity(2);

        shoppingCart.addShoppingCartItem(shoppingCartItem);
        shoppingCart.addShoppingCartItem(shoppingCartItem2);
        BigDecimal expectedValue = BigDecimal.valueOf(60.50);
        shoppingCart.calculateTotalPrice();
        assertTrue(expectedValue.compareTo(shoppingCart.getTotalPrice()) == 0);
    }
}
