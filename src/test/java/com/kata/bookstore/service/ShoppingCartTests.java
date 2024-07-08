package com.kata.bookstore.service;

import com.kata.bookstore.dao.BookRepository;
import com.kata.bookstore.dao.ShoppingCartRepository;
import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.model.Book;
import com.kata.bookstore.model.ShoppingCart;
import com.kata.bookstore.model.ShoppingCartItem;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.CreateCartRequest;
import com.kata.bookstore.model.api.ShoppingCartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class ShoppingCartTests {
    @Mock
    ShoppingCartRepository shoppingCartRepository;
    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    ShoppingCartService shoppingCartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findById() {
        ShoppingCart mockShoppingCart = new ShoppingCart();
        when(shoppingCartRepository.findByUserId(1)).thenReturn(mockShoppingCart);
        when(modelMapper.map(mockShoppingCart, ShoppingCartResponse.class)).thenReturn(new ShoppingCartResponse());
        var result = shoppingCartService.getShoppingCart(1);
        assert (result != null);
    }

    @Test
    public void createShoppingCart_shouldReturnErrorForInvalidUser() {
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setUserId(0);
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("User not found"));
    }

    @Test
    public void createShoppingCart_shouldReturnErrorForExistingCart() {
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setUserId(1);
        ShoppingCart mockShoppingCart = new ShoppingCart();
        User mockUserId = new User();

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUserId));

        when(shoppingCartRepository.findByUserId(1)).thenReturn(mockShoppingCart);
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Shopping cart already exists"));
    }

    @Test
    public void updateShoppingCart_shouldReturnErrorForInvalidBook() {
        ShoppingCart mockShoppingCart = new ShoppingCart();
        when(shoppingCartRepository.findById(1)).thenReturn(Optional.of(mockShoppingCart));

        var result = shoppingCartService.updateBookQuantity(1, 0, 0);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("book not found"));
    }

    @Test
    public void updateShoppingCart_shouldReturnErrorForInvalidCart() {
        var result = shoppingCartService.updateBookQuantity(0, 0, 0);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Cart not found"));
    }

    @Test
    public void removeCartItem_shouldReturnErrorForNonExistingBook() {
        ShoppingCart mockCart = new ShoppingCart();
        Book book = new Book();
        book.setId(1);
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setBook(book);
        mockCart.addShoppingCartItem(shoppingCartItem);// Mocking existing cart
        when(shoppingCartRepository.findById(1)).thenReturn(Optional.of(mockCart));
        when(bookRepository.findById(1)).thenReturn(Optional.empty()); // Book not found

        var result = shoppingCartService.removeCartItem(1, 1); // Remove book with id 1

        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("book not found")); // Check for error message
    }

    @Test
    public void removeCartItem_shouldReturnErrorForShoppingCartDoesNotContainBook() {
        ShoppingCart mockCart = new ShoppingCart();
        Book book = new Book();
        book.setId(1);
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setBook(book);
        mockCart.addShoppingCartItem(shoppingCartItem);// Mocking existing cart
        when(shoppingCartRepository.findById(1)).thenReturn(Optional.of(mockCart));
        when(bookRepository.findById(2)).thenReturn(Optional.of(new Book())); // Book not found in cart

        var result = shoppingCartService.removeCartItem(1, 2); // Remove book with id 2

        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("book not present in cart")); // Check for error message
    }

    @Test
    public void delete_shouldReturnErrorForNonExistingCart() {

        when(shoppingCartRepository.findById(1)).thenReturn(Optional.empty());
        var result = shoppingCartService.deleteCart(1); // Remove book with id 1

        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Cart not found")); // Check for error message
    }
}
