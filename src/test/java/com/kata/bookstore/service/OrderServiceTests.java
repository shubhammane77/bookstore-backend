package com.kata.bookstore.service;

import com.kata.bookstore.dao.ShoppingCartRepository;
import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.model.ShoppingCart;
import com.kata.bookstore.model.api.PlaceOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class OrderServiceTests {
    @Mock
    ShoppingCartRepository shoppingCartRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void placeOrder_shouldReturnErrorForInvalidCart() {
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setCartId(1);
        var result = orderService.placeOrder(placeOrderRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Cart not found"));
    }

    @Test
    public void placeOrder_shouldReturnErrorForInvalidUser() {
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setCartId(1);
        placeOrderRequest.setUserId(1);
        when(shoppingCartRepository.findById(1)).thenReturn(Optional.of(new ShoppingCart()));

        var result = orderService.placeOrder(placeOrderRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("User not found"));
    }
}
