package com.kata.bookstore.controller;


import com.kata.bookstore.model.api.PlaceOrderRequest;
import com.kata.bookstore.model.api.PlaceOrderResponse;
import com.kata.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("placeOrder")
    public ResponseEntity<PlaceOrderResponse> placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {
        var result = orderService.placeOrder(placeOrderRequest);
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return new ResponseEntity(result, CREATED);
    }

}