package com.kata.bookstore.service;

import com.kata.bookstore.dao.OrderRepository;
import com.kata.bookstore.dao.ShoppingCartRepository;
import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.exception.InvalidInputException;
import com.kata.bookstore.model.Order;
import com.kata.bookstore.model.ShoppingCart;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.PlaceOrderRequest;
import com.kata.bookstore.model.api.PlaceOrderResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    /* Places order with given cart items, deletes existing cart */
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest placeOrderRequest) {
        //
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        try {
            ShoppingCart cart = shoppingCartRepository.findById(placeOrderRequest.getCartId())
                    .orElseThrow(() -> new InvalidInputException("Cart not found"));

            User user = userRepository.findById(placeOrderRequest.getUserId())
                    .orElseThrow(() -> new InvalidInputException("User not found"));

            Order order = cart.createOrder(user);
            orderRepository.save(order);
            shoppingCartRepository.delete(cart);
            return placeOrderResponse;
        } catch (InvalidInputException ex) {
            logger.error("Exception " + ex.getMessage());
            placeOrderResponse.setErrorMessage(ex.getMessage());
            return placeOrderResponse;
        } catch (Exception ex) {
            logger.error("Exception " + ex.getMessage());
            placeOrderResponse.setErrorMessage("Error while placing order");
            return placeOrderResponse;
        }
    }
}
