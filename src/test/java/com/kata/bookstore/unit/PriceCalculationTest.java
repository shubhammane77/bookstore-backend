package com.kata.bookstore.unit;

import com.kata.bookstore.model.Author;
import com.kata.bookstore.model.Book;
import com.kata.bookstore.model.ShoppingCart;
import com.kata.bookstore.model.ShoppingCartItem;
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
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        Author author = new Author("Test");
        var book1 = new Book();
        book1.setAuthor(author);
        book1.setTitle("TestTitle");
        book1.setUnitPrice(BigDecimal.TEN);
        shoppingCartItem.setBook(book1);
        ;
        shoppingCartItem.setQuantity(5);
        ShoppingCartItem shoppingCartItem2 = new ShoppingCartItem();
        var book2 = new Book();
        book2.setAuthor(author);
        book2.setTitle("TestTitle2");
        book2.setUnitPrice(BigDecimal.valueOf(5.45));
        shoppingCartItem2.setBook(book2);
        shoppingCartItem2.setQuantity(10);
        List<ShoppingCartItem> shoppingCartItemList = new ArrayList<>();
        shoppingCartItemList.add(shoppingCartItem);
        shoppingCartItemList.add(shoppingCartItem2);
        shoppingCart.setShoppingCartItems(shoppingCartItemList);
        BigDecimal expectedValue = BigDecimal.valueOf(104.50);
        BigDecimal calculatedValue = shoppingCart.calculateTotalPrice();
        assertTrue(expectedValue.compareTo(calculatedValue) == 0);
    }
}
