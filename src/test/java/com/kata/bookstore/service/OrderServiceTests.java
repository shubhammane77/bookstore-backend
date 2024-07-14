package com.kata.bookstore.service;

import com.kata.bookstore.dao.OrderRepository;
import com.kata.bookstore.dao.ShoppingCartRepository;
import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.model.Order;
import com.kata.bookstore.model.ShoppingCart;
import com.kata.bookstore.model.ShoppingCartItem;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.PlaceOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class OrderServiceTests {
    @Mock
    ShoppingCartRepository shoppingCartRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    OrderRepository orderRepository;
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

    @Test
    public void placeOrder_shouldCreateNewOrderWithPlacedItem() {
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setCartId(1);
        placeOrderRequest.setUserId(1);

        User user = new User();
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        ShoppingCart shoppingCart = new ShoppingCart(user);
        shoppingCart.addShoppingCartItem(shoppingCartItem);

        when(shoppingCartRepository.findById(1)).thenReturn(Optional.of(shoppingCart));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        var result = orderService.placeOrder(placeOrderRequest);
        assert (result.getErrorMessage() == null);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
