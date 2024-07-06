package com.kata.bookstore.service;

import com.kata.bookstore.dao.BookRepository;
import com.kata.bookstore.dao.ShoppingCartRepository;
import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.model.ShoppingCart;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.CreateCartRequest;
import com.kata.bookstore.model.api.ShoppingCartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
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
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findById(){
        ShoppingCart mockShoppingCart = new ShoppingCart();
        when(shoppingCartRepository.findByUserId(1)).thenReturn(mockShoppingCart);
        when(modelMapper.map(mockShoppingCart, ShoppingCartResponse.class)).thenReturn(new ShoppingCartResponse());
        var result = shoppingCartService.getShoppingCart(1);
        assert(result != null);
    }

    @Test
    public void createShoppingCart_shouldReturnErrorForInvalidUser(){
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setUserId(0);
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        assert(result.getErrorMessage() != null && result.getErrorMessage().equals("User not found"));
    }

    @Test
    public void createShoppingCart_shouldReturnErrorForExistingCart(){
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setUserId(1);
        ShoppingCart mockShoppingCart = new ShoppingCart();
        User mockUserId = new User();

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUserId));

        when(shoppingCartRepository.findByUserId(1)).thenReturn(mockShoppingCart);
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        assert(result.getErrorMessage() != null && result.getErrorMessage().equals("Shopping cart already exists"));
    }

    @Test
    public void updateShoppingCart_shouldReturnErrorForInvalidBook(){
        ShoppingCart mockShoppingCart = new ShoppingCart();
        when(shoppingCartRepository.findById(1)).thenReturn(Optional.of(mockShoppingCart));

        var result = shoppingCartService.updateBookQuantity(1,0,0, BigDecimal.ZERO);
        assert(result.getErrorMessage() != null && result.getErrorMessage().equals("book not found"));
    }

    @Test
    public void updateShoppingCart_shouldReturnErrorForInvalidCart(){
        var result = shoppingCartService.updateBookQuantity(0,0,0,BigDecimal.ZERO);
        assert(result.getErrorMessage() != null && result.getErrorMessage().equals("Cart item not found"));
    }
}
