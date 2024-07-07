package com.kata.bookstore.controller;

import com.kata.bookstore.model.api.*;
import com.kata.bookstore.service.ShoppingCartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("getShoppingCart")
    public ResponseEntity<ShoppingCartResponse> getShoppingCart(@RequestParam int userId) {
        var result = shoppingCartService.getShoppingCart(userId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("create")
    public ResponseEntity<CreateCartResponse> createShoppingCart(@RequestBody CreateCartRequest createCartRequest) {
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return new ResponseEntity(result, CREATED);
    }

    @PostMapping("update")
    public ResponseEntity<UpdateShoppingCartResponse> updateShoppingCart(@RequestBody UpdateShoppingCartRequest updateShoppingCartRequest) {
        var result = shoppingCartService.updateBookQuantity(updateShoppingCartRequest.getCartId(), updateShoppingCartRequest.getBookId(), updateShoppingCartRequest.getQuantity(), updateShoppingCartRequest.getTotalPrice());
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return new ResponseEntity(result, CREATED);
    }
}
