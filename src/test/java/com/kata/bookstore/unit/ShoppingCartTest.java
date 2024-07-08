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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartTest {

    private List<Book> bookList;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        Author author = new Author("Test");
        var book1 = new Book();
        book1.setAuthor(author);
        book1.setTitle("TestTitle");
        book1.setUnitPrice(BigDecimal.TEN);
        bookList = new ArrayList<>();
        bookList.add(book1);
    }
    @Test
    public void totalPriceCalculationVerification(){
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setBook(bookList.get(0));;
        shoppingCartItem.setQuantity(5);
        List<ShoppingCartItem> shoppingCartItemList = new ArrayList<>();
        shoppingCartItemList.add(shoppingCartItem);
        shoppingCart.setShoppingCartItems(shoppingCartItemList);
        assertEquals(BigDecimal.valueOf(50),shoppingCart.calculateTotalPrice());
    }
}
